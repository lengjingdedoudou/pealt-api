package com.peas.xinrui.api.course.converter;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.peas.xinrui.api.course.entity.AuthCourse;

@Converter(autoApply = true)
public class AuthCoursesConverter implements AttributeConverter<List<AuthCourse>, String> {
    @Override
    public String convertToDatabaseColumn(List<AuthCourse> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<AuthCourse> convertToEntityAttribute(String dbData) {
        try {
            return JSONArray.parseArray(dbData, AuthCourse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}