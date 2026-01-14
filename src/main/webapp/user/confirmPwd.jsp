<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>비밀번호 확인</title>

  <!-- global CSS -->
  <%-- <link rel="stylesheet" href="<c:url value='/css/site.css'/>">  --%>
<link rel="stylesheet" href="<c:url value='/css/site.css?v=20250826'/>">












  <script>
    function validateConfirmPwd() {
      var v = document.getElementById("currentPwd").value.trim();
      if (!v) {
        alert("현재 비밀번호를 입력하세요.");
        document.getElementById("currentPwd").focus();
        return false;
      }
      return true;
    }
  </script>
</head>
<body class="login-page">
  <%@ include file="/common/top.jspf" %>

  <div class="login-card">
    <h2>비밀번호 확인</h2>

    <c:if test="${not empty errorMsg}">
      <div class="error">${errorMsg}</div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/confirmPwd.do"
          onsubmit="return validateConfirmPwd();">
      <div class="form-group">
        <label for="currentPwd">현재 비밀번호</label>
        <input type="password" id="currentPwd" name="currentPwd" autocomplete="current-password" />
      </div>

      <button type="submit">기존 비밀번호 확인</button>
      <a href="${pageContext.request.contextPath}/index.jsp">취소</a>
    </form>
  </div>
</body>
</html>
