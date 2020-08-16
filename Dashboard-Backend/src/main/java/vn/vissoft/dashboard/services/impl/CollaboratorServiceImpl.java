package vn.vissoft.dashboard.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.vissoft.dashboard.config.I18N;
import vn.vissoft.dashboard.dto.CollaboratorDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.BaseUploadEntity;
import vn.vissoft.dashboard.dto.excel.CollaboratorServiceExel;
import vn.vissoft.dashboard.helper.Constants;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.helper.excelreader.ExcelReader;
import vn.vissoft.dashboard.model.*;
import vn.vissoft.dashboard.repo.ActionAuditRepo;
import vn.vissoft.dashboard.repo.CollaboratorRepo;
import vn.vissoft.dashboard.services.ActionAuditService;
import vn.vissoft.dashboard.services.ActionDetailService;
import vn.vissoft.dashboard.services.CollaboratorService;
import vn.vissoft.dashboard.services.PlanMonthlyService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Service
public class CollaboratorServiceImpl implements CollaboratorService {

    public static final Logger LOGGER = LogManager.getLogger(CollaboratorServiceImpl.class);

    @Value("${spring.servlet.multipart.max-file-size}")
    private String fileSize ;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private ActionAuditService actionAuditService;

    @Autowired
    private CollaboratorRepo collaboratorRepo;

    @Autowired
    private PlanMonthlyService planMonthlyService;

    @Autowired
    private ActionDetailService actionDetailService;

    @Autowired
    private ActionAuditRepo actionAuditRepo;

    @Override
    public List<CollaboratorDTO> getCollaborator() throws Exception {
        return collaboratorRepo.getCollaborator();
    }

    @Override
    public List<Collaborator> getCollaboratorByCondition(CollaboratorDTO collaboratorDTO) throws Exception {
        return collaboratorRepo.getCollaboratorByCondition(collaboratorDTO);
    }

    public boolean checkCollaborator(int id) throws Exception {
        boolean vblnCheck;
        List<Object> vlstCheck = new ArrayList<>();
        List<Collaborator> collaboratorList = collaboratorRepo.findById(id);
        vlstCheck.addAll(collaboratorList);

        if (vlstCheck.size() > 0) {
            vblnCheck = true;
        } else {
            vblnCheck = false;
        }

        return vblnCheck;
    }

    @Transactional
    @Override
    public String deleteCollaborator(int id, StaffDTO staffDTO) throws Exception {
        String message;
        long vlngDateTime = System.currentTimeMillis();
        Date vdtCurrentTime = new Date(vlngDateTime);

        if (checkCollaborator(id)) {
            Collaborator collaborator1 = collaboratorRepo.getOne(id);

            ActionAudit actionAudit = new ActionAudit();
            actionAudit.setActionCode(Constants.ACTION_CODE_DELETE);
            actionAudit.setActionDateTime(vdtCurrentTime);
            actionAudit.setPkID((long) id);
            actionAudit.setObjectCode(Constants.COLLABORATOR);
            actionAudit.setUser(staffDTO.getStaffCode());
            actionAuditRepo.save(actionAudit);

            if (collaborator1.getCode() != null) {
                actionDetailService.createActionDetail(Constants.COLLABORATORS.COLLABORATOR_CODE, null, actionAudit.getId(), collaborator1.getCode().trim());
            }
            if (collaborator1.getName() != null) {
                actionDetailService.createActionDetail(Constants.COLLABORATORS.COLLABORATOR_NAME, null, actionAudit.getId(), collaborator1.getName().trim());
            }
            if (collaborator1.getStatus() != null) {
                actionDetailService.createActionDetail(Constants.COLLABORATORS.COLLABORATOR_STATUS, null, actionAudit.getId(), collaborator1.getStatus().trim());
            }

            collaboratorRepo.deleteCollaborator(id);
            message = Constants.CONFIG_GROUP_CARDS.SUCCESS;
        }
        else {
            message = "Khong co";
        }
        return message;
    }

    @Value("${server.tomcat.basedir}")
    private String mstrUploadPath;

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
    public BaseUploadEntity upload(MultipartFile file, StaffDTO staffDTO) throws Exception {
        int vintCheck = 0;
        int vintSumSuccessfulRecord = 0;
        int vintSumRecord = 0;
        Double sizeUpload;

        BaseUploadEntity baseUploadEntity = new BaseUploadEntity();
        String vstrOriginalName = file.getOriginalFilename();
        String vstrPath = mstrUploadPath;

        File customDir = new File(vstrPath);

        if (!customDir.exists()) {
            customDir.mkdir();
        }

        int vintIndex = vstrOriginalName.lastIndexOf(".");
        String vstrResultFileName;
        if (vintIndex > -1) {
            vstrResultFileName = vstrOriginalName.substring(0, vstrOriginalName.lastIndexOf(".")) + "_Ketqua.xlsx";
        } else {
            vstrResultFileName = vstrOriginalName + "_Ketqua.xlsx";
        }
        vstrOriginalName = customDir.getAbsolutePath() + File.separator + vstrOriginalName;


        String vstrResultFilePath = customDir.getAbsolutePath() + File.separator + vstrResultFileName;
        planMonthlyService.saveFile(file.getInputStream(), vstrOriginalName);

        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            if (!Constants.EXCEL_EXTENSION.XLSX.equals(extension)) {
                baseUploadEntity.setMessage(I18N.get("Chỉ được import file có định dạng excel"));
                return baseUploadEntity;
            }
            System.out.println(fileSize);
            //sizeUpload = Double.parseDouble(fileSize.substring(0,1));


            BigDecimal size = new BigDecimal(file.getSize()).divide(new BigDecimal(1048576));
            BigDecimal limitSize = new BigDecimal(2);
            if (size.compareTo(limitSize) == 1) {
                baseUploadEntity.setMessage(I18N.get("Chỉ được import file có dung lượng nhỏ hơn hoặc bằng 2MB"));
                return baseUploadEntity;
            }

            ExcelReader<CollaboratorServiceExel> vreader = new ExcelReader<>();
            List<CollaboratorServiceExel> vlstEliminateExcels = vreader.read(vstrOriginalName, CollaboratorServiceExel.class);

            if (DataUtil.isNullOrEmpty(vlstEliminateExcels)) {
                baseUploadEntity.setMessage(I18N.get(Constants.EMPTY_FILE_ERROR));
                return baseUploadEntity;
            } else {
                planMonthlyService.checkTheSameRecord(vlstEliminateExcels);

                for (CollaboratorServiceExel collaboratorServiceExel : vlstEliminateExcels) {
                    if (!DataUtil.isNullObject(collaboratorServiceExel)) {
                        vintSumRecord++;
                        if (DataUtil.isNullOrEmpty(collaboratorServiceExel.getError())) {
                            if (checkExistedCollaboratorByCode(collaboratorServiceExel.getCode().trim())) {
                                collaboratorServiceExel.setError("Thất bại (CTV đã tồn tại)");
                            }
                        }
                        if (DataUtil.isNullOrEmpty(collaboratorServiceExel.getError())) {
                            try {
                                Timestamp currentDate = new Timestamp(System.currentTimeMillis());
                                Collaborator collaborator = collaboratorRepo.findCollaboratorExcel(collaboratorServiceExel);
                                if (DataUtil.isNullObject(collaborator)) {
                                    collaboratorRepo.persist(new Collaborator(collaboratorServiceExel.getCode().trim(),
                                            collaboratorServiceExel.getName().trim()), staffDTO);
                                } else {
                                    collaborator.setCode(collaboratorServiceExel.getCode());
                                    collaborator.setName(collaboratorServiceExel.getName());
                                    collaborator.setCreatedDate(currentDate);
                                    collaborator.setCreatedUser(staffDTO.getStaffCode());
                                    collaboratorRepo.persist(collaborator, staffDTO);
                                }
                                vintSumSuccessfulRecord++;
                            } catch (Exception e) {
                                LOGGER.error(e.getMessage(), e);
                                collaboratorServiceExel.setError(e.getMessage());
                            }
                        }
                    } else {
                        vintCheck++;
                        continue;
                    }
                }
                //check empty file
                if (vintCheck == vlstEliminateExcels.size()) {
                    baseUploadEntity.setMessage(I18N.get(Constants.EMPTY_FILE_ERROR));
                    return baseUploadEntity;
                }
            }
            vreader.writeResultFile(vstrOriginalName, vstrResultFilePath, CollaboratorServiceExel.class, vlstEliminateExcels, I18N.get("common.success.status"));

            //set cac thong tin tra ve frontend
            baseUploadEntity.setResultFileName(vstrResultFileName);
            baseUploadEntity.setSumSuccessfulRecord(vintSumSuccessfulRecord);
            baseUploadEntity.setSumRecord(vintSumRecord);

        } catch (
                Exception e) {
            baseUploadEntity.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return baseUploadEntity;
    }

    private boolean checkExistedCollaboratorByCode(String pstrCollaboratorCode) throws Exception {
        boolean vblnCheck = false;
        List<Collaborator> lstCollaboratorCode = collaboratorRepo.getCollaboratorByCode(pstrCollaboratorCode);
        if (!DataUtil.isNullOrEmpty(lstCollaboratorCode)) {
            for (Collaborator collaborator : lstCollaboratorCode) {
                if (pstrCollaboratorCode.trim().equalsIgnoreCase(collaborator.getCode().trim())) {
                    vblnCheck = true;
                    break;
                }
            }
        }
        return vblnCheck;
    }

}
