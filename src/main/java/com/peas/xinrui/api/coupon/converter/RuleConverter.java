package com.peas.xinrui.api.coupon.converter;

import com.alibaba.fastjson.JSON;
import com.peas.xinrui.api.coupon.enyity.Rule;

import javax.persistence.AttributeConverter;

/**
 * Create by 李振威 2021/12/23 11:31
 */
public class RuleConverter implements AttributeConverter<Rule, String> {
    @Override
    public String convertToDatabaseColumn(Rule attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public Rule convertToEntityAttribute(String dbData) {
        try {
            return JSON.parseObject(dbData, Rule.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
