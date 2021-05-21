package org.study.community.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.study.community.domain.posts.Posts;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title;
    private String content;
    private String author;
    private Long writerId;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author, Long writerId){
        this.title = title;
        this.content = content;
        this.author = author;
        this.writerId = writerId;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .writerId(writerId)
                .build();
    }
}
