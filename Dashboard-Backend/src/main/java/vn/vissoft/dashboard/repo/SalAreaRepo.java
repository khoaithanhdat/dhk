package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalArea;

import java.util.List;

@Repository
public interface SalAreaRepo extends JpaRepository<SalArea,Integer> {

    List<SalArea> findSalAreaByStatus(String pstrStatus) throws Exception;
}
