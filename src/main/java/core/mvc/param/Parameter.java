package core.mvc.param;

import core.exception.ParameterNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

public class Parameter<T> {
    private final String name;
    private final Class<? extends Annotation> annotation; // 활용은..?

    public Parameter(String name, Class<? extends Annotation> annotation) {
        this.name = name;
        this.annotation = annotation;
    }

    public T searchParam(final HttpServletRequest request) {
        String parameter = request.getParameter(name);

        if (parameter == null) {
            throw new ParameterNotFoundException(name);
        }

        // TODO: 2020/06/15 type과 파라미터를 받아서 해당 값으로 파싱해주는 작업을 해주는 객체 만들기
        return (T) parameter;
    }
}
