package com.fedo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.fedo.Utils.DATA_STORE_DIR;
import static com.fedo.Utils.EXT;

/**
 * //
 * Created by katz on 26.05.2017.
 */
public class Randomizer
        implements RandomizerIFace {

    private List<String> items = new ArrayList<>();
    private String lastRandom = "";

    Randomizer(String title) {
        loadDefaultRezults();
        new MainFrame(this, title);
    }

    public void addItem(String item) {
        if (!items.contains(item) || item.equals(EMPTY_TEXT))
            items.add(item);
        Utils.sortItems(items);
        saveDefaultRezults();
    }

    public void removeItem(String item) {
        if (items.contains(item))
            items.remove(item);
        saveDefaultRezults();
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

    @Override
    public void startNewRandomMeassure(GuiIFace guiIFace) {
        if (items.size() == 0) {
            guiIFace.onNoItemsToRandom();
            return;
        }
        if (items.size() == 0) {
            guiIFace.onFewItemsToRandom();
            return;
        }
        new Thread(() -> {
            int count = 500;
            // value count
            Map<String, Integer> map = new HashMap<>();
            while (count-- > 0 || getRandom().equals(EMPTY_TEXT)) {
                guiIFace.onUpdateRandomResult(getNextRandom());
                if (map.containsKey(getRandom()))
                    map.put(getRandom(), map.get(getRandom()) + 1);
                else if (!getRandom().equals(EMPTY_TEXT))
                    map.put(getRandom(), 1);
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ignored) {
                }
            }
            StringBuilder rezult = new StringBuilder();
            map = Utils.sortByValue(map);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                rezult.insert(0, String.format("<b>%s</b> %d<br>", entry.getKey(), entry.getValue()));
                guiIFace.onUpdateRandomResult(entry.getKey());
            }
            rezult.insert(0, "<html>");
            rezult.append("</html>");
            guiIFace.onFinishRandomResult(rezult.toString());
        }).start();
    }

    @Override
    public void saveItemsToFile(GuiIFace guiIFace, String name) {
        Utils.saveFileToStore(name, (ArrayList<String>) items);
        guiIFace.onUpdateFilesList();
    }

    @Override
    public void loadItemsFromFile(GuiIFace guiIFace, String name) {
        items = Utils.loadFileFromStore(name);
        guiIFace.onUpdateFilesList();
    }

    @Override
    public void deleteItemFile(GuiIFace guiIFace, String name) {
        Utils.deleteFileFromStore(name);
        guiIFace.onUpdateFilesList();
    }

    private Path defaultSavePath = Paths.get(DATA_STORE_DIR + "/default" + EXT);

    private void saveDefaultRezults() {
        try {
            if (!DATA_STORE_DIR.exists() && !DATA_STORE_DIR.mkdirs())
                return;
            Files.write(defaultSavePath, items, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultRezults() {
        try {
            items = Files.readAllLines(defaultSavePath, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.sortItems(items);
    }

}
