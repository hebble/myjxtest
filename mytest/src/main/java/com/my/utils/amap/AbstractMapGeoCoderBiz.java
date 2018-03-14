package com.my.utils.amap;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;

import com.my.pojo.ResultData;
import com.my.utils.amap.model.MapInfoExp;

/**
 * @author lixiaohua
 * @version 1.0
 * @date 2018/1/18
 */
public abstract class AbstractMapGeoCoderBiz implements InitializingBean {
    /**
     * 最大经度
     */
    protected static final BigDecimal MAX_LONGITUDE = new BigDecimal("180");

    /**
     * 最大纬度
     */
    protected static final BigDecimal MAX_LATITUDE = new BigDecimal("90");
    /**
     * 经纬度小数点后最大位数
     */
    protected static final byte DEGREE_MAX_DEC_LENGTH = 6;

    /**
     * @title: 地址转经纬度
     * @description:
     * @param address 结构化地址信息
     * @param city 指定查询的城市
     * @return
     * @version v1.0 date: 2018/1/19 10:44 author:lixiaohua desc: new
     */
    public abstract ResultData<MapInfoExp> geoToCode(String address, String city);

    /**
     * @title: 经纬度转地址
     * @description:
     * @param longitude 经度
     * @param latitude 纬度
     * @return
     * @version v1.0 date: 2018/1/19 11:06 author:lixiaohua desc: new
     */
    public abstract ResultData codeToGeo(String longitude, String latitude);

    /**
     * @Title: 获取经纬度信息
     * @Description: 根据第三方API返回的数据获取出经纬度信息
     * @params apiRet 第三方地理编码接口返回的字符串
     * @return Map集合对象，包含经纬度，longitude：经度  latitude：纬度
     * @version v1.0 date: 2018/1/16 14:46 author:lixiaohua desc: new
     */
    protected abstract ResultData<MapInfoExp> getLocationCode(String apiRet);

    /**
     * @title: 获取地址信息
     * @description: 根据第三方API返回的数据获取出地址信息
     * @param apiRet 第三方地理编码接口返回的字符串
     * @return String类型的结构化地址信息
     * @version v1.0 date: 2018/1/19 11:11 author:lixiaohua desc: new
     */
    protected abstract ResultData getLocationGeo(String apiRet);

    /**
     * @Title: 验证经纬度是否合法
     * @Description:
     * @param longitude 经度
     * @param latitude 纬度
     * @return true：合法  false：非法
     * @version v1.0 date: 2018/1/19 11:00 author:lixiaohua desc:new
     */
    public boolean verifyLocation(String longitude, String latitude) {
        if (!NumberUtils.isNumber(longitude) || !NumberUtils.isNumber(latitude)) {
            return false;
        }

        BigDecimal decLongitude = new BigDecimal(longitude);
        BigDecimal decLatitude = new BigDecimal(latitude);
        if (decLongitude.compareTo(BigDecimal.ZERO) < 0
                || decLatitude.compareTo(BigDecimal.ZERO) < 0
                || decLongitude.compareTo(MAX_LONGITUDE) > 0
                || decLatitude.compareTo(MAX_LATITUDE) > 0) {
            return false;
        }
        // 检查小数点
        // 经度
        if (!verifyDecPoint(decLongitude)) {
            return false;
        }
        // 纬度
        if (!verifyDecPoint(decLatitude)) {
            return false;
        }

        return true;
    }

    /**
     * 检查经纬度小数点
     * @param decDegree
     * @return
     */
    private boolean verifyDecPoint(BigDecimal decDegree) {
        String degree = decDegree.toPlainString();
        int index = degree.indexOf(".");
        int decLength = degree.length() - (index + 1);
        if (decLength > DEGREE_MAX_DEC_LENGTH) {
            return false;
        }
        return true;
    }
}
