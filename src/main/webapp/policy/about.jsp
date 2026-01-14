<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
  <title>브랜드소개</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>">
</head>
<body class="theme-warm">
  <div class="page-wrap">

    <%-- ===== 본문 ===== --%>
    <style>
      .brand-wrap { --max: 920px; --pad: 24px; --ink:#111827; --muted:#6b7280; --line:#e5e7eb; background:#fff; }
      .brand-hero{max-width:var(--max);margin:80px auto 24px;padding:0 var(--pad);}
      .brand-hero .eyebrow{letter-spacing:.12em;font-size:12px;color:var(--muted);text-transform:uppercase;}
      .brand-hero h1{margin:8px 0 12px;font-size:clamp(28px,4vw,40px);line-height:1.2;color:var(--ink);font-weight:700;}
      .brand-hero p.lead{color:var(--muted);font-size:15px;line-height:1.9;max-width:60ch;}

      .brand-section{max-width:var(--max);margin:40px auto;padding:0 var(--pad);}
      .brand-section h2{font-size:14px;letter-spacing:.08em;color:var(--muted);text-transform:uppercase;margin-bottom:12px;}
      .brand-section p{color:var(--ink);line-height:1.9;font-size:15px;}

      .brand-grid{display:grid;gap:28px;grid-template-columns:1fr;border-top:1px solid var(--line);padding-top:28px;}
      @media(min-width:768px){.brand-grid{grid-template-columns:1fr 1fr;}}
      .brand-block h3{font-size:13px;letter-spacing:.06em;color:var(--muted);text-transform:uppercase;margin-bottom:10px;}
      .brand-block p,.brand-block a,.brand-block address{font-size:15px;color:var(--ink);}
      .brand-block a{text-decoration:none;border-bottom:1px solid var(--line);}
      .brand-block a:hover{border-bottom-color:var(--ink);}

      .brand-tail{max-width:var(--max);margin:48px auto 96px;padding:0 var(--pad);border-top:1px solid var(--line);}

      /* ===== 추가: 우측 일러스트(이 페이지 전용) ===== */
      /* 디버깅(한글): brand-wrap을 위치 기준으로 사용 */
      .brand-wrap { position: relative; }
      .brand-art { 
        display: block;            /* 기본: 항상 보이게 */
        position: static;          /* 모바일/태블릿: 흐름 안에서 아래쪽에 노출 */
        margin: 24px auto 0;
        width: 320px;
      }
      .brand-art img { width: 100%; height: auto; display: block; }

      /* 데스크톱에서만 우측 빈 공간에 고정 배치 */
      @media (min-width: 992px) {
        .brand-art {
          position: absolute;
          right: -380px;          /* -340 ~ -420px 사이로 필요시 조정 */
          top: 12px;
          width: 360px;           /* 320~420px 사이로 필요시 조정 */
          margin: 0;
          z-index: 1;
        }
      }
    </style>

    <main class="brand-wrap" role="main" aria-labelledby="brandHeading">
  <section class="brand-hero">
    <%-- 디버깅(한글): 이미지를 hero 기준으로 배치하기 위해 hero 내부로 이동 --%>
    <figure class="brand-art" aria-hidden="true">
      <img src="<c:url value='/images/okims_Generated.png'/>" alt="">
    </figure>

    <span class="eyebrow">ABOUT</span>
    <h1 id="brandHeading">온세상, 따뜻한 연결을 위한 브랜드</h1>

        <p class="lead">
          온세상은 이웃의 작은 손길이 정확히 닿게 하는 커뮤니티입니다.
          도움을 필요로 하는 사람과 도움을 건네는 사람이 망설임 없이 만나는 경험을 설계합니다.
        </p>
        <p class="lead">
          우리는 쓰임과 정확함을 우선합니다. 단정한 서비스, 정직한 정보, 오래 가는 신뢰.
        </p>
      </section>

      <section class="brand-section" aria-label="brand story">
        <h2>Our Story</h2>

        <p class="lead">
          출발점은 하나의 질문이었습니다. “내 시간을 곁의 누군가에게 어떻게 건넬 수 있을까?”
        </p>
        <p class="lead">
          고령화로 커지는 디지털 소외를 보았습니다. 그래서 사람에게 다시 돌아오는 정보,
          누구나 접근할 수 있는 웹, 약속을 담은 브랜드 ‘온세상’을 선택했습니다.
        </p>
        <p class="lead">
          답은 단순했습니다. 가장 단정한 흐름 위에 따뜻함을 더하는 것.
        </p>

        <p>
          우리는 불필요한 장식을 덜고, 필요한 정보만 또렷하게 보이도록 했습니다.
          누구나 쉽게 확인하고 자연스럽게 도움을 주고받도록 동선을 정리했습니다.
        </p>
        <p>
          ‘햇살소식’은 필요한 소식을 빠르고 정확하게, 
          ‘손길나눔’은 도움을 주고받는 연결을,
          ‘마음부탁’은 말로 하기 어려운 마음을 가볍게 전하도록 돕습니다.
        </p>
        <p>
          작은 선의가 생활을 바꾼다고 믿습니다. 온세상은 오늘도 기본을 지키며 조용히 도움이 되겠습니다.
        </p>
      </section>

      <section class="brand-section" aria-label="contact and info">
        <div class="brand-grid">
          <div class="brand-block">
            <h3>Contact</h3>
            <p>
              <a href="mailto:contact@onworld.example">contact@onsesang.example</a><br/>
              고객센터: 1577-0000 (평일 11:00 - 16:00)
            </p>
          </div>

          <div class="brand-block">
            <h3>Address</h3>
            <address>
              04000 서울특별시 구로구 00 (온세상 2F)
            </address>
          </div>

          <div class="brand-block">
            <h3>Hours</h3>
            <p>
              월 - 금 / 10:30 - 16:00<br/>
              주말 및 공휴일 휴무
            </p>
          </div>

          <div class="brand-block">
            <h3>Social</h3>
            <p>
              <a href="#" aria-label="Instagram">Instagram</a><br/>
              <a href="#" aria-label="KakaoTalk">KakaoTalk</a>
            </p>
          </div>
        </div>
      </section>

      <section class="brand-tail" aria-label="legal">
        <p style="color:#6b7280; font-size:12px; line-height:1.8; margin-top:18px;">
          ⓒ 온세상. All Rights Reserved.
        </p>
      </section>
    </main>

    <%-- 임시 로고 버튼(메인). 중앙 배치, 텍스트 버튼 --%>
    <div style="max-width:1080px; margin:20px auto 0; text-align:center;">
      <a href="<c:url value='/'/>"
         style="display:inline-block; font-size:20px; font-weight:700;
                color:#111827; text-decoration:none; border:2px solid #e5e7eb;
                border-radius:8px; padding:8px 16px; background:#fff;">
        온세상
      </a>
    </div>

  </div>
</body>
</html>
