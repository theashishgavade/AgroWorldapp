package com.project.agroworld.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.project.agroworld.R;
import com.project.agroworld.db.PreferenceHelper;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.CustomMultiColorProgressBar;

public class NewsFragment extends Fragment {

    PreferenceHelper preferenceHelper;
    private CustomMultiColorProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = new CustomMultiColorProgressBar(getContext(), getString(R.string.loader_message));
        preferenceHelper = PreferenceHelper.getInstance(getContext());
        progressBar.showProgressBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean selectedLanguage = preferenceHelper.getData(Constants.HINDI_KEY);
        WebView webView = view.findViewById(R.id.newsWebView);
        webView.setWebViewClient(new AgroNews());
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setBuiltInZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);
        settings.setUserAgentString("Android");
        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        settings.setUseWideViewPort(true);
        settings.setAppCacheEnabled(true);
        webView.clearCache(true);
        if (selectedLanguage) {
            webView.loadUrl(Constants.HINDI_NEWS_WEB_URL);
        } else {
            webView.loadUrl(Constants.ENGLISH_NEWS_WEB_URL);
        }

        webView.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
            return false;
        });
    }

    private class AgroNews extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (view.getProgress() == 100){
                progressBar.hideProgressBar();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        progressBar.hideProgressBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressBar.hideProgressBar();
    }


}