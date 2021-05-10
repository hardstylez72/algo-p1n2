package week1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int side;
    private final int size;
    private final boolean[] opened;
    private int openedAmount = 0;
    private final int rightMax;
    private final int leftMin;
    private final int topMin;
    private final int bottomMax;

    private final int topRootIndex;
    private final int bottomRootIndex;
    private final WeightedQuickUnionUF weightedQuickUnionUF;

    //    List<Integer> list = new ArrayList<Integer>();
    // creates n-by-n grid, with all sites initially blocked
    // [1,1] - top-left corner
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        side = n;
        size = n * n + 2;

        weightedQuickUnionUF = new WeightedQuickUnionUF(size);

        rightMax = side;
        bottomMax = side;
        leftMin = 1;
        topMin = 1;
        topRootIndex = 0;
        bottomRootIndex = size - 1;

        opened = new boolean[size];
        opened[topRootIndex] = true;
        opened[bottomRootIndex] = true;
    }

    private int getIndex(int row, int col) {
        return (row - 1) * side + col;
    }

    private void validateInput(int row, int col) {
        if ((row <= 0) || (row > side) || (col <= 0) || (col > side)) {
            throw new IllegalArgumentException();
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateInput(row, col);
        if (isOpen(row, col)) {
            return;
        }

        int site = getIndex(row, col);

        if (row == 1) {
            weightedQuickUnionUF.union(topRootIndex, site);
        }

        // right
        if (col < rightMax) {
            int rightSiteIndex = getIndex(row, col + 1);
            if (isOpen(row, col + 1)) {
                weightedQuickUnionUF.union(rightSiteIndex, site);
            }
        }

        // left
        if (col > leftMin) {
            int leftSiteIndex = getIndex(row, col - 1);
            if (isOpen(row, col - 1)) {
                weightedQuickUnionUF.union(leftSiteIndex, site);
            }
        }

        // top
        if (row > topMin) {
            int topSiteIndex = getIndex(row - 1, col);
            if (isOpen(row - 1, col)) {
                weightedQuickUnionUF.union(topSiteIndex, site);
            }
        }

        // bottom
        if (row < bottomMax) {
            int bottomSiteIndex = getIndex(row + 1, col);
            if (isOpen(row + 1, col)) {
                weightedQuickUnionUF.union(bottomSiteIndex, site);
            }
        }

        if (row == side) {
            weightedQuickUnionUF.union(site, bottomRootIndex);
        }
        opened[site] = true;
        openedAmount++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateInput(row, col);
        return opened[getIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateInput(row, col);
        return  weightedQuickUnionUF.find(getIndex(row, col)) == weightedQuickUnionUF.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedAmount;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.find(topRootIndex) == weightedQuickUnionUF.find(bottomRootIndex);
    }

    // test client (optional)
    public static void main(String[] args) {
        boolean full = false;
        Percolation percolation = new Percolation(3);

//        percolation.open(1, 1);
        percolation.open(3, 1);
        percolation.open(2, 1);
        percolation.open(1, 1);

//        percolation.open(2, 2);
        full = percolation.isFull(3, 1);
        if (!full) {
            return;
        }
    }
}