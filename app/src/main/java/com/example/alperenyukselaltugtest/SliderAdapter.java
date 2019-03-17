package com.example.alperenyukselaltugtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alperenyukselaltugtest.model.DataSliders;

import java.util.Collections;
import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataSliders> datas= Collections.emptyList();
    DataSliders current;
    int currentPos=0;

    public SliderAdapter(Context context, List<DataSliders> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.datas=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.activity_gundem_slider, parent,false);
        SliderAdapter.MyHolder holders=new SliderAdapter.MyHolder(view);
        return holders;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, int position) {

        SliderAdapter.MyHolder myHolder= (SliderAdapter.MyHolder) holders;
        DataSliders current=datas.get(position);
        Glide.with(context).load(current.imageUrl)
                .apply(new RequestOptions().override(1920, 1080))
                .into(myHolder.imageUrl);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView imageUrl;

        public MyHolder(View itemView) {
            super(itemView);
            imageUrl= (ImageView) itemView.findViewById(R.id.imageViewSlider);
        }

    }

}