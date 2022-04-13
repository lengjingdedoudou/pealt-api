package com.peas.xinrui.api.school.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.peas.xinrui.api.school.entity.Location;

@Converter(autoApply = true)
public class LocationConverter implements AttributeConverter<Location, String> {
    @Override
    public String convertToDatabaseColumn(Location attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public Location convertToEntityAttribute(String dbData) {
        try {
            return JSON.parseObject(dbData, Location.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}