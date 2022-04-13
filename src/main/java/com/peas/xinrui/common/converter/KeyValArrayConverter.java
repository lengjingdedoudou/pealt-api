package com.peas.xinrui.common.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.peas.xinrui.common.model.KeyValue;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter(autoApply = true)
public class KeyValArrayConverter implements AttributeConverter<List<KeyValue>, String> {

    @Override
    public String convertToDatabaseColumn(List<KeyValue> list) {
        return JSON.toJSONString(list);
    }

    @Override
    public List<KeyValue> convertToEntityAttribute(String data) {
        try {
            return JSONArray.parseArray(data, KeyValue.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
