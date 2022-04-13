package cn.maidaotech.java07.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sunnysuperman.commons.util.JSONUtil;

import org.junit.Assert;

import com.peas.xinrui.api.admin.entity.AdminSessionWrapper;
import com.peas.xinrui.api.heavywork.model.HeavyWork;
import com.peas.xinrui.common.task.TaskStatus;
import com.peas.xinrui.common.utils.DateUtils;

public class AdminApiMock extends ApiMock
{
    private static String AUTH = null;

    public static void setAuthorization(String authorization)
    {
        AUTH = authorization;
    }

    @Override
    protected Map<String, Object> wrapHeaders() throws Exception
    {
        Map<String, Object> headers = new HashMap<>();
        headers.put("admin-token", ensureAuthorization());
        return headers;
    }

    @Override
    protected String uploadTokenAPI()
    {
        return "adm/file/upload_token";
    }

    public String ensureAuthorization() throws Exception
    {
        Map<String, String> map = new HashMap<>(3);
        map = getProperties();
        System.out.println("adminToken===>" + map.get("adminToken"));
        System.out.println("signinAt===>" + map.get("signinAt"));
        System.out.println("expireAt===>" + map.get("expireAt"));
        if (map.get("adminToken") != null && Long.parseLong(map.get("expireAt")) > System.currentTimeMillis())
        {
            AUTH = map.get("adminToken");
            return AUTH;
        }

        String authorization = null; // loadAuthorization();
        if (authorization == null)
        {
            Map<String, Object> params = new HashMap<>();
            params.put("mobile", "15517509252");
            params.put("password", "Ghdzj521.");
            AdminSessionWrapper result = new AnonymousApiMock()
                    .post("adm/signin", "adminJson", JSONUtil.toJSONString(params)).assertOK()
                    .resultAsBean(AdminSessionWrapper.class);
            authorization = result.getAdminSession().getToken();
            // saveAuthorization(result.getAccessToken());
            // 把token存入properties里面
            Map<String, String> maps = new HashMap<>(3);
            maps.put("adminToken", authorization);
            maps.put("signinAt", String.valueOf(System.currentTimeMillis()));
            maps.put("expireAt", String.valueOf(System.currentTimeMillis() + DateUtils.MILLIS_PER_HOUR * 8));
            setProperties(maps);
        }
        AUTH = authorization;
        return authorization;
    }

    public String assertHeavyWork(HeavyWork work, boolean ok) throws Exception
    {
        while (true)
        {
            HeavyWork status = post("adm/heavywork/find", "id", work.getId(), "secret", work.getSecret())
                    .resultAsBean(HeavyWork.class);
            if (status.getStatus() == TaskStatus.SUCCESS.value())
            {
                Assert.assertTrue(ok);
                return status.getOutput();
            }
            else if (status.getStatus() == TaskStatus.FAIL.value())
            {
                Assert.assertFalse(ok);
                return status.getErrors();
            }
            else
            {
                try
                {
                    Thread.sleep(3000);
                } catch (InterruptedException e)
                {
                    // ignore
                }
            }
        }
    }

    private static boolean setProperties(Map<String, String> maps) throws IOException
    {
        Properties properties = new Properties();
        try
        {
            File file = new File("src/test/resources/admin-token.properties");
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
        FileInputStream fis = new FileInputStream(new File("src/test/resources/admin-token.properties"));
        properties.load(fis);
        fis.close();
        // 获取key对应的value值
        Map<String, String> map = new HashMap<>(3);
        map.put("adminToken", properties.getProperty("adminToken"));
        map.put("signinAt", properties.getProperty("signinAt"));
        map.put("expireAt", properties.getProperty("expireAt"));
        return map;
    }

}
