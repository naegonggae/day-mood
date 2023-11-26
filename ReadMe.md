# ✉️ Simple SNS Project

<br/>
<br/>

### 💁 Introduce

----------------------
Simple SNS는 회원가입, 로그인, 글 작성, 댓글, 좋아요, 마이피드조회 기능을 구현한 SNS API입니다.

<br/>

### ⚒️ Tool

------------------
<div>
    <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />
    <img src="https://img.shields.io/badge/spring-6DB33F?style=flat&logo=spring&logoColor=white" />
    <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=springboot&logoColor=white" />
    <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat&logo=springsecurity&logoColor=white" />
    <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white" />
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white" />
    <img src="https://img.shields.io/badge/AmazonAWS-232F3E?style=flat&logo=AmazonAWS&logoColor=white" />
    <img src="https://img.shields.io/badge/JUnit5-25A162?style=flat&logo=JUnit5&logoColor=white" />
    <img src="https://img.shields.io/badge/GitLab-FC6D26?style=flat&logo=GitLab&logoColor=white" />

</div>
<br/>

### 🖇️ Swagger URL

---------

> http://ec2-54-180-91-171.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html

<br/>

### 📋 기술 스택

-------
| 분류            | 종류 / 버전              |
|:--------------|:---------------------|
| **언어**        | **Java 11**          |
| **Framework** | **SpringBoot 2.7.5** |
| **Build**        | **Gradle 7.4**       |
| **DB**        | **MySQL 8.0**        |
| **CI/CD**     | **GitLab**           |
| **Server**        | **Amazon EC2**       |
| **IDE**        | **IntelliJ**           |

<br/>

### ⚙️ ERD

--------------
![SNS_ERD](https://user-images.githubusercontent.com/99169063/211485788-ea2a16d4-4296-4ecc-b581-99eed881c674.png)

<br/>

### 📝 End Point

------

|      METHOD       | Description               | URL |
|:------------|:-----------------|:-----------------------------------------|
| **Post**    | **회원가입**         | **api/v1/users/join**                    |
| **Post**    | **로그인**          | **api/v1/users/login**                   |
| **Post**    | **Post 작성**      | **api/v1/posts**                         |
| **Get**     | **Post 조회**      | **api/v1/posts**                         |
| **Get**     | **Post 1개 조회**   | **api/v1/posts/{postId}**                |
| **Put**     | **Post 수정**      | **api/v1/posts/{id}**                    |
| **Delete**  | **Post 삭제**      | **api/v1/posts/{postId}**                |
| **Post**    | **댓글 작성**        | **api/v1/posts/{postsId}/comments**      |
| **Get**     | **댓글 조회**        | **api/v1/posts/{postsId}/comments**      |
| **Put**     | **댓글 수정**        | **api/v1/posts/{postsId}/comments/{id}** |
| **Delete**  | **댓글 삭제**        | **api/v1/posts/{postsId}/comments/{id}** |
| **Post**    | **좋아요 누르기**      | **api/v1/posts/{postId}/likes**          |
| **Get**     | **좋아요 조회**       | **api/v1/posts/{postId}/likes**          |
| **Get**     | **마이피드 조회**      | **api/v1/posts/my**                      |

<br/>

### ❗️ Error Code

------
| **에러 코드** | 설명 |
|:------------------------------|:-----------------------|
| **DUPLICATED_USER_NAME**      | **UserName이 중복됩니다.**   |
| **USERNAME_NOT_FOUND**        | **해당 UserName이 없습니다.** |
| **INVALID_PASSWORD**          | **패스워드가 잘못되었습니다.**     |
| **POST_NOT_FOUND**            | **해당 포스트가 없습니다.**      |
| **INVALID_PERMISSION**        | **사용자가 권한이 없습니다.**     |
| **DATABASE_ERROR**            | **해당 정보에 문제가 생겼습니다.**  |
| **INVALID_TOKEN**             | **잘못된 토큰입니다.**         |
| **COMMENT_NOT_FOUND**         | **해당 댓글이 없습니다.**       |
| **ALREADY_LIKED**             | **이미 like를 눌렀습니다.**    |

<br/>