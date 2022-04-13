package com.peas.xinrui.api.common.service;

import java.util.Map;

import com.peas.xinrui.api.sms.model.ValCode;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.SmartStructuralOCRResponse;

public interface CommonService {
    void saveValCode(Long key, ValCode valCode);

    void sendValCode(ValCode valcode) throws Exception;

    ValCode getValCode(Long key);

    void removeValCode(Long key);

    Map geocoder(String lat, String lng, String key);

    Map<String, Object> getOem(Integer schoolId);

    IDCardOCRResponse readIdCard(String url);

    SmartStructuralOCRResponse readVoucherInfo(String url);
}