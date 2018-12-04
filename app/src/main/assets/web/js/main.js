mn = {
	signIn: function (uid, pw) {
		var r = false;
		try {
			var s = rfdo.signIn(uid, pw);
			var u = JSON.parse(s);
			rfdo.kvSet("userId", u[0][0].syscode);
			rfdo.kvSet("user",
				"{\"id\":\"" + u[0][0].syscode +
				"\",\"nam\":\"" + u[0][0].sysname +
				"\",\"rid\":\"" + u[0][0].sysuerid + "\"}"
			);
			r = true;
		} catch (e) {
// mn.log(e.toString());
		}
		return r;
	},
	signOut: function () {
		rfdo.signOut();
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

	log: function (msg) {
		rfdo.log(msg);
	}
};
