package vn.vissoft.dashboard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.vissoft.dashboard.dto.SalConfigSaleFeeDTO;
import vn.vissoft.dashboard.dto.SalConfigStaffTargetDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.VdsStaffDTO;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.model.SalConfigStaffTarget;
import vn.vissoft.dashboard.services.SalConfigStaffTargetService;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api_base_path}/management/salConfigStaffTarget")
public class SalConfigStaffTargetController {

    private static final Logger LOGGER = LogManager.getLogger(SalConfigStaffTargetController.class);

    @Autowired
    private SalConfigStaffTargetService salConfigStaffTargetService;

    @GetMapping("/getAllSalConfigStaffTarget")
    public ApiResponse getAllSalConfigStaffTarget() {
        try {
            List<SalConfigStaffTargetDTO> vlstSalConfigStaffTarget = salConfigStaffTargetService.getAllSalConfigTargetActive("1");
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalConfigStaffTarget);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @GetMapping("/getSalConfigStaffTarget/{id}")
    public ApiResponse getSalConfigStaffTargetById(@PathVariable Long id) {
        try {
            Optional<SalConfigStaffTarget> vlstSalConfigStaffTarget = salConfigStaffTargetService.getById(id);
            if (vlstSalConfigStaffTarget.isPresent()) {
                return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalConfigStaffTarget);
            }
            return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, false, "", new ArrayList<>());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @GetMapping("/getAllSalConfigStaffTargetByService")
    public ApiResponse getAllSalConfigStaffTargetByService() {
        try {
            List<SalConfigStaffTargetDTO> vlstSalConfigStaffTarget = salConfigStaffTargetService.getAllSalConfigTargetByServiceId();
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalConfigStaffTarget);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @GetMapping("/getAllSalConfigStaffTargetCompleteByService")
    public ApiResponse getAllSalConfigStaffTargetCompleteByService(@RequestParam Long serviceId) {
        try {
            List<SalConfigStaffTargetDTO> vlstSalConfigStaffTarget = salConfigStaffTargetService.getAllSalConfigTargetCompleteByServiceId(serviceId);
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalConfigStaffTarget);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @PostMapping("/updateSalConfigStaffTarget")
    public ApiResponse updateSalConfigStaffTarget(@RequestBody SalConfigStaffTargetDTO salConfigStaffTargetDTO, Authentication authentication) {
        try {
            StaffDTO staffDTO = (StaffDTO) authentication.getPrincipal();

            salConfigStaffTargetService.saveSalConfigStaffTarget(salConfigStaffTargetDTO, staffDTO);
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }
}
