package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.model.SalStaffMeasureImport;

@Repository
public interface SalStaffMeasureImportRepo extends JpaRepository<SalStaffMeasureImport,Integer>,SalStaffMeasureImportRepoCustom {
}
