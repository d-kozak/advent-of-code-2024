import static java.util.stream.Collectors.joining;

void main() throws IOException {
    run("test17-2.txt");
    run("day17.txt");
}

private void run(String file) throws IOException {
    try (var reader = new BufferedReader(new FileReader("src/main/resources/" + file))) {
        parseState(reader);
        long b_backup = b;
        long c_backup = c;
        System.out.println(simulate().stream().map(Object::toString).collect(joining(",")));
        var prev = new ArrayList<Long>();
        prev.add(0L);
        var next = new ArrayList<Long>();
        for (int i = code.length - 1; i >= 0; i--) {
            for (var p : prev) {
                for (int lsb = 0; lsb <= 7; lsb++) {
                    long x = (p << 3) | lsb;
                    a = x;
                    b = 0;
                    c = 0;
                    pc = 0;
                    var res = simulate();
                    if (res.get(0) == code[i]) {
                        next.add(x);
                    }
                }
            }
            var tmp = prev;
            prev = next;
            next = tmp;
            next.clear();
        }
        for (var p : prev) {
            a = p;
            b = 0;
            c = 0;
            pc = 0;
            var res = simulate();
            if (res.size() != code.length) throw new IllegalStateException();
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i) != code[i]) throw new IllegalStateException();
            }
        }
        if (!Objects.equals(prev.get(0), prev.stream().sorted().findFirst().get())) throw new IllegalStateException();
        System.out.println(prev.get(0));
    }
}

long a;
long b;
long c;
int[] code;
int pc;

final int ADV = 0;
final int BXL = 1;
final int BST = 2;
final int JNZ = 3;
final int BXC = 4;
final int OUT = 5;
final int BDV = 6;
final int CDV = 7;

long combo(int value) {
    return switch (value) {
        case 0, 1, 2, 3 -> value;
        case 4 -> a;
        case 5 -> b;
        case 6 -> c;
        default -> throw new IllegalStateException("Unexpected value: " + value);
    };
}

private List<Integer> simulate() {
    var idx = 0;
    var res = new ArrayList<Integer>();
    while (pc < code.length) {
//        System.err.println(code[pc] + " " + code[pc+1] + " " + combo(code[pc+1]));
        switch (code[pc]) {
            case ADV -> a = a / myPow();
            case BXL -> b = b ^ code[pc + 1];
            case BST -> b = combo(code[pc + 1]) % 8;
            case JNZ -> {
                if (a != 0) {
                    pc = code[pc + 1];
                    continue;
                }
            }
            case BXC -> b = b ^ c;
            case OUT -> {
                int x = (int) (combo(code[pc + 1]) % 8);
                res.add(x);
            }
            case BDV -> b = a / myPow();
            case CDV -> c = a / myPow();
            default -> throw new IllegalStateException("Unexpected value: " + code);
        }
        pc += 2;
    }
    return res;
}

private long myPow() {
    long input = combo(code[pc + 1]);
    long res = (long) Math.pow(2, input);
//    System.err.println(input + " -> " + res);
    return res;
}

void parseState(BufferedReader reader) throws IOException {
    a = Long.parseLong(reader.readLine().split(": ")[1]);
    b = Long.parseLong(reader.readLine().split(": ")[1]);
    c = Long.parseLong(reader.readLine().split(": ")[1]);
    reader.readLine();
    code = Arrays.stream(reader.readLine().split(": ")[1].split(",")).mapToInt(Integer::parseInt).toArray();
    pc = 0;
}