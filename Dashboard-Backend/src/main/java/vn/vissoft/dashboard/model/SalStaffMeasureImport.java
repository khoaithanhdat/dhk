package vn.vissoft.dashboard.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sal_staff_measure_import")
public class SalStaffMeasureImport {
    private Integer id;
    private Integer prdId;
    private String vdsChannelCode;
    private String shopCode;
    private String staffCode;
    private Integer serviceId;
    private Double monthyImportValue;//so chot thang
    private Double excludeValue;//so loai tru
    private String createdUser;
    private Timestamp createdDate;

    public SalStaffMeasureImport() {
    }

    public SalStaffMeasureImport(Integer prdId, String vdsChannelCode, String shopCode, String staffCode, Integer serviceId, Double monthyImportValue, Double excludeValue, String createdUser, Timestamp createdDate) {
        this.prdId = prdId;
        this.vdsChannelCode = vdsChannelCode;
        this.shopCode = shopCode;
        this.staffCode = staffCode;
        this.serviceId = serviceId;
        this.monthyImportValue = monthyImportValue;
        this.excludeValue = excludeValue;
        this.createdUser = createdUser;
        this.createdDate = createdDate;
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

    @Column(name = "prd_id")
    public Integer getPrdId() {
        return prdId;
    }

    public void setPrdId(Integer prdId) {
        this.prdId = prdId;
    }

    @Column(name = "vds_channel_code")
    public String getVdsChannelCode() {
        return vdsChannelCode;
    }

    public void setVdsChannelCode(String vdsChannelCode) {
        this.vdsChannelCode = vdsChannelCode;
    }

    @Column(name = "shop_code")
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    @Column(name = "staff_code")
    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    @Column(name = "service_id")
    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    @Column(name = "monthly_import_value")
    public Double getMonthyImportValue() {
        return monthyImportValue;
    }

    public void setMonthyImportValue(Double monthyImportValue) {
        this.monthyImportValue = monthyImportValue;
    }

    @Column(name = "exclude_value")
    public Double getExcludeValue() {
        return excludeValue;
    }

    public void setExcludeValue(Double excludeValue) {
        this.excludeValue = excludeValue;
    }

    @Column(name = "created_user")
    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    @Column(name = "created_date")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
