function init() {
	// dat.crtTag(mn.parseEpcObj("SJSJTX00102003201811260004"), 3);
	// dat.crtTag(mn.parseEpcObj("XJSJXX01001008201811260004"), 3);
}

rfid.hdScan = function (arr) {
	var o, e, m = 0;
	for (var i = 0; i < arr.length; i ++) {
		e = mn.parseEpc(arr[i].epc);
		o = dat.ts[e];
		if (o) {
			o.whTim += arr[i].tim;
			o.whTimDoe.innerHTML = o.whTim;
			m = 2;
		} else {
			o = mn.parseEpcObj(e);
			if (o) {
				dat.crtTag(o, arr[i].tim);
				m = 2;
			}
		}
	}
	if (m) {
		mn.music(m);
	}
};

dat = {
	ts: {},		// 设备明细

	// 生成标签
	crtTag: function (o, tim) {
		var b, r, d;
		o.whTim = tim;

		r = document.createElement("tr");
		b = document.createElement("table");
		b.className = "wh_tb sfs";

		d = document.createElement("tbody");
		dat.crtDom(d, "类型", ":", o.typ);
		dat.crtDom(d, "名称", ":", o.nam);
		if (o.typCod === "S") {
			dat.crtDom(d, "分类", ":", o.cls);
		} else {
			dat.crtDom(d, "数量", ":", o.count);
			dat.crtDom(d, "箱号", ":", o.cod + " , 共 " + o.totle + " 箱");
		}

		// 次数统计
		o.whTimDoe = document.createElement("div");
		o.whTimDoe.className = "wh_tb_t sfs";
		o.whTimDoe.innerHTML = o.whTim;
		d.appendChild(o.whTimDoe);

		b.appendChild(d);
		d = document.createElement("td");
		d.appendChild(b);
		r.appendChild(d);

		o.whDoe = r;
		listDom.appendChild(r);
		dat.ts[o.dbm] = o;
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

	clear: function () {
		for (s in dat.ts) {
			delete dat.ts[s];
		}
		listDom.innerHTML = "";
	},

	back: function () {
		window.history.back();
	}
};
