package vn.vissoft.dashboard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api_base_path}/management/salConfigStaffGiff")
public class SalConfigStaffGiffController {

    private static final Logger LOGGER = LogManager.getLogger(SalConfigStaffGiffController.class);
}
