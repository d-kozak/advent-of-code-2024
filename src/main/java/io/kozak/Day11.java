package io.kozak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Day11 {
    public static void main(String[] args) {
        long[] testInput = {125, 17};
        long[] realInput = {28591, 78, 0, 3159881, 4254, 524155, 598, 1};
        simulate(realInput);
        memoize(realInput);
    }

    static Map<Pair<Long, Integer>, Long> dp = new HashMap<>();

    private static void memoize(long[] realInput) {
        var res = 0L;
        for (var x : realInput)
            res += go(x, 75);
        System.out.println(res);
    }

    private static long go(long x, int i) {
        if (i == 0) return 1;
        var key = new Pair<>(x, i);
        var prev = dp.get(key);
        if (prev != null) return prev;
        var res = 0L;
        var s = Long.toString(x);
        if (x == 0)
            res = go(1L, i - 1);
        else if (s.length() % 2 == 0) {
            var left = s.substring(0, s.length() / 2);
            var right = s.substring(s.length() / 2);
            res = go(Long.parseLong(left), i - 1) + go(Long.parseLong(right), i - 1);
        } else {
            res = go(x * 2024, i - 1);
        }
        dp.put(key, res);
        return res;
    }

    private static void simulate(long[] input) {
        var curr = new ArrayList<Long>();
        for (var x : input)
            curr.add(x);
        var next = new ArrayList<Long>();
//        System.out.println("Init: " + curr);
        for (int i = 1; i <= 25; i++) {
            for (var x : curr) {
                var s = x.toString();
                if (x == 0) {
                    next.add(1L);
                } else if (s.length() % 2 == 0) {
                    var left = s.substring(0, s.length() / 2);
                    var right = s.substring(s.length() / 2);
                    next.add(Long.parseLong(left));
                    next.add(Long.parseLong(right));
                } else {
                    next.add(x * 2024L);
                }
            }

            var tmp = curr;
            curr = next;
            next = tmp;
            next.clear();
//            if (i == 6 || i == 25)
//                System.out.println("Blik " + i + ":" + curr);
        }
        System.out.println(curr.size());
    }
}
