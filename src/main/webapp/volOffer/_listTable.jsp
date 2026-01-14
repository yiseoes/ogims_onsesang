<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 목록 테이블 -->
<div style="overflow:auto;">
  <table style="width:100%; border-collapse:collapse; background:var(--surface); border-radius:12px; overflow:hidden;">
    <thead style="background:var(--hover);">
    <tr>
      <th style="padding:10px; border-bottom:1px solid var(--line);">No</th>
      <th style="padding:10px; border-bottom:1px solid var(--line);">카테고리</th>
      <th style="padding:10px; border-bottom:1px solid var(--line);">제목</th>
      <th style="padding:10px; border-bottom:1px solid var(--line);">작성자</th>
   <th style="padding:16px; border-bottom:1px solid var(--line); text-align:center; vertical-align:middle;">
  봉사제공일자
</th>

      <th style="padding:10px; border-bottom:1px solid var(--line);">처리상태</th>
      <th style="padding:10px; border-bottom:1px solid var(--line);">상세</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
      <c:when test="${empty list}">
        <tr><td colspan="7" style="padding:18px; text-align:center;">조회된 데이터가 없습니다.</td></tr>
      </c:when>
      <c:otherwise>
        <c:forEach var="row" items="${list}" varStatus="st">
          <tr style="border-bottom:1px solid var(--line);">
            <!-- 역순 번호: totalCount - (이전페이지건수 + index) -->
            <td style="padding:10px; text-align:center;">
              <c:out value="${totalCount - ((page.currentPage-1)*page.pageSize + st.index)}"/>
            </td>

            <td style="padding:10px; text-align:center;">
              <c:out value="${row.category}"/>
            </td>

            <td style="padding:10px;">
              <a href="<c:url value='/detailVolOffer.do'>
                         <c:param name='volunteerId' value='${row.volunteerId}'/>
                         <c:param name='region' value='${param.region}'/>
                         <c:param name='category' value='${param.category}'/>
                         <c:param name='status' value='${param.status}'/>
                         <c:param name='searchCondition' value='${param.searchCondition}'/>
                         <c:param name='searchKeyword' value='${param.searchKeyword}'/>
                         <c:param name='page' value='${page.currentPage}'/>
                         <c:param name='pageSize' value='${page.pageSize}'/>
                       </c:url>">
                <c:out value="${row.title}"/>
              </a>
            </td>

            <td style="padding:10px; text-align:center;"><c:out value="${row.authorName}"/></td>

            <td style="padding:10px; text-align:center;">
              <c:out value="${row.date}"/>
              <span style="color:var(--subtle-ink);">/</span>
              <c:out value="${row.startTime}"/>
            </td>

            <td style="padding:10px; text-align:center;"><c:out value="${row.status}"/></td>

            <td style="padding:10px; text-align:center;">
              <a class="btn" href="<c:url value='/detailVolOffer.do'>
                         <c:param name='volunteerId' value='${row.volunteerId}'/>
                         <c:param name='region' value='${param.region}'/>
                         <c:param name='category' value='${param.category}'/>
                         <c:param name='status' value='${param.status}'/>
                         <c:param name='searchCondition' value='${param.searchCondition}'/>
                         <c:param name='searchKeyword' value='${param.searchKeyword}'/>
                         <c:param name='page' value='${page.currentPage}'/>
                         <c:param name='pageSize' value='${page.pageSize}'/>
                       </c:url>">열기</a>
            </td>
          </tr>
        </c:forEach>
      </c:otherwise>
    </c:choose>
    </tbody>
  </table>
</div>
