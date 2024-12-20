import io.kozak.Pair;
import io.kozak.Triple;

void main() throws IOException {
//    run("test20.txt");
    run("day20.txt");
}

int R = -1;
int C = -1;
char[][] grid;

int[][] forwardDist;
int[][] backWardDist;

int sr = -1;
int sc = -1;

int er = -1;
int ec = -1;

private void run(String fileName) throws IOException {
    grid = Files.lines(Path.of("src/main/resources", fileName)).map(String::toCharArray).toArray(char[][]::new);
    R = grid.length;
    C = grid[0].length;
    forwardDist = new int[R][C];
    backWardDist = new int[R][C];
    for (int r = 0; r < R; r++) {
        Arrays.fill(forwardDist[r], -1);
        Arrays.fill(backWardDist[r], -1);
    }
    for (int r = 0; r < R; r++) {
        for (int c = 0; c < C; c++) {
            switch (grid[r][c]) {
                case 'S' -> {
                    sr = r;
                    sc = c;
                }
                case 'E' -> {
                    er = r;
                    ec = c;
                }
            }
        }
    }

    int best = findPath(sr, sc, 'E', forwardDist);
    int back = findPath(er, ec, 'S', backWardDist);
    if (best != back)
        throw new IllegalStateException("Should be the same.");
//    System.out.println(best);

//    printGrid(forwardDist);
//    printGrid(backWardDist);

//    part1(best);

    var cheatLen = 20;
    var localQueue = new ArrayDeque<Triple<Integer, Integer, Integer>>();
    var freq = new TreeMap<Integer, Integer>();
    var seen = new HashSet<Pair<Integer, Integer>>();

    for (int r = 0; r < R; r++) {
        for (int c = 0; c < C; c++) {
            if (grid[r][c] == '#') continue;
            localQueue.add(new Triple<>(r, c, 0));
            seen.add(Pair.of(r, c));
            while (!localQueue.isEmpty()) {
                var curr = localQueue.removeFirst();
                if (curr.thr() > cheatLen) continue;
                if (grid[curr.fst()][curr.snd()] != '#') {
                    var score = forwardDist[r][c] + backWardDist[curr.fst()][curr.snd()] + curr.thr();
                    var diff = best - score;
                    if (diff > 0) {
                        freq.put(diff, freq.getOrDefault(diff, 0) + 1);
                    }
                }
                for (var d : nei) {
                    var nr = curr.fst() + d.left();
                    var nc = curr.snd() + d.right();
                    if (nr >= 0 && nr < R && nc >= 0 && nc < C && seen.add(Pair.of(nr, nc))) {
                        localQueue.add(new Triple<>(nr, nc, curr.thr() + 1));
                    }
                }
            }
            seen.clear();
        }
    }

    int res = 0;
    for (var entry : freq.entrySet()) {
//        System.out.println(entry);
        if (entry.getKey() >= 100)
            res += entry.getValue();
    }
    System.out.println(res);
}

private void part1(int best) {
    int res1 = 0;
    var freq = new TreeMap<Integer, Integer>();

    for (int r = 0; r < R; r++) {
        for (int c = 0; c < C; c++) {
            if (grid[r][c] != '#') continue;
            if (r - 1 >= 0 && grid[r - 1][c] != '#' && r + 1 < R && grid[r + 1][c] != '#') {
                int score = forwardDist[r - 1][c] + backWardDist[r + 1][c] + 2;
                if (score < best) {
                    int diff = best - score;
                    if (diff >= 100) res1++;
                    freq.put(diff, freq.getOrDefault(diff, 0) + 1);
                }
                score = forwardDist[r + 1][c] + backWardDist[r - 1][c] + 2;
                if (score < best) {
                    int diff = best - score;
                    if (diff >= 100) res1++;
                    freq.put(diff, freq.getOrDefault(diff, 0) + 1);
                }
            }
            if (c - 1 >= 0 && grid[r][c - 1] != '#' && c + 1 < C && grid[r][c + 1] != '#') {
                int score = forwardDist[r][c - 1] + backWardDist[r][c + 1] + 2;
                if (score < best) {
                    int diff = best - score;
                    if (diff >= 100) res1++;
                    freq.put(diff, freq.getOrDefault(diff, 0) + 1);
                }
                score = forwardDist[r][c + 1] + backWardDist[r][c - 1] + 2;
                if (score < best) {
                    int diff = best - score;
                    if (diff >= 100) res1++;
                    freq.put(diff, freq.getOrDefault(diff, 0) + 1);
                }
            }
        }
    }
    for (var entry : freq.entrySet()) {
        System.out.println(entry);
    }
    System.out.println(res1);
//    System.out.println("===");
}

private void printGrid(int[][] forwardDist) {
    for (int[] row : forwardDist) {
        for (int col : row) {
            print(col + " ");
        }
        println("");
    }
    println("===");
}

ArrayDeque<Triple<Integer, Integer, Integer>> queue = new ArrayDeque<>();
List<Pair<Integer, Integer>> nei = List.of(Pair.of(-1, 0), Pair.of(1, 0), Pair.of(0, -1), Pair.of(0, 1));

private int findPath(int sr, int sc, char target, int[][] dist) {
    queue.clear();
    queue.add(new Triple<>(sr, sc, 0));
    dist[sr][sc] = 0;

    var res = -1;
    while (!queue.isEmpty()) {
        var curr = queue.removeFirst();
        if (grid[curr.fst()][curr.snd()] == target) {
            res = curr.thr();
        }
        for (var n : nei) {
            var nr = curr.fst() + n.left();
            var nc = curr.snd() + n.right();
            if (nr >= 0 && nr < R && nc >= 0 && nc < C && grid[nr][nc] != '#' && dist[nr][nc] == -1) {
                dist[nr][nc] = curr.thr() + 1;
                queue.add(new Triple<>(nr, nc, curr.thr() + 1));
            }
        }
    }
    if (res == -1)
        throw new IllegalStateException("Path not found.");
    return res;
}