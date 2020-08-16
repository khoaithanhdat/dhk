package vn.vissoft.dashboard.services.impl;

import com.google.common.collect.Lists;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vissoft.dashboard.config.I18N;
import vn.vissoft.dashboard.dto.ConfigSaleAreaSalaryDTO;
import vn.vissoft.dashboard.dto.SalaryTimeDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.helper.Constants;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.mapper.BaseMapper;
import vn.vissoft.dashboard.model.ActionAudit;
import vn.vissoft.dashboard.model.SalaryTime;
import vn.vissoft.dashboard.model.SaleAreaSalary;
import vn.vissoft.dashboard.repo.SalaryTimeRepo;
import vn.vissoft.dashboard.repo.SaleAreaSalaryRepo;
import vn.vissoft.dashboard.services.ActionAuditService;
import vn.vissoft.dashboard.services.ActionDetailService;
import vn.vissoft.dashboard.services.SalaryTimeService;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class SalaryTimeServiceImpl implements SalaryTimeService {

    private static final Logger LOGGER = LogManager.getLogger(SalaryTimeServiceImpl.class);

    private BaseMapper<SalaryTimeDTO, SalaryTime> mapper = new BaseMapper<SalaryTimeDTO, SalaryTime>(SalaryTimeDTO.class, SalaryTime.class);


    @Autowired
    private SalaryTimeRepo salaryTimeRepo;

    @Autowired
    SaleAreaSalaryRepo saleAreaSalaryRepo;

    @Autowired
    private ActionAuditService actionAuditService;

    @Autowired
    private ActionDetailService actionDetailService;

    @Override
    public ConfigSaleAreaSalaryDTO getSalaryByArea(String pstrArea) throws Exception {
        SaleAreaSalary saleAreaSalary = saleAreaSalaryRepo.findByAreaCode(pstrArea);
        ConfigSaleAreaSalaryDTO configSaleAreaSalaryDTO = null;

        if (!DataUtil.isNullObject(saleAreaSalary)) {
            configSaleAreaSalaryDTO = new ConfigSaleAreaSalaryDTO();
            configSaleAreaSalaryDTO.setAreaCode(saleAreaSalary.getAreaCode());
            configSaleAreaSalaryDTO.setHardSalary(saleAreaSalary.getHardSalary());
            configSaleAreaSalaryDTO.setTargetSalary(saleAreaSalary.getTargetSalary());
            configSaleAreaSalaryDTO.setHardSalaryByTime(saleAreaSalary.getHardSalaryByTime());
            configSaleAreaSalaryDTO.setTargetSalaryByTime(saleAreaSalary.getTargetSalaryByTime());
            configSaleAreaSalaryDTO.setUpdatedUser(saleAreaSalary.getUpdatedUser());

            List<SalaryTime> salaryTimes = findByAreaSalaryId(saleAreaSalary.getId(), Constants.PARAM_STATUS);
            configSaleAreaSalaryDTO.setLstSalaryTimeDTOS(mapper.toPersistenceBean(salaryTimes));
        }
        return configSaleAreaSalaryDTO;
    }

    private  List<SalaryTime> findByAreaSalaryId(Integer areaSalaryId, String status) throws Exception {
        List<SalaryTime> lstSalaryTimes = new ArrayList<>();
        List<Object[]> lstObjects = salaryTimeRepo.findByAreaSalaryId(areaSalaryId, status);

        if (!DataUtil.isNullOrEmpty(lstObjects)) {
            for (Object[] object : lstObjects) {
                SalaryTime salaryTime = new SalaryTime();
                salaryTime.setId((Integer) object[0]);
                salaryTime.setAreaSalaryId((Integer) object[1]);
                salaryTime.setSalary((Double) object[2]);
                salaryTime.setExpiredMonth((Date) object[3]);
                salaryTime.setStaffType((String) object[4]);
                salaryTime.setCreatedDate((Timestamp) object[5]);
                salaryTime.setCreatedUser((String) object[6]);
                salaryTime.setUpdatedDate((Timestamp) object[7]);
                salaryTime.setUpdatedUser((String) object[8]);
                salaryTime.setStatus((String) object[9]);

                lstSalaryTimes.add(salaryTime);

            }
        }
        return lstSalaryTimes;
    }

    @Override
    public String updateConfig(ConfigSaleAreaSalaryDTO configSaleAreaSalaryDTO, StaffDTO staffDTO) throws Exception {
        String message = null;
        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        Date date = null;

        if (!DataUtil.isNullObject(configSaleAreaSalaryDTO)) {
            SaleAreaSalary saleAreaSalary = saleAreaSalaryRepo.findByAreaCode(configSaleAreaSalaryDTO.getAreaCode());
            if (DataUtil.isNullObject(saleAreaSalary)) {
                SaleAreaSalary saleAreaSalary1 = new SaleAreaSalary(configSaleAreaSalaryDTO.getAreaCode(), configSaleAreaSalaryDTO.getHardSalary(), Constants.PARAM_STATUS
                        , configSaleAreaSalaryDTO.getTargetSalary(), Constants.PARAM_STATUS, new Timestamp(System.currentTimeMillis()), staffDTO.getStaffCode());
                saleAreaSalaryRepo.save(saleAreaSalary1);
                saveActionAuditSalArea(saleAreaSalary1, staffDTO);

            } else {
                saveActionDetailSalArea(saleAreaSalary, configSaleAreaSalaryDTO, staffDTO);

                saleAreaSalary.setHardSalary(configSaleAreaSalaryDTO.getHardSalary());
                saleAreaSalary.setTargetSalary(configSaleAreaSalaryDTO.getTargetSalary());
                if (DataUtil.isNullOrEmpty(configSaleAreaSalaryDTO.getLstSalaryTimeDTOS())) {
                    saleAreaSalary.setHardSalaryByTime(Constants.PARAM_STATUS_0);
                    saleAreaSalary.setTargetSalaryByTime(Constants.PARAM_STATUS_0);
                } else {
                    List<SalaryTimeDTO> lstBusinessSalary = configSaleAreaSalaryDTO.getLstSalaryTimeDTOS().stream()
                            .filter(business -> Constants.STAFF_TYPE.BUSINESS.equals(business.getStaffType())).collect(Collectors.toList());
                    List<SalaryTimeDTO> lstLeaderSalary = configSaleAreaSalaryDTO.getLstSalaryTimeDTOS().stream()
                            .filter(business -> Constants.STAFF_TYPE.LEADER.equals(business.getStaffType())).collect(Collectors.toList());

                    if (!DataUtil.isNullOrEmpty(lstBusinessSalary))
                        saleAreaSalary.setTargetSalaryByTime(Constants.PARAM_STATUS);
                    else
                        saleAreaSalary.setTargetSalaryByTime(Constants.PARAM_STATUS_0);
                    if (!DataUtil.isNullOrEmpty(lstLeaderSalary))
                        saleAreaSalary.setHardSalaryByTime(Constants.PARAM_STATUS);
                    else
                        saleAreaSalary.setHardSalaryByTime(Constants.PARAM_STATUS_0);

                }

                saleAreaSalary.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                saleAreaSalary.setUpdatedUser(staffDTO.getStaffCode());

                saleAreaSalaryRepo.save(saleAreaSalary);

            }
            if (!DataUtil.isNullOrEmpty(configSaleAreaSalaryDTO.getLstSalaryTimeDTOS())) {
                //xoa salary time (chuyen status sang 0)
                List<SalaryTime> lstActiveSalaryTimes = salaryTimeRepo.findByStatus(Constants.PARAM_STATUS);
                List<Integer> idUpdate = configSaleAreaSalaryDTO.getLstSalaryTimeDTOS().stream()
                        .map(salaryTimeDTO -> salaryTimeDTO.getId()).collect(Collectors.toList());

                if (!DataUtil.isNullOrEmpty(lstActiveSalaryTimes)) {
                    for (SalaryTime salaryTime : lstActiveSalaryTimes) {
                        if (!DataUtil.isNullOrZero(salaryTime.getId())) {
                            if (!idUpdate.contains(salaryTime.getId())) {
                                salaryTime.setStatus(Constants.PARAM_STATUS_0);
                                salaryTimeRepo.save(salaryTime);
                            }
                        }
                    }
                }
                for (SalaryTimeDTO salaryTime : configSaleAreaSalaryDTO.getLstSalaryTimeDTOS()) {
                    List<String> vlstBusinessExpiredMonths = Lists.newArrayList();
                    List<String> vlstLeaderExpiredMonths=Lists.newArrayList();
                    if (!DataUtil.isNullOrZero(salaryTime.getExpiredMilis())) {
                        date = new Date(salaryTime.getExpiredMilis());
                        String vstrNewSalaryTime = dateFormat.format(date);
                        if (!DataUtil.isNullOrEmpty(vstrNewSalaryTime) && Constants.STAFF_TYPE.BUSINESS.equals(salaryTime.getStaffType()))
                            vlstBusinessExpiredMonths.add(vstrNewSalaryTime);
                        if (!DataUtil.isNullOrEmpty(vstrNewSalaryTime) && Constants.STAFF_TYPE.LEADER.equals(salaryTime.getStaffType()))
                            vlstLeaderExpiredMonths.add(vstrNewSalaryTime);
                    }
                    if (Constants.STAFF_TYPE.LEADER.equals(salaryTime.getStaffType())) {
                        if (checkDuplicateExpiredMonthHardSal(salaryTime)) {
                            message = I18N.get("duplicate.expired.month.hard.salary.time.error", (String[]) vlstLeaderExpiredMonths.toArray(new String[vlstLeaderExpiredMonths.size()]));
                            return message;
                        } else {
                            if (DataUtil.isNullOrZero(salaryTime.getId())) {
                                Integer areaSalaryId = saleAreaSalaryRepo.findByAreaCode(salaryTime.getAreaCode()).getId();
                                SalaryTime salaryTimeAdd = new SalaryTime(areaSalaryId, salaryTime.getSalary(), date, Constants.STAFF_TYPE.LEADER, new Timestamp(System.currentTimeMillis()),
                                        staffDTO.getStaffCode(), null, null, Constants.PARAM_STATUS);
                                salaryTimeRepo.save(salaryTimeAdd);
                                saveActionAuditSalTime(salaryTimeAdd, staffDTO);
                            } else {
                                SalaryTime salaryTimeUpdate = salaryTimeRepo.getOne(salaryTime.getId());
                            //    saveActionDetailSalTime(salaryTimeUpdate, salaryTime, staffDTO);

                                salaryTimeUpdate.setSalary(salaryTime.getSalary());
                                salaryTimeUpdate.setExpiredMonth(date);
                                salaryTimeUpdate.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                                salaryTimeUpdate.setUpdatedUser(staffDTO.getStaffCode());

                                salaryTimeRepo.save(salaryTimeUpdate);
                            }

                        }
                    } else if (Constants.STAFF_TYPE.BUSINESS.equals(salaryTime.getStaffType())) {
                        if (checkDuplicateExpiredMonthTargetSal(salaryTime)) {
                            message = I18N.get("duplicate.expired.month.target.salary.time.error", (String[]) vlstBusinessExpiredMonths.toArray(new String[vlstBusinessExpiredMonths.size()]));
                            return message;
                        }
                        if (DataUtil.isNullOrZero(salaryTime.getId())) {
                            Integer areaSalaryId = saleAreaSalaryRepo.findByAreaCode(salaryTime.getAreaCode()).getId();
                            SalaryTime salaryTimeAdd = new SalaryTime(areaSalaryId, salaryTime.getSalary(), date, Constants.STAFF_TYPE.BUSINESS, new Timestamp(System.currentTimeMillis()),
                                    staffDTO.getStaffCode(), null, null, Constants.PARAM_STATUS);
                            salaryTimeRepo.save(salaryTimeAdd);
                            saveActionAuditSalTime(salaryTimeAdd, staffDTO);
                        } else {
                            SalaryTime salaryTimeUpdate = salaryTimeRepo.getOne(salaryTime.getId());
                            //saveActionDetailSalTime(salaryTimeUpdate, salaryTime, staffDTO);

                            salaryTimeUpdate.setSalary(salaryTime.getSalary());
                            salaryTimeUpdate.setExpiredMonth(date);
                            salaryTimeUpdate.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                            salaryTimeUpdate.setUpdatedUser(staffDTO.getStaffCode());

                            salaryTimeRepo.save(salaryTimeUpdate);
                        }

                    }

                }
            }

        }

        return message;

    }

    @Override
    public void deleteSalaryTime(SalaryTimeDTO salaryTimeDTO) throws Exception {
        salaryTimeRepo.delete(mapper.toDtoBean(salaryTimeDTO));
    }

    private boolean checkDuplicateExpiredMonthHardSal(SalaryTimeDTO newSalaryTime) {
        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        String vstrOldSalary = null;
        boolean check = false;
        Date newDate = null;
        String vstrNewSalaryTime = null;
        List<SalaryTime> vlstNewSalaryTimes = null;
        if (!DataUtil.isNullOrZero(newSalaryTime.getExpiredMilis())) {
            newDate = new Date(newSalaryTime.getExpiredMilis());
            vstrNewSalaryTime = dateFormat.format(newDate);
        }

        try {
            List<SalaryTime> vlstSalaryTimes = salaryTimeRepo.findByStaffTypeAndStatus(Constants.STAFF_TYPE.LEADER,Constants.PARAM_STATUS);
            if (!DataUtil.isNullOrEmpty(vlstSalaryTimes))
                vlstNewSalaryTimes = vlstSalaryTimes.stream().filter(salaryTime -> salaryTime.getId() != newSalaryTime.getId()).collect(Collectors.toList());
            if (!DataUtil.isNullOrEmpty(vlstNewSalaryTimes)) {
                for (SalaryTime salaryTime : vlstNewSalaryTimes) {
                    if (newSalaryTime.getId() == null || salaryTime.getId() != newSalaryTime.getId()) {
                        if (salaryTime.getExpiredMonth() != null) {
                            vstrOldSalary = dateFormat.format(salaryTime.getExpiredMonth());
                            if (DataUtil.safeEqual(vstrOldSalary, vstrNewSalaryTime)) {
                                check = true;
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return check;
    }

    private boolean checkDuplicateExpiredMonthTargetSal(SalaryTimeDTO newSalaryTime) {
        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        String vstrOldSalary = null;
        boolean check = false;
        Date newDate = null;
        String vstrNewSalaryTime = null;
        List<SalaryTime> vlstNewSalaryTimes = null;
        if (newSalaryTime.getExpiredMilis() != null) {
            newDate = new Date(newSalaryTime.getExpiredMilis());
            vstrNewSalaryTime = dateFormat.format(newDate);
        }

        try {
            List<SalaryTime> vlstSalaryTimes = salaryTimeRepo.findByStaffTypeAndStatus(Constants.STAFF_TYPE.BUSINESS,Constants.PARAM_STATUS);
            if (!DataUtil.isNullOrEmpty(vlstSalaryTimes))
                vlstNewSalaryTimes = vlstSalaryTimes.stream().filter(salaryTime -> salaryTime.getId() != newSalaryTime.getId()).collect(Collectors.toList());
            if (!DataUtil.isNullOrEmpty(vlstNewSalaryTimes)) {
                for (SalaryTime salaryTime : vlstNewSalaryTimes) {
                    if (newSalaryTime.getId() == null || salaryTime.getId() != newSalaryTime.getId()) {
                        if (salaryTime.getExpiredMonth() != null) {
                            vstrOldSalary = dateFormat.format(salaryTime.getExpiredMonth());
                            if (DataUtil.safeEqual(vstrOldSalary, vstrNewSalaryTime)) {
                                check = true;
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return check;
    }

    private void saveActionAuditSalArea(SaleAreaSalary saleAreaSalary, StaffDTO staffDTO) {
        ActionAudit actionAudit = actionAuditService.log(Constants.SALE_AREA_SALARY, Constants.CREATE, staffDTO.getStaffCode(), Long.valueOf(saleAreaSalary.getId()), staffDTO.getShopCode());
        actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.AREA_CODE, saleAreaSalary.getAreaCode(), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.HARD_SALARY, String.valueOf(saleAreaSalary.getHardSalary()), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.HARD_SALARY_BY_TIME, Constants.PARAM_STATUS, actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.TARGET_SALARY, String.valueOf(saleAreaSalary.getTargetSalary()), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.TARGET_SALARY_BY_TIME, Constants.PARAM_STATUS, actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.CREATED_DATE, String.valueOf(saleAreaSalary.getCreatedDate()), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.CREATED_USER, saleAreaSalary.getCreatedUser(), actionAudit.getId(), null);
    }

    private void saveActionDetailSalArea(SaleAreaSalary saleAreaSalary, ConfigSaleAreaSalaryDTO configSaleAreaSalaryDTO, StaffDTO staffDTO) {
        ActionAudit actionAudit = actionAuditService.log(Constants.SALE_AREA_SALARY, Constants.EDIT, staffDTO.getStaffCode(), Long.valueOf(saleAreaSalary.getId()), staffDTO.getShopCode());
        if (!saleAreaSalary.getHardSalary().equals(configSaleAreaSalaryDTO.getHardSalary()))
            actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.HARD_SALARY, String.valueOf(configSaleAreaSalaryDTO.getHardSalary()), actionAudit.getId(), String.valueOf(saleAreaSalary.getHardSalary()));
        if (!saleAreaSalary.getTargetSalary().equals(configSaleAreaSalaryDTO.getTargetSalary()))
            actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.TARGET_SALARY, String.valueOf(saleAreaSalary.getTargetSalary()), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.UPDATED_DATE, String.valueOf(new Timestamp(System.currentTimeMillis())), actionAudit.getId(), String.valueOf(saleAreaSalary.getUpdatedDate()));
        if(!DataUtil.isNullOrEmpty(saleAreaSalary.getUpdatedUser())) {
            if (!saleAreaSalary.getUpdatedUser().equals(staffDTO.getStaffCode()))
                actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.UPDATED_USER, staffDTO.getStaffCode(), actionAudit.getId(), saleAreaSalary.getUpdatedUser());
        }
        else{
            actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.UPDATED_USER, staffDTO.getStaffCode(), actionAudit.getId(), null);
        }
    }

    private void saveActionAuditSalTime(SalaryTime salaryTime, StaffDTO staffDTO) {
        ActionAudit actionAudit = actionAuditService.log(Constants.SALARY_TIME, Constants.CREATE, staffDTO.getStaffCode(), Long.valueOf(salaryTime.getId()), staffDTO.getShopCode());
        actionDetailService.createActionDetail(Constants.SALARY_TIMES.AREA_SALARY_ID, String.valueOf(salaryTime.getAreaSalaryId()), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALARY_TIMES.SALARY, String.valueOf(salaryTime.getSalary()), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALARY_TIMES.EXPIRED_MONTH, String.valueOf(salaryTime.getExpiredMonth()), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALARY_TIMES.STAFF_TYPE, String.valueOf(salaryTime.getStaffType()), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALARY_TIMES.CREATED_DATE, String.valueOf(salaryTime.getCreatedDate()), actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALARY_TIMES.CREATED_USER, salaryTime.getCreatedUser(), actionAudit.getId(), null);

    }

    private void saveActionDetailSalTime(SalaryTime salaryTime, SalaryTimeDTO salaryTimeDTO, StaffDTO staffDTO) {
        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        String newDate = null;
        String oldDate = null;
        ActionAudit actionAudit = actionAuditService.log(Constants.SALARY_TIME, Constants.EDIT, staffDTO.getStaffCode(), Long.valueOf(salaryTime.getId()), staffDTO.getShopCode());
        if (!salaryTime.getSalary().equals(salaryTimeDTO.getSalary()))
            actionDetailService.createActionDetail(Constants.SALARY_TIMES.SALARY, String.valueOf(salaryTimeDTO.getSalary()), actionAudit.getId(), String.valueOf(salaryTime.getSalary()));
        if (!DataUtil.isNullOrZero(salaryTimeDTO.getExpiredMilis())) {
            date = new Date(salaryTimeDTO.getExpiredMilis());
            newDate = dateFormat.format(date);
        }
        if (salaryTime.getExpiredMonth() != null) {
            oldDate = dateFormat.format(salaryTime.getExpiredMonth());
        }

        if (oldDate != null && newDate != null) {
            if (!oldDate.equals(newDate))
                actionDetailService.createActionDetail(Constants.SALARY_TIMES.EXPIRED_MONTH, String.valueOf(date), actionAudit.getId(), String.valueOf(salaryTime.getExpiredMonth()));
        }
        if (salaryTime.getExpiredMonth() != null && salaryTimeDTO.getExpiredMilis() == null)
            actionDetailService.createActionDetail(Constants.SALARY_TIMES.EXPIRED_MONTH, null, actionAudit.getId(), String.valueOf(salaryTime.getExpiredMonth()));
        else if (salaryTime.getExpiredMonth() == null && salaryTimeDTO.getExpiredMilis() != null)
            actionDetailService.createActionDetail(Constants.SALARY_TIMES.EXPIRED_MONTH, newDate, actionAudit.getId(), null);
        actionDetailService.createActionDetail(Constants.SALARY_TIMES.UPDATED_DATE, String.valueOf(new Timestamp(System.currentTimeMillis())), actionAudit.getId(), String.valueOf(salaryTime.getUpdatedDate()));
        if(!DataUtil.isNullOrEmpty(salaryTime.getUpdatedUser())){
            if (!salaryTime.getUpdatedUser().equals(staffDTO.getStaffCode()))
                actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.UPDATED_USER, staffDTO.getStaffCode(), actionAudit.getId(), salaryTime.getUpdatedUser());
        }
        else{
            actionDetailService.createActionDetail(Constants.SALE_AREA_SALARIES.UPDATED_USER, staffDTO.getStaffCode(), actionAudit.getId(), null);
        }
    }
}
