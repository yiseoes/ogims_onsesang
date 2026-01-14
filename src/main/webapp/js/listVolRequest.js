// /js/listVolRequest.js
(function () {
  const form     = document.getElementById('searchForm');
  if (!form) return;

  const pageInp  = form.querySelector('input[name="page"]');
  const radios   = form.querySelectorAll('input[name="category"]');
  const statusCb = form.querySelector('input[name="status"]');

  const condSel  = document.getElementById('searchCondition');
  const kwText   = document.getElementById('keywordText');
  const kwHidden = document.getElementById('searchKeywordHidden');
  const dateGrp  = document.getElementById('dateGroup');
  const dateFrom = document.getElementById('dateFrom');
  const dateTo   = document.getElementById('dateTo');

  function notEmpty(s){ return s != null && s.trim() !== ''; }

  function toggleSearchUI(){
    if (!condSel) return;
    if (condSel.value === 'date') {
      if (dateGrp) dateGrp.style.display = 'inline-flex';
      if (kwText)  kwText.style.display  = 'none';
    } else {
      if (dateGrp) dateGrp.style.display = 'none';
      if (kwText)  kwText.style.display  = 'inline-block';

      if (kwText) {
        if (condSel.value === 'region')      kwText.placeholder = '지역 (예: 서울시 구로구)';
        else if (condSel.value === 'author') kwText.placeholder = '작성자 이름';
        else                                 kwText.placeholder = '검색어';
      }
    }
  }

  function prepareDateKeywordIfNeeded(){
    if (!condSel) return;
    if (condSel.value === 'date') {
      const from = dateFrom ? dateFrom.value : '';
      const to   = dateTo   ? dateTo.value   : '';
      if (kwHidden) kwHidden.value = (notEmpty(from) && notEmpty(to)) ? (from + '~' + to) : (from || to || '');
      if (kwText)   kwText.name = '_'; // 중복 전송 방지
    } else {
      if (kwHidden) kwHidden.value = '';
      if (kwText)   kwText.name = 'searchKeyword';
    }
  }

  // 최초 진입 시 UI 정렬
  toggleSearchUI();

  // 검색조건 변경 시 UI 업데이트
  if (condSel) condSel.addEventListener('change', toggleSearchUI);

  // 카테고리 라디오 변경 → 1페이지로 리셋 + 즉시 제출
  if (radios && radios.length) {
    radios.forEach(r => {
      r.addEventListener('change', () => {
        if (pageInp) pageInp.value = 1;
        prepareDateKeywordIfNeeded();
        form.submit();
      });
    });
  }

  // "모집중만" 체크박스 토글 → 1페이지로 리셋 + 즉시 제출
  if (statusCb) {
    statusCb.addEventListener('change', () => {
      if (pageInp) pageInp.value = 1;
      prepareDateKeywordIfNeeded();
      form.submit();
    });
  }

  // 검색 버튼으로 submit할 때 날짜조건이면 hidden으로 조합
  form.addEventListener('submit', () => {
    prepareDateKeywordIfNeeded();
    // 검색 시 1페이지부터 보고 싶다면 아래 주석을 해제
    // if (pageInp) pageInp.value = 1;
  });
})();
