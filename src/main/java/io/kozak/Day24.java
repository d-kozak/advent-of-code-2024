void main() throws IOException {
    run("test24-1.txt");
    run("test24-2.txt");
    run("day24.txt");
}

class Register implements Comparable<Register> {
    String name;
    int value;

    Register(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Register r)) return false;
        return name.equals(r.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


    @Override
    public int compareTo(Register o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Register{" +
               "name='" + name + '\'' +
               ", value=" + value +
               '}';
    }
}

enum Op {
    AND, OR, XOR, UNKNOWN;

    static Op selectOp(String op) {
        return switch (op) {
            case "AND" -> AND;
            case "OR" -> OR;
            case "XOR" -> XOR;
            default -> throw new IllegalArgumentException("Unknown op: " + op);
        };
    }
}

record OpNode(Op op, Register left, Register right, Register target) {
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }
}

private void run(String filename) throws IOException {
    var registers = new HashMap<String, Register>();
    var operations = new ArrayList<OpNode>();
    var registerToOperations = new HashMap<Register, List<OpNode>>();
    var inputReadyCnt = new HashMap<OpNode, Integer>();
    try (var reader = new BufferedReader(new FileReader("src/main/resources/" + filename))) {
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            var split = line.split(": ");
            registers.put(split[0], new Register(split[0], Integer.parseInt(split[1])));
        }
        while ((line = reader.readLine()) != null) {
            var split = line.split(" ");
            var left = registers.computeIfAbsent(split[0], k -> new Register(k, -1));
            var op = Op.selectOp(split[1]);
            var right = registers.computeIfAbsent(split[2], k -> new Register(k, -1));
            var target = registers.computeIfAbsent(split[4], k -> new Register(k, -1));
            OpNode opNode = new OpNode(op, left, right, target);
            registerToOperations.computeIfAbsent(left, _ -> new ArrayList<>()).add(opNode);
            registerToOperations.computeIfAbsent(right, _ -> new ArrayList<>()).add(opNode);
            operations.add(opNode);
        }
    }
    var worklist = new ArrayDeque<OpNode>();
    for (var reg : registers.values()) {
        if (reg.value != -1) {
            for (OpNode opNode : registerToOperations.get(reg)) {
                inputReadyCnt.put(opNode, inputReadyCnt.getOrDefault(opNode, 0) + 1);
                if (inputReadyCnt.get(opNode) == 2) {
                    worklist.add(opNode);
                }
            }
        }
    }
    while (!worklist.isEmpty()) {
        var curr = worklist.removeFirst();
        if (curr.left.value == -1) throw new IllegalStateException();
        if (curr.right.value == -1) throw new IllegalStateException();
        if (curr.target.value != -1) throw new IllegalStateException();
        var res = switch (curr.op) {
            case AND -> curr.left.value != 0 && curr.right.value != 0;
            case OR -> curr.left.value != 0 || curr.right.value != 0;
            case XOR -> curr.left.value != 0 ^ curr.right.value != 0;
            case UNKNOWN -> throw new IllegalStateException();
        };
        curr.target.value = res ? 1 : 0;

        List<OpNode> deps = registerToOperations.get(curr.target);
        if (deps == null) continue;
        for (OpNode opNode : deps) {
            inputReadyCnt.put(opNode, inputReadyCnt.getOrDefault(opNode, 0) + 1);
            if (inputReadyCnt.get(opNode) == 2) {
                worklist.add(opNode);
            }
        }
    }

    var outputs = registers.values().stream().filter(it -> it.name.startsWith("z")).sorted().toList();
//    System.out.println(outputs);
//    System.out.println(outputs.size());
    var res1 = 0L;
    for (int i = outputs.size() - 1; i >= 0; i--) {
        var reg = outputs.get(i);
        if (reg.value == -1) throw new IllegalStateException();
        res1 = (res1 << 1) | reg.value;
    }
    System.out.println(res1);
    System.out.println("===");
}