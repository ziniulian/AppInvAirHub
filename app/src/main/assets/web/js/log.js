function init() {
	var o = tools.getUrlReq();
	if (o.rid) {
		dat.rid = o.rid;
		dat.getDat();
	}
}

dat = {
	rid: null,	// 设备编号

	getDat: function () {
		var o = mn.qryWs("divicefindbydbmandsn", "{\"dbm\":\"" + dat.rid +"\"}");
		if (o.ok && o.DEVICE.length) {
			for (var i = 0; i < o.DEVICE.length; i ++) {
				dat.flush(o.DEVICE[0]);
			}
		}
	},

	flush: function (o) {
		var s, k, r, b, d;

		// 状态
		switch (o.state) {
			case "002":
				s = "入库";
				k = "rktime";
				break;
			case "003":
				s = "出库";
				k = "jytime";
				break;
		}

		if (s) {
			r = document.createElement("tr");
			b = document.createElement("table");
			b.className = "wh_tb sfs";
			d = document.createElement("tbody");

			dat.crtDom(d, o[k], ",", s);

			b.appendChild(d);
			d = document.createElement("td");
			d.appendChild(b);
			r.appendChild(d);
			listDoe.appendChild(r);
		}
	},

	crtDom: function (b, k, g, v) {
		var r = document.createElement("tr");
		var d = document.createElement("td");
		d.className = "wh_tb_k";
		d.innerHTML = k;
		r.appendChild(d);

		d = document.createElement("td");
		d.className = "wh_tb_g";
		d.innerHTML = g;
		r.appendChild(d);

		d = document.createElement("td");
		d.innerHTML = v;
		r.appendChild(d);
		b.appendChild(r);
	},

	back: function () {
		window.history.back();
	}
};
