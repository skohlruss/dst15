package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ILectureStreamingDAO;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.LectureStatus;
import dst.ass1.jpa.model.impl.Classroom;
import dst.ass1.jpa.model.impl.LectureStreaming;
import dst.ass1.jpa.model.impl.MOCPlatform;
import dst.ass1.jpa.model.impl.VirtualSchool;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by pavol on 24.3.2015.
 */
public class LectureStreamingDAO extends GenericDAOImpl<ILectureStreaming> implements ILectureStreamingDAO {

    public LectureStreamingDAO(EntityManager em) {
        super(em, LectureStreaming.class);
    }

    @Override
    public List<ILectureStreaming> findByStatus(LectureStatus status) {

        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<ILectureStreaming> cq = cb.createQuery(ILectureStreaming.class);
        Root<LectureStreaming> root = cq.from(LectureStreaming.class);

        cq.select(root);
        cq.where(cb.equal(root.get("lectureStatus"), status));
        TypedQuery<ILectureStreaming> query = getEm().createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<ILectureStreaming> findByEndDateIsNull() {

        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<ILectureStreaming> cq = cb.createQuery(ILectureStreaming.class);
        Root<LectureStreaming> root = cq.from(LectureStreaming.class);

        cq.select(root);
        cq.where(cb.isNull(root.get("end")));
        TypedQuery<ILectureStreaming> query = getEm().createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<ILectureStreaming> findFinishedByPlatformName(String platform, int maxResult) {

        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<ILectureStreaming> cq = cb.createQuery(ILectureStreaming.class);
        Root<LectureStreaming> root = cq.from(LectureStreaming.class);

        Join<LectureStreaming, Classroom> classroomJoin = root.join("classrooms");
        Join<Classroom, VirtualSchool> virtualSchoolJoin = classroomJoin.join("virtualSchool");
        Join<VirtualSchool, MOCPlatform> mocPlatformJoin = virtualSchoolJoin.join("mocPlatform");

        Predicate finishedPredicate = cb.equal(root.get("lectureStatus"), LectureStatus.FINISHED);
        Predicate platformPredicate = cb.equal(mocPlatformJoin.get("name"), platform);

        cq.select(root);
        cq.where(cb.and(finishedPredicate, platformPredicate));
        TypedQuery<ILectureStreaming> query = getEm().createQuery(cq);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }
}
