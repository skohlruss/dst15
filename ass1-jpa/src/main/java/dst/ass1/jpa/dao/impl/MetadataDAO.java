package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IMetadataDAO;
import dst.ass1.jpa.model.IMetadata;
import dst.ass1.jpa.model.impl.Metadata;

import javax.persistence.EntityManager;

/**
 * Created by pavol on 24.3.2015.
 */
public class MetadataDAO extends GenericDAOImpl<IMetadata> implements IMetadataDAO {

    public MetadataDAO(EntityManager em) {
        super(em, Metadata.class);
    }
}
