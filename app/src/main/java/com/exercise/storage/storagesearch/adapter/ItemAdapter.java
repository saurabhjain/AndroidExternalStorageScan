package com.exercise.storage.storagesearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exercise.storage.storagesearch.model.Item;
import com.exercise.storage.storagesearch.R;

import java.util.ArrayList;

/**
 * Created by sjain70 on 5/11/16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList<Item> items;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemSize, itemExt;
        public ViewHolder(View view) {
            super(view);
            if (view != null) {
                itemName = (TextView) view.findViewById(R.id.tv_item_name);
                itemSize = (TextView) view.findViewById(R.id.tv_item_size);
                itemExt= (TextView) view.findViewById(R.id.tv_item_ext);
            }
        }
    }

    public ItemAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        if (holder != null) {
            holder.itemName.setText(item.getItemName());
            holder.itemSize.setText("Size: " + String.valueOf(item.getItemSize()) + " KBs");
            holder.itemExt.setText("Ext: " + item.getItemExtension());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}