package com.peas.xinrui.api.sms.service;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.peas.xinrui.api.sms.model.SmsConfig;

@Component
public class SmsSender {

    private static final String TEMPLATE_CODE = "SMS_167970270";

    @Autowired
    private SmsConfig smsConfig;

    public com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(smsConfig.getKey())
                // 您的AccessKey Secret
                .setAccessKeySecret(smsConfig.getSecret());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public boolean send(String mobile, String code) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = createClient();
        SendSmsRequest sendSmsRequest = new SendSmsRequest().setPhoneNumbers(mobile)
                .setSignName(smsConfig.getSignature()).setTemplateCode(TEMPLATE_CODE)
                .setTemplateParam("{\"code\":\"" + code + "\"}");

        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse response = client.sendSms(sendSmsRequest);
        if ("OK".equalsIgnoreCase(response.getBody().getCode())) {
            return true;
        }
        return false;
    }
}
