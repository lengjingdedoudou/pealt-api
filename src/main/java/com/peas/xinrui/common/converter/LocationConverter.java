package com.peas.xinrui.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;

import com.peas.xinrui.common.model.Location;

@Converter(autoApply = true)
public class LocationConverter implements AttributeConverter<Location, String> {

    @Override
    public String convertToDatabaseColumn(Location obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public Location convertToEntityAttribute(String data) {
        try {
            return JSON.parseObject(data, Location.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
