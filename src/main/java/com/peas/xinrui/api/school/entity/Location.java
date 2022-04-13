package com.peas.xinrui.api.school.entity;

public class Location {

    private String code;

    private String detail;

    private Double lat;// 纬度

    private Double lng;// 经度

    private String poiaddress;

    private String poiname;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getPoiaddress() {
        return poiaddress;
    }

    public void setPoiaddress(String poiaddress) {
        this.poiaddress = poiaddress;
    }

    public String getPoiname() {
        return poiname;
    }

    public void setPoiname(String poiname) {
        this.poiname = poiname;
    }
}
