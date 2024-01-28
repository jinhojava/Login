package hello.login.domain.member;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long,Member> store = new HashMap<>();
    private static long sequence= 0L;//static사용

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}",member);
        store.put(member.getId(),member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId){
//        List<Member> all = findAll();
//        for(Member m : all){
//            if(m.getLonginId().equals(loginId)){
//                return Optional.of(m);
//            }
//        }
//        return Optional.empty();              //null반환할수있는 깡통같은optional

        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }//데이터베이스에서 값들을 다 가져와서 그 중 로그인id가 요청받은 loginId와 같은 객체 반환

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
