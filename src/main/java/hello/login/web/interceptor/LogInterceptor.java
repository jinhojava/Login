package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();//<----필터와다르게 지역변수 아님, 호출시점이 분리되어있기때문

       /*참고     //@RequestMapping: HandlerMethod
                 //정적리소스: ResourceHttpRequestHandler
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;//호출할 컨트롤러 메소드의 모든 정보가 포함되어있다.
            hm.메소드                (해당 컨트롤러 메서드의 다양한 정보를 조회하고 활용할 수 있다)

            HandlerMethod
핸들러 정보는 어떤 핸들러 매핑을 사용하는가에 따라 달라진다. 스프링을 사용하면 일반적으로 @Controller ,
@RequestMapping 을 활용한 핸들러 매핑을 사용하는데, 이 경우 핸들러 정보로 HandlerMethod 가 넘어온다.
ResourceHttpRequestHandler
@Controller 가 아니라 /resources/static 와 같은 정적 리소스가 호출 되는 경우
ResourceHttpRequestHandler 가 핸들러 정보로 넘어오기 때문에 타입에 따라서 처리가 필요하다.
        }*/

        request.setAttribute(LOG_ID,uuid);

        log.info("REQUEST[{}][{}][{}]",uuid,requestURI,handler);
        return true;//실제핸들러 호출
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle[{}]",modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE[{}][{}][{}]",logId, requestURI, handler);
        if(ex!=null){
            log.error("afterCompletion error!!",ex);
        }
    }
}
