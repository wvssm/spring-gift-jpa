package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WishRepositoryTest {
    @Autowired
    private WishRepository wishes;
    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("위시 저장 테스트")
    void save() {
        // given
        Product expectedProduct = new Product("아메리카노",2000,"http://example.com/americano");
        Member expectedMember = new Member("a@a.com","1234");
        int expectedQuantity = 2;
        Wish expected = new Wish(expectedMember,expectedProduct,expectedQuantity);

        // when
        Wish actual = wishes.save(expected);

        // then
        assertAll(
                ()->assertThat(actual.getId()).isNotNull(),
                ()->assertThat(actual.getProduct()).isEqualTo(expected.getProduct()),
                ()->assertThat(actual.getMember()).isEqualTo(expected.getMember()),
                ()->assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity())
        );
    }

    @Test
    @DisplayName("위시 멤버 아이디로 조회 테스트")
    void findByMemberId() {
        // given
        Product expectedProduct = new Product("아메리카노",2000,"http://example.com/americano");
        entityManager.persist(expectedProduct);
        Member expectedMember = new Member("a@a.com","1234");
        entityManager.persist(expectedMember);
        entityManager.flush();
        int expectedQuantity = 2;
        Wish expected = new Wish(expectedMember,expectedProduct,expectedQuantity);
        Wish savedWish = wishes.save(expected);
        entityManager.flush();
        entityManager.clear();

        // when
        Wish findWish = wishes.findById(savedWish.getId()).get();

        // then
        assertAll(
                () -> assertThat(findWish.getId()).isNotNull(),
                () -> assertThat(findWish.getMember().getEmail()).isEqualTo(savedWish.getMember().getEmail()),
                () -> assertThat(findWish.getProduct().getName()).isEqualTo(savedWish.getProduct().getName()),
                () -> assertThat(findWish.getQuantity()).isEqualTo(savedWish.getQuantity())
        );
    }

    @Test
    @DisplayName("위시 위시 아이디와 멤버 아이디로 조회 테스트")
    void findByIdAndMemberId() {
        // given
        Product expectedProduct = new Product("아메리카노",2000,"http://example.com/americano");
        entityManager.persist(expectedProduct);
        Member expectedMember = new Member("a@a.com","1234");
        entityManager.persist(expectedMember);
        entityManager.flush();
        int expectedQuantity = 2;
        Wish expected = new Wish(expectedMember,expectedProduct,expectedQuantity);
        Wish savedWish = wishes.save(expected);
        entityManager.flush();
        entityManager.clear();

        // when
        Wish findWish = wishes.findByIdAndMemberId(savedWish.getId(),savedWish.getMember().getId()).get();

        // then
        assertAll(
                () -> assertThat(findWish.getId()).isNotNull(),
                () -> assertThat(findWish.getMember().getEmail()).isEqualTo(savedWish.getMember().getEmail()),
                () -> assertThat(findWish.getProduct().getName()).isEqualTo(savedWish.getProduct().getName()),
                () -> assertThat(findWish.getQuantity()).isEqualTo(savedWish.getQuantity())
        );
    }

    @Test
    void deleteById() {
        // given
        Product expectedProduct = new Product("아메리카노",2000,"http://example.com/americano");
        Member expectedMember = new Member("a@a.com","1234");
        int expectedQuantity = 2;
        Wish expected = new Wish(expectedMember,expectedProduct,expectedQuantity);
        Wish savedWish = wishes.save(expected);

        // when
        wishes.deleteById(savedWish.getId());

        // then
        List<Wish> findWishes = wishes.findAll();
        assertAll(
                () -> assertThat(findWishes.size()).isEqualTo(0)
        );
    }
}