package com.shinhan.bananaapp.mybatisprac.springbasic.section3;//package com.shinhan.bananaapp.section3;
//
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
///**
// * ╔══════════════════════════════════════════════════════════════════╗
// * ║  AnnotationMain — Annotation 기반 컨테이너 실행                  ║
// * ╠══════════════════════════════════════════════════════════════════╣
// * ║  XML 방식:        ClassPathXmlApplicationContext("xxx.xml")      ║
// * ║  Annotation 방식: AnnotationConfigApplicationContext(AppConfig)  ║
// * ╚══════════════════════════════════════════════════════════════════╝
// */
//public class AnnotationMain {
//
//    public static void main(String[] args) {
//
//        // ── 1. Java 설정 클래스로 컨테이너 생성 (XML 없음!) ──────────
//        AnnotationConfigApplicationContext ctx =
//                new AnnotationConfigApplicationContext(AppConfig.class);
//
//        System.out.println("========================================");
//        System.out.println("  Annotation 기반 DI — People 주입 결과");
//        System.out.println("========================================");
//
//        // ── 2. @Component("person") 으로 등록된 빈 가져오기 ──────────
//        People1 person = ctx.getBean("person", People1.class);
//        System.out.println(person);
//
//        // ── 3. @Bean 으로 등록된 개별 Book 빈 확인 ───────────────────
//        System.out.println("\n[개별 Book 빈 직접 조회]");
//        Book1 b1 = ctx.getBean("book1", Book1.class);
//        Book1 b2 = ctx.getBean("book2", Book1.class);
//        System.out.println("book1 → " + b1);
//        System.out.println("book2 → " + b2);
//
//        // ── 4. @Bean 싱글톤 확인 ─────────────────────────────────────
//        // bookList() 안에서 book1()을 호출해도 같은 인스턴스 반환
//        System.out.println("\n[싱글톤 확인]");
//        Book1 fromList = person.getBooks().get(0);
//        Book1 fromContext = ctx.getBean("book1", Book1.class);
//        System.out.println("books.get(0) == getBean(book1) : "
//                + (fromList == fromContext));   // true
//
//        // ── 5. 타입 조회 ──────────────────────────────────────────────
//        System.out.println("\n[자료구조 타입 확인]");
//        System.out.println("emails  : " + person.getEmails().getClass().getSimpleName());
//        System.out.println("friends : " + person.getFriends().getClass().getSimpleName());
//        System.out.println("books   : " + person.getBooks().getClass().getSimpleName());
//        System.out.println("scores  : " + person.getScores().getClass().getSimpleName());
//        System.out.println("contacts: " + person.getContacts().getClass().getSimpleName());
//
//        ctx.close();
//        System.out.println("\n>> Spring 컨테이너 종료");
//    }
//}
