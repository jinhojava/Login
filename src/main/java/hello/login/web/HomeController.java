package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.ArgumentResolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;


//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId",required = false)Long memberId, Model model){

        if(memberId==null){
            return "home";
        }

        //로그인 성공경우
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember==null){         //쿠키정보로 데이터베이스에서 회원정보찾아서 확인하기
            return "home";
        }

        //진짜 성공할경우 로그인성공화면으로 보내기
        model.addAttribute("member",loginMember);
            return "loginHome";
    }


 //   @GetMapping("/")//직접만든 세션 활용
    public String homeLoginV2(HttpServletRequest request, Model model){

      //세션관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);

        //로그인 성공경우
        if(member==null){
            return "home";
        }

        //진짜 성공할경우 로그인성공화면으로 보내기
        model.addAttribute("member",member);
        return "loginHome";
    }


   // @GetMapping("/")//서블릿http 세션 활용
    public String homeLoginV3(HttpServletRequest request, Model model){

        HttpSession session = request.getSession(false);//false 그냥 홈에만 들어온사람도 있는데 세션없다고 새로 생성할 필요없다
        if(session==null){
            return "home";
        }
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);//카테고리같은 키값을 넣어 객체를 꺼낸다.

        //세션에 회원 데이터가 없으면 home
        if(loginMember==null){
            return "home";
        }

        //세션이 유지되면 로그인성공화면으로 보내기
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

 //   @GetMapping("/")//스프링어노테이션 세션 활용..코드짧아진다 알아서 가져와서 찾아준다. //어노테이션이 세션생성은 안함
    public String homeLoginV3Spring(
            @SessionAttribute(name=SessionConst.LOGIN_MEMBER,required = false)Member loginMember, Model model){

//        HttpSession session = request.getSession(false);//그냥 홈에만 들어온사람도 있는데 세션없다고 새로 생성할 필요없다
//        if(session==null){
//            return "home";
//        }
//        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);//카테고리같은 키값을 넣어 객체를 꺼낸다.


        //세션에 회원 데이터가 없으면 home
        if(loginMember==null){
            return "home";
        }

        //세션이 유지되면 로그인성공화면으로 보내기
        model.addAttribute("member",loginMember);
        return "loginHome";
    }


    @GetMapping("/")//ArgumentResolver활용
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){

//        HttpSession session = request.getSession(false);//그냥 홈에만 들어온사람도 있는데 세션없다고 새로 생성할 필요없다
//        if(session==null){
//            return "home";
//        }
//        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);//카테고리같은 키값을 넣어 객체를 꺼낸다.


        //세션에 회원 데이터가 없으면 home
        if(loginMember==null){
            return "home";
        }

        //세션이 유지되면 로그인성공화면으로 보내기
        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}