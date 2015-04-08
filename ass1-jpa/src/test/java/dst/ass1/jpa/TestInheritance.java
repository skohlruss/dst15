package dst.ass1.jpa;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.ILecturerDAO;
import dst.ass1.jpa.dao.IModeratorDAO;
import dst.ass1.jpa.model.IAddress;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IModerator;
import dst.ass1.jpa.model.IPerson;
import org.junit.Test;

import java.util.List;

/**
 * Created by pavol on 8.4.2015.
 */
public class TestInheritance extends AbstractTest {

    @Test
    public void testInheritance() {

        IAddress address = modelFactory.createAddress();
        address.setCity("Trstena");
        address.setStreet("Zapad");
        address.setZipCode("02801");


        ILecturer lecturer = modelFactory.createLecturer();
        lecturer.setFirstName("Pavol");
        lecturer.setLastName("loffay");
        lecturer.setLecturerName("nameress");
        lecturer.setBankCode("2121121");
        lecturer.setBankCode("432432sss");
        lecturer.setAddress(address);


        IModerator moderator = modelFactory.createModerator();
        moderator.setAddress(address);

        em.getTransaction().begin();
        em.persist(lecturer);

//        moderator.setId(lecturer.getId());
        em.persist(moderator);
        em.getTransaction().commit();

        IModeratorDAO moderatorDAO = daoFactory.getModeratorDAO();
        ILecturerDAO lecturerDAO = daoFactory.getLecturerDAO();
        IModerator moderator1 = (IModerator) lecturerDAO.findById(0l);


        List<IModerator> moderatorList = moderatorDAO.findAll();
        List<ILecturer> lecturerList = lecturerDAO.findAll();
        System.out.println("end");
    }
}
