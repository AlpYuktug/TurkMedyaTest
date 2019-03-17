package com.example.alperenyukselaltugtest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.alperenyukselaltugtest.model.DataNews;
import com.example.alperenyukselaltugtest.model.DataSliders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String TAG = MainActivity.class.getSimpleName();
    public static String urlnews = "http://webservice.aksam.com.tr/gunes/homepagetest.asp";
    public RecyclerView RecylerViewNews,RecylerViewSlider;
    public NewsAdapter newsAdapter;
    public SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncFetch().execute();
    }

    public class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        public void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        public String doInBackground(String... params) {
            try {

                url = new URL("http://webservice.aksam.com.tr/gunes/homepagetest.asp");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());

                } else {

                    return ("Basarisiz");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        public void onPostExecute(String result) {

            pdLoading.dismiss();
            List<DataNews> data=new ArrayList<>();
            List<DataSliders> datas = new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONObject jsonObj = new JSONObject(result);
                JSONArray d = jsonObj.getJSONArray("data");

                for (int i = 0; i < d.length(); i++) {

                    JSONObject dd = d.getJSONObject(i);
                    String sectionType = dd.getString("sectionType");
                    HashMap<String, String> allsection = new HashMap<>();
                    allsection.put("sectionType", sectionType);

                    if (sectionType.contains("NEWS")) {
                        JSONArray itemList = dd.getJSONArray("itemList");

                        for (int j = 0; j < itemList.length(); j++) {

                            JSONObject item = itemList.getJSONObject(j);
                            JSONObject category = item.getJSONObject("category");

                            DataNews dataNews = new DataNews();
                            dataNews.imageUrl = item.getString("imageUrl");
                            dataNews.mtitle = item.getString("title");
                            dataNews.title = category.getString("title");
                            data.add(dataNews);
                        }
                    }

                    if (sectionType.contains("SWIPE")) {
                        JSONArray itemList = dd.getJSONArray("itemList");

                        for (int j = 0; j < itemList.length(); j++) {

                            JSONObject item = itemList.getJSONObject(j);

                            DataSliders dataSliders = new DataSliders();
                            dataSliders.imageUrl = item.getString("imageUrl");
                            datas.add(dataSliders);
                        }
                    }
                }

                RecylerViewNews = (RecyclerView)findViewById(R.id.RecylerViewNews);
                newsAdapter = new NewsAdapter(MainActivity.this, data);
                RecylerViewNews.setAdapter(newsAdapter);
                RecylerViewNews.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                RecylerViewSlider = (RecyclerView)findViewById(R.id.RecylerViewSlider);

                PagerSnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(RecylerViewSlider);
                RecylerViewSlider.addItemDecoration(new LinePagerIndicatorDecoration());

                sliderAdapter = new SliderAdapter(MainActivity.this, datas);
                RecylerViewSlider.setAdapter(sliderAdapter);
                RecylerViewSlider.setLayoutManager(layoutManager);


            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}