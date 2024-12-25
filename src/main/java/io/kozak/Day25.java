void main() throws IOException {
    run("test25.txt");
    run("day25.txt");
}

int R = 7;
int C = 5;
int LIM = R - 2;

List<int[]> locks = new ArrayList<>();
List<int[]> keys = new ArrayList<>();

private void run(String fileName) throws IOException {
    locks.clear();
    keys.clear();
    try (var reader = new BufferedReader(new FileReader("src/main/resources/" + fileName))) {
        String line;
        while ((line = reader.readLine()) != null) {
            var cnt = new int[C];
            var isLock = line.charAt(0) == '#';
            for (int r = 1; r < R; r++) {
                line = reader.readLine();
                for (int i = 0; i < C; i++) {
                    if (line.charAt(i) == '#')
                        cnt[i]++;
                }
            }
            if (!isLock) {
                for (int i = 0; i < C; i++) {
                    cnt[i]--;
                }
            }
            if (isLock) locks.add(cnt);
            else keys.add(cnt);

            reader.readLine();
        }
    }
//    System.out.println(locks.stream().map(Arrays::toString).toList());
//    System.out.println(keys.stream().map(Arrays::toString).toList());
    var cnt = 0;
    for (var key : keys) {
        loop:
        for (var lock : locks) {
            for (int i = 0; i < C; i++) {
                if (key[i] + lock[i] > LIM)
                    continue loop;
            }
            cnt++;
        }
    }
    System.out.println(cnt);
}