package vn.vissoft.dashboard.services;

import vn.vissoft.dashboard.dto.SalConfigStaffGiffDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.VdsStaffDTO;

import java.util.List;

public interface SalConfigStaffGiffService {

    List<SalConfigStaffGiffDTO> getAllSalConfigStaffGiffActive(String status);

    SalConfigStaffGiffDTO getSalConfigStaffGiffById(Long id, String status);

    void saveSalConfigStaffGiff(SalConfigStaffGiffDTO salConfigStaffGiffDTO, StaffDTO staffDTO) throws Exception;
}
