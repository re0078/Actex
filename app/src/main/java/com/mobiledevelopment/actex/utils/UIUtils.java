package com.mobiledevelopment.actex.utils;

import android.view.MotionEvent;
import android.view.View;

public class UIUtils {

    public static void setupOnTouchListener(View view){
        view.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                v.callOnClick();
                v.setElevation(0.0f);
                v.setAlpha(0.3f);
            }else {
                v.setElevation(5);
                v.setAlpha(1f);
            }
            return true;
        });
    }
}
