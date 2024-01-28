package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /*
    return이 null이면 로그인 실패
    * */

    public Member login(String loginId, String password){
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//        Member member = findMemberOptional.get();
//        if(member.getPassword().equals(password)){
//            return member;
//        }else {
//            return null;
//        }
        return memberRepository.findByLoginId(loginId)
                .filter(m->m.getPassword().equals(password))
                .orElse(null);
    }// 로그인id가 요청받은 loginId와 같은 객체 반환받고 그 객체의 패스워드가 요청받은 패스워드와
    //같은지 검사하고 맞으면 Member객체 반환(자동컴파일 optinal<Member>에서 Member로)
}
