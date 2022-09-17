package com.qw;

import android.content.Intent;
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
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMain.this, ActivitySecond.class));
            }
        });
        buttonTest = findViewById(R.id.btn1);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TLog.d("qw", "This is An Log in java:",ActivityMain.this);
                Toast.makeText(ActivityMain.this, "", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
