package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.domain.request.CommentAddRequest;
import com.final_project_leesanghun_team2.domain.request.CommentModifyRequest;
import com.final_project_leesanghun_team2.domain.response.CommentDeleteResponse;
import com.final_project_leesanghun_team2.domain.response.CommentModifyResponse;
import com.final_project_leesanghun_team2.domain.response.CommentShowResponse;
import com.final_project_leesanghun_team2.exception.ErrorCode;
import com.final_project_leesanghun_team2.exception.UserSnsException;
import com.final_project_leesanghun_team2.repository.CommentRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /** 댓글 등록 **/
    @Transactional
    public CommentShowResponse add(Integer postsId, CommentAddRequest commentAddRequest,
                                   Authentication authentication) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(postsId)
                .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        // UserName 정보 유무 체크
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UserSnsException(ErrorCode.USERNAME_NOT_FOUND));

        // 해당 댓글 정보 DB에 저장
        Comment savedComment = commentRepository.save(Comment.of(commentAddRequest.getComment(), user, post));

        // Comment -> CommentShowResponse 형태로 포장
        return CommentShowResponse.of(savedComment);
    }

    /** 댓글 조회 **/
    public Page<CommentShowResponse> showAll(Integer id, Pageable pageable) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        // 해당 postId에 등록된 댓글을 전부 호출
        Page<Comment> comments = commentRepository.findAllByPost(post, pageable);

        return CommentShowResponse.toList(comments);
    }

    /** 댓글 수정 **/
    public CommentModifyResponse modify(Integer postsId, Integer id, CommentModifyRequest commentModifyRequest,
                                        Authentication authentication) {

        // 해당 id의 댓글 유무 체크
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new UserSnsException(ErrorCode.COMMENT_NOT_FOUND));

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(postsId)
                .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        // UserName 정보 유무 체크
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UserSnsException(ErrorCode.USERNAME_NOT_FOUND));

        // 댓글에 저장된 id == 로그인 할때 id 체크
        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new UserSnsException(ErrorCode.INVALID_PERMISSION);
        }

        // 댓글에 저장된 포스트 id == 해당 포스트 id 체크
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new UserSnsException(ErrorCode.INVALID_PERMISSION);
        }

        // 새로운 댓글 정보 DB에 저장
        comment.setComment(commentModifyRequest.getComment());
        Comment savedComment = commentRepository.save(comment);

        // Comment -> CommentModifyResponse 형태로 포장
        return CommentModifyResponse.ofModify(savedComment);
        // return new CommentModifyResponse(savedComment);
    }

    /** 댓글 삭제 **/
    @Transactional
    public CommentDeleteResponse delete(Integer postId, Integer id, Authentication authentication) {

        // 해당 id의 댓글 유무 체크
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new UserSnsException(ErrorCode.COMMENT_NOT_FOUND));

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        // UserName 정보 유무 체크
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UserSnsException(ErrorCode.USERNAME_NOT_FOUND));

        // 댓글에 저장된 id == 로그인 할때 id 체크
        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new UserSnsException(ErrorCode.INVALID_PERMISSION);
        }

        // 댓글에 저장된 포스트 id == 해당 포스트 id 체크
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new UserSnsException(ErrorCode.INVALID_PERMISSION);
        }
        // 해당 댓글 삭제
        commentRepository.delete(comment);

        // Comment -> CommentDeleteResponse 로 포장
        return CommentDeleteResponse.of(comment);
    }
}
