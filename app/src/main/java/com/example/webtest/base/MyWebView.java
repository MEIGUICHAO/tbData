package com.example.webtest.base;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyWebView extends WebView {


    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public MyWebView(Context context) {
        super(context);
    }


}
