package dev.flarialvoice.starter;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions(
            new String[]{Manifest.permission.RECORD_AUDIO},
            1
        );

        webView = new WebView(this);
        setContentView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                runOnUiThread(() -> {
                    for (String resource : request.getResources()) {
                        if (PermissionRequest.RESOURCE_AUDIO_CAPTURE.equals(resource)
                                && checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                                == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            request.grant(new String[]{
                                PermissionRequest.RESOURCE_AUDIO_CAPTURE
                            });
                            return;
                        }
                    }
                    request.deny();
                });
            }
        });

        webView.loadUrl("https://meet.jit.si/FlarialVoiceRoom");
    }
}