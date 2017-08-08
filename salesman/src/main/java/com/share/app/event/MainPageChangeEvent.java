package com.share.app.event;

/**
 * Created by Snow on 2017/7/31.
 */

public class MainPageChangeEvent {

    private int index;

    /**
     * @param showPageIndex 要显示的界面的下标
     */
    public MainPageChangeEvent(int showPageIndex){
        this.index = showPageIndex;
    }

    public int getIndex(){
        return this.index;
    }

}
