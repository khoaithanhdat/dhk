package vn.vissoft.dashboard.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vissoft.dashboard.mapper.BaseMapper;
import vn.vissoft.dashboard.model.SaleFee;
import vn.vissoft.dashboard.repo.SaleFeeRepo;
import vn.vissoft.dashboard.services.SaleFeeService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class SaleFeeServiceImpl implements SaleFeeService {

    private BaseMapper<SaleFee, vn.vissoft.dashboard.dto.SaleFee> mapper = new BaseMapper<SaleFee, vn.vissoft.dashboard.dto.SaleFee>(SaleFee.class, vn.vissoft.dashboard.dto.SaleFee.class);

    @Autowired
    private SaleFeeRepo saleFeeRepo;

    @Override
    public List<vn.vissoft.dashboard.dto.SaleFee> getAllBySaleFeeActive(String status) {
        return mapper.toDtoBean(saleFeeRepo.findAllByStatusLike(status));
    }

    @Override
    public vn.vissoft.dashboard.dto.SaleFee getByIdAndStatus(Long id, String status) {
        Optional<SaleFee> saleFee = saleFeeRepo.findByIdAndStatus(id, status);
        if (saleFee.isPresent()) {
            return mapper.toDtoBean(saleFee.get());
        }
        return null;
    }
}
