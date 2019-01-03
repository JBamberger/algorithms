package de.jbamberger.algorithms.align;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Aligner<T> {


    public Alignment<T> align(T[] a, T[] b, DistanceFunction<T> distFun, T empty) {

        final int n = a.length;
        final int m = b.length;

        // distance matrix
        final int[][] dist = new int[n + 1][m + 1];

        dist[0][0] = 0;
        for (int j = 1; j <= m; j++) {
            dist[0][j] = dist[0][j - 1] + distFun.d(empty, b[j - 1]);
        }

        for (int i = 1; i <= n; i++) {
            dist[i][0] = dist[i - 1][0] + distFun.d(a[i - 1], empty);

            for (int j = 1; j <= m; j++) {
                dist[i][j] = dist[i - 1][j - 1] + distFun.d(a[i - 1], b[j - 1]);

                if (dist[i][j - 1] + distFun.d(empty, b[j - 1]) < dist[i][j]) {
                    dist[i][j] = dist[i][j - 1] + distFun.d(empty, b[j - 1]);
                }

                if (dist[i - 1][j] + distFun.d(a[i - 1], empty) < dist[i][j]) {
                    dist[i][j] = dist[i - 1][j] + distFun.d(a[i - 1], empty);
                }
            }
        }

        int i = n;
        int j = m;
        final List<Pair<T, T>> align = new ArrayList<>();

        while (i + j > 0) {
            if (dist[i - 1][j - 1] <= dist[i][j - 1] && dist[i - 1][j - 1] <= dist[i - 1][j]) {
                align.add(new Pair<>(a[i - 1], b[j - 1]));
                i--;
                j--;
            } else if (dist[i][j - 1] <= dist[i - 1][j - 1] && dist[i][j - 1] <= dist[i - 1][j]) {
                align.add(new Pair<>(empty, b[j - 1]));
                j--;
            } else {
                align.add(new Pair<>(a[i - 1], empty));
                i--;
            }
        }
        Collections.reverse(align);
        return new Alignment<>(align, dist[n][m]);
    }

    private static final class Alignment<T> {
        public final Collection<Pair<T, T>> align;
        public final int distance;
        public final int len;

        public Alignment(Collection<Pair<T, T>> align, int distance) {
            this.align = align;
            this.distance = distance;
            this.len = align.size();
        }
    }

    private static final class Pair<L, R> {
        public final L first;
        public final R second;

        public Pair(L first, R second) {
            this.first = first;
            this.second = second;
        }
    }

    public interface DistanceFunction<T> {
        int d(T v1, T v2);
    }
}
