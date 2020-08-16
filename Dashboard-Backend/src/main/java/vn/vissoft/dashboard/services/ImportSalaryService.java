package vn.vissoft.dashboard.services;

import org.springframework.web.multipart.MultipartFile;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.BaseUploadEntity;

public interface ImportSalaryService {
    BaseUploadEntity upload(MultipartFile file, StaffDTO staffDTO,String pstrImportType,Long prdId) throws Exception;

}
