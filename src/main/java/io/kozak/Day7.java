package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Day7 {

    public static void main(String[] args) throws IOException {
        String name = "/test7.txt";
        name = "/day7.txt";
        try (var reader = new BufferedReader(new InputStreamReader(Day7.class.getResourceAsStream(name)))) {
            solve(reader);
        }
    }

    private static void solve(BufferedReader reader) throws IOException {
        String line;
        long res = 0;
        long res2 = 0;
        while ((line = reader.readLine()) != null) {
            var parts = line.split(": ");
            var target = Long.parseLong(parts[0]);
            var operands = Arrays.stream(parts[1].split(" ")).map(Integer::parseInt).toList();
            if (canMatch1(target, operands))
                res += target;
            if (canMatch2(target, operands))
                res2 += target;
        }
        System.out.println(res);
        System.out.println(res2);
    }

    private static boolean canMatch1(long target, List<Integer> operands) {
        return dfs(operands.get(0), 1, operands, target);
    }

    private static boolean dfs(long left, int i, List<Integer> operands, long target) {
        if (i == operands.size()) {
            return left == target;
        }
        return dfs(left + operands.get(i), i + 1, operands, target) || dfs(left * operands.get(i), i + 1, operands, target);
    }

    private static boolean canMatch2(long target, List<Integer> operands) {
        return dfs2(operands.get(0), 1, operands, target);
    }

    private static boolean dfs2(long left, int i, List<Integer> operands, long target) {
        if (i == operands.size()) {
            return left == target;
        }
        return dfs2(left + operands.get(i), i + 1, operands, target)
               || dfs2(left * operands.get(i), i + 1, operands, target)
               || dfs2(concatenate(left, operands.get(i)), i + 1, operands, target);
    }

    private static long concatenate(long left, int integer) {
        return Long.parseLong(Long.toString(left) + integer);
    }

}


