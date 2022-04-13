package com.peas.xinrui.api.category.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.peas.xinrui.api.category.model.Category;
import com.peas.xinrui.api.category.qo.CategoryQo;
import com.peas.xinrui.api.category.repository.CategoryRepository;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DetailedServiceException;
import com.peas.xinrui.common.kvCache.KvCache;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCache<Long, Category> categoryCache;

    @PostConstruct
    private void init() {
        categoryCache = kvCacheFactory.create(new CacheOptions("category", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, Category>() {
                    @Override
                    public Category findByKey(Long key) throws Exception {
                        return categoryRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, Category> findByKeys(Collection<Long> keys) throws Exception {
                        keys = keys.stream().filter((key) -> key != 0).collect(Collectors.toList());
                        Map<Long, Category> map = new HashMap<>();
                        for (Long key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(Category.class));
    }

    @Override
    public void saveCategory(Category category, Byte type) {
        Integer schoolId = category.getSchoolId();
        if (schoolId == null) {
            throw new ArgumentServiceException("schoolId");
        }
        if (StringUtils.isEmpty(category.getName()) || category.getName().length() > 20) {
            throw new ArgumentServiceException("name");
        }
        if (category.getPriority() == null) {
            throw new ArgumentServiceException("priority");
        }
        if (category.getId() == null) {
            Category existName = categoryRepository.findByNameAndSchoolId(category.getName(), schoolId);
            if (existName != null) {
                throw new DetailedServiceException("分类名称已存在");
            }

            category.setCreatedAt(System.currentTimeMillis());
            category.setSchoolId(schoolId);
            category.setType(type);
        } else {
            Category exist = categoryRepository.findById(category.getId()).orElse(null);
            if (exist == null) {
                throw new DetailedServiceException("该分类不存在");
            }
            categoryCache.remove(exist.getId());
        }

        categoryRepository.save(category);
    }

    @Override
    public List<Category> categories(Byte type, CategoryQo qo) {
        qo.setType(type);

        List<Category> list = categoryRepository.findAllForList(qo);
        return list;
    }

    @Override
    public Category category(Long id) {
        Category category = findById(id);
        if (category == null) {
            throw new ArgumentServiceException("category");
        }
        return category;
    }

    private Category findById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return categoryCache.findByKey(id);
    }

    @Override
    public Map<Long, Category> categorysForMap(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ArgumentServiceException("ids");
        }
        return categoryCache.findByKeys(ids);
    }
}