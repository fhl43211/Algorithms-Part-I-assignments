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

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] _trialResults; // Stores the all the trial results;
    private double _sqrtT; // Squre root of total trial times T

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "Both grid size " + n + " and trials " + trials + " must be greater than zero");
        }
        _trialResults = new double[trials];
        _sqrtT = java.lang.Math.sqrt(trials);
        double realSize = n * n;
        for (int i = 0; i < trials; ++i) {
            Percolation percolate = new Percolation(n);
            while (!percolate.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolate.open(row, col);
            }
            // calculate the percentage of open sites
            _trialResults[i] = 1.0 * percolate.numberOfOpenSites() / realSize;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(_trialResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(_trialResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / _sqrtT;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / _sqrtT;
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Input must be of size 2");
        }
        int size = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats testObj = new PercolationStats(size, trials);
        StdOut.println("mean                    = " + testObj.mean());
        StdOut.println("stddev                  = " + testObj.stddev());
        StdOut.println("95% confidence interval = [" + testObj.confidenceLo() + ", " + testObj.confidenceHi() + "]");
    }

}
