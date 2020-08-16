package vn.vissoft.dashboard.dto.excel;

import vn.vissoft.dashboard.helper.excelreader.annotation.ExcelColumn;
import vn.vissoft.dashboard.helper.excelreader.annotation.ExcelEntity;
import vn.vissoft.dashboard.helper.excelreader.annotation.IndexColumn;

@ExcelEntity(dataStartRowIndex = 6, signalConstant = "IMPORT_COLLABORATOR")
public class CollaboratorServiceExel extends BaseExcelEntity{

    private String code;
    private String name;
    private int index;

    @ExcelColumn(name = "A")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ExcelColumn(name = "B")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @IndexColumn
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


}
