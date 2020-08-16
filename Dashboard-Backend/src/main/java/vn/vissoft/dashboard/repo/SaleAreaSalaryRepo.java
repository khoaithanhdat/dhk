package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SaleAreaSalary;

@Repository
public interface SaleAreaSalaryRepo extends JpaRepository<SaleAreaSalary,Integer>,SaleAreaSalaryRepoCustom {

    SaleAreaSalary findByAreaCode(String pstrAreaCode) throws Exception;
}
