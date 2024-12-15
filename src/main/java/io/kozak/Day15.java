package io.kozak;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Day15 {
    public static void main(String[] args) throws IOException {
        String fileName = "src/main/resources/test15-1.txt";
        fileName = "src/main/resources/test15-2.txt";
//        fileName = "src/main/resources/test15-3.txt";
        fileName = "src/main/resources/day15.txt";
        try (var reader = new BufferedReader(new FileReader(fileName))) {
            var grid = parseGrid(reader);
            var moves = parseMoves(reader);
//            part1(grid, moves);
            part2(grid, moves);
        }
    }


    private static Object WALL = new Object();

    record Block(Day14.Coord left, Day14.Coord right) {
    }

    private static void part2(char[][] grid, String moves) {
        var R = grid.length;
        var C = grid[0].length;
        var sr = -1;
        var sc = -1;
        var elems = new HashMap<Day14.Coord, Object>();
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                final Day14.Coord left = new Day14.Coord(r, 2 * c);
                final Day14.Coord right = new Day14.Coord(r, 2 * c + 1);
                switch (grid[r][c]) {
                    case '@' -> {
                        sr = r;
                        sc = 2 * c;
                    }
                    case '#' -> {
                        elems.put(left, WALL);
                        elems.put(right, WALL);
                    }
                    case 'O' -> {
                        var block = new Block(left, right);
                        elems.put(left, block);
                        elems.put(right, block);
                    }
                }
            }
        }

//        printGrid2(R, C * 2, elems, sr, sc);
        for (int i = 0; i < moves.length(); i++) {
            var move = parseMove(moves.charAt(i));
            var nr = sr + move.x;
            var nc = sc + move.y;
            if (canMove2(R, C * 2, elems, nr, nc, move)) {
                move2(R, C * 2, elems, nr, nc, move);
                sr = nr;
                sc = nc;
            }
//            printGrid2(R, C * 2, elems, sr, sc);
        }

        var score = 0L;
        for (var elem : elems.values()) {
            if (elem instanceof Block(var left, _)) {
                score += left.x * 100L + left.y;
            }
        }
        System.out.println(score / 2);
    }

    private static void printGrid2(int R, int C, HashMap<Day14.Coord, Object> elems, int sr, int sc) {
        for (var row = 0; row < R; row++) {
            for (var col = 0; col < C; col++) {
                if (row == sr && col == sc) {
                    System.out.print('@');
                } else {
                    var elem = elems.get(new Day14.Coord(row, col));
                    if (elem == WALL) {
                        System.out.print('#');
                    } else if (elem instanceof Block) {
                        System.out.print('O');
                    } else {
                        System.out.print('.');
                    }
                }
            }
            System.out.println();
        }
        System.out.println("===");
    }

    private static void move2(int R, int C, HashMap<Day14.Coord, Object> elems, int nr, int nc, Day14.Coord move) {
        if (nr < 0 || nr >= R || nc < 0 || nc >= C) {
            throw new IllegalArgumentException();
        }
        Day14.Coord coord = new Day14.Coord(nr, nc);
        var elem = elems.get(coord);
        if (elem == null) return;
        if (elem instanceof Block(var left, var right)) {
            elems.remove(left);
            elems.remove(right);
            left.x += move.x;
            left.y += move.y;
            right.x += move.x;
            right.y += move.y;
            move2(R, C, elems, left.x, left.y, move);
            move2(R, C, elems, right.x, right.y, move);
            elems.put(left, elem);
            elems.put(right, elem);
            return;
        }
        throw new IllegalArgumentException();
    }

    private static boolean canMove2(int R, int C, HashMap<Day14.Coord, Object> elems, int nr, int nc, Day14.Coord move) {
//        System.out.println(nr + " " + nc + " " + move);
        if (nr < 0 || nr >= R || nc < 0 || nc >= C) return false;
        Day14.Coord coord = new Day14.Coord(nr, nc);
        var elem = elems.get(coord);
        if (elem == null) return true;
        if (elem instanceof Block(var left, var right)) {
            elems.remove(coord);
            var leftR = left.x + move.x;
            var leftC = left.y + move.y;
            var rightR = right.x + move.x;
            var rightC = right.y + move.y;
            boolean res = canMove2(R, C, elems, leftR, leftC, move) && canMove2(R, C, elems, rightR, rightC, move);
            elems.put(coord, elem);
            return res;
        }
        // wall
        return false;
    }

    private static void part1(char[][] grid, String moves) {
//        System.out.println(Arrays.deepToString(grid));
//        System.out.println(moves);
        var R = grid.length;
        var C = grid[0].length;

        var sr = -1;
        var sc = -1;

        for (var row = 0; row < R; row++) {
            for (var col = 0; col < C; col++) {
                if (grid[row][col] == '@') {
                    sr = row;
                    sc = col;
                    grid[row][col] = '.';
                }
            }
        }


//        printGrid(grid, sr, sc);
        for (int i = 0; i < moves.length(); i++) {
            var move = parseMove(moves.charAt(i));
            var nr = sr + move.x;
            var nc = sc + move.y;
            if (tryMove(grid, nr, nc, move)) {
                sr = nr;
                sc = nc;
            }
//            printGrid(grid, sr, sc);
        }
        var score = 0L;
        for (var row = 0; row < R; row++) {
            for (var col = 0; col < C; col++) {
                if (grid[row][col] == 'O') {
                    score += row * 100L + col;
                }
            }
        }
        System.out.println(score);
    }

    private static void printGrid(char[][] grid, int sr, int sc) {
        var R = grid.length;
        var C = grid[0].length;
        for (var row = 0; row < R; row++) {
            for (var col = 0; col < C; col++) {
                if (row == sr && col == sc) {
                    System.out.print('@');
                } else {
                    System.out.print(grid[row][col]);
                }
            }
            System.out.println();
        }
        System.out.println("===");
    }

    private static boolean tryMove(char[][] grid, int nr, int nc, Day14.Coord move) {
//        System.out.println(nr + " " + nc + " " + move);
        var R = grid.length;
        var C = grid[0].length;
        if (nr < 0 || nr >= R || nc < 0 || nc >= C) return false;
        if (grid[nr][nc] == '.') return true;
        if (grid[nr][nc] == '#') return false;
        if (grid[nr][nc] == 'O') {
            var nnr = nr + move.x;
            var nnc = nc + move.y;
            if (tryMove(grid, nnr, nnc, move)) {
                grid[nnr][nnc] = 'O';
                grid[nr][nc] = '.';
                return true;
            }
            return false;
        }
        throw new IllegalStateException();
    }

    private static final Day14.Coord LEFT = new Day14.Coord(0, -1);
    private static final Day14.Coord RIGHT = new Day14.Coord(0, +1);
    private static final Day14.Coord UP = new Day14.Coord(-1, 0);
    private static final Day14.Coord DOWN = new Day14.Coord(+1, 0);

    private static Day14.Coord parseMove(char c) {
        return switch (c) {
            case '>' -> RIGHT;
            case '<' -> LEFT;
            case 'v' -> DOWN;
            case '^' -> UP;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    private static String parseMoves(BufferedReader reader) throws IOException {
        String line;
        var builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    private static char[][] parseGrid(BufferedReader reader) throws IOException {
        String line;
        var res = new ArrayList<char[]>();
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) break;
            res.add(line.toCharArray());
        }
        return res.toArray(char[][]::new);
    }
}
