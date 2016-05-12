package com.exercise.storage.storagesearch.model;

import java.io.Serializable;

/**
 * Created by sjain70 on 5/11/16.
 */
public class Item implements Serializable {

    private String itemName;
    private String itemExtension;
    private long itemSize;

    public String getItemExtension() {
        return itemExtension;
    }

    public void setItemExtension(String itemExtension) {
        this.itemExtension = itemExtension;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getItemSize() {
        return itemSize;
    }

    public void setItemSize(long itemSize) {
        this.itemSize = itemSize;
    }
}
