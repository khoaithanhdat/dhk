package vn.vissoft.dashboard.repo;

import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.EliminateNumberExcel;
import vn.vissoft.dashboard.model.SalStaffMeasureImport;

public interface SalStaffMeasureImportRepoCustom {
    SalStaffMeasureImport getStaffMeasureFromPlan(EliminateNumberExcel eliminateNumberExcel , StaffDTO staffDTO, Long prdId,Long serviceId) throws Exception;
}
