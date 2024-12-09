package io.kozak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static io.kozak.Utils.swap;

public class Day9 {

    public static void main(String[] args) throws IOException {
        String filename = "/test9.txt";
        filename = "/day9.txt";
        try (var reader = new BufferedReader(new InputStreamReader(Day9.class.getResourceAsStream(filename)))) {
            var input = reader.readLine();
            solve1(input);
            solve2(input);
        }
    }

    private static class Cell {
        int fileId;
        int len;
        Cell prev;
        Cell next;

        public Cell(int fileId, int len) {
            this.fileId = fileId;
            this.len = len;
        }

        @Override
        public String toString() {
            return "Cell{" +
                   "fileId=" + fileId +
                   ", len=" + len +
                   '}';
        }
    }

    private static void solve2(String input) {
        var n = input.length();
        var isSpace = false;
        var fileId = 0;
        Cell head = null;
        Cell tail = null;
        for (int i = 0; i < n; i++) {
            var curr = new Cell(isSpace ? -1 : fileId++, input.charAt(i) - '0');
            if (head == null) {
                head = curr;
            }
            if (tail != null) {
                tail.next = curr;
                curr.prev = tail;
            }
            tail = curr;
            isSpace = !isSpace;
        }
//        printList(head);

        var right = tail;
        while (right != null) {
            if (right.fileId == -1) {
                right = right.prev;
                continue;
            }

            Cell candidate = null;
            var left = right.prev;
            while (left != null) {
                if (left.fileId == -1 && left.len >= right.len)
                    candidate = left;
                left = left.prev;
            }
            if (candidate != null) {
                var diff = candidate.len - right.len;
                candidate.fileId = right.fileId;
                candidate.len = right.len;
                if (diff > 0) {
                    var extraSpace = new Cell(-1, diff);

                    extraSpace.next = candidate.next;
                    extraSpace.prev = candidate;

                    candidate.next.prev = extraSpace;
                    candidate.next = extraSpace;
                }

//                if (right.next != null && right.next.fileId == -1) {
//                    right.len += right.next.len;
//
//                    right.next = right.next.next;
//                    right.next.prev = right;
//                }
//                if (right.prev.fileId == -1) {
//                    right.prev.len += right.len;
//
//                    right.prev.next = right.next;
//                    if (right.next != null)
//                        right.next.prev = right.prev;
//                }
                right.fileId = -1;
            }

            right = right.prev;
        }
        printList(head);
        prettyPrint(head);
        var score = 0L;
        var curr = head;
        var currId = 0L;
        while (curr != null) {
            if (curr.fileId != -1) {
                for (int i = 0; i < curr.len; i++) {
                    score += curr.fileId * currId++;
                }
            } else {
                currId += curr.len;
            }
            curr = curr.next;
        }
        System.out.println(score);
    }

    private static void prettyPrint(Cell head) {
        var curr = head;
        while (curr != null) {
            for (int i = 0; i < curr.len; i++) {
                System.out.print(curr.fileId != -1 ? (char) (curr.fileId + '0') : '.');
            }
            curr = curr.next;
        }
        System.out.println();
    }

    private static void printList(Cell head) {
        var curr = head;
        while (curr != null) {
            System.out.println(curr);
            curr = curr.next;
        }
    }

    private static void solve1(String input) {
        var arr = expand(input);
        var n = arr.length;
        var left = 0;
        var right = n - 1;
        while (left < right) {
            while (left < right && arr[right] == '.') right--;
            while (left < right && arr[left] != '.') left++;
            if (left >= right) break;
            swap(arr, left++, right--);
        }
//        System.out.println(arr);
        System.out.println(score(arr));
    }

    private static long score(char[] arr) {
        var res = 0L;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '.') break;
            res += ((long) i) * (arr[i] - '0');
        }
        return res;
    }

    private static char[] expand(String input) {
        var res = new char[input.length() * 9];
        var k = 0;
        var isSpace = false;
        var fileId = 0;
        for (int i = 0; i < input.length(); i++) {
            var cnt = input.charAt(i) - '0';
            for (int j = 0; j < cnt; j++) {
                res[k++] = isSpace ? '.' : (char) ('0' + fileId);
            }
            if (isSpace)
                fileId++;
            isSpace = !isSpace;
        }
        while (k < res.length)
            res[k++] = '.';
        return res;
    }
}
