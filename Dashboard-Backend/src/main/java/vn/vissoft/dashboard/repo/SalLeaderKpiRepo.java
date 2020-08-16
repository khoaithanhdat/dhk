package vn.vissoft.dashboard.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalLeaderKpi;

@Repository
public interface SalLeaderKpiRepo extends JpaRepository<SalLeaderKpi,Integer>,SalLeaderKpiRepoCustom {
}
