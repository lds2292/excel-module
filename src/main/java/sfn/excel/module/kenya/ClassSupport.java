package sfn.excel.module.kenya;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ClassSupport {

    public static <T> T createInstance(Cells cells, Class<T> clazz) {
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new InstanceClassException("","생성자 메소드를 찾을 수 없습니 ", e);
        }

        constructor.setAccessible(true);

        try {
            T instance = null;
            instance = constructor.newInstance();
            for (Field declaredField : instance.getClass().getDeclaredFields()) {
                mappingColumn(instance, cells, declaredField);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanceClassException(e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private static <T> void mappingColumn(T instance, Cells cells, Field field) {
        field.setAccessible(true);
        try {
            StringColumn stringColumnAnn = field.getAnnotation(StringColumn.class);
            if (stringColumnAnn != null) {
                String value = getStringFieldValue(cells, stringColumnAnn);
                field.set(instance, value);
                return;
            }

            IntegerColumn integerColumnAnn = field.getAnnotation(IntegerColumn.class);
            if (integerColumnAnn != null) {
                Integer value = getIntegerFieldValue(cells, integerColumnAnn);
                field.set(instance, value);
                return;
            }

            throw new InstanceClassException(field.getName(), "Missing TypeColumn Annotation", null);
        } catch (IllegalAccessException e) {
            throw new InstanceClassException(field.getName(), "Missing TypeColumn Annotation", e);
        } finally {
            field.setAccessible(false);
        }
    }

    private static int getIntegerFieldValue(Cells cells, IntegerColumn ann) {
        if (ann.headerIndex() > -1) {
            return cells.get(ann.headerIndex()).toInt(ann.defaultValue());
        }

        if (!ann.headerName().isBlank()){
            validateHeaderNamePolicy(cells, ann.headerName(), ann.poilcy());
            return cells.get(ann.headerName()).toInt(ann.defaultValue());
        }

        return ann.defaultValue();
    }

    private static String getStringFieldValue(Cells cells, StringColumn ann) {
        if (ann.headerIndex() > -1) {
            String value = cells.getString(ann.headerIndex());
            if (!ann.defaultValue().isBlank() && value.isBlank()) {
                return ann.defaultValue();
            }
            return value;
        }

        if (!ann.headerName().isBlank()){
            validateHeaderNamePolicy(cells, ann.headerName(), ann.policy());

            String value = cells.getString(ann.headerName());
            if (!ann.defaultValue().isBlank() && value.isBlank()) {
                return ann.defaultValue();
            }
            return value;
        }

        return ann.defaultValue();
    }

    private static void validateHeaderNamePolicy(Cells cells, String headerName, NotFoundHeaderNamePolicy policy) {
        if (policy.equals(NotFoundHeaderNamePolicy.ERROR) && cells.findColumnNameIndex(headerName) == -1){
            throw new InstanceClassException("Header Name("+ headerName+")을 찾을 수 없습니다", null);
        }
    }
}
