package vn.vissoft.dashboard.repo;

import vn.vissoft.dashboard.dto.CollaboratorDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.CollaboratorServiceExel;
import vn.vissoft.dashboard.model.Collaborator;

import java.util.List;

public interface CollaboratorRepoCustom {
    public List<CollaboratorDTO> getCollaborator() throws Exception;

    public List<Collaborator> getCollaboratorByCondition(CollaboratorDTO collaboratorDTO) throws Exception;

    public int deleteCollaborator(int id) throws Exception;

    String persist(Collaborator collaborator, StaffDTO staffDTO) throws Exception;

    String checkDuplicateCollaborator(String pstrCollaboratorCode) throws Exception;

    public Collaborator findCollaboratorExcel(CollaboratorServiceExel collaboratorServiceExel) throws Exception;

}
