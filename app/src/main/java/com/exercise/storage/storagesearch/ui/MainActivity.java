package com.exercise.storage.storagesearch.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exercise.storage.storagesearch.R;
import com.exercise.storage.storagesearch.adapter.ItemAdapter;
import com.exercise.storage.storagesearch.model.Item;
import com.exercise.storage.storagesearch.utils.CustomSizeComparator;
import com.exercise.storage.storagesearch.utils.PrefUtils;
import com.exercise.storage.storagesearch.utils.Utils;
import com.exercise.storage.storagesearch.utils.VerticalSpaceItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final long TEN_MB_FILTER = 1048576*10;
    private static final long FIVE_MB_FILTER = 1048576*5;
    private static final long ONE_MB_FILTER = 1048576;
    private static final long HALF_MB_FILTER = 524288;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    private static final int VERTICAL_ITEM_SPACE = 32;
    private ArrayList<Item> fileItems = new ArrayList<>();
    private ProgressBar mProgressBar;
    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mGridLayoutManager;
    private Button mButtonScanAgain, mButtonExtSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.scan_loading_progressbar);
        mButtonScanAgain = (Button) findViewById(R.id.btn_scan_again);
        mButtonExtSpace = (Button) findViewById(R.id.btn_extension_based_space);
        mRecyclerView = (RecyclerView) findViewById(R.id.items_rv);
        mGridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.num_column));
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE, this));
        if(null != PrefUtils.getItems(this) && PrefUtils.getItems(this).size() > 0) {
            fileItems.clear();
            updateContent();
            fileItems = new ArrayList<>(PrefUtils.getItems(this));
            updateContent();
        } else
            checkPermissionsAndScanStorage();

        mButtonScanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndScanStorage();
            }
        });
        mButtonExtSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent extSpaceActivity = new Intent(MainActivity.this, ExtensionBasedSpaceActivity.class);
                startActivity(extSpaceActivity);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            updateScanButton(false);
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE:
                scanStorage();
                break;
        }
    }

    private void checkPermissionsAndScanStorage() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Utils.arePermissionsGranted(this, permissions)) {
            scanStorage();
        } else {
            updateScanButton(false);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    private void scanStorage() {
        if(Utils.isExternalStorageReadable()) {
            updateScanButton(true);
            new ScanTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            updateScanButton(false);
            Toast.makeText(MainActivity.this, R.string.external_storage_not_read_ready, Toast.LENGTH_LONG).show();
        }
    }

    public void scanForFilesAndDirectories(File file) {
        if(file.isDirectory()) {
            if(file.length() >= HALF_MB_FILTER) {
                Item item = new Item();
                item.setItemName("DirName: " + file.getName());
                item.setItemSize(file.length()/1000);
                if(file.getName().split("\\.(?=[^\\.]+$)").length > 1)
                    item.setItemExtension(file.getName().split("\\.(?=[^\\.]+$)")[1]);
                else
                    item.setItemExtension(".dir");
                fileItems.add(item);
            }
            String[] filesAndDirectories = file.list();
            for(String fileOrDirectory : filesAndDirectories) {
                File f = new File(file.getAbsolutePath() + "/" + fileOrDirectory);
                scanForFilesAndDirectories(f);
            }
        } else {
            if(file.length() >= HALF_MB_FILTER) {
                Item item = new Item();
                item.setItemName("FileName: " + file.getName());
                item.setItemSize(file.length()/1000);
                if(file.getName().split("\\.(?=[^\\.]+$)").length > 1)
                    item.setItemExtension(file.getName().split("\\.(?=[^\\.]+$)")[1]);
                else
                    item.setItemExtension(".file");
                fileItems.add(item);
            }
        }
    }

    private class ScanTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected  void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            fileItems.clear();
            scanForFilesAndDirectories(Environment.getExternalStorageDirectory());
            Collections.sort(fileItems, new CustomSizeComparator());
            PrefUtils.storeItems(MainActivity.this, fileItems);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateScanButton(false);
            mProgressBar.setVisibility(View.GONE);
            updateContent();
        }
    }

    private void updateContent() {
        if (mAdapter == null) {
            mAdapter = new ItemAdapter(fileItems);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(fileItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateScanButton(boolean isScanRunning) {
        if(isScanRunning) {
            mButtonScanAgain.setEnabled(false);
            mButtonExtSpace.setEnabled(false);
        }
        else {
            mButtonScanAgain.setEnabled(true);
            mButtonExtSpace.setEnabled(true);
        }
    }
}
