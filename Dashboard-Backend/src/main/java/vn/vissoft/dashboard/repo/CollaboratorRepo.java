package vn.vissoft.dashboard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.model.Collaborator;

import java.util.List;

@Repository
public interface CollaboratorRepo extends JpaRepository<Collaborator, Integer> , CollaboratorRepoCustom {

    Collaborator findByCode(String pstrCollaboratorCode) throws Exception;
    List<Collaborator> findById(int collaboratorId) throws Exception;
    List<Collaborator> getCollaboratorByCode(String code) throws Exception;
}
