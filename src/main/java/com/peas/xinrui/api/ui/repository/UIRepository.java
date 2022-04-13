package com.peas.xinrui.api.ui.repository;

import com.peas.xinrui.api.ui.model.UI;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UIRepository extends BaseRepository<UI, Integer> {

    @Transactional
    @Modifying
    @Query("update UI set isDefault= :isDefault where type=:type and schoolId= :schoolId")
    void offDefault(@Param(value = "isDefault") Byte isDefault, @Param(value = "schoolId") Integer schoolId,
            @Param(value = "type") Byte type);

    UI findByTypeAndIsDefaultAndSchoolId(Byte type, Byte isDefault, Integer schoolId);

}
