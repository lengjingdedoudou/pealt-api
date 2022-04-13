package com.peas.xinrui.api.sms.service;

import com.peas.xinrui.api.sms.model.Notify;
import com.peas.xinrui.api.sms.model.ValCode;

public interface SmsService {
    void sendNotiy(Notify notify) throws Exception;

    void sendValCode(ValCode valcode) throws Exception;

    ValCode getValCode(Long key);

    void removeValCode(Long key);
}