package com.peas.xinrui.common.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;

@SuppressWarnings("all")
@Converter(autoApply = true)
public class JsonStringMapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {
        return JSON.toJSONString(map);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String data) {
        try {
            return JSONObject.parseObject(data, Map.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
