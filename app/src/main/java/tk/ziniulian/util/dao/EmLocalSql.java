package tk.ziniulian.util.dao;

/**
 * SQL语句
 * Created by 李泽荣 on 2018/7/19.
 */

public enum EmLocalSql {

	// 获取键值对
	KvGet("select v from <1> where k = '<0>'"),

	// 设置键值对
	KvSet("update <2> set v = '<1>' where k = '<0>'"),

	// 添加键值对
	KvAdd("insert into <2> values('<0>', '<1>')"),

	// 删除键值对
	KvDel("delete from <1> where k = '<0>'");

	private final String sql;
	EmLocalSql(String s) {
		sql = s;
	}

	@Override
	public String toString() {
		return sql;
	}
}
