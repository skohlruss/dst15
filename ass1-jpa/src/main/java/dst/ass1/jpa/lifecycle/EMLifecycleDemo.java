package dst.ass1.jpa.lifecycle;

import dst.ass1.jpa.model.ILecture;
import dst.ass1.jpa.model.ILectureStreaming;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.ModelFactory;

import javax.persistence.EntityManager;
import java.security.NoSuchAlgorithmException;

public class EMLifecycleDemo {

    private EntityManager em;
    private ModelFactory modelFactory;

    public EMLifecycleDemo(EntityManager em, ModelFactory modelFactory) {
        this.em = em;
        this.modelFactory = modelFactory;
    }

    /**
     * Method to illustrate the persistence lifecycle. EntityManager is opened
     * and closed by the Test-Environment! You have to use at least the
     * following methods (listed in alphabetical order) provided by the EntityManager:
     * - clear(..)
     * - flush(..)
     * - merge(..)
     * - persist(..)
     * - remove(..)
     *
     * Keep in mind that this is not necessarily the correct order!
     *
     * @throws NoSuchAlgorithmException
     */
    public void demonstrateEntityMangerLifecycle()
            throws NoSuchAlgorithmException {

        // EntityManager lifecycle states: NEW, MANAGED, REMOVED, DETACHED

        /**
         * New state
         */
        ILecturer lecturer = modelFactory.createLecturer();
        lecturer.setLecturerName("firstLast");
        lecturer.setFirstName("first");
        lecturer.setLastName("last");
        lecturer.setBankCode("123456");
        lecturer.setAccountNo("123456e");


        em.getTransaction().begin();
        em.persist(lecturer);
        //after commit entity is in detached state (specially with transaction-scoped container-managed em)
        //but in this case is in managed
        em.getTransaction().commit();
        // after commit all entities should be in detached state

        /**
         * Note that if I made changes on entity here(outside a transaction), they will be propagated to DB
         * on next commit, but it is only possible with this configuration (em, persistence)
         *
         * after commit entity is in detached state (specially with transaction-scoped container-managed em)
         */
        lecturer.setLecturerName("changesOutsideTransaction");
        em.getTransaction().begin();
        em.getTransaction().commit();

        /**
         * Lecture
         */
        ILecture lecture = modelFactory.createLecture();
        ILectureStreaming lectureStreaming = modelFactory.createLectureStreaming();
        lecture.setLecturer(lecturer);
        lecture.setLectureStreaming(lectureStreaming);

        em.getTransaction().begin();
        //entity becomes managed
        em.persist(lecture);
        //propagate changes to DB
        em.flush();

        // removed entity
        em.remove(lecture);

        // managed
        em.persist(lecture);

        //detached
        em.detach(lecture);

        // managed
        em.merge(lecture);

        // clear persistence context, all entities becomes detached
        em.clear();
        /**
         * following code will produce exception, because persist call on detached entity
         * - it is possible to call only merge!
         */
        //em.persist(lecture);
        em.getTransaction().commit();
    }

}
