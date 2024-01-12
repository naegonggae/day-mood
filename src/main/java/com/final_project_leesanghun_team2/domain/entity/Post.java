package com.final_project_leesanghun_team2.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.final_project_leesanghun_team2.domain.dto.post.request.PostSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.post.request.PostUpdateRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import org.hibernate.annotations.Where;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(nullable = false, length = 500)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TagPost> tagPostList = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "post")
    private Set<Likes> likeList = new HashSet<>();

    // 연관관계 메서드
    public void removePost(Likes like) {
        this.likeList.remove(like);
    }

    // 생성 메서드
    public static Post createPost(PostSaveRequest request, User user) {
        return new Post(request, user);
    }
    public Post(PostSaveRequest request, User user) {
        this.content = request.getContent();
        this.user = user;
    }
    public void addTagPost(List<TagPost> tagPostList) {
        this.tagPostList = tagPostList;
    }

    // 수정
    public void update(PostUpdateRequest request, List<TagPost> tagPostList) {
        this.content = request.getContent();
        this.tagPostList = tagPostList; // 덮어씀
    }
}