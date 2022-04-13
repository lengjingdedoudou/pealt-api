package com.peas.xinrui.api.ui.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;
import com.peas.xinrui.api.course.entity.CourseWO;
import com.peas.xinrui.api.course.model.CourseSchool;
import com.peas.xinrui.api.course.qo.CourseSchoolQo;
import com.peas.xinrui.api.course.service.CourseService;
import com.peas.xinrui.api.schadmin.entity.SchAdminContexts;
import com.peas.xinrui.api.trainer.model.Trainer;
import com.peas.xinrui.api.trainer.service.TrainerService;
import com.peas.xinrui.api.ui.entity.SettingError;
import com.peas.xinrui.api.ui.entity.UIComponent;
import com.peas.xinrui.api.ui.entity.UIComponentKey;
import com.peas.xinrui.api.ui.entity.UIComponentKeyVO;
import com.peas.xinrui.api.ui.entity.UIComponentVO;
import com.peas.xinrui.api.ui.entity.UIType;
import com.peas.xinrui.api.ui.entity.UITypeVO;
import com.peas.xinrui.api.ui.model.UI;
import com.peas.xinrui.api.ui.qo.UIQo;
import com.peas.xinrui.api.ui.repository.UIRepository;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.cache.KvCacheWrap;
import com.peas.xinrui.common.cache.SingleRepositoryProvider;
import com.peas.xinrui.common.controller.auth.SessionType;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.model.Constants;
import com.peas.xinrui.common.model.ErrorCode;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UIServiceImpl implements UIService, SettingError {

    @Autowired
    private UIRepository uiRepository;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TrainerService trainerService;

    private KvCacheWrap<String, UI> uiCache;

    @PostConstruct
    public void init() {
        uiCache = kvCacheFactory.create(new CacheOptions("ui", 1, DateUtils.SECOND_PER_HOUR),
                new SingleRepositoryProvider<String, UI>() {

                    @Override
                    public UI findByKey(String typeAndSchoolId) throws Exception {
                        Byte type = Byte.parseByte(typeAndSchoolId.substring(0, 1));
                        Integer schoolId = Integer.parseInt(typeAndSchoolId.substring(1, typeAndSchoolId.length()));
                        // 查询某个学校某个端启用的模版
                        UI ui = uiRepository.findByTypeAndIsDefaultAndSchoolId(type, Constants.STATUS_OK, schoolId);
                        // 整理排行榜数据，根据buyNum倒序排列
                        for (UIComponent component : ui.getComponents()) {
                            if (component.getKey().equals(UIComponentKey.TOP.name())) {
                                Page<CourseSchool> courseSchools = courseService.courses(
                                        new CourseSchoolQo("buyNum", schoolId), CourseWO.getNonInstance(),
                                        SessionType.NONE);
                                List<CourseSchool> courseSchoolList = courseSchools.getContent();
                                List<CourseSchool> courseSchools1 = new ArrayList<>();
                                if (UIType.WX_HOME.getType().equals(ui.getType())) {
                                    courseSchools1 = courseSchoolList.size() > 3 ? courseSchoolList.subList(0, 3)
                                            : courseSchoolList;
                                } else if (UIType.PC_HOME.getType().equals(ui.getType())) {
                                    courseSchools1 = courseSchoolList.size() > 4 ? courseSchoolList.subList(0, 4)
                                            : courseSchoolList;
                                }
                                component.setList(courseSchools1);
                            }
                        }
                        return ui;
                    }

                }, new BeanModelConverter<>(UI.class));
    }

    @Override
    public List<UI> uis(UIQo qo) {
        qo.setSchoolId(SchAdminContexts.getSchoolId());
        return uiRepository.findAll(qo);
    }

    private UI findById(Integer id) {
        UI ui = uiRepository.findById(id).orElse(null);
        if (StringUtils.isNull(ui)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return ui;
    }

    @Override
    public UI ui(int id) {
        return findById(id);
    }

    @Override
    public List<UIComponentVO> components() {
        UIComponentKey[] uiComponentKeys = UIComponentKey.values();
        List<UIComponentVO> uiComponentVOs = new ArrayList<>();

        for (UIComponentKey uiComponentKey : uiComponentKeys) {
            uiComponentVOs.add(new UIComponentVO(uiComponentKey.name(), uiComponentKey.getWithTitle(),
                    uiComponentKey.getTitle(), uiComponentKey.getWithList(), uiComponentKey.getRequiredKeys()));
        }

        return uiComponentVOs;
    }

    @Transactional
    @Override
    public void syncData(Byte type) {
        String key = type + "" + SchAdminContexts.getSchoolId();
        UI ui = uiCache.findByKey(key);
        List<UIComponent> components = ui.getComponents().stream().map((component) -> {
            List<Long> ids = new ArrayList<>();

            String k = component.getKey();
            List<?> list = component.getList();
            if (k.equals("PAY") || k.equals("FREE")) {
                ids = list.stream().map((cs) -> {
                    Map<String, Object> cMap = JSON.parseObject(cs.toString(), Map.class);
                    long courseId = Long.valueOf(String.valueOf(cMap.get("courseId")));

                    return courseId;
                }).collect(Collectors.toList());
            }

            if (k.equals("TRAINER")) {
                ids = list.stream().map((cs) -> {
                    Map<String, Object> tMap = JSON.parseObject(cs.toString(), Map.class);
                    long trainerId = Long.valueOf(String.valueOf(tMap.get("id")));

                    return trainerId;
                }).collect(Collectors.toList());
            }

            if (k.equals("PAY") || k.equals("FREE")) {
                CourseSchoolQo csQo = new CourseSchoolQo();
                csQo.setCourseIds(ids);
                List<CourseSchool> courseSchools = courseService.coursesForList(csQo,
                        CourseWO.getAllInstance().setList(true), SessionType.SCHADMIN);

                list = list.stream().map((cs) -> {
                    Map<String, Object> cMap = JSON.parseObject(cs.toString(), Map.class);
                    long courseId = Long.valueOf(String.valueOf(cMap.get("courseId")));
                    CourseSchool curCs = null;
                    for (CourseSchool sc1 : courseSchools) {
                        if (sc1.getCourseId() == courseId) {
                            curCs = sc1;
                        }
                    }
                    cMap = assemb(cMap, CourseSchool.class, JSON.parseObject(JSON.toJSONString(curCs)));
                    return cMap;
                }).collect(Collectors.toList());
            }
            if (k.equals("TRAINER")) {
                Map<Long, Trainer> tMap = trainerService.trainersForMap(ids);

                list = list.stream().map((cs) -> {
                    Map<String, Object> cMap = JSON.parseObject(cs.toString(), Map.class);
                    long trainerId = Long.valueOf(String.valueOf(cMap.get("id")));
                    Trainer curTr = tMap.get(trainerId);

                    cMap = assemb(cMap, Trainer.class, JSON.parseObject(JSON.toJSONString(curTr)));
                    return cMap;
                }).collect(Collectors.toList());
            }
            component.setList(list);
            return component;
        }).collect(Collectors.toList());
        ui.setComponents(components);
        save(ui);
        uiCache.remove(key);
    }

    private Map<String, Object> assemb(Map<String, Object> map, Class<?> clazz, Map<String, Object> curObj) {
        for (String field : map.keySet()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals(field)) {
                    try {
                        f.setAccessible(true);
                        if (f.getType() instanceof Object && f.getType() != String.class && f.getType() != Byte.class
                                && f.getType() != Integer.class && f.getType() != Long.class
                                && f.getType() != List.class) {

                            Map<String, Object> curNextMap = JSON.parseObject(JSON.toJSONString(map.get(field)));
                            Map<String, Object> objNextMap = JSON.parseObject(JSON.toJSONString(curObj.get(field)));
                            map.put(field, assemb(curNextMap, f.getType(), objNextMap));
                        } else {
                            map.put(field, curObj.get(field));
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return map;
    }

    @Override
    public UI uiByTypeAndSchoolId(Byte type, Integer schoolId) {
        String typeAndSchoolId = type.toString() + schoolId.toString();
        return uiCache.findByKey(typeAndSchoolId);
    }

    /**
     * 保存模版对象：将type+schoolId作为一个字段存储于缓存中
     */
    @Override
    @Transactional
    public void save(UI ui) throws ServiceException {

        validateUI(ui);

        Integer id = ui.getId();
        if (id == null || id == 0) {
            uiRepository.save(ui);
        } else {
            UI exist = ui(id);
            exist.setTitle(ui.getTitle());
            exist.setComponents(ui.getComponents());
            uiRepository.save(exist);
        }

        if (ui.getIsDefault() == Constants.STATUS_OK) {
            setDefault(ui.getId(), ui.getType());
        }
        String typeAndSchoolId = ui.getType().toString() + ui.getSchoolId().toString();
        uiCache.remove(typeAndSchoolId);
    }

    /**
     * 启用默认模版：先把所有模版停用，启用指定模版
     */
    @Override
    @Transactional
    public void setDefault(int id, byte type) throws ServiceException {
        Integer schoolId = SchAdminContexts.getSchoolId();
        uiRepository.offDefault(Constants.STATUS_HALT, schoolId, type);
        UI ui = ui(id);
        ui.setIsDefault(Constants.STATUS_OK);
        uiRepository.save(ui);

    }

    /**
     * 删除模版数据：如果启用状态，则报错
     */
    @Override
    @Transactional
    public void remove(int id) throws ServiceException {
        UI ui = ui(id);
        if (ui.getIsDefault() == Constants.STATUS_OK) {
            throw new ServiceException(ERR_PERMISSION_DENIED);
        }
        uiRepository.delete(ui);
    }

    /**
     * 验证模版数据：验证UI组件
     */
    private void validateUI(UI ui) throws ServiceException {

        String title = ui.getTitle();
        if (StringUtils.isEmpty(title)) {
            throw new ServiceException(ERR_TITLE_VALID_DENIED);
        }

        if (!UITypeVO.contains(ui.getType())) {
            throw new ServiceException(ERR_UI_TYPE_VALID_DENIED);
        }

        List<UIComponent> components = ui.getComponents();
        if (components.size() == 0) {
            throw new ServiceException(ERR_UI_COMPONENT_VALID_DENIED);
        }

        for (UIComponent component : components) {
            if (!UIComponentKeyVO.contains(component.getKey())
                    || (!UIComponentKeyVO.contains("TOP") && CollectionUtils.isEmpty(component.getList()))) {
                throw new ServiceException(ERR_UI_COMPONENT_VALID_DENIED);
            }
        }
        if (ui.getIsDefault() == null) {
            ui.setIsDefault(Constants.STATUS_HALT);
        }

        boolean update = ui.getId() != null && ui.getId() > 0;

        if (!update) {
            ui.setCreatedAt(System.currentTimeMillis());
            ui.setSchoolId(SchAdminContexts.getSchoolId());
        }
    }
}
