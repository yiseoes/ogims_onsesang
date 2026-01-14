document.addEventListener('DOMContentLoaded', function () {
    const slider = document.getElementById('image-slider');
    if (!slider) return;

    const slides = slider.querySelectorAll('.slider-item');
    const nextButton = document.getElementById('next-btn');
    const prevButton = document.getElementById('prev-btn');
    const dotsNav = document.getElementById('pagination-dots');
    
    if (slides.length === 0) return;

    let currentSlide = 0;
    const slideCount = slides.length;
    let autoplayInterval;

    // 페이지네이션 점 생성
    slides.forEach((_, index) => {
        const dot = document.createElement('button');
        dot.classList.add('dot'); // 'dot' 클래스 추가
        dot.addEventListener('click', () => {
            goToSlide(index);
            resetAutoplay();
        });
        dotsNav.appendChild(dot);
    });
    
    const dots = dotsNav.querySelectorAll('.dot');

    // UI 업데이트 함수
    const updateUI = (targetIndex) => {
        // 모든 슬라이드에서 'active' 클래스 제거
        slides.forEach(slide => slide.classList.remove('active'));
        // 타겟 슬라이드에만 'active' 클래스 추가
        slides[targetIndex].classList.add('active');
        
        // 페이지네이션 점 업데이트
        dots.forEach((dot, index) => {
            dot.classList.toggle('active', index === targetIndex);
        });
    };
    
    // 슬라이드 이동 함수
    const goToSlide = (targetIndex) => {
        if (targetIndex === currentSlide) return;
        
        currentSlide = (targetIndex + slideCount) % slideCount;
        updateUI(currentSlide);
    };

    // 버튼 이벤트 리스너
    nextButton.addEventListener('click', () => {
        goToSlide(currentSlide + 1);
        resetAutoplay();
    });

    prevButton.addEventListener('click', () => {
        goToSlide(currentSlide - 1);
        resetAutoplay();
    });
    
    // 자동재생
    const startAutoplay = () => {
        clearInterval(autoplayInterval); // 기존 인터벌 클리어
        autoplayInterval = setInterval(() => {
            goToSlide(currentSlide + 1);
        }, 5000); // 5초마다 슬라이드 변경
    };

    const resetAutoplay = () => {
        startAutoplay();
    };

    // 초기화
    updateUI(0);
    startAutoplay();
});