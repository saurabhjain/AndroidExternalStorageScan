package com.exercise.storage.storagesearch.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.exercise.storage.storagesearch.R;
import com.exercise.storage.storagesearch.model.Item;
import com.exercise.storage.storagesearch.utils.PrefUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by sjain70 on 5/11/16.
 */
public class ExtensionBasedSpaceActivity extends AppCompatActivity {

    private TextView mExtSpaceData;
    private StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extension);
        mExtSpaceData = (TextView) findViewById(R.id.tv_ext_space_data);

        if(null != PrefUtils.getItems(this)) {
            calculateAndPrintExtBasedSpaceData(new ArrayList<>(PrefUtils.getItems(this)));
        }
        else
            sb.append("No Data found");
        mExtSpaceData.setText(sb.toString());
    }

    private void calculateAndPrintExtBasedSpaceData(ArrayList<Item> fileItems) {
        if(fileItems.size() == 0) {
            sb.append("No Data found");
            return;
        } else {
            // Need to sort by extension name to make sure following for loop works fine.
            Collections.sort(fileItems, new Comparator<Item>() {
                @Override
                public int compare(final Item object1, final Item object2) {
                    return object1.getItemExtension().compareTo(object2.getItemExtension());
                }
            } );
        }
        String prevExt = fileItems.get(0).getItemExtension(), currentExt;
        long totalCurrentSpace = 0;
        for(int i = 0; i < fileItems.size(); i++) {
            currentExt = fileItems.get(i).getItemExtension();
            if(prevExt.equalsIgnoreCase(currentExt)) {
                totalCurrentSpace = totalCurrentSpace + fileItems.get(i).getItemSize();
            } else {
                sb.append("Total space taken by " + prevExt + ": " + totalCurrentSpace + " Kb's").append("\n\n");
                totalCurrentSpace = fileItems.get(i).getItemSize();
            }
            prevExt = currentExt;
        }
    }
}
