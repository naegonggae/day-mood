package com.final_project_leesanghun_team2.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.final_project_leesanghun_team2.domain.dto.post.request.PostSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.post.request.PostUpdateRequest;
import java.util.ArrayList;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(nullable = false, length = 500)
    private String content;
    @Column(nullable = false)
    private Long likeCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TagPost> tagPostList = new ArrayList<>();

    // 생성 메서드
    public static Post createPost(PostSaveRequest request, User user) {
        return new Post(request, user);
    }
    public Post(PostSaveRequest request, User user) {
        this.content = request.getContent();
        this.likeCount = 0L;
        this.user = user;
    }
    public void addTagPost(List<TagPost> tagPostList) {
        this.tagPostList = tagPostList;
    }

    // 수정
    public void update(PostUpdateRequest request, List<TagPost> tagPostList) {
        this.content = request.getContent();
        this.tagPostList = tagPostList; // 덮어씌워짐
    }

    // 좋아요 증가 메서드
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 좋아요 감소 메서드
    public void decreaseLikeCount() {
        this.likeCount--;
    }

}