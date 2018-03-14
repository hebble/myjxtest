package com.my.utils.amap.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.my.enums.RequestTypeEnum;
import com.my.pojo.ResultData;
import com.my.utils.ConfigManager;
import com.my.utils.HttpUtil;
import com.my.utils.amap.AbstractMapGeoCoderBiz;
import com.my.utils.amap.enums.LocationBizErrorCode;
import com.my.utils.amap.factory.MapGeoCoderFactory;
import com.my.utils.amap.model.MapInfoExp;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lixiaohua
 * @version 1.0
 * @date 2018/1/18
 */
@Slf4j
@Component
public class AMapGeoCoderBiz extends AbstractMapGeoCoderBiz {
    private static final String OK = "1";

    @Override
    public ResultData<MapInfoExp> geoToCode(String address, String city) {
        String key = ConfigManager.getInstance("application").getString("map.api.gaode.key", true);
        String geoUrl = ConfigManager.getInstance("application").getString("map.api.gaode.geo.url", true);

        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        params.put("address", address);
        if (StringUtils.isNotBlank(city)) {
            params.put("city", city);
        }
        String ret = HttpUtil.invoke(params, geoUrl, RequestTypeEnum.GET);
        return getLocationCode(ret);
    }

    @Override
    public ResultData codeToGeo(String longitude, String latitude) {
        String key = ConfigManager.getInstance("application").getString("map.api.gaode.key", true);
        String regeoUrl = ConfigManager.getInstance("application").getString("map.api.gaode.regeo.url", true);

        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        params.put("location", longitude + "," + latitude);
        String ret = HttpUtil.invoke(params, regeoUrl, RequestTypeEnum.GET);
        return getLocationGeo(ret);
    }

    @Override
    public ResultData<MapInfoExp> getLocationCode(String apiRet) {
        log.info("----高德地图地理编码获取经纬度---传入的参数:{}", apiRet);
        Map<String, Object> map = JSON.parseObject(apiRet, Map.class);
        String status = (String) map.get("status");
        if (!Objects.equals(status, OK)) {
            return ResultData.getFailResult(map.get("info").toString());
        }

        List<Map> geocodes = (List) map.get("geocodes");
        if (geocodes == null || geocodes.size() <= 0) {
            return ResultData.setResultData(LocationBizErrorCode.EMPTY_RESULT);
        }
        String location = (String) geocodes.get(0).get("location");
        String[] split = location.split(",");
//        Map<String, String> ret = new HashMap<>();
//        ret.put("longitude", split[0]);
//        ret.put("latitude", split[split.length - 1]);
        MapInfoExp mapInfoExp = new MapInfoExp();
        mapInfoExp.setLatitude(split[split.length - 1]);
        mapInfoExp.setLongitude(split[0]);
        return ResultData.getSuccessData(mapInfoExp);
    }

    @Override
    public ResultData getLocationGeo(String apiRet) {
        log.info("----高德地图逆地理编码获取地理信息---传入的参数:{}", apiRet);
        Map<String, Object> map = JSON.parseObject(apiRet, Map.class);
        String status = (String) map.get("status");
        if (!Objects.equals(status, OK)) {
            return ResultData.getFailResult(map.get("info").toString());
        }

        Map<String, Object> reGeoCode = (Map<String, Object>) map.get("regeocode");
        if (reGeoCode == null || reGeoCode.size() <= 0) {
            return ResultData.setResultData(LocationBizErrorCode.EMPTY_RESULT);
        }
        String formattedAddress = (String) reGeoCode.get("formatted_address");
        return ResultData.getSuccessData(formattedAddress);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MapGeoCoderFactory.registerMapApi("gaode", this);
    }
}
