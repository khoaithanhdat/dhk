package vn.vissoft.dashboard.dto;

import java.util.Date;

public class SalConfigHsTinhLuongDTO {

    private Long id;
    private String staffType;
    private String comparison;
    private Double comparisonValueFrom;
    private Double comparisonValueTo;
    private String valueType;
    private Double value;
    private String formula;
    private Date expiredMonth;
    private Date createdDate;
    private String createdUser;
    private Date updatedDate;
    private String updatedUser;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

    public Double getComparisonValueFrom() {
        return comparisonValueFrom;
    }

    public void setComparisonValueFrom(Double comparisonValueFrom) {
        this.comparisonValueFrom = comparisonValueFrom;
    }

    public Double getComparisonValueTo() {
        return comparisonValueTo;
    }

    public void setComparisonValueTo(Double comparisonValueTo) {
        this.comparisonValueTo = comparisonValueTo;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Date getExpiredMonth() {
        return expiredMonth;
    }

    public void setExpiredMonth(Date expiredMonth) {
        this.expiredMonth = expiredMonth;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
