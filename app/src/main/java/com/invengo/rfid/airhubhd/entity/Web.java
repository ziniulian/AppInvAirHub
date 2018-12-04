package com.invengo.rfid.airhubhd.entity;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.invengo.rfid.airhubhd.Ma;
import com.invengo.rfid.airhubhd.enums.EmUh;
import com.invengo.rfid.airhubhd.enums.EmUrl;

import tk.ziniulian.job.qr.EmQrCb;
import tk.ziniulian.job.qr.InfQrListener;
import tk.ziniulian.job.qr.xc2910.Qrd;
import tk.ziniulian.job.rfid.EmCb;
import tk.ziniulian.job.rfid.EmPushMod;
import tk.ziniulian.job.rfid.InfTagListener;
import tk.ziniulian.job.rfid.tag.T6C;
import tk.ziniulian.job.rfid.xc2910.Rd;
import tk.ziniulian.util.dao.DbLocal;
import tk.ziniulian.util.dao.WebSrv;

/**
 * 业务接口
 * Created by 李泽荣 on 2018/7/17.
 */

public class Web {
	private Rd rfd = new Rd();
	private Qrd qr = new Qrd();
	private Gson gson = new Gson();
	private DbLocal ldao = null;
	private WebSrv ws = null;
	private Ma ma;

	public Web (Ma m) {
		this.ma = m;
	}

	// 读写器设置
	public void initRd () {
		rfd.setPwd(new byte[] {0x20, 0x26, 0x31, 0x07});
		rfd.setHex(true);
		rfd.setPm(EmPushMod.Catch);
		rfd.setTagListenter(new InfTagListener() {
			@Override
			public void onReadTag(T6C bt, InfTagListener itl) {}

			@Override
			public void onWrtTag(T6C bt, InfTagListener itl) {
				ma.sendUrl(EmUrl.RfWrtOk);
			}

			@Override
			public void cb(EmCb e, String[] args) {
				// Log.i("--rfd--", e.name());
				switch (e) {
					case Scanning:
						ma.sendUrl(EmUrl.RfScaning);
						break;
					case Stopped:
						ma.sendUrl(EmUrl.RfStoped);
						break;
					case ErrWrt:
						ma.sendUrl(EmUrl.RfWrtErr);
						break;
					case ErrConnect:
						ma.sendUrl(EmUrl.Err);
						break;
					case Connected:
						ma.sendUh(EmUh.Connected);
						break;
				}
			}
		});
		rfd.init();
	}

	// 数据库初始化
	public void initDb() {
		ldao = new DbLocal(ma);
		if (ldao.kvGet("url") == null) {
			setUrl("192.169.0.35", "8888");
		} else {
			setUrl(null, null);
		}
	}

	// 二维码设置
	public void initQr() {
		qr.setQrListenter(new InfQrListener() {
			@Override
			public void onRead(String content) {
				ma.sendUrl(EmUrl.QrOnRead, gson.toJson(content));
			}

			@Override
			public void cb(EmQrCb e, String[] args) {
//				Log.i("--qr--", e.name());
				switch (e) {
					case ErrConnect:
						ma.sendUrl(EmUrl.Err);
						break;
					case Connected:
						ma.sendUh(EmUh.Connected);
						break;
				}
			}
		});
		qr.init();
	}

	public void open() {
		rfd.open();
		qr.open();
	}

	public void close() {
		rfd.close();
		qr.close();
	}

	public void qrDestroy() {
		qr.destroy();
	}

/*------------------- RFID ---------------------*/

	@JavascriptInterface
	public boolean isRfidBusy () {
		return rfd.isBusy();
	}

	@JavascriptInterface
	public void rfidScan() {
		rfd.scan();
	}

	@JavascriptInterface
	public void rfidStop() {
		rfd.stop();
	}

	@JavascriptInterface
	public void rfidWrt (String bankNam, String dat, String tid) {
		rfd.wrt(bankNam, dat, tid);
	}

	@JavascriptInterface
	public String rfidCatchScanning() {
		return rfd.catchScanning();
	}

	@JavascriptInterface
	public boolean setBank(String bankNam) {
		return rfd.setBank(bankNam);
	}

/*------------------- 二维码 ---------------------*/

	@JavascriptInterface
	public boolean isQrBusy() {
		return qr.isBusy();
	}

	@JavascriptInterface
	public void qrScan() {
		qr.scan();
	}

	@JavascriptInterface
	public void qrStop() {
		qr.stop();
	}

/*------------------- 数据库 ---------------------*/

	@JavascriptInterface
	public String kvGet(String k) {
		return ldao.kvGet(k);
	}

	@JavascriptInterface
	public void kvSet(String k, String v) {
		ldao.kvSet(k, v);
	}

	@JavascriptInterface
	public void kvDel(String k) {
		ldao.kvDel(k);
	}

/*------------------- 登录 ---------------------*/
	// 用户登录
	@JavascriptInterface
	public String signIn (String uid, String pw) {
		return ws.qry("login_json",
			new String[] {"usercode", "userpwd"},
			new String[] {uid, pw}
		);
	}

	// 注销用户
	@JavascriptInterface
	public void signOut () {
		ldao.kvDel("user");
	}

/*------------------- 关于 ---------------------*/
	@JavascriptInterface
	public String getVersion() {
		return ma.getVer();
	}

/*------------------- 设置 ---------------------*/
	// 设置IP、端口、命名空间
	@JavascriptInterface
	public int setUrl(String ip, String port) {
		String u, n;
		if (ip == null) {
			u = ldao.kvGet("url");
			n = ldao.kvGet("npc");
		} else {
			ldao.kvSet("urlIp", ip);
			ldao.kvSet("urlPort", port);
			n = "http://" + ip + ":" + port + "/";
			u = n + "room/DataWebServicePort?wsdl";
			ldao.kvSet("url", u);
			ldao.kvSet("npc", n);
		}
		if (ws == null) {
			ws = new WebSrv(u, n);
		} else {
			ws.setUrl(u);
			ws.setNpc(n);
		}
		return 1;
	}

/*------------------- 其它 ---------------------*/
	@JavascriptInterface
	public void log(String msg) {
		Log.i("---- Web ----", msg);
	}

}
