package com.ecar.ecarnetwork.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 接口应答
 * 
 * @author Administrator
 * 
 */
public class ResBase implements Serializable{
	@SerializedName("state")
	public int state;// 成功标志位（1成功，0失败）默认为0

	@SerializedName(value = "message",alternate="resultmsg")//接口所有msg 改成message  系统级错误 0x02 0x04 重新登录
	public String msg;// 错误信息

	@SerializedName("resultcode")//
	public String code;// 返回的结果code


	@SerializedName("ngis")
	public String sign;//



	public int totalCount;// 列表总数

	@SerializedName("u")
	public String u;// userkey登录的时候获取，要在所有请求中加入

	@SerializedName("t")
	public String t;// 需要同步请求时使用

	@SerializedName("v")
	public String v;// 登录后请求时使用
//
	@SerializedName("ts")
	public long ts;  //时间戳


	@SerializedName("json")
	public String jsonStr;  //json字符串


	@Override
	public String toString() {
		return "ResBase{" +
				"state=" + state +
				", msg='" + msg + '\'' +
				", code='" + code + '\'' +
				", sign='" + sign + '\'' +
				", totalCount=" + totalCount +
				", u='" + u + '\'' +
				", t='" + t + '\'' +
				", v='" + v + '\'' +
				", ts=" + ts +
				'}';
	}
}
