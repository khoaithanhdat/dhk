package vn.vissoft.dashboard.services;

import org.springframework.security.core.Authentication;
import vn.vissoft.dashboard.dto.*;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.model.ManageInfoPartner;
import vn.vissoft.dashboard.model.SalaryMappingAreaProvince;

import java.util.List;

public interface OrganizationalStructureService {

    List<OrganizationalStructureDTO> getOrganizationStructure() throws Exception;

    List<SalaryMappingAreaProvinceDTO> getLstMappingAreaProvice(String provinceCode, String provinceName,String area) throws Exception;
    String updateProvince(SalaryMappingAreaProvinceDTO areaProvinceDTO, Authentication authentication) throws Exception;

    ApiResponse createProvince (SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO,Authentication authentication) throws Exception;

    ApiResponse searchTree(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO) throws Exception;

    ApiResponse changeArea(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO,Authentication authentication) throws Exception;

    ApiResponse changeLeader(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO,Authentication authentication) throws Exception;

    ApiResponse insertBusinessMan(SalaryMappingLeaderStaffDTO mappingLeaderStaffDTO,Authentication authentication) throws Exception;


}
