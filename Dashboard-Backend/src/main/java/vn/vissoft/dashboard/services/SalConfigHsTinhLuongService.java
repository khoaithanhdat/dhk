package vn.vissoft.dashboard.services;

import vn.vissoft.dashboard.dto.SalConfigHsTinhLuongDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.VdsStaffDTO;
import vn.vissoft.dashboard.model.SalConfigHsTinhLuong;

import java.util.List;
import java.util.Optional;

public interface SalConfigHsTinhLuongService {

    Optional<SalConfigHsTinhLuong> getById(Long id);

    List<SalConfigHsTinhLuongDTO> getAllSalConfigHsTinhLuong(String status);

    void saveSalConfigHsTinhLuong(SalConfigHsTinhLuongDTO salConfigHsTinhLuongDTO, StaffDTO staffDTO) throws Exception;
}
