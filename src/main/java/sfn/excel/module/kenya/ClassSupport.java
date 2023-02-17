package sfn.excel.module.kenya;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import sfn.excel.module.kenya.annotation.LocalDateTimeColumn;
import sfn.excel.module.kenya.annotation.NumericColumn;
import sfn.excel.module.kenya.annotation.StringColumn;

public class ClassSupport {

    public static <T> T createInstance(Cells cells, Class<T> clazz) {
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new InstanceClassException("","생성자 메소드를 찾을 수 없습니다 ", e);
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
                setStringFieldValue(instance, field, cells, stringColumnAnn);
                return;
            }

            NumericColumn numericColumnAnn = field.getAnnotation(NumericColumn.class);
            if (numericColumnAnn != null) {
                setNumericFieldValue(instance, field, cells, numericColumnAnn);
                return;
            }

            LocalDateTimeColumn dateTimeColumnAnn = field.getAnnotation(LocalDateTimeColumn.class);
            if (dateTimeColumnAnn != null) {
                setLocalDateTimeFieldValue(instance, field, cells, dateTimeColumnAnn);
                return;
            }

            throw new InstanceClassException(field.getName(), "Missing TypeColumn Annotation", null);
        } catch (IllegalAccessException e) {
            throw new InstanceClassException(field.getName(), "Missing TypeColumn Annotation", e);
        } catch (IllegalArgumentException e) {
            throw new InstanceClassException(field.getName(), e.getMessage(), e);
        }
        finally {
            field.setAccessible(false);
        }
    }

    private static <T> void setNumericFieldValue(T instance, Field field, Cells cells,
        NumericColumn ann) throws IllegalAccessException {
        Class<?> type = field.getType();
        Double value = getNumericFieldValue(cells, ann);

        if (type.equals(Integer.class)) {
            field.set(instance, value.intValue());
            return;
        }

        if (type.equals(Double.class)){
            field.set(instance, value);
        }
    }

    private static double getNumericFieldValue(Cells cells, NumericColumn ann) {
        if (ann.headerIndex() > -1) {
            return cells.get(ann.headerIndex()).toDouble(ann.defaultValue());
        }

        if (!ann.headerName().isBlank()){
            validateHeaderNamePolicy(cells, ann.headerName(), ann.policy());
            return cells.get(ann.headerName()).toDouble(ann.defaultValue());
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

    private static <T> void setLocalDateTimeFieldValue(T instance, Field field, Cells cells, LocalDateTimeColumn ann)
        throws IllegalAccessException {
        String value = "";
        if (ann.headerIndex() > -1) {
            value = cells.getString(ann.headerIndex());
        }
        if (!ann.headerName().isBlank()){
            validateHeaderNamePolicy(cells, ann.headerName(), ann.policy());
            value = cells.getString(ann.headerName());
        }

        if (value.isBlank() && !ann.defaultValue().isBlank()){
            setDateValue(instance, ann.defaultValue(), field, ann.pattern());
        }

        setDateValue(instance, value, field, ann.pattern());
    }

    private static <T> void setDateValue(T instance, String value, Field field, String pattern)
        throws IllegalAccessException {
        Class<?> type = field.getType();

        String replaceValue = DateTypeNormalizer.edit(value);

        try {
            if (type.equals(LocalDate.class)) {
                String sliceValue = replaceValue.substring(0, pattern.length());
                LocalDate localDate = LocalDate.parse(sliceValue,
                    DateTimeFormatter.ofPattern(pattern));
                field.set(instance, localDate);
                return;
            }

            if (type.equals(LocalDateTime.class)) {
                LocalDateTime localDatetime = LocalDateTime.parse(replaceValue,
                    DateTimeFormatter.ofPattern(pattern));
                field.set(instance, localDatetime);
            }
        } catch (DateTimeParseException e) {
            throw new InstanceClassException(field.getName(), "날짜변환중 오류가 발생했습니다 (value: "+value+")", e);
        }
    }

    private static <T> void setStringFieldValue(T instance, Field field, Cells cells,
        StringColumn ann) throws IllegalAccessException {
        Class<?> type = field.getType();
        String value = getStringFieldValue(cells, ann);
        if (type.equals(String.class)) {
            field.set(instance, value);
            return;
        }

        if (type.equals(LocalDate.class)){
            setDateValue(instance, value, field, DateTypeNormalizer.DATE_FORMAT);
            return;
        }

        if (type.equals(LocalDateTime.class)){
            setDateValue(instance, value, field, DateTypeNormalizer.DATETIME_FORMAT);
        }
    }

    private static void validateHeaderNamePolicy(Cells cells, String headerName, NotFoundHeaderNamePolicy policy) {
        if (policy.equals(NotFoundHeaderNamePolicy.ERROR) && cells.findColumnNameIndex(headerName) == -1){
            throw new InstanceClassException("Header Name("+ headerName+")을 찾을 수 없습니다", null);
        }
    }
}
