package com.peas.xinrui.api.trade.converter;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.peas.xinrui.api.trade.entity.CollectionItem;

@Converter(autoApply = true)
public class CollectionItemConverter implements AttributeConverter<List<CollectionItem>, String> {
    @Override
    public String convertToDatabaseColumn(List<CollectionItem> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<CollectionItem> convertToEntityAttribute(String dbData) {
        try {
            return JSONArray.parseArray(dbData, CollectionItem.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}