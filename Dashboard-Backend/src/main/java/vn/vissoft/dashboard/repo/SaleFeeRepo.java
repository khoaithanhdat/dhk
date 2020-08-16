package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SaleFee;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleFeeRepo extends JpaRepository<SaleFee, Long> {

    List<SaleFee> findAllByStatusLike(String status);

    Optional<SaleFee> findByIdAndStatus(Long id, String status);
}
