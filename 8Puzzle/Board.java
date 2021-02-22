import java.util.Arrays;
import edu.princeton.cs.algs4.Stack;

public class Board {

    private final int _n;
    private final int[] _tiles;
    private final Stack<Board> _neighbours;
    private int _hamming;
    private int _manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        _n = tiles.length;
        int loc = 0;
        _tiles = new int[_n * _n];
        for (int row = 0; row < _n; ++row) {
            for (int col = 0; col < _n; ++col) {
                _tiles[loc++] = tiles[row][col];
            }
        }
        _neighbours = new Stack<Board>();
        _hamming = -1;
        _manhattan = -1;
    }

    private void addNeighbourBoard(int[][] originalTiles, int oldZeroLoc, int newZeroLoc) {
        originalTiles[oldZeroLoc / _n][oldZeroLoc % _n] = _tiles[newZeroLoc];
        originalTiles[newZeroLoc / _n][newZeroLoc % _n] = _tiles[oldZeroLoc];
        _neighbours.push(new Board(originalTiles));
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_n + "\n");
        int loc = 0;
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                sb.append(String.format("%2d ", _tiles[loc++]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return _n;
    }

    // number of tiles out of place
    public int hamming() {
        if (_hamming != -1)
            return _hamming;
        _hamming = 0;
        int totalSize = _n * _n;
        for (int i = 0; i < totalSize; ++i) {
            if ((_tiles[i] != 0) && (_tiles[i] != (i + 1) % totalSize))
                ++_hamming;
        }
        return _hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (_manhattan != -1)
            return _manhattan;
        _manhattan = 0;
        int loc = 0;
        for (int row = 0; row < _n; ++row) {
            for (int col = 0; col < _n; ++col, ++loc) {
                if (_tiles[loc] == 0)
                    continue;
                int expectRow = (_tiles[loc] - 1) / _n;
                int expectCol = (_tiles[loc] - 1) % _n;
                _manhattan += Math.abs(expectRow - row) + Math.abs(expectCol - col);
            }
        }
        return _manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        return Arrays.equals(_tiles, that._tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (_neighbours.size() == 0) {
            int[][][] tilesCopy = new int[4][_n][_n];
            int zeroLoc = -1;
            int loc = 0;
            for (int row = 0; row < _n; ++row) {
                for (int col = 0; col < _n; ++col, ++loc) {
                    if (_tiles[loc] == 0)
                        zeroLoc = loc;
                    for (int i = 0; i < 4; ++i) {
                        tilesCopy[i][row][col] = _tiles[loc];
                    }
                }
            }
            int[] possibleLoc = new int[4];
            possibleLoc[0] = zeroLoc % _n != 0 ? zeroLoc - 1 : -1;
            possibleLoc[1] = zeroLoc % _n != _n - 1 ? zeroLoc + 1 : -1;
            possibleLoc[2] = zeroLoc - _n;
            possibleLoc[3] = zeroLoc + _n;
            for (int i = 0; i < 4; ++i) {
                if (possibleLoc[i] < 0 || possibleLoc[i] >= _n * _n)
                    continue;
                addNeighbourBoard(tilesCopy[i], zeroLoc, possibleLoc[i]);
            }
        }
        return _neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int firstLoc = -1;
        int secondLoc = -1;
        int[][] twinTiles = new int[_n][_n];
        int loc = 0;
        for (int row = 0; row < _n; ++row) {
            for (int col = 0; col < _n; ++col, ++loc) {
                twinTiles[row][col] = _tiles[loc];
                if (_tiles[loc] != 0) {
                    if (firstLoc == -1)
                        firstLoc = loc;
                    else if (secondLoc == -1)
                        secondLoc = loc;
                }
            }
        }
        twinTiles[firstLoc / _n][firstLoc % _n] = _tiles[secondLoc];
        twinTiles[secondLoc / _n][secondLoc % _n] = _tiles[firstLoc];
        return new Board(twinTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // Left as blank
    }

}