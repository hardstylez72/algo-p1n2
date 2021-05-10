package week3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private LineSegment[] lineSegment;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException();
        }
        Point[] points2 = points.clone();
        Point[] points1 = new Point[points2.length];
        for (Point point : points2) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
            if (contains(points1, point)) {
                throw new IllegalArgumentException();
            }
        }

        LineSegment[] ls = calculate(points2);
        int count = 0;
        for (LineSegment lineSegment : ls) {
            if (lineSegment != null) {
                count++;
            }
        }

        this.lineSegment = new LineSegment[count];
        count = 0;
        for (LineSegment lineSegment : ls) {
            if (lineSegment != null) {
                this.lineSegment[count++] = lineSegment;
            }
        }
    }

    private LineSegment[] calculate(Point[] points) {
        this.lineSegment = new LineSegment[points.length * points.length];
        int lineSegmentCounter = 0;

        for (int p = 0; p < points.length; p++) {
            Point P = points[p];
            points[p] = null;
            Comparator<Point> pointComparator = P.slopeOrder();
            int length = 0;
            int start = -1;
            Point[] pointsWithoutP = new Point[points.length - 1 - p];

            int p2c = 0;
            for (int i = 0; i < points.length; i++) {
                if (points[i] == null) {
                    continue;
                }
                pointsWithoutP[p2c] = points[i];
                p2c++;
            }

            Arrays.sort(pointsWithoutP, pointComparator);

            Double[] dd = new Double[pointsWithoutP.length];
            int ii = 0;
            for (Point point : pointsWithoutP) {
                dd[ii++] = P.slopeTo(point);
            }

            for (int q = 0; q < pointsWithoutP.length; q++) {

                if (q == 6) {
//                    StdOut.println(1);
                }
                int cmp = -1;
                if (q + 1 != pointsWithoutP.length) {
                    cmp = pointComparator.compare(pointsWithoutP[q], pointsWithoutP[q + 1]);
                }

                if (cmp == 0) {
                    if (start == -1) {
                        start = q;
                    }
                    length++;

                } else {
                    length++;
                    if (length >= 3) {
                        Point[] seg = new Point[length + 1];

                        for (int i = 0; i < length; i++) {
                            seg[i] = pointsWithoutP[i + start];
                        }
                        seg[length] = P;

                        Arrays.sort(seg);

                        this.lineSegment[lineSegmentCounter] = new LineSegment(seg[0], seg[length]);
                        lineSegmentCounter++;
                    }
                    start = -1;
                    length = 0;
                }
            }
        }
        return this.lineSegment;
    }

    private boolean contains(Point[] points, Point target) {
        if (points == null) {
            return false;
        }
        for (Point point : points) {
            if (point == null) {
                continue;
            }
            if (point.equals(target)) {
                return true;
            }
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegment.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegment;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}