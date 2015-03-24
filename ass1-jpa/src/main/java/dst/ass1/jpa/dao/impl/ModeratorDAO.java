package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IModeratorDAO;
import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.model.IVirtualSchool;
import dst.ass1.jpa.model.impl.Moderator;
import dst.ass1.jpa.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class ModeratorDAO implements IModeratorDAO {

    private EntityManager em;

    public ModeratorDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public HashMap<IModerator, Date> findNextVirtualSchoolMaintenanceByModerators() {
        HashMap<IModerator, Date> resultMap = new HashMap<>();

        Query query = em.createNamedQuery(Constants.Q_VIRTUALSCHOOLSOFMODERATOR);
        List<IModerator> result = (List<IModerator>) query.getResultList();

        for (IModerator moderator: result) {
            List<IVirtualSchool> virtualSchools = moderator.getAdvisedVirtualSchools();
            resultMap.put(moderator, virtualSchools.get(0).getNextMaintenance());
        }

        return resultMap;
    }

    @Override
    public IModerator findById(Long id) {
        return em.find(Moderator.class, id);
    }

    @Override
    public List<IModerator> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IModerator> cq = cb.createQuery(IModerator.class);
        Root<Moderator> root = cq.from(Moderator.class);

        cq.select(root);
        TypedQuery<IModerator> query = em.createQuery(cq);
        return query.getResultList();
    }
}
