package vn.vissoft.dashboard.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.EliminateNumberExcel;
import vn.vissoft.dashboard.dto.excel.KpiActionProgramExcel;
import vn.vissoft.dashboard.dto.excel.VTTServiceExcel;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.helper.Constants;
import vn.vissoft.dashboard.helper.excelgenerator.EliminateNumberWriter;
import vn.vissoft.dashboard.helper.excelgenerator.KpiActionProgramWriter;
import vn.vissoft.dashboard.services.ImportSalaryService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("${api_base_path}/salary")
public class ImportSalaryController {

    private static final Logger LOGGER = LogManager.getLogger(ImportSalaryController.class);

    @Autowired
    private EliminateNumberWriter eliminateNumberWriter;

    @Autowired
    private KpiActionProgramWriter kpiActionProgramWriter;

    @Autowired
    private ImportSalaryService importSalaryService;

    @PostMapping(value = "/downloadImportFile/{importType}")
    public ResponseEntity<InputStreamResource> excelVTTReport(@PathVariable String importType) {

        ByteArrayInputStream in = null;

        try {
            if (Constants.IMPORT_TYPE.ELIMINATE_TYPE.equals(importType)) {
                in = eliminateNumberWriter.write(EliminateNumberExcel.class);
            } else if (Constants.IMPORT_TYPE.KPI_TYPE.equals(importType)) {
                in = kpiActionProgramWriter.write(KpiActionProgramExcel.class);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

            return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/upload/{importType}/{prdId}")
    public ApiResponse upload(Authentication authentication, @RequestParam MultipartFile file, @PathVariable String importType, @PathVariable Long prdId) {
        try {
            StaffDTO user = (StaffDTO) authentication.getPrincipal();
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", importSalaryService.upload(file, user, importType,prdId));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }
}
