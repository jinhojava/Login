package hello.login.web.ArgumentResolver;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})//어노테이션을 어디에 사용할지 --파라미터에만사용
@Retention(RetentionPolicy.RUNTIME)//어노테이션이 얼마나 오랫동안 유지될지--리플렉션등을 활용할 수 있도록 런타임까지 에노테이션 정보가 남아있음

public @interface Login {
}
