package hello.login.web.session;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {
    //세션정보와 타임아웃

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session==null){
            return "세션이 없습니다";
        }

        //세션데이터출력//세션 카테고리이름가져와서 이름과 각각 객체 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name->log.info("session name={},value={}",name,session.getAttribute(name)));

        log.info("sessionId={}", session.getId());

        //세션 최대 비활성가능한 시간
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
        //세션 생성시간
        log.info("creationTime={}",new Date(session.getLastAccessedTime()));
        //세션이 마지막으로 접근된 시간
        log.info("lastAccessedTime={}",new Date(session.getLastAccessedTime()));
        //현재 요청이 새로운 세션인지 여부 확인
        log.info("isNew={}",session.isNew());

        return "세션출력";
    }
}
