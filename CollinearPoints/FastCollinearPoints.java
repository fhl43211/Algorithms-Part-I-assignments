import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] _segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Input points must not be null");
        }
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null)
                throw new IllegalArgumentException("At least one of the input points is null");
        }

        Point[] pointsCopy = copyPoints(points, points.length, 0, points.length);
        Arrays.sort(pointsCopy); // O(NlogN)
        for (int i = 1; i < pointsCopy.length; ++i) {
            if (pointsCopy[i].compareTo(pointsCopy[i-1]) == 0)
                throw new IllegalArgumentException("Input points must be distinct");
        }
        _segments = new LineSegment[0];
        double baseSlope;
        Point basePoint;
        int totalSize = pointsCopy.length;
        Point[] slopeOrderPoints = new Point[pointsCopy.length];
        for (int i = 0; i < totalSize - 3; ++i) {
            basePoint = pointsCopy[i];
            for (int copyI = 0; copyI < pointsCopy.length; ++copyI) {
                slopeOrderPoints[copyI] = pointsCopy[copyI]; // O(N^2)
            }
            Arrays.sort(slopeOrderPoints, 0, totalSize, basePoint.slopeOrder()); // O(N^2logN)
            baseSlope = basePoint.slopeTo(slopeOrderPoints[1]);
            int startIndex = 1;
            int endIndex = 2;
            boolean skip = basePoint.compareTo(slopeOrderPoints[1]) > 0;
            for (int j = 2; j < totalSize; ++j) {
                if (basePoint.slopeTo(slopeOrderPoints[j]) == baseSlope) {
                    if (basePoint.compareTo(slopeOrderPoints[j]) > 0)
                        skip = true;
                    ++endIndex;
                }
                else {
                    if (endIndex - startIndex >= 3 && !skip) {
                        addSegment(basePoint, slopeOrderPoints[endIndex-1]);
                    }
                    skip = basePoint.compareTo(slopeOrderPoints[j]) > 0;
                    startIndex = j;
                    endIndex = j+1;
                    baseSlope = basePoint.slopeTo(slopeOrderPoints[j]);
                }
            }
            if (endIndex - startIndex >= 3 && !skip) {
                addSegment(basePoint, slopeOrderPoints[endIndex-1]);
            }
        }
    }

    private void addSegment(Point startPoint, Point endPoint) {
        LineSegment newSeg = new LineSegment(startPoint, endPoint);
        LineSegment[] newSegs = new LineSegment[_segments.length+1];
        for (int i = 0; i < _segments.length; ++i)
        {
            newSegs[i] = _segments[i];
        }
        newSegs[_segments.length] = newSeg;
        _segments = newSegs;
    }

    private static Point[] copyPoints(Point[] inputPoints, int newLength, int startIndex, int endIndex) {
        assert endIndex <= inputPoints.length;
        assert newLength >= endIndex;
        Point[] newCopy = new Point[newLength];
        for (int i = startIndex; i < endIndex; ++i) {
            newCopy[i] = inputPoints[i];
        }
        return newCopy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return _segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] copy = new LineSegment[_segments.length];
        for (int i = 0; i < _segments.length; ++i)
        {
            copy[i] = _segments[i];
        }
        return copy;
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
