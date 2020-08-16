package vn.vissoft.dashboard.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import vn.vissoft.dashboard.dto.CollaboratorDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.CollaboratorServiceExel;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.helper.excelgenerator.CollaboratorExcelWriter;
import vn.vissoft.dashboard.model.Collaborator;
import vn.vissoft.dashboard.model.PlanMonthly;
import vn.vissoft.dashboard.services.CollaboratorService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("${api_base_path}/management/collaborator")
public class CollaboratorController {

    private static final Logger LOGGER = LogManager.getLogger(OrganizationalStructureController.class);

    @Autowired
    private CollaboratorExcelWriter collaboratorExcelWriter;

    @Autowired
    CollaboratorService collaboratorService;

    @GetMapping("/getCollaborator")
    public ApiResponse getAllCollaborator() throws Exception {
        List<CollaboratorDTO> units = collaboratorService.getCollaborator();
        if (DataUtil.isNullOrEmpty(units)) {
            return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, false, "content is not found", null);
        }
        return ApiResponse.build(HttpServletResponse.SC_OK, true, "", units);
    }

    @PostMapping("/getCollaboratorByCondition")
    public ApiResponse getCollaboratorByCondition(@RequestBody CollaboratorDTO collaboratorDTO) throws Exception {
        List<Collaborator> units = collaboratorService.getCollaboratorByCondition(collaboratorDTO);
        if (DataUtil.isNullOrEmpty(units)) {
            return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, false, "content is not found", null);
        }
        return ApiResponse.build(HttpServletResponse.SC_OK, true, "", units);
    }

    @GetMapping("/deleteCollaborator/{collabroratorId}")
    public ApiResponse deletePlanMonthly(@PathVariable(value = "collabroratorId") int id,Authentication authentication) {
        StaffDTO staffDTO = (StaffDTO) authentication.getPrincipal();
        try {
            ApiResponse apiResponse = ApiResponse.build(HttpServletResponse.SC_OK, true, "", collaboratorService.deleteCollaborator(id,staffDTO));
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @PostMapping("/addCollaborator")
    public ApiResponse addCollaborator(Authentication authentication, @RequestBody Collaborator collaborator) {
        StaffDTO staffDTO = (StaffDTO) authentication.getPrincipal();
        try {
            String vstrResult = collaboratorService.persist(collaborator, staffDTO);
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vstrResult);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @GetMapping(value = "/downloadCollaboratorFile")
    public ResponseEntity<InputStreamResource> excelVTTReport() {

        ByteArrayInputStream in = null;

        try {
            in = collaboratorExcelWriter.write(CollaboratorServiceExel.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=collaborator.xlsx");

            return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/uploadCollaboratorFile")
    @ExceptionHandler(MultipartException.class)
    public ApiResponse upload(Authentication authentication, @RequestParam MultipartFile file) {
        try {
            StaffDTO user = (StaffDTO) authentication.getPrincipal();
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", collaboratorService.upload(file, user));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @ExceptionHandler(IllegalStateException.class)
    public void catchException(IllegalStateException e){
        if (e.getMessage()!= null){
            System.out.println("abc");
        }
    }

}
