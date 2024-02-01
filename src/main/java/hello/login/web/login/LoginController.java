package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URL;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm")LoginForm form){
         return "login/loginForm";
    }



    //@PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {                                                    //쿠키전송위해서
            return "login/loginForm";
        }
            Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

            if (loginMember == null) {
                bindingResult.reject("loginFail", "아이디또는 비밀번호가 맞지않습니다.");
                return "login/loginForm";//글로벌오류
            }
            //로그인 성공처리 TODO

        //쿠키에 시간 정보를 주지않으면 세션쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie); //응답객체에 cookie넣어보내기
        return "redirect:/";
        }




    //@PostMapping("/login")//직접만든 세션활용
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디또는 비밀번호가 맞지않습니다.");
            return "login/loginForm";//글로벌오류
        }
        //로그인 성공처리
        //세션관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(loginMember,response);

        return "redirect:/";
    }

 //   @PostMapping("/login")//HTTP세션활용
    public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디또는 비밀번호가 맞지않습니다.");
            return "login/loginForm";//글로벌오류
        }

        //로그인 성공처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성, 고유한 id를 가진 세션이 생성된다.
        HttpSession session = request.getSession();//(true)기본값임 false로하고 세션이 없으면 새로운 새션생성안하고null반환

        //세션에 로그인 회원 정보를 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);//세션에 보관하고싶은 객체담기
        //키값에 카테고리같이 세션의 범주를 넣어주고, 값에 객체를 넣어준다.
        return "redirect:/";
    }


    @PostMapping("/login")//HTTP세션활용//로그인필터 리다이렉트 추가
    public String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                          @RequestParam(defaultValue = "/")String redirectURL,//redirectURL비어있으면 기본값/
                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디또는 비밀번호가 맞지않습니다.");
            return "login/loginForm";//글로벌오류
        }
        //로그인 성공처리

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성, 고유한 id를 가진 세션이 생성된다.
        HttpSession session = request.getSession();//(true)기본값임 //false경우 세션이 없으면 새로운 새션생성안하고null반환

        //세션에 로그인 회원 정보를 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);//세션에 보관하고싶은 객체담기
        //키값에 카테고리같이 세션의 범주를 넣어주고, 값에 객체를 넣어준다.
        return "redirect:"+redirectURL;
    }





    //   @PostMapping("/logout")//로그아웃시 요청 쿠키받고 쿠키생명0으로 만든뒤 반환
        public String logout(HttpServletResponse response){
            expireCookie(response,"memberId");
            return "redirect:/";
        }


 //   @PostMapping("/logout")//직접만든 세션활용
    public String logoutV2(HttpServletRequest request){
        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/logout")//http 세션활용
    public String logoutV3(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session!=null){
            session.invalidate();//세션삭제
        }
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
         Cookie cookie = new Cookie(cookieName,null);
         cookie.setMaxAge(0);
          response.addCookie(cookie);
        }
}//쿠키의 값은 임의로 변경할 수가 있다...웹브라우져에서도..그래서 중요정보는 사용 ㄴㄴ
//세션활용하자 ㄱㄱㄱㄱㄱㄱ
