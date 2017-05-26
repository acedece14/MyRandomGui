package com.fedo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * //
 * Created by katz on 26.05.2017.
 */
public class Randomizer implements RandomizerIFace {

    private List<String> items = new ArrayList<>();
    private String lastRandom = "";

    Randomizer(String title) {
        loadRezults();
        new MainFrame(this, title);
    }

    public void addItem(String item) {
        if (!items.contains(item) || item.equals(EMPTY_TEXT))
            items.add(item);
        sortItems();
        saveRezults();
    }

    private void sortItems() {

        items.sort((obj1, obj2) -> {
            if (Objects.equals(obj1, obj2))
                return 0;
            if (obj1 == null)
                return -1;
            if (obj2 == null)
                return 1;
            if (obj1.equals(EMPTY_TEXT))
                return 1;
            if (obj2.equals(EMPTY_TEXT))
                return -1;
            return obj1.compareTo(obj2);
        });
    }

    public void removeItem(String item) {
        if (items.contains(item))
            items.remove(item);
        saveRezults();
    }

    @Override
    public List<String> getItems() {
        return items;
    }

    public String getRandom() {
        return lastRandom;
    }

    public String getNextRandom() {

        if (items.size() == 0)
            return "";
        int rand = new Random().nextInt(items.size());
        lastRandom = items.get(rand);
        return lastRandom;
    }

    @Override
    public void clearItems() {
        items.clear();
    }

    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".random");
    private Path savePath = Paths.get(DATA_STORE_DIR + "/items.txt");

    private void saveRezults() {
        try {
            if (!DATA_STORE_DIR.exists() && !DATA_STORE_DIR.mkdirs())
                return;
            Files.write(savePath, items, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRezults() {
        try {
            items = Files.readAllLines(savePath, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sortItems();
    }

}