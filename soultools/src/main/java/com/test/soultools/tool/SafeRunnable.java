package com.test.soultools.tool;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

/**
 * @author cd5160866
 * 带生命周期感知的runnable
 */
public class SafeRunnable implements Runnable {

    private Runnable imp;

    private Lifecycle lifecycle;

    public SafeRunnable(@NonNull Lifecycle lifecycle, @NonNull Runnable imp) {
        this.imp = imp;
        this.lifecycle = lifecycle;
    }

    @Override
    public void run() {
        if (null == lifecycle || null == imp) {
            return;
        }
        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        imp.run();
    }
}
