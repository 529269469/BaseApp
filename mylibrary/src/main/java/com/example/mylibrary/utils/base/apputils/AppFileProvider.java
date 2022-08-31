package com.example.mylibrary.utils.base.apputils;

import android.app.Application;

import androidx.core.content.FileProvider;

import com.example.mylibrary.utils.base.apputils.AppManager;


/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-08-26 16:52.
 * Description :
 */
public class AppFileProvider extends FileProvider {
    @Override
    public boolean onCreate() {
        AppManager.Companion.getInstance().init((Application) getContext().getApplicationContext());
        return true;
    }
}