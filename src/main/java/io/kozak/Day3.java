package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day3 {

    public static void main(String[] args) throws IOException {
//        String fileName = "/test3.2.txt";
        String fileName = "/day3.txt";
        // v1
        try (var reader = new BufferedReader(new InputStreamReader(Day3.class.getResourceAsStream(fileName)))) {
            var start = System.currentTimeMillis();
            solve(reader);
            var len = System.currentTimeMillis() - start;
            System.out.println("Regex took " + len + " ms.");
        }
        //v2
        try (var reader = new BufferedReader(new InputStreamReader(Day3.class.getResourceAsStream(fileName)))) {
            var start = System.currentTimeMillis();
            solveLinearScan(reader);
            var len = System.currentTimeMillis() - start;
            System.out.println("Linear scan took " + len + " ms.");
        }
    }

    private static void solveLinearScan(BufferedReader reader) throws IOException {
        var res1 = 0;
        var res2 = 0;
        var enabled = true;
        String line;
        while ((line = reader.readLine()) != null) {
            var n = line.length();
            var curr = 0;
            while (curr < n) {
                var start = curr;
                while (curr < n && line.charAt(curr) != '(') {
                    curr++;
                }
                if (curr == n) continue;
//                System.out.println(line.substring(start, curr));
                if (match("do", start, curr, line)) {
                    curr++;
                    if (curr < n && line.charAt(curr) == ')') {
                        enabled = true;
                        curr++;
//                        System.out.println(line.substring(start, curr));
                        continue;
                    }
                    curr--;
                }
                if (match("don't", start, curr, line)) {
                    curr++;
                    if (curr < n && line.charAt(curr) == ')') {
//                        System.out.println(line.substring(start, curr));
                        enabled = false;
                    }
                    curr++;
                    continue;
                } else if (match("mul", start, curr, line)) {
                    curr++;
                    start = curr;
                    while (curr < n && Character.isDigit(line.charAt(curr))) {
                        curr++;
                    }
                    var len = curr - start;
                    var leftNum = -1;
                    if (len >= 1 && len <= 3) {
                        leftNum = 0;
                        for (int i = start; i <= curr - 1; i++)
                            leftNum = leftNum * 10 + (line.charAt(i) - '0');
                    } else {
                        continue;
                    }
                    if (curr >= n || line.charAt(curr) != ',') {
                        curr++;
                        continue;
                    }
                    curr++;
                    start = curr;
                    while (curr < n && Character.isDigit(line.charAt(curr))) {
                        curr++;
                    }
                    len = curr - start;
                    var rightNum = -1;
                    if (len >= 1 && len <= 3) {
                        rightNum = 0;
                        for (int i = start; i <= curr - 1; i++)
                            rightNum = rightNum * 10 + (line.charAt(i) - '0');
                    } else {
                        continue;
                    }
                    if (curr >= n || line.charAt(curr) != ')') {
                        curr++;
                        continue;
                    }
//                    System.out.println(leftNum + " * " + rightNum);
                    res1 += leftNum * rightNum;
                    if (enabled) {
//                        System.out.println("Enabled");
                        res2 += leftNum * rightNum;
                    }
                }
                curr++;
            }
        }
        System.out.println(res1);
        System.out.println(res2);
    }

    private static boolean match(String pattern, int from, int to, String line) {
//        String substring = line.substring(from, to);
//        System.out.println(substring);
        var len = to - from;
        if (len < pattern.length()) return false;
        for (int i = pattern.length() - 1; i >= 0; i--) {
            if (pattern.charAt(i) != line.charAt(to - 1 - (pattern.length() - 1 - i))) return false;
        }
        return true;
    }


    private static Pattern MUL_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    private static Pattern MUL_COND_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");

    private static void solve(BufferedReader reader) throws IOException {
        String line;
        var res1 = 0;
        var enabled = true;
        var res2 = 0;
        while ((line = reader.readLine()) != null) {
            var matcher = MUL_PATTERN.matcher(line);
            res1 += matcher.results().mapToInt(matchResult ->
                            Integer.parseInt(matchResult.group(1)) * Integer.parseInt(matchResult.group(2)))
                    .sum();
            matcher = MUL_COND_PATTERN.matcher(line);
            for (MatchResult matchResult : matcher.results().toList()) {
                var chunk = matchResult.group();
                if (chunk.charAt(0) == 'm') {
                    if (enabled) {
                        var mul = Integer.parseInt(matchResult.group(1)) * Integer.parseInt(matchResult.group(2));
//                        System.out.println("\t" + mul);
                        res2 += mul;
                    }
                } else if (chunk.charAt(2) == 'n') {
                    // don't
                    enabled = false;
                } else {
                    // do
                    enabled = true;
                }
            }
        }
        System.out.println(res1);
        System.out.println(res2);
    }
}
