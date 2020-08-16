package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalaryMappingAreaProvince;

@Repository
public interface SalaryMappingAreaProvinceRepo extends JpaRepository<SalaryMappingAreaProvince, Long>, SalaryMappingAreaProvinceRepoCustom {

}
