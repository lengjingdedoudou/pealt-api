package com.peas.xinrui.api.heavywork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peas.xinrui.api.heavywork.entity.HeavyWorkConstants;
import com.peas.xinrui.api.heavywork.model.HeavyWork;
import com.peas.xinrui.api.heavywork.repository.HeavyWorkRepository;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.model.ErrorCode;
import com.peas.xinrui.common.utils.StringUtils;

@Service
public class HeavyWorkServiceImpl implements HeavyWorkService {

    @Autowired
    private HeavyWorkRepository heavyWorkRepository;

    @Override
    public HeavyWork create(String owner) {
        HeavyWork job = new HeavyWork();
        job.setSecret(StringUtils.getRandNum(64));
        job.setOwner(owner);
        job.setProgress(0);
        job.setCreatedAt(System.currentTimeMillis());
        job.setUpdatedAt(job.getCreatedAt());
        job.setStatus(HeavyWorkConstants.STATUS_INIT);
        heavyWorkRepository.save(job);
        return job;
    }

    private HeavyWork getById(int id) {
        return heavyWorkRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void updateProgress(Integer id, int progress) {
        if (progress > 100) {
            progress = 100;
        }

        HeavyWork work = getById(id);
        work.setUpdatedAt(System.currentTimeMillis());
        work.setProgress(progress);
        heavyWorkRepository.save(work);
    }

    @Override
    public void updateFailed(Integer id, String errors) {
        HeavyWork work = getById(id);
        work.setUpdatedAt(System.currentTimeMillis());
        work.setStatus(HeavyWorkConstants.STATUS_FAILED);
        work.setErrors(errors);

        heavyWorkRepository.save(work);

    }

    @Override
    public void updateSuccess(Integer id, String output) {
        HeavyWork work = getById(id);
        work.setUpdatedAt(System.currentTimeMillis());
        work.setStatus(HeavyWorkConstants.STATUS_SUCCESS);
        work.setProgress(100);
        work.setOutput(output);
        heavyWorkRepository.save(work);
    }

    // @Override
    // public void updateSuccess(Integer id, String output, String fileName, Long
    // fileSize) {
    //
    // HeavyWork work = getById(id);
    // work.setUpdatedAt(System.currentTimeMillis());
    // work.setStatus(HeavyWorkConstants.STATUS_SUCCESS);
    // work.setProgress(100);
    // work.setOutput(output);
    // work.setFileName(fileName);
    // work.setFileSize(fileSize);
    //
    // heavyWorkRepository.save(work);
    //
    // }

    @Override
    public HeavyWork getById(Integer id, String secret) {

        HeavyWork work = heavyWorkRepository.findById(id).orElse(null);
        if (work == null || !work.getSecret().equals(secret)) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return work;
    }

    @Transactional
    @Override
    public int getProgress(String secret) {
        if (StringUtils.isEmpty(secret)) {
            return 0;
        }
        return heavyWorkRepository.findProgressBySecret(secret);
    }
}
