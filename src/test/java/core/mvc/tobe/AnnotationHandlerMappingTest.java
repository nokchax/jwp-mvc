package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.db.DataBase;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class AnnotationHandlerMappingTest {
    private static final String BASE_PACKAGE = "core.mvc.tobe";
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        handlerMapping.initialize();
    }

    @Test
    public void create_find() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        request.setParameter("userId", user.getUserId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertThat(request.getAttribute("user")).isEqualTo(user);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("request method 가 설정되어 있지 않다면 모든 종류의 request method 에 대해서 맵핑해 준다.")
    void initialize(final RequestMethod requestMethod) {
        MockHttpServletRequest request = new MockHttpServletRequest(requestMethod.name(), "/test");

        assertThat(handlerMapping.getHandler(request)).isNotNull();
    }

    private static Stream<RequestMethod> initialize() {
        return Stream.of(RequestMethod.values());
    }

    @Test
    @DisplayName("존재 하지 않는 url 을 호출하면 null 을 리턴한다")
    void getHandlerThatIsNotExist() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/not-exist");

        assertThat(handlerMapping.getHandler(request)).isNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("베이스 패키지가 비어있거나 널일 경우 스캐너를 초기화 할 수 없으므로 예외 발생")
    void constructorThrowException(final Object... objects) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new AnnotationHandlerMapping(objects));
    }

    @Test
    @DisplayName("요청 url 과 method 가 같으면 엔트리 포인트 등록시 예외가 발생한다")
    void registerSameRequestUrl() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    AnnotationHandlerMapping mapping = new AnnotationHandlerMapping("core.mvc.controller");
                    mapping.initialize();
                });
    }
}
