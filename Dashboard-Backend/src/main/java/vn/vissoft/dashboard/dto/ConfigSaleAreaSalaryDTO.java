package vn.vissoft.dashboard.dto;

import vn.vissoft.dashboard.model.SalaryTime;

import java.sql.Timestamp;
import java.util.List;

public class ConfigSaleAreaSalaryDTO {
    private Integer id;
    private String areaCode;
    private Double hardSalary;
    private String hardSalaryByTime;
    private Double targetSalary;
    private String targetSalaryByTime;
    private Timestamp createdDate;
    private String createdUser;
    private Timestamp updatedDate;
    private String updatedUser;
    private List<SalaryTimeDTO> lstSalaryTimeDTOS ;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Double getHardSalary() {
        return hardSalary;
    }

    public void setHardSalary(Double hardSalary) {
        this.hardSalary = hardSalary;
    }

    public String getHardSalaryByTime() {
        return hardSalaryByTime;
    }

    public void setHardSalaryByTime(String hardSalaryByTime) {
        this.hardSalaryByTime = hardSalaryByTime;
    }

    public Double getTargetSalary() {
        return targetSalary;
    }

    public void setTargetSalary(Double targetSalary) {
        this.targetSalary = targetSalary;
    }

    public String getTargetSalaryByTime() {
        return targetSalaryByTime;
    }

    public void setTargetSalaryByTime(String targetSalaryByTime) {
        this.targetSalaryByTime = targetSalaryByTime;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public List<SalaryTimeDTO> getLstSalaryTimeDTOS() {
        return lstSalaryTimeDTOS;
    }

    public void setLstSalaryTimeDTOS(List<SalaryTimeDTO> lstSalaryTimeDTOS) {
        this.lstSalaryTimeDTOS = lstSalaryTimeDTOS;
    }

}
