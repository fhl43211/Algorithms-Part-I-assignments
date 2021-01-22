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

    private int _n; // size;
    private boolean[] _cellStatus; // cell status
    private int _totalOpenCells; // count number of total open cells
    private WeightedQuickUnionUF _uf;
    private int _topLoc; // Location of the virtual top node
    private int _bottomLoc; // Location of the virtual bottom node

    // calculate the location index of (row, col)
    // returns (row-1)*_n + col;
    private int calculateLoc(int row, int col) {
        if (!validRange(row) || !validRange(col)) {
            throw new IllegalArgumentException(
                    "index indexed by " + row + " or " + col + " is not between 1 and " + _n);
        }
        return (row - 1) * _n + col;
    }

    // check if the input col or row is valid
    private boolean validRange(int rc) {
        return rc >= 1 && rc <= _n;
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Size " + n + " must be greater than zero");
        _n = n;
        // totalSize = real size of blocks + 2 virtual nodes
        int totalSize = _n * _n + 2;
        _cellStatus = new boolean[totalSize];
        _totalOpenCells = 0;
        // Index 0 is refered to top virtual node and index totalSize-1 is refered to
        // down virtual node
        // Each loc index is calculated by (row-1)*_n + col;
        _uf = new WeightedQuickUnionUF(totalSize);
        _topLoc = 0;
        _bottomLoc = totalSize - 1;
        // Preset the virtual top and bottom node to be open
        _cellStatus[_topLoc] = true;
        _cellStatus[_bottomLoc] = true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        ++_totalOpenCells;
        int loc = calculateLoc(row, col);
        _cellStatus[loc] = true;
        // Union it with the neighbors
        int[] directions = { -1, 1 };
        for (int rowDir : directions) {
            int newRow = row + rowDir;
            if (validRange(newRow) && isOpen(newRow, col)) {
                _uf.union(calculateLoc(newRow, col), loc);
            }
        }
        for (int colDir : directions) {
            int newCol = col + colDir;
            if (validRange(newCol) && isOpen(row, newCol)) {
                _uf.union(calculateLoc(row, newCol), loc);
            }
        }
        // If it is the top row, connect it to the virtual top node
        if (row == 1) {
            _uf.union(_topLoc, loc);
        }
        // If it is the bottom row, connect it to the virtual bottom node
        if (row == _n) {
            _uf.union(_bottomLoc, loc);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return _cellStatus[calculateLoc(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int loc = calculateLoc(row, col);
        return _uf.find(0) == _uf.find(loc);

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return _totalOpenCells;
    }

    // does the system percolate?
    public boolean percolates() {
        return _uf.find(_topLoc) == _uf.find(_bottomLoc);
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}