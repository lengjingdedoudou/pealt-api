package com.peas.xinrui.api.user.service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.peas.xinrui.api.common.service.CommonService;
import com.peas.xinrui.api.file.service.FileService;
import com.peas.xinrui.api.heavywork.model.ExportExcelTask;
import com.peas.xinrui.api.heavywork.model.HeavyWork;
import com.peas.xinrui.api.heavywork.service.HeavyWorkService;
import com.peas.xinrui.api.schadmin.entity.SchAdminContexts;
import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.schadmin.service.SchAdminService;
import com.peas.xinrui.api.sms.model.ValCode;
import com.peas.xinrui.api.user.entity.UserContexts;
import com.peas.xinrui.api.user.entity.UserError;
import com.peas.xinrui.api.user.entity.UserSessionWrapper;
import com.peas.xinrui.api.user.entity.UserState;
import com.peas.xinrui.api.user.entity.Verify;
import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.model.UserSession;
import com.peas.xinrui.api.user.qo.UserQo;
import com.peas.xinrui.api.user.repository.UserRepository;
import com.peas.xinrui.api.user.repository.UserSessionRepository;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.cache.KvCacheWrap;
import com.peas.xinrui.common.cache.SingleRepositoryProvider;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DetailedServiceException;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.exception.SessionServiceException;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.model.ByteUtils;
import com.peas.xinrui.common.task.TaskService;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.StringUtils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserError {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserSessionRepository sessionRepository;

    @Autowired
    private HeavyWorkService heavyWorkService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private SchAdminService schAdminService;

    @Autowired
    private FileService fileService;

    @Value("${admin.salt}")
    private String salt;

    @Value("${admin.tokenHour}")
    private Integer tokenHour;

    @Value("${val.code}")
    private String code;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<Long, User> userCache;

    private KvCacheWrap<String, UserSession> sessionCache;

    @PostConstruct
    public void init() {
        userCache = kvCacheFactory.create(new CacheOptions("user", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, User>() {
                    @Override
                    public User findByKey(Long key) throws Exception {
                        return repository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, User> findByKeys(java.util.Collection<Long> keys) throws Exception {
                        return repository.findByIdIn(keys).stream().collect(Collectors.toMap(User::getId, t -> t));
                    }
                }, new BeanModelConverter<>(User.class));

        sessionCache = kvCacheFactory.create(new CacheOptions("session", 1, DateUtils.SECOND_PER_DAY),
                new SingleRepositoryProvider<String, UserSession>() {
                    @Override
                    public UserSession findByKey(String key) throws Exception {
                        return sessionRepository.findByToken(key);
                    }
                }, new BeanModelConverter<>(UserSession.class));
    }

    @Transactional
    @Override
    public UserSessionWrapper signin(User user, ValCode valCode) {
        String mobile = user.getMobile();
        validValCodeAndCountryCode(mobile, user.getCountryCode(), valCode);

        if (user.getSchoolId() == 0) {
            throw new ArgumentServiceException("schoolId");
        }

        String cMobile = user.getCountryCode() + "-" + mobile;
        User exist = repository.findByMobileAndSchoolId(cMobile, user.getSchoolId());
        if (exist == null) {
            user.setCreatedAt(System.currentTimeMillis());
            user.setStatus(UserState.Enable.getState());
            user.setMobile(cMobile);
            exist = repository.save(user);
        }

        UserSession session = new UserSession();
        String token = StringUtils.randomAlphanumeric(64);
        Long curTime = System.currentTimeMillis();
        Long expireTime = curTime + tokenHour * 3600 * 1000;
        session.setUserId(exist.getId());
        session.setToken(token);
        session.setSigninAt(curTime);
        session.setExpireAt(expireTime);

        sessionRepository.save(session);

        return new UserSessionWrapper(session, exist);
    }

    @Override
    public void validMobile(User user, ValCode valCode) {
        String mobile = user.getMobile();

        validValCodeAndCountryCode(mobile, user.getCountryCode(), valCode);

        mobile = user.getCountryCode() + "-" + mobile;
        String pfMobile = UserContexts.getUser().getMobile();
        if (!pfMobile.equals(mobile)) {
            throw new DetailedServiceException("输入的手机号有误");
        }

    }

    @Transactional
    @Override
    public void modProfileMobile(User user, ValCode valCode) {
        String mobile = user.getMobile();

        validValCodeAndCountryCode(mobile, user.getCountryCode(), valCode);

        String cMobile = user.getCountryCode() + "-" + mobile;
        User exist = repository.findByMobileAndSchoolId(cMobile, user.getSchoolId());
        if (exist != null) {
            throw new DetailedServiceException("该手机号已注册");
        }

        repository.updateByMobile(UserContexts.getUserId(), cMobile);
        userCache.remove(UserContexts.getUserId());
    }

    private void validValCodeAndCountryCode(String mobile, String countryCode, ValCode valCode) {
        if (StringUtils.isNotChinaMobile(mobile)) {
            throw new ArgumentServiceException("mobile");
        }

        if (StringUtils.isEmpty(countryCode)) {
            throw new ArgumentServiceException("countryCode");
        }

        if (!mobile.equals(valCode.getAccount())) {
            throw new ArgumentServiceException("valCode");
        }

        ValCode valCode2 = commonService.getValCode(valCode.getKey());
        if (valCode2 == null || !valCode.getAccount().equals(valCode2.getAccount())) {
            throw new ArgumentServiceException("valCode");
        }

        if (!valCode.getAccountType().equals(valCode2.getAccountType())) {
            throw new ArgumentServiceException("valCode");
        }

        if (StringUtils.isEmpty(valCode.getCode()) || !valCode.getCode().equals(valCode2.getCode())) {
            throw new ArgumentServiceException("code");
        }
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        Integer schoolId = SchAdminContexts.getSchoolId();
        String mobile = user.getMobile();
        if (StringUtils.isNotChinaMobile(mobile)) {
            throw new ArgumentServiceException("mobile");
        }
        if (StringUtils.isEmpty(user.getCountryCode())) {
            throw new ArgumentServiceException("countryCode");
        }
        if (StringUtils.isEmpty(user.getName())) {
            throw new ArgumentServiceException("name");
        }
        if (user.getSex() == null) {
            throw new ArgumentServiceException("sex");
        }
        Verify verify = user.getVerify();
        if (user.getIdentity() == null) {
            throw new ArgumentServiceException("identity");
        }
        if (verify.getIdCardFront() == null) {
            throw new ArgumentServiceException("id-card");
        }
        if (verify.getAddressCode() == null) {
            throw new ArgumentServiceException("address-code");
        }
        if (user.getSalesmanId() != 0) {
            user.setSalesmanId(SchAdminContexts.getAdminId());
            user.setSchoolId(schoolId);
            SchAdmin schAdmin = schAdminService.admin(user.getSalesmanId());
            if (schAdmin.getSchoolId() != schoolId) {
                throw new DetailedServiceException("不能处理其他学校事务");
            }
        }

        // User exist = repository.findByNameAndSchoolId(user.getName(), schoolId);
        // if (user.getId() == 0 && exist != null) {
        // throw new ArgumentServiceException("name");
        // }
        // if (user.getId() != 0 && (exist != null && exist.getId() != user.getId())) {
        // throw new ArgumentServiceException("name");
        // }

        user.setMobile(user.getCountryCode() + "-" + user.getMobile());
        User eUser = repository.findByIdentity(user.getIdentity());
        if (user.getId() == 0) {
            if (eUser != null) {
                throw new DetailedServiceException("该身份证已被认证");
            }
            user.setSchoolId(schoolId);
            user.setCreatedAt(System.currentTimeMillis());
            user.setType(ByteUtils.BYTE_1);
            user.setStatus(ByteUtils.BYTE_1);
        } else {
            User exist = getById(user.getId());
            if (eUser != null && eUser.getId() != exist.getId()) {
                throw new DetailedServiceException("该身份证已被认证");
            }
            exist.setVerify(verify);
            exist.setName(user.getName());
            exist.setIdentity(user.getIdentity());
            exist.setSalesmanId(user.getSalesmanId());
            exist.setMobile(user.getMobile());
            exist.setSex(user.getSex());
            user = exist;
        }
        repository.save(user);
        userCache.remove(user.getId());

    }

    @Override
    public Set<User> findByIdIn(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        return repository.findByIdIn(ids);
    }

    @Override
    public Map<Long, User> findByIds(Set<Long> ids) {
        return userCache.findByKeys(ids);
    }

    @Override
    public Page<User> users(UserQo qo) {
        return repository.findAll(qo);
    }

    @Override
    public UserSessionWrapper findByToken(String token) {
        UserSession session = sessionCache.findByKey(token);
        if (session == null || session.getExpireAt() < System.currentTimeMillis()) {
            throw new SessionServiceException();
        }

        Long userId = session.getUserId();
        User user = getById(userId);
        return new UserSessionWrapper(session, user);
    }

    @Override
    public User profile() {
        return UserContexts.getUser();
    }

    @Transactional
    @Override
    public void saveProfile(User user) {
        String name = user.getName();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(name) || name.length() > 8) {
            throw new ArgumentServiceException("name");
        }

        Long curUserId = UserContexts.getUserId();
        User curUser = repository.findById(curUserId).orElse(null);

        user.setId(curUserId);
        user.setMobile(curUser.getMobile());

        repository.save(user);
        userCache.remove(curUserId);
    }

    @Override
    public void state(Long id, Byte state) {
        getById(id);
        if (state != UserState.Enable.getState() && state != UserState.Disable.getState()) {
            throw new ArgumentServiceException("state");
        }

        repository.updateByState(id, state);
    }

    @Override
    public HeavyWork exportUsers(UserQo qo) {
        HeavyWork work = heavyWorkService.create(String.valueOf(UserContexts.getUserId()));
        taskService.addTask(new ProgressDisplayTask(work.getSecret()));
        taskService.addTask(new ExportUsersTask(work, qo));
        return work;
    }

    class ProgressDisplayTask implements Runnable {
        String secret;
        HeavyWorkService heavyWorkService;
        Integer progress = 0;

        public ProgressDisplayTask(String secret) {
            this.secret = secret;
            heavyWorkService = UserServiceImpl.this.heavyWorkService;
        }

        @Override
        public void run() {
            while (true) {
                int curPress = progress;
                progress = heavyWorkService.getProgress(secret);

                if (progress != curPress) {
                    System.out.println("当前任务进度：  " + progress + "%");
                }
                if (progress == 100) {
                    System.out.println("上传成功！！！");
                    break;
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ExportUsersTask extends ExportExcelTask {
        HeavyWork work;
        UserQo qo;

        public ExportUsersTask(HeavyWork work, UserQo qo) {
            super(work, UserServiceImpl.this.heavyWorkService, UserServiceImpl.this.fileService);
            this.qo = qo;
        }

        @Override
        protected Workbook writeToWorkbook() throws Exception {
            int batchNum = 1;
            SXSSFWorkbook wb = new SXSSFWorkbook(batchNum);
            SXSSFSheet sheet = wb.createSheet("用户");
            int rowIndex = 0;
            {
                int cellIndex = -1;
                Row row = sheet.createRow(rowIndex++);
                row.createCell(++cellIndex).setCellValue("姓名");
                row.createCell(++cellIndex).setCellValue("手机号");
            }
            qo.setPageSize(batchNum);
            while (true) {
                Page<User> page = users(qo);
                if (page.isEmpty()) {
                    break;
                }
                for (User item : page.getContent()) {
                    int cellIndex = -1;
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(++cellIndex).setCellValue(item.getName());
                    row.createCell(++cellIndex).setCellValue(item.getMobile());
                }
                if (page.getContent().size() < batchNum) {
                    break;
                }

                qo.setPageNumber(qo.getPageNumber() + 2);
                doUpdateProgress(getProgress() + 90 / page.getTotalPages());
            }
            return wb;
        }

    }

    @Override
    public User getById(Long id) {
        User user = findById(id);

        if (user == null) {
            throw new ServiceException(ERR_USER_NOT_FOUND);
        }
        return user;
    }

    private User findById(Long id) {
        if (id == null) {
            throw new ServiceException(ERR_ILLEGAL_ARGUMENT);
        }
        return userCache.findByKey(id);
    }

    @Override
    public void auth(User user) {
        User exist = getById(user.getId());
        Verify verify = user.getVerify();
        if (user.getIdentity() == null) {
            throw new ArgumentServiceException("identity");
        }
        if (user.getName() == null) {
            throw new ArgumentServiceException("name");
        }
        if (verify.getIdCardFront() == null) {
            throw new ArgumentServiceException("id-card");
        }
        if (verify.getAddressCode() == null) {
            throw new ArgumentServiceException("address-code");
        }

        User eUser = repository.findByIdentity(user.getIdentity());
        if (eUser != null) {
            throw new DetailedServiceException("改身份证已被认证");
        }

        exist.setVerify(verify);
        exist.setName(user.getName());
        exist.setIdentity(user.getIdentity());

        repository.save(exist);
        userCache.remove(user.getId());
    }

    @Override
    public User findByIdentity(String identity) {
        if (StringUtils.isEmpty(identity)) {
            throw new ArgumentServiceException("identity");
        }
        return repository.fineByIdentity(identity);
    }

    @Override
    public void status(Long id, Byte state) {
        User user = getById(id);
        user.setStatus(state);

        repository.save(user);
    }
}