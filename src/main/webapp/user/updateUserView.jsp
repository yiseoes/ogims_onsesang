<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.semi.domain.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="u" value="${sessionScope.loginUser}"/>

<%
    // 콘솔 디버그 (선택)
    User uObj = (User)session.getAttribute("loginUser");
    if (uObj != null) {
        System.out.println("[DEBUG] ===== User Info in Session =====");
        System.out.println("name      = " + uObj.getName());
        System.out.println("birthDate = " + uObj.getBirthDate()
                           + " (class=" + (uObj.getBirthDate()!=null ? uObj.getBirthDate().getClass() : "null") + ")");
        System.out.println("phone     = " + uObj.getPhone());
        System.out.println("region    = " + uObj.getRegion());
        System.out.println("gender    = " + uObj.getGender());
        System.out.println("category  = " + uObj.getCategory());
        System.out.println("[DEBUG] ===============================");
    }
%>

<%-- 화면용 값: 파라미터 > 세션 --%>
<c:set var="nameVal"     value="${not empty param.name      ? param.name      : u.name}" />
<c:set var="regionVal"   value="${not empty param.region    ? param.region    : u.region}" />
<c:set var="phoneVal"    value="${not empty param.phone     ? param.phone     : u.phone}" />
<c:set var="genderVal"   value="${not empty param.gender    ? param.gender    : u.gender}" />
<c:set var="categoryVal" value="${not empty param.category  ? param.category  : u.category}" />

<%-- birthdate: LocalDate는 문자열로 그대로 (yyyy-MM-dd) --%>
<c:set var="birthYmd"    value="${not empty param.birthdate ? param.birthdate : (empty u.birthDate ? '' : u.birthDate)}" />

<html>
<head><title>내정보 수정</title>
<link rel="stylesheet" href="<c:url value='/css/site.css'/>">
</head>
<body>
 <main class="container-main">
    <section class="form-wrap">
      <h2 class="form-title">내정보 수정</h2>

      <c:if test="${not empty error}">
        <div class="form-alert">${error}</div>
      </c:if>

      <form action="<c:url value='/updateUser.do'/>" method="post" class="form-grid">
        <div class="form-row">
          <label class="form-label" for="name">이름</label>
          <input type="text" id="name" name="name" class="input" value="${nameVal}">
        </div>

        <div class="form-row">
          <label class="form-label" for="birthdate">생년월일</label>
          <input type="date" id="birthdate" name="birthdate" class="input" value="${birthYmd}">
        </div>

        <div class="form-row">
          <label class="form-label" for="phone">전화번호</label>
          <input type="tel" id="phone" name="phone" class="input" value="${phoneVal}">
        </div>

        <div class="form-row">
          <label class="form-label" for="region">지역</label>
          <input type="text" id="region" name="region" class="input" value="${regionVal}">
        </div>

        <div class="form-row">
          <label class="form-label" for="gender">성별</label>
          <select id="gender" name="gender" class="select">
            <option value="M" ${genderVal=='M'?'selected':''}>남성</option>
            <option value="F" ${genderVal=='F'?'selected':''}>여성</option>
          </select>
        </div>

       <div class="form-row">
  <label class="form-label" for="category">카테고리</label>
  <select id="category" name="category" class="select">
    <option value="" ${empty categoryVal ? 'selected' : ''}>선택 안 함</option>
    <option value="병원동행"  ${categoryVal=='동행' ? 'selected' : ''}>동행</option>
    <option value="청소"      ${categoryVal=='청소' ? 'selected' : ''}>청소</option>
    <option value="장보기"    ${categoryVal=='장보기' ? 'selected' : ''}>장보기</option>
    <option value="배달"      ${categoryVal=='배달' ? 'selected' : ''}>배달</option>
    <option value="집수리"    ${categoryVal=='집수리' ? 'selected' : ''}>집수리</option>
    <option value="요리"      ${categoryVal=='요리' ? 'selected' : ''}>요리</option>
    <option value="밭일"      ${categoryVal=='밭일' ? 'selected' : ''}>밭일</option>
    <option value="미용"      ${categoryVal=='미용' ? 'selected' : ''}>미용</option>
    <option value="벌래잡기"  ${categoryVal=='벌레잡기' ? 'selected' : ''}>벌레잡기</option>
    <option value="소통대화"  ${categoryVal=='소통대화' ? 'selected' : ''}>소통대화</option>
    <option value="목욕"      ${categoryVal=='목욕' ? 'selected' : ''}>목욕</option>
  </select>
</div>


        <div class="actions btn-row">
          <button type="submit" class="btn">수정하기</button>
        </div>
      </form>
    </section>
  </main>
</body>
</html>