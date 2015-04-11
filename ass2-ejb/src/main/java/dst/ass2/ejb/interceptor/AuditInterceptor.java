package dst.ass2.ejb.interceptor;

import dst.ass1.jpa.dao.DAOFactory;
import dst.ass1.jpa.model.ModelFactory;
import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;

import javax.annotation.PostConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

public class AuditInterceptor {

    @PersistenceContext(name = "dst")
    private EntityManager em;

    private ModelFactory modelFactory = new ModelFactory();


    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {

        IAuditLog auditLog = modelFactory.createAuditLog();
        auditLog.setInvocationTime(new Date());
        auditLog.setMethod(invocationContext.getMethod().getName());

        Object result = null;

        Exception exception = null;
        try {
            result = invocationContext.proceed();
            auditLog.setResult(result != null ? result.toString(): null);
        } catch (Exception ex) {
            auditLog.setResult(ex.toString());
            exception = ex;
        }

        int index = 0;
        for (Object parameter: invocationContext.getParameters()) {

            IAuditParameter auditParameter = modelFactory.createAuditParameter();
            auditParameter.setParameterIndex(index);
            auditParameter.setType(parameter != null ? parameter.getClass().getCanonicalName() : "null");
            auditParameter.setValue(parameter != null ? parameter.toString() : "null");
            auditParameter.setAuditLog(auditLog);

            auditLog.getParameters().add(auditParameter);
            index++;
        }

        em.persist(auditLog);
        if (exception != null) {
            throw exception;
        }

        return result;
    }
}
