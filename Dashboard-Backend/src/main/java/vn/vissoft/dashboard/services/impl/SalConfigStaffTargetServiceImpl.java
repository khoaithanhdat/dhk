package vn.vissoft.dashboard.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vissoft.dashboard.dto.SalConfigStaffTargetDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.mapper.BaseMapper;
import vn.vissoft.dashboard.model.SalConfigStaffTarget;
import vn.vissoft.dashboard.repo.SalConfigStaffTargetRepo;
import vn.vissoft.dashboard.services.SalConfigStaffTargetService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SalConfigStaffTargetServiceImpl implements SalConfigStaffTargetService {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private SalConfigStaffTargetRepo salConfigStaffTargetRepo;

    private BaseMapper<SalConfigStaffTarget, SalConfigStaffTargetDTO> mapper = new BaseMapper<SalConfigStaffTarget, SalConfigStaffTargetDTO>(SalConfigStaffTarget.class, SalConfigStaffTargetDTO.class);

    @Override
    public Optional<SalConfigStaffTarget> getById(Long id) {
        Optional<SalConfigStaffTarget> salConfigStaffTarget = salConfigStaffTargetRepo.findByIdAndStatus(id, "1");
        return salConfigStaffTarget;
    }

    @Override
    public List<SalConfigStaffTargetDTO> getAllSalConfigTargetActive(String status) {
        List<SalConfigStaffTarget> salConfigStaffTarget = salConfigStaffTargetRepo.findAllByStatusLike(status);
        return mapper.toDtoBean(salConfigStaffTarget);
    }

    @Override
    public List getAllSalConfigTargetByServiceId() {
        List<SalConfigStaffTarget> salConfigStaffTargets = salConfigStaffTargetRepo.getOrderByServiceId();
        if (salConfigStaffTargets.size() > 0) {
            List salConfigStaffTargetByServiceId = new ArrayList();
            salConfigStaffTargets.forEach(item -> {
                if (item.getServiceId() != 0) {
                    salConfigStaffTargetByServiceId.add(salConfigStaffTargetRepo.findAllByServiceIdAndStatusLike(item.getServiceId(), "1"));
                }
            });
            return salConfigStaffTargetByServiceId;
        }
        return null;
    }

    @Override
    public List<SalConfigStaffTargetDTO> getAllSalConfigTargetCompleteByServiceId(Long serviceId) {
        List<SalConfigStaffTarget> salConfigStaffTargetDTOS = salConfigStaffTargetRepo.findAllByServiceIdAndStatusLike(serviceId, "1");
        return mapper.toDtoBean(salConfigStaffTargetDTOS);
    }

    /**
     *  staffType = 2 - cụm trưởng
     * @param salConfigStaffTargetDTO
     * @param staffDTO
     * @author tunglm
     */

    @Override
    public void saveSalConfigStaffTarget(SalConfigStaffTargetDTO salConfigStaffTargetDTO, StaffDTO staffDTO) throws Exception{
            Optional<SalConfigStaffTarget> salConfigStaffTargetById = salConfigStaffTargetRepo.findByIdAndStatus(salConfigStaffTargetDTO.getId(), "1");
            if (salConfigStaffTargetById.isPresent()) {
                salConfigStaffTargetDTO.setUpdatedUser(staffDTO.getStaffCode());
                salConfigStaffTargetDTO.setUpdatedDate(new Date());
            } else {
                salConfigStaffTargetDTO.setCreatedDate(new Date());
                salConfigStaffTargetDTO.setCreatedUser(staffDTO.getStaffCode());
            }
            salConfigStaffTargetRepo.save(mapper.toPersistenceBean(salConfigStaffTargetDTO));
    }
}
