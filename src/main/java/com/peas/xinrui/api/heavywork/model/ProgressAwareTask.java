package com.peas.xinrui.api.heavywork.model;

import com.peas.xinrui.api.heavywork.service.HeavyWorkService;
import com.peas.xinrui.common.L;

public abstract class ProgressAwareTask implements Runnable {
    protected HeavyWork work;
    protected HeavyWorkService heavyWorkService;
    protected int progress = 0;

    public ProgressAwareTask(HeavyWork work, HeavyWorkService heavyWorkService) {
        super();
        this.work = work;
        this.heavyWorkService = heavyWorkService;
    }

    protected final int getProgress() {
        return progress;
    }

    protected final void doUpdateProgress(int progress) {
        doUpdateProgress(progress, 99);
    }

    protected final void doUpdateProgress(int progress, int noMoreThan) {
        try {
            // 进度不能倒退
            if (progress <= this.progress) {
                return;
            }
            if (progress > noMoreThan) {
                progress = noMoreThan;
            }
            this.progress = progress;
            heavyWorkService.updateProgress(work.getId(), progress);
        } catch (Exception ex) {
            L.error(ex);
        }
    }

    protected final void reportSuccess(String output) {
        try {
            heavyWorkService.updateSuccess(work.getId(), output);
        } catch (Exception ex) {
            L.error(ex);
        }
    }

    protected final void reportFail(String output) {
        try {
            heavyWorkService.updateFailed(work.getId(), output);
        } catch (Exception ex) {
            L.error(ex);
        }
    }
}
