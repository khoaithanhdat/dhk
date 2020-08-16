package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalConfigHsTinhLuong;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalConfigHsTinhLuongRepo extends JpaRepository<SalConfigHsTinhLuong, Long> {

    List<SalConfigHsTinhLuong> findAllByStatusLike(String status);

    Optional<SalConfigHsTinhLuong> findByIdAndStatus(Long id, String status);
}
