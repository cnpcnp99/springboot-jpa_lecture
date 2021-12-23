package jpabook.jpshop.service;

import jpabook.jpshop.domain.Member;
import jpabook.jpshop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional // 이게 있어야 롤백이 됨
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    
    @Test
    @DisplayName("회원 가입 테스트")
    // @Rollback(value = false)
    public void register() throws Exception {
        // given
        Member member = new Member();
        member.setName("김아무개");
        // when
        Long member_id = memberService.join(member);
        // then
        assertEquals(member, memberRepository.findOne(member_id));
    }
    
    @Test()
    @DisplayName("중복 회원 예외 테스트")
    public void duplicateMemberException() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("김");
        Member member2 = new Member();
        member2.setName("김");
        // when
        memberService.join(member1);
        // then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2); // 예외가 발생해야 한다.
        });
    }
}