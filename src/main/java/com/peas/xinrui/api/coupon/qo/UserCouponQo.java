package com.peas.xinrui.api.coupon.qo;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

/**
 * Create by 李振威
 * 2021/12/23 16:46
 */
public class UserCouponQo extends DataQueryObjectPage {

    @QueryField(type = QueryType.EQUAL, name = "userId")
    private Long userId;

    @QueryField(type = QueryType.EQUAL, name = "status")
    private Byte status;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
