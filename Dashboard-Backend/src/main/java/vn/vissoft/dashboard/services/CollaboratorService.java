package vn.vissoft.dashboard.services;

import org.springframework.web.multipart.MultipartFile;
import vn.vissoft.dashboard.dto.CollaboratorDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.BaseUploadEntity;
import vn.vissoft.dashboard.model.Collaborator;

import java.util.List;

public interface CollaboratorService {

    List<CollaboratorDTO> getCollaborator() throws Exception;

    List<Collaborator> getCollaboratorByCondition(CollaboratorDTO collaboratorDTO) throws Exception;

    public String deleteCollaborator(int id,StaffDTO staffDTO) throws Exception;

    String persist(Collaborator collaborator, StaffDTO staffDTO) throws Exception;

    String checkDuplicateCollaborator(String pstrCollaboratorCode) throws Exception;

    BaseUploadEntity upload(MultipartFile file, StaffDTO staffDTO) throws Exception;
}
