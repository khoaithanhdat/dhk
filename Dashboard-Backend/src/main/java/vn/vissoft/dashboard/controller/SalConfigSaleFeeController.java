package vn.vissoft.dashboard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.vissoft.dashboard.dto.SalConfigSaleFeeDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.VdsStaffDTO;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.model.SalConfigSaleFee;
import vn.vissoft.dashboard.services.SalConfigSaleFeeService;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api_base_path}/management/salConfigSaleFee")
public class SalConfigSaleFeeController {

    private static final Logger LOGGER = LogManager.getLogger(SalConfigSaleFeeController.class);

    @Autowired
    private SalConfigSaleFeeService salConfigSaleFeeService;

    @GetMapping("/getAllSalConfigSaleFee")
    public ApiResponse getAllSalConfigSaleFee() {
        try {
            List<SalConfigSaleFeeDTO> vlstSalConfigSaleFee = salConfigSaleFeeService.getAllSalConfigSaleFeeActive("1");
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalConfigSaleFee);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @GetMapping("/getSalConfigSaleFeeById/{id}")
    public ApiResponse getSalConfigSaleFeeById(@PathVariable Long id) {
        try {
            Optional<SalConfigSaleFee> vlstSalConfigSaleFee = salConfigSaleFeeService.getById(id);
            if (vlstSalConfigSaleFee.isPresent()) {
                return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalConfigSaleFee);
            }
            return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, true, "", new ArrayList<>());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @GetMapping("/getSalConfigSaleFeeByCondition")
    public ApiResponse getSalConfigSaleFeeByCondition(@RequestParam(name = "feeName", required = false) String feeName, @RequestParam(name = "receiveFrom", required = false) String receiveFrom) {
        try {
            List<SalConfigSaleFeeDTO> vlstSalConfigSaleFeeDTO = salConfigSaleFeeService.getByCondition(feeName, receiveFrom);
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalConfigSaleFeeDTO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @PostMapping("/updateSalConfigSaleFee")
    public ApiResponse updateSalConfigSaleFee(@RequestBody SalConfigSaleFeeDTO salConfigSaleFeeDTO, Authentication authentication) {
        try {
            StaffDTO staffDTO = (StaffDTO) authentication.getPrincipal();

            salConfigSaleFeeService.saveSalConfigSaleFee(salConfigSaleFeeDTO, staffDTO);
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }
}
