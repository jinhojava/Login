package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
    public class LoginCheckFilter implements Filter{

    //로그인하지않아도 허용되는 페이지들
    private static final String[] whitelist = {"/","/members/add", "/login","/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse =(HttpServletResponse) response;

        try{
            log.info("인증체크필터 시작{}", requestURI);
            if(isLoginCheckPath(requestURI)){
                log.info("인증체크 로직 실행{}",requestURI);
                HttpSession session = httpRequest.getSession(false);
                 if(session ==null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
                     log.info("미인증 사용자 요청{}", requestURI );
                     //로그인창으로 redirect
                     httpResponse.sendRedirect("/login?redirectURL="+requestURI);
                     //로그인화면 보내고 로그인 성공하면 기존 화면으로 돌아올 수 있게 배려하기위해 일단 현재페이지URL도 loginController로 같이 넘겨주자
                     return;//인증안되면 다음 필터나 컨트롤러로 못넘어감. finally문 실행되고 메소드 종료
                 }
            }
                chain.doFilter(request, response);//(whitelist이면)false면 다음 필터로 가든가 아님 서블릿으로
        }catch (Exception e){
            throw e;
        }finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }

    }
    /*
    * 화이트 리스트의 경우 인증 체크하지않는 메소드*/
    private boolean isLoginCheckPath(String requestURI){
            return !PatternMatchUtils.simpleMatch(whitelist,requestURI);
    }//접근하는 사이트가 whitelist이면 false ,   whitelist가 아니면 필터체크해야하니 true반환
}

