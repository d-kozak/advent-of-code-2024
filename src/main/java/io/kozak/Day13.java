package io.kozak;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day13 {
    record Coord(int x, int y) {
    }

    record Machine(Coord a, Coord b, Coord prize) {
    }

    public static void main(String[] args) throws IOException {
        try (var reader = new BufferedReader(new FileReader("src/main/resources/day13.txt"))) {
            var machines = parseMachines(reader);
            System.out.println(machines);
            solve(machines);
        }
    }

    private static void solve(List<Machine> machines) {
        var res1 = 0L;
        var res2 = 0L;
        for (var machine : machines) {
            for (int usedA = 0; usedA < 100; usedA++) {
                var remX = machine.prize.x - usedA * machine.a.x;
                if (remX < 0) break;
                if (remX % machine.b.x != 0) continue;
                var usedB = remX / machine.b.x;

                if (machine.prize.y != usedA * machine.a.y + usedB * machine.b.y) continue;

                var prize = usedA * 3 + usedB;
                res1 += prize;
            }
            res2 += solve2(machine);
        }
        System.out.println(res1);
        System.out.println(res2);
    }

    private static long solve2(Machine machine) {
        long x1 = machine.a.x;
        long x2 = machine.b.x;
        long y1 = machine.a.y;
        long y2 = machine.b.y;
        var p1 = machine.prize.x + 10000000000000L;
        var p2 = machine.prize.y + 10000000000000L;
        var denom = x1 * y2 - x2 * y1;
        if (denom == 0) {
            var n1 = p1 / x1;
            var n2 = p2 / x2;
            if (n1 * x1 == p1 && n1 * y1 == p2 && 3 * n1 < n2)
                return 3 * n1;
            else if (n2 * x2 == p1 && n2 * y2 == p2)
                return n2;
        } else {
            var x = p1;
            var y = p2;
            var a = (x * y2 - x2 * y) / denom;
            var b = (x1 * y - x * y1) / denom;
            if (a * x1 + b * x2 == x && a * y1 + b * y2 == y)
                return 3 * a + b;
        }
        return 0;
    }

    private static List<Machine> parseMachines(BufferedReader reader) throws IOException {
        var res = new ArrayList<Machine>();
        String fst;
        while ((fst = reader.readLine()) != null) {
            var snd = reader.readLine();
            var thr = reader.readLine();
            res.add(new Machine(parseButton(fst), parseButton(snd), parsePrize(thr)));
            reader.readLine();
        }
        return res;
    }

    private static Coord parseButton(String input) {
        var split = input.split(", ");
        var x = Integer.parseInt(split[0].substring("Button A: X+".length()));
        var y = Integer.parseInt(split[1].substring("Y+".length()));
        return new Coord(x, y);
    }

    private static Coord parsePrize(String input) {
        var split = input.split(", ");
        var x = Integer.parseInt(split[0].substring("Prize: X=".length()));
        var y = Integer.parseInt(split[1].substring("Y=".length()));
        return new Coord(x, y);
    }
}
