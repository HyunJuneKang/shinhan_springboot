package com.shinhan.bananaapp.mybatisprac.springbasic.section3;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * ╔══════════════════════════════════════════════════════════════════════╗
 * ║  PeopleV2 — 생성자 주입 방식 (Constructor Injection, 권장 방식)      ║
 * ╠══════════════════════════════════════════════════════════════════════╣
 * ║  필드 주입(@Autowired on field) 방식의 문제점:                        ║
 * ║    - 테스트 시 Mock 주입이 어려움                                     ║
 * ║    - 순환 참조를 컴파일 시점에 감지 못함                              ║
 * ║    - 불변(final) 필드 선언 불가                                       ║
 * ║                                                                      ║
 * ║  생성자 주입의 장점:                                                  ║
 * ║    - final 필드 → 불변 보장                                          ║
 * ║    - 생성자만 보면 의존성이 한눈에 보임                               ║
 * ║    - Spring 없이도 new PeopleV2(...) 테스트 가능                     ║
 * ║    - Spring 4.3+ : 생성자가 1개면 @Autowired 생략 가능               ║
 * ╚══════════════════════════════════════════════════════════════════════╝
 */
@Getter
//@Component("personV2")
public class PeopleV2 {

    // final → 생성자 주입 시 불변 보장
    private final String name;
    private final int age;
    private final String birthday;
    private final List<String> emails;
    private final Set<String> friends;
    private final List<Book1> books;
    private final Map<String, Integer> scores;
    private final Properties contacts;

    /**
     * 생성자 주입 — Spring이 이 생성자를 통해 모든 의존성을 주입
     *
     * @Value : 단순 값 (String, int, SpEL 컬렉션)
     * @Qualifier : 같은 타입 빈이 여럿일 때 이름으로 특정
     * <p>
     * Spring 4.3+: 생성자 1개면 @Autowired 생략 가능
     */
    //@Autowired
    public PeopleV2(
            @Value("김영희") String name,
            @Value("25") int age,
            @Value("1999-07-22") String birthday,
            @Value("#{ {'kim@gmail.com', 'kim@daum.net'} }") List<String> emails,
            @Value("#{ {'홍길동', '최지우', '오세훈'} }") Set<String> friends,
            @Qualifier("bookList") List<Book1> books,
            @Qualifier("scoreMap") Map<String, Integer> scores,
            @Qualifier("contactProps") Properties contacts
    ) {
        System.out.println(">> PeopleV2 생성자 주입 호출");
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.emails = emails;
        this.friends = friends;
        this.books = books;
        this.scores = scores;
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔══════════════════════════════════════════════\n");
        sb.append("║  PeopleV2 (생성자 주입 — 권장 방식)\n");
        sb.append("╠══════════════════════════════════════════════\n");
        sb.append("║  이름      : ").append(name).append("\n");
        sb.append("║  나이      : ").append(age).append("세\n");
        sb.append("║  생일      : ").append(birthday).append("\n");
        sb.append("║  이메일    : ").append(emails).append("\n");
        sb.append("║  친구목록  : ").append(friends).append("\n");
        sb.append("║  보유도서  :\n");
        if (books != null) books.forEach(b -> sb.append("║    - ").append(b).append("\n"));
        sb.append("║  성적      : ").append(scores).append("\n");
        sb.append("║  SNS계정   : ").append(contacts).append("\n");
        sb.append("╚══════════════════════════════════════════════");
        return sb.toString();
    }
}
