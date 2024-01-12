package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.comment.CommentSaveResponse;
import com.final_project_leesanghun_team2.domain.dto.comment.ReplySaveResponse;
import com.final_project_leesanghun_team2.domain.entity.Comment;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.domain.dto.comment.request.CommentSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.request.CommentUpdateRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.request.ReplySaveRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.CommentFindResponse;
import com.final_project_leesanghun_team2.domain.dto.comment.ReplyFindResponse;
import com.final_project_leesanghun_team2.exception.comment.NoSuchCommentException;
import com.final_project_leesanghun_team2.exception.post.NoSuchPostException;
import com.final_project_leesanghun_team2.exception.user.PermissionDeniedException;
import com.final_project_leesanghun_team2.repository.comment.CommentRepository;
import com.final_project_leesanghun_team2.repository.post.PostRepository;
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

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 등록
    @Transactional
    public CommentSaveResponse saveComment(Long id, CommentSaveRequest request, User user) {

        // 게시물
        Post findPost = postRepository.findById(id).orElseThrow(NoSuchPostException::new);

        // 댓글 생성
        Comment comment = Comment.createComment(request, findPost, user);
        Comment savedComment = commentRepository.save(comment);

        return CommentSaveResponse.from(savedComment);
    }

    // 답글 등록
    @Transactional
    public ReplySaveResponse saveReply(Long parentId, Long postId, ReplySaveRequest request, User user) {

        // 게시물
        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchPostException::new);

        // Parents 댓글
        Comment findComment = commentRepository.findById(parentId).orElseThrow(NoSuchCommentException::new);

        // 답글 생성
        Comment reply = Comment.createReply(request, findPost, findComment, user);
        Comment savedReply = commentRepository.save(reply);

        return ReplySaveResponse.from(savedReply);
    }

    // 댓글 전체 조회
    public Page<CommentFindResponse> findAllComments(Long id, Pageable pageable) {

        // 게시물
        Post findPost = postRepository.findById(id).orElseThrow(NoSuchPostException::new);

        // Parents 댓글이 null 인 게시물의 댓글들
        return commentRepository.findAllByPostAndParentIsNull(findPost, pageable).map(CommentFindResponse::from);
    }

    // 답글 전체 조회
    public Page<ReplyFindResponse> findAllReplies(Long parentId, Long postId, Pageable pageable) {

        // 게시물
        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchPostException::new);

        // Parents 댓글
        Comment findComment = commentRepository.findById(parentId).orElseThrow(NoSuchCommentException::new);

        // Parents 댓글의 답글들
        return commentRepository.findAllByPostAndParentComment(findPost, findComment, pageable).map(ReplyFindResponse::from);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long postId, Long cmtId, CommentUpdateRequest request, User user) {

        // 게시물
        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchPostException::new);

        // 댓글
        Comment findComment = commentRepository.findById(cmtId).orElseThrow(NoSuchCommentException::new);

        // Comment User == login User 일 때 수정
        checkPermission(user, findComment);

        findComment.update(request);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long postId, Long cmtId, User user) {

        // 게시물
        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchPostException::new);

        // 댓글
        Comment findComment = commentRepository.findById(cmtId).orElseThrow(NoSuchCommentException::new);

        // Comment User == login User 일 때 삭제
        checkPermission(user, findComment);

        // 논리적 삭제
        findComment.softDelete();
    }

    private void checkPermission(User user, Comment findComment) {
        if (!Objects.equals(findComment.getUser().getId(), user.getId())) {
            throw new PermissionDeniedException();
        }
    }
}
