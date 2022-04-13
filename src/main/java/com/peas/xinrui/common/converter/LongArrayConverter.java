package com.peas.xinrui.common.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter(autoApply = true)
public class LongArrayConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> list) {
        return JSON.toJSONString(list);
    }

    @Override
    public List<Long> convertToEntityAttribute(String data) {
        try {
            return JSONArray.parseArray(data, Long.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
