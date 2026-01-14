<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>비밀번호 변경</title>



  <!-- CSS: site.css를 항상 마지막에! 캐시버스터 추가 -->

  
<link rel="stylesheet" href="<c:url value='/css/site.css?v=20250826'/>">
  <script>
    function validateUpdatePwd() {
      var p1 = document.getElementById("newPwd").value;
      var p2 = document.getElementById("newPwd2").value;
      if (!p1.trim() || !p2.trim()) { alert("새 비밀번호를 입력하세요."); return false; }
      if (p1 !== p2) { alert("새 비밀번호가 일치하지 않습니다."); document.getElementById("newPwd2").focus(); return false; }
      if (p1.length < 6) { alert("비밀번호는 6자 이상이어야 합니다."); document.getElementById("newPwd").focus(); return false; }
      return true;
    }
  </script>
</head>

<body class="login-page">
  <%@ include file="/common/top.jspf" %>

  <div class="pwd-card"><!-- ✅ 카드 클래스 -->

  <h2>비밀번호 변경</h2>

  <form method="post"
        action="<c:url value='/updatePwd.do'/>"
        onsubmit="return validateUpdatePwd();">
    <div class="form-group">
      <label for="newPwd">새 비밀번호</label>
      <input type="password" id="newPwd" name="newPwd" required minlength="6" />
    </div>

    <div class="form-group">
      <label for="newPwd2">새 비밀번호 확인</label>
      <input type="password" id="newPwd2" name="newPwd2" required minlength="6" />
    </div>

    <!-- ✅ 버튼 : accent 색 적용됨 -->
    <button type="submit">비밀번호 수정 확인</button>

    <!-- ✅ 취소 링크 : subtle-ink 색 -->
    <a href="${pageContext.request.contextPath}/index.jsp" class="link-cancel">취소</a>
  </form>
</div>

</body>
</html>
