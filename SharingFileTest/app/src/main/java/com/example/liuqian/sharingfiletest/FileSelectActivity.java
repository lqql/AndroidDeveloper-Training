package com.example.liuqian.sharingfiletest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

public class FileSelectActivity extends AppCompatActivity {
    private File mIntFilesDir;
    private File mIntFile;
    File[] mIntFiles;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
        mIntFilesDir = getFilesDir();
        mIntFile = new File(mIntFilesDir,"files");
        mIntFiles = mIntFile.listFiles();
        mListView = (ListView)findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mIntFiles));
        setResult(Activity.RESULT_CANCELED,null);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File requestFile = mIntFiles[position];
                Uri fileUri;
                Intent resultIntent = new Intent();;
                try {
                    fileUri = FileProvider.getUriForFile(
                            FileSelectActivity.this,
                            "com.example.liuqian.sharingfiletest.fileprovider",
                            requestFile);
                    if (fileUri!=null){

                        resultIntent.addFlags(
                                Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        resultIntent.setDataAndType(
                                fileUri,
                                getContentResolver().getType(fileUri));
                        // Set the result
                        setResult(Activity.RESULT_OK,
                                resultIntent);
                    } else {
                        resultIntent.setDataAndType(null, "");
                        setResult(RESULT_CANCELED,
                                resultIntent);
                    }
                    finish();

                } catch (IllegalArgumentException e) {
                    Log.e("File Selector",
                            "The selected file can't be shared: " +
                                    requestFile.getName());
                }

            }
        });
    }

}
