package com.shinhan.bananaapp.section2;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * ┌─────────────────────────────────────────────────────────────────┐
 * │  People — DI 주입 대상 자료구조 총정리                            │
 * ├──────────────┬──────────────────────────────────────────────────┤
 * │  필드        │  자료구조                                         │
 * ├──────────────┼──────────────────────────────────────────────────┤
 * │  name        │  String                                          │
 * │  age         │  int  (기본형)                                    │
 * │  birthday    │  String  ("YYYY-MM-DD")                          │
 * │  emails      │  List<String>   - 이메일 여러 개                  │
 * │  friends     │  Set<String>    - 친구 이름 (중복 불허)            │
 * │  books       │  List<Book>     - 소유 도서 목록 (빈 참조)         │
 * │  scores      │  Map<String,Integer>  - 과목별 점수               │
 * │  contacts    │  Properties     - SNS 계정 정보 (key=value)       │
 * └──────────────┴──────────────────────────────────────────────────┘
 */
@Builder@Setter@Getter
@NoArgsConstructor@AllArgsConstructor
public class People {
    // ── 기본 타입 ──────────────────────────────────────────────────
    private String name;
    private int    age;
    private String birthday;
    // ── 컬렉션 ────────────────────────────────────────────────────
    private List<String>         emails;    // list  (중복 허용)
    private Set<String>          friends;   // set   (중복 불허)
    private List<Book>           books;     // list  (빈 객체 참조)
    private Map<String, Integer> scores;    // map   (key-value)
    private Properties           contacts;  // properties (SNS 계정)

    // ═══════════════════════════════════════════════════════════════
    //  Constructor - index / name 두 방식 모두 같은 생성자 사용
    //  XML ① index:  <constructor-arg index="0" value="홍길동"/>
    //  XML ② name:   <constructor-arg name="name" value="홍길동"/>
    // ═══════════════════════════════════════════════════════════════
    public People(String name, int age, String birthday) {
        System.out.println(">> People 생성자 호출: " + name);
        this.name     = name;
        this.age      = age;
        this.birthday = birthday;
    }


    // ── toString (결과 확인용) ─────────────────────────────────────
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔═══════════════════════════════════════════\n");
        sb.append("║  People 정보\n");
        sb.append("╠═══════════════════════════════════════════\n");
        sb.append("║  이름      : ").append(name).append("\n");
        sb.append("║  나이      : ").append(age).append("세\n");
        sb.append("║  생일      : ").append(birthday).append("\n");
        sb.append("║  이메일    : ").append(emails).append("\n");
        sb.append("║  친구목록  : ").append(friends).append("\n");
        sb.append("║  보유도서  :\n");
        if (books != null) books.forEach(b -> sb.append("║    - ").append(b).append("\n"));
        sb.append("║  성적      : ").append(scores).append("\n");
        sb.append("║  SNS계정   : ").append(contacts).append("\n");
        sb.append("╚═══════════════════════════════════════════");
        return sb.toString();
    }
}
