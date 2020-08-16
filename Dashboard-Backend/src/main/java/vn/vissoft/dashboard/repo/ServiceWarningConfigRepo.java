package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.ServiceWarningConfig;
@Repository
public interface ServiceWarningConfigRepo extends JpaRepository<ServiceWarningConfig,Long>,ServiceWarningRepoCustom {
}
