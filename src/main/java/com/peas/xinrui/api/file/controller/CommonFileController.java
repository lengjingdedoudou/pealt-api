package com.peas.xinrui.api.file.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.utils.ImgUtils;

@Controller
@RequestMapping("/common/file")
public class CommonFileController extends BaseController {

    @RequestMapping(value = "/img_to_base64")
    public ModelAndView imgToBase64(String url) throws Exception {
        return feedBack(ImgUtils.base64FromURL(url));
    }

}
