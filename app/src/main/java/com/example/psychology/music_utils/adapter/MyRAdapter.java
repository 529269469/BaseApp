package com.example.psychology.music_utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.psychology.R;
import com.example.psychology.music_utils.Modle;

import java.util.ArrayList;

/**
 * Created by 52926 on 2017/4/28.
 */

public class MyRAdapter extends RecyclerView.Adapter<MyRAdapter.MyViewHolder> {
    private OnItemClickliner onItemClickliner;

    public void setOnItemClickliner(OnItemClickliner onItemClickliner) {
        this.onItemClickliner = onItemClickliner;
    }

    public interface OnItemClickliner{
        public void onItemclick(int p);
    }


    private ArrayList<Modle> list;
    private Context context;

    public MyRAdapter(ArrayList<Modle> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.f1_item,null);
        RecyclerView.LayoutParams params=new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        );
        view.setLayoutParams(params);
        MyViewHolder myViewHolder=new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.position=position;
        holder.textView.setText(list.get(position).getName());
        holder.textView2.setText(list.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        int position;
        LinearLayout linearLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.f1_item_text1);
            textView2= (TextView) itemView.findViewById(R.id.f1_item_text2);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.f1_item);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null!=onItemClickliner){
                        onItemClickliner.onItemclick(position);
                    }
                }
            });
        }
    }
}
