package com.fedo;

/**
 * Created by katz on 26.05.2017.
 */
public interface GuiIFace {

    void onUpdateRandomResult(String rezult);

    void onFinishRandomResult(String rezult);

    void onNoItemsToRandom();

    void onFewItemsToRandom();

    void onUpdateFilesList();
}
