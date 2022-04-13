package com.peas.xinrui.api.heavywork.service;

import com.peas.xinrui.api.heavywork.model.HeavyWork;

public interface HeavyWorkService {

    HeavyWork create(String owner);

    HeavyWork getById(Integer id, String secret);

    void updateProgress(Integer id, int progress);

    void updateFailed(Integer id, String errors);

    void updateSuccess(Integer id, String output);

    int getProgress(String owner);
    // void updateSuccess(Integer id, String output, String fileName, Long
    // fileSize);
    //
}
