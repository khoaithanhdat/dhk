package vn.vissoft.dashboard.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vissoft.dashboard.helper.Constants;
import vn.vissoft.dashboard.model.SalArea;
import vn.vissoft.dashboard.repo.SalAreaRepo;
import vn.vissoft.dashboard.services.SalAreaService;

import java.util.List;

@Service
public class SalAreaServiceImpl implements SalAreaService {

    @Autowired
    private SalAreaRepo salAreaRepo;

    @Override
    public List<SalArea> getActiveSalArea() throws Exception {
        return salAreaRepo.findSalAreaByStatus(Constants.PARAM_STATUS);
    }
}
