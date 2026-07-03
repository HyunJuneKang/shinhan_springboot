package com.shinhan.bananaapp.section2;


import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  InjectionMain — Constructor / Setter Injection 실습 실행 클래스 ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 *  실행 흐름:
 *  1. Spring 컨테이너 생성 (applicationContext.xml 로딩)
 *  2. person1 빈 가져오기 → Constructor index 방식 확인
 *  3. person2 빈 가져오기 → Constructor name 방식 확인
 *  4. 각 필드 개별 출력으로 주입된 자료구조 타입 확인
 */
public class InjectionMain {

    public static void main(String[] args) {

        // ── 1. Spring 컨테이너 시작 ─────────────────────────────
        ClassPathXmlApplicationContext ctx =
                new ClassPathXmlApplicationContext("section2.xml");

        System.out.println("==============================================");
        System.out.println("  ① Constructor Injection — index 방식");
        System.out.println("==============================================");

        // ── 2. person1 (index 방식) ──────────────────────────────
        People p1 = ctx.getBean("person1",People.class);
        System.out.println(p1);

        // 자료구조 타입 확인 출력
        System.out.println("\n[타입 확인]");
        System.out.println("emails  타입 : " + p1.getEmails().getClass().getSimpleName());
        System.out.println("friends 타입 : " + p1.getFriends().getClass().getSimpleName());
        System.out.println("books   타입 : " + p1.getBooks().getClass().getSimpleName());
        System.out.println("scores  타입 : " + p1.getScores().getClass().getSimpleName());
        System.out.println("contacts타입 : " + p1.getContacts().getClass().getSimpleName());

        System.out.println("\n==============================================");
        System.out.println("  ② Constructor Injection — name 방식");
        System.out.println("==============================================");

        // ── 3. person2 (name 방식) ───────────────────────────────
        People p2 = (People) ctx.getBean("person2");
        System.out.println(p2);

        // Set 중복 제거 확인
        System.out.println("\n[Set 중복 제거 확인 - person1 friends]");
        System.out.println("friends = " + p1.getFriends());
        System.out.println("(XML에 '김철수' 2번 입력했지만 Set이므로 1번만 저장됨)\n");

        // Map 개별 접근
        System.out.println("[Map 개별 접근 - person1 scores]");
        p1.getScores().forEach((subject, score) ->
                System.out.printf("  %s : %d점%n", subject, score));

        // Properties 개별 접근
        System.out.println("\n[Properties 개별 접근 - person1 contacts]");
        p1.getContacts().forEach((k, v) ->
                System.out.printf("  %s → %s%n", k, v));

        // ── 4. 컨테이너 종료 ──────────────────────────────────────
        ctx.close();
        System.out.println("\n>> Spring 컨테이너 종료");
    }
}
