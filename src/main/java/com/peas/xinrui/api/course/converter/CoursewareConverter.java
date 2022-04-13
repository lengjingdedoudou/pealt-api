package com.peas.xinrui.api.course.converter;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.peas.xinrui.api.course.entity.Courseware;

@Converter(autoApply = true)
public class CoursewareConverter implements AttributeConverter<List<Courseware>, String> {
    @Override
    public String convertToDatabaseColumn(List<Courseware> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<Courseware> convertToEntityAttribute(String dbData) {
        try {
            return JSONArray.parseArray(dbData, Courseware.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}