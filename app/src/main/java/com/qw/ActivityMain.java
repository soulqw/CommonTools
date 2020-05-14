package com.qw;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.qw.tools.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
                Toast.makeText(ActivityMain.this, "", Toast.LENGTH_SHORT).show();
            }
        });

        startHook(buttonTest);
        ImageView imageView = findViewById(R.id.iv_glide);
        ImageView imageView2 = findViewById(R.id.iv_glide2);
        Glide.with(this).load("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1906469856,4113625838&fm=26&gp=0.jpg")
                .into(imageView);
        Glide.with(this).load("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1141259048,554497535&fm=26&gp=0.jpg")
                .into(imageView2);
    }

    private void startHook(View view) {
        try {
            Method method = View.class.getDeclaredMethod("getListenerInfo");
            method.setAccessible(true);
            Object mListenerInfo = method.invoke(view);

            Class<?> lisntenrClass = Class.forName("android.view.View$ListenerInfo");
            Field field = lisntenrClass.getDeclaredField("mOnClickListener");
            final View.OnClickListener onClickListener = (View.OnClickListener) field.get(mListenerInfo);
            Object proxy = Proxy.newProxyInstance(view.getContext().getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Log.d("qw", "this is from invoke");
                    return method.invoke(onClickListener, args);
                }
            });
            field.set(mListenerInfo, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
