function init() {
	tools.memo.bind(memoDom);
	dat.flush();
}

dat = {
	// 刷新页面
	flush: function () {
		ipDom.value = mn.kvGet("urlIp");
		portDom.value = mn.kvGet("urlPort");
	},

	// 保存
	sav: function () {
		var ip = ipDom.value;
		var port = portDom.value;
		if (ip) {
			if (port) {
				if (mn.setUrl (ip, port)) {
					tools.memo.show("保存成功！");
					// setTimeout(tools.syn, 100);
				}
			} else {
				tools.memo.show("端口不能为空！");
			}
		} else {
			tools.memo.show("IP不能为空！");
		}
	},

	back: function () {
		window.history.back();
	}
};
