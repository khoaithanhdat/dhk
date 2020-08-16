package vn.vissoft.dashboard.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "sal_config_staff_giff")
public class SalConfigStaffGiff {

    private Long id;
    private String comparison;
    private Double comparisonValueFrom;
    private Double comparisonValueTo;
    private Double value;
    private Date expiredMonth;
    private Date createdDate;
    private String createdUser;
    private Date updatedDate;
    private String updatedUser;
    private String status;

    @Column(name = "id")
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "comparison")
    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

    @Column(name = "comparison_value_from")
    public Double getComparisonValueFrom() {
        return comparisonValueFrom;
    }

    public void setComparisonValueFrom(Double comparisonValueFrom) {
        this.comparisonValueFrom = comparisonValueFrom;
    }

    @Column(name = "comparison_value_to")
    public Double getComparisonValueTo() {
        return comparisonValueTo;
    }

    public void setComparisonValueTo(Double comparisonValueTo) {
        this.comparisonValueTo = comparisonValueTo;
    }

    @Column(name = "value")
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Column(name = "expired_month")
    public Date getExpiredMonth() {
        return expiredMonth;
    }

    public void setExpiredMonth(Date expiredMonth) {
        this.expiredMonth = expiredMonth;
    }

    @Column(name = "created_date")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
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
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Column(name = "udpated_user")
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
