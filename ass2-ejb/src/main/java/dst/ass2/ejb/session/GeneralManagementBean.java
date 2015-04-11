package dst.ass2.ejb.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass2.ejb.dao.IAuditLogDAO;
import dst.ass2.ejb.dto.AuditLogDTO;
import dst.ass2.ejb.dto.AuditParameterDTO;
import dst.ass2.ejb.dto.BillDTO;

import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;
import dst.ass2.ejb.model.impl.AuditParameter;
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

    private DAOFactory daoFactory;
    private IAuditLogDAO auditLogDAO;


    @PostConstruct
    public void postConstruct() {
        this.daoFactory = new DAOFactory(em);
        this.auditLogDAO = daoFactory.getAuditLogDAO();
    }

    @Override
    public void addPrice(Integer nrOfHistoricalLectures, BigDecimal price) {
        priceManagementBean.setPrice(nrOfHistoricalLectures, price);
    }

    @Override
    public Future<BillDTO> getBillForLecturer(String lecturerName) throws Exception {
        return null;
    }

    @Override
    public List<AuditLogDTO> getAuditLogs() {

        List<IAuditLog> auditLogs = auditLogDAO.findAll();
        List<AuditLogDTO> auditLogDTOs = new ArrayList<>();
        for (IAuditLog auditLog: auditLogs) {

            List<AuditParameterDTO> parameterDTOs = new ArrayList<>();
            for (IAuditParameter parameter: auditLog.getParameters()) {
                AuditParameterDTO parameterDTO = new AuditParameterDTO(
                        parameter.getId(),
                        parameter.getParameterIndex(),
                        parameter.getType(),
                        parameter.getValue());

                parameterDTOs.add(parameterDTO);
            }

            AuditLogDTO auditLogDTO = new AuditLogDTO(
                    auditLog.getId(),
                    auditLog.getMethod(),
                    auditLog.getResult(),
                    auditLog.getInvocationTime(),
                    parameterDTOs);
            auditLogDTOs.add(auditLogDTO);
        }

        return auditLogDTOs;
    }

    @Override
    public void clearPriceCache() {
        priceManagementBean.clearCache();
    }
}
