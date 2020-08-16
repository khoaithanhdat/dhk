package vn.vissoft.dashboard.services;

import vn.vissoft.dashboard.dto.SaleFee;

import java.util.List;

public interface SaleFeeService {

    List<SaleFee> getAllBySaleFeeActive(String status);

    SaleFee getByIdAndStatus(Long id, String status);
}
