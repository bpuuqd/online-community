package org.study.community.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.study.community.domain.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 10, nullable = false)
    private String author;

    @Column(name="writer_id")
    private Long writerId;

    @Builder
    public Posts(Long id, String title, String content, String author, Long writerId){
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.writerId = writerId;
    }

    public void update(String title,String content){
        this.title = title;
        this.content = content;
    }
}
