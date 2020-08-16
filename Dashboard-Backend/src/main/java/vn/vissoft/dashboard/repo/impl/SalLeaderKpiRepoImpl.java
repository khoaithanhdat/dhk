package vn.vissoft.dashboard.repo.impl;

import vn.vissoft.dashboard.dto.StaffDTO;
import vn.vissoft.dashboard.dto.excel.EliminateNumberExcel;
import vn.vissoft.dashboard.dto.excel.KpiActionProgramExcel;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.model.SalLeaderKpi;
import vn.vissoft.dashboard.repo.SalLeaderKpiRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;

public class SalLeaderKpiRepoImpl implements SalLeaderKpiRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public SalLeaderKpi getLeaderKpiFromPlan(KpiActionProgramExcel kpiActionProgramExcel, StaffDTO staffDTO,Long prdId) throws Exception {
        HashMap mapParams = new HashMap();
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("select p from SalLeaderKpi p");
        sqlBuilder.append(buildSQLFromPlanKpi(kpiActionProgramExcel, mapParams,prdId));

        Query query = entityManager.createQuery(sqlBuilder.toString());
        mapParams.forEach((k, v) -> {
            query.setParameter(k.toString(), v);
        });
        List<SalLeaderKpi> lstLeaderKpis = query.getResultList();
        if (DataUtil.isNullOrEmpty(lstLeaderKpis)) return null;
        return lstLeaderKpis.get(0);
    }

    private StringBuilder buildSQLFromPlanKpi(KpiActionProgramExcel kpiActionProgramExcel, HashMap params,Long prdId) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" where 1=1 and p.vdsChannelCode = 'VDS_TINH' ");

        if(!DataUtil.isNullOrZero(prdId)){
            Integer intPrdId= Math.toIntExact(DataUtil.convertToPrdId(prdId));
            stringBuilder.append(" and p.prdId = :prdId ");
            params.put("prdId",intPrdId);
        }
        if (!DataUtil.isNullOrEmpty(kpiActionProgramExcel.getShopCode())) {
            stringBuilder.append(" and p.shopCode = :shopCode ");
            params.put("shopCode", kpiActionProgramExcel.getShopCode().trim());
        }

        if (!DataUtil.isNullOrEmpty(kpiActionProgramExcel.getStaffCode())) {
            stringBuilder.append(" and p.leaderCode = :leaderCode ");
            params.put("leaderCode", kpiActionProgramExcel.getStaffCode().trim());

        }

        return stringBuilder;
    }
}
