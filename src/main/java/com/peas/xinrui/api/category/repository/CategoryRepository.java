package com.peas.xinrui.api.category.repository;

import java.util.List;

import com.peas.xinrui.api.category.model.Category;
import com.peas.xinrui.common.repository.BaseRepository;

public interface CategoryRepository extends BaseRepository<Category, Long> {
    Category findByNameAndSchoolId(String name, Integer schoolId);

    List<Category> findBySchoolIdAndType(Integer schoolId, Byte type);
}