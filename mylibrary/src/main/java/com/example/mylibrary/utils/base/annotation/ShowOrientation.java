package com.example.mylibrary.utils.base.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-11-03 10:45.
 * Description : 设置toast显示方向
 */
@IntDef({ShowOrientation.START, ShowOrientation.TOP, ShowOrientation.END, ShowOrientation.BOTTOM})
@Target(PARAMETER)
@Retention(SOURCE)
public @interface ShowOrientation {
    int START = 0;
    int TOP = 1;
    int END = 2;
    int BOTTOM = 3;
}
