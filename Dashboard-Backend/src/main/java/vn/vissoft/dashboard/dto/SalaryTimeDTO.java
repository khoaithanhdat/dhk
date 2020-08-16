package vn.vissoft.dashboard.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class SalaryTimeDTO {

    private Integer id;
    private Integer areaSalaryId;
    private Double salary;
    private Date expiredMonth;
    private String staffType;
    private Timestamp createdDate;
    private  String createdUser;
    private Timestamp updatedDate;
    private  String updatedUser;
    private Long expiredMilis;
    private String areaCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaSalaryId() {
        return areaSalaryId;
    }

    public void setAreaSalaryId(Integer areaSalaryId) {
        this.areaSalaryId = areaSalaryId;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getExpiredMonth() {
        return expiredMonth;
    }

    public void setExpiredMonth(Date expiredMonth) {
        this.expiredMonth = expiredMonth;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
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

    public Long getExpiredMilis() {
        return expiredMilis;
    }

    public void setExpiredMilis(Long expiredMilis) {
        this.expiredMilis = expiredMilis;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
