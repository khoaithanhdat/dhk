package vn.vissoft.dashboard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.vissoft.dashboard.dto.SaleFee;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.services.SaleFeeService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("${api_base_path}/management/saleFee")
public class SaleFeeController {

    private static final Logger LOGGER = LogManager.getLogger(SaleFeeController.class);

    @Autowired
    private SaleFeeService saleFeeService;

    @GetMapping("/getAllSaleFee")
    public ApiResponse getAllSaleFee() {
        try {
            List<SaleFee> saleFees = saleFeeService.getAllBySaleFeeActive("1");
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", saleFees);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    @GetMapping("/getSaleFeeByIdAndStatus")
    public ApiResponse getSaleFeeByIdAndStatus(@RequestParam Long id) {
        try {
            SaleFee saleFees = saleFeeService.getByIdAndStatus(id, "1");
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", saleFees);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }
}
