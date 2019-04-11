package com.aks.theindiannews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ListItemClickListener {
    private  RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView myrecyclerview;
    String link;
    TextView searchEmpty;
    int flag=0;
    ArrayList<News> newsArrayList = new ArrayList<>();
    ArrayList<News> newsList = new ArrayList<>();
    Realm mRealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
//       android.support.v7.widget.Toolbar toolbarTop =  findViewById(R.id.toolbar);
//        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
//        setSupportActionBar(toolbarTop);
//        mTitle.setText(toolbarTop.getTitle());
//
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(" The Indian News ");
        mRealm = Realm.getDefaultInstance();
        Realm.init(getApplicationContext());
        readFromRealm();
        myrecyclerview= findViewById(R.id.recyclerviewall);
        searchEmpty = findViewById(R.id.searchempty);
        recyclerViewAdapter= new RecyclerViewAdapter(this,newsArrayList,this);
//        myrecyclerview.setHasFixedSize(true);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        myrecyclerview.setAdapter(recyclerViewAdapter);
        com.github.clans.fab.FloatingActionButton floatingActionButtonRefresh = findViewById(R.id.fab_refresh);
        floatingActionButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                fetchJSON();
            }
        });

    }
    @Override
    public void onListItemClick(final int itemIndex, String Newslink) {

        Realm mRealm;
        mRealm = Realm.getDefaultInstance();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<News> results = realm.where(News.class).findAll();
                link = results.get(itemIndex).getUrl();

                Intent clickIntent = new Intent(MainActivity.this,DetailActivity.class);
                clickIntent.putExtra("itemindex",itemIndex);
                startActivity(clickIntent);
            }
        });


    }

    private void fetchJSON() {

        /*
         * Fetching fresh data from API and updating realm database
         * */
        String url = getString(R.string.url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, String.format(url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        JSONArray newsArray = null;
                        JSONObject Objecct = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "json error 1" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        try {
//                    Objecct = jsonObject.getJSONObject("articles");
                            newsArray = jsonObject.getJSONArray("articles");
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "json error 2" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        for (int j = 0; j < newsArray.length(); j++) {
                            String author = null;
                            String title = null;
                            String description = null;
                            String url= null;
                            String urlToImg= null;
                            String publishedAt = null;
                            String contennt = null;
                            JSONObject obj = null;
                            try {
                                obj = newsArray.getJSONObject(j);

// Get Author name
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "json error 3" + e.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            try {
                                author=obj.getString("author");
// Get title
                                title = obj.getString("title");
//Get description,News Url, Image url and time and content
                                description = obj.getString("description");
                                url = obj.getString("url");
                                urlToImg = obj.getString("urlToImage");
                                publishedAt =obj.getString("publishedAt");
                                contennt = obj.getString("content");

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "json error4" + e.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            News news = new News();
                            news.setAuthor(author);
                            news.setTitle(title);
                            news.setDescription(description);
                            news.setUrl(url);
                            news.setUrlToImg(urlToImg);
                            news.setPublishedAt(publishedAt);
                            news.setContennt(contennt);
                            newsList.add(news);

                        }

                        newsArrayList.clear();
                        addDataToRealm(newsList);

                       updateList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "volleyError" + error.toString(),
                        Toast.LENGTH_SHORT).show();
                flag = 2;
                showNewActivity(flag);

            }
        });

        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void updateList(){
        mRealm = Realm.getDefaultInstance();
        readFromRealm();
        myrecyclerview= findViewById(R.id.recyclerviewall);
        searchEmpty = findViewById(R.id.searchempty);
        recyclerViewAdapter= new RecyclerViewAdapter(this,newsArrayList,this);
//        myrecyclerview.setHasFixedSize(true);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        myrecyclerview.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
        Toast.makeText(this,"News Updated",Toast.LENGTH_LONG).show();
    }
    private void showNewActivity(int flag) {
        switch (flag) {
            case 1:

                Intent i = new Intent(this, MainActivity.class);
                Bundle bundle = new Bundle();


                startActivity(i);
                finish();

                break;
            case 2:
                Intent i1 = new Intent(this, ErrorActivity.class);
                i1.putExtra("flag", flag);
                startActivity(i1);

                break;
            case 3:

                Intent i2 = new Intent(this, ErrorActivity.class);
                i2.putExtra("flag", flag);
                startActivity(i2);
                break;
        }
    }

    private void addDataToRealm(final ArrayList<News> newsArrayList) {

        Realm realm = null;
        Realm.init(getApplicationContext());

        try {

            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {


                    try {

                        int i;
                        for (i = 0; i < newsArrayList.size(); i++) {
                            newsArrayList.get(i).news_author = newsArrayList.get(i).getAuthor();
                            newsArrayList.get(i).news_title = newsArrayList.get(i).getTitle();
                            newsArrayList.get(i).news_description = newsArrayList.get(i).getDescription();
                            newsArrayList.get(i).news_url = newsArrayList.get(i).getUrl();
                            newsArrayList.get(i).news_urlToImg = newsArrayList.get(i).getUrlToImg();
                            newsArrayList.get(i).news_publishedAt = newsArrayList.get(i).getPublishedAt();
                            newsArrayList.get(i).news_contennt = newsArrayList.get(i).getContennt();
                            realm.insertOrUpdate(newsArrayList.get(i));
                        }

                    } catch (RealmException e) {
                        Toast.makeText(getApplicationContext(),
                                "Realm error + " + e.toString(), Toast.LENGTH_SHORT).show();
                        flag = 3;
                    }

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {

            // super.onBackPressed();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Do you want to Exit?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            moveTaskToBack(true);
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();


        } else {
            getFragmentManager().popBackStack();
        }

    }

    private void readFromRealm() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<News> results = realm.where(News.class).findAll();
                newsArrayList.addAll(results);

            }
        });
    }


}
