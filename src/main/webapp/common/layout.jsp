<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  String ctx  = request.getContextPath();
  String path = request.getRequestURI().substring(ctx.length());
  boolean isHome = "/".equals(path) || "/index.do".equals(path) || "/home.do".equals(path); //
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title><c:out value="${pageTitle != null ? pageTitle : '온세상'}"/></title>

  <link rel="stylesheet" href="<c:url value='/css/site.css'/>">

  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;700&family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Gowun+Dodum&family=Poppins:wght@500;600&display=swap" rel="stylesheet">

  <%-- ✅ 홈을 포함한 모든 페이지에서 테마가 로드되도록 수정 --%>
  <link rel="stylesheet" href="<c:url value='/css/theme-tailwind.css'/>">

  <style>
    :root{
      --hero-bg: url('<c:url value="/images/main.jpg"/>' ); /* */
    }
  </style>
</head>

<body class="<%= isHome ? "" : "theme-warm" %>"
      style="background-image: var(--hero-bg); background-repeat:no-repeat; background-position:center center; background-attachment:fixed; background-size:cover;">
  <%@ include file="/common/top.jspf" %>

  <main class="container-main">
    <div class="page-wrap">
      <jsp:include page="${contentPage}" />
    </div>
  </main>

  <%@ include file="/common/footer.jspf" %>
</body>
</html>