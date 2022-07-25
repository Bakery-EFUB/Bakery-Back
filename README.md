# 특별한 하루메이커, CAKER 

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FBakery-EFUB%2FBakery-Back&count_bg=%23FF7B72&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

## 🍏 백엔드 팀원 소개

| [김시연](https://github.com/siyeonkm)                                                                                             | [김지인](https://github.com/kji-dec)                                                                       | [김현진](https://github.com/gimkuku)                    |
|:--------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------:|
| <img src = "https://github.com/MINJU-KIMmm/GitHubTest/blob/main/image/porkProfile/KimSiyeon.jpeg" width="80%"/>                    | <img src = "https://github.com/MINJU-KIMmm/GitHubTest/blob/main/image/porkProfile/KimSiyeon.jpeg" width="80%"/>                    | <img src = "https://github.com/MINJU-KIMmm/GitHubTest/blob/main/image/porkProfile/KimSiyeon.jpeg" width="80%"/>                    |
| [로그인] 카카오 로그인, 세션 유지 및 유저별 권한설정 </br> [유저] 유저 추가 및 수정, 유저 탈퇴하기, 유저 프로필 조회 </br> [이미지] s3 presigned url을 사용한 업로드/다운로드 구현 </br> [배포] Github Action 과 CodeDeploy를 이용한 배포자동화 구조 설계 </br> [기타] 데이터베이스 설계, 리드미 작성 등. | [유저] 소셜로그인, 프로필수정, 유저 정보 수정, 학교 인증, 유저 탈퇴 </br> [폴더] 폴더 리스트로 반환</br> [DB] 리뷰 DB 구축 등. | [배포] EC2/loadBalancer/route53을 이용한 배포, https 사용, </br> Github Action 과 CodeDeploy를 이용한 배포자동화 구조 설계</br> [기타] 데이터베이스 설계, 다이어그램 제작 |

-------------------
## 🍰 개요
'CAKER'는 레터링 케이크 주문/판매 플랫폼으로, 레터링 케이크를 주문하고자 하는 구매자와 그에 맞는 서비스를 제공하는 판매자를 서로 연결해주어 기존의 불편함을 해소하고 원하는 케이크 가게를 손쉽게 찾게 해줍니다.

## 🍰 기술 스택    
- DEVELOP &nbsp; 
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=round-square&logo=Spring&logoColor=white) <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Kakao-FFCD00?style=flat-square&logo=Kakao&logoColor=white"/>

- AWS &nbsp;
<img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=Amazon%20AWS&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=AmazonS3&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat-square&logo=Amazon EC2&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=flat-square&logo=Amazon RDS&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon LoadBalancer-E68B49?style=flat-square&logo=Amazon LoadBalancer&logoColor=white"/> <img src="https://img.shields.io/badge/Amazon CodeDeploy-7D9B4B?style=flat-square&logo=Amazon CodeDeploy&logoColor=white"/>

- ETC &nbsp; 
<img src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/> <img src="https://img.shields.io/badge/GitHub -181717?style=flat-square&logo=GitHub&logoColor=white"/></br>

<img src="https://github.com/Bakery-EFUB/Bakery-Back/blob/develop/BakeryArchitectureDiagram.png"/>

## 🍰 라이브러리
1. lombok
2. spring web
3. spring data jpa
4. oauth2 client
5. spring boot test
6. spring session jdbc
7. spring security test
8. amazon awssdk
9. spring cloud aws
10. mysql driver

## 🍰 프로젝트 구조

### 설명
1. main/java/[프로젝트명]/config ▶️ Config
2. main/java/[프로젝트명]/controller ▶ Controller
3. main/java/[프로젝트명]/domain ▶️ Entity
4. main/java/[프로젝트명]/dto ▶️ DTO
5. main/java/[프로젝트명]/exception ▶️ ErrorHandler
6. main/java/[프로젝트명]/respository ▶️ Repository
7. main/java/[프로젝트명]/service ▶️ Service
8. main/java/[프로젝트명]/Application.java
9. main/resources/application.properties ▶️ session 관련 설정
10. main/resources/application-aws.properties ▶️ aws 관련 설정
11. main/resources/application-oauth.properties ▶️ kakao login 관련 설정

### 🍰 폴더 
<pre>
<code>
└── 🗂 main
    ├── 🗂 java
    │   └── 🗂 com
    │       └── 🗂 bakery
    │           └── 🗂 caker
    │               ├── 📑 Application.java
    │               ├── 🗂 config
    │               │   ├── 📑 Authority.java
    │               │   ├── 📑 CORSConfigjava
    │               │   ├── 📑 LoginUser.java
    │               │   ├── 📑 LoginUserArgumentResolver.java
    │               │   ├── 📑 SecurityCOnfig.java
    │               │   └── 📑 WebConfig.java
    │               ├── 🗂 controller
    │               │   ├── 📑 EventController.java
    │               │   ├── 📑 MemberController.java
    │               │   ├── 📑 SheetController.ja
    │               │   └── 📑 StoreController.java
    │               ├── 🗂 domain
    │               │   ├── 📑 BaseTimeEntity.java
    │               │   ├── 📑 Comment.java
    │               │   ├── 📑 Event.java
    │               │   ├── 📑 Member.java
    │               │   ├── 📑 Recomment.java
    │               │   ├── 📑 Sheet.java
    │               │   └── 📑 Store.java
    │               ├── 🗂 dto
    │               ├── 🗂 exception
    │               │   ├── 📑 CustomException.java
    │               │   ├── 📑 ErrorCode.java
    │               │   ├── 📑 ErrorResponse.java
    │               │   └── 📑 GlobalExceptionHandler.java
    │               ├── 🗂 repository
    │               │   ├── 📑 CommentRepository.java
    │               │   ├── 📑 EventRepository.java
    │               │   ├── 📑 MemberRepository.java
    │               │   ├── 📑 RecommentRepository.java
    │               │   ├── 📑 SheetRepository.java
    │               │   └── 📑 StoreRepository.java
    │               ├── 🗂 service
    │               │   ├── 📑 CommentService.java
    │               │   ├── 📑 EventService.java
    │               │   ├── 📑 ImageUploadService.java
    │               │   ├── 📑 MemberService.java
    │               │   ├── 📑 OAuthUserService.java
    │               │   ├── 📑 SheetService.java
    │               └── └── 📑 StoreService.java
    └── 🗂 resources
        ├── 📑 application.properties
        ├── 📑 application-aws.properties
        └── 📑 application-oauth.properties
</code>
</pre>


## 🍰 데이터베이스 설계도(E-R diagram)
<img src = "https://github.com/Bakery-EFUB/Bakery-Back/blob/develop/erd-diagram.PNG"/>

## 🍰 API 명세서
### [🔗 Link](https://www.notion.so/efub/API-6461422a295b47ee831e14a51340c2a0)


