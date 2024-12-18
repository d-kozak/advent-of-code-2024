import io.kozak.Pair;
import io.kozak.Triple;


final int SIZE = 71;

final int R = SIZE;
final int C = SIZE;

final int MOVES = 1024;

char[][] grid = new char[R][C];

List<Pair<Integer, Integer>> nei = List.of(Pair.of(-1, 0), Pair.of(1, 0), Pair.of(0, -1), Pair.of(0, 1));

void main() throws IOException {
    String filename = "src/main/resources/day18.txt";
//    filename = "src/main/resources/test18.txt";
    var bytes = Files.lines(Path.of(filename))
            .map(line -> line.split(","))
            .map(split -> Pair.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]))
            ).toList();
    for (int i = 0; i < MOVES; i++) {
        var b = bytes.get(i);
        int row = b.right();
        int col = b.left();
        grid[row][col] = '#';
    }

    search(true);


    for (var b : bytes) {
        int row = b.right();
        int col = b.left();
        grid[row][col] = '#';
        if (!search(false)) {
            System.out.println(col + "," + row);
            return;
        }
    }

    throw new IllegalStateException();
}

private boolean search(boolean part1) {
    boolean[][] seen = new boolean[R][C];
    var queue = new ArrayDeque<Triple<Integer, Integer, Integer>>();
    queue.add(new Triple<>(0, 0, 0));
    seen[0][0] = true;
    while (!queue.isEmpty()) {
        var curr = queue.removeFirst();
        int row = curr.snd();
        int col = curr.fst();

        if (row == R - 1 && col == C - 1) {
            if (part1) {
                System.out.println(curr.thr());
            }
            return true;
        }

        for (var n : nei) {
            var nr = row + n.right();
            var nc = col + n.left();
            if (nr >= 0 && nr < R && nc >= 0 && nc < C && grid[nr][nc] != '#' && !seen[nr][nc]) {
                seen[nr][nc] = true;
                queue.add(new Triple<>(nc, nr, curr.thr() + 1));
            }
        }
    }
    return false;
}