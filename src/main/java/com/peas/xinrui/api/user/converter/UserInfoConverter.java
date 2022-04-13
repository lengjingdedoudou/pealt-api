package com.peas.xinrui.api.user.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;

import com.peas.xinrui.api.user.entity.UserInfo;

@Converter(autoApply = true)
public class UserInfoConverter implements AttributeConverter<UserInfo, String> {
    @Override
    public String convertToDatabaseColumn(UserInfo attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public UserInfo convertToEntityAttribute(String dbData) {
        try {
            return JSON.parseObject(dbData, UserInfo.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}