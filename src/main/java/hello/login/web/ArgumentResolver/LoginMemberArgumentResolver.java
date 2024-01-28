package hello.login.web.ArgumentResolver;


import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //핸들러어댑터에게 파라미터를 지원할지 말지 결정하는 메소드. 검사를해야한다그래서
        // hasLoginAnnotation은 파라미터에 @Login 어노테이션이 붙어있는지 여부를 확인합니다.
        //hasMemberType은 파라미터의 타입이 Member 클래스나 그의 하위 클래스인지를 확인합니다.
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);//파라미터에 로그인 에노테이션이 붙어있는가물어봄
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasMemberType;
        //둘 다 해당되면 참을 반납한다. 그러면 아래 메소드가 실행된다.
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolverArgument 실행");
        //HttpServletRequest 획득: webRequest.getNativeRequest()를 통해 현재 요청에 대한 HttpServletRequest 객체를 획득
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        //세션 검사: request.getSession(false)를 호출하여 현재 요청의 세션을 가져옴, false를 인자로 주어 세션이 없으면 null을 반환.
        HttpSession session = request.getSession(false);
        if(session==null){
            return null;
        }

        //세션에 LOGIN_MEMBER 속성 확인: 세션이 존재하고, 세션에 LOGIN_MEMBER라는 속성이 있다면 해당 속성의 값을 반환. 만약 세션이나 LOGIN_MEMBER 속성이 없다면 null을 반환
        return session.getAttribute(SessionConst.LOGIN_MEMBER);

        //반환된 파라미터는 이젠 핸들러어댑터거쳐서 컨트롤러 메서드 파라미터로 전달됨.
    }


}
