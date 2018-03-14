package com.my.utils.amap.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 地图信息
 */
@Data
public class MapInfoExp implements Serializable{
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;

}
