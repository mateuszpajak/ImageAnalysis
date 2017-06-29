import Jama.Matrix;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Ransac {

    private ArrayList<Pair> pairs;
    private int iter;
    private double maxError;
    private double r;
    private double R;

    public Ransac(int iter, double maxError, double r, double R, ArrayList<Pair> pairs) {
        this.iter = iter;
        this.maxError = maxError;
        this.r = r;
        this.R = R;
        this.pairs = pairs;
    }

    public ArrayList<Pair> getMatchingPairs(Matrix model) {
        ArrayList<Pair> result = new ArrayList<>();

        pairs.forEach(pair -> {
            if (model != null && modelError(model, pair) < maxError) {
                result.add(pair);
            }
        });
        return result;
    }

    public Matrix ransacAlgorithm(boolean affine) {

        Matrix bestModel = null;
        int bestScore = 0;

        for (int i = 0; i < iter; i++) {
            Matrix model = null;

            while (model == null) {
                if (affine) {
                    ArrayList<Pair> samples = chooseSamples(pairs, 3);
                    model = calculateModelAffine(samples);
                } else {
                    ArrayList<Pair> samples = chooseSamples(pairs, 4);
                    model = calculateModelPerspective(samples);
                }
            }
            int score = 0;
            for (int j = 0; j < pairs.size(); j++) {
                double error = modelError(model, pairs.get(j));
                if (error < maxError) {
                    score++;
                }
            }
            if (score > bestScore) {
                bestScore = score;
                bestModel = model;
            }
        }
        return bestModel;
    }

    private ArrayList<Pair> chooseSamples(ArrayList<Pair> pairs, int n) {
        ArrayList<Pair> samples = new ArrayList<>();

        do {
            samples.add(pairs.get(ThreadLocalRandom.current().nextInt(0, pairs.size())));
            if (checkConstraints(samples)) {
                n--;
            } else {
                samples.remove(samples.size() - 1);
            }
        } while (n > 0);

        return samples;
    }

    private boolean checkConstraints(ArrayList<Pair> samples) {
        boolean[] result = {true};
        samples.forEach(pair ->
                samples.forEach(inner -> {
                    if (samples.indexOf(inner) > samples.indexOf(pair) && result[0]) {
                        result[0] = checkCondition(pair.getKeyPointA(), inner.getKeyPointA()) && checkCondition(pair.getKeyPointB(), inner.getKeyPointB());
                    }
                })
        );
        return result[0];
    }

    private boolean checkCondition(KeyPoint left, KeyPoint right) {
        double tmp = Math.pow(left.getCoordinateX() - right.getCoordinateX(), 2)
                + Math.pow(left.getCoordinateY() - right.getCoordinateY(), 2);

        return Math.pow(r, 2) < tmp && tmp < Math.pow(R, 2);
    }

    private Matrix calculateModelAffine(ArrayList<Pair> samples) {

        double[][] matrixArray = {{samples.get(0).getKeyPointA().getCoordinateX(), samples.get(0).getKeyPointA().getCoordinateY(), 1, 0, 0, 0},
                {samples.get(1).getKeyPointA().getCoordinateX(), samples.get(1).getKeyPointA().getCoordinateY(), 1, 0, 0, 0},
                {samples.get(2).getKeyPointA().getCoordinateX(), samples.get(2).getKeyPointA().getCoordinateY(), 1, 0, 0, 0},
                {0, 0, 0, samples.get(0).getKeyPointA().getCoordinateX(), samples.get(0).getKeyPointA().getCoordinateY(), 1},
                {0, 0, 0, samples.get(1).getKeyPointA().getCoordinateX(), samples.get(1).getKeyPointA().getCoordinateY(), 1},
                {0, 0, 0, samples.get(2).getKeyPointA().getCoordinateX(), samples.get(2).getKeyPointA().getCoordinateY(), 1}};

        double[][] vectorArray = {{samples.get(0).getKeyPointB().getCoordinateX()},
                {samples.get(1).getKeyPointB().getCoordinateX()},
                {samples.get(2).getKeyPointB().getCoordinateX()},
                {samples.get(0).getKeyPointB().getCoordinateY()},
                {samples.get(1).getKeyPointB().getCoordinateY()},
                {samples.get(2).getKeyPointB().getCoordinateY()}};

        Matrix matrix = new Matrix(matrixArray);
        Matrix vector = new Matrix(vectorArray);

        if (matrix.det() > 0) {
            Matrix result = (matrix.inverse()).times(vector);

            double[][] resultArray = {{result.get(0, 0), result.get(1, 0), result.get(2, 0)},
                    {result.get(3, 0), result.get(4, 0), result.get(5, 0)},
                    {0, 0, 1}};

            return new Matrix(resultArray);
        } else {
            return null;
        }
    }

    private Matrix calculateModelPerspective(ArrayList<Pair> samples) {

        double[][] matrixArray = {{samples.get(0).getKeyPointA().getCoordinateX(), samples.get(0).getKeyPointA().getCoordinateY(), 1, 0, 0, 0,
                -(samples.get(0).getKeyPointB().getCoordinateX() * samples.get(0).getKeyPointA().getCoordinateX()),
                -(samples.get(0).getKeyPointB().getCoordinateX() * samples.get(0).getKeyPointA().getCoordinateY())},

                {samples.get(1).getKeyPointA().getCoordinateX(), samples.get(1).getKeyPointA().getCoordinateY(), 1, 0, 0, 0,
                        -(samples.get(1).getKeyPointB().getCoordinateX() * samples.get(1).getKeyPointA().getCoordinateX()),
                        -(samples.get(1).getKeyPointB().getCoordinateX() * samples.get(1).getKeyPointA().getCoordinateY())
                },

                {samples.get(2).getKeyPointA().getCoordinateX(), samples.get(2).getKeyPointA().getCoordinateY(), 1, 0, 0, 0,
                        -(samples.get(2).getKeyPointB().getCoordinateX() * samples.get(2).getKeyPointA().getCoordinateX()),
                        -(samples.get(2).getKeyPointB().getCoordinateX() * samples.get(2).getKeyPointA().getCoordinateY())
                },

                {samples.get(3).getKeyPointA().getCoordinateX(), samples.get(3).getKeyPointA().getCoordinateY(), 1, 0, 0, 0,
                        -(samples.get(3).getKeyPointB().getCoordinateX() * samples.get(3).getKeyPointA().getCoordinateX()),
                        -(samples.get(3).getKeyPointB().getCoordinateX() * samples.get(3).getKeyPointA().getCoordinateY())
                },

                {0, 0, 0, samples.get(0).getKeyPointA().getCoordinateX(), samples.get(0).getKeyPointA().getCoordinateY(), 1,
                        -(samples.get(0).getKeyPointB().getCoordinateY() * samples.get(0).getKeyPointA().getCoordinateX()),
                        -(samples.get(0).getKeyPointB().getCoordinateY() * samples.get(0).getKeyPointA().getCoordinateY())
                },

                {0, 0, 0, samples.get(1).getKeyPointA().getCoordinateX(), samples.get(1).getKeyPointA().getCoordinateY(), 1,
                        -(samples.get(1).getKeyPointB().getCoordinateY() * samples.get(1).getKeyPointA().getCoordinateX()),
                        -(samples.get(1).getKeyPointB().getCoordinateY() * samples.get(1).getKeyPointA().getCoordinateY())
                },

                {0, 0, 0, samples.get(2).getKeyPointA().getCoordinateX(), samples.get(2).getKeyPointA().getCoordinateY(), 1,
                        -(samples.get(2).getKeyPointB().getCoordinateY() * samples.get(2).getKeyPointA().getCoordinateX()),
                        -(samples.get(2).getKeyPointB().getCoordinateY() * samples.get(2).getKeyPointA().getCoordinateY())
                },

                {0, 0, 0, samples.get(3).getKeyPointA().getCoordinateX(), samples.get(3).getKeyPointA().getCoordinateY(), 1,
                        -(samples.get(3).getKeyPointB().getCoordinateY() * samples.get(3).getKeyPointA().getCoordinateX()),
                        -(samples.get(3).getKeyPointB().getCoordinateY() * samples.get(3).getKeyPointA().getCoordinateY())
                }
        };

        double[][] vectorArray = {{samples.get(0).getKeyPointB().getCoordinateX()},
                {samples.get(1).getKeyPointB().getCoordinateX()},
                {samples.get(2).getKeyPointB().getCoordinateX()},
                {samples.get(3).getKeyPointB().getCoordinateX()},
                {samples.get(0).getKeyPointB().getCoordinateY()},
                {samples.get(1).getKeyPointB().getCoordinateY()},
                {samples.get(2).getKeyPointB().getCoordinateY()},
                {samples.get(3).getKeyPointB().getCoordinateY()}};

        Matrix matrix = new Matrix(matrixArray);
        Matrix vector = new Matrix(vectorArray);

        if (matrix.det() > 0) {
            Matrix result = (matrix.inverse()).times(vector);
            double[][] resultArray = {{result.get(0, 0), result.get(1, 0), result.get(2, 0)},
                    {result.get(3, 0), result.get(4, 0), result.get(5, 0)},
                    {result.get(6, 0), result.get(7, 0), 1}};

            return new Matrix(resultArray);
        } else {
            return null;
        }
    }

    private double modelError(Matrix model, Pair pair) {
        KeyPoint keyPointA = pair.getKeyPointA();
        KeyPoint keyPointB = pair.getKeyPointB();

        double x1 = model.get(0, 0) * keyPointA.getCoordinateX() + model.get(0, 1) * keyPointA.getCoordinateY() + model.get(0, 2);
        double y1 = model.get(1, 0) * keyPointA.getCoordinateX() + model.get(1, 1) * keyPointA.getCoordinateY() + model.get(1, 2);
        double x2 = model.get(0, 0) * keyPointB.getCoordinateX() + model.get(0, 1) * keyPointB.getCoordinateY() + model.get(0, 2);
        double y2 = model.get(1, 0) * keyPointB.getCoordinateX() + model.get(1, 1) * keyPointB.getCoordinateY() + model.get(1, 2);

        return Math.sqrt(Math.pow(x1 - keyPointA.getCoordinateX(), 2) + Math.pow(y1 - keyPointA.getCoordinateY(), 2)) +
                Math.sqrt(Math.pow(x2 - keyPointB.getCoordinateX(), 2) + Math.pow(y2 - keyPointB.getCoordinateY(), 2));
    }
}
