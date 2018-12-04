package com.invengo.rfid.airhubhd;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.invengo.rfid.airhubhd.entity.Web;
import com.invengo.rfid.airhubhd.enums.EmUh;
import com.invengo.rfid.airhubhd.enums.EmUrl;

import tk.ziniulian.util.AdrSys;
import tk.ziniulian.util.Str;

public class Ma extends Activity {
	private Web w = new Web(this);
	private WebView wv;
	private Handler uh = new UiHandler();
	private String ver = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); // 全屏、不锁屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD, WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); // 不锁屏
		setContentView(R.layout.activity_ma);
		ver = AdrSys.getVerNam(this);

		// 数据库初始化
		w.initDb();
		// 读写器初始化
//		w.initRd();

		// 页面设置
		wv = (WebView)findViewById(R.id.wv);
		WebSettings ws = wv.getSettings();
		ws.setDefaultTextEncodingName("UTF-8");
		ws.setJavaScriptEnabled(true);
		wv.addJavascriptInterface(w, "rfdo");

		sendUrl(EmUrl.Transition);
	}

	@Override
	protected void onResume() {
		w.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		w.close();
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_SOFT_RIGHT:
				if (event.getRepeatCount() == 0) {
					EmUrl e = getCurUi();
					if (e != null) {
						switch (getCurUi()) {
							case ScanTt:
							case ScanTmpTt:
								w.rfidScan();
								break;
							case QrTt:
								w.qrScan();
								break;
						}
					}
				}
				return true;
			case KeyEvent.KEYCODE_BACK:
				EmUrl e = getCurUi();
				if (e != null) {
					switch (e) {
						case Exit:
						case Err:
							return super.onKeyDown(keyCode, event);
						default:
							sendUrl(EmUrl.Back);
							break;
					}
				} else {
					wv.goBack();
				}
				return true;
			default:
				return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_SOFT_RIGHT:
				w.rfidStop();
				w.qrStop();
				return true;
			default:
				return super.onKeyUp(keyCode, event);
		}
	}

	public String getVer() {
		return ver;
	}

	// 获取当前页面信息
	private EmUrl getCurUi () {
		try {
			return EmUrl.valueOf(wv.getTitle());
		} catch (Exception e) {
			return null;
		}
	}

	// 页面跳转
	public void sendUrl (String url) {
//		Log.i("---", url);
		uh.sendMessage(uh.obtainMessage(EmUh.Url.ordinal(), 0, 0, url));
	}

	// 页面跳转
	public void sendUrl (EmUrl e) {
		sendUrl(e.toString());
	}

	// 页面跳转
	public void sendUrl (EmUrl e, String... args) {
		sendUrl(Str.meg(e.toString(), args));
	}

	// 发送页面处理消息
	public void sendUh (EmUh e) {
		uh.sendMessage(uh.obtainMessage(e.ordinal()));
	}

	// 页面处理器
	private class UiHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			EmUh e = EmUh.values()[msg.what];
			switch (e) {
				case Url:
					wv.loadUrl((String)msg.obj);
					break;
				case Connected:
					if (getCurUi() == EmUrl.Err) {
						wv.goBack();
					}
				default:
					break;
			}
		}
	}
}
