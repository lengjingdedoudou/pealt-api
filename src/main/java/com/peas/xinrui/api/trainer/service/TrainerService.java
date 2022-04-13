package com.peas.xinrui.api.trainer.service;

import java.util.List;
import java.util.Map;

import com.peas.xinrui.api.trainer.entity.TrainerWO;
import com.peas.xinrui.api.trainer.model.Trainer;
import com.peas.xinrui.api.trainer.qo.TrainerQo;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.data.domain.Page;

public interface TrainerService {
    void saveTrainer(Trainer trainer);

    Trainer trainer(Long id, TrainerWO wo, SessionType sessionType);

    Page<Trainer> trainers(TrainerQo qo, TrainerWO wo, SessionType type);

    Map<Long, Trainer> trainersForMap(List<Long> ids);

    void follow(Long trainerId, SessionType type);
}