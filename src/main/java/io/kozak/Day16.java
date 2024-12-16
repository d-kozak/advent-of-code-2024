import io.kozak.Day14;
import io.kozak.Pair;

import static io.kozak.Day8.readGrid;

void main() throws IOException {
    run("test16-1.txt");
    run("test16-2.txt");
    run("day16.txt");
}

private void run(String filename) throws IOException {
    try (var reader = new BufferedReader(new FileReader("src/main/resources/" + filename))) {
        var grid = readGrid(reader);
        analyzeGrid(grid);
        solve(grid);
    }
}

int R = -1;
int C = -1;

int sr = -1;
int sc = -1;

int tr = -1;
int tc = -1;

private void analyzeGrid(char[][] grid) {
    R = grid.length;
    C = grid[0].length;
    for (int r = 0; r < R; r++) {
        for (int c = 0; c < C; c++) {
            if (grid[r][c] == 'S') {
                sr = r;
                sc = c;
            } else if (grid[r][c] == 'E') {
                tr = r;
                tc = c;
            }
        }
    }
}

enum Dir {
    EAST(new Day14.Coord(0, +1)),
    WEST(new Day14.Coord(0, -1)),
    NORTH(new Day14.Coord(-1, 0)),
    SOUTH(new Day14.Coord(+1, 0));

    public final Day14.Coord dir;

    Dir(Day14.Coord dir) {
        this.dir = dir;
    }

    public Dir rotateClockWise() {
        return switch (this) {
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case NORTH -> EAST;
        };
    }

    public Dir rotateCounterClockWise() {
        return switch (this) {
            case EAST -> NORTH;
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
        };
    }


}

record State(Day14.Coord pos, Dir dir, long score, State prev) implements Comparable<State> {
    @Override
    public int compareTo(State o) {
        return Long.compare(score, o.score);
    }
}

private void solve(char[][] grid) {
    var queue = new PriorityQueue<State>();
    var seen = new HashMap<Pair<Day14.Coord, Dir>, Long>();
    queue.add(new State(new Day14.Coord(sr, sc), Dir.EAST, 0, null));
    var bestScore = -1L;
    while (!queue.isEmpty()) {
        var curr = queue.poll();
        var key = Pair.of(curr.pos, curr.dir);
        var prev = seen.get(key);
        if (prev != null && prev < curr.score) continue;
        if (prev == null) seen.put(key, curr.score);

        var nr = curr.pos.x + curr.dir.dir.x;
        var nc = curr.pos.y + curr.dir.dir.y;
        if (nr >= 0 && nr < R && nc >= 0 && nc < C && grid[nr][nc] != '#') {
            queue.add(new State(new Day14.Coord(nr, nc), curr.dir, curr.score + 1, curr));
        }
        queue.add(new State(curr.pos, curr.dir.rotateClockWise(), curr.score + 1000, curr));
        queue.add(new State(curr.pos, curr.dir.rotateCounterClockWise(), curr.score + 1000, curr));

        if (curr.pos.x == tr && curr.pos.y == tc) {
            if (bestScore == -1) {
                bestScore = curr.score;
                System.out.println(bestScore);
                markPath(grid, curr);
            } else if (bestScore == curr.score) {
                markPath(grid, curr);
            } else {
                countBest(grid);
                System.out.println("===");
                return;
            }
        }
    }
    throw new IllegalStateException("Path not found");
}

private void countBest(char[][] grid) {
    var res = 0;
    for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
            if (grid[r][c] == 'O') {
                res++;
            }
//            System.out.print(grid[r][c]);
        }
//        System.out.println();
    }
    System.out.println(res);
}

private void markPath(char[][] grid, State curr) {
    while (curr != null) {
        grid[curr.pos.x][curr.pos.y] = 'O';
        curr = curr.prev;
    }
}