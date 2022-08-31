package com.example.baseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mylibrary.base.BaseMVVMActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}