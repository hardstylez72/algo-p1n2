package week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Solver {
    private MinPQ<Node> minPQ ;
    private MinPQ<Node> twin ;
    private Stack<Board> stack;
    private int moves;
    private boolean isSolvable;
    private int cnt = 0;
    private int step = 0;

    private static class Node implements Comparable<Node> {
        public Board item;
        public Node prev;
        public int moves;
        public int manhattan;
        public Node() {

        }

        @Override
        public int compareTo(Node node) {
            if (item.equals(node.item)) return 0;

            if (manhattan + moves > node.item.manhattan() + node.moves) {
                return 1;
            } else {
                return -1;
            }
        }
    }
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        Node node = new Node();
        node.item = initial;
        node.prev = null;
        node.moves = 0;
        node.manhattan = initial.manhattan();

        this.minPQ = new MinPQ<>();
        this.minPQ.insert(node);


        Node uNode = new Node();
        uNode.item = initial.twin();
        uNode.prev = null;
        uNode.moves = 0;
        uNode.manhattan = initial.manhattan();

        this.twin = new MinPQ<>();
        this.twin.insert(uNode);

        this.isSolvable = false;
        this.moves = 0;
        this.solve();
    }

    private void solve() {
        Node node = null;
        Node uNode = null;
        while (true) {
            // twin
            {
                uNode = this.twin.min();
                if (uNode.item.isGoal()) {
                    this.isSolvable = false;
                    break;
                }

                this.twin.delMin();

                Iterator<Board> uIterator = uNode.item.neighbors().iterator();
                for (; uIterator.hasNext(); ) {
                    Board board = uIterator.next();

                    if (uNode.prev != null && board.equals(uNode.prev.item)) {
                        continue;
                    }

                    Node node1 = new Node();
                    node1.item = board;
                    node1.prev = uNode;
                    node1.moves = uNode.moves + 1;
                    node1.manhattan = board.manhattan();
                    this.twin.insert(node1);
                }
            }
            step++;
//            StdOut.println("================= step: " + step);

            {
                node = this.minPQ.min();
                if (node.item.isGoal()) {
                    this.isSolvable = true;
                    break;
                }
//                StdOut.println(node.item.toString());
//                StdOut.println("manhattan: " + node.manhattan);
//                StdOut.println("moves: " + node.moves);
//                StdOut.println("cnt: " + cnt++);

                this.minPQ.delMin();

                Iterator<Board> iterator = node.item.neighbors().iterator();
                for (; iterator.hasNext(); ) {
                    Board board = iterator.next();

                    if (node.prev != null && board.equals(node.prev.item)) {
                        continue;
                    }

                    Node node1 = new Node();
                    node1.item = board;
                    node1.prev = node;
                    node1.moves = node.moves + 1;
                    node1.manhattan = board.manhattan();
                    this.minPQ.insert(node1);

//                    StdOut.println(node1.item.toString());
//                    StdOut.println("manhattan: " + node1.manhattan);
//                    StdOut.println("moves: " + node1.moves);
//                    StdOut.println("cnt: " + cnt++);

                }


            }
        }

        if (!this.isSolvable) {
            return;
        }

        boolean isNodeRoot = false;
        this.stack = new Stack<>();
        this.moves = node.moves;
        while (!isNodeRoot) {
            this.stack.push(node.item);
            if (node.prev == null) {
                isNodeRoot = true;
            }
            node = node.prev;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!this.isSolvable()) {
            return -1;
        }
        return this.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.isSolvable()) {
            return null;
        }
        return this.stack;
    }

    // test client (see below)
    public static void main(String[] args) {
// create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
        }
    }

}