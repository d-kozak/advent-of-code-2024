package io.kozak;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Utils {


    public static Stream<String> loadInput(String name) {
        try {
            return Files.lines(Path.of(Utils.class.getResource("/" + name).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void swap(char[] arr, int left, int right) {
        var tmp = arr[left];
        arr[left] = arr[right];
        arr[right] = tmp;
    }
}
