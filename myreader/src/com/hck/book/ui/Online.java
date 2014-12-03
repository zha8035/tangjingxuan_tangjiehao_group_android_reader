package com.hck.book.ui;

import com.hck.test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Online	 extends Activity {
	public Intent intent;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.online);
			WebView webView = (WebView) findViewById(R.id.webView01);
			String url = "http://www.qidian.com/Default.aspx";
			webView.loadUrl(url);
		    webView.setWebViewClient(new WebViewClient(){       
                public boolean shouldOverrideUrlLoading(WebView view, String url) {       
                    view.loadUrl(url);       
                    return true;       
                }       
    });   

}
}