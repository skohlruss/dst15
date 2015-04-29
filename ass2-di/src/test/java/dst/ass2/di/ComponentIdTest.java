package dst.ass2.di;

import dst.ass2.di.type.extended.InvalidId;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.fail;

/**
 * Created by pavol on 29.4.2015.
 */
public class ComponentIdTest {
    private IInjectionController ic;

    @Before
    public void before() {
        ic = InjectionControllerFactory.getNewStandaloneInstance();
    }

    @Test
    public void testInvalidId() {
        InvalidId invalidId = new InvalidId();

        try {
            ic.initialize(invalidId);

            fail(InjectionException.class.getName() + " expected");
        } catch (InjectionException ex) {
            // ex expected
        }
    }
}
