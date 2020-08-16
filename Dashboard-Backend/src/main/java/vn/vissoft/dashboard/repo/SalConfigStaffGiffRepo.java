package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalConfigStaffGiff;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalConfigStaffGiffRepo extends JpaRepository<SalConfigStaffGiff, Long> {

    List<SalConfigStaffGiff> findAllByStatusLike(String status);

    Optional<SalConfigStaffGiff> getByIdAndStatusLike(Long id, String status);

}
