package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day8 {

    public static void main(String[] args) throws IOException {
        String fileName = "/test8.txt";
        fileName = "/day8.txt";
        try (var reader = new BufferedReader(new InputStreamReader(Day8.class.getResourceAsStream(fileName)))) {
            solve(readGrid(reader));
        }
    }

    private static void solve(char[][] grid) {
        var n = grid.length;
        var m = grid[0].length;
        var antennas = new HashMap<Character, List<Pair<Integer, Integer>>>();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (grid[r][c] != '.')
                    antennas.computeIfAbsent(grid[r][c], ArrayList::new).add(new Pair<>(r, c));
            }
        }

        var antinodes1 = new boolean[n][m];
        var antinodes2 = new boolean[n][m];
        for (var entry : antennas.entrySet()) {
            var positions = entry.getValue();
            for (int i = 0; i < positions.size(); i++) {
                for (int j = 0; j < positions.size(); j++) {
                    if (i == j) continue;
                    var dr = positions.get(i).left() - positions.get(j).left();
                    var dc = positions.get(i).right() - positions.get(j).right();
                    var nr = positions.get(i).left() + dr;
                    var nc = positions.get(i).right() + dc;
                    if (nr >= 0 && nr < n && nc >= 0 && nc < m) {
                        antinodes1[nr][nc] = true;
                    }
                    antinodes2[positions.get(i).left()][positions.get(i).right()] = true;
                    while (nr >= 0 && nr < n && nc >= 0 && nc < m) {
                        antinodes2[nr][nc] = true;
                        nr += dr;
                        nc += dc;
                    }
                }
            }
        }

        var res1 = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (antinodes1[r][c]) {
                    System.out.print('#');
                    res1++;
                } else System.out.print(grid[r][c]);
            }
            System.out.println();
        }
        System.out.println(res1);

        var res2 = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (antinodes2[r][c]) {
                    System.out.print('#');
                    res2++;
                } else System.out.print(grid[r][c]);
            }
            System.out.println();
        }
        System.out.println(res2);
    }

    public static char[][] readGrid(BufferedReader reader) throws IOException {
        var lines = new ArrayList<char[]>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line.toCharArray());
        }
        return lines.toArray(char[][]::new);
    }
}
