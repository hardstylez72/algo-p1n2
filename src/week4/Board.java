package week4;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Iterator;

public class Board {
    private final int[] tiles;
    private final int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }

        int height = input.length;
        int count = 0;
        this.tiles = new int[height * height];

        for (int row = 0; row < input.length; row++) {
            if (input[row].length != height) {
                throw new IllegalArgumentException();
            }
            for (int col = 0; col < input[row].length; col++) {
                this.tiles[count] = input[row][col];
                count++;
            }
        }

        this.dimension = height;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension).append("\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", tiles[i * dimension + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        int want = 0;
        for (int row = 0; row < this.dimension; row++) {
            for (int col = 0; col < this.dimension; col++) {
                want++;
                int have = this.tiles[row * dimension + col];

                if (have == 0) {
                    continue;
                }

                if (have != want) {
                    hamming++;
                }
            }
        }

        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int result = 0;
        int want = 0;

        // row = 1 col = 2 have 3
        for (int row = 0; row < this.dimension; row++) {
            for (int col = 0; col < this.dimension; col++) {

                want++;
                int have = this.tiles[row * dimension + col];

                if (have == 0) {
                    continue;
                }

                if (have != want) {
                    int wantRow = (have - 1) / (this.dimension);
                    int wantCol = (have - 1) % (this.dimension);
                    int d = Math.abs(wantRow - row) + Math.abs(wantCol - (col));
                    result += d;
                }
            }
        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int want = 0;
        for (int row = 0; row < this.dimension; row++) {

            int colLength = this.dimension;
            if (row == this.dimension - 1) {
                colLength -= 1;
            }

            for (int col = 0; col < colLength; col++) {
                want++;

                if (this.tiles[row * dimension + col] != want) {
                    return false;
                }
            }
        }

        return this.tiles[this.dimension * this.dimension - 1] == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y instanceof Board) {
            Board board = (Board) y;
            return Arrays.equals(this.tiles, board.tiles);

        } else {
            return false;
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        int row0 = 0;
        int col0 = 0;

        for (int row = 0; row < this.dimension; row++) {
            for (int col = 0; col < this.dimension; col++) {
                int have = this.tiles[row * this.dimension + col];

                if (have == 0) {
                    row0 = row;
                    col0 = col;
                }
            }
        }

        if (row0 > 0) {
            int[] m1 = this.copyTiles();
            int newRow0 = row0 - 1;
            swap(m1, row0, col0, newRow0, col0);
            stack.push(new Board(this.convArr(m1, this.dimension)));
        }

        if (row0 < this.dimension - 1) {
            int[] m1 = this.copyTiles();
            int newRow0 = row0 + 1;
            swap(m1, row0, col0, newRow0, col0);
            stack.push(new Board(this.convArr(m1, this.dimension)));
        }

        if (col0 > 0) {
            int[] m1 = this.copyTiles();
            int newCol0 = col0 - 1;
            swap(m1, row0, col0, row0, newCol0);
            stack.push(new Board(this.convArr(m1, this.dimension)));
        }

        if (col0 < this.dimension - 1) {
            int[] m1 = this.copyTiles();
            int newCol0 = col0 + 1;
            swap(m1, row0, col0, row0, newCol0);
            stack.push(new Board(this.convArr(m1, this.dimension)));
        }

        return stack;
    }

    private void swap(int[] a, int row1, int col1, int row2, int col2) {
        int t = a[row1 * this.dimension() + col1];
        a[row1 * this.dimension() + col1] = a[row2 * this.dimension() + col2];
        a[row2 * this.dimension() + col2] = t;
    }

    private int[][] convArr(int[] arr, int len) {
        int[][] m = new int[len][len];
        for (int i = 0; i < arr.length; i++) {
            int row = i / len;
            int col = i % len;
            m[row][col] = arr[i];
        }
        return m;
    }

    private int[] copyTiles() {
        return Arrays.copyOf(this.tiles, this.tiles.length);
    }

    /*
        6  5  4
        0  7  8
        3  1  2
    */
    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[] m1 = tiles.clone();



        for (int i = 0; i < m1.length; i++) {
            if (i % dimension() == 0 || m1[i] * m1[i - 1] == 0) continue;

            int row1 = i / dimension();
            int col1 = i % dimension();

            int row2 =  (i - 1) / dimension();
            int col2 = (i - 1)  % dimension();
            swap(m1, row1, col1, row2, col2);
            return new Board(this.convArr(m1, this.dimension));
        }

        return null;
    }

    private boolean compare(Board board) {
        int c = 0;
        for (int i = 0; i < board.tiles.length; i++) {
            int a = board.tiles[i];
            int b = this.tiles[i];
            if (a != b) {
                c++;
            }
        }
        return c == 2;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] m = new int[3][3];
        m[0][0] = 6;
        m[0][1] = 5;
        m[0][2] = 4;

        m[1][0] = 0;
        m[1][1] = 7;
        m[1][2] = 8;

        m[2][0] = 3;
        m[2][1] = 1;
        m[2][2] = 2;


        Board board = new Board(m);

        StdOut.println("board.toString(): " + board.toString());
        for (int i = 0; i < 2; i++) {
            if (!(board.twin().compare(board))) {
                StdOut.println(1);
            }

        }

        Board b = new Board(m);
        if (!(b.equals(board))) {
            StdOut.println(1);
        }
        b.neighbors();
        b.neighbors();
        b.isGoal();
        if (!(b.equals(board))) {
            StdOut.println(1);
        }
        if (!(b.equals(board))) {
            StdOut.println(1);
        }
        b.neighbors();
        b.neighbors();
        StdOut.println("board.twin().toString(): " + b.twin().toString());
        b.hamming();
        StdOut.println("board.twin().toString(): " + b.twin().toString());
//        StdOut.println("board.twin().toString(): " + b.twin().toString());
//        StdOut.println("board.twin().toString(): " + b.twin().toString());

        StdOut.println("board.dimension(): " + board.dimension());
        StdOut.println("board.hamming(): " + board.hamming());
        StdOut.println("board.manhattan(): " + board.manhattan());
        StdOut.println("board.toString(): " + board.toString());
        StdOut.println("board.twin().toString(): " + board.twin().toString());
        StdOut.println("board.isGoal(): " + board.isGoal());

        m[0][0] = 6;
        StdOut.println("board.twin().toString(): " + board.twin().toString());

        StdOut.println("board.neighbors()");
        Iterable iterable = board.neighbors();
        Iterator<Board> iterator = iterable.iterator();

        for (Iterator<Board> it = iterator; it.hasNext(); ) {
            Board board1 = it.next();
            StdOut.println(board1.toString());
        }
    }

}