package com.peas.xinrui.common.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.StringUtils;

import com.peas.xinrui.common.repository.support.*;

@SuppressWarnings("all")
public class BaseRepositoryImpl<T, ID extends java.io.Serializable> extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID>, JpaSpecificationExecutor<T> {

    private final EntityManager entityManager;
    private final Class<T> clazz;
    final private BaseRepositoryImpl exmple;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.clazz = domainClass;
        this.entityManager = entityManager;
        exmple = this;
    }

    @Override
    public List<T> findAll(DataQueryObjectSort dataQueryObjectSort) {
        final DataQueryObject dqo = dataQueryObjectSort;
        // 如果排序内容为空 则执行不排序的 查找
        if (dataQueryObjectSort.getSortPropertyName() != null
                && dataQueryObjectSort.getSortPropertyName().trim().length() != 0) {
            return this.findAll(dqo,
                    new Sort(dataQueryObjectSort.isSortAscending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                            dataQueryObjectSort.getSortPropertyName()));
        } else {
            return this.findAll(dqo);
        }
    }

    @Override
    public List<T> findAll(DataQueryObject dataQueryObject, Sort sort) {
        final DataQueryObject dqo = dataQueryObject;
        return this.findAll(new Specification<T>() {

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return exmple.getPredocate(root, query, cb, dqo);
            }
        }, sort);
    }

    @Override
    public List<T> findAll(DataQueryObject dataQueryObject) {
        final DataQueryObject dqo = dataQueryObject;
        return this.findAll(new Specification<T>() {

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return exmple.getPredocate(root, query, cb, dqo);
            }
        });
    }

    @Override
    public Page<T> findAll(DataQueryObjectPage dataQueryObjectpage) {
        Pageable pageable = null;
        if (dataQueryObjectpage.getSortPropertyName() != null
                && dataQueryObjectpage.getSortPropertyName().trim().length() != 0) {
            pageable = PageRequest.of(dataQueryObjectpage.getPageNumber(), dataQueryObjectpage.getPageSize(),
                    new Sort(dataQueryObjectpage.isSortAscending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                            dataQueryObjectpage.getSortPropertyName()));
        } else {
            pageable = PageRequest.of(dataQueryObjectpage.getPageNumber(), dataQueryObjectpage.getPageSize());
        }
        return this.findAll(dataQueryObjectpage, pageable);
    }

    @Override
    public Page<T> findAll(DataQueryObject dataQueryObject, Pageable page) {
        final DataQueryObject dqo = dataQueryObject;
        return this.findAll(new Specification<T>() {

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return exmple.getPredocate(root, query, cb, dqo);
            }
        }, page);
    }

    // 核心方法 拼接条件
    protected Predicate getPredocate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb, DataQueryObject dqo) {
        List<Predicate> predicates = new ArrayList<>();
        // 获取查询对象的所有属性
        Field[] fields = dqo.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            String[] queryFileds = null;
            QueryType queryType = null;
            Object value = null;
            Predicate predicate = null;
            // 获取属性的 自定义注解类型
            QueryField annotaion = field.getAnnotation(QueryField.class);
            // 如果没有注解 则跳过
            if (annotaion == null) {
                continue;
            }
            // 如果注解中 name为空 则用字段名称作为属性名
            if (!StringUtils.isEmpty(annotaion.name())) {
                queryFileds = annotaion.name();
            } else {
                queryFileds = new String[] { field.getName() };
            }
            queryType = annotaion.type();
            try {
                value = field.get(dqo);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            // 判断字段类型是否为空
            if (value == null && !queryType.isCanBeNull()) {
                // L.error("查询类型：" + queryType + " " + queryFiled + "不允许为空。");
                continue;
            }
            Predicate[] subPredicate = new Predicate[queryFileds.length];
            QueryBetween queryBetween = null;
            // 判断注解中 的条件类型
            switch (queryType) {
                case EQUAL:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<Object> equal = getRootByQueryFiled(queryFileds[i], root);
                        subPredicate[i] = cb.equal(equal, value);
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case BEWTEEN:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<Comparable> between = getRootByQueryFiledComparable(queryFileds[i], root);
                        if (value instanceof QueryBetween)
                            queryBetween = (QueryBetween) value;
                        else
                            continue;
                        subPredicate[i] = cb.between(between, queryBetween.after, queryBetween.before);
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case LESS_THAN:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<Comparable> lessThan = getRootByQueryFiledComparable(queryFileds[i], root);
                        if (value instanceof QueryBetween)
                            queryBetween = (QueryBetween) value;
                        else
                            continue;
                        subPredicate[i] = cb.lessThan(lessThan, queryBetween.after);
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case LESS_THAN_EQUAL:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<Comparable> lessThanOrEqualTo = getRootByQueryFiledComparable(queryFileds[i], root);
                        if (value instanceof QueryBetween)
                            queryBetween = (QueryBetween) value;
                        else
                            continue;
                        subPredicate[i] = cb.lessThanOrEqualTo(lessThanOrEqualTo, queryBetween.after);
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case GREATEROR_THAN:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<Comparable> greaterThan = getRootByQueryFiledComparable(queryFileds[i], root);
                        if (value instanceof QueryBetween)
                            queryBetween = (QueryBetween) value;
                        else
                            continue;
                        subPredicate[i] = cb.greaterThan(greaterThan, queryBetween.after);
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case GREATEROR_THAN_EQUAL:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<Comparable> greaterThanOrEqualTo = getRootByQueryFiledComparable(queryFileds[i], root);
                        if (value instanceof QueryBetween)
                            queryBetween = (QueryBetween) value;
                        else
                            continue;
                        subPredicate[i] = cb.greaterThanOrEqualTo(greaterThanOrEqualTo, queryBetween.after);
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case NOT_EQUAL:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<Object> notEqual = getRootByQueryFiled(queryFileds[i], root);
                        subPredicate[i] = cb.notEqual(notEqual, value);
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case IS_NULL:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<Object> isNull = getRootByQueryFiled(queryFileds[i], root);
                        subPredicate[i] = cb.isNull(isNull);
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case IS_NOT_NULL:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<Object> isNotNull = getRootByQueryFiled(queryFileds[i], root);
                        subPredicate[i] = cb.isNotNull(isNotNull);
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case LEFT_LIKE:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<String> leftLike = getRootByQueryFiledString(queryFileds[i], root);
                        subPredicate[i] = cb.like(leftLike, "%" + value.toString());
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case RIGHT_LIKE:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<String> rightLike = getRootByQueryFiledString(queryFileds[i], root);
                        subPredicate[i] = cb.like(rightLike, value.toString() + "%");
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case FULL_LIKE:
                    // TODO 模糊查询，应使用搜索引擎
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<String> fullLike = getRootByQueryFiledString(queryFileds[i], root);
                        subPredicate[i] = cb.like(fullLike, "%" + value.toString() + "%");
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case BATCH_FULL_LIKE:
                    for (String queryFiled : queryFileds) {
                        Path<String> fullLike = getRootByQueryFiledString(queryFiled, root);
                        List<String> ss = (List<String>) value;
                        for (String s : ss) {
                            predicates.add(cb.or(cb.like(fullLike, "%" + s + "%")));
                        }
                    }
                    break;
                case DEFAULT_LIKE:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<String> like = getRootByQueryFiledString(queryFileds[i], root);
                        subPredicate[i] = cb.like(like, value.toString());
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case NOT_LIKE:
                    for (int i = 0; i < queryFileds.length; i++) {
                        Path<String> notLike = getRootByQueryFiledString(queryFileds[i], root);
                        subPredicate[i] = cb.like(notLike, value.toString());
                    }
                    predicates.add(cb.or(subPredicate));
                    break;
                case IN:

                    Path<Object> in = getRootByQueryFiled(queryFileds[0], root);
                    In ins = cb.in(in);
                    List inList = null;
                    if (value instanceof List) {
                        inList = (List) value;
                    }
                    for (Object object : inList) {
                        ins.value(object);
                    }
                    predicates.add(ins);
                    break;
                default:
                    break;
            }
        }
        // 如果 为空 代表 没有任何有效的条件
        if (predicates.size() == 0) {
            return cb.and();
        }
        Object[] list = predicates.toArray();
        Predicate[] t = new Predicate[predicates.size()];
        Predicate[] result = predicates.toArray(t);
        return cb.and(result);
    }

    private Path<Object> getRootByQueryFiled(String queryFiled, Root<T> root) {
        if (queryFiled.indexOf(".") < 0) {
            return root.get(queryFiled);
        } else {
            return getRootByQueryFiled(queryFiled.substring(queryFiled.indexOf(".") + 1, queryFiled.length()),
                    root.get(queryFiled.substring(0, queryFiled.indexOf("."))));
        }
    }

    private Path<Object> getRootByQueryFiled(String queryFiled, Path<Object> path) {
        if (queryFiled.indexOf(".") < 0) {
            return path.get(queryFiled);
        } else {
            return getRootByQueryFiled(queryFiled.substring(queryFiled.indexOf(".") + 1, queryFiled.length()),
                    path.get(queryFiled.substring(0, queryFiled.indexOf("."))));
        }
    }

    private Path<String> getRootByQueryFiledString(String queryFiled, Root<T> root) {
        if (queryFiled.indexOf(".") < 0) {
            return root.get(queryFiled);
        } else {
            return getRootByQueryFiledString(queryFiled.substring(queryFiled.indexOf(".") + 1, queryFiled.length()),
                    root.get(queryFiled.substring(0, queryFiled.indexOf("."))));
        }
    }

    private Path<String> getRootByQueryFiledString(String queryFiled, Path<Object> path) {
        if (queryFiled.indexOf(".") < 0) {
            return path.get(queryFiled);
        } else {
            return getRootByQueryFiledString(queryFiled.substring(queryFiled.indexOf(".") + 1, queryFiled.length()),
                    path.get(queryFiled.substring(0, queryFiled.indexOf("."))));
        }
    }

    private Path<Comparable> getRootByQueryFiledComparable(String queryFiled, Root<T> root) {
        if (queryFiled.indexOf(".") < 0) {
            return root.get(queryFiled);
        } else {
            return getRootByQueryFiledComparable(queryFiled.substring(queryFiled.indexOf(".") + 1, queryFiled.length()),
                    root.get(queryFiled.substring(0, queryFiled.indexOf("."))));
        }
    }

    private Path<Comparable> getRootByQueryFiledComparable(String queryFiled, Path<Object> path) {
        if (queryFiled.indexOf(".") < 0) {
            return path.get(queryFiled);
        } else {
            return getRootByQueryFiledComparable(queryFiled.substring(queryFiled.indexOf(".") + 1, queryFiled.length()),
                    path.get(queryFiled.substring(0, queryFiled.indexOf("."))));
        }
    }

    @Override
    public List<T> findAllForList(DataQueryObjectPage query) {
        final DataQueryObjectPage dqo = query;
        return this.findAll(new Specification<T>() {

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return exmple.getPredocate(root, query, cb, dqo);
            }
        });
    }

}