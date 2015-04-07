package dst.ass1.jpa;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.ILectureDAO;
import dst.ass1.jpa.dao.ILecturerDAO;
import dst.ass1.jpa.model.ILecturer;
import org.junit.Test;

/**
 * Created by pavol on 7.4.2015.
 */
public class TestEquals extends AbstractTest {

    @Test
    public void equalsTest() {
        ILecturer lecturer = modelFactory.createLecturer();
        lecturer.setBankCode("dsadasda");
        lecturer.setBankCode("31232113");
        lecturer.setLecturerName("lecturer1");

        em.getTransaction().begin();
        em.persist(lecturer);
        em.getTransaction().commit();
        em.clear();


        ILecturerDAO lecturerDAO = daoFactory.getLecturerDAO();
        ILecturer lecturerFromDB = lecturerDAO.findById(lecturer.getId());
        if (lecturer.equals(lecturerFromDB)) {
            System.out.println("lecturer.equals(lecturerFromDB) == true");
        }
    }
}
