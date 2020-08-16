package vn.vissoft.dashboard.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "salary_time")
public class SalaryTime {

    private Integer id;
    private Integer areaSalaryId;
    private Double salary;
    private Date expiredMonth;
    private String staffType;
    private Timestamp createdDate;
    private String createdUser;
    private Timestamp updatedDate;
    private String updatedUser;
    private String status;

    public SalaryTime() {
    }

    public SalaryTime(Integer areaSalaryId, Double salary, Date expiredMonth, String staffType, Timestamp createdDate, String createdUser, Timestamp updatedDate, String updatedUser, String status) {
        this.areaSalaryId = areaSalaryId;
        this.salary = salary;
        this.expiredMonth = expiredMonth;
        this.staffType = staffType;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
        this.updatedDate = updatedDate;
        this.updatedUser = updatedUser;
        this.status = status;
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

    @Column(name = "sal_area_salary_id")
    public Integer getAreaSalaryId() {
        return areaSalaryId;
    }

    public void setAreaSalaryId(Integer areaSalaryId) {
        this.areaSalaryId = areaSalaryId;
    }

    @Column(name = "salary")
    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Column(name = "expired_month")
    public Date getExpiredMonth() {
        return expiredMonth;
    }

    public void setExpiredMonth(Date expiredMonth) {
        this.expiredMonth = expiredMonth;
    }

    @Column(name = "staff_type")
    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
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

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
