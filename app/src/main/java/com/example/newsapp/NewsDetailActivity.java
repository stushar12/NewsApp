package com.example.newsapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
ImageView imageView,img,img2,img3;
TextView appbar_title,appbar_subtitle,date,time,title;
 boolean isHideToolBarView=false;
 FrameLayout date_behavior;
 ProgressBar progressBar;
 RelativeLayout relativeLayout,relativeLayout2,relativeLayout3;
 LinearLayout titleAppbar;
AppBarLayout appBarLayout;
 Toolbar toolbar;

public String mUrl,mImg,mTitle,mDate,mSource,mAuthor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      this.getSupportActionBar().setTitle("");
   this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
final CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
collapsingToolbarLayout.setTitle("");
relativeLayout=findViewById(R.id.headerContent);
relativeLayout2=findViewById(R.id.relative2);
relativeLayout3=findViewById(R.id.relative3);
progressBar=findViewById(R.id.progress_bar);
appBarLayout=findViewById(R.id.appbar);
appBarLayout.addOnOffsetChangedListener(this);
date_behavior=findViewById(R.id.date_behavior);
titleAppbar=findViewById(R.id.title_appbar);
imageView=findViewById(R.id.backdrop);
img=findViewById(R.id.img);
img2=findViewById(R.id.img2);
img3=findViewById(R.id.img3);
appbar_title=findViewById(R.id.title_on_appbar);
appbar_subtitle = findViewById(R.id.subtitle_on_appbar);
date=findViewById(R.id.date);
time=findViewById(R.id.time);
title=findViewById(R.id.title);
Intent intent=getIntent();
        mUrl=intent.getStringExtra("url");
        mImg=intent.getStringExtra("img");
        mTitle=intent.getStringExtra("title");
        mDate=intent.getStringExtra("date");
        mSource=intent.getStringExtra("source");
        mAuthor=intent.getStringExtra("author");

        RequestOptions requestOptions =new RequestOptions();
       requestOptions.error(Utils.getRandomDrawbleColor());


        Glide.with(this).
                load(mImg).
                apply(requestOptions).
                transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        appbar_title.setText(mSource);
        appbar_subtitle.setText(mUrl);
        date.setText(Utils.DateFormat(mDate));
        title.setText(mTitle);


      String author=null;
        if(mAuthor!=null)
        {
            mAuthor="\u2022" + mAuthor;
        }
        else
        {
              author="";
        }
        time.setText(mSource + author +" \u2022 "+Utils.DateToTime(mDate));
        intiWebView(mUrl);
    }


   public void intiWebView(String url)
    {
        WebView webView=findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
       // webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll=appBarLayout.getTotalScrollRange();
        float percentage=(float)Math.abs(i)/(float) maxScroll;
        if(percentage == 1f && isHideToolBarView)
        {
            date_behavior.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolBarView = !isHideToolBarView;
        }
        else if(percentage < 1f && isHideToolBarView)
        {
            date_behavior.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            isHideToolBarView = !isHideToolBarView;
        }
        {

        }
    }
}
