// /js/home-cards-scrape.js
// - 목록(/listNotice.do)에서 제목/링크/날짜를 먼저 그린 뒤
// - 각 아이템의 상세 페이지(/detailNotice.do?... )를 병렬로 불러와
//   본문 요약을 카드에 점진적으로 채워 넣는다.
// - 캐러셀 초기화(3개 보기)는 "초기 렌더 직후" 수행 → 레이아웃 깨짐 방지

(function () {
  'use strict';

  // ===== 설정 =====
  var BODY_MAX = 120;     // 카드 본문 최대 글자수
  var CONCURRENCY = 4;    // 상세 페이지 동시 요청 개수(너무 크면 서버 부하↑)
  var DETAIL_TIMEOUT = 8000; // 상세 요청 타임아웃(ms)

  // 컨텍스트 루트
  var ctxMeta = document.getElementById('app-ctx');
  var CTX = ctxMeta ? (ctxMeta.getAttribute('data-ctx') || '') : '';

  // 주입 대상 컨테이너(카드 트랙)
  var container = document.getElementById('home-cards');
  if (!container) return;

  // 목록 URL (필요시 pageSize 조정)
  var LIST_URL = CTX + '/listNotice.do?page=1&pageSize=12';

  // 1) 목록 HTML 가져오기 → 초기 카드 렌더
  fetch(LIST_URL, { headers: { 'Accept': 'text/html' } })
    .then(ok).then(function (res) { return res.text(); })
    .then(function (html) {
      var doc = new DOMParser().parseFromString(html, 'text/html');
      var items = extractItems(doc);

      if (!items.length) {
        container.innerHTML =
          '<article class="card"><div class="card-title">공지 없음</div>' +
          '<div class="card-body">등록된 공지가 없습니다.</div></article>';
      } else {
        // 초기 렌더: 본문은 일단 "불러오는 중…" 표시
        container.innerHTML = items.map(function (it, idx) {
          return toCardHTML({
            url: it.url,
            title: it.title,
            date: it.date,
            body: it.body && it.body.trim() ? it.body : '본문을 불러오는 중…'
          }, idx);
        }).join('');
      }

      // 캐러셀 초기화(바로)
      setTimeout(function () {
        if (window.initCardsSlider) {
          window.initCardsSlider({ visible: 3, interval: 5000 });
        } else {
          document.dispatchEvent(new CustomEvent('cards:ready', {
            detail: { visible: 3, interval: 5000 }
          }));
        }
      }, 0);

      // 2) 상세 본문 병렬 수집 → 카드 본문 교체
      enrichBodies(items).catch(function (e) {
        console.warn('[home-cards] detail enrich skipped:', e);
      });
    })
    .catch(function (err) {
      console.error('[home-cards] 목록 불러오기 실패:', err);
      container.innerHTML =
        '<article class="card"><div class="card-title">오류</div>' +
        '<div class="card-body">공지 목록을 불러오지 못했습니다.</div></article>';
    });

  // ===== 상세 본문 채우기 =====
  function enrichBodies(items) {
    // 동시성 제한 큐
    var i = 0, active = 0, done = 0;
    return new Promise(function (resolve) {
      function next() {
        while (active < CONCURRENCY && i < items.length) {
          run(i++);
        }
        if (done >= items.length) resolve();
      }
      function run(idx) {
        active++;
        fetchDetail(items[idx]).then(function (detail) {
          if (detail && detail.body) {
            // DOM 업데이트: index 기반으로 카드 찾기
            var card = container.querySelector('[data-card-idx="' + idx + '"]');
            if (card) {
              var bodyEl = card.querySelector('.card-body');
              if (bodyEl) bodyEl.textContent = detail.body;
              // 날짜가 더 정확히 추출되면 갱신
              if (detail.date) {
                var metaEl = card.querySelector('.card-meta');
                if (metaEl) metaEl.textContent = detail.date;
              }
            }
          }
        }).catch(function(){ /* fail-soft */ })
          .finally(function () { active--; done++; next(); });
      }
      next();
    });
  }

  function fetchDetail(item) {
    // 상세 URL: 목록에서 받은 링크를 그대로 사용(같은 도메인 가정)
    var url = item.url;
    var ctrl = ('AbortController' in window) ? new AbortController() : null;
    var to = ctrl ? setTimeout(function(){ ctrl.abort(); }, DETAIL_TIMEOUT) : null;

    return fetch(url, {
      headers: { 'Accept': 'text/html' },
      signal: ctrl ? ctrl.signal : undefined
    })
      .then(ok)
      .then(function (res) { return res.text(); })
      .then(function (html) {
        if (to) clearTimeout(to);
        var doc = new DOMParser().parseFromString(html, 'text/html');
        var body = extractDetailBody(doc);
        var date = extractDetailDate(doc) || item.date;
        return { body: body || item.body, date: date };
      })
      .catch(function (e) {
        if (to) clearTimeout(to);
        console.warn('[home-cards] detail fail:', e);
        return { body: item.body, date: item.date };
      });
  }

  // ===== 파서들 =====

  // 목록 추출(기존 로직 최대한 유지)
  function extractItems(doc) {
    var out = [];

    // (A) data-* 마크업
    var rich = doc.querySelectorAll('[data-role="notice-item"]');
    if (rich.length) {
      rich.forEach(function (el) {
        var a = el.querySelector('[data-field="title"]') || el.querySelector('a[href*="detailNotice.do"]');
        var date = text(el.querySelector('[data-field="date"]'));
        var body = text(el.querySelector('[data-field="content"]'));
        pushIfAnchor(out, a, date, body);
      });
      return out.slice(0, 12);
    }

    // (B) UL/LI
    var lis = doc.querySelectorAll('ul.notice-list li, .notice-list .notice-item');
    if (lis.length) {
      lis.forEach(function (li) {
        var a = li.querySelector('a.notice-title, a[href*="detailNotice.do"]');
        var date = text(li.querySelector('.notice-date'));
        var body = text(li.querySelector('.notice-content'));
        pushIfAnchor(out, a, date, body);
      });
      return out.slice(0, 12);
    }

    // (C) 테이블
    var rows = doc.querySelectorAll('table.notice-list tbody tr, tbody tr.notice-item');
    if (rows.length) {
      rows.forEach(function (tr) {
        var a = tr.querySelector('a[href*="detailNotice.do"]') || tr.querySelector('td.title a, .title a');
        var date = text(tr.querySelector('.date')) || cellText(tr, 1);
        var body = text(tr.querySelector('.content')) || cellText(tr, 2);
        pushIfAnchor(out, a, date, body);
      });
      return out.slice(0, 12);
    }

    // (D) 최후 수단
    var anchors = doc.querySelectorAll('a[href*="detailNotice.do"]');
    anchors.forEach(function (a) {
      var near = a.closest('li, tr, .notice-item, .row, .card, .item') || a.parentElement || a;
      var nearText = text(near);
      var date = (nearText.match(/(20\d{2}|19\d{2})[-./](0?[1-9]|1[0-2])[-./](0?[1-9]|[12]\d|3[01])/) || [''])[0];
      var body = nearText.replace(/\s+/g, ' ').trim();
      pushIfAnchor(out, a, date, body);
    });

    return out.slice(0, 12);
  }

  // 상세 본문 추출: 프로젝트 마크업에 맞게 셀렉터를 넓게 시도
  function extractDetailBody(doc) {
    var CANDIDATES = [
      '[data-role="notice-body"]',
      '.notice-body', '.board-view .content', '.board-view .article',
      '.post-content', 'article .content', '.view .content',
      '.content-body', '.article-body', '.detail-body',
      '#content .content', '#content .article', '.detail .content'
    ];
    for (var i = 0; i < CANDIDATES.length; i++) {
      var el = doc.querySelector(CANDIDATES[i]);
      if (el && text(el)) {
        return clamp(text(el).replace(/\s+/g, ' ').trim(), BODY_MAX);
      }
    }
    // 그래도 못 찾으면 페이지의 main/article에서 텍스트만 추출
    var main = doc.querySelector('main, article') || doc.body;
    return clamp(text(main).replace(/\s+/g, ' ').trim(), BODY_MAX);
  }

  function extractDetailDate(doc) {
    var dateEl = doc.querySelector('[data-field="date"], .post-date, .meta .date, time[datetime]');
    if (dateEl) {
      var t = dateEl.getAttribute && dateEl.getAttribute('datetime');
      return (t ? t.slice(0, 10) : text(dateEl).slice(0, 10));
    }
    return '';
  }

  function pushIfAnchor(out, a, dateText, bodyText) {
    if (!a) return;
    var href = a.getAttribute('href') || '#';
    // 이미 컨텍스트 경로가 포함되어 있으면 그대로 사용, 아니면 추가
    var url;
    if (href.startsWith('http')) {
      url = href;
    } else if (href.startsWith(CTX + '/') || href.startsWith(CTX + '?')) {
      // 이미 컨텍스트 포함됨
      url = href;
    } else if (href.startsWith('/')) {
      // 절대 경로지만 컨텍스트 없음 → 추가
      url = CTX + href;
    } else {
      // 상대 경로
      url = CTX + '/' + href;
    }
    var title = (a.textContent || '').trim();
    var date = (dateText || '').trim().slice(0, 10);
    var body = (bodyText || '').trim();
    if (!body) body = '';                       // 상세에서 채울 예정
    out.push({ url: url, title: title, date: date, body: body });
  }

  // ===== 렌더/유틸 =====
  function toCardHTML(item, idx) {
    return [
      '<article class="card" data-card-idx="', idx, '">',
        '<div class="card-title"><a href="', attr(item.url), '">', esc(item.title), '</a></div>',
        '<div class="card-meta">', esc(item.date || ''), '</div>',
        '<div class="card-body">', esc(item.body || ''), '</div>',
      '</article>'
    ].join('');
  }

  function clamp(s, n){ if (!s) return s; return s.length > n ? (s.slice(0, n) + '...') : s; }
  function esc(s) {
    return String(s)
      .replace(/&/g, '&amp;').replace(/</g, '&lt;')
      .replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
  }
  function attr(s) { return esc(s); }
  function text(el) { return el ? (el.textContent || '').trim() : ''; }
  function cellText(tr, idx) {
    var td = tr && tr.cells && tr.cells.length > idx ? tr.cells[idx] : null;
    return text(td);
  }
  function ok(res){ if(!res.ok) throw new Error('HTTP '+res.status); return res; }
})();
