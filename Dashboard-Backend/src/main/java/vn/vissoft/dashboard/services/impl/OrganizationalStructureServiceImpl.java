package vn.vissoft.dashboard.services.impl;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vn.vissoft.dashboard.config.I18N;
import vn.vissoft.dashboard.controller.OrganizationalStructureController;
import vn.vissoft.dashboard.dto.*;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.helper.Constants;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.model.ActionAudit;
import vn.vissoft.dashboard.model.ManageInfoPartner;
import vn.vissoft.dashboard.model.SalaryMappingAreaProvince;
import vn.vissoft.dashboard.model.SalaryMappingLeaderStaff;
import vn.vissoft.dashboard.repo.ActionAuditRepo;
import vn.vissoft.dashboard.repo.SalaryMappingAreaProvinceRepo;
import vn.vissoft.dashboard.services.ActionDetailService;
import vn.vissoft.dashboard.services.OrganizationalStructureService;
import vn.vissoft.dashboard.services.PartnerService;
import vn.vissoft.dashboard.services.StaffService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_YEAR;

@Transactional
@Service
public class OrganizationalStructureServiceImpl implements OrganizationalStructureService {
    private static final Logger LOGGER = LogManager.getLogger(OrganizationalStructureServiceImpl.class);
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private SalaryMappingAreaProvinceRepo salaryMappingAreaProvinceRepo;

    @Autowired
    private ActionDetailService actionDetailService;

    @Autowired
    private ActionAuditRepo actionAuditRepo;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    StaffService staffService;

    @Override
    public List<OrganizationalStructureDTO> getOrganizationStructure() throws Exception {
        return salaryMappingAreaProvinceRepo.getOrganizationalStructure();
    }

    @Override
    public List<SalaryMappingAreaProvinceDTO> getLstMappingAreaProvice(String provinceCode, String provinceName,String areaCode) throws Exception {
        List<Object[]>  lstAreaProvinceDTO = salaryMappingAreaProvinceRepo.getLstMappingAreaProvice(provinceCode,provinceName,areaCode);
        List<SalaryMappingAreaProvinceDTO> result = new ArrayList<>();
        if(!DataUtil.isNullOrEmpty(lstAreaProvinceDTO)){

            for(Object[] objects: lstAreaProvinceDTO){
                SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO = new SalaryMappingAreaProvinceDTO();
                mappingAreaProvinceDTO.setShopCode(DataUtil.safeToString(objects[0]));
                mappingAreaProvinceDTO.setShortName(DataUtil.safeToString(objects[1]));
                mappingAreaProvinceDTO.setShopName(DataUtil.safeToString(objects[2]));
                mappingAreaProvinceDTO.setAreaName(DataUtil.safeToString(objects[3]));

                Timestamp timestamp = new Timestamp(DataUtil.safeToDate(objects[4]).getTime());
                mappingAreaProvinceDTO.setExpiredDate(timestamp);
                mappingAreaProvinceDTO.setVdsChannelCode(DataUtil.safeToString(objects[5]));
                mappingAreaProvinceDTO.setId(DataUtil.safeToInt(objects[6]));
                result.add(mappingAreaProvinceDTO);
            }
        }
        return result;
    }

    @Override
    public String updateProvince(SalaryMappingAreaProvinceDTO areaProvinceDTO, Authentication authentication) throws Exception {
        String result = null;
        StaffDTO user = (StaffDTO) authentication.getPrincipal();
        if(null==areaProvinceDTO){
            return null;
        }else {
            //check null
            if(DataUtil.isNullOrEmpty(areaProvinceDTO.getCode()) || DataUtil.isNullOrEmpty(areaProvinceDTO.getParent())){
                return "01";
            }
            //check ton tai
            List<Object[]> object = salaryMappingAreaProvinceRepo.checkByShopCode(areaProvinceDTO.getCode(),null,null,"update");

            if(!DataUtil.isNullOrEmpty(object)){
                if(object.size() >1){
                    return "04";
                }
                SalaryMappingAreaProvinceDTO dtoOld =   getProvinceDTO(object.get(0));
                java.util.Date vdtCurrentDate = new java.util.Date();
                long vlngTime = vdtCurrentDate.getTime();
                Timestamp timestamp = new Timestamp(vlngTime);
                //neu ngay het han nho hon ngay hien tai
                if(null!=areaProvinceDTO.getExpiredDate()){
                    if(areaProvinceDTO.getExpiredDate().getTime()<timestamp.getTime()){
                        return "03";
                    }
                }
                result = salaryMappingAreaProvinceRepo.updateProvince(user,areaProvinceDTO,null);

                //luu action audit
                createOrUpdateActionAudit("update",dtoOld,areaProvinceDTO,user,null);



            }else {
                return "02"; // khong ton tai ban ghi
            }
        }

        return result;
    }

    @Override
    public ApiResponse createProvince(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO, Authentication authentication) throws Exception {
        StaffDTO user = (StaffDTO) authentication.getPrincipal();
        String messErro ="";
        String messSuscess = "";
        List<String> vlstTitleParams = Lists.newArrayList();
        if (null != mappingAreaProvinceDTO && !DataUtil.isNullOrEmpty(mappingAreaProvinceDTO.getLstPartner())) {
            //check ton tai  tinh
            for (ManageInfoPartnerDTO partnerDTO : mappingAreaProvinceDTO.getLstPartner()) {
                List<ManageInfoPartner> infoPartner = partnerService.findAllById(partnerDTO.getId());
                if (!DataUtil.isNullOrEmpty(infoPartner)) {
                    List<ManageInfoPartner> infoPartnerList = ((List<ManageInfoPartner>) infoPartner).stream().
                            filter(item -> DataUtil.safeEqual(item.getParentShopCode(), Constants.MANAGE_INFO_PARTNER.CHANNEL_TINH)).
                            filter(item -> DataUtil.safeEqual(item.getVdsChannelCode(), Constants.MANAGE_INFO_PARTNER.VDS_TINH)).
                            filter(item -> DataUtil.safeEqual(item.getStatus(), Constants.MANAGE_INFO_PARTNER.STATUS_ONE))
                            .collect(Collectors.toList());
                    //neu ton tai thi lu vao db
                    if (!DataUtil.isNullOrEmpty(infoPartnerList)) {
                        //check trong bang sal_mapping_area_province da ton tai hay chua
                        // neu ton tai roi ma ngay het han chua het thi k cho luu
                        // chua ton tai thi luu
                        // ton tai roi thi
                        List<Object[]> object = salaryMappingAreaProvinceRepo.checkByShopCode(infoPartnerList.get(0).getShopCode(), mappingAreaProvinceDTO.getParent(), mappingAreaProvinceDTO.getExpiredDate(),"insert");
                        if (DataUtil.isNullOrEmpty(object)) {
                            //bat try catch khi insert loi thi van cho tiep tuc chay for
                            try {
                                if(null != mappingAreaProvinceDTO.getExpiredDate()){
                                    Calendar calendar =  Calendar.getInstance();
                                    calendar.setTime(mappingAreaProvinceDTO.getExpiredDate());
                                    calendar.add(DATE,1);
                                    mappingAreaProvinceDTO.setEffectiveDate(calendar.getTime());
                                }
                                SalaryMappingAreaProvince salaryMappingAreaProvince = salaryMappingAreaProvinceRepo.createProvince(mappingAreaProvinceDTO, infoPartnerList.get(0), user);
                                if (salaryMappingAreaProvince == null) {
                                    //todo thong bao khong thanh cong tinh. ..
                                    messErro = messErro + partnerDTO.getShortName();
                                }else{
                                    //todo insert bang actionAudit
                                    createOrUpdateActionAudit("insert",null,null,user,salaryMappingAreaProvince);
                                    messSuscess = messSuscess +  partnerDTO.getShopName();
                                }
                            } catch (Exception ex) {
                                LOGGER.error(ex.getMessage(), ex);
                                messErro =messErro + partnerDTO.getShopName() + ", ";
                            }
                        } else {
                            // thong bao loi khong thanh cong
                            // thong bao insert loi
                            System.out.println(partnerDTO.getShopName());
                            messErro =messErro + partnerDTO.getShopName() + ", ";
                        }

                    }else {
                        // thong bao tinh k hop le
                        // thong bao insert loi
                        messErro = messErro + partnerDTO.getShopName();
                    }
                }else {
                    // thong bao tinh k hop le ?
                    // thong bao insert loi .
                    messErro = messErro + partnerDTO.getShopName();
                }
            }
        } else {
            // thong bao k co du lieu truyen vao khong du?
        }
        System.out.println(messErro);
        vlstTitleParams.add(messSuscess);
        vlstTitleParams.add(messErro);

        return ApiResponse.build(HttpServletResponse.SC_OK, false, I18N.get(Constants.SAL_MAPPING_AREA_PROVINCE.SHOW_MESSAGE, (String[]) vlstTitleParams.toArray(new String[vlstTitleParams.size()])), mappingAreaProvinceDTO);
    }
    // tim kiem
    @Override
    public ApiResponse searchTree(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO) throws Exception {
        List<OrganizationalStructureDTO> structureDTOList = new ArrayList<>();
       List<Object[]> objects = salaryMappingAreaProvinceRepo.searchTree(mappingAreaProvinceDTO);
       if(null==mappingAreaProvinceDTO){
           return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
       }else {
           if (DataUtil.isNullOrEmpty(mappingAreaProvinceDTO.getParent())){
               return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "chưa chọn vùng", null);
           }
       }
       if(!DataUtil.isNullOrEmpty(objects)){
           for (Object[] obj: objects){
               OrganizationalStructureDTO  structureDTO = new OrganizationalStructureDTO();
               structureDTO.setCode(DataUtil.safeToString(obj[0]));
               structureDTO.setName(DataUtil.safeToString(obj[1]));
               structureDTO.setParent(DataUtil.safeToString(obj[2]));
               structureDTO.setType(DataUtil.safeToString(obj[3]));
               structureDTO.setParentName(DataUtil.safeToString(obj[4]));
               structureDTO.setPartnerCode(DataUtil.safeToString(obj[5]));
               structureDTO.setPartnerName(DataUtil.safeToString(obj[6]));
               structureDTO.setShortName(DataUtil.safeToString(obj[7]));

               if(null ==obj[8] ){
                   //todo
               }else {
                   Timestamp timestamp = new Timestamp(DataUtil.safeToDate(obj[8]).getTime());
                   structureDTO.setExpiredDate(timestamp);
               }
               if(null!=obj[9]){
                   structureDTO.setEffectiveDate(DataUtil.safeToDate(obj[9]));
               }

               structureDTOList.add(structureDTO);

           }
       }else {
           return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
       }
        return ApiResponse.build(HttpServletResponse.SC_OK, false, "content is not found", structureDTOList);
    }
    // chuyen  vung
    @Override
    public ApiResponse changeArea(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO,Authentication authentication) throws Exception {
        StaffDTO user = (StaffDTO) authentication.getPrincipal();

        ManageInfoPartner manageInfoPartner = new ManageInfoPartner();
        java.util.Date vdtCurrentDate = new java.util.Date();
        Long vlngTime = vdtCurrentDate.getTime();
        Timestamp timestamp = new Timestamp(vlngTime);

        if(null== mappingAreaProvinceDTO){
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
        }else {
            // check kiem tra xem vung do da
            List<Object[]> object = salaryMappingAreaProvinceRepo.checkByShopCode(mappingAreaProvinceDTO.getCode(), mappingAreaProvinceDTO.getParent(), mappingAreaProvinceDTO.getExpiredDate(),"changeArea");
             //todo khi chuyen vung thuc thien update ban ghi cu
            if(!DataUtil.isNullOrEmpty(object)){
                if(object.size() >1){
                    return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, I18N.get("message.erro.system.db"), null);
                }
                SalaryMappingAreaProvinceDTO dtoOld =   getProvinceDTO(object.get(0));

                //neu ngay het han > ngay hien tai
                if(null!=mappingAreaProvinceDTO.getExpiredDate()){
                    if(mappingAreaProvinceDTO.getExpiredDate().getTime()>timestamp.getTime()){
                        return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, I18N.get("ngày hết hạn cũ phải nhở hơn hoặc bằng ngay hiện tại"), null);
                    }
                }
                //update
                 salaryMappingAreaProvinceRepo.updateProvince(user,mappingAreaProvinceDTO,"1");
                //todo luu actionAudit
                createActionAuditForChangeArea("updateChangeArea",dtoOld,mappingAreaProvinceDTO,null,user);
                //validate cho them moi
                if(null==mappingAreaProvinceDTO.getExpiredDateNew()){
                    //todo
                }else if(null!=mappingAreaProvinceDTO.getExpiredDateNew()){
                    if(mappingAreaProvinceDTO.getExpiredDateNew().getTime()<=mappingAreaProvinceDTO.getExpiredDate().getTime()){
                        return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, I18N.get("ngày hết hạn mới không được nhỏ hơn ngày hết hạn cũ"), null);
                    }
                }
                // insert them moi ban ghi
                //ngay hieu luc = nagy het han cu +1
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mappingAreaProvinceDTO.getExpiredDate());
                calendar.add(DATE,1);
                mappingAreaProvinceDTO.setEffectiveDate(calendar.getTime());
                manageInfoPartner.setShopCode(dtoOld.getShopCode());
                manageInfoPartner.setVdsChannelCode(dtoOld.getVdsChannelCode());
                SalaryMappingAreaProvince salaryMappingAreaProvince = salaryMappingAreaProvinceRepo.createProvince(mappingAreaProvinceDTO, manageInfoPartner, user);
                //todo luu actionAudit
                createActionAuditForChangeArea("insertChangeArea",null,null,salaryMappingAreaProvince,user);

                //todo luu actionAudit
            }else {
                return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, I18N.get("message.erro.notexist"), null);
            }


        }
        return ApiResponse.build(HttpServletResponse.SC_OK, false, I18N.get("common.servicescore.updatesuccess"), mappingAreaProvinceDTO);
    }
    // chuyen cum truong
    @Override
    public ApiResponse changeLeader(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO, Authentication authentication) throws Exception {
        //todo

        return null;
    }
    // them moi  nhan vien kinh doanh
    @Override
    public ApiResponse insertBusinessMan(SalaryMappingLeaderStaffDTO mappingLeaderStaffDTO, Authentication authentication) throws Exception {
        StaffDTO user = (StaffDTO) authentication.getPrincipal();
        Long messErro =0L ;
        Long messSuscess =0L;
        List<Long> vlstTitleParams = Lists.newArrayList();
        // lay ngay hien tai dang timestamp
        java.util.Date vdtCurrentDate = new java.util.Date();
        Long vlngTime = vdtCurrentDate.getTime();
        Timestamp timestamp = new Timestamp(vlngTime);
        if(null!=mappingLeaderStaffDTO && !DataUtil.isNullOrEmpty(mappingLeaderStaffDTO.getLstVdsStaffDTO())){
            // ngay het han phai lon hon ngay hien tai
            if(mappingLeaderStaffDTO.getExpiredDate()!=null){
               if(mappingLeaderStaffDTO.getExpiredDate().getTime()<timestamp.getTime()) {
                   return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, I18N.get("message.not.sysdate"), null);
               }
            }
            for(VdsStaffDTO vdsStaffDTO : mappingLeaderStaffDTO.getLstVdsStaffDTO()){
                // check ton tai nhan vien kinh doanh
              List <VdsStaffDTO>  staffDTOList =  staffService.getAllStaffBusiness(vdsStaffDTO.getId());
              // neu ton tai thi check tiep
              if(!DataUtil.isNullOrEmpty(staffDTOList)){
                    //todo thuc hien insert
                  try {
                      SalaryMappingLeaderStaff leaderStaff = salaryMappingAreaProvinceRepo.insertBusinessMan(mappingLeaderStaffDTO,user);
                      if(null!=leaderStaff){
                            // insert acction audit
                          createActionAuditForInsertBusinessMan(leaderStaff,user);
                          messSuscess =messSuscess++;
                      }else {
                          messErro = messErro++;
                      }
                  }catch (Exception ex){
                      LOGGER.error(ex.getMessage(),ex);
                      messErro = messErro++;
                  }

              }else {
                  // nhan vien khong ton tai
                  messErro = messErro++;
              }
            }

        }else {
            return ApiResponse.build(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "content is not found", null);
        }
        vlstTitleParams.add(messSuscess);
        vlstTitleParams.add(messErro);

        return ApiResponse.build(HttpServletResponse.SC_OK, false, I18N.get(Constants.SAL_MAPPING_AREA_PROVINCE.SHOW_MESSAGE, (String[]) vlstTitleParams.toArray(new String[vlstTitleParams.size()])), mappingLeaderStaffDTO);
    }
    //ghi log khi them nhan vien
    private  void createActionAuditForInsertBusinessMan(SalaryMappingLeaderStaff leaderStaff,StaffDTO user) throws Exception{
        long vlngDateTime = System.currentTimeMillis();
        java.sql.Date vdtDateTime = new Date(vlngDateTime);
        ActionAudit actionAudit = new ActionAudit();
        actionAudit.setActionCode(Constants.INSERT);
        actionAudit.setActionDateTime(vdtDateTime);
        actionAudit.setPkID(new Long(leaderStaff.getId()));
        actionAudit.setObjectCode(Constants.SAL_MAPPING_LEAR_STAFF.NAME_TABLE);
        actionAudit.setUser(user.getStaffCode());
        actionAudit.setShopCode(user.getShopCode());
        actionAuditRepo.save(actionAudit);
        //luu action detail
        actionDetailService.createActionDetail(Constants.SAL_MAPPING_LEAR_STAFF.STAFF_TYPE, leaderStaff.getStaffType(), actionAudit.getId(), leaderStaff.getStaffType().trim());
        actionDetailService.createActionDetail(Constants.SAL_MAPPING_LEAR_STAFF.STAFF_CODE, leaderStaff.getStaffType(), actionAudit.getId(), leaderStaff.getStaffCode().trim());
        actionDetailService.createActionDetail(Constants.SAL_MAPPING_LEAR_STAFF.UPDATED_DATE,new java.util.Date().toString() , actionAudit.getId(), new java.util.Date().toString());
        actionDetailService.createActionDetail(Constants.SAL_MAPPING_LEAR_STAFF.UPDATED_USER, user.getStaffCode(), actionAudit.getId(), user.getStaffCode());
        actionDetailService.createActionDetail(Constants.SAL_MAPPING_LEAR_STAFF.SHOP_CODE, leaderStaff.getShopCode(), actionAudit.getId(), leaderStaff.getShopCode());
        actionDetailService.createActionDetail(Constants.SAL_MAPPING_LEAR_STAFF.EFFECTIVE_DATE, leaderStaff.getEffectiveDate().toString(), actionAudit.getId(), leaderStaff.getEffectiveDate().toString());
    }



    // ghi log cho ham changeArea
    private  void createActionAuditForChangeArea(String type,SalaryMappingAreaProvinceDTO updateOld,SalaryMappingAreaProvinceDTO areaProvinceDTOUpdate,SalaryMappingAreaProvince newInsert, StaffDTO user) throws Exception{
        long vlngDateTime = System.currentTimeMillis();
        java.sql.Date vdtDateTime = new Date(vlngDateTime);
        ActionAudit actionAudit = new ActionAudit();
        if (DataUtil.safeEqual(type,"updateChangeArea")){
            actionAudit.setActionCode(Constants.EDIT);
            actionAudit.setActionDateTime(vdtDateTime);
            actionAudit.setPkID(new Long(updateOld.getId()));
            actionAudit.setObjectCode(Constants.SAL_MAPPING_AREA_PROVINCE.NAME_TABLE);
            actionAudit.setUser(user.getStaffCode());
            actionAudit.setShopCode(user.getShopCode());
        }else if(DataUtil.safeEqual(type,"insertChangeArea")){
            actionAudit.setActionCode(Constants.INSERT);
            actionAudit.setActionDateTime(vdtDateTime);
            actionAudit.setPkID(new Long(newInsert.getId()));
            actionAudit.setObjectCode(Constants.SAL_MAPPING_AREA_PROVINCE.NAME_TABLE);
            actionAudit.setUser(user.getStaffCode());
            actionAudit.setShopCode(user.getShopCode());
        }
        actionAuditRepo.save(actionAudit);
        if (DataUtil.safeEqual(type,"updateChangeArea")){
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.UPDATED_USER, user.getStaffCode(), actionAudit.getId(), updateOld.getUpdatedUser());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.UPDATED_DATE,new java.util.Date().toString() , actionAudit.getId(), updateOld.getUpdatedDate()== null ? null : updateOld.getUpdatedDate().toString());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.EXPIRED_DATE,areaProvinceDTOUpdate.getExpiredDate().toString().toString(), actionAudit.getId(), updateOld.getExpiredDate()==null ? null :updateOld.getExpiredDate().toString());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.STATUS,"1" , actionAudit.getId(), updateOld.getStatus());
        }else if(DataUtil.safeEqual(type,"insertChangeArea")){
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.AREA_CODE, newInsert.getAreaCode(), actionAudit.getId(), newInsert.getAreaCode());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.UPDATED_DATE,new java.util.Date().toString() , actionAudit.getId(), new java.util.Date().toString());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.UPDATED_USER, user.getStaffCode(), actionAudit.getId(), user.getStaffCode());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.SHOP_CODE, newInsert.getShopCode(), actionAudit.getId(), newInsert.getShopCode());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.EFFECTIVE_DATE, newInsert.getEffectiveDate().toString(), actionAudit.getId(), newInsert.getEffectiveDate() == null ? null :newInsert.getEffectiveDate().toString());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.EXPIRED_DATE,newInsert.getExpiredDate() ==null ? null:  newInsert.getExpiredDate().toString(), actionAudit.getId(), newInsert.getExpiredDate()== null ? null :newInsert.getExpiredDate().toString());
        }
    }



    //ham insert or update ActionAudit
    private void createOrUpdateActionAudit(String type,SalaryMappingAreaProvinceDTO dtoOld,SalaryMappingAreaProvinceDTO  newUpdateAreaProvinceDTO,StaffDTO user,SalaryMappingAreaProvince newInsert) throws Exception{
        long vlngDateTime = System.currentTimeMillis();
        java.sql.Date vdtDateTime = new Date(vlngDateTime);
        ActionAudit actionAudit = new ActionAudit();
        if(DataUtil.safeEqual("update",type)){
            actionAudit.setActionCode(Constants.EDIT);
            actionAudit.setActionDateTime(vdtDateTime);
            actionAudit.setPkID(new Long(dtoOld.getId()));
            actionAudit.setObjectCode(Constants.SAL_MAPPING_AREA_PROVINCE.NAME_TABLE);
            actionAudit.setUser(user.getStaffCode());
            actionAudit.setShopCode(user.getShopCode());
        }else if(DataUtil.safeEqual("insert",type)){
            actionAudit.setActionCode(Constants.INSERT);
            actionAudit.setActionDateTime(vdtDateTime);
            actionAudit.setPkID(new Long(newInsert.getId()));
            actionAudit.setObjectCode(Constants.SAL_MAPPING_AREA_PROVINCE.NAME_TABLE);
            actionAudit.setUser(user.getStaffCode());
            actionAudit.setShopCode(user.getShopCode());
        }

        actionAuditRepo.save(actionAudit);
        if(DataUtil.safeEqual("update",type)){
            //luu action detail
//                 actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.AREA_CODE, areaProvinceDTO.getAreaCode(), actionAudit.getId(), dtoOld.getAreaCode().trim());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.UPDATED_DATE,dtoOld.getUpdatedDate().toString() , actionAudit.getId(), new java.util.Date().toString());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.UPDATED_USER, user.getStaffCode(), actionAudit.getId(), dtoOld.getUpdatedUser());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.SHOP_CODE, newUpdateAreaProvinceDTO.getShopCode(), actionAudit.getId(), dtoOld.getShopCode());
        }else if(DataUtil.safeEqual("insert",type)){
            //luu action detail
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.AREA_CODE, newInsert.getAreaCode(), actionAudit.getId(), newInsert.getAreaCode().trim());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.UPDATED_DATE,new java.util.Date().toString() , actionAudit.getId(), newInsert.getUpdatedDate().toString());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.UPDATED_USER, user.getStaffCode(), actionAudit.getId(), user.getStaffCode());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.SHOP_CODE, newInsert.getShopCode(), actionAudit.getId(), newInsert.getShopCode());
            actionDetailService.createActionDetail(Constants.SAL_MAPPING_AREA_PROVINCE.EFFECTIVE_DATE, newInsert.getEffectiveDate().toString(), actionAudit.getId(), newInsert.getEffectiveDate().toString());
        }


    }



    private SalaryMappingAreaProvinceDTO getProvinceDTO(Object[] object){
        SalaryMappingAreaProvinceDTO areaProvinceDTO = new SalaryMappingAreaProvinceDTO();
        if(null!=object){
            areaProvinceDTO.setId(DataUtil.safeToInt(object[0]));
            areaProvinceDTO.setAreaCode(DataUtil.safeToString(object[1]));
            areaProvinceDTO.setShopCode(DataUtil.safeToString(object[2]));
            areaProvinceDTO.setVdsChannelCode(DataUtil.safeToString(object[3]));
            if(null!=object[4]){
                Timestamp timestamp = new Timestamp(DataUtil.safeToDate(object[4]).getTime());
                areaProvinceDTO.setExpiredDate(timestamp);
            }else {
                areaProvinceDTO.setExpiredDate(null);
            }
            areaProvinceDTO.setCreatedDate(DataUtil.safeToDate(object[5]));
            areaProvinceDTO.setCreatedUser(DataUtil.safeToString(object[6]));
            areaProvinceDTO.setUpdatedDate(DataUtil.safeToDate(object[7]));
            areaProvinceDTO.setUpdatedUser(DataUtil.safeToString(object[8]));
        }
        return areaProvinceDTO;
    }


}
