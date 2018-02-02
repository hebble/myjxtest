package com.my.pojo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

public class ResultData<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  // 处理成功
  public static final String OK = "0000";
  public static final String OK_MSG = "操作成功";
  // 其他错误
  public static final String ERR = "9999";
  // 其他错误
  public static final String ERR_MSG = "操作失败";

  public static final ResultData SUCESS = new ResultData(OK, OK_MSG);
  
  public static final ResultData UNKNOWN_EXCEPTION = new ResultData(ERR, ERR_MSG);

  private String code = ERR;
  private String msg = "";
  private T data;


  /**
   * 失败
   *
   * @return
   */
  public static ResultData getFailResult() {
    return new ResultData(ERR, ERR_MSG);
  }
  public static ResultData ERROR(String msg) {
	  return new ResultData("-1",msg);
  }
  /**
   * 失败
   *
   * @param message
   * @return
   */
  public static ResultData getFailResult(String message) {

    return new ResultData(ERR, message);
  }

  /**
   * 成功
   *
   * @param message
   * @return
   */
  public static ResultData getSuccessResult(String message) {
    return new ResultData(OK, message);
  }

  /**
   * 成功
   * @param data
   * @param <T>
   * @return
   */
  public static <T> ResultData getSuccessData(T data) {
    return new ResultData(OK, OK_MSG, data);
  }

  /**
   * 成功
   * @param data
   * @param message
   * @return
   */
  public static <T> ResultData getSuccessResult(T data, String message) {
    return new ResultData(OK, message, data);
  }
  


  public ResultData() {}

  public ResultData(String code, String message) {
    this.code = code;
    this.msg = message;
  }

  public ResultData(String code, String message, T result) {
    this.code = code;
    this.msg = message;
    this.data = result;
  }

  public ResultData(T result) {
    this(OK, "操作成功！", result);
  }
  
  /**
   * 设置编码和消息
   * @param serverCode
   * @return
   */
  public static ResultData setResultData(ServerCode serverCode) {
	  return new ResultData(serverCode.getCode(), serverCode.getMsg());
  }
  
  /**
   * 设置编码和自定义消息
   * @param serverCode
   * @param msg
   * @return
   */
  public static ResultData setResultData(ServerCode serverCode, String msg) {
	  return new ResultData(serverCode.getCode(), msg);
  }
  
  /**
   * 设置编码,信息和数据
   * @param serverCode
   * @param dataObj
   * @return
   */
  public static ResultData setResultData(ServerCode serverCode, Object data) {
	  return new ResultData(serverCode.getCode(), serverCode.getMsg(), data);
  }
  
  /**
   * 设置编码,信息和数据
   * @param serverCode
   * @param dataObj
   * @return
   * {
   * "code": "xxx",
   * "msg": "xxxx",
   * "responseData": {
   *    data
   * }
   *}
   */
  public static ResultData setResultDataJson(ServerCode serverCode, Object data) {
	  String jsonStr = JSON.toJSONString(data);
	  JSONObject json = new JSONObject();
	  json.put("responseData", jsonStr);
	  return new ResultData(serverCode.getCode(), serverCode.getMsg(), json);
  }
  
  
  
  public T getData() {
    return data;
  }

  public ResultData setData(T data) {
    this.data = data;
    return this;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String message) {

    this.msg = message;
  }
}
