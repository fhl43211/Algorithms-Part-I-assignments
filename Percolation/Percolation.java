/**
 * MIT License
 * 
 * Copyright (c) 2021 Hongliang Fan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int m_n; // size;
    private final boolean[] m_cellStatus; // cell status
    private int m_totalCounts; // count number of total open cells
    private final WeightedQuickUnionUF m_percolates; // the union for determination of percolation
    private final WeightedQuickUnionUF m_full; // the union to determine isFull?
    private final int m_topLoc; // Location of the virtual top node
    private final int m_bottomLoc; // Location of the virtual bottom node

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Size " + n + " must be greater than zero");
        m_n = n;
        // totalSize = real size of blocks + 2 virtual nodes
        int totalSize = m_n * m_n + 2;
        m_cellStatus = new boolean[totalSize];
        m_totalCounts = 0;
        // Index 0 is refered to top virtual node and index totalSize-1 is refered to
        // down virtual node
        // Each loc index is calculated by (row-1)*m_n + col;
        m_percolates = new WeightedQuickUnionUF(totalSize);
        m_full = new WeightedQuickUnionUF(totalSize - 1); // Full cannot be achieved by a single path from the virtual bottom node
        m_topLoc = 0;
        m_bottomLoc = totalSize - 1;
        // Preset the virtual top and bottom node to be open
        m_cellStatus[m_topLoc] = true;
        m_cellStatus[m_bottomLoc] = true;
    }

    // calculate the location index of (row, col)
    // returns (row-1)*m_n + col;
    private int calculateLoc(int row, int col) {
        if (!validRange(row) || !validRange(col)) {
            throw new IllegalArgumentException(
                    "index indexed by " + row + " or " + col + " is not between 1 and " + m_n);
        }
        return (row - 1) * m_n + col;
    }

    // check if the input col or row is valid
    private boolean validRange(int rc) {
        return rc >= 1 && rc <= m_n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        ++m_totalCounts;
        int loc = calculateLoc(row, col);
        m_cellStatus[loc] = true;
        // Union it with the neighbors
        int[] directions = { -1, 1 };
        for (int rowDir : directions) {
            int newRow = row + rowDir;
            if (validRange(newRow) && isOpen(newRow, col)) {
                m_percolates.union(calculateLoc(newRow, col), loc);
                m_full.union(calculateLoc(newRow, col), loc);
            }
        }
        for (int colDir : directions) {
            int newCol = col + colDir;
            if (validRange(newCol) && isOpen(row, newCol)) {
                m_percolates.union(calculateLoc(row, newCol), loc);
                m_full.union(calculateLoc(row, newCol), loc);
            }
        }
        // If it is the top row, connect it to the virtual top node
        if (row == 1) {
            m_percolates.union(m_topLoc, loc);
            m_full.union(m_topLoc, loc);
        }
        // If it is the bottom row and it is full, connect it to the virtual bottom node
        if (row == m_n) {
            m_percolates.union(m_bottomLoc, loc);
            // m_full will not union bottom loc with loc to avoid a single path from the bottom loc
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return m_cellStatus[calculateLoc(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int loc = calculateLoc(row, col);
        // Because there is no such thing as virtual bottom loc in m_full
        // if m_full and virtual top node has shared parent, then it is full
        return m_full.find(m_topLoc) == m_full.find(loc);

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return m_totalCounts;
    }

    // does the system percolate?
    public boolean percolates() {
        return m_percolates.find(m_topLoc) == m_percolates.find(m_bottomLoc);
    }

    // test client (optional)
    public static void main(String[] args) {
        // Left as blank
    }
}