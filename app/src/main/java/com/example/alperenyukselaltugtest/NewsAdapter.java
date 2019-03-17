package com.example.alperenyukselaltugtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alperenyukselaltugtest.R;
import com.example.alperenyukselaltugtest.model.DataNews;

import java.util.Collections;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataNews> data= Collections.emptyList();
    DataNews current;
    int currentPos=0;

    public NewsAdapter(Context context, List<DataNews> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.activity_gundem_basliklar, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder= (MyHolder) holder;
        DataNews current=data.get(position);
        myHolder.title.setText(current.title);
        myHolder.mtitle.setText(current.mtitle);

        Glide.with(context).load(current.imageUrl)
                .into(myHolder.imageUrl);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        ImageView imageUrl;
        TextView title;
        TextView mtitle;

        public MyHolder(View itemView) {
            super(itemView);
            mtitle= (TextView) itemView.findViewById(R.id.textViewNewsTitle);
            title= (TextView) itemView.findViewById(R.id.textViewNewsKind);
            imageUrl= (ImageView) itemView.findViewById(R.id.imageViewNewsSmall);

        }

    }

}
