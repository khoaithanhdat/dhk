package vn.vissoft.dashboard.repo;

import vn.vissoft.dashboard.dto.*;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.model.ManageInfoPartner;
import vn.vissoft.dashboard.model.SalaryMappingAreaProvince;
import vn.vissoft.dashboard.model.SalaryMappingLeaderStaff;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface SalaryMappingAreaProvinceRepoCustom  {
    public List<OrganizationalStructureDTO> getOrganizationalStructure() throws Exception;
    public List<Object[]> getLstMappingAreaProvice(String provinceCode, String provinceName,String areaCode) throws Exception;
    String updateProvince( StaffDTO userTO,SalaryMappingAreaProvinceDTO newProvinceDTO,String status) throws Exception;

    List<Object[]> checkByShopCode(String shopCode, String areaCode, Timestamp expiredDate,String type) throws Exception;

    SalaryMappingAreaProvince createProvince (SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO, ManageInfoPartner infoPartner, StaffDTO user) throws Exception;

    List<Object[]>  searchTree(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO) throws Exception;

    SalaryMappingLeaderStaff insertBusinessMan(SalaryMappingLeaderStaffDTO mappingLeaderStaffDTO,StaffDTO user) throws Exception;
}
