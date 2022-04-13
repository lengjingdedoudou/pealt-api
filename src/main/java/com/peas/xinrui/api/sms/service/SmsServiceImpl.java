package com.peas.xinrui.api.sms.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peas.xinrui.api.sms.model.Notify;
import com.peas.xinrui.api.sms.model.ValCode;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.cache.KvCacheWrap;
import com.peas.xinrui.common.cache.SingleRepositoryProvider;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.service.ApiTask;
import com.peas.xinrui.common.task.TaskService;
import com.peas.xinrui.common.utils.StringUtils;

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private SmsSender smsSender;

    @Autowired
    private TaskService taskService;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<Long, ValCode> valCodeCache;

    @PostConstruct
    public void init() {
        valCodeCache = kvCacheFactory.create(new CacheOptions("valcode", 1, 300),
                new SingleRepositoryProvider<Long, ValCode>() {
                    @Override
                    public ValCode findByKey(Long key) throws Exception {
                        throw new RuntimeException();
                    }
                }, new BeanModelConverter<>(ValCode.class));
    }

    @Override
    public void sendValCode(ValCode valCode) throws Exception {
        if (valCode.getAccountType() == null) {
            throw new ArgumentServiceException("accountType");
        }
        if (!StringUtils.validMobile(valCode.getAccount())) {
            throw new ArgumentServiceException("account");
        }

        valCode.setKey(System.currentTimeMillis());
        valCode.setCode(StringUtils.randomNumeric(6));

        taskService.addTask(new SendValCodeTask(valCode));
    }

    @Override
    public void sendNotiy(Notify notify) throws Exception {
        if (StringUtils.isEmpty(notify.getContent())) {
            throw new ArgumentServiceException("content");
        }

        if (!StringUtils.validMobile(notify.getMobile())) {
            throw new ArgumentServiceException("mobile");
        }

        taskService.addTask(new SendNotifyTask(notify));
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

    private class SendNotifyTask extends ApiTask {
        private Notify notify;

        public SendNotifyTask(Notify notify) {
            this.notify = notify;
        }

        @Override
        protected void doApiWork() throws Exception {
            smsSender.send(notify.getMobile(), notify.getContent());
        }
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
}
