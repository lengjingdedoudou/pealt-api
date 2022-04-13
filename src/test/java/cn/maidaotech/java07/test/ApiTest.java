package cn.maidaotech.java07.test;

import java.io.File;
import java.io.IOException;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sunnysuperman.commons.util.FileUtil;
import com.sunnysuperman.commons.util.JSONUtil;

import com.peas.xinrui.common.utils.UUIDGeneratorFactory;
import junit.framework.TestCase;

public class ApiTest extends TestCase
{

    protected final String stringify(Object obj)
    {
        return JSONUtil.toJSONString(obj, null, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.PrettyFormat, SerializerFeature.MapSortField);
    }

    protected final File newFile(String suffix) throws IOException
    {
        File file = new File("/tmp/" + UUIDGeneratorFactory.create().generate() + "." + suffix);
        file.delete();
        FileUtil.ensureFile(file);
        return file;
    }

    // protected void sendSmsCode(ApiMock mock, String api, String mobile) throws
    // Exception {
    // String timestamp = String.valueOf(System.currentTimeMillis());
    // String platform = "ios";
    // String deviceId = StringUtils.randomAlphanumeric(12);
    // String code = CodeDeliveryValidator.getDeliveryCode(mobile, timestamp,
    // platform, deviceId);
    // mock.post(api, "mobile", mobile, "t", timestamp, "p", platform, "d",
    // deviceId, "v", code).assertOK();
    // }

    // protected String getSmsCode(String mobile, String subject) throws Exception {
    // String vcode = (String) new AdminApiMock().post("adm/user/get_smscode",
    // "mobile", mobile, "subject", subject)
    // .resultAsMap().get("code");
    // System.out.println("vcode: " + vcode);
    // return vcode;
    // }
}
