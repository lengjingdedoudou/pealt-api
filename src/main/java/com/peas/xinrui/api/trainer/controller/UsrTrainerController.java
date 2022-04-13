package com.peas.xinrui.api.trainer.controller;

import com.peas.xinrui.api.trainer.entity.TrainerWO;
import com.peas.xinrui.api.trainer.qo.TrainerQo;
import com.peas.xinrui.api.trainer.service.TrainerService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usr/trainer")
@RequiredSession(SessionType.USER)
public class UsrTrainerController extends BaseController {
    @Autowired
    private TrainerService trainerService;

    @RequestMapping("/trainers")
    public ModelAndView trainers(String trainerQoJson) {
        return feedBack(trainerService.trainers(parseModel(trainerQoJson, new TrainerQo()), TrainerWO.getAllInstance(),
                SessionType.USER));
    }

    @RequestMapping("/struct_trainer")
    public ModelAndView trainer(Long id) {
        return feedBack(trainerService.trainer(id, TrainerWO.getAllInstance(), SessionType.USER));
    }

    @RequestMapping("/follow")
    public ModelAndView follow(Long trainerId) {
        trainerService.follow(trainerId, SessionType.USER);
        return feedBack(null);
    }
}