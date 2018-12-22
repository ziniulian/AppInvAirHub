function init() {
	tools.memo.bind(memoDom);
	dat.flush();
}

dat = {
	// 刷新页面
	flush: function () {
		ipDom.value = mn.kvGet("urlIp");
		portDom.value = mn.kvGet("urlPort");
		rateDom.value = mn.kvGet("rate");
		var s = mn.kvGet("jid");
		if (s) {
			jidDom.value = s;
		}
	},

	// 保存
	sav: function () {
		var r = false;
		var ip = ipDom.value;
		var port = portDom.value;
		var jid = jidDom.value;
		var rat = rateDom.value;
		if (jid) {
			mn.kvSet("jid", jid);
		}

		if (ip) {
			if (port) {
				if (rat) {
					rat -= 0;
					if (rat >= 1) {
						if (rat <= 30) {
							if (mn.setUrl (ip, port)) {
								rfid.setRate(rat);	// 功率设置
								mn.kvSet("rate", rat);
								tools.memo.show("保存成功！");
								r = true;
							}
						} else {
							rateDom.value = 30;
							tools.memo.show("功率不能大于 30 ！");
						}
					} else {
						rateDom.value = 1;
						tools.memo.show("功率不能小于 1 ！");
					}
				} else {
					tools.memo.show("功率不能为空！");
				}
			} else {
				tools.memo.show("端口不能为空！");
			}
		} else {
			tools.memo.show("IP不能为空！");
		}

		return r;
	},

	// 同步数据字典
	syn: function () {
		// 保存IP
		// 测试网络
		// 同步数据字典
	},

	back: function () {
		window.history.back();
	}
};
