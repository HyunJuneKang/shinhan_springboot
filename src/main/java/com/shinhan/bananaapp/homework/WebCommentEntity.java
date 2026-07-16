package com.shinhan.bananaapp.homework;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "tbl_web_comment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WebCommentEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String writer;

    @Column(nullable = false, length = 50)
    private String writerId;

    @CreationTimestamp @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private WebPostEntity post;

    public void update(String content) { this.content = content; }
}