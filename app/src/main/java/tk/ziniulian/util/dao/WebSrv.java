package tk.ziniulian.util.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * 访问WebService
 * Created by LZR on 2018/11/27.
 */

public class WebSrv {
	private String npc;		// 命名空间
	private HttpTransportSE ht;
	private Gson gson = new Gson();
	private Type tm = new TypeToken<LinkedHashMap<String, String>>(){}.getType();

	public WebSrv (String u, String n) {
		this.npc = n;
		this.ht = new HttpTransportSE(u);
	}

	public WebSrv setUrl(String url) {
		this.ht.setUrl(url);
		return this;
	}

	public WebSrv setNpc(String npc) {
		this.npc = npc;
		return this;
	}

	private String qry (SoapObject req) {
		String r = null;

		SoapSerializationEnvelope msg = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//		SoapSerializationEnvelope msg = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		msg.bodyOut = req;
//		msg.dotNet = true;

		try {
//			ht.call(npc + req.getName(), msg);
			ht.call(null, msg);
			SoapPrimitive res = (SoapPrimitive) msg.getResponse();
			if (res != null) {
				r = res.toString();
			}
			ht.getServiceConnection().openOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	public String qry (String mnam) {
		return qry(mnam, null);
	}

	public String qry (String mnam, String[] ks, Object[] vs) {
		SoapObject req = new SoapObject(npc, mnam);
		if (ks != null && vs != null) {
			for (int i = 0; i < ks.length; i ++) {
				req.addProperty(ks[i], vs[i]);
			}
		}
		return qry(req);
	}

	public String qry (String mnam, LinkedHashMap<String, String> p) {
		SoapObject req = new SoapObject(npc, mnam);
		if (p != null) {
			Iterator<Entry<String, String>> iterator = p.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				req.addProperty(entry.getKey(), entry.getValue());
			}
		}
		return qry(req);
	}

	public String jsonQry (String mnam, String parmJson) {
		LinkedHashMap<String, String> m = null;
		if (parmJson != null) {
			try {
				m = gson.fromJson(parmJson, tm);
			} catch (Exception e) {
				 e.printStackTrace();
			}
		}
		return qry(mnam, m);
	}

	public static void main (String[] args) {
		Gson gson = new Gson();

		// 测试
//		WebSrv ws = new WebSrv("http://127.0.0.1:8080/TestWebService/ws", "http://ws2ws.lzr.invengo.com/");
//		WebSrv ws = new WebSrv("http://127.0.0.1:8080/Ws2ws/ws", "http://ws2ws.lzr.invengo.com/");
//		WebSrv ws = new WebSrv("http://192.169.0.35:8888/room/DataWebServicePort?wsdl", "http://sys.action.web.cw.com/");
		WebSrv ws = new WebSrv("http://192.169.0.35:8080/Ws2ws/ws", "http://ws2ws.lzr.invengo.com/");
//		System.out.println(ws.jsonQry("getdevice_rkjson", "{" +
//				"\"infoid\":\"402880e4676f693901676f6ddb5e0001\"" +
//		"}"));
//		System.out.println(ws.jsonQry("login_json", "{" +
//				"\"usercode\":\"admin\"," +
//				"\"userpwd\":\"admin\"" +
//		"}"));
//		System.out.println(ws.jsonQry("setWs", "{" +
//				"\"url\":\"http://192.169.0.35:8888/room/DataWebServicePort\"," +
//				"\"npc\":\"http://sys.action.web.cw.com/\"" +
//		"}"));
//		System.out.println(ws.qry("call",
//				new String[] {"meth", "parm"},
//				new String[] {"hello", null}
//		));
//		System.out.println(ws.jsonQry("call", "{" +
//				"\"meth\":\"hello\"," +
//				"\"parm\":null" +
//		"}"));
		System.out.println(ws.jsonQry("call", "{" +
				"\"meth\":\"login_json\"," +
				"\"parm\":" + gson.toJson("{" +
					"\"usercode\":\"admin\"," +
					"\"userpwd\":\"admin\"" +
				"}") +
		"}"));
//		System.out.println(ws.jsonQry("call", "{" +
//				"\"meth\":\"getrksqlist_json\"," +
//				"\"parm\":null" +
//		"}"));
//		System.out.println(ws.jsonQry("call", "{" +
//				"\"meth\":\"getdevice_rkjson\"," +
//				"\"parm\":" + gson.toJson("{" +
//					"\"infoid\":\"402880e4676f693901676f6ddb5e0001\"" +
//				"}") +
//		"}"));
//		System.out.println(ws.jsonQry("call", "{" +
//				"\"meth\":\"rkdevicesave_json\"," +
//				"\"parm\":" + gson.toJson("{" +
//					"\"dbm\":\"SJSJ00100001000201812100001\"" +
//					",\"hjh\":\"\"" +
//					",\"sysuerid\":\"111\"" +
//				"}") +
//		"}"));
//		System.out.println(ws.jsonQry("call", "{" +
//				"\"meth\":\"getcksqlist_json\"," +
//				"\"parm\":null" +
//		"}"));
//		System.out.println(ws.jsonQry("call", "{" +
//				"\"meth\":\"ckspwedivice_json\"," +
//				"\"parm\":" + gson.toJson("{" +
//					"\"outWeInfoid\":\"402980a367c90e860167c923700d0025\"" +
//				"}") +
//		"}"));
//		System.out.println(ws.jsonQry("call", "{" +
//				"\"meth\":\"divicefindbydbmandsn\"," +
//				"\"parm\":" + gson.toJson("{" +
//					"\"dbm\":\"SBJGQX00000000201811290002\"" +
//				"}") +
//		"}"));
//		System.out.println(ws.jsonQry("call", "{" +
//				"\"meth\":\"divicefindbydbmandsn\"," +
//				"\"parm\":" + gson.toJson("{" +
//					"\"dbm\":\"SSJ2JS00100000201811300009\"" +
//				"}") +
//		"}"));
//		System.out.println(ws.jsonQry("call", "{" +
//				"\"meth\":\"getsysspecialtylistbycjtime\"," +
//				"\"parm\":" + gson.toJson("{" +
//					"\"cjtime\":\"2018-11-11 11:11:11\"" +
//				"}") +
//		"}"));
//		System.out.println(ws.jsonQry("getsysspecialtylistbycjtime", "{" +
//				"\"cjtime\":\"2018-11-11 11:11:11\"" +
//		"}"));
	}
}
