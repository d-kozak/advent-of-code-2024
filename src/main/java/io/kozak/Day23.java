void main() throws IOException {
    run("test23.txt");
    run("day23.txt");
}

private void run(String fileName) throws IOException {
    var graph = new HashMap<String, List<String>>();
    for (var edge : Files.lines(Path.of("src/main/resources", fileName)).map(it -> it.split("-")).toList()) {
        var from = edge[0];
        var to = edge[1];
        graph.computeIfAbsent(from, (_) -> new ArrayList<>()).add(to);
        graph.computeIfAbsent(to, (_) -> new ArrayList<>()).add(from);
    }

    var valid = new HashSet<String>();
    Set<String> biggest = Set.of();
    var seen = new HashSet<String>();

    for (var node : graph.keySet()) {
        var triples = findTriples(node, graph);
        triples.stream()
                .filter(it -> it.stream().anyMatch(it2 -> it2.startsWith("t")))
                .peek(Collections::sort)
                .map(it -> String.join(",", it))
                .forEach(valid::add);
        if (!seen.contains(node)) {
            var reach = findReachable(node, graph);
            seen.addAll(reach);
            if (biggest.size() < reach.size())
                biggest = reach;
        }

    }
    System.out.println(valid.size());
    System.out.println(String.join(",", biggest.stream().sorted().toList()));
    System.out.println("===");
}

private Set<String> findReachable(String node, HashMap<String, List<String>> graph) {
    var seen = new HashSet<String>();
    var stack = new Stack<String>();
    stack.add(node);
    seen.add(node);
    while (!stack.isEmpty()) {
        var curr = stack.removeLast();
        for (var nei : graph.get(curr)) {
            if (seen.add(nei))
                stack.add(nei);
        }
    }
    return seen;
}

private List<List<String>> findTriples(String node, Map<String, List<String>> graph) {
    record State(String node, int len, State prev) {
    }
    var res = new ArrayList<List<String>>();
    var stack = new ArrayList<State>();
    stack.add(new State(node, 0, null));
    while (!stack.isEmpty()) {
        var curr = stack.removeLast();
        if (curr.len == 3) {
            if (curr.node.equals(node))
                res.add(new ArrayList<>(List.of(node, curr.prev.prev.node, curr.prev.node)));
            continue;
        }
        for (var nei : graph.get(curr.node)) {
            stack.add(new State(nei, curr.len + 1, curr));
        }
    }
    return res;
}