package vn.vissoft.dashboard.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vissoft.dashboard.dto.SalConfigHsTinhLuongDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.mapper.BaseMapper;
import vn.vissoft.dashboard.model.SalConfigHsTinhLuong;
import vn.vissoft.dashboard.repo.SalConfigHsTinhLuongRepo;
import vn.vissoft.dashboard.services.SalConfigHsTinhLuongService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SalConfigHsTinhLuongServiceImpl implements SalConfigHsTinhLuongService {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private BaseMapper<SalConfigHsTinhLuong, SalConfigHsTinhLuongDTO> mapper = new BaseMapper<SalConfigHsTinhLuong, SalConfigHsTinhLuongDTO>(SalConfigHsTinhLuong.class, SalConfigHsTinhLuongDTO.class);

    @Autowired
    private SalConfigHsTinhLuongRepo salConfigHsTinhLuongRepo;

    @Override
    public Optional<SalConfigHsTinhLuong> getById(Long id) {
        Optional<SalConfigHsTinhLuong> salConfigHsTinhLuong = salConfigHsTinhLuongRepo.findByIdAndStatus(id, "1");
        return salConfigHsTinhLuong;
    }

    @Override
    public List<SalConfigHsTinhLuongDTO> getAllSalConfigHsTinhLuong(String status) {
        List<SalConfigHsTinhLuong> salConfigHsTinhLuongs = salConfigHsTinhLuongRepo.findAllByStatusLike(status);
        return mapper.toDtoBean(salConfigHsTinhLuongs);
    }

    @Override
    public void saveSalConfigHsTinhLuong(SalConfigHsTinhLuongDTO salConfigHsTinhLuongDTO, StaffDTO staffDTO) throws Exception{
            Optional<SalConfigHsTinhLuong> salConfigHsTinhLuongById = salConfigHsTinhLuongRepo.findByIdAndStatus(salConfigHsTinhLuongDTO.getId(), "1");
            if (salConfigHsTinhLuongById.isPresent()) {
                salConfigHsTinhLuongDTO.setUpdatedUser(staffDTO.getStaffCode());
                salConfigHsTinhLuongDTO.setUpdatedDate(new Date());
            } else {
                salConfigHsTinhLuongDTO.setCreatedDate(new Date());
                salConfigHsTinhLuongDTO.setCreatedUser(staffDTO.getStaffCode());
            }
            salConfigHsTinhLuongRepo.save(mapper.toPersistenceBean(salConfigHsTinhLuongDTO));
    }
}
