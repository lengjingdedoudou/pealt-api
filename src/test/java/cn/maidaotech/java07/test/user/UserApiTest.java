package cn.maidaotech.java07.test.user;

import com.peas.xinrui.api.heavywork.model.HeavyWork;
import cn.maidaotech.java07.test.UserApiMock;
import junit.framework.TestCase;

public class UserApiTest extends TestCase
{
    public void test_heavy() throws Exception
    {
        new UserApiMock().post("/usr/export_excel", "userQoJsonStr", "{}").resultAsBean(HeavyWork.class);
    }
}