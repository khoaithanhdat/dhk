package vn.vissoft.dashboard.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vissoft.dashboard.dto.SalConfigSaleFeeDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.mapper.BaseMapper;
import vn.vissoft.dashboard.model.SalConfigSaleFee;
import vn.vissoft.dashboard.repo.SalConfigSaleFeeRepo;
import vn.vissoft.dashboard.services.SalConfigSaleFeeService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SalConfigSaleFeeServiceImpl implements SalConfigSaleFeeService {

    private BaseMapper<SalConfigSaleFee, SalConfigSaleFeeDTO> mapper = new BaseMapper<SalConfigSaleFee, SalConfigSaleFeeDTO>(SalConfigSaleFee.class, SalConfigSaleFeeDTO.class);

    @Autowired
    private SalConfigSaleFeeRepo salConfigSaleFeeRepo;

    @Override
    public List<SalConfigSaleFeeDTO> getAllSalConfigSaleFeeActive(String status) {
        return mapper.toDtoBean(salConfigSaleFeeRepo.findAllByStatusLike("1"));
    }

    @Override
    public Optional<SalConfigSaleFee> getById(Long id) {
        Optional<SalConfigSaleFee> salConfigSaleFee = salConfigSaleFeeRepo.findByIdAndStatus(id, "1");
        return salConfigSaleFee;
    }

    @Override
    public List<SalConfigSaleFeeDTO> getByCondition(String feeName, String receiveFrom) {
        return mapper.toDtoBean(salConfigSaleFeeRepo.findSalConfigSaleFeeByCondition(feeName, receiveFrom));
    }

    @Override
    public void saveSalConfigSaleFee(SalConfigSaleFeeDTO salConfigSaleFeeDTO, StaffDTO staffDTO) throws Exception {
        Optional<SalConfigSaleFee> salConfigSaleFeeDTOById = salConfigSaleFeeRepo.findByIdAndStatus(salConfigSaleFeeDTO.getId(), "1");

        if (salConfigSaleFeeDTOById.isPresent()) {
            salConfigSaleFeeDTO.setUpdatedDate(new Date());
            salConfigSaleFeeDTO.setUpdatedUser(staffDTO.getStaffCode());
        } else {
            salConfigSaleFeeDTO.setCreatedDate(new Date());
            salConfigSaleFeeDTO.setCreatedUser(staffDTO.getStaffCode());
        }

        SalConfigSaleFee salConfigSaleFee =  mapper.toPersistenceBean(salConfigSaleFeeDTO);
        salConfigSaleFeeRepo.save(salConfigSaleFee);
    }
}
