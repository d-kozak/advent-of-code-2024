package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Day1 {
    public static void main(String[] args) throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(Day1.class.getResourceAsStream("/day1.txt")))) {
            String line;
            var left = new ArrayList<Integer>();
            var right = new ArrayList<Integer>();
            var freqRight = new HashMap<Integer, Integer>();
            while ((line = reader.readLine()) != null) {
                var parts = line.split("\\s+");
                left.add(Integer.parseInt(parts[0]));
                int x = Integer.parseInt(parts[1]);
                freqRight.put(x, freqRight.getOrDefault(x, 0) + 1);
                right.add(x);
            }
            Collections.sort(left);
            Collections.sort(right);
            var res1 = 0;
            var res2 = 0;
            for (int i = 0; i < left.size(); i++) {
                var diff = Math.abs(left.get(i) - right.get(i));
                res1 += diff;
                res2 += left.get(i) * freqRight.getOrDefault(left.get(i), 0);
            }
            System.out.println(res1);
            System.out.println(res2);
        }
    }
}