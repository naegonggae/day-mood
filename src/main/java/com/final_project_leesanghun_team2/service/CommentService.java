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
    public CommentSaveResponse saveComment(Long id, CommentSaveRequest request, User user) {

        Post findPost = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        // 댓글 생성
        Comment comment = Comment.createComment(request, findPost, user);
        Comment savedComment = commentRepository.save(comment);

        return CommentSaveResponse.from(savedComment);
    }

    // 답글 등록
    @Transactional
    public ReplySaveResponse saveReply(Long parentId, Long postId, ReplySaveRequest request, User user) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);

        Comment findComment = commentRepository.findById(parentId)
                .orElseThrow(NoSuchCommentException::new);

        // 댓글 생성
        Comment reply = Comment.createReply(request, findPost, findComment, user);
        Comment savedReply = commentRepository.save(reply);

        return ReplySaveResponse.from(savedReply);
    }

    // 댓글 전체 조회
    public Page<CommentFindResponse> findAllComments(Long id, Pageable pageable) {

        Post findPost = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        return commentRepository.findAllByPostAndParentNull(findPost, pageable).map(CommentFindResponse::from);
    }

    // 대댓글 전체 조회
    public Page<ReplyFindResponse> findAllReplies(Long parentId, Long postId, Pageable pageable) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);

        Comment findComment = commentRepository.findById(parentId)
                .orElseThrow(NoSuchCommentException::new);

        return commentRepository.findAllByPostAndParent(findPost, findComment, pageable).map(ReplyFindResponse::from);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long postId, Long cmtId, CommentUpdateRequest request, User user) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);

        Comment findComment = commentRepository.findById(cmtId)
                .orElseThrow(NoSuchCommentException::new);

        // Comment User == login User 일 때 수정
        if (!Objects.equals(findComment.getUser().getId(), user.getId())) {
            throw new PermissionDeniedException();
        }

        findComment.update(request);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long postId, Long cmtId, User user) {

        // 해당 postId의 포스트 유무 체크
        Post findPost = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);

        // 해당 id의 댓글 유무 체크
        Comment findComment = commentRepository.findById(cmtId)
                .orElseThrow(NoSuchCommentException::new);

        // Comment User == login User 일 때 삭제
        if (!Objects.equals(findComment.getUser().getId(), user.getId())) {
            throw new PermissionDeniedException();
        }

        commentRepository.delete(findComment);
    }
}
