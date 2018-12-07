mn = {
	signIn: function (uid, pw) {
		var r = false;
		try {
			var u = JSON.parse(rfdo.qryWs("login_json", "{\"usercode\":\""
				+ uid + "\",\"userpwd\":\""
				+ pw + "\"}"));
			if (u.ok) {
				rfdo.kvSet("userId", u.dat[0][0].syscode);
				rfdo.kvSet("user",
					"{\"id\":\"" + u.dat[0][0].syscode +
					"\",\"nam\":\"" + u.dat[0][0].sysname +
					"\",\"rid\":\"" + u.dat[0][0].sysuerid + "\"}"
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
		return rfdo.qryWs(m, p);
	},

	log: function (msg) {
		rfdo.log(msg);
	}
};
