package sfn.excel.module.kenya;

public enum NotFoundHeaderNamePolicy {
    /**
     * 헤더명으로 찾지 못한다면 Exception을 발생시킨다
     */
    ERROR,

    /**
     * 헤더명으로 찾지 못한다면 기본값으로 표시한다
     */
    DEFAULT_VALUE
}
