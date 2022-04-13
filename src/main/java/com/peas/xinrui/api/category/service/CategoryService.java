package com.peas.xinrui.api.category.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.peas.xinrui.api.category.model.Category;
import com.peas.xinrui.api.category.qo.CategoryQo;

public interface CategoryService {
    void saveCategory(Category category, Byte type);

    List<Category> categories(Byte type, CategoryQo qo);

    Category category(Long id);

    Map<Long, Category> categorysForMap(Set<Long> ids);
}