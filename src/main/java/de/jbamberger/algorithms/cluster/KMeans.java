package de.jbamberger.algorithms.cluster;

import de.jbamberger.algorithms.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class KMeans {

    private final int maxIter;
    private final List<DataPoint> data;
    private final int K;
    private final DataPoint[] centroids;
    private final int[] assignment;

    public KMeans(int maxIter, List<DataPoint> data, int k) {
        this.maxIter = Preconditions.assertPositive(maxIter);
        this.data = Preconditions.assertNonNull(data);
        this.K = Preconditions.assertPositive(k);
        this.centroids = new DataPoint[K];
        this.assignment = new int[data.size()];
    }

    private void initCentroids() {
        for (int i = 0; i < K; i++) {
            centroids[i] = random();
        }
    }

    private void assignPoints() {
        for (int i = 0; i < data.size(); i++) {
            final DataPoint point = data.get(i);
            int minDist = Integer.MAX_VALUE;
            int cls = -1;
            for (int j = 0; j < K; j++) {
                final DataPoint centroid = centroids[j];
                final int newDist = centroid.distanceTo(point);
                if (newDist < minDist) {
                    minDist = newDist;
                    cls = j;
                }
            }
            assignment[i] = cls;

        }
    }

    private void computeCentroids() {

        for (int i = 0; i < K; i++) {


            centroids[i] = newCentroid;
        }
    }

    int[] cluster() {
        initCentroids();

        for (int i = 0; i < maxIter; i++) {
            assignPoints();
            computeCentroids();
        }

        return assignment.clone();
    }

}
