package com.peas.xinrui.api.trade.repository;

import com.peas.xinrui.api.trade.model.CollectionSettings;
import com.peas.xinrui.common.repository.BaseRepository;

public interface CollectionSettingsRepository extends BaseRepository<CollectionSettings, Long> {

    CollectionSettings findBySchoolId(Integer schoolId);

}