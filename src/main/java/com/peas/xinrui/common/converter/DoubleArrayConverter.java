package com.peas.xinrui.common.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter(autoApply = true)
public class DoubleArrayConverter implements AttributeConverter<List<Double>, String> {

    @Override
    public String convertToDatabaseColumn(List<Double> list) {
        return JSON.toJSONString(list);
    }

    @Override
    public List<Double> convertToEntityAttribute(String data) {
        try {
            return JSONArray.parseArray(data, Double.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
