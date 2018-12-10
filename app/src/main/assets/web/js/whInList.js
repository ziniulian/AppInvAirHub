function init() {
	dat.flush(dat.getDat());
}

dat = {
	method: "getrksqlist_json",
	link: "whIn.html?rid=",

	getDat: function () {
		var r = null;
		var o = mn.qryWs(dat.method, null);
		if (o) {
			o = JSON.parse(o);
			if (o.ok) {
				r = o.STORAGES_IN;
			}
		}
		return r;
	},

	flush: function (d) {
		var a, v, i;
		if (d && d.length) {
			listDom.innerHTML = "";
			for (i = 0; i < d.length; i ++) {
				a = document.createElement("a");
				a.className = "wh_list";
				a.href = dat.link + d[i].infoid + "&nam=" + d[i].bh;
				a.ontouchstart = function () {
					this.className = "wh_list wh_list_scd";
				}
				a.ontouchend = function () {
					this.className = "wh_list";
				}

				v = document.createElement("div");
				v.className = "wh_list_nam mfs";
				v.innerHTML = d[i].bh;
				a.appendChild(v);

				v = document.createElement("div");
				v.className = "wh_list_remark sfs";
				v.innerHTML = d[i].creattime + "<br/>" + d[i].squseridname;
				a.appendChild(v);

				listDom.appendChild(a);
			}
		}
	},

	back: function () {
		window.history.back();
	}
};
