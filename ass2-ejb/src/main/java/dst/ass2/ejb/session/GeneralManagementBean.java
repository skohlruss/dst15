package dst.ass2.ejb.session;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

import dst.ass2.ejb.dto.AuditLogDTO;
import dst.ass2.ejb.dto.BillDTO;

import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.session.interfaces.IGeneralManagementBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class GeneralManagementBean implements IGeneralManagementBean {

    @EJB
    private IPriceManagementBean priceManagementBean;
    @PersistenceContext(name = "dst")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        System.out.println("qq\n\n\n");
    }

    @Override
    public void addPrice(Integer nrOfHistoricalLectures, BigDecimal price) {
        priceManagementBean.setPrice(nrOfHistoricalLectures, price);
    }


    @Override
    public Future<BillDTO> getBillForLecturer(String lecturerName) throws Exception {
        // TODO
        return null;
    }

    @Override
    public List<AuditLogDTO> getAuditLogs() {
        // TODO
        return null;
    }

    @Override
    public void clearPriceCache() {
        priceManagementBean.clearCache();
    }
}
