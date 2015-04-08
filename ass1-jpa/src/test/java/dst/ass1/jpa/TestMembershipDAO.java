package dst.ass1.jpa;

import dst.ass1.AbstractTest;
import dst.ass1.jpa.dao.IMembershipDAO;
import dst.ass1.jpa.model.ILecturer;
import dst.ass1.jpa.model.IMOCPlatform;
import dst.ass1.jpa.model.IMembership;
import dst.ass1.jpa.model.IMembershipKey;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

/**
 * Created by pavol on 7.4.2015.
 */
public class TestMembershipDAO extends AbstractTest {

    @Test
    public void testQuery() {
        ILecturer lecturer = modelFactory.createLecturer();
        lecturer.setLecturerName("nameSurname");
        lecturer.setAccountNo("2212111");
        lecturer.setBankCode("3131211");

        ILecturer lecturer2 = modelFactory.createLecturer();
        lecturer2.setLecturerName("nameSurname2");
        lecturer2.setAccountNo("e2212111");
        lecturer2.setBankCode("e3131211");


        IMOCPlatform mocPlatform = modelFactory.createPlatform();
        mocPlatform.setName("one");


        IMembership membership = modelFactory.createMembership();
        IMembershipKey membershipKey = modelFactory.createMembershipKey();
        membershipKey.setLecturer(lecturer);
        membershipKey.setMOCPlatform(mocPlatform);
        membership.setId(membershipKey);

        IMOCPlatform mocPlatform2 = modelFactory.createPlatform();
        mocPlatform.setName("two");


        em.getTransaction().begin();
        em.persist(lecturer);
        em.persist(lecturer2);
        em.persist(mocPlatform);
        em.persist(membership);
        em.getTransaction().commit();

        IMembershipDAO membershipDAO = daoFactory.getMembershipDAO();
        List<IMembership> result = membershipDAO.findByLecturerAndPlatform(lecturer, mocPlatform);
        assertTrue(result.size() == 1);
    }
}
