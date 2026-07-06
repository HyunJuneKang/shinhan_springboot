package com.shinhan.bananaapp.section3;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Set;
//
///**
// * ╔════════════════════════════════════════════════════════════════════╗
// * ║  Annotation DI 핵심 3종 세트                                       ║
// * ╠══════════════╦═════════════════════════════════════════════════════╣
// * ║ @Component   ║ 이 클래스를 Spring 빈으로 등록                       ║
// * ║ @Autowired   ║ 타입(Type) 기준으로 빈을 자동 주입                   ║
// * ║ @Qualifier   ║ 같은 타입 빈이 여러 개일 때 이름으로 특정             ║
// * ║ @Value       ║ 단순 값(String, int) 주입                           ║
// * ╚══════════════╩═════════════════════════════════════════════════════╝
// */
////@Getter
////@Component("person")   // 빈 이름을 직접 지정: getBean("person")
//public class People1 {
//    // ── getter ─────────────────────────────────────────────────────
//    // ── ① @Value : 기본 타입 주입 ─────────────────────────────────
//    @Value("홍길동")
//    private String name;
//    @Value("28")
//    private int age;
//
//    @Value("1996-03-15")
//    private String birthday;
//
//    // ── ② @Value : List, Set — SpEL(#{ }) 로 컬렉션 주입 ──────────
//    //    SpEL: #{...} 안에서 Java 표현식 사용 가능
//    @Value("#{ {'hong@gmail.com', 'hong@naver.com', 'hong@work.com'} }")
//    private List<String> emails;
//
//    @Value("#{ {'김철수', '이영희', '박민준'} }")
//    private Set<String> friends;
//
//    // ── ③ @Autowired : 빈 객체 리스트 주입 ────────────────────────
//    //    JavaConfig에서 정의한 List<Book> 빈을 타입으로 자동 주입
//    //    @Qualifier로 빈 이름을 명시해 정확히 특정
//    @Autowired
//    @Qualifier("bookList")          // AppConfig 의 @Bean("bookList") 와 매핑
//    private List<Book1> books;
//
//    // ── ④ @Autowired : Map 주입 ────────────────────────────────────
//    @Autowired
//    @Qualifier("scoreMap")
//    private Map<String, Integer> scores;
//
//    // ── ⑤ @Autowired : Properties 주입 ───────────────────────────
//    @Autowired
//    @Qualifier("contactProps")
//    private Properties contacts;
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("\n╔══════════════════════════════════════════════\n");
//        sb.append("║  People (Annotation 기반 DI)\n");
//        sb.append("╠══════════════════════════════════════════════\n");
//        sb.append("║  이름      : ").append(name).append("\n");
//        sb.append("║  나이      : ").append(age).append("세\n");
//        sb.append("║  생일      : ").append(birthday).append("\n");
//        sb.append("║  이메일    : ").append(emails).append("\n");
//        sb.append("║  친구목록  : ").append(friends).append("\n");
//        sb.append("║  보유도서  :\n");
//        if (books != null) books.forEach(b -> sb.append("║    - ").append(b).append("\n"));
//        sb.append("║  성적      : ").append(scores).append("\n");
//        sb.append("║  SNS계정   : ").append(contacts).append("\n");
//        sb.append("╚══════════════════════════════════════════════");
//        return sb.toString();
//    }
//}
