package jpabook.jpshop;

import jpabook.jpshop.domain.Member;
import jpabook.jpshop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {
//        // given
//        Member member = new Member();
//        member.setName("memberA");
//
//        // when
//        Long savedID = memberRepository.save(member);
//        Member foundMember = memberRepository.find(savedID);
//
//        // then
//        Assertions.assertThat(foundMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(foundMember.getName()).isEqualTo(member.getName());
//        Assertions.assertThat(foundMember).isEqualTo(member);
    }
}