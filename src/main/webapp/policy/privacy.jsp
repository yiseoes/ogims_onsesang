<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
  <title>개인정보처리방침</title>
  <link rel="stylesheet" href="<c:url value='/css/site.css'/>">
</head>
<body class="theme-warm">
  <div class="page-wrap">

    <h1 class="page-title">개인정보처리방침</h1>

    <!-- 카드형 목차 -->
    <div class="form-wrap" style="margin-top:12px">
      <h2 class="form-title" style="margin-bottom:10px">목차</h2>
      <ol class="policy-toc">
        <li><a href="#p1">제1조 개인정보의 처리 목적</a></li>
        <li><a href="#p2">제2조 처리하는 개인정보 항목</a></li>
        <li><a href="#p3">제3조 처리 및 보유기간</a></li>
        <li><a href="#p4">제4조 개인정보의 제3자 제공</a></li>
        <li><a href="#p5">제5조 개인정보처리의 위탁</a></li>
        <li><a href="#p6">제6조 정보주체의 권리·의무</a></li>
        <li><a href="#p7">제7조 개인정보의 파기</a></li>
        <li><a href="#p8">제8조 안전성 확보 조치</a></li>
        <li><a href="#p9">제9조 개인정보 보호책임자</a></li>
        <li><a href="#p10">제10조 고지 및 시행일</a></li>
      </ol>
    </div>

    <!-- 본문 카드 -->
    <div class="form-wrap">
      <p>온세상(이하 “서비스”)은 개인정보 보호법 제30조에 따라 이용자의 개인정보를 보호하고 권익을 보장하기 위하여 다음과 같이 개인정보처리방침을 수립·공개합니다.</p>

      <h3 id="p1" class="policy-h">제1조(개인정보의 처리 목적)</h3>
      <ul class="policy-list">
        <li>회원 가입 및 관리</li>
        <li>봉사 신청 및 활동 관리</li>
        <li>공지·민원 처리 및 고지사항 전달</li>
      </ul>

      <h3 id="p2" class="policy-h">제2조(처리하는 개인정보 항목)</h3>
      <p>성명, 아이디, 비밀번호, 생년월일, 연락처, 이메일, 접속·로그 기록 등</p>

      <h3 id="p3" class="policy-h">제3조(개인정보의 처리 및 보유기간)</h3>
      <p>목적 달성 시 지체 없이 파기합니다. 다만 관련 법령에 따라 보관이 필요한 경우 해당 기간까지 보관합니다.</p>

      <h3 id="p4" class="policy-h">제4조(개인정보의 제3자 제공)</h3>
      <p>원칙적으로 제3자에게 제공하지 않습니다. 법령 근거 또는 수사기관의 요청이 있는 경우 예외로 합니다.</p>

      <h3 id="p5" class="policy-h">제5조(개인정보처리의 위탁)</h3>
      <p>서비스 제공을 위하여 필요한 경우 수탁자에게 위탁할 수 있으며, 위탁계약 시 개인정보 보호 관련 법령을 준수합니다.</p>

      <h3 id="p6" class="policy-h">제6조(정보주체의 권리·의무)</h3>
      <p>이용자는 개인정보 열람·정정·삭제·처리정지 요구 등 권리를 행사할 수 있습니다.</p>

      <h3 id="p7" class="policy-h">제7조(개인정보의 파기)</h3>
      <p>보유기간 경과 또는 처리 목적 달성 시 지체 없이 파기합니다.</p>

      <h3 id="p8" class="policy-h">제8조(안전성 확보 조치)</h3>
      <ul class="policy-list">
        <li>접근권한 관리 및 최소권한 원칙</li>
        <li>비밀번호/중요정보 암호화 저장</li>
        <li>접속기록 보관 및 위·변조 방지</li>
        <li>물리적 보호조치(전산실, 자료보관실 등)</li>
      </ul>

      <h3 id="p9" class="policy-h">제9조(개인정보 보호책임자)</h3>
      <p>책임자: 홍길동 / 이메일: privacy@onseworld.org / 전화: 02-1234-5678</p>

      <h3 id="p10" class="policy-h">제10조(고지 및 시행일)</h3>
      <p>본 방침은 2025년 8월 26일부터 적용됩니다.</p>

      <div class="actions btn-row">
        <a class="btn secondary" href="<c:url value='/'/>">홈으로</a>
        <a class="btn" href="#top">맨 위로</a>
      </div>
    </div>

  </div>
</body>
</html>
