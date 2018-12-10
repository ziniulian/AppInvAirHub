// 通用工具
tools = {

	// 信息提示
	memo: {
		tid: 0,
		doe: null,
		til: "",
		timout: 3000,

		show: function (msg, t) {
			if (this.tid) {
				clearTimeout(this.tid);
			}
			this.doe.innerHTML = msg;
			if (t !== 0) {
				if (!t) {
					t = this.timout;
				}
				this.tid = setTimeout(this.hid_s, t);
			}
		},

		exit: function (msg, til) {
			if (!this.til) {
				this.til = document.title;
				document.title = til;
				this.show(msg);
			}
		},

		bind: function (d) {
			var self = this;
			this.doe = d;
			this.hid_s = function () {
				self.hid ();
			};
		},

		hid: function () {
			this.doe.innerHTML = "";
			this.tid = 0;
			if (this.til) {
				document.title = this.til;
				this.til = "";
			}
		}
	},

	// 获取GET参数
	getUrlReq: function () {
		var url = location.search;
		var theRequest = {};
		if (url.indexOf("?") != -1) {
			url = url.substr(1).split("&");
			for(var i = 0; i < url.length; i ++) {
				var str = url[i].split("=");
				theRequest[str[0]] = decodeURIComponent(str[1]);
			}
		}
		return theRequest;
	}

};
