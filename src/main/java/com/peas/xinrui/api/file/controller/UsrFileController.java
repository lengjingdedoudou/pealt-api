package com.peas.xinrui.api.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.peas.xinrui.api.file.service.FileService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

@Controller
@RequestMapping("/usr/file")
@RequiredSession(SessionType.USER)
public class UsrFileController extends BaseController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload_token")
    public ModelAndView uploadToken(String fileName, int fileSize) throws Exception {
        return feedBack(fileService.uploadToken("f", fileName, fileSize, true));
    }

}
