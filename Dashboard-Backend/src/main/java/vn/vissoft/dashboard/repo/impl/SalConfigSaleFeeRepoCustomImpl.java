package vn.vissoft.dashboard.repo.impl;

import org.springframework.stereotype.Repository;
import vn.vissoft.dashboard.helper.DataUtil;
import vn.vissoft.dashboard.model.SalConfigSaleFee;
import vn.vissoft.dashboard.repo.SalConfigSaleFeeRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;

@Repository
public class SalConfigSaleFeeRepoCustomImpl implements SalConfigSaleFeeRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<SalConfigSaleFee> findSalConfigSaleFeeByCondition(String feeName, String receiveFrom) {

        HashMap salConfigSaleFeeMapParam = new HashMap();
        StringBuilder scsfSqlBuilder = new StringBuilder();
        scsfSqlBuilder.append("SELECT s FROM SalConfigSaleFee s JOIN SaleFee sf ON s.feeId = sf.id");
        scsfSqlBuilder.append(" WHERE s.status = 1 AND sf.status = 1 ");
        if (!DataUtil.isNullOrEmpty(feeName)) {
            scsfSqlBuilder.append(" AND sf.name LIKE :feeName ");
            salConfigSaleFeeMapParam.put("feeName", "%" + feeName+ "%");
        }
        if (!DataUtil.isNullOrEmpty(receiveFrom) && !receiveFrom.equalsIgnoreCase("null")) {
            scsfSqlBuilder.append(" AND s.receiveFrom = :receiveFrom ");
            salConfigSaleFeeMapParam.put("receiveFrom", receiveFrom);
        }
            Query query = entityManager.createQuery(scsfSqlBuilder.toString());
        salConfigSaleFeeMapParam.forEach((k, v) -> {
            query.setParameter(k.toString(), v);
        });
        return query.getResultList();
    }
}
