package vn.vissoft.dashboard.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "sale_area_salary")
public class SaleAreaSalary {

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

    public SaleAreaSalary() {
    }

    public SaleAreaSalary(String areaCode, Double hardSalary, String hardSalaryByTime, Double targetSalary, String targetSalaryByTime, Timestamp createdDate, String createdUser) {
        this.areaCode = areaCode;
        this.hardSalary = hardSalary;
        this.hardSalaryByTime = hardSalaryByTime;
        this.targetSalary = targetSalary;
        this.targetSalaryByTime = targetSalaryByTime;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "area_code")
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Column(name = "df_hard_salary")
    public Double getHardSalary() {
        return hardSalary;
    }

    public void setHardSalary(Double hardSalary) {
        this.hardSalary = hardSalary;
    }

    @Column(name = "Is_hard_salary_by_time")
    public String getHardSalaryByTime() {
        return hardSalaryByTime;
    }

    public void setHardSalaryByTime(String hardSalaryByTime) {
        this.hardSalaryByTime = hardSalaryByTime;
    }

    @Column(name = "df_target_salary")
    public Double getTargetSalary() {
        return targetSalary;
    }

    public void setTargetSalary(Double targetSalary) {
        this.targetSalary = targetSalary;
    }

    @Column(name = "Is_target_salary_by_time")
    public String getTargetSalaryByTime() {
        return targetSalaryByTime;
    }

    public void setTargetSalaryByTime(String targetSalaryByTime) {
        this.targetSalaryByTime = targetSalaryByTime;
    }

    @Column(name = "created_date")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "created_user")
    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    @Column(name = "updated_date")
    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Column(name = "updated_user")
    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }
}
