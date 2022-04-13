package com.peas.xinrui.api.trade.converter;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.peas.xinrui.api.trade.entity.TradeItem;

@Converter(autoApply = true)
public class TradeItemConverter implements AttributeConverter<List<TradeItem>, String> {
    @Override
    public String convertToDatabaseColumn(List<TradeItem> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<TradeItem> convertToEntityAttribute(String dbData) {
        try {
            return JSONArray.parseArray(dbData, TradeItem.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}