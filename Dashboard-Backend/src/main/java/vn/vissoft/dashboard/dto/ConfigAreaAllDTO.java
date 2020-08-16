package vn.vissoft.dashboard.dto;

import java.sql.Timestamp;

/**
 * )
 *
 * @param ConfigAreaAllDTO
 * @return
 * @throws Exception
 * @author Phucnv
 * @since 2020
 */
public class ConfigAreaAllDTO {
    private String  provinceCode;// ma tinh
    private String  provinceName;// ten tinh
    private String  souceName;// ten nguon
    private String area;// vung
    private Timestamp expiredDate;// ngay het han

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getSouceName() {
        return souceName;
    }

    public void setSouceName(String souceName) {
        this.souceName = souceName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Timestamp getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Timestamp expiredDate) {
        this.expiredDate = expiredDate;
    }
}
