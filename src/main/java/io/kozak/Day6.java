package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

public class Day6 {
    public static void main(String[] args) throws IOException {
        String fileName = "/test6.txt";
        fileName = "/day6.txt";
        try (var reader = new BufferedReader(new InputStreamReader(Day5.class.getResourceAsStream(fileName)))) {
            char[][] grid = readGrid(reader);
            var n = grid.length;
            var m = grid[0].length;
            var sr = -1;
            var sc = -1;
            loop:
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < m; c++) {
                    if (grid[r][c] == '^') {
                        sr = r;
                        sc = c;
                        break loop;
                    }
                }
            }
            assert sr != -1 && sc != -1;
            part1(grid, sr, sc);
            part2(grid, sr, sc);
        }
    }

    private static char[][] readGrid(BufferedReader reader) throws IOException {
        var res = new ArrayList<char[]>();
        String line;
        while ((line = reader.readLine()) != null) {
            res.add(line.toCharArray());
        }
        return res.toArray(char[][]::new);
    }

    enum Dir {
        UP(-1, -0),
        DOWN(+1, -0),
        LEFT(0, -1),
        RIGHT(0, +1);

        public final int dr;
        public final int dc;

        Dir(int dr, int dc) {
            this.dr = dr;
            this.dc = dc;
        }

        public Dir rotateRight() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }
    }

    private static void part1(char[][] grid, int sr, int sc) {
        var n = grid.length;
        var m = grid[0].length;
        var dir = Dir.UP;
        var visited = new boolean[n][m];
        var cnt = 0;
        while (sr >= 0 && sr < n && sc >= 0 && sc < m) {
            if (!visited[sr][sc]) {
                visited[sr][sc] = true;
                cnt++;
            }
            var nr = sr + dir.dr;
            var nc = sc + dir.dc;
            if (nr >= 0 && nr < n && nc >= 0 && nc < m && grid[nr][nc] == '#') {
                dir = dir.rotateRight();
            } else {
                sr = nr;
                sc = nc;
            }
        }
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                System.out.print(visited[r][c] ? 'X' : grid[r][c]);
            }
            System.out.println();
        }
        System.out.println(cnt);
    }


    private static void part2(char[][] grid, int sr, int sc) {
        var n = grid.length;
        var m = grid[0].length;
        var cnt = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (grid[r][c] == '.') {
                    grid[r][c] = '#';
                    if (hasLoop(grid, sr, sc))
                        cnt++;
                    grid[r][c] = '.';
                }
            }
        }
        System.out.println(cnt);
    }

    private static boolean hasLoop(char[][] grid, int sr, int sc) {
        var n = grid.length;
        var m = grid[0].length;
        var dir = Dir.UP;
        var seen = new HashSet<State>();
        while (sr >= 0 && sr < n && sc >= 0 && sc < m) {
            if (!seen.add(new State(sr, sc, dir))) return true;
            var nr = sr + dir.dr;
            var nc = sc + dir.dc;
            if (nr >= 0 && nr < n && nc >= 0 && nc < m && grid[nr][nc] == '#') {
                dir = dir.rotateRight();
            } else {
                sr = nr;
                sc = nc;
            }
        }
        return false;
    }

    record State(int r, int c, Dir dir) {
    }
}
