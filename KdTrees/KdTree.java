import edu.princeton.cs.algs4.Queue;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private KdNode _root;
    private int _size;
    private double _nearestDistanceSq;
    private Point2D _nearestP;

    public KdTree() {
        _size = 0;
    }

    private KdNode doInsertion(KdNode currentNode, Point2D p, boolean compareX) {
        if (currentNode == null) {
            ++_size;
            return new KdNode(p, compareX);
        }
        if (p.equals(currentNode.point))
            return currentNode;
        boolean goLeft = compareX ? p.x() < currentNode.point.x() : p.y() < currentNode.point.y();
        if (goLeft) {
            currentNode.left = doInsertion(currentNode.left, p, !compareX);
        } else {
            currentNode.right = doInsertion(currentNode.right, p, !compareX);
        }
        return currentNode;
    }

    public boolean isEmpty() {
        return _root == null;
    }

    public int size() {
        return _size;
    }

    private void checkPoint(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Cannot insert a null Point2D");
    }

    public void insert(Point2D p) {
        checkPoint(p);
        _root = doInsertion(_root, p, true);
    }

    private boolean doContain(KdNode node, Point2D p) {
        if (node == null)
            return false;
        if (node.point.equals(p))
            return true;
        boolean goLeft = node.compX ? p.x() < node.point.x() : p.y() < node.point.y();
        if (goLeft)
            return doContain(node.left, p);
        else
            return doContain(node.right, p);

    }

    private RectHV formLeftSubNodeEnclosedRect(KdNode node, RectHV enclosedRect) {
        return node.compX ? new RectHV(enclosedRect.xmin(), enclosedRect.ymin(), node.point.x(), enclosedRect.ymax())
                : new RectHV(enclosedRect.xmin(), enclosedRect.ymin(), enclosedRect.xmax(), node.point.y());
    }

    private RectHV formRightSubNodeEnclosedRect(KdNode node, RectHV enclosedRect) {
        return node.compX ? new RectHV(node.point.x(), enclosedRect.ymin(), enclosedRect.xmax(), enclosedRect.ymax())
                : new RectHV(enclosedRect.xmin(), node.point.y(), enclosedRect.xmax(), enclosedRect.ymax());
    }

    private void doFindNearest(KdNode node, Point2D p, RectHV enclosedRect) {
        if (node == null)
            return;
        if (enclosedRect.distanceSquaredTo(p) >= _nearestDistanceSq)
            return;
        double currentPointDistanceSq = node.point.distanceSquaredTo(p);
        if (currentPointDistanceSq < _nearestDistanceSq) {
            _nearestDistanceSq = currentPointDistanceSq;
            _nearestP = node.point;
        }
        boolean goLeft = node.compX ? p.x() < node.point.x() : p.y() < node.point.y();
        RectHV leftBranchRect = formLeftSubNodeEnclosedRect(node, enclosedRect);
        RectHV rightBranchRect = formRightSubNodeEnclosedRect(node, enclosedRect);
        if (goLeft) {
            doFindNearest(node.left, p, leftBranchRect);
            doFindNearest(node.right, p, rightBranchRect);
        } else {
            doFindNearest(node.right, p, rightBranchRect);
            doFindNearest(node.left, p, leftBranchRect);
        }

    }

    public Point2D nearest(Point2D p) {
        checkPoint(p);
        if (isEmpty())
            return null;
        _nearestDistanceSq = Double.POSITIVE_INFINITY;
        _nearestP = null;
        doFindNearest(_root, p, new RectHV(0, 0, 1, 1));
        return _nearestP;
    }

    public boolean contains(Point2D p) {
        checkPoint(p);
        return doContain(_root, p);
    }

    private void doRange(RectHV rect, KdNode node, Queue<Point2D> queue) {
        if (node == null)
            return;
        if (rect.contains(node.point)) {
            queue.enqueue(node.point);
        }
        boolean goLeft = node.compX ? rect.xmin() < node.point.x() : rect.ymin() < node.point.y();
        boolean goRight = node.compX ? rect.xmax() >= node.point.x() : rect.ymax() >= node.point.y();
        if (goLeft)
            doRange(rect, node.left, queue);
        if (goRight)
            doRange(rect, node.right, queue);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Cannot form range with a null RectHV");
        Queue<Point2D> queue = new Queue<Point2D>();
        doRange(rect, _root, queue);
        return queue;
    }

    private void drawSubTree(KdNode node, RectHV enclosedRect) {
        if (node == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();
        StdDraw.setPenRadius();
        if (node.compX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), enclosedRect.ymin(), node.point.x(), enclosedRect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(enclosedRect.xmin(), node.point.y(), enclosedRect.xmax(), node.point.y());
        }
        RectHV leftBranchRect = formLeftSubNodeEnclosedRect(node, enclosedRect);

        RectHV rightBranchRect = formRightSubNodeEnclosedRect(node, enclosedRect);
        drawSubTree(node.left, leftBranchRect);
        drawSubTree(node.right, rightBranchRect);
    }

    public void draw() {
        drawSubTree(_root, new RectHV(0, 0, 1, 1));
    }

    private class KdNode {
        private final Point2D point;
        private final boolean compX;
        private KdNode left;
        private KdNode right;

        private KdNode(Point2D p, boolean compareX) {
            point = p;
            compX = compareX;
            left = null;
            right = null;
        }
    }

}
