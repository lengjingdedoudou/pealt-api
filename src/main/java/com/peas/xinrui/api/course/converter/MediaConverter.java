package com.peas.xinrui.api.course.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.peas.xinrui.api.course.entity.Media;

@Converter(autoApply = true)
public class MediaConverter implements AttributeConverter<Media, String> {
    @Override
    public String convertToDatabaseColumn(Media attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public Media convertToEntityAttribute(String dbData) {
        try {
            return JSON.parseObject(dbData, Media.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}