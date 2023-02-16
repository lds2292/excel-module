package sfn.excel.module.kenya;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ClassSupport {

    public static <T> T createInstance(Class<T> clazz)
        throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        T instance = constructor.newInstance();
        for (Field declaredField : instance.getClass().getDeclaredFields()) {
            System.out.println(Arrays.toString(declaredField.getAnnotations()));
        }
        constructor.setAccessible(false);
        return instance;
    }

}
