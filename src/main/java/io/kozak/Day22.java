import io.kozak.Pair;

import static java.lang.Math.max;

void main() throws IOException {
    var input = 123L;
    for (var i = 0; i < 10; i++) {
        input = next(input);
        System.out.println(input);
    }

    var inputList = List.of(1L, 10L, 100L, 2024L);
    List<Long> realInput = Files.lines(Paths.get("src/main/resources/day22.txt")).map(Long::parseLong).toList();
    inputList = realInput;
    var sum = 0L;
    for (var x : inputList) {
        var res = x;
        for (int i = 0; i < 2000; i++) {
            res = next(res);
        }
//        System.out.println(x + ": " + res);
        sum += res;
    }
    System.out.println(sum);
    System.out.println("===");


    inputList = List.of(1L, 2L, 3L, 2024L);
//    inputList = List.of(123L);
    inputList = realInput;
    var N = inputList.size();
    Map<String, Integer>[] maps = new HashMap[N];
    for (int k = 0; k < N; k++) {
        System.out.println(k + "/" + N);
        var x = inputList.get(k);
        maps[k] = new HashMap<>();
        var generated = generateSequence(x, 2000);
        var M = generated.size();
        for (int i = 3; i < M; i++) {
            var builder = new StringBuilder();
            for (int j = i - 3; j <= i; j++) {
                builder.append(generated.get(j).right());
            }
            var key = builder.toString();
            int left = generated.get(i).left();
            maps[k].putIfAbsent(key, left);
        }
    }
    System.out.println("---");
    var res2 = 0L;
    for (int i = 0; i < N; i++) {
        System.out.println(i + "/" + N);
        for (var entry : maps[i].entrySet()) {
            var key = entry.getKey();
            var score = entry.getValue();
            for (int j = 0; j < N; j++) {
                if (i == j) continue;
                score += maps[j].getOrDefault(key, 0);
            }
            res2 = max(res2, score);
        }
//        System.out.println(maps[i].get("-21-13"));
    }
    System.out.println(res2);
}

private List<Pair<Integer, Integer>> generateSequence(long x, int cnt) {
    var res = new ArrayList<Pair<Integer, Integer>>();
    for (int i = 0; i < cnt; i++) {
        var prev = x;
        x = next(x);
        res.add(Pair.of((int) (x % 10), (int) (x % 10 - prev % 10)));
    }
    return res;
}

private long next(long x) {
    var y = x * 64;
    int mod = 16777216;
    x = (x ^ y) % mod;

    y = x / 32;
    x = (x ^ y) % mod;

    y = x * 2048;
    x = (x ^ y) % mod;
    return x;
}