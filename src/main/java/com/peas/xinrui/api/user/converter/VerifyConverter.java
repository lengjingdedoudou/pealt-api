package com.peas.xinrui.api.user.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.peas.xinrui.api.user.entity.Verify;

@Converter(autoApply = true)
public class VerifyConverter implements AttributeConverter<Verify, String> {
    @Override
    public String convertToDatabaseColumn(Verify attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public Verify convertToEntityAttribute(String dbData) {
        try {
            return JSON.parseObject(dbData, Verify.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}