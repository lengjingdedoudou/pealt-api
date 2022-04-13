package com.peas.xinrui.api.category.controller;

import com.peas.xinrui.api.category.entity.CategoryTypeEnum;
import com.peas.xinrui.api.category.model.Category;
import com.peas.xinrui.api.category.qo.CategoryQo;
import com.peas.xinrui.api.category.service.CategoryService;
import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSchoolPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/common/trainer-category")
@RequiredSession(SessionType.SCHADMIN)
public class TrainerCategoryController extends BaseController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/save")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView saveCategory(String categoryJson) {
        categoryService.saveCategory(parseModel(categoryJson, new Category()), CategoryTypeEnum.Trainer.getVal());
        return feedBack(null);
    }

    @RequestMapping("/categories")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST, SchAdminPermission.COURSE_EDIT })
    public ModelAndView categories(String categoryQoJson) {
        return feedBack(categoryService.categories(CategoryTypeEnum.Trainer.getVal(),
                parseModel(categoryQoJson, new CategoryQo())));
    }
}