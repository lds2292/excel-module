package sfn.excel.module.kenya;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import sfn.excel.module.kenya.exception.FailedParseException;
import sfn.excel.module.kenya.support.DateTypeNormalizer;

public class CellValue {

    private String value = "";
    private String format;
    private CellType type;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CellValue() {}

    public CellValue(Cell cell, String value){
        this.value = value;
        this.format = cell == null ? null : cell.getCellStyle().getDataFormatString();
        this.type = cell == null ? null : cell.getCellType();
    }

    public CellValue(String value) {
        this.value = value;
    }

    /**
     * Cell에 있는 값을 Integer로 변환합니다. Cell value가 null, 빈값 또는 변환할 수 없을때는 null을 반환합니다
     * </p>
     * @return Integer 타입을 반환합니다. null이 반환될 수 있습니다
     */
    public Integer toInt() {
        return toInt(null);
    }

    /**
     * Cell에 있는 값을 Integer로 변환합니다. Cell value가 null, 빈값 또는 변환할 수 없을때는 defaultValue를 반환합니다
     * </p>
     * @param defaultValue 변환실패시 반환할 기본값
     * @return Integer 타입을 반환합니다. null이 반환될 수 있습니다
     */
    public Integer toInt(Integer defaultValue) {
        if (Objects.isNull(this.value)) return defaultValue;
        if (value.isBlank()) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    /**
     * Cell에 있는 값을 Double로 반환합니다
     * </p>
     * @return Double 타입을 반환합니다. null이 반환될 수 있습니다.
     * @exception NumberFormatException 숫자로 변환할 수 없을때 발생합니다.
     */
    public Double toDouble() {
        return this.toDouble(null);
    }

    /**
     * Cell에 있는 값을 Double타입으로 변환합니다. Cell value가 null, 빈값 또는 변환할 수 없을때는 defaultValue를 반환합니다
     * </p>
     * @param defaultValue 변환실패시 반환할 기본값
     * @return Integer 타입을 반환합니다. null이 반환될 수 있습니다
     */
    public Double toDouble(Double defaultValue) {
        if (Objects.isNull(this.value)) return defaultValue;
        if (value.isBlank()) return defaultValue;
        return Double.parseDouble(value);
    }

    public LocalDate toLocalDate(){
        return this.toLocalDateTime().toLocalDate();
    }

    public LocalDate toLocalDate(LocalDateTime defaultValue) {
        return this.toLocalDateTime(defaultValue).toLocalDate();
    }

    public LocalDate toLocalDate(LocalDateTime defaultValue, DateTimeFormatter formatter) {
        return this.toLocalDateTime(defaultValue, formatter).toLocalDate();
    }

    public LocalDateTime toLocalDateTime(){
       return this.toLocalDateTime(null, formatter);
    }

    /**
     * 특정 포매터를 사용하여 문자열을 local date-time으로 반환합니다
     * @param defaultValue null일 경우 반환할 기본값 입니다
     * @param formatter  어떤 날짜형식으로 반환할지 사용하는 포매터
     * @return local date-time으로 반환하고 null이 반환될수 있습니다
     * @throws DateTimeParseException 문자열을 파싱할 수 없을때 Exception
     */
    public LocalDateTime toLocalDateTime(LocalDateTime defaultValue, DateTimeFormatter formatter) {
        if (Objects.isNull(this.value)) return defaultValue;
        if (this.value.isBlank()) return defaultValue;

        try {
            String replaceValue = DateTypeNormalizer.edit(this.value);

            return LocalDateTime.parse(replaceValue, formatter);
        } catch (DateTimeParseException ex) {
            String message = "데이터 변환중 오류가 발생했습니다 (value: %s, type: %s, format: %s)";
            throw new FailedParseException(String.format(message, value, type, format), ex);
        }
    }

    /**
     * 특정 포매터를 사용하여 문자열을 local date-time으로 반환합니다
     * @param defaultValue null일 경우 반환할 기본값 입니다
     * @return local date-time으로 반환하고 null이 반환될수 있습니다
     * @throws DateTimeParseException 문자열을 파싱할 수 없을때 Exception
     */
    public LocalDateTime toLocalDateTime(LocalDateTime defaultValue) {
        return this.toLocalDateTime(defaultValue, formatter);
    }

    @Override
    public String toString() {
        return value;
    }
}
