package com.example.liuqian.filesharetestclient;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.getFile);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mRequestFileIntent = new Intent(Intent.ACTION_PICK);
                mRequestFileIntent.setType("text/plain");
                startActivityForResult(mRequestFileIntent, 0);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent returnIntent) {
        // If the selection didn't work
        if (resultCode != RESULT_OK) {
            // Exit without doing anything else
            return;
        } else {
            readFile(returnIntent.getData());
        }
    }
    private void readFile(Uri returnUri) {
        ParcelFileDescriptor inputPFD;
        //获取文件句柄
        try {
            inputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        //获取文件名字和大小
        Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
		    /*
		     * Get the column indexes of the data in the Cursor,
		     * move to the first row in the Cursor, get the data,
		     * and display it.
		     */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        Toast.makeText(MainActivity.this,returnCursor.getString(nameIndex),Toast.LENGTH_LONG).show();
        returnCursor.close();

        //读取文件内容
        String content = "";
        FileReader fr = null;
        char[] buffer = new char[1024];

        try {
            StringBuilder strBuilder = new StringBuilder();
            fr = new FileReader(inputPFD.getFileDescriptor());
            while (fr.read(buffer) != -1) {
                strBuilder.append(buffer);
            }
            fr.close();
            content = strBuilder.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (content.length() != 0) {
            Toast.makeText(MainActivity.this,content,Toast.LENGTH_LONG).show();
        } else {
            return;
        }

        try {
            inputPFD.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


