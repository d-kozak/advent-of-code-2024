package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Day4 {

    public static void main(String[] args) throws IOException {
        String inputFile = "/day4.txt";
        try (var reader = new BufferedReader(new InputStreamReader(Day4.class.getResourceAsStream(inputFile)))) {
            ArrayList<String> grid = parseGrid(reader);
            solve(grid);
            solve2(grid);
        }
    }

    private static void solve(ArrayList<String> grid) throws IOException {
        var n = grid.size();
        var m = grid.get(0).length();
        var nei = List.of(d(-1, -1), d(-1, 0), d(-1, 1), d(0, -1), d(0, 1), d(1, -1), d(1, 0), d(1, 1));
        var pattern = "XMAS";
        var res1 = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                for (var d : nei) {
                    var dr = d[0];
                    var dc = d[1];

                    var cr = r;
                    var cc = c;
                    var match = true;
                    for (int i = 0; i < pattern.length(); i++) {
                        if (cr < 0 || cr >= n || cc < 0 || cc >= m || grid.get(cr).charAt(cc) != pattern.charAt(i)) {
                            match = false;
                            break;
                        }
                        cr += dr;
                        cc += dc;
                    }
                    if (match) {
                        res1++;
                    }
                }
            }
        }
        System.out.println(res1);
    }

    private static ArrayList<String> parseGrid(BufferedReader reader) throws IOException {
        var grid = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
            grid.add(line);
        }
        return grid;
    }

    private static int[] d(int dr, int dc) {
        return new int[]{dr, dc};
    }

    private static void solve2(ArrayList<String> grid) {
        var n = grid.size();
        var m = grid.get(0).length();
        var pattern = "MAS";
        var res2 = 0;
        for (int r = 1; r < n - 1; r++) {
            for (int c = 1; c < m - 1; c++) {
                var forwardL = true;
                var backwardL = true;
                var forwardR = true;
                var backwardR = true;
                var d = -1;
                for (int i = 0; i < pattern.length(); i++) {
                    if (grid.get(r + d).charAt(c + d) != pattern.charAt(i)) {
                        forwardL = false;
                    }
                    if (grid.get(r + d).charAt(c + d) != pattern.charAt(pattern.length() - 1 - i)) {
                        backwardL = false;
                    }
                    var md = d * -1;
                    if (grid.get(r + d).charAt(c + md) != pattern.charAt(i)) {
                        forwardR = false;
                    }
                    if (grid.get(r + d).charAt(c + md) != pattern.charAt(pattern.length() - 1 - i)) {
                        backwardR = false;
                    }
                    d++;
                }
                if ((forwardL || backwardL) && (forwardR || backwardR)) {
//                    for (int rr = r - 1; rr <= r + 1; rr++) {
//                        for (int cc = c - 1; cc <= c + 1; cc++) {
//                            System.out.print(grid.get(rr).charAt(cc));
//                        }
//                        System.out.println();
//                    }
//                    System.out.println(forwardL);
//                    System.out.println(backwardL);
//                    System.out.println(forwardR);
//                    System.out.println(backwardR);
//                    System.out.println("===");
                    res2++;
                }
            }
        }
        System.out.println(res2);
    }
}
