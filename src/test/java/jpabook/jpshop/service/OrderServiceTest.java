package jpabook.jpshop.service;

import jpabook.jpshop.domain.Address;
import jpabook.jpshop.domain.Member;
import jpabook.jpshop.domain.Order;
import jpabook.jpshop.domain.OrderStatus;
import jpabook.jpshop.domain.item.Book;
import jpabook.jpshop.domain.item.Item;
import jpabook.jpshop.exception.NotEnoughStockException;
import jpabook.jpshop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional // 이게 있어야 롤백이 됨
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    
    @Test
    @DisplayName("상품 주문 테스트")
    public void orderTest() throws Exception {
        // given
        Member member = createMember();

        Book book = createBook("책1", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문 시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 x 수량이다.");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrderTest() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("책1", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL이다.");
        assertEquals(10, book.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");

    }
    
    @Test
    @DisplayName("상품 주문 수량 초과 테스트")
    public void orderStockQuantityExceededTest() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("책1", 10000, 10);

        // when
        int orderCount = 11;

        // then
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        });
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "동대문", "123-123"));
        em.persist(member);
        return member;
    }
}