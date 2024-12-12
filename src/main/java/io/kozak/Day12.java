import io.kozak.Pair;

void main() throws IOException {
    String filename = "main/resources/test12.txt";
//    filename = "main/resources/test12.2.txt";
//    filename = "main/resources/test12.3.txt";
    filename = "main/resources/day12.txt";
    var grid = Files.lines(Paths.get("src/" + filename))
            .map(String::toCharArray)
            .toArray(char[][]::new);
    solve(grid);
}

void solve(char[][] grid) {
    var n = grid.length;
    var m = grid[0].length;
    var res = 0L;
    var res2 = 0L;
    var seen = new boolean[n][m];
    var nei = List.of(Pair.of(-1, 0), Pair.of(1, 0), Pair.of(0, -1), Pair.of(0, 1));
    var rUp = new ArrayList<ArrayList<Integer>>();
    for (int i = 0; i < n; i++) {
        rUp.add(new ArrayList<>());
    }
    var rDown = new ArrayList<ArrayList<Integer>>();
    for (int i = 0; i < n; i++) {
        rDown.add(new ArrayList<>());
    }
    var colLeft = new ArrayList<ArrayList<Integer>>();
    for (int i = 0; i < m; i++) {
        colLeft.add(new ArrayList<>());
    }
    var colRight = new ArrayList<ArrayList<Integer>>();
    for (int i = 0; i < m; i++) {
        colRight.add(new ArrayList<>());
    }
    var lists = List.of(rUp, rDown, colLeft, colRight);
    for (int r = 0; r < n; r++) {
        for (int c = 0; c < m; c++) {
            if (seen[r][c]) continue;
            seen[r][c] = true;
            var queue = new ArrayDeque<Pair<Integer, Integer>>();
            queue.add(Pair.of(r, c));
            var perimeter = 0L;
            var area = 0L;
            while (!queue.isEmpty()) {
                var curr = queue.removeFirst();
                area++;
                for (var d : nei) {
                    var nr = curr.left() + d.left();
                    var nc = curr.right() + d.right();
                    if (nr >= 0 && nr < n && nc >= 0 && nc < m && grid[r][c] == grid[nr][nc]) {
                        if (!seen[nr][nc]) {
                            seen[nr][nc] = true;
                            queue.add(Pair.of(nr, nc));
                        }
                    } else {
                        if (d.left() == +1) {
                            rUp.get(curr.left()).add(curr.right());
                        } else if (d.left() == -1) {
                            rDown.get(curr.left()).add(curr.right());
                        } else if (d.right() == +1) {
                            colRight.get(curr.right()).add(curr.left());
                        } else if (d.right() == -1) {
                            colLeft.get(curr.right()).add(curr.left());
                        }
                        perimeter++;
                    }
                }
            }
            var price = perimeter * area;
            res += price;
//            IO.println("Kind: " + grid[r][c] + " Perimeter: " + perimeter + " Area: " + area + " Price: " + price);
            var borders = 0;
            for (var g : lists) {
                for (var l : g) {
                    var prev = -2;
                    l.sort(Integer::compareTo);
                    for (var x : l) {
                        if (prev + 1 != x) {
                            borders++;
                        }
                        prev = x;
                    }
                    l.clear();
                }
            }
            IO.println("Kind: " + grid[r][c] + " Borders: " + borders);
            price = borders * area;
            res2 += price;
        }
    }
    IO.println(res);
    IO.println(res2);
}

