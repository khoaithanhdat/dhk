package vn.vissoft.dashboard.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vissoft.dashboard.dto.SalConfigStaffGiffDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.mapper.BaseMapper;
import vn.vissoft.dashboard.model.SalConfigStaffGiff;
import vn.vissoft.dashboard.repo.SalConfigStaffGiffRepo;
import vn.vissoft.dashboard.services.SalConfigStaffGiffService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SalConfigStaffGiffServiceImpl implements SalConfigStaffGiffService {

    private BaseMapper<SalConfigStaffGiff, SalConfigStaffGiffDTO> mapper = new BaseMapper<SalConfigStaffGiff, SalConfigStaffGiffDTO>(SalConfigStaffGiff.class, SalConfigStaffGiffDTO.class);

    @Autowired
    private SalConfigStaffGiffRepo salConfigStaffGiffRepo;

    @Override
    public List<SalConfigStaffGiffDTO> getAllSalConfigStaffGiffActive(String status) {
        return mapper.toDtoBean(salConfigStaffGiffRepo.findAllByStatusLike(status));
    }

    @Override
    public SalConfigStaffGiffDTO getSalConfigStaffGiffById(Long id, String status) {
        Optional<SalConfigStaffGiff> salConfigStaffGiff = salConfigStaffGiffRepo.getByIdAndStatusLike(id, status);
        if (salConfigStaffGiff.isPresent()) {
            return mapper.toDtoBean(salConfigStaffGiff.get());
        }
        return null;
    }

    @Override
    public void saveSalConfigStaffGiff(SalConfigStaffGiffDTO salConfigStaffGiffDTO, StaffDTO staffDTO) throws Exception {
         Optional<SalConfigStaffGiff> salConfigStaffGiffById = salConfigStaffGiffRepo.getByIdAndStatusLike(salConfigStaffGiffDTO.getId(), "1");
         if (salConfigStaffGiffById.isPresent()) {
             salConfigStaffGiffDTO.setUpdatedUser(staffDTO.getStaffCode());
             salConfigStaffGiffDTO.setUpdatedDate(new Date());
         } else {
             salConfigStaffGiffDTO.setCreatedUser(staffDTO.getStaffCode());
             salConfigStaffGiffDTO.setCreatedDate(new Date());
         }
         salConfigStaffGiffRepo.save(mapper.toPersistenceBean(salConfigStaffGiffDTO));
    }
}
