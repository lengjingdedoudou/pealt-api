package com.peas.xinrui.api.heavywork.model;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Workbook;

import com.peas.xinrui.api.file.entity.UploadOptions;
import com.peas.xinrui.api.file.service.FileService;
import com.peas.xinrui.api.heavywork.service.HeavyWorkService;
import com.peas.xinrui.common.L;
import com.peas.xinrui.common.service.LocalFileHelper;
import com.peas.xinrui.common.service.ServiceManager;
import com.peas.xinrui.common.utils.FileUtils;

public abstract class ExportExcelTask extends ProgressAwareTask {
    private FileService fileService;
    private LocalFileHelper localFileHelper;

    public ExportExcelTask(HeavyWork work, HeavyWorkService heavyWorkService, FileService fileService) {
        super(work, heavyWorkService);
        this.fileService = fileService;
        localFileHelper = ServiceManager.get(LocalFileHelper.class);
        if (localFileHelper == null) {
            throw new NullPointerException("localFileHelper");
        }
    }

    protected abstract Workbook writeToWorkbook() throws Exception;

    @Override
    public final void run() {
        // 开始
        doUpdateProgress(1);
        // 生成表格
        Workbook wb = null;
        File file = null;
        try {
            wb = writeToWorkbook();
            file = localFileHelper.createTmpFile("xlsx");
            // 写入文件
            {
                FileOutputStream out = new FileOutputStream(file);
                wb.write(out);
                out.flush();
                out.close();
            }
            // 上传前先报告一下状态
            doUpdateProgress(95);
            // 上传
            String exportUrl = fileService.upload(file, new UploadOptions().setPrivat(true));
            // 报告成功
            reportSuccess(exportUrl);
        } catch (Exception ex) {
            L.error(ex);
            reportFail(null);
        } finally {
            FileUtils.close(wb);
            FileUtils.deleteQuietly(file);
        }
    }

}
