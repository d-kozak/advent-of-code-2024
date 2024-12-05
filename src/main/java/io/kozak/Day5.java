package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.swap;

public class Day5 {

    public static void main(String[] args) throws IOException {
//        String fileName = "/test5.txt";
        String fileName = "/day5.txt";
        try (var reader = new BufferedReader(new InputStreamReader(Day5.class.getResourceAsStream(fileName)))) {
            solve(reader);
        }
    }

    private static void solve(BufferedReader reader) throws IOException {
        var rules = parseRules(reader);
        String line;
        var res1 = 0;
        var res2 = 0;
        var seen = new HashSet<Integer>();
        var remaining = new HashSet<Integer>();
        while ((line = reader.readLine()) != null) {
            var update = Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            remaining.addAll(update);
            var correct = true;

            loop:
            for (var x : update) {
                var preds = rules.get(x);
                if (preds != null) {
                    for (var pred : preds) {
                        if (remaining.contains(pred) && !seen.contains(pred)) {
                            correct = false;

                            var fixed = fix(update, rules);
//                            System.out.println(update + " => " + fixed);
                            res2 += fixed.get(fixed.size() / 2);

                            break loop;
                        }
                    }
                }
                seen.add(x);
                remaining.remove(x);
            }
            seen.clear();
            remaining.clear();

            if (correct) {
//                System.out.println(update);
                res1 += update.get(update.size() / 2);
            }
        }
        System.out.println(res1);
        System.out.println(res2);
    }

    private static List<Integer> fix(List<Integer> update, Map<Integer, List<Integer>> rules) {
        var n = update.size();
        var res = new ArrayList<Integer>(n);
        var pos = new HashMap<Integer, Integer>();
        var seen = new HashSet<Integer>();
        for (int i = 0; i < n; i++) {
            if (pos.containsKey(update.get(i))) {
                throw new IllegalArgumentException("Duplicate key " + update.get(i));
            }
            pos.put(update.get(i), i);
            res.add(update.get(i));
        }
        for (int i = 0; i < n; ) {
            var x = res.get(i);
            var preds = rules.get(x);
            var swap = false;
            if (preds != null) {
                var j = Integer.MAX_VALUE;
                var minPred = -1;
                for (var pred : preds) {
                    if (pos.containsKey(pred) && !seen.contains(pred)) {
                        var predPos = pos.get(pred);
                        if (predPos < j) {
                            j = predPos;
                            minPred = pred;
                        }
                    }
                }
                if (minPred != -1) {
                    assert j > i;
                    pos.put(x, j);
                    pos.put(minPred, i);
                    swap(res, i, j);
                    x = minPred;
                    swap = true;
                }
            }
            if (!swap) {
                seen.add(x);
                pos.remove(x);
                i++;
            }
        }
        return res;
    }

    private static Map<Integer, List<Integer>> parseRules(BufferedReader reader) throws IOException {
        var res = new HashMap<Integer, List<Integer>>();
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            var split = line.split("\\|");
            int pred = Integer.parseInt(split[0]);
            int curr = Integer.parseInt(split[1]);
            var preds = res.computeIfAbsent(curr, ArrayList::new);
            preds.add(pred);
        }
        return res;
    }
}
