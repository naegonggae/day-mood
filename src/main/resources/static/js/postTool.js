// 좋아요
async function pushLike(postId) {
  try {
    const response = await fetch(`/api/v1/posts/${postId}/likes`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getTokenFromCookie(),
      },
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      console.log('좋아요 이벤트 성공: ' + result);
      const likedByMe = result.result.push;
      const likesCount = result.result.count;

      const likeIcon = document.getElementById(`likeButton-${postId}`);
      const likeCountElement = document.querySelector(`.likeCount-${postId}`);

      likeCountElement.innerText = likesCount;
      likeIcon.className = `fa ${likedByMe ? 'fa-heart text-danger' : 'fa-heart-o'}`;
    } else {
      console.log('좋아요 이벤트 실패. HTTP 상태 코드:', response.status);
      console.log('좋아요 이벤트 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('좋아요 이벤트 에러 발생:', error);
  }
}

// 댓글 열기
function toggleCommentForm(postId) {
  const commentFormContainer = document.getElementById(`commentFormContainer-${postId}`);
  commentFormContainer.style.display = commentFormContainer.style.display === 'none' ? 'block' : 'none';

  if (commentFormContainer.style.display === 'block') {
    loadComments(postId);
  }
}

// 댓글 로드
async function loadComments(postId) {
  try {
    const commentListContainer = document.getElementById(`commentList-${postId}`);
    const commentFormContainer = document.getElementById(`commentFormContainer-${postId}`);

    const response = await fetch(`/api/v1/posts/${postId}/comments`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      console.log('댓글 로드 성공: '+result);
      commentListContainer.innerHTML = '';

      result.result.content.forEach(comment => {
        const commentElement = createCommentElement(comment, postId);
        commentListContainer.appendChild(commentElement);
      });

      // 댓글 보기 버튼을 누르면 다시 눌러서 숨기기까지 가능하도록
      commentFormContainer.style.display = 'block';
    } else {
      console.log('댓글 로드 실패. HTTP 상태 코드:', response.status);
      console.log('댓글 로드 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('Error during Comment request:', error);
  }
}
function createCommentElement(comment, postId) {
  const commentElement = document.createElement('div');
  commentElement.innerHTML = `
                    <div class="comment-${comment.id}" data-comment-id="1">
                      <div class="cont-cmt-header">
                        <div class="cont-cmt-user">
                          <i onclick="goToUserInfo(${comment.userId})" class="fa fa-user-circle" aria-hidden="true"></i>
                          <a onclick="goToUserInfo(${comment.userId})" class="nickName">${comment.nickName}</a>
                          <a class="cmt-time-ago"> ${comment.updatedAt}</a>
                        </div>
                      </div>
                      <div class="cmt-info">
                        <p class="cmt-id">${comment.id}</p>
                        <p>${comment.content}</p>
                      </div>
                      <a class="reply-btn" id="comment-${comment.id}" onclick="toggleReplyForm(${postId}, ${comment.id})">답글 보기</a>
                      <div class="reply-set" id="replyFormContainer-${comment.id}" style="display: none;">
                        <form class="d-flex">
                          <div class="form-group flex-grow-1">
                            <input class="form-control" id="replyContent-${comment.id}" name="reply" rows="5" placeholder="답글을 입력하세요." required></input>
                          </div>
                          <button type="button" class="btn btn-primary" onclick="addReply(${postId}, ${comment.id})">
                            <i class="fa fa-angle-up" aria-hidden="true"></i>
                          </button>
                        </form>
                        <div id="replyList-${comment.id}" class="reply-list">
                          <!-- 여기에 답글 리스트가 들어갑니다 -->
                        </div>
                      </div>
                    </div>
    `;
  return commentElement;
}

// 댓글 작성
async function comment(postId) {
  try {
    const contentInput = document.getElementById(`commentContent-${postId}`);
    const content = contentInput.value.trim();

    if (content === '') {
      alert('댓글 내용을 입력하세요.');
      return;
    }

    const data = {
      content : content,
    };

    const response = await fetch('/api/v1/posts/'+postId+'/comments', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getTokenFromCookie(),
      },
      body: JSON.stringify(data),
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      console.log('Comment 요청 성공:', result);
      contentInput.value = '';
      await loadComments(postId);
    } else {
      console.log('Comment 실패. HTTP 상태 코드:', response.status);
      console.log('Comment 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('Error during Comment request:', error);
  }
}

// 답글 열기
function toggleReplyForm(postId, commentId) {
  const replyFormContainer = document.getElementById(`replyFormContainer-${commentId}`);
  replyFormContainer.style.display = replyFormContainer.style.display === 'none' ? 'block' : 'none';

  if (replyFormContainer.style.display === 'block') {
    loadReplies(postId, commentId);
  }
}

// 답글 로드
async function loadReplies(postId, commentId) {
  try {
    const replyFormContainer = document.getElementById(`replyFormContainer-${commentId}`);
    const replyListContainer = document.getElementById(`replyList-${commentId}`);

    const response = await fetch(`/api/v1/posts/${postId}/comments/${commentId}/replies`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      console.log('답글 로드 성공:', result);
      replyListContainer.innerHTML = '';
      result.result.content.forEach(reply => {
        const replyElement = createReplyElement(reply);
        replyListContainer.appendChild(replyElement);
      });

      replyFormContainer.style.display = 'block';
    } else {
      console.log('답글 로드 실패. HTTP 상태 코드:', response.status);
      console.log('답글 로드 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('Error during Reply request:', error);
  }
}
function createReplyElement(reply) {
  const replyElement = document.createElement('div');
  replyElement.innerHTML = `
                    <div class="reply-${reply.id}" data-reply-id="${reply.id}">
                      <div class="cont-rp-header">
                        <div class="cont-rp-user">
                          <i onclick="goToUserInfo(${reply.userId})" class="fa fa-user-circle" aria-hidden="true"></i>
                          <a onclick="goToUserInfo(${reply.userId})" class="nickName">${reply.nickName}</a>
                          <a class="rp-time-ago"> ${reply.updatedAt}</a>
                        </div>
                      </div>
                      <div class="rp-info">
                        <p class="rp-id">${reply.id}</p>
                        <p>${reply.content}</p>
                      </div>
                    <div>
    `;
  return replyElement;
}

// 답글 등록
async function addReply(postId, commentId) {
  try {
    const replyContent = document.getElementById(`replyContent-${commentId}`);
    const content = replyContent.value.trim();

    if (content === '') {
      alert('답글 내용을 입력하세요.');
      return;
    }

    const data = {
      content: content,
    };

    const response = await fetch(`/api/v1/posts/${postId}/comments/${commentId}/replies`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getTokenFromCookie(),
      },
      body: JSON.stringify(data),
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      console.log('Reply 요청 성공:', result);
      replyContent.value = '';
      await loadReplies(postId, commentId);
    } else {
      console.log('Reply 실패. HTTP 상태 코드:', response.status);
      console.log('Reply 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('Error during Reply request:', error);
  }
}