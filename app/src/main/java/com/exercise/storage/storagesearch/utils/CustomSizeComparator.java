package com.exercise.storage.storagesearch.utils;

import com.exercise.storage.storagesearch.model.Item;

import java.util.Comparator;

/**
 * Created by sjain70 on 5/11/16.
 */
public class CustomSizeComparator implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        return Long.valueOf(o2.getItemSize()).compareTo(o1.getItemSize());
    }
}
