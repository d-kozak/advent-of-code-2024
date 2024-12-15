package io.kozak;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day14 {
    static class Coord {
        int x;
        int y;

        Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Coord{" +
                   "x=" + x +
                   ", y=" + y +
                   '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Coord coord)) return false;
            return x == coord.x && y == coord.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    record Robot(Coord position, Coord velocity) {
    }


    public static void main(String[] args) throws IOException {
        String fileName = "src/main/resources/day14.txt";
//        fileName = "src/main/resources/test14.txt";
        try (var reader = new BufferedReader(new FileReader(fileName))) {
            var robots = parseRobots(reader);
            simulate(robots);
        }
    }

    private static void simulate(List<Robot> robots) {
        var C = 11;
        var R = 7;
        C = 101;
        R = 103;
        var grid = new int[R][C];
        var steps = 10403;
        for (int i = 1; i <= steps; i++) {
            for (Robot robot : robots) {
                robot.position.x = (robot.position.x + robot.velocity.x) % C;
                if (robot.position.x < 0) robot.position.x += C;
                robot.position.y = (robot.position.y + robot.velocity.y) % R;
                if (robot.position.y < 0) robot.position.y += R;
            }
            printRobots(robots, grid, R, C, i);
        }

        var quadrants = new int[5];
        for (Robot robot : robots) {
            int quadrant = getQuadrant(robot, R, C);
//            System.out.println(robot + " " + quadrant);
            quadrants[quadrant]++;
        }
        long res = quadrants[0];
        for (int i = 1; i < 4; i++)
            res *= quadrants[i];
        System.out.println(res);
    }

    private static void printRobots(List<Robot> robots, int[][] grid, int r, int c, int i) {
        for (var row : grid) {
            Arrays.fill(row, 0);
        }
        for (Robot robot : robots) {
            grid[robot.position.y][robot.position.x]++;
            if (grid[robot.position.y][robot.position.x] > 1) return;
        }
        System.out.println("===");
        System.out.println(i);
        for (var row : grid) {
            for (var x : row) {
                if (x != 0) System.out.print(x);
                else System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println("===");
    }

    private static int getQuadrant(Robot robot, int r, int c) {
        var mr = r / 2;
        var mc = c / 2;
        if (robot.position.y < mr && robot.position.x < mc) return 0;
        if (robot.position.y < mr && robot.position.x > mc) return 1;
        if (robot.position.y > mr && robot.position.x < mc) return 2;
        if (robot.position.y > mr && robot.position.x > mc) return 3;
        return 4;
    }

    private static List<Robot> parseRobots(BufferedReader reader) throws IOException {
        var robots = new ArrayList<Robot>();
        String line;
        while ((line = reader.readLine()) != null) {
            var parts = line.split(" ");
            var pos = parseCoord(parts[0]);
            var vel = parseCoord(parts[1]);
            robots.add(new Robot(pos, vel));
        }
        return robots;
    }

    private static Coord parseCoord(String input) {
        var parts = input.split(",");
        var x = Integer.parseInt(parts[0].substring("p=".length()));
        var y = Integer.parseInt(parts[1]);
        return new Coord(x, y);
    }
}
