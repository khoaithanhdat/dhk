package vn.vissoft.dashboard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.vissoft.dashboard.dto.SalConfigHsTinhLuongDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.VdsStaffDTO;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.model.SalConfigHsTinhLuong;
import vn.vissoft.dashboard.services.SalConfigHsTinhLuongService;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api_base_path}/management/salConfigHsTinhLuong")
public class SalConfigHsTinhLuongController {

    private static final Logger LOGGER = LogManager.getLogger(SalConfigHsTinhLuongController.class);

    @Autowired
    private SalConfigHsTinhLuongService salConfigHsTinhLuongService;

    @GetMapping("/getAllSalConfigHsTinhLuong")
    public ApiResponse getAllSalConfigHsTinhLuong() {
        try {
            List<SalConfigHsTinhLuongDTO> vlstSalConfigHsTinhLuong = salConfigHsTinhLuongService.getAllSalConfigHsTinhLuong("1");
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalConfigHsTinhLuong);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @GetMapping("/getSalConfigHsTinhLuongById/{id}")
    public ApiResponse getSalConfigHsTinhLuongById(@PathVariable Long id) {
        try {
            Optional<SalConfigHsTinhLuong> vlstSalConfigHsTinhLuong = salConfigHsTinhLuongService.getById(id);
            if (vlstSalConfigHsTinhLuong.isPresent()) {
                return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalConfigHsTinhLuong);
            }
            return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, true, "", new ArrayList<>());

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @PostMapping("/updateSalConfigHsTinhLuong")
    public ApiResponse updateSalConfigHsTinhLuong(@RequestBody SalConfigHsTinhLuongDTO salConfigHsTinhLuongDTO, Authentication authentication) {
        try {
            StaffDTO staffDTO = (StaffDTO) authentication.getPrincipal();
            salConfigHsTinhLuongService.saveSalConfigHsTinhLuong(salConfigHsTinhLuongDTO, staffDTO);
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }
}
