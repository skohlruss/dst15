package dst.ass2.ejb.model.impl;

import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

/**
 * Created by pavol on 7.4.2015.
 */
@Entity
public class AuditLog implements IAuditLog {

    @Id
    @GeneratedValue
    private Long id;
    private String method;
    private String result;
    private Date invocationDate;

    @OneToMany(mappedBy = "auditLog",  targetEntity = AuditParameter.class)
    private List<IAuditParameter> parameters;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public Date getInvocationTime() {
        return invocationDate;
    }

    @Override
    public void setInvocationTime(Date invocationTime) {
        this.invocationDate = invocationTime;
    }

    @Override
    public List<IAuditParameter> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(List<IAuditParameter> parameters) {
        this.parameters = parameters;
    }
}
