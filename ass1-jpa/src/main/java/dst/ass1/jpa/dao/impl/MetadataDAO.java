package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IMetadataDAO;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.impl.Metadata;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class MetadataDAO implements IMetadataDAO {

    private EntityManager em;

    public MetadataDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public IMetadata findById(Long id) {
        return em.find(Metadata.class, id);
    }

    @Override
    public List<IMetadata> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IMetadata> cq = cb.createQuery(IMetadata.class);
        Root<Metadata> root = cq.from(Metadata.class);

        cq.select(root);
        TypedQuery<IMetadata> query = em.createQuery(cq);
        return query.getResultList();
    }
}
