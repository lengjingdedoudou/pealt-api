package com.peas.xinrui.api.coupon.converter;

import com.alibaba.fastjson.JSON;
import com.peas.xinrui.api.coupon.enyity.Payload;

import javax.persistence.AttributeConverter;

/**
 * Create by 李振威 2021/12/23 11:30
 */
public class PayloadConverter implements AttributeConverter<Payload, String> {
    @Override
    public String convertToDatabaseColumn(Payload attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public Payload convertToEntityAttribute(String dbData) {
        try {
            return JSON.parseObject(dbData, Payload.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
