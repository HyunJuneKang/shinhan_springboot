package com.shinhan.bananaapp.homework;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "tbl_web_post")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WebPostEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String writer;       // 이름

    @Column(nullable = false, length = 50)
    private String writerId;     // Security mid

    @Builder.Default
    private int viewCount = 0;

    @CreationTimestamp @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Builder.Default
    private List<WebCommentEntity> comments = new ArrayList<>();

    public void increaseViewCount() { this.viewCount++; }

    public void update(String title, String content) {
        this.title   = title;
        this.content = content;
    }
}
