
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>





<div class="slogan-box">
  <h1>
    이웃의 손길이 닿는 순간<br>
    세상은 더 따뜻해집니다
  </h1>
</div>





<!-- ===== 하단 중앙 이미지 슬라이더 ===== -->
<section class="home-slider">
  <div class="home-slider-card">
    <div class="home-slider-viewport" aria-live="polite">
      <div class="home-slider-track">
        <!-- slide 1 -->
        <div class="home-slider-item">
          <img src="<c:url value='/images/Image.png'/>" alt="내가 등록한 이미지">
          
        </div>
        <!-- slide 2 -->
        <div class="home-slider-item">
            <img src="<c:url value='/images/slider.png'/>" alt="내가 등록한 이미지">
        </div>
        <!-- slide 3 -->
        <div class="home-slider-item">
         <img src="<c:url value='/images/서방도.png'/>" alt="내가 등록한 이미지">
        </div>
        <!-- slide 4 -->
        <div class="home-slider-item">
          <img src="<c:url value='/images/Gemini_Generated_Image_ulyzd2ulyzd2ulyz.png'/>" alt="내가 등록한 이미지">
        </div>
        <!-- slide 5 -->
        <div class="home-slider-item">
         <img src="<c:url value='/images/Gemini_Generated_Image_8ekxe18ekxe18ekx.png'/>" alt="내가 등록한 이미지">
        </div>
      </div>
    </div>

    <!-- 컨트롤 -->
    <button class="home-slider-btn prev" aria-label="이전 슬라이드">&larr;</button>
    <button class="home-slider-btn next" aria-label="다음 슬라이드">&rarr;</button>

    <!-- 인디케이터(점) -->
    <div class="home-slider-dots" role="tablist" aria-label="슬라이드 선택">
      <button class="dot is-active" role="tab" aria-selected="true" aria-controls="slide1"></button>
      <button class="dot" role="tab" aria-selected="false" aria-controls="slide2"></button>
      <button class="dot" role="tab" aria-selected="false" aria-controls="slide3"></button>
      <button class="dot" role="tab" aria-selected="false" aria-controls="slide4"></button>
      <button class="dot" role="tab" aria-selected="false" aria-controls="slide5"></button>
    </div>
  </div>
</section>

<style>
/* 홈 하단 슬라이더 최소 스타일 (겹치지 않게 prefix만 사용) */
.home-slider{max-width:1100px;margin:24px auto 72px;padding:0 16px;}
.home-slider-card{position:relative;border-radius:16px;background:rgba(255,255,255,.85);
  border:1px solid rgba(235,221,211,.70);box-shadow:0 12px 30px rgba(154,120,98,.16);
  backdrop-filter:blur(10px) saturate(120%);-webkit-backdrop-filter:blur(10px) saturate(120%);overflow:hidden;}
.home-slider-viewport{width:100%;height:clamp(180px,28vw,360px);overflow:hidden;}
.home-slider-track{display:flex;width:100%;height:100%;transform:translateX(0);
  transition:transform .5s cubic-bezier(.22,.7,.25,1);}
.home-slider-item{flex:0 0 100%;width:100%;height:100%;}
.home-slider-item img{width:100%;height:100%;object-fit:cover;display:block;}
.home-slider-btn{position:absolute;top:50%;transform:translateY(-50%);width:44px;height:44px;border-radius:999px;
  border:1px solid color-mix(in srgb, var(--accent) 40%, #fff);background:color-mix(in srgb, var(--accent) 16%, #fff);
  display:grid;place-items:center;cursor:pointer;box-shadow:0 8px 22px rgba(0,0,0,.12);}
.home-slider-btn.prev{left:12px;} .home-slider-btn.next{right:12px;}
.home-slider-dots{position:absolute;left:50%;bottom:10px;transform:translateX(-50%);
  display:flex;gap:8px;padding:6px 8px;background:rgba(255,255,255,.7);
  border:1px solid rgba(235,221,211,.7);border-radius:999px;box-shadow:0 8px 20px rgba(0,0,0,.08);}
.home-slider-dots .dot{width:10px;height:10px;border-radius:999px;border:0;cursor:pointer;
  background:color-mix(in srgb,var(--accent) 45%, #fff);opacity:.5;}
.home-slider-dots .dot.is-active{opacity:1;}
@media (max-width:640px){.home-slider-btn{width:38px;height:38px;}}
</style>
<script>
(function(){
  const root = document.querySelector('.home-slider');
  if(!root) return;

  const track = root.querySelector('.home-slider-track');
  const items = Array.from(root.querySelectorAll('.home-slider-item'));
  const dots  = Array.from(root.querySelectorAll('.home-slider-dots .dot'));
  const prev  = root.querySelector('.home-slider-btn.prev');
  const next  = root.querySelector('.home-slider-btn.next');
  const view  = root.querySelector('.home-slider-viewport');

  let idx = 0, timer = null, DUR = 4000;

  function go(i){
    idx = (i + items.length) % items.length;
    track.style.transform = 'translateX(' + (-idx * 100) + '%)';
    dots.forEach((d,di)=>{
      const on = di===idx;
      d.classList.toggle('is-active', on);
      d.setAttribute('aria-selected', on ? 'true' : 'false');
    });
  }
  function start(){ stop(); timer = setInterval(()=> go(idx+1), DUR); }
  function stop(){ if(timer){ clearInterval(timer); timer=null; } }

  // 컨트롤
  prev?.addEventListener('click', ()=>{ go(idx-1); start(); });
  next?.addEventListener('click', ()=>{ go(idx+1); start(); });
  dots.forEach((d,di)=> d.addEventListener('click', ()=>{ go(di); start(); }));

  // 자동재생 + 호버 일시정지
  root.addEventListener('mouseenter', stop);
  root.addEventListener('mouseleave', start);

  // 키보드 접근성
  view?.setAttribute('tabindex','0');
  view?.addEventListener('keydown', (e)=>{
    if(e.key==='ArrowLeft'){ go(idx-1); start(); }
    if(e.key==='ArrowRight'){ go(idx+1); start(); }
  });

  // DOM 준비 후 시작 (include 위치 상관없이 안전)
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', ()=>{ go(0); start(); }, { once:true });
  } else {
    go(0); start();
  }
})();
</script>










<!-- 홈 캐러셀 뷰포트: data-visible/data-interval 로 카드 개수·자동 회전 간격 제어 -->
<!-- ▼▼ 기존 <section class="cards"> ... </section> 전체를 아래로 교체 ▼▼ -->

<!-- 컨텍스트 루트 전달용(스크립트에서 fetch 경로 작성) -->
<meta id="app-ctx" data-ctx="<%=request.getContextPath()%>"/>

<div id="cardsViewport" class="home-slider" data-visible="3" data-interval="5000">
  <section class="cards" id="home-cards"><!-- JS가 공지 카드를 주입합니다 --></section>

  <div class="carousel-buttons">
    <button class="btn" data-cards-prev aria-label="이전">〈 이전</button>
    <button class="btn" data-cards-next aria-label="다음">다음 〉</button>
  </div>
</div>

<!-- (선호) 레이아웃에서 공통으로 로드하면 아래 두 줄은 생략 가능 -->
<script src="<c:url value='/js/home-cards-scrape.js'/>"></script>
<script src="<c:url value='/js/cards-slider.js'/>"></script>





<section class="home-photo-list">
  <!-- 1장 -->
  <figure class="home-photo">
    <img src="<c:url value='/images/세대간의 연결, 따뜻한 미소.png'/>" alt="main1 이미지" class="home-photo-img"/>
  </figure>

  <!-- 2장 -->
  <figure class="home-photo">
    <img src="<c:url value='/images/화면 캡처 2025-08-29 090451.png'/>" alt="main2 이미지" class="home-photo-img"/>
  </figure>

  <!-- 3장 -->
  <figure class="home-photo">
    <img src="<c:url value='/images/20250829_1811_봉사하는 젊은이들_simple_compose_01k3thd6t1fjks3qfr84y1k290.png'/>" alt="main3 이미지" class="home-photo-img"/>
  </figure>
</section>






