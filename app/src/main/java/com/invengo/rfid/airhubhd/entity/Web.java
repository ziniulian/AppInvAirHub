package com.invengo.rfid.airhubhd.entity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.invengo.rfid.airhubhd.Ma;
import com.invengo.rfid.airhubhd.R;
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
import tk.ziniulian.util.Str;
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

	// 声音
	private SoundPool sp = null;
	private int music_err;
	private int music_ok;
	private int music_tag;

	public Web (Ma m) {
		this.ma = m;
	}

	// 读写器设置
	public void initRd () {
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
		initWs();

		// 数据字典测试数据
		ldao.mkvSet("JSJ", "计算机", "NamKv");
		ldao.mkvSet("LDC", "背景光亮度计", "NamKv");
		ldao.mkvSet("0100000001", "监视专业-X系统-测试", "ClsKv");
		ldao.mkvSet("TX00000000", "通信专业-测试", "ClsKv");
		ldao.mkvSet("QX00000000", "气象专业-测试", "ClsKv");
	}

	// WebService初始化
	protected void initWs() {
		String ip = ldao.kvGet("urlIp");
		String port = ldao.kvGet("urlPort");
		String npc = ldao.kvGet("npc");
		if (ip == null) {
			ip = "192.169.0.35";
			port = "8080";
			npc = "http://ws2ws.lzr.invengo.com/";
			ldao.kvSet("urlIp", ip);
			ldao.kvSet("urlPort", port);
			ldao.kvSet("npc", npc);
			ldao.kvSet("rate", "30");
			ldao.kvSet("synTim", "2018-11-11 11:11:11");
		}
		ws = new WebSrv(megUrl(ip, port), npc);
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

	// 声音初始化
	public void initMusic () {
		sp = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
		music_err = sp.load(ma, R.raw.error, 1);
		music_ok = sp.load(ma, R.raw.tag, 1);
		music_tag = sp.load(ma, R.raw.click, 1);
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

	@JavascriptInterface
	public void setRate(String r) {
		rfd.rate(r);
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

/*------------------- WebService ---------------------*/
	@JavascriptInterface
	public String qryWs(String method, String parm) {
//		String s = ws.jsonQry(method, parm);
		String s = ws.jsonQry("call", "{\"meth\":\"" + method + "\",\"parm\":" + gson.toJson(parm) + "}");
		if (s == null) {
			s = "{\"ok\": false, \"error\": \"网络故障！\"}";
		}
//Log.i("----- WS - " + method + " - " + parm + " -----", s);
		return s;
	}

/*------------------- 关于 ---------------------*/
	@JavascriptInterface
	public String getVersion() {
		return ma.getVer();
	}

/*------------------- 设置 ---------------------*/
	// 设置IP、端口
	@JavascriptInterface
	public int setUrl(String ip, String port) {
		ldao.kvSet("urlIp", ip);
		ldao.kvSet("urlPort", port);
		ws.setUrl(megUrl(ip, port));
		return 1;
	}

	protected String megUrl(String ip, String port) {
		return "http://" + ip + ":" + port + "/Ws2ws/ws";
	}

/*------------------- 其它 ---------------------*/
	@JavascriptInterface
	public void log(String msg) {
		Log.i("---- Web ----", msg);
	}

	@JavascriptInterface
	public void music(int typ) {
		switch (typ) {
			case 0:
				typ = music_err;
				break;
			case 1:
				typ = music_ok;
				break;
			case 2:
				typ = music_tag;
				break;
			default:
				return;
		}
		sp.play(typ, 1, 1, 0, 0, 1);
	}

/*------------------- 业务 ---------------------*/
	@JavascriptInterface
	public String parseEpc (String epc) {
		String r = Str.Hexstr2Dat(epc.substring(0, 12));
		return r + epc.substring(12, 32);
	}

	@JavascriptInterface
	public void setNam (String k, String v) {
		ldao.mkvSet(k, v, "NamKv");
	}

	@JavascriptInterface
	public void setCls (String k, String v) {
		ldao.mkvSet(k, v, "ClsKv");
	}

/*------------------- 查询 ---------------------*/
	@JavascriptInterface
	public String parseNam (String nam) {
		return ldao.mkvGet(nam, "NamKv");
	}

	@JavascriptInterface
	public String parseCls (String cls) {
		return ldao.mkvGet(cls, "ClsKv");
	}

}
