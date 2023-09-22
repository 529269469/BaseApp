package com.example.psychology.music_utils;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psychology.R;
import com.example.psychology.music_utils.adapter.MyRAdapter;

import java.util.ArrayList;

public class MusicFragment extends Fragment implements View.OnClickListener {
    private ImageView start, top, next;
    public static SeekBar seekBar;
    private View view;
    private RecyclerView recyclerView;
    private MyRAdapter adapter;
    private ArrayList<Modle> list = new ArrayList<>();
    private IService myService;

    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("dur");
            int current = bundle.getInt("cur");
            seekBar.setMax(duration);
            seekBar.setProgress(current);

        }
    };

    private ServiceConnection serviceConnection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_a, null);
        initView();

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myService = (IService) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


//        list.add(new Modle("人间惊鸿客"));
//        list.add(new Modle("孤勇者"));
//        list.add(new Modle("数码宝贝"));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickliner(p -> {
            start.setImageResource(R.mipmap.icon_pause);
            myService.tiaomu(p);
        });
        return view;
    }

    private void initView() {
        start = (ImageView) view.findViewById(R.id.start);
        top = (ImageView) view.findViewById(R.id.top);
        next = (ImageView) view.findViewById(R.id.next);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        seekBar = (SeekBar) view.findViewById(R.id.seekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myService.seet(seekBar.getProgress());
            }
        });

        start.setOnClickListener(this);
        top.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                if (myService.getSeekTo() == 0) {
                    start.setImageResource(R.mipmap.icon_pause);
                    myService.start();
                } else if (myService.isPlay()) {
                    myService.pause();
                    start.setImageResource(R.mipmap.icon_play);
                } else {
                    myService.resume();
                    start.setImageResource(R.mipmap.icon_pause);
                }
                break;
            case R.id.top:
                start.setImageResource(R.mipmap.icon_pause);
                myService.top();
                break;
            case R.id.next:
                start.setImageResource(R.mipmap.icon_pause);
                myService.next();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
    }
}
