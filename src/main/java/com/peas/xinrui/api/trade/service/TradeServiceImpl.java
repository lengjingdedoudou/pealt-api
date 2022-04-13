package com.peas.xinrui.api.trade.service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.peas.xinrui.api.bill.entity.ReferTypeEnum;
import com.peas.xinrui.api.bill.model.Bill;
import com.peas.xinrui.api.bill.service.BillService;
import com.peas.xinrui.api.course.model.Course;
import com.peas.xinrui.api.course.model.CoursePkg;
import com.peas.xinrui.api.course.service.CourseService;
import com.peas.xinrui.api.file.entity.UploadOptions;
import com.peas.xinrui.api.file.service.FileService;
import com.peas.xinrui.api.heavywork.model.HeavyWork;
import com.peas.xinrui.api.heavywork.service.HeavyWorkService;
import com.peas.xinrui.api.schadmin.entity.SchAdminContexts;
import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.schadmin.model.SchRole;
import com.peas.xinrui.api.schadmin.service.SchAdminService;
import com.peas.xinrui.api.trade.entity.CartWO;
import com.peas.xinrui.api.trade.entity.CollectionItem;
import com.peas.xinrui.api.trade.entity.CollectionTypeEnum;
import com.peas.xinrui.api.trade.entity.CourseBillAuditDTO;
import com.peas.xinrui.api.trade.entity.TradeAuditDTO;
import com.peas.xinrui.api.trade.model.Cart;
import com.peas.xinrui.api.trade.model.CollectionSettings;
import com.peas.xinrui.api.trade.model.CourseBill;
import com.peas.xinrui.api.trade.model.Trade;
import com.peas.xinrui.api.trade.qo.CartQo;
import com.peas.xinrui.api.trade.qo.CourseBillQo;
import com.peas.xinrui.api.trade.qo.TradeQo;
import com.peas.xinrui.api.trade.repository.CartRepository;
import com.peas.xinrui.api.trade.repository.CollectionSettingsRepository;
import com.peas.xinrui.api.trade.repository.CourseBillRepository;
import com.peas.xinrui.api.trade.repository.TradeReporitory;
import com.peas.xinrui.api.user.entity.UserContexts;
import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.service.UserService;
import com.peas.xinrui.common.L;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.controller.auth.SessionType;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DetailedServiceException;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.kvCache.KvCache;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.model.Constants;
import com.peas.xinrui.common.service.ApiTask;
import com.peas.xinrui.common.task.TaskService;
import com.peas.xinrui.common.utils.ByteUtils;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.StringUtils;
import com.sunnysuperman.commons.util.FileUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private CourseService courseService;

    @Autowired
    private BillService billService;

    @Autowired
    private UserService userService;

    @Autowired
    private SchAdminService schAdminService;

    @Autowired
    private HeavyWorkService heavyWorkService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TradeReporitory tradeRepository;

    @Autowired
    private CourseBillRepository courseBillRepository;

    @Autowired
    private CollectionSettingsRepository settingsRepository;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCache<Long, Cart> cartCache;
    private KvCache<String, Trade> tradeCache;

    @PostConstruct
    private void init() {
        cartCache = kvCacheFactory.create(new CacheOptions("cart", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, Cart>() {
                    @Override
                    public Cart findByKey(Long key) throws Exception {
                        return cartRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, Cart> findByKeys(Collection<Long> keys) throws Exception {
                        Map<Long, Cart> map = new HashMap<>();
                        for (Long key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(Cart.class));
        tradeCache = kvCacheFactory.create(new CacheOptions("trade", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<String, Trade>() {
                    @Override
                    public Trade findByKey(String key) throws Exception {
                        return tradeRepository.findByTradeNumber(key);
                    }

                    @Override
                    public Map<String, Trade> findByKeys(Collection<String> keys) throws Exception {
                        Map<String, Trade> map = new HashMap<>();
                        for (String key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(Trade.class));
    }

    @Override
    public void addCart(Cart cart) {
        User user = UserContexts.getUser();
        if (cart.getPkgId() == null) {
            throw new ArgumentServiceException("pkgId");
        }
        if (cart.getCourseId() == null) {
            throw new ArgumentServiceException("courseId");
        }

        Cart exist = cartRepository.findByCourseIdAndSchoolIdAndUserId(cart.getCourseId(), user.getSchoolId(),
                user.getId());
        if (exist != null) {
            if (exist.getPkgId() == cart.getPkgId()) {
                return;
            }
            cartRepository.deleteById(exist.getId());
            cartCache.remove(exist.getId());
        }
        List<Trade> trades = tradeRepository.findByUserId(user.getId());
        if (CollectionUtils.isNotEmpty(trades)) {
            trades.forEach((trade) -> {
                trade.getItems().forEach((item) -> {
                    if (item.getCourseId() == cart.getCourseId() && trade.getStatus() != Constants.TRADE_CANCEL) {
                        throw new DetailedServiceException("已购买该课程");
                    }
                });
            });
        }

        cart.setUserId(user.getId());
        cart.setSchoolId(user.getSchoolId());
        cart.setCreatedAt(System.currentTimeMillis());

        cartRepository.save(cart);
    }

    @Override
    public Page<Cart> carts(CartQo qo, CartWO wo) {
        User cUser = UserContexts.getUser();
        qo.setSchoolId(cUser.getSchoolId());
        qo.setUserId(cUser.getId());
        Page<Cart> page = cartRepository.findAll(qo);
        wrapCarts(page.getContent(), wo);
        return page;
    }

    @Override
    public List<Cart> cartForList(CartQo qo, CartWO wo) {
        User cUser = UserContexts.getUser();
        qo.setSchoolId(cUser.getSchoolId());
        qo.setUserId(cUser.getId());
        List<Cart> carts = cartRepository.findAllForList(qo);
        wrapCarts(carts, wo);
        return carts;
    }

    private void wrapCarts(Collection<Cart> carts, CartWO wo) {
        if (CollectionUtils.isNotEmpty(carts)) {
            Set<Long> pkgIds = new HashSet<>();
            Set<Long> cIds = new HashSet<>();
            carts.stream().forEach(cart -> {
                pkgIds.add(cart.getPkgId());
                cIds.add(cart.getCourseId());
            });
            Map<Long, CoursePkg> pkgMap = courseService.coursePkgsForMap(pkgIds);
            Map<Long, Course> cMap = courseService.coursesForMap(cIds);
            carts.stream().forEach(cart -> {
                cart.setCourse(cMap.get(cart.getCourseId()));
                cart.setPkg(pkgMap.get(cart.getPkgId()));
            });
        }
    }

    private Cart findCartById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return cartCache.findByKey(id);
    }

    private Cart getCartById(Long id) {
        Cart cart = findCartById(id);
        if (cart == null) {
            throw new ArgumentServiceException("cart");
        }
        return cart;
    }

    @Override
    public Trade trade(Long id) {
        return getTradeById(id);
    }

    @Override
    public Trade tradeByTradeNumber(String tradeNumber) {
        if (tradeNumber == null) {
            throw new ArgumentServiceException("tradeNumber");
        }
        Trade trade = tradeCache.findByKey(tradeNumber);
        List<CourseBill> courseBills = courseBillRepository.findByTradeNumber(tradeNumber);
        int unApprovedAmount = 0;
        int approvedFieldAmount = 0;
        for (CourseBill cb : courseBills) {
            if (cb.getStatus() == Constants.BILL_WEIT) {
                unApprovedAmount += cb.getPaidAmount();
            }
            if (cb.getStatus() == Constants.BILL_AUDIT_FAIL) {
                approvedFieldAmount += cb.getPaidAmount();
            }
        }
        trade.setUnApprovedAmount(unApprovedAmount);
        trade.setApprovalFailedAmount(approvedFieldAmount);
        return trade;
    }

    private Trade findTradeById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return tradeRepository.findById(id).orElse(null);
    }

    private Trade getTradeById(Long id) {
        Trade trade = findTradeById(id);
        if (trade == null) {
            throw new ArgumentServiceException("trade");
        }
        return trade;
    }

    @Override
    public Page<Trade> trades(TradeQo qo) {
        Page<Trade> page = tradeRepository.findAll(qo);
        wrapTrades(page.getContent());
        return page;
    }

    private void wrapTrades(Collection<Trade> trades) {
        if (CollectionUtils.isNotEmpty(trades)) {
            Set<Long> uIds = trades.stream().map((trade) -> trade.getUserId()).collect(Collectors.toSet());
            Set<User> users = userService.findByIdIn(uIds);
            Map<Long, User> uMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));
            trades.stream().forEach(trade -> trade.setUser(uMap.get(trade.getUserId())));
        }
    }

    @Transactional
    @Override
    public Long submitTrade(Trade trade) {
        if (trade.getTotal() == null) {
            throw new ArgumentServiceException("total");
        }
        if (trade.getItems() == null) {
            throw new ArgumentServiceException("items");
        }

        User user = UserContexts.getUser();
        long curTime = System.currentTimeMillis();
        if (trade.getId() != null) {
            throw new ServiceException(1);
        }

        trade.setCreatedAt(curTime);
        trade.setSchoolId(user.getSchoolId());
        trade.setUserId(user.getId());
        trade.setTradeNumber(StringUtils.getOrderNumber());
        trade.setStatus(Constants.TRADE_NOT_PAY);
        Date date1 = new Date(curTime + 30 * 60 * 1000);
        trade.setEndAt(date1.getTime());

        Trade newTrade = tradeRepository.save(trade);

        List<Long> ids = trade.getItems().stream().map((item) -> item.getCartId()).collect(Collectors.toList());
        cartRepository.deleteByIds(ids);
        ids.forEach(id -> cartCache.remove(id));

        return newTrade.getId();
    }

    @Transactional
    @Override
    public void payTrade(Long tradeId, List<CourseBill> courseBills) {
        if (tradeId == null) {
            throw new DetailedServiceException("tradeId");
        }
        if (CollectionUtils.isEmpty(courseBills)) {
            throw new DetailedServiceException("请添加支付凭证");
        }

        long curTime = System.currentTimeMillis();
        Trade trade = getTradeById(tradeId);

        if (trade.getEndAt() < curTime) {
            throw new DetailedServiceException("订单已过期");
        }
        if (trade.getStatus() != Constants.TRADE_NOT_PAY) {
            throw new ServiceException(1);
        }

        List<String> payNumbers = new ArrayList<>();
        int paidAmount = 0;
        for (CourseBill courseBill : courseBills) {
            if (courseBill.getPaidAmount() == null) {
                throw new ArgumentServiceException("paidAmount");
            }
            if (courseBill.getPayNumber() == null) {
                throw new ArgumentServiceException("payNumber");
            }
            if (courseBill.getPayType() == null) {
                throw new ArgumentServiceException("payType");
            }
            if (courseBill.getVoucherChart() == null) {
                throw new ArgumentServiceException("voucherChart");
            }

            courseBill.setCreatedAt(curTime);
            courseBill.setPaidAt(curTime);
            courseBill.setStatus(Constants.BILL_WEIT);
            courseBill.setUserId(trade.getUserId());
            courseBill.setTradeNumber(trade.getTradeNumber());
            courseBill.setSchoolId(UserContexts.getUser().getSchoolId());
            paidAmount += courseBill.getPaidAmount();

            payNumbers.add(courseBill.getPayNumber());
        }

        List<CourseBill> exists = courseBillRepository.findByPayNumberIn(payNumbers);
        if (CollectionUtils.isNotEmpty(exists)) {
            throw new DetailedServiceException("存在相同流水号");
        }
        courseBillRepository.saveAll(courseBills);

        trade.setPaidAmount(paidAmount);
        trade.setPaidAt(curTime);
        trade.setStatus(Constants.TRADE_PAIED);
        tradeRepository.save(trade);
        tradeCache.remove(trade.getTradeNumber());
    }

    @Transactional
    @Override
    public void auditTrade(TradeAuditDTO dto) {
        if (dto.getTradeId() == null) {
            throw new ArgumentServiceException("id");
        }
        if (dto.getStatus() == null) {
            throw new ArgumentServiceException("status");
        }
        if (dto.getStatus() == Constants.TRADE_DELETE && dto.getRejectReason() == null) {
            throw new ArgumentServiceException("rejectReason");
        }
        Trade trade = getTradeById(dto.getTradeId());
        if (trade.getStatus() != Constants.TRADE_PAIED) {
            throw new ServiceException(1);
        }

        if (dto.getStatus() == Constants.TRADE_AUDITED) {
            List<CourseBill> courseBills = courseBillRepository.findByTradeNumber(trade.getTradeNumber());

            if (CollectionUtils.isEmpty(courseBills)) {
                throw new ServiceException(1);
            }

            int paidAmount = 0;
            for (CourseBill cb : courseBills) {
                if (cb.getStatus() == Constants.BILL_AUDIT_SUCCESS) {
                    paidAmount += cb.getPaidAmount();
                }
            }

            if (paidAmount < trade.getTotal() * 0.8) {
                throw new DetailedServiceException("支付金额不足");
            }
        }
        trade.setStatus(dto.getStatus());
        tradeRepository.save(trade);
        tradeCache.remove(trade.getTradeNumber());
    }

    @Transactional
    @Override
    public void cancelTrade(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        Trade trade = getTradeById(id);
        if (trade.getStatus() != Constants.TRADE_NOT_PAY) {
            throw new ServiceException(1);
        }
        trade.setStatus(Constants.TRADE_CANCEL);
        tradeRepository.save(trade);
        tradeCache.remove(trade.getTradeNumber());
    }

    @Override
    public void saveCollectionSettings(CollectionSettings settings) {
        List<CollectionItem> items = settings.getItems();
        if (CollectionUtils.isNotEmpty(items)) {
            items.forEach(item -> {
                byte type = item.getType();
                if (type == 0) {
                    throw new ArgumentServiceException("type");
                }

                if (type == CollectionTypeEnum.BANK.getVal()) {
                    if (item.getBankInfo() == null) {
                        throw new ArgumentServiceException("bank_info");
                    }
                } else {
                    if (item.getImg() == null) {
                        throw new ArgumentServiceException("img");
                    }
                }
            });
        }

        if (settings.getId() == null) {
            settings.setCreatedAt(System.currentTimeMillis());
            settings.setSchoolId(SchAdminContexts.getSchoolId());
        }

        settingsRepository.save(settings);
    }

    @Override
    public CollectionSettings collectionSettings(SessionType sessionType) {
        int schoolId = 0;
        if (sessionType == SessionType.SCHADMIN) {
            schoolId = SchAdminContexts.getSchoolId();
        }
        if (sessionType == SessionType.USER) {
            schoolId = UserContexts.getUser().getSchoolId();
        }

        return settingsRepository.findBySchoolId(schoolId);
    }

    @Override
    public List<CourseBill> courseBills(String tradeNumber) {
        if (StringUtils.isEmpty(tradeNumber)) {
            throw new ArgumentServiceException("tradeNumber");
        }
        List<CourseBill> bills = courseBillRepository.findByTradeNumber(tradeNumber);
        return bills;
    }

    @Override
    public Page<CourseBill> courseBills(CourseBillQo qo) {
        if (qo == null) {
            throw new ArgumentServiceException("qo");
        }

        return courseBillRepository.findAll(qo);
    }

    @Transactional
    @Override
    public void auditCourseBill(CourseBillAuditDTO dto) {
        if (dto == null) {
            throw new ArgumentServiceException("dto");
        }

        CourseBill courseBill = courseBillRepository.findById(dto.getCourseBillId()).orElse(null);
        if (courseBill == null) {
            throw new ServiceException(1);
        }

        SchRole role = SchAdminContexts.adminSessionWrapper().getRole();
        boolean allowFinancial = false;
        for (String permission : role.getPermissions()) {
            if (permission.equals(SchAdminPermission.BILL_CHECK.name())) {
                allowFinancial = true;
            }
        }

        byte status = courseBill.getStatus();

        if (status == Constants.BILL_WEIT) {
            if (!allowFinancial) {
                throw new DetailedServiceException("没有财务审核权限");
            }
        } else {
            throw new DetailedServiceException("不属于待审核订单");
        }

        if (dto.getStatus() == Constants.BILL_AUDIT_FAIL) {
            if (StringUtils.isEmpty(dto.getRejectReason())) {
                throw new DetailedServiceException("错误信息为空！");
            }
            courseBill.setRejectReason(dto.getRejectReason());
        }
        if (status == Constants.BILL_AUDIT_SUCCESS) {
            Bill bill = new Bill();
            bill.setAmount(courseBill.getPaidAmount());
            bill.setRenferId(courseBill.getId());
            bill.setRenferType(ReferTypeEnum.SCHOOL_COURSE.getVal());
            bill.setPayNumber(courseBill.getPayNumber());
            bill.setPayType(courseBill.getPayType());
            bill.setCreatedAt(System.currentTimeMillis());
            billService.saveBill(bill);
        }
        courseBill.setStatus(dto.getStatus());
        courseBillRepository.save(courseBill);
    }

    @Override
    public HeavyWork exportBill(CourseBillQo billQo) {
        HeavyWork task = heavyWorkService.create(String.valueOf(System.currentTimeMillis()));
        taskService.addTask(new ExportBillTask(task, billQo));
        return task;
    }

    @Override
    public HeavyWork exportBillMsg(Integer id, String secret) {
        return heavyWorkService.getById(id, secret);
    }

    public class ExportBillTask extends ApiTask {
        HeavyWork job;
        CourseBillQo qo;
        int progress = 0;
        Map<Byte, String> payTypes;

        public ExportBillTask(HeavyWork job, CourseBillQo qo) {
            super();
            this.job = job;
            this.qo = qo;
        }

        private Workbook writeToWorkbook() throws Exception {
            SXSSFWorkbook wb = new SXSSFWorkbook(100);
            SXSSFSheet sheet = wb.createSheet("账单表");

            {
                int cellIndex = -1;
                sheet.setColumnWidth(++cellIndex, 6 * 256);// 序号
                sheet.setColumnWidth(++cellIndex, 24 * 256);// 订单编号
                sheet.setColumnWidth(++cellIndex, 16 * 256);// 用户姓名
                sheet.setColumnWidth(++cellIndex, 18 * 256);// 手机号码
                sheet.setColumnWidth(++cellIndex, 16 * 256);// 业务员
                sheet.setColumnWidth(++cellIndex, 12 * 256);// 小计
                sheet.setColumnWidth(++cellIndex, 16 * 256);// 付款方式
                sheet.setColumnWidth(++cellIndex, 50 * 256);// 汇款单号
                sheet.setColumnWidth(++cellIndex, 24 * 256);// 录入时间
                sheet.setColumnWidth(++cellIndex, 24 * 256);// 付款日期
                sheet.setColumnWidth(++cellIndex, 20 * 256);// 审核状态
                sheet.setColumnWidth(++cellIndex, 40 * 256);// 失败原因
                sheet.setColumnWidth(++cellIndex, 50 * 256);// 订单内容
            }

            int rowIndex = 0;
            {
                int cellIndex = -1;
                Row titleRow = sheet.createRow(rowIndex);
                titleRow.setHeightInPoints(50);

                titleRow.setRowStyle(getTitleStyle(wb));

                titleRow.createCell(++cellIndex).setCellValue("序号");
                titleRow.createCell(++cellIndex).setCellValue("订单编号");
                titleRow.createCell(++cellIndex).setCellValue("用户姓名");
                titleRow.createCell(++cellIndex).setCellValue("手机号码");
                titleRow.createCell(++cellIndex).setCellValue("业务员");
                titleRow.createCell(++cellIndex).setCellValue("小计");
                titleRow.createCell(++cellIndex).setCellValue("付款方式");
                titleRow.createCell(++cellIndex).setCellValue("汇款单号");
                titleRow.createCell(++cellIndex).setCellValue("付款日期");
                titleRow.createCell(++cellIndex).setCellValue("录入时间");
                titleRow.createCell(++cellIndex).setCellValue("审核状态");
                titleRow.createCell(++cellIndex).setCellValue("失败原因");
                titleRow.createCell(++cellIndex).setCellValue("订单内容");

                for (Iterator<Cell> cit = titleRow.cellIterator(); cit.hasNext();) {
                    SXSSFCell cell = (SXSSFCell) cit.next();
                    cell.setCellStyle(getTitleStyle(wb));
                }

            }
            Page<CourseBill> page;
            int sum = 0;
            while (true) {
                // 查询
                page = courseBillRepository.findAll(qo);

                List<CourseBill> bills = page.getContent();
                if (bills.size() == 0) {
                    break;
                }

                int per = 90 / page.getTotalPages();

                Set<Long> userIds = page.stream().map(CourseBill::getUserId).collect(Collectors.toSet());
                Set<Integer> salesManIds = page.stream().map(CourseBill::getSalesmanId).collect(Collectors.toSet());
                Map<Integer, SchAdmin> salesManMap = schAdminService.findByIdIn(salesManIds);
                Map<Long, User> userMap = userService.findByIds(userIds);

                // 写入Excel
                for (CourseBill bill : bills) {

                    User user = userMap.get(bill.getUserId());
                    String username = "账号丢失", mobile = "账号丢失";
                    if (user != null) {
                        username = user.getName();
                        mobile = user.getMobile();
                    }

                    Row row = sheet.createRow(++rowIndex);
                    row.setHeightInPoints(30);
                    row.setRowStyle(getContentStyle(wb));

                    int cellIndex = -1;

                    String salesmanName = "无";
                    if (bill.getSalesmanId() != null) {
                        SchAdmin salesman = salesManMap.get(bill.getSalesmanId());
                        if (salesman != null) {
                            salesmanName = salesman.getName();
                        }
                    }

                    row.createCell(++cellIndex, CellType.NUMERIC).setCellValue(rowIndex);
                    row.createCell(++cellIndex).setCellValue(bill.getTradeNumber());
                    row.createCell(++cellIndex).setCellValue(username);
                    row.createCell(++cellIndex, CellType.STRING).setCellValue(mobile);
                    row.createCell(++cellIndex).setCellValue(salesmanName);
                    row.createCell(++cellIndex, CellType.NUMERIC).setCellValue(new BigDecimal(bill.getPaidAmount())
                            .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).doubleValue());
                    row.createCell(++cellIndex).setCellValue(payTypes.get(bill.getPayType()));
                    row.createCell(++cellIndex, CellType.STRING).setCellValue(bill.getPayNumber());
                    row.createCell(++cellIndex)
                            .setCellValue(DateUtils.format(new Date(bill.getPaidAt()), "yyyy-MM-dd HH:mm"));
                    row.createCell(++cellIndex)
                            .setCellValue(DateUtils.format(new Date(bill.getCreatedAt()), "yyyy-MM-dd HH:mm"));
                    row.createCell(++cellIndex).setCellValue(ensureStatus(bill.getStatus()));
                    row.createCell(++cellIndex).setCellValue(bill.getRejectReason());
                    row.createCell(++cellIndex).setCellValue(bill.getRemarks());

                    for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext();) {
                        SXSSFCell cell = (SXSSFCell) cit.next();
                        cell.setCellStyle(getContentStyle(wb));
                    }

                }
                // 进度加一点
                int newProgress = progress + per;
                if (newProgress > 90) {
                    newProgress = 90;
                }
                doUpdateProgress(newProgress);
                // 没有更多了
                if (page.getContent().size() < qo.getPageSize()) {
                    break;
                }
                // 标记分页，由于分页算法默认-1，此处需+2
                qo.setPageNumber(qo.getPageNumber() + 2);
                // 最多10w条
                sum += page.getSize();
                if (sum >= 100000) {
                    break;
                }
            }
            return wb;
        }

        private void doUpdateProgress(int progress) {
            try {
                // 进度不能倒退
                if (progress <= this.progress) {
                    return;
                }
                this.progress = progress;
                heavyWorkService.updateProgress(job.getId(), progress);
            } catch (Exception ex) {
                L.error(ex);
            }
        }

        @Override
        protected void doApiWork() throws Exception {
            // 开始
            doUpdateProgress(1);
            // 生成表格
            Workbook wb = null;
            File file = null;

            try {
                payTypes = new HashMap<>();
                payTypes.put(ByteUtils.BYTE_1, "银行卡");
                payTypes.put(ByteUtils.BYTE_2, "支付宝");
                payTypes.put(ByteUtils.BYTE_3, "微信");
                // payTypes = payTypeService.payTypes(qo.getSchoolId()).stream()
                // .collect(Collectors.toMap(PayType::getId, PayType::getName));
                wb = writeToWorkbook();
                file = fileService.createTmpFile("bill-export", "xlsx");
                // 写入文件
                {
                    FileOutputStream out = new FileOutputStream(file);
                    wb.write(out);
                    out.flush();
                    out.close();
                }
                // 上传前先报告一下状态
                doUpdateProgress(95);

                // 上传
                UploadOptions options = new UploadOptions();
                options.setContentType("application/x-xls");
                String exportUrl = fileService.upload(file, options);

                // 报告成功
                heavyWorkService.updateSuccess(job.getId(), exportUrl);
            } catch (Exception ex) {
                L.error(ex);
                heavyWorkService.updateFailed(job.getId(), null);
            } finally {
                FileUtil.close(wb);
                if (file != null) {
                    FileUtil.delete(file);
                }
            }
        }

        private CellStyle getTitleStyle(SXSSFWorkbook wb) {
            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
            Font font = wb.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short) 14);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setWrapText(true);
            return style;
        }

        private CellStyle getContentStyle(SXSSFWorkbook wb) {
            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 13);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setWrapText(true);
            return style;
        }

        private String ensureStatus(Byte status) {

            String str = "状态错误";
            if (status == null) {
                return str;
            }

            if (status == Constants.BILL_WEIT) {
                str = "待审核";
            } else if (status == Constants.BILL_AUDIT_SUCCESS) {
                str = "审核成功";
            } else if (status == Constants.BILL_AUDIT_FAIL) {
                str = "审核失败";
            }
            return str;

        }

    }

}