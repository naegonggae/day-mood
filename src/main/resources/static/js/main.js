'use strict';
// const baseUrl = 'http://localhost:8080';
const baseUrl = 'ec2-3-38-194-228.ap-northeast-2.compute.amazonaws.com:8080';
const token = getTokenFromCookie();

// 홈 이동
async function goLoginHome() {
  try {
    const response = await fetch('/api/v1/users/check', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
      }
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      const userId = result.result.id;
      window.location.href = `/${userId}`;
    } else {
      console.log('유저 확인 실패. HTTP 상태 코드:', response.status);
      console.log('유저 확인 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('유저 확인 에러 발생:', error);
  }
}

// 로그인
async function login() {
  const username = document.getElementById('loginUsername').value;
  const password = document.getElementById('loginPassword').value;

  const data = {
    username : username,
    password : password
  };

  try {
    const response = await fetch('/api/v1/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    const result = await response.json();
    const accessToken = response.headers.get('Authorization');
    console.log('accessToken', accessToken);

    if (result.resultCode === 'SUCCESS') {
      console.log('로그인 성공!');

      $('#loginModal').modal('hide');
      // window.location.href = `/${result.result.id}`;
    } else {
      console.log('로그인 실패. HTTP 상태 코드:', response.status);
      console.log('로그인 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('Error during login:', error);
  }
}

// 회원가입
async function join() {
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;
  const confirmPassword = document.getElementById('confirmPassword').value;
  const nickName = document.getElementById('nickName').value;

  if (password !== confirmPassword) {
    alert('비밀번호와 확인 비밀번호가 일치하지 않습니다.');
    return;
  }

  const data = {
    username : username,
    password : password,
    nickName : nickName
  };

  try {
    const response = await fetch('/api/v1/users/join', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      console.log('회원가입 성공!');
      $('#signupModal').modal('hide');
      window.location.href = '/';
    } else {
      console.log('회원가입 실패. HTTP 상태 코드:', response.status);
      console.log('회원가입 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('Error during signup:', error);
  }
}

// 쿠키 추출
function getTokenFromCookie() {
  const cookies = document.cookie.split(';');

  for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i].trim();
    if (cookie.startsWith('accessToken=')) {
      // 'Authentication=' 이후의 문자열이 토큰입니다.
      const token = cookie.substring('accessToken='.length);
      return token;
    }
  }
  return null;
}

// 내 상세 페이지 이동
async function goToMyInfo() {
  try {
    const response = await fetch('/api/v1/users/check', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
      }
    });
    const result = await response.json();
    console.log('token', token);

    if (result.resultCode === 'SUCCESS') {
      const userId = result.result.id;
      window.location.href = `/users/myInfo/${userId}`;
    } else {
      console.log('유저 확인 실패. HTTP 상태 코드:', response.status);
      console.log('유저 확인 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('유저 확인 에러 발생:', error);
  }
}

// 유저 상세 페이지 이동
async function goToUserInfo(findUserId) {
  try {
    const response = await fetch('/api/v1/users/check', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
      }
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      const userId = result.result.id;
      if (findUserId === userId) {
        window.location.href = `/users/myInfo/${findUserId}`;
      } else {
        window.location.href = `/userInfo/${findUserId}/loginUser/${result.result.id}`;
      }
    } else {
      console.log('유저 확인 실패. HTTP 상태 코드:', response.status);
      console.log('유저 확인 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('유저 확인 에러 발생:', error);
  }
}

// 피드백 제출
async function feedBackSubmit() {
  const title = document.getElementById("feedBackTitle").value;
  const content = document.getElementById("feedBackContent").value;

  const data = {
    title: title,
    content: content
  };

  try {
    const response = await fetch('/api/v1/feedback', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
      },
      body: JSON.stringify(data)
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      console.log('피드백 제출 성공:', result);
      $('#feedBackModal').modal('hide');
    } else {
      console.log('유저 확인 실패. HTTP 상태 코드:', response.status);
      console.log('유저 확인 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// 태그로 게시물 검색
async function searchPost() {
  try {
    const response = await fetch('/api/v1/users/check', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
      }
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      const keyword = document.getElementById('searchInput').value.trim();
      const userId = result.result.id;
      try {
        const response = await fetch(`/api/v1/tags?tagName=${encodeURIComponent(keyword)}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });
        const tagResult = await response.json();

        if (tagResult.resultCode === 'SUCCESS') {

          if (keyword === '') {
            alert('키워드를 입력하세요.');
            return false;
          }

          window.location.href = `/search/${userId}?keyword=${encodeURIComponent(keyword)}`;
          return false;
        } else {
          console.log('유저 확인 실패. HTTP 상태 코드:', response.status);
          console.log('유저 확인 실패. 응답값:', tagResult);
          alert("해당 태그의 게시물이 없습니다.");
        }
      } catch (error) {
        console.error('유저 확인 에러 발생:', error.message.json());
      }
    } else {
      console.log('유저 확인 실패. HTTP 상태 코드:', response.status);
      console.log('유저 확인 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('유저 확인 에러 발생:', error);
  }
}

// 설정 모달 열기
async function openMyInfoModal() {
  try {
    const response = await fetch('/api/v1/users/check', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
      }
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      const userNickname = result.result.nickName;
      const myModal = new bootstrap.Modal(document.getElementById('myModal'));

      document.getElementById('userNameSpan').innerText = userNickname;
      myModal.show();
    } else {
      console.log('유저 확인 실패. HTTP 상태 코드:', response.status);
      console.log('유저 확인 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('유저 확인 에러 발생:', error);
  }
}

// 회원정보 수정
async function updateMyInfo() {
  try {
    const response = await fetch('/api/v1/users/check', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
      }
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      const userId = result.result.id;
      try {
        const newNickname = document.getElementById('updateNickname').value;

        const response = await fetch(`/api/v1/users/${userId}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
          },
          body: JSON.stringify({ nickName: newNickname }),
        });
        const result = await response.json();

        if (result.resultCode === 'SUCCESS') {
          console.log('회원 수정 성공:', result);
          $('#myModal').modal('hide');

        } else {
          console.log('회원 수정 실패. HTTP 상태 코드:', response.status);
          console.log('회원 수정 실패. 응답값:', result);
          if (result && result.result && result.result.message !== undefined) {
            alert(result.result.message);
          } else {
            alert(result.message);
          }
        }
      } catch (error) {
        console.error('회원 수정 에러 발생:', error);
      }
    } else {
      console.log('유저 확인 실패. HTTP 상태 코드:', response.status);
      console.log('유저 확인 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('유저 확인 에러 발생:', error);
  }
}

// 유저 삭제
async function deleteMyInfo() {
  try {
    const response = await fetch('/api/v1/users/check', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token,
      }
    });
    const result = await response.json();

    if (result.resultCode === 'SUCCESS') {
      const userId = result.result.id;
      try {
        const confirmed = await showConfirmationDialog('정말로 계정을 삭제하시겠습니까?');

        if (!confirmed) {
          return;
        }

        await fetch(`/api/v1/users/${userId}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
          },
        });

        $('#myModal').modal('hide');
        window.location.href = `/`; // 홈페이지로 이동

      } catch (error) {
        console.error('회원 삭제 에러 발생:', error);
      }
    } else {
      console.log('유저 확인 실패. HTTP 상태 코드:', response.status);
      console.log('유저 확인 실패. 응답값:', result);
      if (result && result.result && result.result.message !== undefined) {
        alert(result.result.message);
      } else {
        alert(result.message);
      }
    }
  } catch (error) {
    console.error('유저 확인 에러 발생:', error);
  }
}

// 정말 삭제할지 경고창
function showConfirmationDialog(message) {
  return new Promise((resolve) => {
    const confirmed = window.confirm(message);
    resolve(confirmed);
  });
}