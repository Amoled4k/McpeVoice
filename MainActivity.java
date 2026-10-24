package dev.flarialvoice.starter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.net.URLEncoder;

public class MainActivity extends Activity {
  private WebView web;
  private PermissionRequest pending;
  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    LinearLayout layout = new LinearLayout(this); layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(24,24,24,24);
    TextView info = new TextView(this);
    info.setText("Flarial Voice Starter (Prototyp)\nAlle Teilnehmer brauchen denselben Raumnamen. Kein Proximity Chat. Keine Verbindung zur Flarial API. Sprachchat kann im Hintergrund pausieren.");
    EditText room = new EditText(this); room.setSingleLine(true); room.setHint("Privater Raumname (mind. 8 Zeichen)");
    Button join = new Button(this); join.setText("Sprachraum öffnen");
    web = new WebView(this);
    web.getSettings().setJavaScriptEnabled(true);
    web.getSettings().setDomStorageEnabled(true);
    web.getSettings().setMediaPlaybackRequiresUserGesture(false);
    web.setWebChromeClient(new WebChromeClient() {
      @Override public void onPermissionRequest(PermissionRequest request) {
        runOnUiThread(() -> {
          boolean audio = false;
          for (String r : request.getResources()) if (PermissionRequest.RESOURCE_AUDIO_CAPTURE.equals(r)) audio = true;
          if (!audio || !request.getOrigin().toString().startsWith("https://meet.jit.si")) { request.deny(); return; }
          if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) request.grant(new String[]{PermissionRequest.RESOURCE_AUDIO_CAPTURE});
          else { pending = request; requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 9); }
        });
      }
    });
    join.setOnClickListener(v -> {
      String name = room.getText().toString().trim();
      if (!name.matches("[A-Za-z0-9_-]{8,64}")) { room.setError("8 bis 64 Zeichen: Buchstaben, Zahlen, _ oder -"); return; }
      // Jitsi is an external service. Users must agree to its terms/privacy policy.
      web.loadUrl("https://meet.jit.si/FlarialVoice-" + name + "#config.prejoinPageEnabled=false");
    });
    layout.addView(info); layout.addView(room); layout.addView(join);
    layout.addView(web, new LinearLayout.LayoutParams(-1,0,1));
    setContentView(layout);
  }
  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
    super.onRequestPermissionsResult(requestCode, permissions, results);
    if (requestCode == 9 && pending != null) {
      if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) pending.grant(new String[]{PermissionRequest.RESOURCE_AUDIO_CAPTURE});
      else pending.deny();
      pending = null;
    }
  }
  @Override public void onBackPressed() { if (web.canGoBack()) web.goBack(); else super.onBackPressed(); }
  @Override protected void onDestroy() { web.destroy(); super.onDestroy(); }
}
