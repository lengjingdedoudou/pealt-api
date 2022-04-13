package com.peas.xinrui.api.common.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.peas.xinrui.api.school.service.SchoolService;
import com.peas.xinrui.api.sms.model.ValCode;
import com.peas.xinrui.api.sms.service.SmsSender;
import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.service.UserService;
import com.peas.xinrui.common.L;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.cache.KvCacheWrap;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.model.ErrorCode;
import com.peas.xinrui.common.service.ApiTask;
import com.peas.xinrui.common.service.TencentOCRApi;
import com.peas.xinrui.common.task.TaskService;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.SimpleHttpClient;
import com.peas.xinrui.common.utils.StringUtils;
import com.sunnysuperman.commons.util.FormatUtil;
import com.sunnysuperman.commons.util.JSONUtil;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.SmartStructuralOCRResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private SmsSender smsSender;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private TencentOCRApi tencentOCRApi;

    @Autowired
    private KvCacheFactory factory;

    private KvCacheWrap<Long, ValCode> valCodeCache;

    @PostConstruct
    private void init() {
        valCodeCache = factory.create(new CacheOptions("val-code", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, ValCode>() {

                    @Override
                    public ValCode findByKey(Long key) throws Exception {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public Map<Long, ValCode> findByKeys(Collection<Long> keys) throws Exception {
                        // TODO Auto-generated method stub
                        return null;
                    }

                }, new BeanModelConverter<>(ValCode.class));

    }

    @Override
    public void saveValCode(Long key, ValCode valCode) {
        valCodeCache.save(key, valCode);
    }

    @Override
    public void sendValCode(ValCode valCode) throws Exception {
        if (valCode.getAccountType() == null) {
            throw new ArgumentServiceException("accountType");
        }
        if (!StringUtils.validMobile(valCode.getAccount())) {
            throw new ArgumentServiceException("account");
        }

        valCode.setKey(valCode.getKey());
        valCode.setCode(StringUtils.randomNumeric(6));

        taskService.addTask(new SendValCodeTask(valCode));
    }

    @Override
    public ValCode getValCode(Long key) {
        if (key == null) {
            throw new ArgumentServiceException("key");
        }

        return valCodeCache.findByKey(key);
    }

    @Override
    public void removeValCode(Long key) {
        if (key == null) {
            throw new ArgumentServiceException("key");
        }

        valCodeCache.remove(key);
    }

    private class SendValCodeTask extends ApiTask {
        private ValCode valCode;

        public SendValCodeTask(ValCode valCode) {
            this.valCode = valCode;
        }

        @Override
        protected void doApiWork() throws Exception {
            boolean success = smsSender.send(valCode.getAccount(), valCode.getCode());
            if (success) {
                valCodeCache.save(valCode.getKey(), valCode);
            }
        }
    }

    @Override
    public Map geocoder(String lat, String lng, String key) {
        String url = "http://apis.map.qq.com/ws/geocoder/v1/?location=" + lat + "," + lng + "&key=" + key;

        Map<String, Object> params = new HashMap<>();

        SimpleHttpClient client = new SimpleHttpClient();
        String responseString = "";
        try {
            responseString = client.get(url, params, CollectionUtils.arrayAsMap());
        } catch (Exception e) {
            throw new ArgumentServiceException("geocode");
        }
        if (L.isInfoEnabled()) {
            L.info("mbyApi - " + url + ", result: " + responseString);
        }
        Map<?, ?> response = JSONUtil.parseJSONObject(responseString);
        Integer status = FormatUtil.parseInteger(response.get("status"));
        if (L.isInfoEnabled()) {
            L.info("response status = " + status);
        }
        return response;
    }

    @Override
    public Map<String, Object> getOem(Integer schoolId) {
        return schoolService.oem(schoolId);
    }

    @Override
    public IDCardOCRResponse readIdCard(String img) {
        IDCardOCRResponse card;
        try {
            card = tencentOCRApi.readIdCart(img, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.ERROR_WX_AUTH_FAIL);
        }
        String identity = card.getIdNum();
        User user = userService.findByIdentity(identity);
        // if (user != null) {
        // throw new DetailedServiceException("该身份证已被认证，请重试");
        // }
        return card;
    }

    @Override
    public SmartStructuralOCRResponse readVoucherInfo(String url) {
        SmartStructuralOCRResponse voucher;
        try {
            voucher = tencentOCRApi.readVoucher(url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.ERROR_WX_AUTH_FAIL);
        }

        return voucher;
    }
}