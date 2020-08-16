package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalConfigStaffTarget;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalConfigStaffTargetRepo extends JpaRepository<SalConfigStaffTarget, Long> {

    List<SalConfigStaffTarget> findAllByStatusLike(String status);

    @Query(value = "select * from sal_config_staff_target as t where t.status = '1' GROUP BY t.service_id", nativeQuery = true)
    List<SalConfigStaffTarget> getOrderByServiceId();

    List<SalConfigStaffTarget> findAllByServiceIdAndStatusLike(Long serviceId, String status);

    Optional<SalConfigStaffTarget> findByIdAndStatus(Long id, String status);
}
