package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalaryTime;

import java.util.List;

@Repository
public interface SalaryTimeRepo extends JpaRepository<SalaryTime,Integer>,SalaryTimeRepoCustom {

    List<SalaryTime> findByStaffTypeAndStatus(String staffType,String status) throws Exception;
    List<SalaryTime> findByStatus(String status) throws Exception;

}
