package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.PrintStreamPrinter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.api.ApiClient;
import com.example.newsapp.api.ApiInterface;
import com.example.newsapp.models.Article;
import com.example.newsapp.models.News;

import java.util.ArrayList;
import java.util.List;
import  android.widget.SearchView;
public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "76b4cb18563d4c3d89d46849da05e27c";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private TextView topHeadline;
    private String TAG = MainActivity.class.getSimpleName();
private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        topHeadline=findViewById(R.id.topheadelines);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        onLoadingSwipeRefresh("");
    }

    public void LoadJson(final String keyword) {
topHeadline.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String country = Utils.getCountry();
        String language = Utils.getLanguage();

        Call<News> call;
        if (keyword.length() > 0) {
            call = apiInterface.getNewsSearch(keyword, language, "publishedAt", API_KEY);
        } else {
            call = apiInterface.getNews(country, API_KEY);
        }
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null) {
                    if (!articles.isEmpty()) {
                        articles.clear();
                    }
                    articles = response.body().getArticle();
                    adapter = new Adapter(articles, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initListener();
                     topHeadline.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
                else
                    {
                    topHeadline.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "No Result", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
swipeRefreshLayout.setRefreshing(false);
                topHeadline.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initListener()
    {
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent= new Intent(MainActivity.this,NewsDetailActivity.class);
                Article article= articles.get(position);
                intent.putExtra("url",article.getUrl());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("img",article.getUrlToImage());
                intent.putExtra("date",article.getPublishedAt());
                intent.putExtra("source",article.getSource().getName());
                intent.putExtra("author",article.getAuthor());


                startActivity(intent);

            }
        });
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), MainActivity.class)));

            searchView.setQueryHint("Search latest  News...");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    if (s.length() > 2)
                   onLoadingSwipeRefresh(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String text) {
                    return false;
                }
            });
            searchMenuItem.getIcon().setVisible(false, false);
        }
            return super.onCreateOptionsMenu(menu);

        }

    @Override
    public void onRefresh() {
        LoadJson("");
    }
    private void onLoadingSwipeRefresh(final String keyword) {
    swipeRefreshLayout.post(
            new Runnable() {
                @Override
                public void run() {
                    LoadJson(keyword);
                }
            }
    );
    }
}
