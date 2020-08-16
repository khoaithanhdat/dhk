package vn.vissoft.dashboard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vissoft.dashboard.dto.ConfigSalaryLeaderDTO;
import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.services.SalConfigHsTinhLuongService;
import vn.vissoft.dashboard.services.SalConfigSaleFeeService;
import vn.vissoft.dashboard.services.SalConfigStaffGiffService;
import vn.vissoft.dashboard.services.SalConfigStaffTargetService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
@RequestMapping("${api_base_path}/management/configSalaryLeader")
public class ConfigSalaryLeaderController {

    private static final Logger LOGGER = LogManager.getLogger(ConfigSalaryLeaderController.class);

    @Autowired
    private SalConfigStaffTargetService salConfigStaffTargetService;

    @Autowired
    private SalConfigSaleFeeService salConfigSaleFeeService;

    @Autowired
    private SalConfigHsTinhLuongService salConfigHsTinhLuongService;

    @Autowired
    private SalConfigStaffGiffService salConfigStaffGiffService;

    @Transactional
    @PostMapping("/updateConfigSalaryLeader")
    public ApiResponse updateConfigSalaryLeader(@RequestBody ConfigSalaryLeaderDTO configSalaryLeaderDTO, Authentication authentication) {
        try {

            StaffDTO staffDTO = (StaffDTO) authentication.getPrincipal();

            if (null != configSalaryLeaderDTO.getSalConfigStaffTargetModel() && configSalaryLeaderDTO.getSalConfigStaffTargetModel().size() > 0) {
                for(int i = 0; i < configSalaryLeaderDTO.getSalConfigStaffTargetModel().size(); i++) {
                    salConfigStaffTargetService.saveSalConfigStaffTarget(configSalaryLeaderDTO.getSalConfigStaffTargetModel().get(i), staffDTO);
                }
            }

            if (null != configSalaryLeaderDTO.getSalConfigHsTinhLuongModel() && configSalaryLeaderDTO.getSalConfigHsTinhLuongModel().size() > 0) {
                for (int i = 0; i < configSalaryLeaderDTO.getSalConfigHsTinhLuongModel().size(); i++) {
                    salConfigHsTinhLuongService.saveSalConfigHsTinhLuong(configSalaryLeaderDTO.getSalConfigHsTinhLuongModel().get(i), staffDTO);
                }
            }

            if (null != configSalaryLeaderDTO.getSalConfigSaleFeeModel() && configSalaryLeaderDTO.getSalConfigSaleFeeModel().size() > 0) {
                for (int i = 0; i < configSalaryLeaderDTO.getSalConfigSaleFeeModel().size(); i++) {
                    salConfigSaleFeeService.saveSalConfigSaleFee(configSalaryLeaderDTO.getSalConfigSaleFeeModel().get(i), staffDTO);
                }
            }

            if (null != configSalaryLeaderDTO.getSalConfigStaffGiffModel() && configSalaryLeaderDTO.getSalConfigStaffGiffModel().size() > 0) {
                for (int i = 0; i < configSalaryLeaderDTO.getSalConfigStaffGiffModel().size(); i++) {
                    salConfigStaffGiffService.saveSalConfigStaffGiff(configSalaryLeaderDTO.getSalConfigStaffGiffModel().get(i), staffDTO);
                }
            }
            
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }
}
