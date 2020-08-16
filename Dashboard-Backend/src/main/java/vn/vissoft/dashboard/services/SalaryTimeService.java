package vn.vissoft.dashboard.services;

import vn.vissoft.dashboard.dto.ConfigSaleAreaSalaryDTO;
import vn.vissoft.dashboard.dto.SalaryTimeDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.model.SalaryTime;

import java.util.List;

public interface SalaryTimeService {

    ConfigSaleAreaSalaryDTO getSalaryByArea(String pstrArea) throws Exception;

    // update cau hinh
    String updateConfig(ConfigSaleAreaSalaryDTO configSaleAreaSalaryDTO, StaffDTO staffDTO) throws Exception;

    void deleteSalaryTime(SalaryTimeDTO salaryTimeDTO) throws Exception;

}
