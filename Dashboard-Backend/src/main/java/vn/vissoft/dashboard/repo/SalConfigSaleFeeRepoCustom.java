package vn.vissoft.dashboard.repo;

import vn.vissoft.dashboard.model.SalConfigSaleFee;

import java.util.List;

public interface SalConfigSaleFeeRepoCustom {

    List<SalConfigSaleFee> findSalConfigSaleFeeByCondition(String feeName, String receiveFrom);
}
