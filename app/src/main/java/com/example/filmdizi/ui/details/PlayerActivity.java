package com.example.filmdizi.ui.details;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.example.filmdizi.R;

public class PlayerActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        webView = findViewById(R.id.webView_player);

        // Gelen URL'i al
        String videoUrl = getIntent().getStringExtra("VIDEO_URL");

        // Video oynatıcılar için hayati ayarlar
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // PlayIMDB'nin çalışması için şart
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        // Uygulama dışına (Chrome'a) atmasını engelle, içerde aç
        webView.setWebViewClient(new WebViewClient());
        // Videoların tam ekran vs. özelliklerini desteklemesi için
        webView.setWebChromeClient(new WebChromeClient());

        // Linki yükle
        if (videoUrl != null) {
            webView.loadUrl(videoUrl);
        }
    }

    // Kullanıcı geri tuşuna basarsa direkt çıkmasın, web sayfasında geriye gitsin
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}