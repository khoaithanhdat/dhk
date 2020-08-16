package vn.vissoft.dashboard.repo.impl;

import vn.vissoft.dashboard.dto.ConfigSaleAreaSalaryDTO;
import vn.vissoft.dashboard.helper.ApiResponse;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.model.SalaryTime;
import vn.vissoft.dashboard.repo.SalaryTimeRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class SalaryTimeRepoImpl implements SalaryTimeRepoCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public List<Object[]> findByAreaSalaryId(Integer areaSalaryId, String status) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select s.*,case when s.expired_month is null then 1  else 2 end lv from salary_time s ")
                .append("where s.sal_area_salary_id =:areaSalaryId and s.status =:status ")
                .append("order by lv,s.expired_month desc");

        Query query = entityManager.createNativeQuery(stringBuilder.toString());
        if (!DataUtil.isNullOrZero(areaSalaryId))
            query.setParameter("areaSalaryId", areaSalaryId);
        if (!DataUtil.isNullOrEmpty(status))
            query.setParameter("status", status);

        return query.getResultList();
    }
}
