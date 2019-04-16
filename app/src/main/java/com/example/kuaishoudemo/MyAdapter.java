package com.example.kuaishoudemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Harry on 2019/4/15.
 * desc:
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

    private List<String> datas;
    private Context context;
    private OnClickListener onClickListener;

    public MyAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
        Glide.with(context).load(datas.get(i)).into(myViewHolder.imageView);
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(v,myViewHolder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    public interface OnClickListener {
        void onClick(View v,int position);
    }
}
