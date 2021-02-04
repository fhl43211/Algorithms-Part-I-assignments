import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] _segments;
    // Use extra spaces because the LineSegment does not have getter to report end points.
    private Point[] _segmentStartPoints;
    private Point[] _segmentEndPoints;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Input points must not be null");
        }
        Arrays.sort(points);
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null)
                throw new IllegalArgumentException("At least one of the input points is null");
            if (i > 0 && points[i] == points[i-1])
                throw new IllegalArgumentException("Input points must be distinct");
        }
        _segments = new LineSegment[0];
        _segmentStartPoints = new Point[0];
        _segmentEndPoints = new Point[0];
        double baseSlope;
        Point basePoint;
        int totalSize = points.length;
        Point[] copy = new Point[points.length];
        for (int i = 0; i < totalSize - 3; ++i) {
            basePoint = points[i];
            for (int copyI = i+1; copyI < points.length; ++copyI) {
                copy[copyI] = points[copyI];
            }
            Arrays.sort(copy, i+1, totalSize, basePoint.slopeOrder());
            baseSlope = basePoint.slopeTo(copy[i+1]);
            int colinearCount = 2;
            int lastLoc = i+1;
            for (int j = i+2; j < totalSize; ++j) {

                if (basePoint.slopeTo(copy[j]) == baseSlope) {
                    ++colinearCount;
                    lastLoc = j;
                }
                else {
                    if (colinearCount >= 4) {
                        addSegment(basePoint, copy[lastLoc]);
                    }
                    baseSlope = basePoint.slopeTo(copy[j]);
                    colinearCount = 2;
                }
            }
            if (colinearCount >= 4) {
                addSegment(basePoint, copy[lastLoc]);
            }
        }
    }

    private void addSegment(Point startPoint, Point endPoint) {
        if (checkColinearWithExistingSegments(startPoint, endPoint)) return;
        _segmentStartPoints = addPoint(startPoint, _segmentStartPoints);
        _segmentEndPoints = addPoint(endPoint, _segmentEndPoints);
        LineSegment newSeg = new LineSegment(startPoint, endPoint);
        LineSegment[] newSegs = new LineSegment[_segments.length+1];
        for (int i = 0; i < _segments.length; ++i)
        {
            newSegs[i] = _segments[i];
        }
        newSegs[_segments.length] = newSeg;
        _segments = newSegs;
    }

    private static Point[] addPoint(Point newPoint, Point[] vec) {
        Point[] newPoints = new Point[vec.length+1];
        for (int i = 0; i < vec.length; ++i)
        {
            newPoints[i] = vec[i];
        }
        newPoints[vec.length] = newPoint;
        return newPoints;
    }

    // Check if the input segment, identified by the start and end points, is colinear with any of the existing segments
    private boolean checkColinearWithExistingSegments(Point p, Point q) {
        assert _segmentStartPoints.length == _segmentEndPoints.length;
        for (int i = 0; i < _segmentStartPoints.length; ++i) {
            if (p.slopeTo(_segmentStartPoints[i]) == p.slopeTo(_segmentEndPoints[i]) && (p.slopeTo(_segmentStartPoints[i]) == p.slopeTo(q))) {
                return true;
            }
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return _segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return _segments;
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
