package com.ecar.ecarnetfream.login.entity;



import com.ecar.ecarnetwork.bean.ResBase;
import com.ecar.ecarnetwork.http.api.ApiBox;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 登陆应答解析
 * 
 * @author aa
 * 
 */
public class ResLogin extends ResBase implements Serializable{
	/**
	 * 05-05 15:50:46.142: D/tag(23566): HttpResponse =
	 * {"state":"1","msg":"登录成功"
	 * ,"code":"success","data":{"ParkId":"100000025","LoginName"
	 * :"15960259423","UserName"
	 * :"","UserSex":"True","UserType":"1","safecode":"115090"}}
	 */

	@SerializedName("ParkId")
	public String ParkId;

	@SerializedName("LoginName")
	public String LoginName;
	/**
	 * （true男，false女）
	 */
	@SerializedName("UserSex")
	public boolean UserSex;
	/**
	 * 注册来源（1APP，2RFID，3IVR,4建行，5官网）
	 */
	@SerializedName("UserType")
	public int UserType;
	/**
	 * 验证码
	 */
	@SerializedName("safecode")
	public String safecode;
	/**
	 * 
	 */
	@SerializedName("SystemTime")
	public String SystemTime;

	@SerializedName("SID")
	public String SID;// 登陆时获取的sessionId
}
