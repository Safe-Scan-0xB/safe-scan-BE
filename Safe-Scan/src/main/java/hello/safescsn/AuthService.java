package hello.safescsn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public String login(String MemberId, String password) {
        Member member = memberRepository.findByMemberId(MemberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디"));

        if (!member.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호 불일치");
        }

        return "로그인 성공";
    }

    public String signup(String memberId, String password) {
        if (memberRepository.findByMemberId(memberId).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디");
        }

        Member member = new Member();
        member.setMemberId(memberId);
        member.setPassword(password);

        memberRepository.save(member);
        return "회원가입 성공";
    }
}


