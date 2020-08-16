package vn.vissoft.dashboard.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "sal_config_sale_fee")
public class SalConfigSaleFee {

    private Long id;
    private Long feeId;
    private String receiveFrom;
    private Double receivePercent;
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

    @Column(name = "fee_id")
    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }

    @Column(name = "receive_from")
    public String getReceiveFrom() {
        return receiveFrom;
    }

    public void setReceiveFrom(String receiveFrom) {
        this.receiveFrom = receiveFrom;
    }

    @Column(name = "receive_percent")
    public Double getReceivePercent() {
        return receivePercent;
    }

    public void setReceivePercent(Double receivePercent) {
        this.receivePercent = receivePercent;
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
