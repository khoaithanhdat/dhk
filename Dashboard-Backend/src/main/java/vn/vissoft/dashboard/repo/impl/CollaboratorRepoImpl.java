package vn.vissoft.dashboard.repo.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import vn.vissoft.dashboard.controller.ServiceScoreController;
import vn.vissoft.dashboard.dto.CollaboratorDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.CollaboratorServiceExel;
import vn.vissoft.dashboard.dto.excel.VdsStaffExcel;
import vn.vissoft.dashboard.helper.Constants;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.model.ActionAudit;
import vn.vissoft.dashboard.model.Collaborator;
import vn.vissoft.dashboard.model.VdsStaff;
import vn.vissoft.dashboard.repo.CollaboratorRepo;
import vn.vissoft.dashboard.repo.CollaboratorRepoCustom;
import vn.vissoft.dashboard.services.ActionAuditService;
import vn.vissoft.dashboard.services.ActionDetailService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollaboratorRepoImpl implements CollaboratorRepoCustom {
    private static final Logger LOGGER = LogManager.getLogger(ServiceScoreController.class);

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private CollaboratorRepo collaboratorRepo;

    @Autowired
    private ActionAuditService actionAuditService;

    @Autowired
    private ActionDetailService actionDetailService;

    @Override
    public List<CollaboratorDTO> getCollaborator() throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT c.id,c.code,c.name,c.status,c.created_date,c.created_user,c.updated_date,c.updated_user FROM dashboard_dev.sal_collaborator c order by c.created_date desc;");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        return setCollaboratorDTO(query.getResultList());
    }

    @Override
    public List<Collaborator> getCollaboratorByCondition(CollaboratorDTO collaboratorDTO) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();
        HashMap hashMap = new HashMap();
        sqlBuilder.append("SELECT c FROM Collaborator c");
        sqlBuilder.append(" where 1=1 ");
        if (!DataUtil.isNullOrEmpty(collaboratorDTO.getCode())){
            sqlBuilder.append(" and c.code like :code");
            hashMap.put("code", "%"+collaboratorDTO.getCode().trim()+"%");
        }
        if (!DataUtil.isNullOrEmpty(collaboratorDTO.getName())){
            sqlBuilder.append(" and c.name like :name");
            hashMap.put("name", "%"+collaboratorDTO.getName().trim()+"%");
        }
        sqlBuilder.append(" order by c.createdDate desc");
        Query query = entityManager.createQuery(sqlBuilder.toString());
        hashMap.forEach((k, v) -> {
            query.setParameter(k.toString(), v);
        });
        return query.getResultList();
    }

    @Transactional
    @Override
    public int deleteCollaborator(int id) throws Exception {
        int count;
        StringBuilder vsbdSqlBuilder = new StringBuilder();
        vsbdSqlBuilder.append("delete from dashboard_dev.sal_collaborator  where id = :id");
        Query query = entityManager.createNativeQuery(vsbdSqlBuilder.toString());
        query.setParameter("id", id);
        count = query.executeUpdate();
        return count;
    }

    private List<CollaboratorDTO> setCollaboratorDTO(List<Object[]> objects) {
        List<CollaboratorDTO> ListCollaboratorDTO = null;
        if (!DataUtil.isNullOrEmpty(objects)) {
            ListCollaboratorDTO = new ArrayList<>();
            for (Object[] object : objects) {
                CollaboratorDTO dto = new CollaboratorDTO();
                dto.setId(DataUtil.safeToInt(object[0]));
                dto.setCode(DataUtil.safeToString(object[1]));
                dto.setName(DataUtil.safeToString(object[2]));
                dto.setStatus(DataUtil.safeToString(object[3]));
                dto.setCreatedDate((Timestamp) (object[4]));
                dto.setCreatedUser(DataUtil.safeToString(object[5]));
                dto.setUpdatedDate((Timestamp) (object[6]));
                dto.setUpdatedUser(DataUtil.safeToString(object[7]));
                ListCollaboratorDTO.add(dto);
            }
        }
        return ListCollaboratorDTO;
    }

    @Override
    @Transactional
    public String persist(Collaborator collaborator, StaffDTO staffDTO) throws Exception {
        try {
            long vlngTime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(vlngTime);
            if (!DataUtil.isNullObject(collaborator)) {
                String vstrCheck = checkDuplicateCollaborator(collaborator.getCode());
                if (DataUtil.isNullOrEmpty(vstrCheck)) {
                    collaborator.setCode(collaborator.getCode().trim());
                    collaborator.setName(collaborator.getName().trim());
                    collaborator.setCreatedDate(timestamp);
                    collaborator.setCreatedUser(staffDTO.getStaffCode());
                    collaborator.setStatus("1");
                    entityManager.persist(collaborator);
                    saveAction(collaborator, staffDTO);
                } else {
                    return vstrCheck;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String checkDuplicateCollaborator(String pstrCollaboratorCode) throws Exception {
        try {
            Collaborator collaborator = collaboratorRepo.findByCode(pstrCollaboratorCode.trim());
            if (DataUtil.isNullObject(collaborator))
                return null;
            else return Constants.DUPLICATED_VTT_GROUP_CHANNEL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveAction(Collaborator collaborator, StaffDTO staffDTO) {
        ActionAudit actionAudit = actionAuditService.log(Constants.COLLABORATOR, Constants.CREATE, staffDTO.getStaffCode(), new Long(collaborator.getId()), staffDTO.getShopCode());
        actionDetailService.createActionDetail(Constants.COLLABORATORS.COLLABORATOR_CODE, collaborator.getCode().trim(), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.COLLABORATORS.COLLABORATOR_NAME, collaborator.getName().trim(), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.COLLABORATORS.COLLABORATOR_STATUS, collaborator.getStatus(), actionAudit.getId(), null);
    }

    @Override
    public Collaborator findCollaboratorExcel(CollaboratorServiceExel collaboratorServiceExel) throws Exception {
        try {
            StringBuilder sqlBuilder = new StringBuilder();
            HashMap hashMap = new HashMap();
            sqlBuilder.append("SELECT c.id,c.code,c.name,c.status,c.created_date,c.created_user,c.updated_date,c.updated_user FROM dashboard_dev.sal_collaborator c");
            sqlBuilder.append(conditionSQLExcel(collaboratorServiceExel, hashMap));
            Query query = entityManager.createQuery(sqlBuilder.toString());
            hashMap.forEach((k, v) -> {
                query.setParameter(k.toString(), v);
            });
            List<Collaborator> list = query.getResultList();
            if (list.size() > 0) {
                return list.get(0);
            } else return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    StringBuilder conditionSQLExcel(CollaboratorServiceExel collaboratorServiceExel, HashMap hashMap) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" where 1 = 1");
        if (!DataUtil.isNullOrEmpty(collaboratorServiceExel.getCode())) {
            sqlBuilder.append(" and code = :code");
            hashMap.put("code", collaboratorServiceExel.getCode());
        }
        return sqlBuilder;
    }
}
