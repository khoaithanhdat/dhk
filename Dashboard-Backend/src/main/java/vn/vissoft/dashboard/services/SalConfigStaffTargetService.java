package vn.vissoft.dashboard.services;

import vn.vissoft.dashboard.dto.SalConfigStaffTargetDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.VdsStaffDTO;
import vn.vissoft.dashboard.model.SalConfigStaffTarget;

import java.util.List;
import java.util.Optional;

public interface SalConfigStaffTargetService {

    Optional<SalConfigStaffTarget> getById(Long id);

    List<SalConfigStaffTargetDTO> getAllSalConfigTargetActive(String status);

    List<SalConfigStaffTargetDTO> getAllSalConfigTargetByServiceId();

    List<SalConfigStaffTargetDTO> getAllSalConfigTargetCompleteByServiceId(Long serviceId);

    void saveSalConfigStaffTarget(SalConfigStaffTargetDTO salConfigStaffTargetDTO, StaffDTO staffDTO) throws Exception;
}
