import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int _steps;
    private Stack<Board> _solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Cannot build a solver with null board");
        }
        _steps = -1;
        _solution = null;
        if (initial.isGoal()) {
            _steps = 0;
            _solution = new Stack<Board>();
            _solution.push(initial);
            return;
        }
        MinPQ<SolverStep> priorityQueue = new MinPQ<SolverStep>();
        MinPQ<SolverStep> priorityQueueTwin = new MinPQ<SolverStep>();
        priorityQueue.insert(new SolverStep(initial, 0, null));
        priorityQueueTwin.insert(new SolverStep(initial.twin(), 0, null));
        SolverStep finalStep = null;
        Boolean twinSolvable = false;
        while (!priorityQueue.isEmpty() && !priorityQueueTwin.isEmpty()) {
            if (_steps != -1 || twinSolvable) {
                break;
            }
            SolverStep topItem = priorityQueue.delMin();
            SolverStep topItemTwin = priorityQueueTwin.delMin();
            Board currentBoard = topItem.board();
            Board currentBoardTwin = topItemTwin.board();
            Board prevBoard = topItem.prevStep() == null ? null : topItem.prevStep().board();
            Board prevBoardTwin = topItemTwin.prevStep() == null ? null : topItemTwin.prevStep().board();
            int step = topItem.step();
            int stepTwin = topItemTwin.step();
            for (Board n : currentBoard.neighbors()) {
                if (n.equals(prevBoard))
                    continue;
                SolverStep nextStep = new SolverStep(n, step + 1, topItem);
                if (n.isGoal()) {
                    _steps = nextStep.step();
                    finalStep = nextStep;
                    break;
                }
                priorityQueue.insert(nextStep);
            }
            if (_steps != -1)
                break;
            for (Board n : currentBoardTwin.neighbors()) {
                if (n.equals(prevBoardTwin))
                    continue;
                if (n.isGoal()) {
                    twinSolvable = true;
                    finalStep = null;
                    break;
                }
                priorityQueueTwin.insert(new SolverStep(n, stepTwin + 1, topItemTwin));
            }
        }
        if (isSolvable()) {
            _solution = new Stack<Board>();
            _solution.push(finalStep.board());
            SolverStep prev = finalStep.prevStep();
            while (prev != null) {
                _solution.push(prev.board());
                prev = prev.prevStep();
            }
        }
    }

    private class SolverStep implements Comparable<SolverStep> {
        private final Board currentBoard;
        private final int manhattan;
        private final int step;
        private final SolverStep prevStep;

        public SolverStep(Board thisBoard, int s, SolverStep prev) {
            currentBoard = thisBoard;
            manhattan = thisBoard.manhattan();
            step = s;
            prevStep = prev;
        }

        public int compareTo(SolverStep o) {
            int thisPriority = this.manhattan() + this.step();
            int thatPriority = o.manhattan() + o.step();
            if (thisPriority == thatPriority) {
                if (this.manhattan() < o.manhattan())
                    return -1;
                else if (this.manhattan() > o.manhattan())
                    return 1;
                else
                    return 0;
            } else if (thisPriority < thatPriority)
                return -1;
            else
                return 1;
        }

        public Board board() {
            return currentBoard;
        }

        public int step() {
            return step;
        }

        public SolverStep prevStep() {
            return prevStep;
        }

        public int manhattan() {
            return manhattan;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return _steps != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return _steps;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return _solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
