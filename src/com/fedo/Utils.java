package com.fedo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by katz on 26.05.2017.
 */
public class Utils {

    static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".random");
    static final String EXT = ".txt";

    static void saveFileToStore(String name, ArrayList<String> items) {

        try {
            Path path = Paths.get(DATA_STORE_DIR + "/" + name + EXT);
            if (DATA_STORE_DIR.exists() && DATA_STORE_DIR.mkdirs())
                return;
            Files.write(path, items, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<String> loadFileFromStore(String name) {

        Path path = Paths.get(DATA_STORE_DIR + "/" + name + EXT);
        if (new File(String.valueOf(path.toAbsolutePath())).exists())
            try {
                return Files.readAllLines(path, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return new ArrayList<>();
    }

    static void deleteFileFromStore(String name) {

        Path path = Paths.get(DATA_STORE_DIR + "/" + name + EXT);
        if (new File(String.valueOf(path.toAbsolutePath())).exists()) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static ArrayList<String> getStoreFilesList() {

        ArrayList<String> rezult = new ArrayList<>();
        File dir = new File(String.valueOf(DATA_STORE_DIR));
        if (!dir.exists())
            return rezult;
        File[] files = dir.listFiles();
        if (files != null)
            for (File file : files) {
                String fname = file.getName().replaceFirst("[.][^.]+$", "");
                if (!fname.equals("default"))
                    rezult.add(fname);
            }
        return rezult;
    }

    static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    static void sortItems(List<String> items) {

        items.sort((obj1, obj2) -> {
            if (Objects.equals(obj1, obj2))
                return 0;
            if (obj1 == null)
                return -1;
            if (obj2 == null)
                return 1;
            if (obj1.equals(RandomizerIFace.EMPTY_TEXT))
                return 1;
            if (obj2.equals(RandomizerIFace.EMPTY_TEXT))
                return -1;
            return obj1.compareTo(obj2);
        });
    }

}
