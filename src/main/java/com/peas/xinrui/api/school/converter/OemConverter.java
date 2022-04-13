package com.peas.xinrui.api.school.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.peas.xinrui.api.school.entity.Oem;

@Converter(autoApply = true)
public class OemConverter implements AttributeConverter<Oem, String> {
    @Override
    public String convertToDatabaseColumn(Oem attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public Oem convertToEntityAttribute(String dbData) {
        try {
            return JSON.parseObject(dbData, Oem.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}