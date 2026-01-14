// /js/cards-slider.js
(function(){
  // 외부에서 호출할 초기화 함수
  window.initCardsSlider = function(opts){
    var viewport = document.getElementById('cardsViewport');
    if (!viewport) return;

    var track = viewport.querySelector('.cards');
    if (!track) return;

    // 옵션 우선 → data-* → 기본값
    var VISIBLE = (opts && opts.visible) ? parseInt(opts.visible,10)
                 : parseInt(viewport.getAttribute('data-visible') || '3', 10);
    var INTERVAL_MS = (opts && opts.interval) ? parseInt(opts.interval,10)
                    : parseInt(viewport.getAttribute('data-interval') || '5000', 10);

    // 레이아웃 강제(트랙은 flex)
    viewport.style.maxWidth = '1100px';
    viewport.style.width = '100%';
    viewport.style.marginLeft = 'auto';
    viewport.style.marginRight = 'auto';
    viewport.style.overflow = 'hidden';
    viewport.style.boxSizing = 'border-box';
    track.style.display = 'flex';
    if (!getComputedStyle(track).gap) track.style.gap = '20px';
    track.style.margin = '0';
    track.style.padding = '0';
    track.style.maxWidth = 'none';
    track.style.willChange = 'transform';

    var originals = Array.prototype.slice.call(track.children);
    var ORG_COUNT = originals.length;

    // 아이템이 3개 이하면 슬라이드 불필요
    if (ORG_COUNT <= VISIBLE) {
      originals.forEach(function (c) { c.style.flex = '0 0 auto'; c.style.flexShrink = '0'; });
      return;
    }

    // 중복 초기화 방지: 다시 들어오면 리셋
    if (viewport.__cardsInited) {
      // 기존 클론 제거
      Array.prototype.slice.call(track.querySelectorAll('[data-clone="1"]')).forEach(function (n) { n.remove(); });
      viewport.__cardsTimer && clearInterval(viewport.__cardsTimer);
    }
    viewport.__cardsInited = true;

    // 앞뒤 클론 생성
    function addClones() {
      Array.prototype.slice.call(track.querySelectorAll('[data-clone="1"]')).forEach(function (n) { n.remove(); });
      var tail = originals.slice(-VISIBLE).map(function (node) {
        var c = node.cloneNode(true); c.setAttribute('data-clone','1'); return c;
      });
      tail.reverse().forEach(function (c) { track.insertBefore(c, track.firstChild); });
      var head = originals.slice(0, VISIBLE).map(function (node) {
        var c = node.cloneNode(true); c.setAttribute('data-clone','1'); return c;
      });
      head.forEach(function (c) { track.appendChild(c); });
    }
    addClones();

    var allItems = Array.prototype.slice.call(track.children);
    var index = VISIBLE; // 클론 뒤에 첫 원본부터 시작
    var gapPx = 0, cardW = 0, stepX = 0;

    function getGapPx() {
      var cs = getComputedStyle(track);
      var g = parseFloat(cs.gap || cs.columnGap || '0');
      return isNaN(g) ? 0 : g;
    }

    function layout() {
      gapPx = getGapPx();
      var vw = viewport.clientWidth;
      cardW = (vw - gapPx * (VISIBLE - 1)) / VISIBLE;
      stepX = cardW + gapPx;
      allItems.forEach(function (it) { it.style.flex = '0 0 ' + cardW + 'px'; });
      setTransition(false);
      jumpTo(index);
      requestAnimationFrame(function(){ setTransition(true); });
    }

    function setTransition(on) { track.style.transition = on ? 'transform 0.5s ease-in-out' : 'none'; }
    function jumpTo(i) { track.style.transform = 'translateX(' + (-i * stepX) + 'px)'; }

    function next() { index += 1; jumpTo(index); }
    function prev() { index -= 1; jumpTo(index); }

    function handleEdgeLoop() {
      var firstOriginalIndex = VISIBLE;
      var lastOriginalIndex  = VISIBLE + ORG_COUNT - 1;
      if (index > lastOriginalIndex) {
        setTransition(false);
        index = firstOriginalIndex + (index - lastOriginalIndex - 1);
        jumpTo(index);
        requestAnimationFrame(function(){ setTransition(true); });
      } else if (index < firstOriginalIndex) {
        setTransition(false);
        index = lastOriginalIndex - (firstOriginalIndex - index - 1);
        jumpTo(index);
        requestAnimationFrame(function(){ setTransition(true); });
      }
    }

    track.addEventListener('transitionend', handleEdgeLoop);
    window.addEventListener('resize', layout);

    // 버튼
    var btnPrev = viewport.querySelector('[data-cards-prev]');
    var btnNext = viewport.querySelector('[data-cards-next]');
    if (btnPrev) btnPrev.addEventListener('click', function(){ stopAuto(); prev(); });
    if (btnNext) btnNext.addEventListener('click', function(){ stopAuto(); next(); });

    // 자동재생
    function startAuto() {
      stopAuto();
      viewport.__cardsTimer = setInterval(next, INTERVAL_MS);
    }
    function stopAuto() {
      if (viewport.__cardsTimer) { clearInterval(viewport.__cardsTimer); viewport.__cardsTimer = null; }
    }

    viewport.addEventListener('mouseenter', stopAuto);
    viewport.addEventListener('mouseleave', startAuto);

    // 초기 레이아웃 & 자동재생 시작
    layout();
    jumpTo(index);
    startAuto();
  };

  // 데이터가 나중에 들어올 수도 있으니 이벤트도 수신
  document.addEventListener('cards:ready', function(e){
    var d = (e && e.detail) || {};
    window.initCardsSlider({ visible: d.visible || 3, interval: d.interval || 5000 });
  });

  // DOMContentLoaded 시점에 이미 카드가 있다면 자동 초기화
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function(){
      if (document.getElementById('home-cards')?.children.length) {
        window.initCardsSlider({ visible:3, interval:5000 });
      }
    });
  } else {
    if (document.getElementById('home-cards')?.children.length) {
      window.initCardsSlider({ visible:3, interval:5000 });
    }
  }
})();
