package vn.vissoft.dashboard.repo.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import vn.vissoft.dashboard.dto.*;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.model.ManageInfoPartner;
import vn.vissoft.dashboard.model.SalaryMappingAreaProvince;
import vn.vissoft.dashboard.model.SalaryMappingLeaderStaff;
import vn.vissoft.dashboard.repo.DashboardRepoCustom;
import vn.vissoft.dashboard.repo.SalaryMappingAreaProvinceRepo;
import vn.vissoft.dashboard.repo.SalaryMappingAreaProvinceRepoCustom;
import vn.vissoft.dashboard.repo.SalaryMappingLeaderStaffRepo;
import vn.vissoft.dashboard.services.impl.TopDataServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SalaryMappingAreaProvinceRepoImpl implements SalaryMappingAreaProvinceRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private DashboardRepoCustom dashboardRepoCustom;
    @Autowired
    private SalaryMappingAreaProvinceRepo salaryMappingAreaProvinceRepo;
    @Autowired
    private SalaryMappingLeaderStaffRepo salaryMappingLeaderStaffRepo;

    private static final Logger LOGGER = LogManager.getLogger(TopDataServiceImpl.class);

    /**
     * xay dung cay tinh luong dashboard
     *
     * @return
     * @throws Exception
     * @author AnhNQ
     * @version 1.0
     * @since 16/07/2020
     */
    @Override
    public List<OrganizationalStructureDTO> getOrganizationalStructure() throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select CODE,NAME,parent,type,parent_name,partner_code,partner_name,short_name,CAST(expired_date AS DATETIME) as expired_date from v_organizational_structure");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        return setOrganizationalStructureDTO(query.getResultList());
    }
    /**
     * lay ra danh sach tinh
     *
     * @return
     * @throws Exception
     * @author phucnv
     * @version 1.0
     * @since 16/07/2020
     */
    @Override
    public List<Object[]> getLstMappingAreaProvice(String provinceCode, String provinceName,String areaCode) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" SELECT sl.shop_code,m.short_name,m.shop_name,sa.area_name,sl.expired_date , ");
        sqlBuilder.append(" sl.vds_channel_code,sl.id");
        sqlBuilder.append(" FROM sal_mapping_area_province sl , manage_info_partner m , sal_area sa ");
        sqlBuilder.append(" WHERE sl.shop_code =m.shop_code AND sa.area_code =  sl.area_code ");
        if(!DataUtil.isNullOrEmpty(provinceCode)){
            sqlBuilder.append(" AND sl.shop_code like :provinceCode ");
        }
        if(!DataUtil.isNullOrEmpty(provinceName)){
            sqlBuilder.append(" AND m.shop_name like :provinceName");
        }
        sqlBuilder.append(" AND sa.area_code =:areaCode");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        if(!DataUtil.isNullOrEmpty(provinceCode)){
            query.setParameter("provinceCode","%" +provinceCode+ "%");
        }
        if(!DataUtil.isNullOrEmpty(provinceName)){
            query.setParameter("provinceName","%" +provinceName+ "%");
        }
        query.setParameter("areaCode",areaCode.trim());
        List<Object[]> lstAreaProvice = query.getResultList();
        return lstAreaProvice;
    }

    @Override
    public String updateProvince( StaffDTO user,SalaryMappingAreaProvinceDTO newProvinceDTO,String status) throws Exception {
        StringBuffer sqlBuilder = new StringBuffer();
        Date date = new Date();
        if(null!= newProvinceDTO.getExpiredDate()){
            Long time = newProvinceDTO.getExpiredDate().getTime();
            date.setTime(time);
        }
        int result;
        sqlBuilder.append(" update sal_mapping_area_province  ");
        sqlBuilder.append(" set expired_date=:expiredDate,  ");
//        if(!DataUtil.isNullOrEmpty(newProvinceDTO.getParent())){
//            sqlBuilder.append("  area_code=:areaCode,  ");
//        }
        sqlBuilder.append("  updated_date=SYSDATE(),  ");
        sqlBuilder.append("  updated_user=:updateUser  ");
        if(!DataUtil.isNullOrEmpty(status)){
            sqlBuilder.append("  ,status=:status  ");
        }
        sqlBuilder.append(" where shop_code =:shopCode  ");
        sqlBuilder.append(" and status =0 ");

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        if(null==newProvinceDTO.getExpiredDate()){
            query.setParameter("expiredDate",null);
        }else {
            query.setParameter("expiredDate",date);
        }
//        if(!DataUtil.isNullOrEmpty(newProvinceDTO.getParent())){
//          query.setParameter("areaCode",newProvinceDTO.getParent());
//        }

        query.setParameter("updateUser",user.getStaffCode());
        query.setParameter("shopCode",newProvinceDTO.getCode());
        if(!DataUtil.isNullOrEmpty(status)){
            query.setParameter("status", status);
        }
        result = query.executeUpdate();
        return String.valueOf(result);
    }

    @Override
    public List<Object[]> checkByShopCode(String shopCode,String areaCode, Timestamp expiredDate,String type) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append( "select id ,area_code ,shop_code, " );
        stringBuffer.append( "  vds_channel_code,expired_date, " );
        stringBuffer.append( "  created_date,created_user, " );
        stringBuffer.append( "  updated_date ,updated_user  " );
        stringBuffer.append( " from  sal_mapping_area_province  " );
        stringBuffer.append(" where shop_code=:shopCode ");
        // neu type la changeArea (ma hinh chuuyen) khong lam gi ca
        if(DataUtil.safeEqual("changeArea",type) || DataUtil.safeEqual("update",type)){
            //todo
        }else {
            if(!DataUtil.isNullOrEmpty(Collections.singleton(expiredDate))){
                stringBuffer.append(" and (UNIX_TIMESTAMP(expired_date) >  UNIX_TIMESTAMP(NOW()) or expired_date is null) ");
            }
        }

        stringBuffer.append(" and STATUS = 0 ");
        Query query = entityManager.createNativeQuery(stringBuffer.toString());

        query.setParameter("shopCode", shopCode);

        List<Object[]> objects = query.getResultList();

        return objects;
    }
    @Transactional
    @Override
    public SalaryMappingAreaProvince createProvince(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO, ManageInfoPartner infoPartner, StaffDTO user) throws Exception {

        SalaryMappingAreaProvince mappingAreaProvince = new SalaryMappingAreaProvince();
        mappingAreaProvince.setAreaCode(mappingAreaProvinceDTO.getParent());
        mappingAreaProvince.setVdsChannelCode(infoPartner.getVdsChannelCode());
        if(null!=mappingAreaProvince.getExpiredDate()){
            mappingAreaProvince.setExpiredDate(mappingAreaProvinceDTO.getExpiredDate());
        }
        mappingAreaProvince.setCreatedDate(new Date());
        mappingAreaProvince.setCreatedUser(user.getStaffCode());
        mappingAreaProvince.setUpdatedUser(user.getStaffCode());
        mappingAreaProvince.setUpdatedDate(new Date());
        mappingAreaProvince.setShopCode(infoPartner.getShopCode());
        mappingAreaProvince.setEffectiveDate(mappingAreaProvinceDTO.getEffectiveDate());
        mappingAreaProvince.setStatus("0");


        return salaryMappingAreaProvinceRepo.save(mappingAreaProvince);
    }
// tim kiem
    @Override
    public List<Object[]> searchTree(SalaryMappingAreaProvinceDTO mappingAreaProvinceDTO) throws Exception {
        StringBuffer sqlBuilder = new StringBuffer();
        sqlBuilder.append(" SELECT CODE,NAME,parent,type,parent_name, ");
        sqlBuilder.append(" partner_code,partner_name,short_name, ");
        sqlBuilder.append(" CAST(expired_date AS DATETIME) as expired_date, ");
        sqlBuilder.append(" NOW() AS Effective_Date ");
        sqlBuilder.append("  FROM v_organizational_structure   ");
        sqlBuilder.append("  WHERE parent =:parent  ");
        if(!DataUtil.isNullOrEmpty(mappingAreaProvinceDTO.getCode())){
            sqlBuilder.append("  AND  code  like :code ");
        }
        if(!DataUtil.isNullOrEmpty(mappingAreaProvinceDTO.getName())){
            sqlBuilder.append(" AND name like :name ");
        }
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        query.setParameter("parent",mappingAreaProvinceDTO.getParent());
        if(!DataUtil.isNullOrEmpty(mappingAreaProvinceDTO.getCode())){
            query.setParameter("code","%"+mappingAreaProvinceDTO.getCode()+"%");
        }
        if(!DataUtil.isNullOrEmpty(mappingAreaProvinceDTO.getName())){
            query.setParameter("name","%"+mappingAreaProvinceDTO.getName()+"%");
        }

        List<Object[]> objects =  query.getResultList();
        return objects;
    }
// them moi nhan vien kinh doanh
    @Transactional
    @Override
    public SalaryMappingLeaderStaff insertBusinessMan(SalaryMappingLeaderStaffDTO mappingLeaderStaffDTO,StaffDTO user) throws Exception {
        SalaryMappingLeaderStaff leaderStaff = new SalaryMappingLeaderStaff();
        leaderStaff.setCreatedUser(user.getStaffCode());
        leaderStaff.setUpdatedUser(user.getStaffCode());
        leaderStaff.setUpdatedDate(mappingLeaderStaffDTO.getUpdatedDate());
        leaderStaff.setCreatedDate(mappingLeaderStaffDTO.getCreatedDate());
        leaderStaff.setEffectiveDate(mappingLeaderStaffDTO.getEffectiveDate());
        leaderStaff.setExpiredDate(mappingLeaderStaffDTO.getExpiredDate());
        leaderStaff.setVdsChannelCode(mappingLeaderStaffDTO.getVdsChannelCode());
        leaderStaff.setShopCode(mappingLeaderStaffDTO.getShopCode());
        leaderStaff.setStaffCode(mappingLeaderStaffDTO.getStaffCode());

        return   salaryMappingLeaderStaffRepo.save(leaderStaff);
    }


    private List<OrganizationalStructureDTO> setOrganizationalStructureDTO(List<Object[]> objects) throws ParseException {
        List<OrganizationalStructureDTO> OrganizationalStructures = null;
        if (!DataUtil.isNullOrEmpty(objects)) {
            OrganizationalStructures = new ArrayList<>();
            for (Object[] object : objects) {
                OrganizationalStructureDTO organizationalStructureDTO = new OrganizationalStructureDTO();
                organizationalStructureDTO.setCode(DataUtil.safeToString(object[0]));
                organizationalStructureDTO.setName(DataUtil.safeToString(object[1]));
                organizationalStructureDTO.setParent(DataUtil.safeToString(object[2]));
                organizationalStructureDTO.setType(DataUtil.safeToString(object[3]));
                organizationalStructureDTO.setParentName(DataUtil.safeToString(object[4]));
                organizationalStructureDTO.setPartnerCode(DataUtil.safeToString(object[5]));
                organizationalStructureDTO.setPartnerName(DataUtil.safeToString(object[6]));
                organizationalStructureDTO.setShortName(DataUtil.safeToString(object[7]));

                if(null ==object[8] ){
                    //todo
                }else {
                    Timestamp timestamp = new Timestamp(DataUtil.safeToDate(object[8]).getTime());
                    organizationalStructureDTO.setExpiredDate(timestamp);
                }

                OrganizationalStructures.add(organizationalStructureDTO);
            }
        }
        return OrganizationalStructures;
    }




}
