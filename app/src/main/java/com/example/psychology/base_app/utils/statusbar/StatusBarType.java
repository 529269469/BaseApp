package com.example.psychology.base_app.utils.statusbar;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-11-16 10:43.
 * Description :
 */
@IntDef({StatusBarType.STATUSBAR_TYPE_DEFAULT, StatusBarType.STATUSBAR_TYPE_MIUI, StatusBarType.STATUSBAR_TYPE_FLYME, StatusBarType.STATUSBAR_TYPE_ANDROID6})
@Retention(RetentionPolicy.SOURCE)
public @interface StatusBarType {
    int STATUSBAR_TYPE_DEFAULT = 0;
    int STATUSBAR_TYPE_MIUI = 1;
    int STATUSBAR_TYPE_FLYME = 2;
    int STATUSBAR_TYPE_ANDROID6 = 3; // Android 6.0
}
