package com.anenn.imageloader;

/**
 * Created by anenn on 6/2/16.
 */
public class GlobalDisplayImage {

    private static DisplayImage mDisplayImage = new DisplayImage.Build().build();

    public static void setDisplayImage(DisplayImage displayImage) {
        if (displayImage == null) {
            throw new IllegalArgumentException("DisplayImage cannot be empty.");
        }

        mDisplayImage = displayImage;
    }

    public static int getImageOnLoading() {
        return mDisplayImage.getImageOnLoading();
    }

    public static int getImageForEmptyUri() {
        return mDisplayImage.getImageForEmptyUri();
    }

    public static int getImageOnFail() {
        return mDisplayImage.getImageOnFail();
    }

}
