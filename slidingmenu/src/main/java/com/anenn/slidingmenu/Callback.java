package com.anenn.slidingmenu;

public interface Callback {
    void onBefore();

    boolean onRun();

    void onAfter(boolean b);
}
