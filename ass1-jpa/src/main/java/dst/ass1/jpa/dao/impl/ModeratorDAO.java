package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IModeratorDAO;
import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.model.IVirtualSchool;
import dst.ass1.jpa.model.impl.Moderator;
import dst.ass1.jpa.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class ModeratorDAO extends GenericDAOImpl<IModerator> implements IModeratorDAO {

    public ModeratorDAO(EntityManager em) {
        super(em, Moderator.class);
    }

    @Override
    public HashMap<IModerator, Date> findNextVirtualSchoolMaintenanceByModerators() {
        HashMap<IModerator, Date> resultMap = new HashMap<>();

        Query query = getEm().createNamedQuery(Constants.Q_VIRTUALSCHOOLSOFMODERATOR);
        List<IModerator> result = (List<IModerator>) query.getResultList();

        for (IModerator moderator: result) {
            List<IVirtualSchool> virtualSchools = moderator.getAdvisedVirtualSchools();
            resultMap.put(moderator, virtualSchools.get(0).getNextMaintenance());
        }

        return resultMap;
    }
}
