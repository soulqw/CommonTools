package com.qw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.qw.tools.R;
import com.test.soultools.tool.log.TLog;


/**
 * Author: 思忆
 * Date: Created in 2020/5/14 4:24 PM
 */
public class ActivityMain extends AppCompatActivity {
    Button buttonTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonTest = findViewById(R.id.btn_hook1);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TLog.d("qw","123");
                Toast.makeText(ActivityMain.this, "", Toast.LENGTH_SHORT).show();
            }
        });


    }



}
