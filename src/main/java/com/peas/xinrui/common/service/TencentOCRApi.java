package com.peas.xinrui.common.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.BankCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.BankCardOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.SmartStructuralOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.SmartStructuralOCRResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component // 当类不属于 controller service 时使用这个注解来标注这个类
public class TencentOCRApi {
    @Value("${ocr.secret-id}")
    private String secretId;
    @Value("${ocr.secret-key}")
    private String secretKey;

    private OcrClient ocrClient = null;

    @PostConstruct
    public void init() {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ocr.ap-shanghai.tencentcloudapi.com");
        ClientProfile profile = new ClientProfile(ClientProfile.SIGN_TC3_256, httpProfile);
        ocrClient = new OcrClient(cred, "ap-shanghai", profile);

    }

    public IDCardOCRResponse readIdCart(String img, boolean isFace) throws Exception {
        try {
            IDCardOCRRequest req = new IDCardOCRRequest();
            req.setImageUrl(img);
            req.setCardSide(isFace ? "FRONT" : "BACK");
            return ocrClient.IDCardOCR(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(1);
        }
    }

    public Map<String, Object> readBankCard(String imgUrl) throws TencentCloudSDKException {
        BankCardOCRRequest req = new BankCardOCRRequest();
        req.setImageUrl(imgUrl);
        BankCardOCRResponse response = ocrClient.BankCardOCR(req);
        return CollectionUtils.arrayAsMap("cardNo", response.getCardNo(), "bankInfo", response.getBankInfo());
    }

    public SmartStructuralOCRResponse readVoucher(String img) throws Exception {
        try {
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.ap-shanghai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            OcrClient client = new OcrClient(cred, "ap-shanghai", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SmartStructuralOCRRequest req = new SmartStructuralOCRRequest();
            req.setImageUrl(img);
            // 返回的resp是一个SmartStructuralOCRResponse的实例，与请求对象对应
            SmartStructuralOCRResponse resp = client.SmartStructuralOCR(req);
            // 输出json格式的字符串回包
            return resp;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return null;
    }

}
