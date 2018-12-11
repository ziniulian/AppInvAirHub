mn = {
/****************** 登录 ******************/
	signIn: function (uid, pw) {
		var r = false;
		try {
			var u = JSON.parse(rfdo.qryWs("login_json", "{\"usercode\":\""
				+ uid + "\",\"userpwd\":\""
				+ pw + "\"}"));
			if (u.ok) {
				u = u.SYSUSER[0];
				rfdo.kvSet("userId", u.syscode);
				rfdo.kvSet("user",
					"{\"id\":\"" + u.syscode +
					"\",\"nam\":\"" + u.sysname +
					"\",\"rid\":\"" + u.sysuerid + "\"}"
				);
				r = true;
			}
		} catch (e) {
// mn.log(e.toString());
		}
		return r;
	},
	signOut: function () {
		rfdo.kvDel("user");
	},
	getUser: function () {
		var u = rfdo.kvGet("user");
		if (u) {
			return JSON.parse(u);
		} else {
			return null;
		}
	},

/****************** 基础方法 ******************/
	getVersion: function () {
		return rfdo.getVersion();
	},
	setUrl: function (ip, port) {
		return rfdo.setUrl(ip, port);
	},

	kvGet: function (k) {
		return rfdo.kvGet(k);
	},
	kvSet: function (k, v) {
		return rfdo.kvSet(k, v);
	},
	kvDel: function (k) {
		return rfdo.kvDel(k);
	},
	qryWs: function (m, p) {
		return JSON.parse(rfdo.qryWs(m, p));
	},

	music: function (typ) {
		rfdo.music(typ);
	},

	log: function (msg) {
		rfdo.log(msg);
	}
};
