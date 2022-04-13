package com.peas.xinrui.api.ui.service;

import java.util.List;

import com.peas.xinrui.api.ui.entity.UIComponentVO;
import com.peas.xinrui.api.ui.model.UI;
import com.peas.xinrui.api.ui.qo.UIQo;
import com.peas.xinrui.common.exception.ServiceException;

public interface UIService {

    List<UI> uis(UIQo qo);

    UI ui(int id);

    List<UIComponentVO> components();

    void syncData(Byte type);

    UI uiByTypeAndSchoolId(Byte type, Integer schoolId);

    void save(UI ui) throws ServiceException;

    void setDefault(int id, byte type) throws ServiceException;

    void remove(int id) throws ServiceException;

}
