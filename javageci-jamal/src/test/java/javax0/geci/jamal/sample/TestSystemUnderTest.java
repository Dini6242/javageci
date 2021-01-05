package javax0.geci.jamal.sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This is the manual implementation of the test that tests the sample SystemUnderTest with the
 * SystemUnderTest inner class manually crafted.
 */
public class TestSystemUnderTest {

    @Test
    void testCounter() throws Exception {
        final var sut = new SystemUnderTest();
        sut.setCounter(0);
        sut.increment("");
        Assertions.assertEquals(1, sut.getCounter());
    }


    private static class SystemUnderTest {
        private javax0.geci.jamal.sample.SystemUnderTest sut = new javax0.geci.jamal.sample.SystemUnderTest();

        private void setCounter(int z) throws NoSuchFieldException, IllegalAccessException {
            Field f = sut.getClass().getDeclaredField("counter");
            f.setAccessible(true);
            f.set(sut, z);
        }

        private int getCounter() throws NoSuchFieldException, IllegalAccessException {
            Field f = sut.getClass().getDeclaredField("counter");
            f.setAccessible(true);
            return (int) f.get(sut);
        }

        private void increment(String z) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method m = sut.getClass().getDeclaredMethod("increment",String.class);
            m.setAccessible(true);
            m.invoke(sut,z);
        }

        private int count(int z) {
            return sut.count(z);
        }
    }
}
