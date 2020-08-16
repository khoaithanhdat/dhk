package vn.vissoft.dashboard.helper.excelgenerator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import vn.vissoft.dashboard.dto.*;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.helper.excelreader.annotation.ExcelColumn;
import vn.vissoft.dashboard.helper.excelreader.annotation.ExcelEntity;
import vn.vissoft.dashboard.repo.PartnerRepo;
import vn.vissoft.dashboard.services.ReportSqlService;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TemplateReportExcelWriter {
    private String mstrStartCol;
    private String mstrEndCol;

    public static String PROVINCE_UNIT_SUM = "BAO_CAO_TONG_HOP_DON_VI_TINH_TOAN_QUOC";
    public static String STAFF_UNIT_SUM = "BAO_CAO_TONG_HOP_DON_VI_NHAN_VIEN_TOAN_QUOC";

    public ByteArrayInputStream write(Class pclsClazz,
                                      SqlReportTemplateDTO sqlReportTemplateDTO, ResultsProvincialDTO resultsProvincialDTO) throws Exception {
        int vintStartRowIndex;

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (pclsClazz.isAnnotationPresent(ExcelEntity.class)) {
            Annotation ann1 = pclsClazz.getAnnotation(ExcelEntity.class);
            ExcelEntity entityAnn = (ExcelEntity) ann1;
            entityAnn.signalConstant();
            vintStartRowIndex = ((ExcelEntity) ann1).dataStartRowIndex();

        } else {
            throw new Exception("You must annotate class with @ExcelEntity");
        }

        determineColRange(pclsClazz);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream resource = classLoader.getResourceAsStream("templates/excel/"+sqlReportTemplateDTO.getType()+".xlsx");

        File file = new File("demo1.txt");
        file.createNewFile();
        copyInputStreamToFile(resource,file);
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        if(sqlReportTemplateDTO.getType().equals(PROVINCE_UNIT_SUM))
        {
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<BusinessResultProvincialDTO> resultsProvincials= resultsProvincialDTO.getResultsProvincial();
            if(resultsProvincials!=null && resultsProvincials.size()>0) {
                //title
                XSSFRow titleRow = sheet.createRow(0);
                XSSFCell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("BẢNG CHẤM ĐIỂM KẾT QUẢ KINH DOANH VIETTELPAY TỈNH/THÀNH PHỐ THÁNG " + sqlReportTemplateDTO.getMonth().substring(0,2) + " NĂM " + sqlReportTemplateDTO.getMonth().substring(2,sqlReportTemplateDTO.getMonth().length()));
                CellStyle titleCellStyle = workbook.createCellStyle();
                titleCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                titleCellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                titleCellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                Font titleFont = workbook.createFont();
                titleFont.setColor(HSSFColor.BLACK.index);
                titleFont.setFontHeightInPoints((short) 15);
                titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                titleFont.setFontName("Times New Roman");
                titleCellStyle.setFont(titleFont);
                titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                titleCell.setCellStyle(titleCellStyle);

                //sub title
                XSSFRow subTitleRow = sheet.getRow(2);
                CellStyle subTitleCellStyle = workbook.createCellStyle();
                subTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                subTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                subTitleCellStyle.setBorderTop(CellStyle.BORDER_THIN);
                subTitleCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                subTitleCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                subTitleCellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                subTitleCellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
                Font subTitleFont = workbook.createFont();
                subTitleFont.setColor(HSSFColor.BLACK.index);
                subTitleFont.setFontHeightInPoints((short) 12);
                subTitleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                subTitleFont.setFontName("Times New Roman");
                subTitleCellStyle.setFont(subTitleFont);
                subTitleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                XSSFCell subTitleCell = subTitleRow.createCell(5);
                String scoreTbtt = resultsProvincialDTO.getScoreTbtt()==null?"N/A":resultsProvincialDTO.getScoreTbtt().toString();
                subTitleCell.setCellValue("THUÊ BAO TĂNG THÊM ("+scoreTbtt+" điểm)");
                subTitleCell.setCellStyle(subTitleCellStyle);
                XSSFCell subTitleCell2 = subTitleRow.createCell(11);
                String scoreTbm = resultsProvincialDTO.getScoreTbm()==null?"N/A":resultsProvincialDTO.getScoreTbm().toString();
                subTitleCell2.setCellValue("THUÊ BAO MỚI ("+scoreTbm+" điểm)");
                subTitleCell2.setCellStyle(subTitleCellStyle);
                XSSFCell subTitleCell3 = subTitleRow.createCell(15);
                String scoreTbrm = resultsProvincialDTO.getScoreTbrm()==null?"N/A":resultsProvincialDTO.getScoreTbrm().toString();
                subTitleCell3.setCellValue("THUÊ BAO RỜI MẠNG ("+scoreTbrm+" điểm)");
                subTitleCell3.setCellStyle(subTitleCellStyle);


                CellStyle logCellStyle = workbook.createCellStyle();
                logCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                logCellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                logCellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                Font logFont = workbook.createFont();
                logFont.setColor(HSSFColor.BLACK.index);
                logFont.setFontHeightInPoints((short) 11);
                logFont.setFontName("Times New Roman");
                logFont.setItalic(true);
                logCellStyle.setFont(logFont);
                logCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                XSSFRow logRow = sheet.createRow(1);
                XSSFCell logUserCell = logRow.createCell(10);
                logUserCell.setCellValue("Người kết xuất: "+sqlReportTemplateDTO.getUser());
                logUserCell.setCellStyle(logCellStyle);
                XSSFCell logDateCell = logRow.createCell(13);
                logDateCell.setCellValue("Ngày kết xuất: "+new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                logDateCell.setCellStyle(logCellStyle);

                //rows
                int beginIndex = 5;

                Double sumAddedScheduleMonth = 0d;
                Double sumAddedPerformAccumulatedN1 = 0d;
                Double sumAddedPerformAccumulatedN = 0d;
                Double sumAddedDelta = 0d;

                Double sumNewScheduleMonth = 0d;
                Double sumNewPerformAccumulatedN = 0d;

                Double sumLeaveScheduleMonth = 0d;
                Double sumLeavePerformAccumulatedN = 0d;
                for (int i = 0; i < resultsProvincials.size(); i++) {
                    BusinessResultProvincialDTO resultsProvincial = resultsProvincials.get(i);
                    XSSFRow row = sheet.createRow(beginIndex + i);
                    int columnIndex = 0;
                    row.createCell(columnIndex).setCellValue(i + 1);
                    columnIndex++;
                    row.createCell(columnIndex).setCellValue(resultsProvincial.getProvincialCode() == null ? "" : resultsProvincial.getProvincialCode());
                    columnIndex++;
                    row.createCell(columnIndex).setCellValue(resultsProvincial.getProvincialCode() == null ? "" : resultsProvincial.getProvincialCode());
                    columnIndex++;
                    this.addCellWithNull(row, columnIndex, resultsProvincial.getTotalScore());
                    columnIndex++;
                    this.addCellWithNull(row, columnIndex, resultsProvincial.getRank());
                    columnIndex++;

                    List<BusinessResultDetailDTO> details = resultsProvincial.getListDetail();
                    if (details.size() == 3) {
                        //added
                        BusinessResultDetailDTO addedSub = details.get(0);
                        Double addedScheduleMonth = addedSub.getScheduleMonth() == null ? 0d : addedSub.getScheduleMonth();
                        Double addedPerformAccumulatedN1 = addedSub.getPerformAccumulatedN1() == null ? 0d : addedSub.getPerformAccumulatedN1();
                        Double addedPerformAccumulatedN = addedSub.getPerformAccumulatedN() == null ? 0d : addedSub.getPerformAccumulatedN();
                        Double addedDelta = addedSub.getDelta() == null ? 0d : addedSub.getDelta();
                        sumAddedScheduleMonth += addedScheduleMonth;
                        sumAddedPerformAccumulatedN1 += addedPerformAccumulatedN1;
                        sumAddedPerformAccumulatedN += addedPerformAccumulatedN;
                        sumAddedDelta += addedDelta;
                        this.addCellWithNull(row, columnIndex, addedSub.getScheduleMonth());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getPerformAccumulatedN1());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getPerformAccumulatedN());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getDelta());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getComplete());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getScorePass());
                        columnIndex++;

                        //new
                        BusinessResultDetailDTO newSub = details.get(1);
                        Double newScheduleMonth = newSub.getScheduleMonth() == null ? 0d : newSub.getScheduleMonth();
                        Double newPerformAccumulatedN = newSub.getPerformAccumulatedN() == null ? 0d : newSub.getPerformAccumulatedN();
                        sumNewScheduleMonth += newScheduleMonth;
                        sumNewPerformAccumulatedN += newPerformAccumulatedN;
                        this.addCellWithNull(row, columnIndex, newSub.getScheduleMonth());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, newSub.getPerformAccumulatedN());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, newSub.getComplete());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, newSub.getScorePass());
                        columnIndex++;

                        //leave
                        BusinessResultDetailDTO leaveSub = details.get(2);
                        Double leaveScheduleMonth = leaveSub.getScheduleMonth() == null ? 0d : leaveSub.getScheduleMonth();
                        Double leavePerformAccumulatedN = leaveSub.getPerformAccumulatedN() == null ? 0L : leaveSub.getPerformAccumulatedN();
                        sumLeaveScheduleMonth += leaveScheduleMonth;
                        sumLeavePerformAccumulatedN += leavePerformAccumulatedN;
                        this.addCellWithNull(row, columnIndex, leaveSub.getScheduleMonth());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, leaveSub.getPerformAccumulatedN());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, leaveSub.getComplete());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, leaveSub.getScorePass());
                        columnIndex++;

                    }
                }

                CellRangeAddress region = new CellRangeAddress(beginIndex, beginIndex + resultsProvincials.size(), 0, 19);
                for (int i = region.getFirstRow(); i < region.getLastRow(); i++) {
                    Row row = sheet.getRow(i);
                    for (int j = region.getFirstColumn(); j < region.getLastColumn(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {

                            if (j == 3 || j == 4 || j == 10 || j == 14 || j == 18) {
                                CellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                                cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                                Font fontRed = workbook.createFont();
                                fontRed.setColor(HSSFColor.RED.index);
                                fontRed.setFontName("Times New Roman");
                                cellStyle.setFont(fontRed);
                                cell.setCellStyle(cellStyle);
                            } else if (j == 5 || j == 11 || j == 15) {
                                CellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                                cellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                                Font fontBlue = workbook.createFont();
                                fontBlue.setColor(HSSFColor.BLUE.index);
                                fontBlue.setFontName("Times New Roman");
                                cellStyle.setFont(fontBlue);
                                cell.setCellStyle(cellStyle);
                            } else if(j==9||j==13||j==17)
                            {
                                CellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setBorderLeft(CellStyle.BORDER_THIN   );
                                cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                                cellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                                cellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                                cellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
                                Font font = workbook.createFont();
                                font.setColor(HSSFColor.BLACK.index);
                                font.setFontName("Times New Roman");
                                cellStyle.setFont(font);
                                cell.setCellStyle(cellStyle);
                            }
                            else
                                {
                                CellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setBorderLeft(CellStyle.BORDER_THIN   );
                                cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                                cellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                                cellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                                Font font = workbook.createFont();
                                font.setColor(HSSFColor.BLACK.index);
                                font.setFontName("Times New Roman");
                                cellStyle.setFont(font);
                                cell.setCellStyle(cellStyle);
                            }
                        }
                    }
                }

                XSSFRow sumRow = sheet.createRow(beginIndex - 1);

                sumRow.createCell(0).setCellValue("Tổng");

                XSSFCell cellSumScheduleMonth = sumRow.createCell(5);
                cellSumScheduleMonth.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumScheduleMonth.setCellFormula("SUM(F6:F" + resultsProvincials.size()+ beginIndex + ")");

                XSSFCell cellSumAddedPerformAccumulatedN1 = sumRow.createCell(6);
                cellSumAddedPerformAccumulatedN1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumAddedPerformAccumulatedN1.setCellFormula("SUM(G6:G" + resultsProvincials.size()+ beginIndex + ")");

                XSSFCell cellSumAddedPerformAccumulatedN = sumRow.createCell(7);
                cellSumAddedPerformAccumulatedN.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumAddedPerformAccumulatedN.setCellFormula("SUM(H6:H" + resultsProvincials.size()+ beginIndex + ")");

                XSSFCell cellSumAddedDelta = sumRow.createCell(8);
                cellSumAddedDelta.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumAddedDelta.setCellFormula("SUM(I6:I" + resultsProvincials.size()+ beginIndex + ")");
                this.addCellWithNull(sumRow, 9, sumAddedDelta / sumAddedPerformAccumulatedN1);
                XSSFCell cell9 = sumRow.createCell(9);
                cell9.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell9.setCellFormula("I5/F5");

                XSSFCell cellSumNewScheduleMonth = sumRow.createCell(11);
                cellSumNewScheduleMonth.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumNewScheduleMonth.setCellFormula("SUM(L6:L" + resultsProvincials.size()+ beginIndex + ")");
                XSSFCell cellSumNewPerformAccumulatedN = sumRow.createCell(12);
                cellSumNewPerformAccumulatedN.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumNewPerformAccumulatedN.setCellFormula("SUM(M6:M" + resultsProvincials.size()+ beginIndex + ")");
                XSSFCell cell13 = sumRow.createCell(13);
                cell13.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell13.setCellFormula("M5/L5");


                XSSFCell cellSumLeaveScheduleMonth = sumRow.createCell(15);
                cellSumLeaveScheduleMonth.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumLeaveScheduleMonth.setCellFormula("AVERAGE(P6:P" + resultsProvincials.size()+ beginIndex + ")");

                XSSFCell cellSumLeavePerformAccumulatedN = sumRow.createCell(16);
                cellSumLeavePerformAccumulatedN.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumLeavePerformAccumulatedN.setCellFormula("AVERAGE(Q6:Q" + resultsProvincials.size()+ beginIndex + ")");
                XSSFCell cell17 = sumRow.createCell(17);
                cell17.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell17.setCellFormula("2-Q5/P5");

                CellRangeAddress sumRegion = new CellRangeAddress(beginIndex - 1, beginIndex, 0, 19);
                for (int i = sumRegion.getFirstRow(); i < sumRegion.getLastRow(); i++) {
                    Row row = sheet.getRow(i);
                    for (int j = sumRegion.getFirstColumn(); j < sumRegion.getLastColumn(); j++) {
                        Cell cell = row.getCell(j);

                        if (cell != null) {
                            if(j==9||j==13||j==17)
                            {
                                XSSFCellStyle style = workbook.createCellStyle();
                                style.setBorderLeft(CellStyle.BORDER_THIN);
                                style.setBorderRight(CellStyle.BORDER_THIN);
                                style.setBorderTop(CellStyle.BORDER_THIN);
                                style.setBorderBottom(CellStyle.BORDER_THIN);
                                XSSFFont font = workbook.createFont();
                                font.setFontHeightInPoints((short) 12);
                                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                                font.setFontName("Times New Roman");
                                style.setFont(font);
                                style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
                                cell.setCellStyle(style);
                            }
                            else
                            {
                                XSSFCellStyle style = workbook.createCellStyle();
                                style.setBorderLeft(CellStyle.BORDER_THIN);
                                style.setBorderRight(CellStyle.BORDER_THIN);
                                style.setBorderTop(CellStyle.BORDER_THIN);
                                style.setBorderBottom(CellStyle.BORDER_THIN);
                                XSSFFont font = workbook.createFont();
                                font.setFontHeightInPoints((short) 12);
                                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                                font.setFontName("Times New Roman");
                                style.setFont(font);
                                cell.setCellStyle(style);
                            }
                        }
                    }
                }
            }
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }



    public ByteArrayInputStream write(Class pclsClazz,
                                      SqlReportTemplateDTO sqlReportTemplateDTO, ResultsStaffDTO resultsStaffDTO) throws Exception {
        int vintStartRowIndex;

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (pclsClazz.isAnnotationPresent(ExcelEntity.class)) {
            Annotation ann1 = pclsClazz.getAnnotation(ExcelEntity.class);
            ExcelEntity entityAnn = (ExcelEntity) ann1;
            entityAnn.signalConstant();
            vintStartRowIndex = ((ExcelEntity) ann1).dataStartRowIndex();

        } else {
            throw new Exception("You must annotate class with @ExcelEntity");
        }

        determineColRange(pclsClazz);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream resource = classLoader.getResourceAsStream("templates/excel/"+sqlReportTemplateDTO.getType()+".xlsx");
        File file = new File("demo1.txt");
        file.createNewFile();
        copyInputStreamToFile(resource,file);
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        if(sqlReportTemplateDTO.getType().equals(STAFF_UNIT_SUM))
        {
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<BusinessResultStaffDTO> resultsStaffs= resultsStaffDTO.getResultsStaff();
            if(resultsStaffs!=null && resultsStaffs.size()>0) {
                //title
                XSSFRow titleRow = sheet.createRow(0);
                XSSFCell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("BẢNG CHẤM ĐIỂM KẾT QUẢ KINH DOANH NHÂN VIÊN VIETTELPAY TỈNH/THÀNH PHỐ THÁNG " + sqlReportTemplateDTO.getMonth().substring(0,2) + " NĂM " + sqlReportTemplateDTO.getMonth().substring(2,sqlReportTemplateDTO.getMonth().length()));
                CellStyle titleCellStyle = workbook.createCellStyle();
                titleCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                titleCellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                titleCellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                Font titleFont = workbook.createFont();
                titleFont.setColor(HSSFColor.BLACK.index);
                titleFont.setFontHeightInPoints((short) 15);
                titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                titleFont.setFontName("Times New Roman");
                titleCellStyle.setFont(titleFont);
                titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                titleCell.setCellStyle(titleCellStyle);

                //sub title
                XSSFRow subTitleRow = sheet.getRow(3);
                CellStyle subTitleCellStyle = workbook.createCellStyle();
                subTitleCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                subTitleCellStyle.setBorderRight(CellStyle.BORDER_THIN);
                subTitleCellStyle.setBorderTop(CellStyle.BORDER_THIN);
                subTitleCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                subTitleCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                subTitleCellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                subTitleCellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
                Font subTitleFont = workbook.createFont();
                subTitleFont.setColor(HSSFColor.BLACK.index);
                subTitleFont.setFontHeightInPoints((short) 12);
                subTitleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                subTitleFont.setFontName("Times New Roman");
                subTitleCellStyle.setFont(subTitleFont);
                subTitleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                XSSFCell subTitleCell = subTitleRow.createCell(7);
                String scoreTbtt = resultsStaffDTO.getScoreTbtt()==null?"N/A":resultsStaffDTO.getScoreTbtt().toString();
                subTitleCell.setCellValue("THUÊ BAO TĂNG THÊM ("+scoreTbtt+" điểm)");
                subTitleCell.setCellStyle(subTitleCellStyle);
                XSSFCell subTitleCell2 = subTitleRow.createCell(13);
                String scoreTbm = resultsStaffDTO.getScoreTbm()==null?"N/A":resultsStaffDTO.getScoreTbm().toString();
                subTitleCell2.setCellValue("THUÊ BAO MỚI ("+scoreTbm+" điểm)");
                subTitleCell2.setCellStyle(subTitleCellStyle);
                XSSFCell subTitleCell3 = subTitleRow.createCell(17);
                String scoreTbrm = resultsStaffDTO.getScoreTbrm()==null?"N/A":resultsStaffDTO.getScoreTbrm().toString();
                subTitleCell3.setCellValue("THUÊ BAO RỜI MẠNG ("+scoreTbrm+" điểm)");
                subTitleCell3.setCellStyle(subTitleCellStyle);

                CellStyle logCellStyle = workbook.createCellStyle();
                logCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                logCellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                logCellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                Font logFont = workbook.createFont();
                logFont.setColor(HSSFColor.BLACK.index);
                logFont.setFontHeightInPoints((short) 11);
                logFont.setFontName("Times New Roman");
                logFont.setItalic(true);
                logCellStyle.setFont(logFont);
                logCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                XSSFRow logRow = sheet.createRow(2);
                XSSFCell logUserCell = logRow.createCell(12);
                logUserCell.setCellValue("Người kết xuất: "+sqlReportTemplateDTO.getUser());
                logUserCell.setCellStyle(logCellStyle);
                XSSFCell logDateCell = logRow.createCell(15);
                logDateCell.setCellValue("Ngày kết xuất: "+new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                logDateCell.setCellStyle(logCellStyle);

                //rows
                int beginIndex = 6;

                Double sumAddedScheduleMonth = 0d;
                Double sumAddedPerformAccumulatedN1 = 0d;
                Double sumAddedPerformAccumulatedN = 0d;
                Double sumAddedDelta = 0d;

                Double sumNewScheduleMonth = 0d;
                Double sumNewPerformAccumulatedN = 0d;

                Double sumLeaveScheduleMonth = 0d;
                Double sumLeavePerformAccumulatedN = 0d;
                for (int i = 0; i < resultsStaffs.size(); i++) {
                    BusinessResultStaffDTO resultsStaff = resultsStaffs.get(i);
                    XSSFRow row = sheet.createRow(beginIndex + i);
                    int columnIndex = 0;
                    row.createCell(columnIndex).setCellValue(i + 1);
                    columnIndex++;
                    row.createCell(columnIndex).setCellValue(resultsStaff.getProvincialCode() == null ? "" : resultsStaff.getProvincialCode());
                    columnIndex++;
                    row.createCell(columnIndex).setCellValue(resultsStaff.getProvincialCode() == null ? "" : resultsStaff.getProvincialCode());
                    columnIndex++;
                    row.createCell(columnIndex).setCellValue(resultsStaff.getStaffCode() == null ? "" : resultsStaff.getStaffCode());
                    columnIndex++;
                    row.createCell(columnIndex).setCellValue(resultsStaff.getStaffName() == null ? "" : resultsStaff.getStaffName());
                    columnIndex++;
                    this.addCellWithNull(row, columnIndex, resultsStaff.getTotalScore());
                    columnIndex++;
                    this.addCellWithNull(row, columnIndex, resultsStaff.getRank());
                    columnIndex++;

                    List<BusinessResultDetailDTO> details = resultsStaff.getListDetail();
                    if (details.size() == 3) {
                        //added
                        BusinessResultDetailDTO addedSub = details.get(0);
                        Double addedScheduleMonth = addedSub.getScheduleMonth() == null ? 0d : addedSub.getScheduleMonth();
                        Double addedPerformAccumulatedN1 = addedSub.getPerformAccumulatedN1() == null ? 0d : addedSub.getPerformAccumulatedN1();
                        Double addedPerformAccumulatedN = addedSub.getPerformAccumulatedN() == null ? 0d : addedSub.getPerformAccumulatedN();
                        Double addedDelta = addedSub.getDelta() == null ? 0d : addedSub.getDelta();
                        sumAddedScheduleMonth += addedScheduleMonth;
                        sumAddedPerformAccumulatedN1 += addedPerformAccumulatedN1;
                        sumAddedPerformAccumulatedN += addedPerformAccumulatedN;
                        sumAddedDelta += addedDelta;
                        this.addCellWithNull(row, columnIndex, addedSub.getScheduleMonth());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getPerformAccumulatedN1());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getPerformAccumulatedN());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getDelta());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getComplete());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, addedSub.getScorePass());
                        columnIndex++;

                        //new
                        BusinessResultDetailDTO newSub = details.get(1);
                        Double newScheduleMonth = newSub.getScheduleMonth() == null ? 0d : newSub.getScheduleMonth();
                        Double newPerformAccumulatedN = newSub.getPerformAccumulatedN() == null ? 0d : newSub.getPerformAccumulatedN();
                        sumNewScheduleMonth += newScheduleMonth;
                        sumNewPerformAccumulatedN += newPerformAccumulatedN;
                        this.addCellWithNull(row, columnIndex, newSub.getScheduleMonth());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, newSub.getPerformAccumulatedN());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, newSub.getComplete());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, newSub.getScorePass());
                        columnIndex++;

                        //leave
                        BusinessResultDetailDTO leaveSub = details.get(2);
                        Double leaveScheduleMonth = leaveSub.getScheduleMonth() == null ? 0d : leaveSub.getScheduleMonth();
                        Double leavePerformAccumulatedN = leaveSub.getPerformAccumulatedN() == null ? 0L : leaveSub.getPerformAccumulatedN();
                        sumLeaveScheduleMonth += leaveScheduleMonth;
                        sumLeavePerformAccumulatedN += leavePerformAccumulatedN;
                        this.addCellWithNull(row, columnIndex, leaveSub.getScheduleMonth());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, leaveSub.getPerformAccumulatedN());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, leaveSub.getComplete());
                        columnIndex++;
                        this.addCellWithNull(row, columnIndex, leaveSub.getScorePass());
                        columnIndex++;

                    }
                }

                CellRangeAddress region = new CellRangeAddress(beginIndex, beginIndex + resultsStaffs.size(), 0, 21);
                for (int i = region.getFirstRow(); i < region.getLastRow(); i++) {
                    Row row = sheet.getRow(i);
                    for (int j = region.getFirstColumn(); j < region.getLastColumn(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {

                            if (j == 5 || j == 6 || j == 12 || j == 16 || j == 20) {
                                CellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                                cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                                Font fontRed = workbook.createFont();
                                fontRed.setColor(HSSFColor.RED.index);
                                fontRed.setFontName("Times New Roman");
                                cellStyle.setFont(fontRed);
                                cell.setCellStyle(cellStyle);
                            } else if (j == 7 || j == 13 || j == 17) {
                                CellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
                                cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                                cellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                                Font fontBlue = workbook.createFont();
                                fontBlue.setColor(HSSFColor.BLUE.index);
                                fontBlue.setFontName("Times New Roman");
                                cellStyle.setFont(fontBlue);
                                cell.setCellStyle(cellStyle);
                            } else if(j==11||j==15||j==19)
                            {
                                CellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setBorderLeft(CellStyle.BORDER_THIN   );
                                cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                                cellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                                cellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                                cellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
                                Font font = workbook.createFont();
                                font.setColor(HSSFColor.BLACK.index);
                                font.setFontName("Times New Roman");
                                cellStyle.setFont(font);
                                cell.setCellStyle(cellStyle);
                            }
                            else
                            {
                                CellStyle cellStyle = workbook.createCellStyle();
                                cellStyle.setBorderLeft(CellStyle.BORDER_THIN   );
                                cellStyle.setBorderRight(CellStyle.BORDER_THIN);
                                cellStyle.setBorderTop(CellStyle.BORDER_THIN);
                                cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
                                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                                cellStyle.setFillPattern(CellStyle.THIN_FORWARD_DIAG);
                                cellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                                Font font = workbook.createFont();
                                font.setColor(HSSFColor.BLACK.index);
                                font.setFontName("Times New Roman");
                                cellStyle.setFont(font);
                                cell.setCellStyle(cellStyle);
                            }
                        }
                    }
                }

                XSSFRow sumRow = sheet.createRow(beginIndex - 1);

                sumRow.createCell(0).setCellValue("Tổng");

                XSSFCell cellSumScheduleMonth = sumRow.createCell(7);
                cellSumScheduleMonth.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumScheduleMonth.setCellFormula("SUM(H7:H" + resultsStaffs.size()+ beginIndex + ")");

                XSSFCell cellSumAddedPerformAccumulatedN1 = sumRow.createCell(8);
                cellSumAddedPerformAccumulatedN1.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumAddedPerformAccumulatedN1.setCellFormula("SUM(I7:I" + resultsStaffs.size()+ beginIndex + ")");

                XSSFCell cellSumAddedPerformAccumulatedN = sumRow.createCell(9);
                cellSumAddedPerformAccumulatedN.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumAddedPerformAccumulatedN.setCellFormula("SUM(J7:J" + resultsStaffs.size()+ beginIndex + ")");

                XSSFCell cellSumAddedDelta = sumRow.createCell(10);
                cellSumAddedDelta.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumAddedDelta.setCellFormula("SUM(K7:K" + resultsStaffs.size()+ beginIndex + ")");
                this.addCellWithNull(sumRow, 9, sumAddedDelta / sumAddedPerformAccumulatedN1);
                XSSFCell cell9 = sumRow.createCell(11);
                cell9.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell9.setCellFormula("K5/H5");

                XSSFCell cellSumNewScheduleMonth = sumRow.createCell(13);
                cellSumNewScheduleMonth.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumNewScheduleMonth.setCellFormula("SUM(N7:N" + resultsStaffs.size()+ beginIndex + ")");
                XSSFCell cellSumNewPerformAccumulatedN = sumRow.createCell(14);
                cellSumNewPerformAccumulatedN.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumNewPerformAccumulatedN.setCellFormula("SUM(O7:O" + resultsStaffs.size()+ beginIndex + ")");
                XSSFCell cell13 = sumRow.createCell(15);
                cell13.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell13.setCellFormula("O5/N5");


                XSSFCell cellSumLeaveScheduleMonth = sumRow.createCell(17);
                cellSumLeaveScheduleMonth.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumLeaveScheduleMonth.setCellFormula("AVERAGE(R6:R" + resultsStaffs.size()+ beginIndex + ")");

                XSSFCell cellSumLeavePerformAccumulatedN = sumRow.createCell(18);
                cellSumLeavePerformAccumulatedN.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cellSumLeavePerformAccumulatedN.setCellFormula("AVERAGE(S6:S" + resultsStaffs.size()+ beginIndex + ")");
                XSSFCell cell17 = sumRow.createCell(19);
                cell17.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell17.setCellFormula("2-S5/R5");

                CellRangeAddress sumRegion = new CellRangeAddress(beginIndex - 1, beginIndex, 0, 21);
                for (int i = sumRegion.getFirstRow(); i < sumRegion.getLastRow(); i++) {
                    Row row = sheet.getRow(i);
                    for (int j = sumRegion.getFirstColumn(); j < sumRegion.getLastColumn(); j++) {
                        Cell cell = row.getCell(j);

                        if (cell != null) {
                            if(j==11||j==15||j==19)
                            {
                                XSSFCellStyle style = workbook.createCellStyle();
                                style.setBorderLeft(CellStyle.BORDER_THIN);
                                style.setBorderRight(CellStyle.BORDER_THIN);
                                style.setBorderTop(CellStyle.BORDER_THIN);
                                style.setBorderBottom(CellStyle.BORDER_THIN);
                                XSSFFont font = workbook.createFont();
                                font.setFontHeightInPoints((short) 12);
                                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                                font.setFontName("Times New Roman");
                                style.setFont(font);
                                style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
                                cell.setCellStyle(style);
                            }
                            else
                            {
                                XSSFCellStyle style = workbook.createCellStyle();
                                style.setBorderLeft(CellStyle.BORDER_THIN);
                                style.setBorderRight(CellStyle.BORDER_THIN);
                                style.setBorderTop(CellStyle.BORDER_THIN);
                                style.setBorderBottom(CellStyle.BORDER_THIN);
                                XSSFFont font = workbook.createFont();
                                font.setFontHeightInPoints((short) 12);
                                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                                font.setFontName("Times New Roman");
                                style.setFont(font);
                                cell.setCellStyle(style);
                            }
                        }
                    }
                }
            }
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addCellWithNull(XSSFRow row, int index, Double val){
        if(val==null || val == 0d) {
            row.createCell(index).setCellValue("");
        }
        else
        {
            row.createCell(index).setCellValue(val);
        }
    }

    private void addCellWithNull(XSSFRow row, int index, Integer val){
        if(val==null || val == 0) {
            row.createCell(index).setCellValue("");
        }
        else
        {
            row.createCell(index).setCellValue(val);
        }
    }

    private void writeDetailsReport(XSSFSheet sheet, List<Object[]> listData, int pintStartRowIndex,
                                    Map<String, CellStyle> pmpStyles, List<String> listColumn
    ) throws Exception {


        int vintRowIdx = pintStartRowIndex - 1;
        int vintStt = 0;
        if (!DataUtil.isNullOrEmpty(listData)) {
            for (Object[] planMonthly : listData) {
                Row row = sheet.createRow(vintRowIdx++);
                vintStt++;

                row.createCell(0).setCellValue(vintStt);

                for (int i = 0; i < listColumn.size() - 1; i++) {
                    if (planMonthly[i] != null) {
                        row.createCell(i + 1).setCellValue(planMonthly[i].toString());
                    } else {
                        row.createCell(i + 1).setCellValue("");
                    }
                }

                for (int i = 0; i < row.getLastCellNum(); i++) {
                    row.getCell(i).setCellStyle(pmpStyles.get("content"));
                    sheet.autoSizeColumn(i);
                }

            }
            sheet.setColumnWidth(0, 1600);
        }

    }

    public Map<String, CellStyle> createStyles(XSSFWorkbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle cellStyle;

        //table name
        Font tableName = workbook.createFont();
        tableName.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        tableName.setFontHeightInPoints((short) 13);
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFont(tableName);
        cellStyle.setWrapText(true);
        styles.put("tableName", cellStyle);

        //header style
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        cellStyle = createBorderedStyle(workbook);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFont(headerFont);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("header", cellStyle);

        //content style
        cellStyle = createBorderedStyle(workbook);
        styles.put("content", cellStyle);

        //date style
        cellStyle = createBorderedStyle(workbook);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put("date", cellStyle);

        return styles;

    }

    private CellStyle createBorderedStyle(XSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            // commons-io
            //IOUtils.copy(inputStream, outputStream);

        }

    }

    private void determineColRange(Class pclsClazz) {
        for (Method method : pclsClazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn colAnn = (ExcelColumn) method.getAnnotation(ExcelColumn.class);
                String colName = colAnn.name();
                if (mstrStartCol == null || mstrEndCol == null) {
                    if (mstrStartCol == null) {
                        mstrStartCol = colName;
                    }
                    if (mstrEndCol == null) {
                        mstrEndCol = colName;
                    }
                } else {
                    if (colName != null) {
                        if (colName.compareTo(mstrStartCol) < 0) {
                            mstrStartCol = colName;
                        }

                        if (colName.compareTo(mstrEndCol) > 0) {
                            mstrEndCol = colName;
                        }
                    }
                }
            }
        }

    }

}
