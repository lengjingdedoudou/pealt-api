package com.peas.xinrui.api.trade.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.peas.xinrui.api.trade.entity.BankInfo;

@Converter(autoApply = true)
public class BankInfoConverter implements AttributeConverter<BankInfo, String> {
    @Override
    public String convertToDatabaseColumn(BankInfo attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public BankInfo convertToEntityAttribute(String dbData) {
        try {
            return JSON.parseObject(dbData, BankInfo.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}