package vn.vissoft.dashboard.repo.impl;

import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.EliminateNumberExcel;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.model.SalStaffMeasureImport;
import vn.vissoft.dashboard.repo.SalStaffMeasureImportRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;

public class SalStaffMeasureImportRepoImpl implements SalStaffMeasureImportRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public SalStaffMeasureImport getStaffMeasureFromPlan(EliminateNumberExcel eliminateNumberExcel, StaffDTO staffDTO, Long prdId,Long serviceId) throws Exception {
        HashMap mapParams = new HashMap();
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("select p from SalStaffMeasureImport p");
        sqlBuilder.append(buildSQLFromPlanKpi(eliminateNumberExcel, mapParams,prdId,serviceId));

        Query query = entityManager.createQuery(sqlBuilder.toString());
        mapParams.forEach((k, v) -> {
            query.setParameter(k.toString(), v);
        });
        List<SalStaffMeasureImport> lstStaffMesures = query.getResultList();
        if (DataUtil.isNullOrEmpty(lstStaffMesures)) return null;
        return lstStaffMesures.get(0);
    }

    private StringBuilder buildSQLFromPlanKpi(EliminateNumberExcel eliminateNumberExcel, HashMap params,Long prdId,Long serviceId) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" where 1=1 and p.vdsChannelCode = 'VDS_TINH' ");

        if(!DataUtil.isNullOrZero(prdId)){
            Integer intPrdId= Math.toIntExact(DataUtil.convertToPrdId(prdId));
            stringBuilder.append(" and p.prdId = :prdId ");
            params.put("prdId",intPrdId);
        }
        if (!DataUtil.isNullOrEmpty(eliminateNumberExcel.getShopCode())) {
            stringBuilder.append(" and p.shopCode = :shopCode ");
            params.put("shopCode", eliminateNumberExcel.getShopCode().trim());
        }

        if (!DataUtil.isNullOrEmpty(eliminateNumberExcel.getStaffCode())) {
            stringBuilder.append(" and p.staffCode = :staffCode ");
            params.put("staffCode", eliminateNumberExcel.getStaffCode().trim());
        }

        if(!DataUtil.isNullOrZero(serviceId)){
            stringBuilder.append(" and p.serviceId = :serviceId");
            params.put("serviceId",Math.toIntExact(serviceId));
        }

        return stringBuilder;
    }
}
