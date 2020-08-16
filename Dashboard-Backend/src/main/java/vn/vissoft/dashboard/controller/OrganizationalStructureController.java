package vn.vissoft.dashboard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.vissoft.dashboard.config.I18N;
import vn.vissoft.dashboard.dto.*;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.model.ManageInfoPartner;
import vn.vissoft.dashboard.services.OrganizationalStructureService;
import vn.vissoft.dashboard.services.PartnerService;
import vn.vissoft.dashboard.services.StaffService;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api_base_path}/management/org")
public class OrganizationalStructureController {

    private static final Logger LOGGER = LogManager.getLogger(OrganizationalStructureController.class);
    @Autowired
    OrganizationalStructureService organizationalStructureService;
    @Autowired
    PartnerService partnerService;

    @Autowired
    StaffService staffService;

    @GetMapping("/getOrg")
    public ApiResponse getAllUnits() throws Exception {
        List<OrganizationalStructureDTO> units = organizationalStructureService.getOrganizationStructure();
        if (DataUtil.isNullOrEmpty(units)) {
            return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, false, "content is not found", null);
        }
        return ApiResponse.build(HttpServletResponse.SC_OK, true, "", units);
    }
    // lay ra 63 tinh thanh
    @GetMapping("/getProvince")
    public ApiResponse getAllProvince() throws Exception{
        List<ManageInfoPartnerDTO> lstManageInfoPartner = partnerService.getAllProvince();
        if (DataUtil.isNullOrEmpty(lstManageInfoPartner)) {
            return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, false, "content is not found", null);
        }
        return ApiResponse.build(HttpServletResponse.SC_OK, true, "", lstManageInfoPartner);
    }

    // tim kiem danh sach theo vug
    @PostMapping("/getByArea")
    public ApiResponse geArea(@RequestParam("provinceCode") String provinceCode,@RequestParam("provinceName") String provinceName,@RequestParam("areaCode") String areaCode)throws Exception{
        List<SalaryMappingAreaProvinceDTO> lstAreaProvinceDTOS = new ArrayList<>();
        try {
            if(DataUtil.isNullOrEmpty(areaCode)){
                return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, false, I18N.get("message.erro.val.null"), null);
            }


            lstAreaProvinceDTOS =   organizationalStructureService.getLstMappingAreaProvice(provinceCode,provinceName,areaCode);
            if(DataUtil.isNullOrEmpty(lstAreaProvinceDTOS)){
                return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, false, "content is not found", null);
            }
            return ApiResponse.build(HttpServletResponse.SC_OK, true, "", lstAreaProvinceDTOS);
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
            return ApiResponse.build(HttpServletResponse.SC_NO_CONTENT, false, "content is not found", null);
        }
    }

     // update thong tin vietell tinh
    @PostMapping("/updateProvince")
    public ApiResponse updateProvince(@RequestBody SalaryMappingAreaProvinceDTO  mappingAreaProvinceDTO,Authentication authentication) throws Exception{
        String result = null;
        try{
            if(null==mappingAreaProvinceDTO){
                return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
            }

            result = organizationalStructureService.updateProvince(mappingAreaProvinceDTO,authentication);
            if(DataUtil.safeEqual("01",result)){
                return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, I18N.get("message.erro.system"), null);
            }else if(DataUtil.safeEqual("02",result)){
                return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, I18N.get("message.erro.notexist"), null);
            }else if(DataUtil.safeEqual("03",result)){
                return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, I18N.get("message.not.sysdate"), null);
            }else if(DataUtil.safeEqual("04",result)){
                return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, I18N.get("message.erro.system.db"), null);
            }
            if(DataUtil.safeEqual("1",result)){
                return ApiResponse.build(HttpServletResponse.SC_OK, false, I18N.get("common.servicescore.updatesuccess"), null);
            }else {
                return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "cap nhap khong thanh cong", null);
            }

        }catch (Exception ex){
            LOGGER.error(ex.getMessage(),ex);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
        }
    }
    // them moi viettel tinh
    @PostMapping("/createProvince")
    private ApiResponse createProvince(@RequestBody SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO,Authentication authentication) throws  Exception{
        ApiResponse apiResponse = new ApiResponse();
        try {
            if(null == mappingAreaProvinceDTO){
                return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
            }else {
                apiResponse = organizationalStructureService.createProvince(mappingAreaProvinceDTO,authentication);
            }

            return apiResponse;
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(),ex);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
        }
    }

    // tim kiem theo dieu kien cua cay cau hinh to chuc vung
    @PostMapping("/searchTree")
    private  ApiResponse searchTree(@RequestBody SalaryMappingAreaProvinceDTO salaryMappingAreaProvinceDTO) throws Exception{
        ApiResponse apiResponse = new ApiResponse();
        try{
            apiResponse =   organizationalStructureService.searchTree(salaryMappingAreaProvinceDTO);
            return apiResponse;
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(),ex);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
        }
    }

    // chuyen vung
    @PostMapping("/changeArea")
    private ApiResponse changeArea(@RequestBody SalaryMappingAreaProvinceDTO salaryMappingAreaProvinceDTO,Authentication authentication) throws Exception{
        ApiResponse apiResponse = new ApiResponse();
        try {

            return    organizationalStructureService.changeArea(salaryMappingAreaProvinceDTO,authentication);

        }catch (Exception ex){
            LOGGER.error(ex.getMessage(),ex);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
        }
    }
   // chuyen cum truong
    @PostMapping("/changeLear")
    private ApiResponse changeLear(@RequestBody SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO , Authentication authentication) throws  Exception{
        ApiResponse apiResponse = new ApiResponse();
        try {
            return null;
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(),ex);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
        }
    }

    // lay ra nhan vien kinh doanh
     @GetMapping("/getAllStaffBusiness")
    private ApiResponse getAllStaffBusiness() throws Exception{
        ApiResponse apiResponse= new ApiResponse();
        try {
          List<VdsStaffDTO> staffDTOList=  staffService.getAllStaffBusiness(null);
          if(!DataUtil.isNullOrEmpty(staffDTOList)){
              return ApiResponse.build(HttpServletResponse.SC_OK, false, "content is not found", staffDTOList);
          }
            return ApiResponse.build(HttpServletResponse.SC_OK, false, "content is not found", null);
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(),ex);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
        }
     }
    // luu nhan vien kinh doanh
    @PostMapping("/insertBusinessMan")
    private ApiResponse insertBusinessMan(@RequestBody SalaryMappingLeaderStaffDTO mappingLeaderStaffDTO , Authentication authentication){
        try {
            return   organizationalStructureService.insertBusinessMan(mappingLeaderStaffDTO,authentication);
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(),ex);
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
        }
    }
}
