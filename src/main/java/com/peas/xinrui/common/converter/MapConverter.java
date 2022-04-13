package com.peas.xinrui.common.converter;

import com.alibaba.fastjson.JSON;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;

@SuppressWarnings("all")
@Converter(autoApply = true)
public class MapConverter implements AttributeConverter<Map, String> {

    @Override
    public String convertToDatabaseColumn(Map obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public Map convertToEntityAttribute(String data) {
        try {
            return (Map) JSON.parse(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
