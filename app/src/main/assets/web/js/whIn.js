function init() {
	var o = tools.getUrlReq();
	if (o.rid) {
		dat.rid = o.rid;
		namDom.innerHTML = o.nam;
		dat.user = mn.getUser();
		dat.getDat();
	}
}

rfid.hdScan = function (arr) {
	var o, e, m = 0;
	for (var i = 0; i < arr.length; i ++) {
		e = mn.parseEpc(arr[i].epc);
		o = dat.ts[e];
		if (o) {
			if (m === 0) {
				m = 2;
			}
			o.whTim += arr[i].tim;
			o.whTimDoe.innerHTML = o.whTim;
			if (o.whStat === 0) {
				if (dat.sav(o)) {
					m = 1;
				}
			}
		}
	}
	if (m === 2) {
		mn.music(2);
	}
};

dat = {
	rid: null,	// 入库单号
	count: 0,	// 总数
	oknum: 0,	// 已入库数
	ts: {},		// 设备明细
	user: null,	// 用户id

	getDat: function () {
		var o = mn.qryWs("getdevice_rkjson", "{\"infoid\":\"" + dat.rid + "\"}");
		if (o.ok && o.STORAGE_DEVICE_INFO.length) {
			dat.hdDat(o.STORAGE_DEVICE_INFO);
		}
	},

	hdDat: function (a) {
		var i, o, b, r, d;
		dat.count = a.length;
		numDom.innerHTML = a.length;
		for (i = 0; i < a.length; i ++) {
			o = a[i];
// mn.log(o.dbm);
			dat.ts[o.dbm] = o;
			o.whId = i;
			o.whTim = 0;

			// 生成 DOM
			r = document.createElement("tr");
			o.whDoe = r;
			if (o.isrk === "001") {		// TODO ： 目前001代表已入库，但是否为001还有待确认
				o.whStat = 1;	// 已入库
				dat.oknum ++;
				okListDom.appendChild(r);
			} else {
				o.whStat = 0;	// 未入库
				noListDom.appendChild(r);
			}

			b = document.createElement("table");
			b.className = "wh_tb sfs";
			d = document.createElement("tbody");

			// 数据内容
			o.whImgDoe = dat.crtDom(d, o.whStat, "", o.sbidname);
			if (o.serialNumber) {
				dat.crtDom(d, "S/N", ":", o.serialNumber);
			}
			dat.crtDom(d, "型号", ":", o.ggidname);
			dat.crtDom(d, "品牌", ":", o.supplieridname);
			dat.crtDom(d, "分类", ":", o.zyidname);

			// 次数统计
			o.whTimDoe = document.createElement("div");
			o.whTimDoe.className = "wh_tb_t sfs";
			d.appendChild(o.whTimDoe);

			b.appendChild(d);
			d = document.createElement("td");
			d.appendChild(b);
			r.appendChild(d);
		}
		dat.flushNum();
	},

	crtDom: function (b, k, g, v) {
		var r = document.createElement("tr");
		var d = document.createElement("td");
		var rr = d;
		switch (k) {
			case 0:
				d.className = "wh_out_tb_img wh_out_tb_img_no";
				break;
			case 1:
				d.className = "wh_out_tb_img wh_out_tb_img_ok";
				break;
			default:
				d.className = "wh_tb_k";
				d.innerHTML = k;
				break;
		}
		r.appendChild(d);

		d = document.createElement("td");
		d.className = "wh_tb_g";
		d.innerHTML = g;
		r.appendChild(d);

		d = document.createElement("td");
		d.innerHTML = v;
		r.appendChild(d);
		b.appendChild(r);

		return rr;
	},

	sav: function (o) {
		var r = false;
		var s = mn.qryWs("rkdevicesave_json",
			"{\"dbm\":\"" + o.dbm +
			"\",\"hjh\":\"" + "d001-01" +	// TODO: 库位为空时接口有问题，不能返回 true
			"\",\"sysuerid\":\"" + dat.user +
		"\"}");
		if (s.ok) {
			// 保存成功
			o.whStat = 1;
			dat.oknum ++;
			noListDom.removeChild(o.whDoe);
			o.whImgDoe.className = "wh_out_tb_img wh_out_tb_img_ok";
			okListDom.appendChild(o.whDoe);
			mn.music(1);
			r = true;
			dat.flushNum();
		}

		return r;
	},

	flushNum: function () {
		okNumDom.innerHTML = dat.oknum;
	},

	back: function () {
		window.history.back();
	}
};
