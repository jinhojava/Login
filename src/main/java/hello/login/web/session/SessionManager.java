package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*직접만들어보는
*세션관리
* */
@Component
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mysessionId";
    //          <세션아이디,객체>
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    //ConcurrentHashMap - 동시에 여러요청 여러 쓰레드 처리시 권장


    /*
     * 세션생성
     *  sessionId생성(임의의 추정 불가능한 랜덤 값)
     *  세션저장소에 sessionId와 보관함 값 저장
     *  sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     * */
    public void createSession(Object value, HttpServletResponse response) {
        //세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString(); //난수 생성
        sessionStore.put(sessionId, value);

        //쿠키생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /*
     * 세션조회*/
    public Object getSession(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if(cookies == null){
//            return null;
//        }
//        for (Cookie cookie : cookies) {
//            if(cookie.getName().equals(SESSION_COOKIE_NAME)){//쿠키이름확인
//                return sessionStore.get(cookie.getValue());//키값
//            }
//        }
//        return null;
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());//세션스토어에 쿠키아이디를 키로넣어
        //객체를 찾아반환.
    }

    /*
    * 세션만료
    * */
    public void expire(HttpServletRequest request){
        Cookie sessioncookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessioncookie!=null){
            sessionStore.remove(sessioncookie.getValue());
        }
    }


    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
        //배열 cookies를 스트림을 변환후 조건을 걸어 필터링한다. 여기서는 getName()이 주어진 cookieName과
        //일치하는지 확인후 만족하는 쿠키 반환한다. 없으면 null반환
    }
}
