package app.algorithm;

import app.algorithm.pso.Particle;
import it.itc.etoc.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class TestExecutor {
    public static Set run(Object objectToTest, MethodSignature methodSignature, Particle<?> particle) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class<?> clazz = (Class<?>) objectToTest;
        clearTrace(clazz);

        Object[] args = particle.getPosition().toArray();
        Method method = clazz.getMethod(methodSignature.getName(), double.class, double.class, double.class);
        method.invoke(null, args);

        Method getTrace = clazz.getMethod("getTrace");
        return (Set) getTrace.invoke(null);
    }

    private static void clearTrace(Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
        Field f = clazz.getDeclaredField("trace");
        f.setAccessible(true);
        f.set(null, new HashSet<>());
    }
}
