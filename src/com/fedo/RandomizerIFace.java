package com.fedo;

import java.util.List;

/**
 * //
 * Created by katz on 26.05.2017.
 */
public interface RandomizerIFace {

    String EMPTY_TEXT = "-----------";

    void addItem(String item);

    void removeItem(String item);

    List<String> getItems();

    String getRandom();

    String getNextRandom();

    void clearItems();
}
