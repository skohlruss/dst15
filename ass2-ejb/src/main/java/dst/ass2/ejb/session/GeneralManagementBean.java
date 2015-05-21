package dst.ass2.ejb.session;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import dst.ass1.jpa.dao.*;
import dst.ass1.jpa.model.*;
import dst.ass2.ejb.dao.IAuditLogDAO;
import dst.ass2.ejb.dto.AuditLogDTO;
import dst.ass2.ejb.dto.AuditParameterDTO;
import dst.ass2.ejb.dto.BillDTO;

import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;
import dst.ass2.ejb.session.interfaces.IGeneralManagementBean;

import javax.annotation.PostConstruct;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Cannot be remote because - AsyncResult<> is not serializable
 */
//@Remote(IGeneralManagementBean.class)
@Stateless
public class GeneralManagementBean implements IGeneralManagementBean {

    @EJB
    private IPriceManagementBean priceManagementBean;
    @PersistenceContext(name = "dst")
    private EntityManager em;

    private IAuditLogDAO auditLogDAO;
    private ILectureDAO lectureDAO;
    private IMembershipDAO membershipDAO;
    private ILecturerDAO lecturerDAO;



    @PostConstruct
    public void postConstruct() {
        DAOFactory daoFactory = new DAOFactory(em);
        this.auditLogDAO = daoFactory.getAuditLogDAO();
        this.lectureDAO = daoFactory.getLectureDAO();
        this.membershipDAO = daoFactory.getMembershipDAO();
        this.lecturerDAO = daoFactory.getLecturerDAO();
    }

    @Override
    public void addPrice(Integer nrOfHistoricalLectures, BigDecimal price) {
        priceManagementBean.setPrice(nrOfHistoricalLectures, price);
    }

    @Override
    @Asynchronous
    public Future<BillDTO> getBillForLecturer(String lecturerName) throws Exception {

        ILecturer lecturer = lecturerDAO.findByName(lecturerName).get(0);

        BillDTO billDTO = new BillDTO();
        billDTO.setLecturerName(lecturerName);

        List<ILecture> lectures = lectureDAO.findNotPayedLectures(lecturerName);
        List<BillDTO.BillPerLecture> billPerLectures = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        for (ILecture lecture: lectures) {
            /**
             * get discount
             */
            IClassroom classroom = lecture.getLectureStreaming().getClassrooms().get(0);
            IMOCPlatform platform = classroom.getVirtualSchool().getMOCPlatform();
            IMembership membership = membershipDAO.findByLecturerAndPlatform(lecturer, platform).get(0);

            BigDecimal discount = new BigDecimal(1 - membership.getDiscount());
            Integer numberOfPayedLectures = lectureDAO.findNumberOfPayedLectures(lecturerName);

            /**
             * Prices
             */
            BigDecimal setupCost = priceManagementBean.getPrice(numberOfPayedLectures).multiply(discount);
            BigDecimal streamingCost = platform.getCostsPerStudent().multiply(new BigDecimal(lecture.getStreamingTime()));
            streamingCost = streamingCost.multiply(discount);
            BigDecimal lectureCost = setupCost.add(streamingCost);

            BillDTO.BillPerLecture bill = billDTO.new BillPerLecture();
            bill.setLectureId(lecture.getId());
            bill.setNumberOfClassrooms(lecture.getLectureStreaming().getClassrooms().size());

            bill.setSetupCosts(setupCost.setScale(2, BigDecimal.ROUND_HALF_UP));
            bill.setStreamingCosts(streamingCost.setScale(2, BigDecimal.ROUND_HALF_UP));
            bill.setLectureCosts(lectureCost.setScale(2, BigDecimal.ROUND_HALF_UP));


            billPerLectures.add(bill);
            totalPrice = totalPrice.add(bill.getLectureCosts());
            lecture.setPaid(true);
            em.flush();
        }


        billDTO.setBills(billPerLectures);
        billDTO.setTotalPrice(totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));

        AsyncResult<BillDTO> result = new AsyncResult<>(billDTO);
        return result;
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
