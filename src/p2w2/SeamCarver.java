package p2w2;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {

    private Picture p;
    private double[][] energies;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture.width() < 0 || picture.height() < 0) throw new IllegalArgumentException();

        this.p = new Picture(picture);
        this.energies = new double[this.p.height()][this.p.width()];

        for (int x = 0; x < this.p.width(); x++) {
            for (int y = 0; y < this.p.height(); y++) {
                this.energies[y][x] = this.energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return this.p;
    }

    // width of current picture
    public int width() {
        return this.p.width();
    }

    // height of current picture
    public int height() {
        return this.p.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x == 0 || this.width() - 1 == x) return 1000;
        if (y == 0 || this.height() - 1 == y) return 1000;

        return Math.sqrt(this.squareGradientX(x, y) + this.squareGradientY(x, y));
    }

    private double squareGradientY(int x, int y) {
        Color c1 = this.p.get(x, y - 1);
        Color c2 = this.p.get(x, y + 1);

        return this.calcColors(c1, c2);
    }

    private double squareGradientX(int x, int y) {
        Color c1 = this.p.get(x - 1, y);
        Color c2 = this.p.get(x + 1, y);
        return this.calcColors(c1, c2);
    }

    private double calcColors(Color c1, Color c2) {
        double r = c2.getRed() - c1.getRed();
        double g = c2.getGreen() - c1.getGreen();
        double b = c2.getBlue() - c1.getBlue();

        return r * r + g * g + b * b;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int w = this.width();
        int h = this.height();
        double[][] localEnergies = new double[w][h];
        for (int x = 0; x < h; x++) {
            for (int y = 0; y < w; y++) {
                localEnergies[w - y - 1][h - x - 1] = this.energies[x][y];
            }
        }

        int[] seam = this.findSeam(h, w, localEnergies);
        int[] reversedSeam = new int[seam.length];
        for (int i = 0; i < seam.length; i++) {
            reversedSeam[seam.length - i - 1] = h - 1 - seam[i];
        }
        return reversedSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int w = this.width();
        int h = this.height();
        return this.findSeam(w, h, this.energies);
    }

    /* 1000 1000 1000 1000 1000 1000
     * 1000 1    2    3    4    1000
     * 1000 5    6    7    8    1000
     * 1000 9    10   11   12   1000
     * 1000 13   14   15   16   1000
     * 1000 1000 1000 1000 1000 1000
     *
     * */
    private int[] findSeam(int w, int h, double[][] localEnergies) {
        double[][] dist = new double[h][w];
        int[][] edgeTo = new int[h][w];

        // w - x
        // h - y
        // [h][w] -> [y][x]
        for (int y = 0; y < h - 1; y++) {
            for (int x = 0; x < w - 1; x++) {
                this.processPixel(x, y, w, h, localEnergies, dist, edgeTo);
            }
        }
        double min = -1;
        if (dist[h - 1].length == 1) {
            min = dist[h - 1][0];
        } else {
            min = dist[h - 1][1];
        }
        // find min dist
        int minIndex = 0;
        for (int i = 1; i < w - 1; i++) {
            double cur = dist[h - 1][i];
            if (cur <= min) {
                min = dist[h - 1][i];
                minIndex = i;
            }
        }

        // build seam
        int[] seam = new int[h];
        Integer nextX = null;
        for (int i = 0; i < h; i++) {
            if (nextX == null) {
                seam[h - i - 1] = minIndex;
                nextX = edgeTo[h - 1][minIndex];
            } else {
                seam[h - i - 1] = nextX;
                nextX = edgeTo[h - i - 1][nextX];
            }
        }
        if (seam.length > 1) {
            seam[0] = seam[1];
        }
        return seam;
    }

    private void processPixel(int x, int y, int w, int h, double[][] localEnergies, double[][] dist, int[][] edgeTo) {

        // w - x
        // h - y
        // [h][w] -> [y][x]

        // resolve point dist

        if (dist[y][x] == 0) {
            dist[y][x] = localEnergies[y][x];
            edgeTo[y][x] = x;
        }
        double d = dist[y][x];

        int yNext = y + 1;
        // left point
        if (x > 1) {
            int leftX = x - 1;
            double left = localEnergies[yNext][leftX] + d;
            double rel = dist[yNext][leftX];
            if (rel > left || rel == 0) {
                dist[yNext][leftX] = left;
                edgeTo[yNext][leftX] = x;
            }
        }

        // middle point
        double xNextMiddle = localEnergies[yNext][x] + d;
        if (dist[yNext][x] > xNextMiddle || dist[yNext][x] == 0) {
            dist[yNext][x] = xNextMiddle;
            edgeTo[yNext][x] = x;
        }

        // right point
        if (x < w - 1) {
            int xNextRight = x + 1;
            double right = localEnergies[yNext][xNextRight] + d;
            if (dist[yNext][xNextRight] > right || dist[yNext][xNextRight] == 0) {
                dist[yNext][xNextRight] = right;
                edgeTo[yNext][xNextRight] = x;
            }
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (this.width() != seam.length) throw new java.lang.IllegalArgumentException();

        for (int s : seam) {
            if (s >= this.height() || s < 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }


        Picture result = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); x++) {
            int mid = seam[x];
            for (int y = 0; y < height() - 1; y++) {
                if (y < mid) {
                    result.set(x, y, this.p.get(x, y));
                } else {
                    result.set(x, y, this.p.get(x, y + 1));
                }
            }
        }
        this.p = result;

        for (int x = 0; x < this.p.width(); x++) {
            for (int y = 0; y < this.p.height() - 1; y++) {
                this.energies[y][x] = this.energy(x, y);
            }
        }

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (this.height() != seam.length) throw new java.lang.IllegalArgumentException();

        for (int s : seam) {
            if (s >= this.width() || s < 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }


        Picture result = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            int mid = seam[y];
            for (int x = 0; x < width() - 1; x++) {
                if (x < mid) {
                    result.set(x, y, this.p.get(x, y));
                } else {
                    result.set(x, y, this.p.get(x + 1, y));
                }
            }
        }
        this.p = result;

        for (int x = 0; x < this.p.width() - 1; x++) {
            for (int y = 0; y < this.p.height(); y++) {
                this.energies[y][x] = this.energy(x, y);
            }
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
    }

}