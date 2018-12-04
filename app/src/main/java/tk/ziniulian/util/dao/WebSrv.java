package tk.ziniulian.util.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * 访问WebService
 * Created by LZR on 2018/11/27.
 */

public class WebSrv {
	private String url;		// 服务地址
	private String npc;		// 命名空间

	public WebSrv (String u, String n) {
		this.url = u;
		this.npc = n;
	}

	public WebSrv setUrl(String url) {
		this.url = url;
		return this;
	}

	public WebSrv setNpc(String npc) {
		this.npc = npc;
		return this;
	}

	public String qry (String mnam) {
		return qry(mnam, null, null);
	}

	public String qry (String mnam, String[] ks, Object[] vs) {
		String r = null;

		SoapObject req = new SoapObject(npc, mnam);
		if (vs != null) {
			for (int i = 0; i < ks.length; i ++) {
				req.addProperty(ks[i], vs[i]);
			}
		}

//		SoapSerializationEnvelope msg = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapSerializationEnvelope msg = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		msg.bodyOut = req;
//		msg.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(url);

		try {
//			ht.call(npc + mnam, msg);
			ht.call(null, msg);
			SoapPrimitive res = (SoapPrimitive) msg.getResponse();
			if (res != null) {
				r = res.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	public static void main (String[] args) {
		// 测试
		WebSrv ws = new WebSrv("http://192.169.0.35:8888/room/DataWebServicePort?wsdl", "http://192.169.0.35:8888/");
//		System.out.println(ws.qry("hello"));
//		System.out.println(ws.qry("login_json",
//				new String[] {"usercode", "userpwd"},
//				new String[] {"admin", "admin"}
//		));
//		System.out.println(ws.qry("getrksqlist_json"));
//		System.out.println(ws.qry("getdevice_rkjson",
//				new String[] {"infoid"},
//				new String[] {"402880e466ef70480166ef75d0890001"}
//		));
//		System.out.println(ws.qry("rkdevicesave_json"));
//		System.out.println(ws.qry("getcksqlist_json"));
//		System.out.println(ws.qry("ckspwedivice_json",
//				new String[] {"outWeInfoid"},
//				new String[] {"ff80808166ce68fb0166ce6fe0ff000c"}
//		));
	}
}
