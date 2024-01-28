package hello.login.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {

    private Long id;

    @NotEmpty//비어있으면안됨
    private String loginId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

}
