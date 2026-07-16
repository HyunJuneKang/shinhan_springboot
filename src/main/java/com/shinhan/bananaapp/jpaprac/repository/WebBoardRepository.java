//package com.shinhan.bananaapp.jpaprac.repository;
//
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.core.types.Predicate;
//import com.shinhan.bananaapp.jpaprac.entity.entity2.QWebBoardEntity;
//import com.shinhan.bananaapp.jpaprac.entity.entity2.WebBoardEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.querydsl.QuerydslPredicateExecutor;
//
//public interface WebBoardRepository
//        extends JpaRepository<WebBoardEntity, Long>,
//        QuerydslPredicateExecutor<WebBoardEntity> {
//
//    default Predicate makePredicate(String type, String keyword) {
//
//        QWebBoardEntity board = QWebBoardEntity.webBoardEntity;
//        BooleanBuilder builder = new BooleanBuilder();
//
//        if (type == null || type.isBlank()) {
//            return builder;
//        }
//        if (keyword == null || keyword.isBlank()) {
//            return builder;
//        }
//
//        //or title like concat('%',keyword,'%')
//        if (type.contains("t")) {
//            builder.or(board.title.containsIgnoreCase(keyword));
//        }
//        //or content like concat('%',keyword,'%')
//        if (type.contains("c")) {
//            builder.or(board.content.containsIgnoreCase(keyword));
//        }
//        //or writer like concat('%',keyword,'%')
//        if (type.contains("w")) {
//            builder.or(board.writer.containsIgnoreCase(keyword));
//        }
//
//        return builder;
//    }
//}