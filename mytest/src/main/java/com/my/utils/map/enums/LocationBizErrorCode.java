package com.my.utils.map.enums;

import com.my.pojo.ServerCode;

import lombok.Getter;

/**
 * 错误信息枚举工具类
 */
@Getter
public enum LocationBizErrorCode implements ServerCode {
	/**请求成功*/
	SUCCESS("0000","请求成功"),
    //========================================================================//
    //                              系统错误
    //========================================================================//
	/**系统未知错误*/
    UNKNOWN_EXCEPTION("9999","系统未知错误"),
    //                              请求校验
    //========================================================================//
    /**请求参数非法*/
    REQUEST_PARAM_ILLEGAL("0001","请求参数非法"),
    /**地址信息为空*/
    EMPTY_ADDRESS("0002", "地址信息不能为空"),
    /**第三方网关名称为空*/
    EMPTY_GATEWAY_NAME("0003", "第三方网关名称不能为空"),
	//========================================================================//
    //                              地图API(10XX)
    //========================================================================//
    /**未查询到结果*/
	EMPTY_RESULT("1001", "未查询到该地址信息"),
    /**经纬度不正确*/
    LNG_LAT_ERROR("1002", "经纬度不正确"),
    /**网关名称不正确*/
    NO_GATEWAY_NAME("1003", "网关名称不正确"),

    ;

    /** 操作代码 */
    private final String code;

    /** 描述 */
    private final String msg;

    private LocationBizErrorCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return
     */
    public static LocationBizErrorCode getByCode(String code) {
        for (LocationBizErrorCode status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
