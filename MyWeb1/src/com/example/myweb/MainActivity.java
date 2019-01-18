package com.example.myweb;

import java.lang.reflect.Method;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
		private ImageView m_ivBackward;
		private ImageView m_ivForward;
		private ImageView m_ivRefresh;
		private WebView m_wvWebBrowser;
		private LinearLayout m_lytLoading;
		private FrameLayout fl_bodyLayout;
		private String m_strUrl = "";
		private String m_preUrl = "";
		
		public static final int TYPE_SHARE = 101;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			super.setContentView(R.layout.activity_main);
			initView();
			initWebView();
			initData();
		}

		private void initWebView() {
			WebSettings webSettings = m_wvWebBrowser.getSettings();
			webSettings.setSupportZoom(true);
			webSettings.setBuiltInZoomControls(false);
			webSettings.setJavaScriptEnabled(true);
			//webSettings.setPluginsEnabled(true);
			webSettings.setDefaultTextEncodingName("UTF-8");
			webSettings.setUseWideViewPort(false);
			webSettings.setLoadWithOverviewMode(true);
			// webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		}

		/**
		 * 判断状态
		 */
		private void judgeWebViewState() {
			if (m_wvWebBrowser.canGoBack()
					&& (!TextUtils.isEmpty(m_wvWebBrowser.getTitle()) && !m_wvWebBrowser
							.getTitle().equalsIgnoreCase("警告"))) {
				m_ivBackward.setEnabled(true);
				m_ivBackward.setImageResource(R.drawable.custom_browser_left);
			} else {
				m_ivBackward.setEnabled(false);
				m_ivBackward.setImageResource(R.drawable.no_browser_press);
			}

			if (m_wvWebBrowser.canGoForward()) {
				m_ivForward.setEnabled(true);
				m_ivForward.setImageResource(R.drawable.custom_browser_right);
			} else {
				m_ivForward.setEnabled(false);
				m_ivForward.setImageResource(R.drawable.no_browser_right_press);
			}
		}

		protected void initView() {
			m_ivBackward = (ImageView) findViewById(R.id.sbrowser_act_control_left);
			m_ivForward = (ImageView) findViewById(R.id.sbrowser_act_control_right);
			m_ivRefresh = (ImageView) findViewById(R.id.sbrowser_act_control_refresh);
			m_wvWebBrowser = (WebView) findViewById(R.id.sbrowser_act_web);
			m_lytLoading = (LinearLayout) findViewById(R.id.sbrowser_act_loading);
			fl_bodyLayout = (FrameLayout) findViewById(R.id.body_frame);

			m_ivBackward.setOnClickListener(this);
			m_ivForward.setOnClickListener(this);
			m_ivRefresh.setOnClickListener(this);
		}

		private Handler mHandler = new Handler();

		protected void initData() {
			
			//调入本地网页
			m_wvWebBrowser.loadUrl("file:///android_asset/html/index.html");
			//调入网站
			//m_wvWebBrowser.loadUrl("http://m.dianping.com/");

				
			m_lytLoading.setVisibility(View.VISIBLE);
			m_wvWebBrowser.setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					m_strUrl = url;
					view.loadUrl(m_strUrl);
					m_lytLoading.setVisibility(View.VISIBLE);
					return super.shouldOverrideUrlLoading(view, url);
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					
					judgeWebViewState();
					super.onPageFinished(view, url);
				}
			});
			m_wvWebBrowser.setWebChromeClient(new WebChromeClient() {

				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					if (newProgress >= 100) {
						m_lytLoading.setVisibility(View.GONE);
					}
					super.onProgressChanged(view, newProgress);
				}
			});
		}

		private String addUrlHttp(String paramUrl) {
			if (TextUtils.isEmpty(paramUrl)) {
				return "";
			}
			if (!paramUrl.contains("http://")&&!paramUrl.startsWith("https://")) {
				return "http://" + paramUrl;
			}
			
			return paramUrl;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.sbrowser_act_control_left:
				if (m_wvWebBrowser.canGoBack()) {
					m_wvWebBrowser.goBack();
				}
				break;
			case R.id.sbrowser_act_control_right:
				if (m_wvWebBrowser.canGoForward()) {
					m_wvWebBrowser.goForward();
				}
				break;
			case R.id.sbrowser_act_control_refresh:
				m_wvWebBrowser.reload();
				break;
			}
		}

		/**
		 * 调用WebView的隐藏方法
		 * 
		 * @param name
		 */
		private void callHiddenMethod(String name) {
			if (m_wvWebBrowser != null) {
				try {
					Method method = WebView.class.getMethod(name);
					method.invoke(m_wvWebBrowser);
				} catch (Exception e) {
				}
			}

		}

		@Override
		/**
		 * 对返回键进行处理，返回前一个页面
		 */
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 if (m_wvWebBrowser.canGoBack()
			 && event.getKeyCode() == KeyEvent.KEYCODE_BACK
			 && event.getRepeatCount() == 0) {
			 m_wvWebBrowser.goBack();
			 return true;
			 }
			return super.onKeyDown(keyCode, event);
		}

		@Override
		protected void onPause() {
			callHiddenMethod("onPause");
			super.onPause();
		}

		@Override
		protected void onResume() {
			callHiddenMethod("onResume");
			super.onResume();
		}

		@Override
		protected void onDestroy() {
			CookieSyncManager.createInstance(this);
			CookieSyncManager.getInstance().startSync();
			CookieManager.getInstance().removeSessionCookie();
			fl_bodyLayout.removeView(m_wvWebBrowser);
			m_wvWebBrowser.removeAllViews();
			m_wvWebBrowser.clearCache(true);
			m_wvWebBrowser.clearHistory();
			m_wvWebBrowser.destroy();
			m_wvWebBrowser.setVisibility(View.GONE);
			
			super.onDestroy();
		}

}
