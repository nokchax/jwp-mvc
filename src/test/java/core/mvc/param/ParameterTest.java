package core.mvc.param;

import core.annotation.web.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("메소드의 파라미터를 표현하기 위한 클래스")
class ParameterTest {

    @Test
    @DisplayName("파라미터는 파라미터 이름과 그 파라미터의 타입, 그리고 해당 파라미터가 가지고 있는 어노테이션을 가지고 있다.")
    void constructor() {
        assertThatCode(() -> new Parameter<String>("name", Controller.class))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("해당 파라미터는 request 에서 파라미터에 해당하는 데이터를 받아다가 리턴해준다")
    void searchParam() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("name", "nokchax");
        Parameter<String> parameter = new Parameter<>("name", null);

        String refinedParam = parameter.searchParam(request);

        assertThat(refinedParam).isEqualTo("nokchax");
    }
}