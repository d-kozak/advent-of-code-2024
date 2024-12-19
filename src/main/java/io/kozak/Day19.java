import io.kozak.Pair;

void main() throws IOException {
    run("test19.txt");
    run("day19.txt");
}

Set<String> patterns;
Map<Pair<String, Integer>, Long> dp = new HashMap<>();

private void run(String fileName) throws IOException {
    var lines = Files.readAllLines(Path.of("src/main/resources/" + fileName), Charset.defaultCharset());
    patterns = Arrays.stream(lines.get(0).split(", ")).collect(Collectors.toSet());
    dp.clear();

    var res1 = 0;
    var res2 = 0L;
    for (int i = 2; i < lines.size(); i++) {
        var design = lines.get(i);
        long matchCnt = canMatch(design, 0);
        if (matchCnt > 0)
            res1++;
        res2 += matchCnt;
    }
    System.out.println(res1);
    System.out.println(res2);
}

private long canMatch(String design, int idx) {
    if (idx == design.length()) return 1;
    var key = Pair.of(design, idx);
    var prev = dp.get(key);
    if (prev != null) return prev;
    var res = 0L;
    loop:
    for (var p : patterns) {
        if (idx + p.length() > design.length()) continue;
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) != design.charAt(idx + i)) continue loop;
        }
        res += canMatch(design, idx + p.length());
    }
    dp.put(key, res);
    return res;
}
