package com.aks.theindiannews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
int position_of_news;
Realm mRealm;
    String author,title,description,url,urlToImage,publishedAt,content;
    ArrayList<News> newsArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent clickIntent = getIntent();
       position_of_news =  clickIntent.getIntExtra("itemindex",0);
        mRealm = Realm.getDefaultInstance();
        Realm.init(getApplicationContext());
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
//       android.support.v7.widget.Toolbar toolbarTop =  findViewById(R.id.toolbar);
//        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
//        setSupportActionBar(toolbarTop);
//        mTitle.setText(toolbarTop.getTitle());
//
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(" The Indian News ");
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<News> results = realm.where(News.class).findAll();
                newsArrayList.addAll(results);

            }
        });

     author = newsArrayList.get(position_of_news).getAuthor();
     title = newsArrayList.get(position_of_news).getTitle();
     description = newsArrayList.get(position_of_news).getDescription();
     url = newsArrayList.get(position_of_news).getUrl();
     urlToImage = newsArrayList.get(position_of_news).getUrlToImg();
     publishedAt = newsArrayList.get(position_of_news).getPublishedAt();
     content = newsArrayList.get(position_of_news).getContennt();
        Button visitLink = findViewById(R.id.linkbutton);
        Button sharebutton = findViewById(R.id.sharebutton);
        visitLink.setOnClickListener(this);
        sharebutton.setOnClickListener(this);
        ImageView image = findViewById(R.id.imagedetail);
        TextView titlenews = findViewById(R.id.detailtitle);
        TextView authorr = findViewById(R.id.authordetail);
        TextView time = findViewById(R.id.publishedat);
        TextView contenttt= findViewById(R.id.contentdetail);
        if(author!= null && !author.isEmpty()){authorr.setText(author);}
        else{authorr.setText(" "); }
        if(title!= null && !title.isEmpty()){titlenews.setText(title);}else{titlenews.setText(" "); }
        if(publishedAt!= null && !publishedAt.isEmpty()){time.setText(publishedAt);}else{time.setText(" "); }
        if(description!= null && !description.isEmpty()){contenttt.setText(description);}else{contenttt.setText(" "); }
        if(urlToImage.length()<2){
            image.setImageResource(R.drawable.defaultimage);
        }

        else
            Picasso.with(this).load(urlToImage).into(image);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.linkbutton:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.sharebutton:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "For more Information Click here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
                startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                break;

        }
    }
}
