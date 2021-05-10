package week3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] lineSegment;
    private Point[] points;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        this.points = points.clone();

        Point[] points1 = new Point[this.points.length];
        int cnt = 0;
        for (Point point : this.points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
            if (contains(points1, point)) {
                throw new IllegalArgumentException();
            }
            points1[cnt] = point;
            cnt++;
        }

        LineSegment[] ls = calculate(this.points);
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

        this.lineSegment = new LineSegment[points.length];
        int lineSegmentCounter = 0;

        for (int p = 0; p < points.length; p++) {
            for (int q = p + 1; q < points.length; q++) {
                double pq = points[p].slopeTo(points[q]);
                for (int r = q + 1; r < points.length; r++) {
                    double pr = points[p].slopeTo(points[r]);
                    if (pq != pr) {
                        continue;
                    }
                    for (int s = r + 1; s < points.length; s++) {
                        double ps = points[p].slopeTo(points[s]);

                        if ((pr == ps) && (ps == pq)) {

                            Point[] seg = new Point[4];
                            seg[0] = points[p];
                            seg[1] = points[q];
                            seg[2] = points[r];
                            seg[3] = points[s];
                            Arrays.sort(seg);

                            this.lineSegment[lineSegmentCounter++] = new LineSegment(seg[0], seg[3]);
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}