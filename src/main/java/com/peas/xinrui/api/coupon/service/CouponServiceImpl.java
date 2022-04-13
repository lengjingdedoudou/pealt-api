package com.peas.xinrui.api.coupon.service;

import com.peas.xinrui.api.coupon.model.Coupon;
import com.peas.xinrui.api.coupon.model.UserCoupon;
import com.peas.xinrui.api.coupon.qo.CouponQo;
import com.peas.xinrui.api.coupon.qo.UserCouponQo;
import com.peas.xinrui.api.coupon.repository.CouponRepository;
import com.peas.xinrui.api.coupon.repository.UserCouponRepository;
import com.peas.xinrui.api.schadmin.entity.SchAdminContexts;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.cache.KvCacheWrap;
import com.peas.xinrui.common.cache.SingleRepositoryProvider;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DataNotFoundServiceException;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.utils.ByteUtils;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Create by 李振威 2021/12/23 11:21
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCacheWrap<Integer, Coupon> couponKvCache;
    private KvCacheWrap<Integer, UserCoupon> userCouponKvCache;

    @PostConstruct
    public void init() {
        couponKvCache = kvCacheFactory.create(new CacheOptions("coupon", 1, 8 * DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Integer, Coupon>() {
                    @Override
                    public Coupon findByKey(Integer key) throws Exception {
                        return couponRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Integer, Coupon> findByKeys(Collection<Integer> keys) throws Exception {
                        if (CollectionUtils.isEmpty(keys)) {
                            return new HashMap<>();
                        } else {
                            List<Integer> ids = keys.stream().filter(integer -> integer != 0 && integer != null)
                                    .collect(Collectors.toList());
                            List<Coupon> couponList = couponRepository.findAllById(ids);
                            Map<Integer, Coupon> couponMap = couponList.stream()
                                    .collect(Collectors.toMap(Coupon::getId, coupon -> coupon));
                            return couponMap;
                        }
                    }
                }, new BeanModelConverter<>(Coupon.class));

        userCouponKvCache = kvCacheFactory.create(
                new CacheOptions("user_coupon", 1, 8 * DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Integer, UserCoupon>() {
                    @Override
                    public UserCoupon findByKey(Integer key) throws Exception {
                        return userCouponRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Integer, UserCoupon> findByKeys(Collection<Integer> keys) throws Exception {
                        if (CollectionUtils.isEmpty(keys)){
                            return new HashMap<>();
                        }else {
                           List<Integer> ids = keys.stream().filter(integer -> integer != 0 && integer != null).collect(Collectors.toList());
                           return userCouponRepository.findAllById(ids).stream().collect(Collectors.toMap(UserCoupon::getId,userCoupon -> userCoupon));
                        }
                    }
                },
                new BeanModelConverter<>(UserCoupon.class)
        );
    }

    @Override
    public void save(Coupon coupon) {
        checkCouponValid(coupon);

        boolean update = false;

        if (coupon.getId() != null && coupon.getId() != 0) {
            update = true;
        }

        if (update) {
            Coupon exist = findById(coupon.getId());
            // TODO 修改优惠券价格， 周期时长权限
            exist.setDuration(coupon.getDuration());
            exist.setPayload(coupon.getPayload());
            exist.setRule(coupon.getRule());
            couponRepository.save(coupon);
            couponKvCache.remove(coupon.getId());
        } else {
            Integer schoolId = SchAdminContexts.getSchoolId();
            coupon.setSchoolId(schoolId);
            coupon.setStatus(ByteUtils.BYTE_1);
            couponRepository.save(coupon);
            couponKvCache.save(coupon.getId(), coupon);
        }
    }

    private void checkCouponValid(Coupon coupon) {
        if (StringUtils.isNull(coupon.getDuration())) {
            throw new ArgumentServiceException("duration");
        }
        if (coupon.getPayload() == null) {
            throw new ArgumentServiceException("payload");
        }
        if (coupon.getRule() == null) {
            throw new ArgumentServiceException("rule");
        }
    }

    private Coupon findById(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        Coupon coupon = couponKvCache.findByKey(id);
        if (coupon == null) {
            throw new DataNotFoundServiceException("coupon, id= " + id);
        }
        return coupon;
    }

    @Override
    public void remove(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        couponRepository.deleteById(id);
        couponKvCache.remove(id);
    }

    @Override
    public void updateStatus(Integer id, Byte status) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        if (status == null) {
            throw new ArgumentServiceException("status");
        }
        couponRepository.updateStatus(id, status);
        couponKvCache.remove(id);
    }

    @Override
    public Page<Coupon> coupons(CouponQo qo) {
        Page<Coupon> page = couponRepository.findAll(qo);
        return page;
    }

    @Override
    public void saveUserCoupon(UserCoupon userCoupon) {
        if (userCoupon.getUserId() == null){
            throw new ArgumentServiceException("userId");
        }
        if (userCoupon.getCouponId() == null){
            throw new ArgumentServiceException("couponId");
        }

        userCoupon.setCreatedAt(System.currentTimeMillis());
        userCoupon.setStatus(ByteUtils.BYTE_1);
        userCouponRepository.save(userCoupon);
        userCouponKvCache.save(userCoupon.getId(), userCoupon);
    }

    @Transactional(readOnly = false)
    @Override
    public void removeUserCoupon(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        userCouponRepository.deleteById(id);
        userCouponKvCache.remove(id);
    }

    @Override
    public UserCoupon getUserCouponById(Integer id) {
        UserCoupon userCoupon = findUserCouponById(id);
        if (userCoupon == null){
            throw new DataNotFoundServiceException();
        }
        wrapper(Collections.singleton(userCoupon));
        return userCoupon;
    }

    private UserCoupon findUserCouponById(Integer id){
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return userCouponKvCache.findByKey(id);
    }

    @Transactional(readOnly = false)
    @Override
    public void updateUserCouponStatus(Integer id, Byte status) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        if (status == null) {
            throw new ArgumentServiceException("status");
        }
        userCouponRepository.updateStatus(id, status);
        userCouponKvCache.remove(id);
    }

    @Override
    public Page<UserCoupon> userCoupons(UserCouponQo qo) {
        Page<UserCoupon> page = userCouponRepository.findAll(qo);
        wrapper(page.getContent());
        return page;
    }

    private void wrapper(Collection<UserCoupon> userCoupons){
        if (CollectionUtils.isEmpty(userCoupons)){
            return;
        }

        List<Integer> couponIds = userCoupons.stream().map(userCoupon -> userCoupon.getCouponId()).collect(Collectors.toList());
        Map<Integer, Coupon> couponMap = couponKvCache.findByKeys(couponIds);
        for (UserCoupon userCoupon:userCoupons) {
            userCoupon.setCoupon(couponMap.get(userCoupon.getCouponId()));
        }
    }
}
