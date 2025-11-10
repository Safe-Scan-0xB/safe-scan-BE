package hello.safescsn;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // PK는 이거 하나만

    private String memberId;  // 로그인용 아이디(문자열)
    private String password;
}



