package vn.vissoft.dashboard.dto.excel;

import vn.vissoft.dashboard.helper.excelreader.annotation.ExcelColumn;
import vn.vissoft.dashboard.helper.excelreader.annotation.ExcelEntity;
import vn.vissoft.dashboard.helper.excelreader.annotation.IndexColumn;

import java.util.Objects;

@ExcelEntity(dataStartRowIndex = 6, signalConstant = "IMPORT_KPI_ACTION_PROGRAM")
public class KpiActionProgramExcel extends BaseExcelEntity {

    private String shopCode;
    private String staffCode;
    private Double schedule;
    private Double perform;
    private Double density;
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

    @ExcelColumn(name = "D",regex = "^[+]?(([0-9]\\d*))(\\.\\d+)?$")
    public Double getSchedule() {
        return schedule;
    }

    public void setSchedule(Double schedule) {
        this.schedule = schedule;
    }

    @ExcelColumn(name = "E",regex = "^[+]?(([0-9]\\d*))(\\.\\d+)?$")
    public Double getPerform() {
        return perform;
    }

    public void setPerform(Double perform) {
        this.perform = perform;
    }

    @ExcelColumn(name = "F",maxLength = 5,regex = "^[+]?(([0-9]\\d*))(\\.\\d+)?$")
    public Double getDensity() {
        return density;
    }

    public void setDensity(Double density) {
        this.density = density;
    }

    @IndexColumn
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
