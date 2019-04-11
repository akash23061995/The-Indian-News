package com.aks.theindiannews;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context; String  newslink;
    ArrayList<News> mData= new ArrayList<>();;
    String link;
    public Activity activity;
     private ListItemClickListener mOnClickListener;
    public interface ListItemClickListener{
        void onListItemClick(int itemIndex, String link);
    }
    public RecyclerViewAdapter(Context context, ArrayList<News> mData, ListItemClickListener listener) {
        this.context = context;
        this.mData = mData;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v=LayoutInflater.from(context).inflate(R.layout.row,viewGroup,false);
        MyViewHolder myViewHolder= new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(mData.get(position).getTitle());
        String authorr= mData.get(position).getAuthor();
        if(authorr != null && !authorr.isEmpty()){holder.author.setText(authorr);}
        else {
            holder.author.setText("");}
        String img = mData.get(position).getUrlToImg();

        if(img.length()<2){
            holder.img.setImageResource(R.drawable.def16);
        }

        else
        Picasso.with(context).load(mData.get(position).getUrlToImg()).into(holder.img);
//
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public   class MyViewHolder extends  RecyclerView.ViewHolder   implements View.OnClickListener {

        private TextView title,author;
        private ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.newstitle);
            author=itemView.findViewById(R.id.author);
            img= itemView.findViewById(R.id.imagee);


        itemView.setOnClickListener(this);

        }







        @Override
        public void onClick(View v) {

            final int position= getAdapterPosition();

            mOnClickListener.onListItemClick(position,newslink);

        }
    }
    public  void updateList(List<News>  newList){
        mData= new ArrayList<>();
        mData.addAll(newList);
        notifyDataSetChanged();
    }


}
