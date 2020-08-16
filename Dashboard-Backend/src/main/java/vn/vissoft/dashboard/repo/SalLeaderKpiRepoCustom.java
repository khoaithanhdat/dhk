package vn.vissoft.dashboard.repo;

import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.KpiActionProgramExcel;
import vn.vissoft.dashboard.model.SalLeaderKpi;

public interface SalLeaderKpiRepoCustom  {
    SalLeaderKpi getLeaderKpiFromPlan(KpiActionProgramExcel kpiActionProgramExcel , StaffDTO staffDTO,Long prdId) throws Exception;
}
