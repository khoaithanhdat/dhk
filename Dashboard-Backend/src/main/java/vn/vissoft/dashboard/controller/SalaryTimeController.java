package vn.vissoft.dashboard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.vissoft.dashboard.dto.ConfigSaleAreaSalaryDTO;
import vn.vissoft.dashboard.dto.SalaryTimeDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.helper.ApiResponse;

import vn.vissoft.dashboard.services.SalaryTimeService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("${api_base_path}/management/salarytime")
public class SalaryTimeController {

    private static final Logger LOGGER = LogManager.getLogger(SalaryTimeController.class);

    @Autowired
    private SalaryTimeService salaryTimeService;

    @PostMapping("/getSalaryTimeByArea/{pstrAreaCode}")
    public ApiResponse getSalaryByArea(@PathVariable String pstrAreaCode) {
        try {
            ConfigSaleAreaSalaryDTO configSaleAreaSalaryDTO = salaryTimeService.getSalaryByArea(pstrAreaCode);
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", configSaleAreaSalaryDTO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }


    @PostMapping("/updateByRegion")
    public ApiResponse updateByRegion(Authentication authentication, @RequestBody ConfigSaleAreaSalaryDTO configSaleAreaSalaryDTO) throws Exception {
        try {
            StaffDTO staffDTO = (StaffDTO) authentication.getPrincipal();
            String message = salaryTimeService.updateConfig(configSaleAreaSalaryDTO, staffDTO);
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", message);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, true, "", null);
        }
    }

    @DeleteMapping("/deleteSalaryTime")
    public ApiResponse deleteSalaryTime(@RequestBody SalaryTimeDTO salaryTimeDTO) {
        try {
            salaryTimeService.deleteSalaryTime(salaryTimeDTO);
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", null);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, true, "", null);
        }
    }
}
