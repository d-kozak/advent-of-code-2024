package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;

import static io.kozak.Day8.readGrid;

public class Day10 {

    public static void main(String[] args) throws IOException {
        String filename = "/test10.txt";
        filename = "/day10.txt";
        try (var reader = new BufferedReader(new InputStreamReader(Day10.class.getResourceAsStream(filename)))) {
            var grid = readGrid(reader);
            solve(grid);
        }
    }

    private static void solve(char[][] grid) {
        var res1 = 0L;
        var res2 = 0L;
        var n = grid.length;
        var m = grid[0].length;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (grid[r][c] != '0') continue;
                var score = scoreOf(grid, r, c);
                res1 += score.left();
                res2 += score.right();
            }
        }
        System.out.println(res1);
        System.out.println(res2);
    }

    private static final List<Pair<Integer, Integer>> nei = List.of(new Pair<>(-1, 0), new Pair<>(1, 0), new Pair<>(0, -1), new Pair<>(0, 1));

    private static Pair<Integer, Integer> scoreOf(char[][] grid, int sr, int sc) {
        var n = grid.length;
        var m = grid[0].length;
        var targets = new HashSet<Pair<Integer, Integer>>();
        var paths = new HashSet<String>();
        var queue = new ArrayDeque<Triple<Integer, Integer, String>>();
        queue.add(new Triple<>(sr, sc, str(sr, sc)));
        while (!queue.isEmpty()) {
            var curr = queue.removeFirst();
            var r = curr.fst();
            var c = curr.snd();
            if (grid[r][c] == '9') {
                targets.add(new Pair<>(r, c));
                paths.add(curr.thr());
                continue;
            }
            for (var d : nei) {
                var nr = r + d.left();
                var nc = c + d.right();
                if (nr >= 0 && nr < n && nc >= 0 && nc < m && grid[r][c] + 1 == grid[nr][nc]) {
                    queue.add(new Triple<>(nr, nc, curr.thr() + " " + str(nr, nc)));
                }
            }
        }
        return new Pair<>(targets.size(), paths.size());
    }

    private static String str(int sr, int sc) {
        return "[" + sr + "," + sc + "]";
    }
}
