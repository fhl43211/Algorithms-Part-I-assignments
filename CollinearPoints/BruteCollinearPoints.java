import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] _segments;
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        double firstSlope;
        Point basePoint;
        int totalSize = pointsCopy.length;
        for (int i = 0; i < totalSize; ++i) {
            basePoint = pointsCopy[i];
            for (int j = i+1; j < totalSize; ++j) {
                firstSlope = basePoint.slopeTo(pointsCopy[j]);
                for (int k = j+1; k < totalSize; ++k) {
                    if (basePoint.slopeTo(pointsCopy[k]) != firstSlope) {
                        continue;
                    }
                    for (int l = k+1; l < totalSize; ++l) {
                        if (basePoint.slopeTo(pointsCopy[l]) != firstSlope) {
                            continue;
                        }
                        addSegment(new LineSegment(basePoint, pointsCopy[l]));
                    }
                }
            }
        }
    }

    private void addSegment(LineSegment newSeg) {
        LineSegment[] newSegs = new LineSegment[_segments.length+1];
        for (int i = 0; i < _segments.length; ++i)
        {
            newSegs[i] = _segments[i];
        }
        newSegs[_segments.length] = newSeg;
        _segments = newSegs;
    }

    // the number of line segments
    public int numberOfSegments() {
        return _segments.length;
    }

    private Point[] copyPoints(Point[] inputPoints, int newLength, int startIndex, int endIndex) {
        assert endIndex <= inputPoints.length;
        assert newLength >= endIndex;
        Point[] newCopy = new Point[newLength];
        for (int i = startIndex; i < endIndex; ++i) {
            newCopy[i] = inputPoints[i];
        }
        return newCopy;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
