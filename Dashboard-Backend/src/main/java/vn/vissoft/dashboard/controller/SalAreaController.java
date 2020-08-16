package vn.vissoft.dashboard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.model.SalArea;
import vn.vissoft.dashboard.services.SalAreaService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("${api_base_path}/management/salarea")
public class SalAreaController {

    private static final Logger LOGGER = LogManager.getLogger(SalAreaController.class);

    @Autowired
    private SalAreaService salAreaService;

    @GetMapping("/getActiveSalArea")
    public ApiResponse getActiveSalArea() {
        try {
            List<SalArea> vlstSalAreas = salAreaService.getActiveSalArea();
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", vlstSalAreas);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }


}
