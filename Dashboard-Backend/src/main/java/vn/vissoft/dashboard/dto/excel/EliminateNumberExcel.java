package vn.vissoft.dashboard.dto.excel;

import vn.vissoft.dashboard.helper.excelreader.annotation.ExcelColumn;
import vn.vissoft.dashboard.helper.excelreader.annotation.ExcelEntity;
import vn.vissoft.dashboard.helper.excelreader.annotation.IndexColumn;


@ExcelEntity(dataStartRowIndex = 6, signalConstant = "IMPORT_ELIMINATE_NUMBER")
public class EliminateNumberExcel extends BaseExcelEntity {

    private String shopCode;
    private String staffCode;
    private String serviceCode;
    private String serviceName;
    private Double monthBoltNumber;
    private Double eliminateNumber;
    private int index;

    @ExcelColumn(name = "B",maxLength = 10)
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    @ExcelColumn(name = "C",maxLength = 20)
    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    @ExcelColumn(name = "D")
    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @ExcelColumn(name = "E",nullable = true)
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @ExcelColumn(name = "F",regex = "^[+]?(([0-9]\\d*))(\\.\\d+)?$")
    public Double getMonthBoltNumber() {
        return monthBoltNumber;
    }

    public void setMonthBoltNumber(Double monthBoltNumber) {
        this.monthBoltNumber = monthBoltNumber;
    }

    @ExcelColumn(name = "G",regex = "^[+]?(([0-9]\\d*))(\\.\\d+)?$",nullable = true)
    public Double getEliminateNumber() {
        return eliminateNumber;
    }

    public void setEliminateNumber(Double eliminateNumber) {
        this.eliminateNumber = eliminateNumber;
    }

    @IndexColumn
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
