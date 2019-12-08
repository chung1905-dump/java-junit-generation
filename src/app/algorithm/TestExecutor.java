package app.algorithm;

import app.algorithm.pso.Particle;
import app.system.Output;
import it.itc.etoc.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class TestExecutor {
    public static Set<Integer> run(Class<?> objectToTest, MethodSignature methodSignature, Particle<?> particle) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        objectToTest.getMethod("newTrace").invoke(null); // clear traces

        Object[] args = particle.getPosition().toArray();
        Method method = objectToTest.getMethod(methodSignature.getName(), stringToClass(methodSignature.getParameters().toArray()));
        Output.disable();
        method.invoke(null, args);
        Output.enable();

        Method getTrace = objectToTest.getMethod("getTrace");
        return (Set<Integer>) getTrace.invoke(null);
    }

    private static Class<?>[] stringToClass(Object[] strings) {
        Class<?>[] classes = new Class[strings.length];
        for (int i = 0; i < strings.length; i++) {
            Class<?> c;
            switch (strings[i].toString()) {
                case "double":
                    c = double.class;
                    break;
                case "float":
                    c = float.class;
                    break;
                case "int":
                    c = int.class;
                    break;
                case "boolean":
                    c = boolean.class;
                    break;
                default:
                    // Non-support type
                    c = null;
            }
            classes[i] = c;
        }

        return classes;
    }
}
