<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
  <c:when test="${empty user}">
    <section class="section">
      <div class="ui-card ui-card--lg" style="padding:24px 28px;">
        <h2 class="page-title" style="margin:0;">내 정보 보기</h2>
        <p style="margin:14px 0 0;">사용자 정보를 찾을 수 없습니다. 로그인이 필요할 수 있어요.</p>
        <div class="actions btn-row" style="margin-top:12px;">
          <a href="<c:url value='/loginView.do'/>" class="btn">로그인 하러 가기</a>
        </div>
      </div>
    </section>
  </c:when>

  <c:otherwise>
    <section class="section">
      <!-- ✅ 한 장의 카드 안에 제목 + 내용 + 버튼을 모두 포함 -->
   <div class="card card--wide glass-layer">
        <h2 class="page-title" style="margin:0 0 16px;">내 정보 보기</h2>

        <table class="table table--profile" style="width:100%; font-size:15px;">
          <colgroup>
            <col style="width:160px;" />
            <col />
          </colgroup>
          <tbody>
            <tr>
              <th style="padding:14px 16px;">아이디</th>
              <td style="padding:14px 16px;"><c:out value="${user.userId}"/></td>
            </tr>
            <tr>
              <th style="padding:14px 16px;">이름</th>
              <td style="padding:14px 16px;"><c:out value="${user.name}"/></td>
            </tr>
            <tr>
              <th style="padding:14px 16px;">생년월일</th>
              <td style="padding:14px 16px;">
                <c:catch var="bdErr">
                  <fmt:formatDate value="${user.birthDate}" pattern="yyyy-MM-dd" var="bdStr"/>
                </c:catch>
                <c:choose>
                  <c:when test="${empty bdErr and not empty bdStr}">${bdStr}</c:when>
                  <c:otherwise><c:out value="${user.birthDate}"/></c:otherwise>
                </c:choose>
              </td>
            </tr>
            <tr>
              <th style="padding:14px 16px;">전화번호</th>
              <td style="padding:14px 16px;"><c:out value="${user.phone}"/></td>
            </tr>
            <tr>
              <th style="padding:14px 16px;">성별</th>
              <td style="padding:14px 16px;">
                <c:choose>
                  <c:when test="${user.gender == 'M'}">남</c:when>
                  <c:when test="${user.gender == 'F'}">여</c:when>
                  <c:otherwise><c:out value="${user.gender}"/></c:otherwise>
                </c:choose>
              </td>
            </tr>
            <tr>
              <th style="padding:14px 16px;">지역</th>
              <td style="padding:14px 16px;"><c:out value="${user.region}"/></td>
            </tr>
            <tr>
              <th style="padding:14px 16px;">카테고리</th>
              <td style="padding:14px 16px;"><c:out value="${user.category}"/></td>
            </tr>
          </tbody>
        </table>

        <!-- 링크 URL 변수화 -->
        <c:set var="login" value="${sessionScope.user != null ? sessionScope.user : sessionScope.loginUser}" />
        <c:url var="updateUrl" value="/updateUserView.do"><c:param name="userId" value="${user.userId}"/></c:url>
        <c:url var="pwdUrl"    value="/updatePwdView.do"><c:param name="userId" value="${user.userId}"/></c:url>
        <c:url var="deleteUrl" value="/deleteAccView.do"/>

        <!-- 버튼도 카드 내부로 -->
        <div class="actions btn-row justify-end" style="margin-top:14px;">
          <a href="javascript:history.back()" class="btn ui-btn--ghost">뒤로</a>
          <c:if test="${not empty login and login.userId == user.userId}">
            <a class="btn" href="${updateUrl}">정보 수정</a>
            <a class="btn" href="${pwdUrl}">비밀번호 변경</a>
            <a class="btn secondary" href="${deleteUrl}">회원 탈퇴</a>
          </c:if>
        </div>
      </div>
    </section>
  </c:otherwise>
</c:choose>
