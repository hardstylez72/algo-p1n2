package week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double RATIO = 1.96;
    private final int size;
    private final double sampleMean;
    private final double deviation;
    private final int trials;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if ((n <= 0) || (trials <= 0)) {
            throw new IllegalArgumentException();
        }
        size = n;
        this.trials = trials;

        double[] thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {

            Percolation percolation = new Percolation(n);
            boolean isOver = false;
            while (!isOver) {
                int row = chooseRandomRow();
                int col = chooseRandomCol();
                percolation.open(row, col);
                if (percolation.percolates()) {
                    isOver = true;
                }
            }

            thresholds[i] = percolation.numberOfOpenSites() / (double) (size * size);

        }
        // подсчет разультатов
        // среднее арифметическое
        deviation = StdStats.stddev(thresholds);
        sampleMean = StdStats.mean(thresholds);
    }

    private int chooseRandomCol() {
        int r = StdRandom.uniform(size);
        if (r < size) {
            r += 1;
        }
        return r;
    }

    private int chooseRandomRow() {
        int r = StdRandom.uniform(size);
        if (r < size) {
            r += 1;
        }
        return r;
    }

    // sample mean of percolation threshold
    public double mean() {
        return sampleMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return deviation;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - RATIO * stddev()/Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + RATIO * stddev()/Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("invalid input: " + args.length);
            return;
        }
        PercolationStats percolation = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean = " + percolation.mean());
        System.out.println("stddev = " + percolation.stddev());
        System.out.println("95% confidence interval = [" + percolation.confidenceLo() + ", " + percolation.confidenceHi() + "]");
    }

}