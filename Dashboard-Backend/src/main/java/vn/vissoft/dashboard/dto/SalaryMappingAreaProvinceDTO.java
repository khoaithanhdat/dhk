package vn.vissoft.dashboard.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * )
 *
 * @param SalaryMappingAreaProvince
 * @return
 * @throws Exception
 * @author Phucnv
 * @since 2020
 */
public class SalaryMappingAreaProvinceDTO {
    private Integer   id;
    private String areaCode;
    private String shopCode; //ma don vi cap tinh ||
    private String vdsChannelCode;//ma kenh
    private Date createdDate;
    private String createdUser;
    private Date updatedDate ;
    private String updatedUser ;
    private String status;

    private String shortName;//nguon
    private String areaName;//ten vung
    private String shopName;//ten vt tinh
    private Timestamp expiredDate;// ngay het han
    private String  souceName;// ten nguon
    private List<ManageInfoPartnerDTO> lstPartner;
    //load du lieu tu fontend
    private String code; //ma vt tinh
    private String parent;
    private Date effectiveDate;
    private String name; // ten tim kiem vt tinh
    private Timestamp expiredDateNew;
    private List<VdsStaffDTO> lstVdsStaffDTO;

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

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getVdsChannelCode() {
        return vdsChannelCode;
    }

    public void setVdsChannelCode(String vdsChannelCode) {
        this.vdsChannelCode = vdsChannelCode;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Timestamp getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Timestamp expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getSouceName() {
        return souceName;
    }

    public void setSouceName(String souceName) {
        this.souceName = souceName;
    }

    public List<ManageInfoPartnerDTO> getLstPartner() {
        return lstPartner;
    }

    public void setLstPartner(List<ManageInfoPartnerDTO> lstPartner) {
        this.lstPartner = lstPartner;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getExpiredDateNew() {
        return expiredDateNew;
    }

    public void setExpiredDateNew(Timestamp expiredDateNew) {
        this.expiredDateNew = expiredDateNew;
    }

    public List<VdsStaffDTO> getLstVdsStaffDTO() {
        return lstVdsStaffDTO;
    }

    public void setLstVdsStaffDTO(List<VdsStaffDTO> lstVdsStaffDTO) {
        this.lstVdsStaffDTO = lstVdsStaffDTO;
    }
}
