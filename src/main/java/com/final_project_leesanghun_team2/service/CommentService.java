package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.comment.CommentSaveResponse;
import com.final_project_leesanghun_team2.domain.dto.comment.ReplySaveResponse;
import com.final_project_leesanghun_team2.domain.entity.Comment;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.domain.dto.comment.CommentSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.CommentUpdateRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.ReplySaveRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.CommentFindResponse;
import com.final_project_leesanghun_team2.domain.dto.comment.ReplyFindResponse;
import com.final_project_leesanghun_team2.exception.comment.NoSuchCommentException;
import com.final_project_leesanghun_team2.exception.post.NoSuchPostException;
import com.final_project_leesanghun_team2.exception.user.PermissionDeniedException;
import com.final_project_leesanghun_team2.repository.CommentRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 등록
    @Transactional
    public CommentSaveResponse save(Long id, CommentSaveRequest request, User user) {

        // 해당 postId의 포스트 유무 체크
        Post findPost = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        // 댓글 생성
        Comment comment = Comment.createComment(request, findPost, user);

        // 해당 댓글 정보 DB에 저장
        Comment savedComment = commentRepository.save(comment);

        return CommentSaveResponse.from(savedComment);
    }

    // 대댓글 등록
    @Transactional
    public ReplySaveResponse saveReply(Long parentId, Long postId, ReplySaveRequest request, User user) {

        // 해당 postId의 포스트 유무 체크
        Post findPost = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);

        Comment findComment = commentRepository.findById(parentId)
                .orElseThrow(NoSuchCommentException::new);

        // 댓글 생성
        Comment reply = Comment.createReply(request, findPost, findComment, user);

        // 해당 댓글 정보 DB에 저장
        Comment savedReply = commentRepository.save(reply);

        return ReplySaveResponse.from(savedReply);
    }

    // 댓글 전체 조회
    public Page<CommentFindResponse> findAll(Long id, Pageable pageable) {

        // 해당 postId의 포스트 유무 체크
        Post findPost = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        return commentRepository.findAllByPost(findPost, pageable).map(CommentFindResponse::from);
    }

    // 대댓글 전체 조회
    public Page<ReplyFindResponse> findAllReply(Long parentId, Long postId, Pageable pageable) {

        // 해당 postId의 포스트 유무 체크
        Post findPost = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);

        Comment findComment = commentRepository.findById(parentId)
                .orElseThrow(NoSuchCommentException::new);

        return commentRepository.findAllByPostAndParent(findPost, findComment, pageable).map(ReplyFindResponse::from);
    }

    // 댓글 수정
    @Transactional
    public void update(Long postId, Long cmtId, CommentUpdateRequest request, User user) {

        // 해당 postId의 포스트 유무 체크
        Post findPost = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);

        // 해당 id의 댓글 유무 체크
        Comment findComment = commentRepository.findById(cmtId)
                .orElseThrow(NoSuchCommentException::new);

        // 이거 검증도 참 중요해 뭔가 일어날거같지는 않지만
        // 댓글에 저장된 id == 로그인 할때 id 체크
        if (!Objects.equals(findComment.getUser().getId(), user.getId())) {
            throw new PermissionDeniedException();
        }

        // 댓글에 저장된 포스트 id == 해당 포스트 id 체크
        if (!Objects.equals(findComment.getPost().getId(), findPost.getUser().getId())) {
            throw new PermissionDeniedException();
        }

        findComment.update(request);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long postId, Long cmtId, User user) {

        // 해당 postId의 포스트 유무 체크
        Post findPost = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);

        // 해당 id의 댓글 유무 체크
        Comment findComment = commentRepository.findById(cmtId)
                .orElseThrow(NoSuchCommentException::new);

        // 이거 검증도 참 중요해 뭔가 일어날거같지는 않지만
        // 댓글에 저장된 id == 로그인 할때 id 체크
        if (!Objects.equals(findComment.getUser().getId(), user.getId())) {
            throw new PermissionDeniedException();
        }

        // 댓글에 저장된 포스트 id == 해당 포스트 id 체크
        if (!Objects.equals(findComment.getPost().getId(), findPost.getUser().getId())) {
            throw new PermissionDeniedException();
        }

        // 해당 댓글 삭제
        commentRepository.delete(findComment);
    }
}
