package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalConfigSaleFee;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalConfigSaleFeeRepo extends JpaRepository<SalConfigSaleFee, Long>, SalConfigSaleFeeRepoCustom {

    List<SalConfigSaleFee> findAllByStatusLike(String status);

    Optional<SalConfigSaleFee> findByIdAndStatus(Long id, String status);
}
