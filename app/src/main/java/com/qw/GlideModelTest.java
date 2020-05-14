package com.qw;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.module.GlideModule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author: 思忆
 * Date: Created in 2020/5/14 6:46 PM
 */
public class GlideModelTest implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        Log.d("GlideModule", "applyOptions");
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        Log.d("GlideModule", "registerComponents");
        startGlideHook(context, glide);
    }

    private void startGlideHook(Context context, Glide glide) {
        try {
            Method getEngine = Glide.class.getDeclaredMethod("getEngine");
            getEngine.setAccessible(true);
            Engine engine = (Engine) getEngine.invoke(glide);
            Object engineProxy = Proxy.newProxyInstance(Engine.class.getClassLoader(), new Class[]{Engine.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}