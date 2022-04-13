package cn.maidaotech.java07.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sunnysuperman.commons.util.JSONUtil;

import com.peas.xinrui.api.user.entity.UserSessionWrapper;
import com.peas.xinrui.common.utils.DateUtils;

public class UserApiMock extends ApiMock
{
    // 101023-18620452252
    // 101027-18694215936
    // 101029-15738888064
    public static String MOBILE = "15517509252";// 18611919500
    private static String TOKEN;

    public static void setToken(String token)
    {
        TOKEN = token;
    }

    @Override
    protected Map<String, Object> wrapHeaders()
    {
        if (TOKEN == null)
        {
            try
            {
                // String vcode = (String) new AdminApiMock().post("adm/user/renew_smscode",
                // "mobile", MOBILE)
                // .resultAsMap().get("code");
                String token = getProperties().get("userToken");
                if (token != null)
                {
                    TOKEN = token;
                }
                else
                {
                    Map<String, Object> params = new HashMap<>();
                    params.put("mobile", MOBILE);
                    params.put("password", "Ghdzj521");
                    UserSessionWrapper result = new AnonymousApiMock()
                            .post("usr/signin", "userJsonStr", JSONUtil.toJSONString(params)).assertOK()
                            .resultAsBean(UserSessionWrapper.class);
                    TOKEN = result.getUserSession().getToken();
                    Map<String, String> maps = new HashMap<>();
                    maps.put("userToken", TOKEN);
                    maps.put("signinAt", String.valueOf(System.currentTimeMillis()));
                    maps.put("expireAt", String.valueOf(System.currentTimeMillis() + DateUtils.MILLIS_PER_HOUR * 8));
                    setProperties(maps);
                }
            } catch (Exception ex)
            {
                throw new RuntimeException("Failed to signin", ex);
            }
        }
        Map<String, Object> headers = new HashMap<>();
        headers.put("user-token", TOKEN);
        return headers;
    }

    @Override
    protected String uploadTokenAPI()
    {
        return "usr/file/upload_token";
    }

    private static boolean setProperties(Map<String, String> maps) throws IOException
    {
        Properties properties = new Properties();
        try
        {
            File file = new File("src/test/resources/user-token.properties");
            if (!file.exists())
            {
                file.createNewFile();
            }
            // 写入
            for (String key : maps.keySet())
            {
                properties.setProperty(key, maps.get(key));
            }
            FileOutputStream fos = new FileOutputStream(file);
            properties.store(fos, null);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private static Map<String, String> getProperties() throws IOException
    {
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        FileInputStream fis = new FileInputStream(new File("src/test/resources/user-token.properties"));
        properties.load(fis);
        fis.close();
        // 获取key对应的value值
        Map<String, String> map = new HashMap<>(3);
        map.put("userToken", properties.getProperty("userToken"));
        map.put("signinAt", properties.getProperty("signinAt"));
        map.put("expireAt", properties.getProperty("expireAt"));
        return map;
    }

}
