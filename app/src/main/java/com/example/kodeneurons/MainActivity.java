package com.example.kodeneurons;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ValueCallback<Uri[]> filePathCallback; // Handles file chooser result

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);

        // WebView Settings
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);        // Enable JavaScript
        settings.setDomStorageEnabled(true);        // Enable local storage (HTML5)
        settings.setAllowFileAccess(true);          // Allow file access
        settings.setAllowContentAccess(true);       // Allow content access

        // Ensures links open inside the WebView instead of an external browser
        webView.setWebViewClient(new WebViewClient());

        // Handle file input and permission requests in WebView
        webView.setWebChromeClient(new WebChromeClient() {

            /**
             * Handles <input type="file"> from the web page.
             */
            @Override
            public boolean onShowFileChooser(
                    WebView webView,
                    ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams
            ) {
                MainActivity.this.filePathCallback = filePathCallback;
                fileChooserLauncher.launch(fileChooserParams.createIntent());
                return true;
            }

            /**
             * Auto–grants permission requests (e.g., camera / microphone)
             * coming from the WebView.
             */
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        // Request permissions for camera and media
        requestPermissionsIfNeeded();

        // Load your Web App
        webView.loadUrl("https://kodeneurons.tech");
    }

    /**
     * Handles result from file picker (image / file upload)
     */
    private final ActivityResultLauncher<Intent> fileChooserLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (filePathCallback == null) return;

                        Uri[] results = null;

                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            if (uri != null) {
                                results = new Uri[]{uri};
                            }
                        }

                        filePathCallback.onReceiveValue(results);
                        filePathCallback = null;
                    }
            );

    /**
     * Requests camera + media permissions if not already granted.
     */
    private void requestPermissionsIfNeeded() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
                break;
            }
        }
    }

    /**
     * Enables WebView back navigation using the device back button.
     */
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
