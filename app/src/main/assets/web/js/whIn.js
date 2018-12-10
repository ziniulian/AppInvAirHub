function init() {
	var o = tools.getUrlReq();
	if (o.rid) {
		dat.rid = o.rid;
		namDom.innerHTML = o.nam;
		dat.getDat();
	}
}

dat = {
	rid: null,	// 入库单号
	count: 0,	// 总数
	oknum: 0,	// 已入库数
	dvs: null,	// 设备明细
	ds: {},		// 设备编号集合

	method: "getdevice_rkjson",

	getDat: function () {
		var o = mn.qryWs(dat.method, "{\"infoid\":\"" + dat.rid + "\"}");
		if (o) {
			o = JSON.parse(o);
			if (o.ok && o.STORAGE_DEVICE_INFO.length) {
				dat.dvs = o.STORAGE_DEVICE_INFO;
				dat.count = dat.dvs.length;
				dat.hdDat();
			}
		}
	},

	hdDat: function (d) {
		var i, o, b, r, d;
		for (i = 0; i < dat.count; i ++) {
			o = dat.dvs[i];
			dat.ds[o.dbm] = o;

			// 生成 DOM
			r = document.createElement("tr");
			o.whDoe = r;

			d = document.createElement("td");
			o.whImgDoe = d;
			if (o.isrk === "001") {		// TODO ： 目前001代表已入库，但是否为001还有待确认
				d.className = "wh_out_tb_img wh_out_tb_img_ok";
				okListDom.appendChild(r);
				dat.oknum ++;
			} else {
				d.className = "wh_out_tb_img wh_out_tb_img_no";
				noListDom.appendChild(r);
			}
			r.appendChild(d);

			b = document.createElement("table");
			b.className = "wh_tb sfs";
			d = document.createElement("tbody");

			// 数据内容
			dat.crtDom(d, "名称", o.sbidname);
			if (o.serialNumber) {
				dat.crtDom(d, "序列号", o.serialNumber);
			}
			dat.crtDom(d, "型号", o.ggidname);
			dat.crtDom(d, "品牌", o.supplieridname);
			dat.crtDom(d, "分类", o.zyidname);

			//

			b.appendChild(d);
			d = document.createElement("td");
			d.appendChild(b);
			r.appendChild(d);
		}
		dat.flushNum();
	},

	crtDom: function (b, k, v) {
		var r = document.createElement("tr");
		var d = document.createElement("td");
		d.className = "wh_tb_k";
		d.innerHTML = k;
		r.appendChild(d);

		d = document.createElement("td");
		d.className = "wh_tb_g";
		d.innerHTML = ":";
		r.appendChild(d);

		d = document.createElement("td");
		d.innerHTML = v;
		r.appendChild(d);
		b.appendChild(r);
	},

	sav: function () {},

	flushNum: function () {
		okNumDom.innerHTML = dat.oknum;
		noNumDom.innerHTML = (dat.count - dat.oknum);
	},

	back: function () {
		window.history.back();
	}
};
