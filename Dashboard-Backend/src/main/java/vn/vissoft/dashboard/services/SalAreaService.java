package vn.vissoft.dashboard.services;

import vn.vissoft.dashboard.model.SalArea;

import java.util.List;

public interface SalAreaService {

    List<SalArea> getActiveSalArea() throws Exception;
}
