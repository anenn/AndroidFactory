package com.anenn.core.app;


import android.app.Activity;

import java.util.Stack;

/**
 * Activity Manager
 * Created by Anenn on 15-7-23.
 */
public class ActivityManager {
    private Stack<Activity> mActivityStack;
    private static ActivityManager mActivityManager;

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (mActivityManager == null) {
            synchronized (ActivityManager.class) {
                if (mActivityManager == null) {
                    mActivityManager = new ActivityManager();
                }
            }
        }
        return mActivityManager;
    }

    /**
     * Get the stack which stores activity
     *
     * @return
     */
    public final Stack<Activity> getActivityStack() {
        return mActivityStack;
    }

    /**
     * Get current activity which the last one pushed into the stack
     *
     * @return
     */
    public final Activity currentActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * Add activity into stack
     *
     * @param activity
     */
    public final void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }

        if (activity != null) {
            mActivityStack.add(activity);
        }
    }

    /**
     * Remove current activity which the last one pushed into the stack
     */
    public final void removeActivity() {
        Activity activity = mActivityStack.lastElement();
        removeActivity(activity);
    }

    /**
     * Remove the specific activity from stack
     */
    public final void removeActivity(Activity activity) {
        if (activity != null) {
            if (activity.isFinishing()) {
                mActivityStack.remove(activity);
                activity = null;
            }
        }
    }

    /**
     * Remove all of activity
     */
    public final void removeActivities() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (mActivityStack.get(i) != null) {
                mActivityStack.get(i).finish();
            }
        }
    }

    /**
     * Exit the application
     */
    public final void AppExit() {
        try {
            removeActivities();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
