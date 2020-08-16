package vn.vissoft.dashboard.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.vissoft.dashboard.config.I18N;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.BaseUploadEntity;
import vn.vissoft.dashboard.dto.excel.EliminateNumberExcel;

import vn.vissoft.dashboard.dto.excel.KpiActionProgramExcel;
import vn.vissoft.dashboard.helper.Constants;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.helper.excelreader.ExcelReader;

import vn.vissoft.dashboard.model.SalLeaderKpi;
import vn.vissoft.dashboard.model.SalStaffMeasureImport;
import vn.vissoft.dashboard.repo.*;
import vn.vissoft.dashboard.services.ImportSalaryService;
import vn.vissoft.dashboard.services.PlanMonthlyService;

import java.io.File;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ImportSalaryServiceImpl implements ImportSalaryService {

    public static final Logger LOGGER = LogManager.getLogger(ImportSalaryServiceImpl.class);

    @Value("${server.tomcat.basedir}")
    private String mstrUploadPath;

    @Autowired
    private PlanMonthlyService planMonthlyService;

    @Autowired
    private ChannelRepo channelRepo;

    @Autowired
    private VdsStaffRepo vdsStaffRepo;

    @Autowired
    private SalLeaderKpiRepo salLeaderKpiRepo;

    @Autowired
    private PartnerRepo partnerRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private SalStaffMeasureImportRepo salStaffMeasureImportRepo;

    @Override
    public BaseUploadEntity upload(MultipartFile file, StaffDTO staffDTO, String pstrImportType, Long prdId) throws Exception {
        int vintCheck = 0;
        int vintSumSuccessfulRecord = 0;
        int vintSumRecord = 0;

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
            if (!Constants.EXCEL_EXTENSION.XLSX.equals(extension) && !Constants.EXCEL_EXTENSION.XLS.equals(extension)) {
                baseUploadEntity.setMessage(I18N.get("upload.file.excel.type.error"));
                return baseUploadEntity;
            }
            BigDecimal size = new BigDecimal(file.getSize()).divide(new BigDecimal(1048576));
            BigDecimal limitSize=new BigDecimal(2);
            if (size.compareTo(limitSize)==1) {
                baseUploadEntity.setMessage(I18N.get("upload.file.excel.size.error"));
                return baseUploadEntity;
            }

            if (Constants.IMPORT_TYPE.ELIMINATE_TYPE.equals(pstrImportType)) {
                ExcelReader<EliminateNumberExcel> vreader = new ExcelReader<>();
                List<EliminateNumberExcel> vlstEliminateExcels = vreader.read(vstrOriginalName, EliminateNumberExcel.class);

                if (DataUtil.isNullOrEmpty(vlstEliminateExcels)) {
                    baseUploadEntity.setMessage(I18N.get(Constants.EMPTY_FILE_ERROR));
                    return baseUploadEntity;
                } else {
                    planMonthlyService.checkTheSameRecord(vlstEliminateExcels);

                    for (EliminateNumberExcel eliminateNumberExcel : vlstEliminateExcels) {
                        if (!DataUtil.isNullObject(eliminateNumberExcel)) {
                            vintSumRecord++;
                            if (DataUtil.isNullOrEmpty(eliminateNumberExcel.getError())) {
                                if (!checkShopCodeOfProvince(eliminateNumberExcel.getShopCode().trim())) {
                                    eliminateNumberExcel.setError(I18N.get("common.excel.active.province.error") + ". " + I18N.get("common.excel.column.error") + " B ");
                                } else if (!checkExistedStaffInChannel(eliminateNumberExcel.getShopCode().trim(), eliminateNumberExcel.getStaffCode())) {
                                    eliminateNumberExcel.setError(I18N.get("common.excel.staff.in.province.error") + ". " + I18N.get("common.excel.column.error") + " C ");
                                }
                                else if (!serviceRepo.checkExistedServiceCode(eliminateNumberExcel.getServiceCode().trim())) {
                                    eliminateNumberExcel.setError(I18N.get("common.excel.active.error") + ". " + I18N.get("common.excel.column.error") + " D ");
                                }
                            }
                            if (DataUtil.isNullOrEmpty(eliminateNumberExcel.getError())) {
                                try {
                                    Timestamp currentDate = new Timestamp(System.currentTimeMillis());
                                    Long vlngServiceId = serviceRepo.findServiceIdByCode(eliminateNumberExcel.getServiceCode().trim());
                                    SalStaffMeasureImport salStaffMeasureImport = salStaffMeasureImportRepo.getStaffMeasureFromPlan(eliminateNumberExcel, staffDTO, prdId, vlngServiceId);
                                    Integer intPrdId = Math.toIntExact(DataUtil.convertToPrdId(prdId));
                                    if (DataUtil.isNullObject(salStaffMeasureImport)) {
                                        salStaffMeasureImportRepo.save(new SalStaffMeasureImport(intPrdId, Constants.PROVINCE_CODE, eliminateNumberExcel.getShopCode().trim(),
                                                eliminateNumberExcel.getStaffCode().trim(), Math.toIntExact(vlngServiceId), eliminateNumberExcel.getMonthBoltNumber(),eliminateNumberExcel.getEliminateNumber(),
                                                staffDTO.getStaffCode(), currentDate ));
                                    } else {
                                        salStaffMeasureImport.setMonthyImportValue(eliminateNumberExcel.getMonthBoltNumber());
                                        salStaffMeasureImport.setExcludeValue(eliminateNumberExcel.getEliminateNumber());
                                        salStaffMeasureImport.setCreatedDate(currentDate);
                                        salStaffMeasureImport.setCreatedUser(staffDTO.getStaffCode());
                                        salStaffMeasureImportRepo.save(salStaffMeasureImport);
                                    }
                                    vintSumSuccessfulRecord++;
                                } catch (Exception e) {
                                    LOGGER.error(e.getMessage(), e);
                                    eliminateNumberExcel.setError(e.getMessage());
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
                vreader.writeResultFile(vstrOriginalName, vstrResultFilePath, EliminateNumberExcel.class, vlstEliminateExcels, I18N.get("common.success.status"));
            } else if (Constants.IMPORT_TYPE.KPI_TYPE.equals(pstrImportType)) {
                ExcelReader<KpiActionProgramExcel> vreader = new ExcelReader<>();
                List<KpiActionProgramExcel> vlstKpiActions = vreader.read(vstrOriginalName, KpiActionProgramExcel.class);

                if (DataUtil.isNullOrEmpty(vlstKpiActions)) {
                    baseUploadEntity.setMessage(I18N.get(Constants.EMPTY_FILE_ERROR));
                    return baseUploadEntity;
                } else {
                    planMonthlyService.checkTheSameRecord(vlstKpiActions);

                    for (KpiActionProgramExcel kpiActionProgramExcel : vlstKpiActions) {
                        if (!DataUtil.isNullObject(kpiActionProgramExcel)) {
                            vintSumRecord++;
                            if (DataUtil.isNullOrEmpty(kpiActionProgramExcel.getError())) {
                                if (!checkShopCodeOfProvince(kpiActionProgramExcel.getShopCode().trim())) {
                                    kpiActionProgramExcel.setError(I18N.get("common.excel.active.province.error") + ". " + I18N.get("common.excel.column.error") + " B ");
                                } else if (!checkExistedStaffInChannel(kpiActionProgramExcel.getShopCode().trim(), kpiActionProgramExcel.getStaffCode())) {
                                    kpiActionProgramExcel.setError(I18N.get("common.excel.staff.in.province.error") + ". " + I18N.get("common.excel.column.error") + " C ");
                                } else if (!checkExistedLeaderStaffInChannel(kpiActionProgramExcel.getShopCode().trim(), kpiActionProgramExcel.getStaffCode())) {
                                    kpiActionProgramExcel.setError(I18N.get("common.excel.leader.staff.error") + ". " + I18N.get("common.excel.column.error") + " C ");
                                }
                            }
                            if (DataUtil.isNullOrEmpty(kpiActionProgramExcel.getError())) {
                                try {
                                    Timestamp currentDate = new Timestamp(System.currentTimeMillis());
                                    SalLeaderKpi salLeaderKpi = salLeaderKpiRepo.getLeaderKpiFromPlan(kpiActionProgramExcel, staffDTO, prdId);
                                    Integer intPrdId = Math.toIntExact(DataUtil.convertToPrdId(prdId));
                                    if (DataUtil.isNullObject(salLeaderKpi)) {
                                        salLeaderKpiRepo.save(new SalLeaderKpi(intPrdId, Constants.PROVINCE_CODE, kpiActionProgramExcel.getShopCode().trim(),
                                                kpiActionProgramExcel.getStaffCode().trim(), kpiActionProgramExcel.getSchedule(),kpiActionProgramExcel.getPerform(), kpiActionProgramExcel.getDensity(),
                                                currentDate, staffDTO.getStaffCode()));
                                    } else {
                                        salLeaderKpi.setSchedule(kpiActionProgramExcel.getSchedule());
                                        salLeaderKpi.setSchedule(kpiActionProgramExcel.getPerform());
                                        salLeaderKpi.setProportion(kpiActionProgramExcel.getDensity());
                                        salLeaderKpi.setCreatedDate(currentDate);
                                        salLeaderKpi.setCreatedUser(staffDTO.getStaffCode());
                                        salLeaderKpiRepo.save(salLeaderKpi);
                                    }
                                    vintSumSuccessfulRecord++;
                                } catch (Exception e) {
                                    LOGGER.error(e.getMessage(), e);
                                    kpiActionProgramExcel.setError(e.getMessage());
                                }
                            }
                        } else {
                            vintCheck++;
                            continue;
                        }
                    }
                    //check empty file
                    if (vintCheck == vlstKpiActions.size()) {
                        baseUploadEntity.setMessage(I18N.get(Constants.EMPTY_FILE_ERROR));
                        return baseUploadEntity;
                    }
                }
                vreader.writeResultFile(vstrOriginalName, vstrResultFilePath, KpiActionProgramExcel.class, vlstKpiActions, I18N.get("common.success.status"));
            }
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


    private boolean checkExistedStaffInChannel(String pstrChannelCode, String pstrStaffCode) throws Exception {
        boolean vblnCheck = false;
        List<String> lstStaffCodes = vdsStaffRepo.getStaffCodeByChannelCode(pstrChannelCode);
        if (!DataUtil.isNullOrEmpty(lstStaffCodes)) {
            for (String staffCode : lstStaffCodes) {
                if (pstrStaffCode.trim().equalsIgnoreCase(staffCode.trim())) {
                    vblnCheck = true;
                    break;
                }
            }
        }

        return vblnCheck;
    }

    private boolean checkExistedLeaderStaffInChannel(String pstrShopCode, String pstrStaffCode) throws Exception {
        boolean vblnCheck = false;
        List<String> lstStaffCodes = vdsStaffRepo.getLeaderStaffInProvince(pstrShopCode);
        if (!DataUtil.isNullOrEmpty(lstStaffCodes)) {
            for (String staffCode : lstStaffCodes) {
                if (pstrStaffCode.trim().equalsIgnoreCase(staffCode.trim())) {
                    vblnCheck = true;
                    break;
                }
            }
        }

        return vblnCheck;
    }

    private boolean checkShopCodeOfProvince(String shopCode) throws Exception {
        boolean checkShopOfProvince = false;
        List<String> vlstShopCodeOfProvince = partnerRepo.getPartnerOfProvince();

        if (!DataUtil.isNullOrEmpty(vlstShopCodeOfProvince)) {
            if (vlstShopCodeOfProvince.contains(shopCode.trim()))
                checkShopOfProvince = true;
        }

        return checkShopOfProvince;
    }
}
