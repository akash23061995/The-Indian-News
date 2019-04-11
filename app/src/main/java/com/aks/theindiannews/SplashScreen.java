package com.aks.theindiannews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import java.util.concurrent.ThreadLocalRandom;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.exceptions.RealmException;

public class SplashScreen extends AppCompatActivity {
    ArrayList<News> newsList = new ArrayList<>();
    Realm mRealm;
    int flagNextActivity = 0;
    Animation animation;
    ImageView logo_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Creating full screen activity for splash screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        Realm.init(getApplicationContext());
        mRealm = Realm.getDefaultInstance();

        MediaPlayer mPlayer = MediaPlayer.create(SplashScreen.this, R.raw.startup);
        mPlayer.start();
        ImageView image = findViewById(R.id.logo_id);
        int randomNum = ThreadLocalRandom.current().nextInt(1, 26 + 1);
        switch (randomNum){
            case 1:
                image.setImageResource(R.drawable.def1);
                break;
            case 2:
                image.setImageResource(R.drawable.def2);
                break;
            case 3:
                image.setImageResource(R.drawable.def3);
                break;
            case 4:
                image.setImageResource(R.drawable.def4);
                break;
            case 5:
                image.setImageResource(R.drawable.def5);
                break;
            case 6:
                image.setImageResource(R.drawable.def6);
                break;
            case 7:
                image.setImageResource(R.drawable.def7);
                break;
            case 8:
                image.setImageResource(R.drawable.def8);
                break;
            case 9:
                image.setImageResource(R.drawable.def9);
                break;
            case 10:
                image.setImageResource(R.drawable.def10);
                break;
            case 11:
                image.setImageResource(R.drawable.def11);
                break;
            case 12:
                image.setImageResource(R.drawable.def12);
                break;
            case 13:
                image.setImageResource(R.drawable.def13);
                break;
            case 14:
                image.setImageResource(R.drawable.def14);
                break;
            case 15:
                image.setImageResource(R.drawable.def15);
                break;
            case 16:
                image.setImageResource(R.drawable.def16);
                break;
            case 17:
                image.setImageResource(R.drawable.def18);
                break;
            case 18:
                image.setImageResource(R.drawable.def19);
                break;
            case 19:
                image.setImageResource(R.drawable.def20);
                break;
            case 20:
                image.setImageResource(R.drawable.def21);
                break;
            case 21:
                image.setImageResource(R.drawable.def22);
                break;
            case 22:
                image.setImageResource(R.drawable.def23);
                break;
            case 23:
                image.setImageResource(R.drawable.def24);
                break;
            case 24:
                image.setImageResource(R.drawable.def25);
                break;
            case 25:
                image.setImageResource(R.drawable.def26);
                break;
            case 26:
                image.setImageResource(R.drawable.def27);
                break;


        }


        String url = getString(R.string.url);
        StringRequest requestApiData = new StringRequest(Request.Method.GET, String.format(url), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*
                This method is called if the volley networking call is successful.
                The call is asynchronous.
                 */

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

                deleteRealmIfExists();
                addDataToRealm(newsList);
                flagNextActivity = 1;
                showNextActivity(flagNextActivity);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                /**
                 * This method is called when there is error in volley networking call.
                 */
                flagNextActivity = 2;
                showNextActivity(flagNextActivity);

            }
        });

        //Getting instance of Singleton class to add request to the request queue.
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(requestApiData);

    }


    private void showNextActivity(int flagNextActivity) {

        switch (flagNextActivity){
            case 1: //The case of success.
                Intent successIntent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(successIntent);

                break;

            case 2:
                Intent errorIntent = new Intent(SplashScreen.this, ErrorActivity.class);
                errorIntent.putExtra("flag", flagNextActivity);
                startActivity(errorIntent);

                break;


            case 3:
                Intent errorIntent2 = new Intent(SplashScreen.this, ErrorActivity.class);
                errorIntent2.putExtra("flag", flagNextActivity);
                startActivity(errorIntent2);

        }
    }

    private void deleteRealmIfExists() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();

        realm.commitTransaction();
    }

    private void addDataToRealm(final ArrayList<News> newsList){

        Realm realm = null;
        Realm.init(getApplicationContext());

        try {

            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {


                    try {

                        int i = 0;
                        for (i = 0; i < newsList.size(); i++) {
                            newsList.get(i).news_author = newsList.get(i).getAuthor();
                            newsList.get(i).news_title= newsList.get(i).getTitle();
                            newsList.get(i).news_description = newsList.get(i).getDescription();
                            newsList.get(i).news_url = newsList.get(i).getUrl();
                            newsList.get(i).news_urlToImg = newsList.get(i).getUrlToImg();
                            newsList.get(i).news_publishedAt = newsList.get(i).getPublishedAt();
                            newsList.get(i).news_contennt = newsList.get(i).getContennt();
                            realm.insertOrUpdate(newsList.get(i));
                        }

                    } catch (RealmException e) {
                        Toast.makeText(getApplicationContext(),
                                "Realm error + "+ e.toString(), Toast.LENGTH_SHORT).show();
                        flagNextActivity= 3;
                    }

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }


    }

}
