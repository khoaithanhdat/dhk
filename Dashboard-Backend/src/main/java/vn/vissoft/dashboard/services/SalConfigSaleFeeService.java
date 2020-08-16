package vn.vissoft.dashboard.services;

import vn.vissoft.dashboard.dto.SalConfigSaleFeeDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.VdsStaffDTO;
import vn.vissoft.dashboard.model.SalConfigSaleFee;

import java.util.List;
import java.util.Optional;

public interface SalConfigSaleFeeService {

    List<SalConfigSaleFeeDTO> getAllSalConfigSaleFeeActive(String status);

    Optional<SalConfigSaleFee> getById(Long id);

    List<SalConfigSaleFeeDTO> getByCondition(String feeName, String receiveFrom);

    void saveSalConfigSaleFee(SalConfigSaleFeeDTO salConfigSaleFeeDTO, StaffDTO staffDTO) throws Exception;
}
