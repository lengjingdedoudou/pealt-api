package com.peas.xinrui.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunnysuperman.commons.util.StringUtil;

import org.springframework.web.servlet.view.AbstractView;

public class JsonView extends AbstractView {
    private Object result;

    public JsonView(Object result) {
        super();
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    // 这一步将model信息和request信息放入了response中
    @Override
    protected void renderMergedOutputModel(java.util.Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(replace(JsonSerializerManager.serialize(result)));
    }

    String replace(String data) {
        if (StringUtil.isEmpty(data)) {
            return "";
        }
        return data.replaceAll("lpykey", "lpy-oss");
    }

}
