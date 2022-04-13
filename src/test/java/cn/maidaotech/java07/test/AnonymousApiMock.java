package cn.maidaotech.java07.test;

import java.util.HashMap;
import java.util.Map;

public class AnonymousApiMock extends ApiMock
{

    @Override
    protected Map<String, Object> wrapHeaders()
    {
        Map<String, Object> headers = new HashMap<>();
        return headers;
    }

    @Override
    protected String uploadTokenAPI()
    {
        throw new UnsupportedOperationException();
    }

}
