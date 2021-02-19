import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> _set;

    public PointSET() {
        _set = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return _set.isEmpty();
    }

    public int size() {
        return _set.size();
    }

    private void checkPoint(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Cannot insert a null Point2D");
    }

    public void insert(Point2D p) {
        checkPoint(p);
        _set.add(p);
    }

    public boolean contains(Point2D p) {
        checkPoint(p);
        return _set.contains(p);
    }

    public void draw() {
        for (Point2D each : _set) {
            each.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Cannot form range with a null RectHV");
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D each : _set) {
            if (rect.contains(each))
                queue.enqueue(each);
        }
        return queue;
    }

    public Point2D nearest(Point2D p) {
        checkPoint(p);
        if (isEmpty())
            return null;
        Point2D nearestPoint = null;
        double minDistanceSquare = Double.POSITIVE_INFINITY;
        for (Point2D current : _set) {
            double currentDistanceSquare = p.distanceSquaredTo(current);
            if (currentDistanceSquare < minDistanceSquare) {
                minDistanceSquare = currentDistanceSquare;
                nearestPoint = current;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {
        // left empty
    }
}