package com.karhatsu.suosikkipysakit.ui;

import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ToolbarUtil {
    public static void setToolbarPadding(Toolbar toolbar) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            lp.topMargin = topInset;
            v.setLayoutParams(lp);
            return insets;
        });
        ViewCompat.requestApplyInsets(toolbar);
    }
}
