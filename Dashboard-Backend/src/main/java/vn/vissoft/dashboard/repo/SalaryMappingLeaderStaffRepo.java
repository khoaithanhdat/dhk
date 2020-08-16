package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalaryMappingLeaderStaff;
@Repository
public interface SalaryMappingLeaderStaffRepo extends JpaRepository<SalaryMappingLeaderStaff,Long>,SalaryMappingLeaderStaffRepoCustom{
}
