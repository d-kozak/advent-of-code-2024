package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static java.lang.Math.abs;

public class Day2 {
    public static void main(String[] args) throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(Day2.class.getResourceAsStream("/day2.txt")))) {
            solve(reader);
        }
    }

    private static void solve(BufferedReader reader) throws IOException {
        String line;
        var res1 = 0;
        var res2 = 0;
        while ((line = reader.readLine()) != null) {
            int[] levels = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            if (isSafe1(levels))
                res1++;
            if (isSafe2(levels)) {
                res2++;
            }
        }
        System.out.println(res1);
        System.out.println(res2);
    }


    private static boolean isSafe1(int[] levels) {
        var inc = true;
        var dec = true;
        for (int i = 0; i < levels.length - 1; i++) {
            inc = inc && levels[i] < levels[i + 1];
            dec = dec && levels[i] > levels[i + 1];
            var diff = abs(levels[i] - levels[i + 1]);
            if (diff < 1 || diff > 3 || (!inc && !dec)) return false;
        }
        return true;
    }

    private static boolean isSafe2(int[] levels) {
        boolean inc = go(0, 1, 0, true, levels) || go(1, 2, 1, true, levels);
        boolean dec = go(0, 1, 0, false, levels) || go(1, 2, 1, false, levels);
        return inc || dec;
    }

    private static boolean go(int prev, int curr, int errCnt, boolean inc, int[] levels) {
        if (curr == levels.length) return true;
        var diff = abs(levels[prev] - levels[curr]);
        var err = diff < 1 || diff > 3 || (inc ? levels[prev] >= levels[curr] : levels[prev] <= levels[curr]);
        if (err) {
            if (errCnt == 1) return false;
            return go(prev, curr + 1, 1, inc, levels);
        }
        var res = go(curr, curr + 1, errCnt, inc, levels);
        if (errCnt == 0) {
            res = res || go(prev, curr + 1, 1, inc, levels);
        }
        return res;
    }
}
