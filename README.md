# 🌐 Convert Any Website Into an Android App (WebView)

This guide will help you convert **any website** into an **Android App** using **Android Studio** and **WebView**.

---

## 🛠 Requirements

* Android Studio (latest version)
* Basic Java knowledge
* A website URL

---

## 🚀 Step 1: Create a New Android Project

1. Open **Android Studio**
2. Click **New Project**
3. Select **Empty Views Activity**
4. Click **Next**
5. Fill project details:

   * App Name: Anything you like
   * Language: **Java**
   * Minimum SDK: **API 21 or above**
6. Click **Finish**

---

## 🧩 Step 2: Edit `activity_main.xml`

1. Go to:

   ```
   app > res > layout > activity_main.xml
   ```
2. **Delete all existing code**
3. Paste the code below:

```xml
<?xml version="1.0" encoding="utf-8"?>
<WebView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/webview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_marginTop="40dp" />
```

---

## ☕ Step 3: Edit `MainActivity.java`

1. Go to:

   ```
   app > java > your_package_name > MainActivity.java
   ```
2. **Keep the first line (your package name)**
3. Delete everything else
4. Paste the code below:

```java
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
    private ValueCallback<Uri[]> filePathCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {

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

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        requestPermissionsIfNeeded();

        // 🔴 REPLACE WITH YOUR WEBSITE URL
        webView.loadUrl("https://yourwebsite.com");
    }

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

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
```

📌 **Change the website URL** to your own.

---

## 🌐 Step 4: Add Internet Permission

1. Open:

   ```
   app > manifests > AndroidManifest.xml
   ```
2. Add this line **above `<application>`**:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 🎨 Step 5: Change App Logo (Optional but Recommended)

1. Go to:

   ```
   app > res
   ```
2. Right-click **res**
3. Select **New > Image Asset**
4. Choose:

   * Icon Type: **Launcher Icons**
   * Asset Type: **Image**
5. Upload your logo (PNG recommended)
6. Click **Next → Finish**

✔️ Your app icon is now changed.

---

## ▶️ Step 6: Run the App

* Click **Run ▶**
* Choose an emulator or physical device
* Your website will open as an Android app 🎉

---

## ✅ Features Included

* WebView with JavaScript
* File upload support
* Camera access
* Back button navigation
* Modern permissions handling

---

## 💡 You’re All Set!

You’ve successfully converted a website into an Android app 🚀
Perfect for portfolios, startups, or client projects.
