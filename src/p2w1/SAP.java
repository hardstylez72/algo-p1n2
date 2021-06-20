package p2w1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

// shortest ancestral path
public class SAP {
    private final Digraph digraph ;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        this.digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfdpv = new BreadthFirstDirectedPaths(this.digraph, v);
        BreadthFirstDirectedPaths bfdpw = new BreadthFirstDirectedPaths(this.digraph, w);
        return this.length(bfdpv, bfdpw);
    }

    private int length(BreadthFirstDirectedPaths a, BreadthFirstDirectedPaths b) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this.digraph.V(); i++) {
            if (a.hasPathTo(i) && b.hasPathTo(i)) {
                min = Math.min(min, a.distTo(i) + b.distTo(i));
            }
        }
        if (min == Integer.MAX_VALUE) return -1;

        return min;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(this.digraph, v);
        BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(this.digraph, w);

        return ancestor(a, b);
    }

    private int ancestor(BreadthFirstDirectedPaths a, BreadthFirstDirectedPaths b) {
        int result = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this.digraph.V(); i++) {
            if (a.hasPathTo(i) && b.hasPathTo(i)) {
                int d = a.distTo(i) + b.distTo(i);
                if (d <= min){
                    min = d;
                    result = i;
                }
            }
        }
        if (min == Integer.MAX_VALUE) return -1;
        return result;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        for (Iterator<Integer> i = v.iterator(); i.hasNext(); ) {
            if (i.next() == null) throw new IllegalArgumentException();
        }
        for (Iterator<Integer> i = w.iterator(); i.hasNext(); ) {
            if (i.next() == null) throw new IllegalArgumentException();
        }

        BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(this.digraph, v);
        BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(this.digraph, w);

        return this.length(a, b);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        for (Iterator<Integer> i = v.iterator(); i.hasNext(); ) {
            if (i.next() == null) throw new IllegalArgumentException();
        }
        for (Iterator<Integer> i = w.iterator(); i.hasNext(); ) {
            if (i.next() == null) throw new IllegalArgumentException();
        }

        BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(this.digraph, v);
        BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(this.digraph, w);

        return ancestor(a, b);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
