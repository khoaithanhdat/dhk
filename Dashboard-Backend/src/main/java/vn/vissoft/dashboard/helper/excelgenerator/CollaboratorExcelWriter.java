package vn.vissoft.dashboard.helper.excelgenerator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.vissoft.dashboard.config.I18N;
import vn.vissoft.dashboard.helper.excelreader.annotation.ExcelEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.util.Map;

@Component
public class CollaboratorExcelWriter {

    @Autowired
    private VTTExcelWriter vttExcelWriter;

    public ByteArrayInputStream write(Class pclsClass) throws Exception{
        String vstrSignal;
        int vintStartRowIndex;
        String[] columns = new String[]{"Ma CTV","Ten CTV"};
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if(pclsClass.isAnnotationPresent(ExcelEntity.class)){
            Annotation ann1 = pclsClass.getAnnotation(ExcelEntity.class);
            ExcelEntity entityAnn = (ExcelEntity)ann1;

            vstrSignal = entityAnn.signalConstant();
            vintStartRowIndex = ((ExcelEntity) ann1).dataStartRowIndex();
        }else{
            throw new Exception("You must annotate class with @ExcelEnity");
        }

        vttExcelWriter.determineColRange(pclsClass);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        Map<String, CellStyle> styles = vttExcelWriter.createStyles(workbook);

        Row h = sheet.createRow(0);
        Cell c = h.createCell(0);
        c.setCellValue(vstrSignal);

        Row tblName = sheet.createRow(vintStartRowIndex -4);
        Cell tblCell = tblName.createCell(0);
        tblCell.setCellValue("DANH SACH CONG TAC VIEN");
        tblCell.setCellStyle(styles.get("tblName"));

        Row headerRow = sheet.createRow(vintStartRowIndex - 2);
        sheet.addMergedRegion(new CellRangeAddress(vintStartRowIndex - 4, vintStartRowIndex - 4, 0, columns.length - 1));
        Row blankRow = sheet.createRow(vintStartRowIndex - 1);

        //header
        for (int col = 0; col < columns.length; col++) {
            sheet.setColumnWidth(0, 2000);
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col].toUpperCase());
            cell.setCellStyle(styles.get("header"));
            blankRow.createCell(col).setCellValue("");
            blankRow.getCell(col).setCellStyle(styles.get("content"));
            sheet.autoSizeColumn(col);
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}
