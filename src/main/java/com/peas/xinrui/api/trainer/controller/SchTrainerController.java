package com.peas.xinrui.api.trainer.controller;

import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.api.trainer.entity.TrainerWO;
import com.peas.xinrui.api.trainer.model.Trainer;
import com.peas.xinrui.api.trainer.qo.TrainerQo;
import com.peas.xinrui.api.trainer.service.TrainerService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSchoolPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sch/trainer")
@RequiredSession(SessionType.SCHADMIN)
public class SchTrainerController extends BaseController {
    @Autowired
    private TrainerService trainerService;

    @RequestMapping("/save")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView saveTrainer(String trainerJson) {
        trainerService.saveTrainer(parseModel(trainerJson, new Trainer()));
        return feedBack(null);
    }

    @RequestMapping("/trainers")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView trainers(String trainerQoJson) {
        return feedBack(trainerService.trainers(parseModel(trainerQoJson, new TrainerQo()),
                TrainerWO.getCategoryInstance(), SessionType.SCHADMIN));
    }

    @RequestMapping("/item")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView trainer(Long id) {
        return feedBack(trainerService.trainer(id, TrainerWO.getNonInstance(), SessionType.SCHADMIN));
    }
}