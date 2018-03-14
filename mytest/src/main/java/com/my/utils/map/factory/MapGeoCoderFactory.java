package com.my.utils.map.factory;

import java.util.HashMap;
import java.util.Map;

import com.my.pojo.ResultData;
import com.my.utils.map.AbstractMapGeoCoderBiz;

import static com.my.utils.map.enums.LocationBizErrorCode.NO_GATEWAY_NAME;
/**
 * @author lixiaohua
 * @version 1.0
 * @date 2018/1/18
 */
public class MapGeoCoderFactory {
    private static Map<String, AbstractMapGeoCoderBiz> geoCoderMap = new HashMap<>();
    private static final AbstractMapGeoCoderBiz EMPTY = new EmptyMapGeoCoder();

    public static AbstractMapGeoCoderBiz getGeoCoder(String gatewayName) {
        AbstractMapGeoCoderBiz geoCoder = geoCoderMap.get(gatewayName);
        return geoCoder == null ? EMPTY : geoCoder;
    }

    /**
     * @title: 注册对象
     * @description: 将具体的地图实体加入到工厂中
     * @param key
     * @param mapApi
     * @return
     * @version v1.0 date: 2018/1/19 10:37 author:lixiaohua desc: new
     */
    public static void registerMapApi(String key, AbstractMapGeoCoderBiz mapApi) {
        geoCoderMap.put(key, mapApi);
    }

    private static class EmptyMapGeoCoder extends AbstractMapGeoCoderBiz {

        @Override
        public ResultData geoToCode(String address, String city) {
            return ResultData.setResultData(NO_GATEWAY_NAME);
        }

        @Override
        public ResultData codeToGeo(String longitude, String latitude) {
            return ResultData.setResultData(NO_GATEWAY_NAME);
        }

        @Override
        public ResultData getLocationCode(String apiRet) {
            return ResultData.setResultData(NO_GATEWAY_NAME);
        }

        @Override
        protected ResultData getLocationGeo(String apiRet) {
            return ResultData.setResultData(NO_GATEWAY_NAME);
        }

        @Override
        public void afterPropertiesSet() throws Exception {

        }
    }
}
