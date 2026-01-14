<%-- /WEB-INF/views/comment/commentListFragment.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- ListCommentAction 에서 내려준 데이터 정리 --%>
<c:set var="map"       value="${commentMap}" />
<c:set var="list"      value="${empty map ? comments : map.list}" />
<c:set var="count"     value="${empty map ? (empty list ? 0 : list.size()) : map.count}" />
<c:set var="page"      value="${empty map ? (empty param.page ? 1 : param.page) : map.page}" />
<c:set var="pageSize"  value="${empty map ? (empty param.pageSize ? initParam['comment.pageSize'] : param.pageSize) : map.pageSize}" />
<c:set var="totalPage" value="${empty map ? 1 : map.totalPage}" />
<%-- 액션이 내려주는 volunteerId --%>
<c:set var="volunteerId" value="${volunteerId}" />

<div id="comment-area">
  <div>댓글 <strong>${count}</strong>건</div>

  <c:if test="${empty list}">
    <div style="padding:8px 0; color:var(--subtle-ink);">등록된 댓글이 없습니다.</div>
  </c:if>

  <%-- ... 위쪽 동일 --%>
<c:if test="${not empty list}">
  <ul style="list-style:none; padding:0; margin:0;">
    <c:forEach var="cmt" items="${list}">
      <li style="padding:10px; border:1px solid var(--line); border-radius:10px; margin-bottom:8px; background:var(--surface);">
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <div>
            <div style="font-weight:600;">${fn:escapeXml(cmt.authorId)}</div>
            <div style="color:var(--subtle-ink); font-size:12px;">
              <fmt:formatDate value="${cmt.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
            </div>
          </div>
          <div id="act-${cmt.commentId}">
            <c:if test="${sessionScope.user.userId == cmt.authorId}">
              <!-- ✅ 수정 버튼 -->
              <button type="button" class="btn" onclick="toggleEdit('${cmt.commentId}')">수정</button>

              <!-- 삭제 버튼 -->
              <form method="post" action="<c:url value='/deleteComment.do'/>" style="display:inline;">
                <input type="hidden" name="commentId" value="${cmt.commentId}"/>
                <input type="hidden" name="volunteerId" value="${volunteerId}"/>
                <button class="btn" type="submit">삭제</button>
              </form>
            </c:if>
          </div>
        </div>

        <!-- 본문 -->
        <div id="content-${cmt.commentId}" style="margin-top:6px; white-space:pre-line;">
          ${fn:escapeXml(cmt.content)}
        </div>

        <!-- ✅ 인라인 수정 폼 (기본 숨김) -->
        <c:if test="${sessionScope.user.userId == cmt.authorId}">
          <form id="edit-${cmt.commentId}" method="post" action="<c:url value='/updateComment.do'/>"
                style="display:none; margin-top:8px;">
            <input type="hidden" name="commentId" value="${cmt.commentId}"/>
            <input type="hidden" name="volunteerId" value="${volunteerId}"/>
            <textarea name="content" rows="3" maxlength="4000" required
                      style="width:100%; box-sizing:border-box;">${fn:escapeXml(cmt.content)}</textarea>
            <div style="margin-top:8px; display:flex; gap:8px; justify-content:flex-end;">
              <button type="submit" class="btn">저장</button>
              <button type="button" class="btn" onclick="toggleEdit('${cmt.commentId}', true)">취소</button>
            </div>
          </form>
        </c:if>
      </li>
    </c:forEach>
  </ul>

  <c:if test="${totalPage > 1}">
    <div class="pagination" style="margin-top:8px;">
      <c:forEach begin="1" end="${totalPage}" var="p">
        <c:choose>
          <c:when test="${p == page}">
            <strong>[${p}]</strong>
          </c:when>
          <c:otherwise>
            <a href="${pageContext.request.contextPath}/detailVolRequest.do?volunteerId=${volunteerId}&commentPage=${p}#comment-area">[${p}]</a>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </div>
  </c:if>
</c:if>
</div>

<!-- ✅ 토글 스크립트 -->
<script>
  function toggleEdit(id, cancel) {
    var form    = document.getElementById('edit-' + id);
    var content = document.getElementById('content-' + id);
    var actions = document.getElementById('act-' + id);
    if (!form || !content || !actions) return;

    var editing = (form.style.display === 'block');

    if (cancel || editing) {
      // 보기 모드
      form.style.display = 'none';
      content.style.display = 'block';
      actions.style.visibility = 'visible';   // or actions.style.display = 'block';
    } else {
      // 편집 모드
      form.style.display = 'block';
      content.style.display = 'none';
      actions.style.visibility = 'hidden';    // or actions.style.display = 'none';
      var ta = form.querySelector('textarea');
      if (ta) { ta.focus(); ta.selectionStart = ta.value.length; }
    }
  }
</script>


