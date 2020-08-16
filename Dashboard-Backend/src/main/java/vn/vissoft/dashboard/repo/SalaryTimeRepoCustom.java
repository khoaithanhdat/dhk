package vn.vissoft.dashboard.repo;

import vn.vissoft.dashboard.dto.ConfigSaleAreaSalaryDTO;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.model.SalaryTime;

import java.util.List;

public interface SalaryTimeRepoCustom {

    List<Object[]> findByAreaSalaryId(Integer areaSalaryId,String status) throws Exception;
}
