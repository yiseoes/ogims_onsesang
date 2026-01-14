// DOM 콘텐츠가 모두 로드된 후에 스크립트 실행
document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('form');
  
  // 페이지에 form이 없으면 아무 작업도 하지 않음
  if (!form) {
    return;
  }

  form.addEventListener('submit', function(event) {
    event.preventDefault();
    clearErrors();
    let isValid = true;

    const title = form.querySelector('[name="title"]');
    const phone = form.querySelector('[name="phone"]');
    const region = form.querySelector('[name="region"]');
    const category = form.querySelector('[name="category"]');
    const content = form.querySelector('[name="content"]');
    const start = form.querySelector('[name="start"]');
    const end = form.querySelector('[name="end"]');

    if (!title.value.trim()) {
      showError(title, '제목을 입력해주세요.');
      isValid = false;
    }
    if (!phone.value.trim()) {
      showError(phone, '연락처를 입력해주세요.');
      isValid = false;
    }
    if (!region.value.trim()) {
      showError(region, '지역을 입력해주세요.');
      isValid = false;
    }
    if (!category.value.trim()) {
      showError(category, '카테고리를 입력해주세요.');
      isValid = false;
    }
    if (!content.value.trim()) {
      showError(content, '봉사요청 상세내용을 입력해주세요.');
      isValid = false;
    }
    
    if (start.value && end.value) {
      const startDate = new Date(start.value);
      const endDate = new Date(end.value);
      if (endDate < startDate) {
        showError(end, '종료일시는 시작일시보다 빠를 수 없습니다.');
        isValid = false;
      }
    }

    if (isValid) {
      form.submit();
    }
  });

  function showError(field, message) {
    field.classList.add('is-invalid');
    const errorContainer = field.parentElement.querySelector('.error-message');
    if (errorContainer) {
      errorContainer.textContent = message;
    }
  }

  function clearErrors() {
    form.querySelectorAll('.is-invalid').forEach(field => field.classList.remove('is-invalid'));
    form.querySelectorAll('.error-message').forEach(msg => msg.textContent = '');
  }

  // 미리보기 기능 (addVolRequest.jsp에만 해당)
  // #preview 요소가 있을 때만 실행되도록 하여 에러 방지
  const previewElement = document.getElementById('preview');
  if (previewElement) {
    document.addEventListener('input', (e) => {
      if (['start', 'end', 'content', 'phone', 'region', 'category'].includes(e.target.name)) {
        syncPreview();
      }
    });
    
    function syncPreview() {
      previewElement.textContent =
        (form.querySelector('[name=start]').value || '') + "\n" +
        (form.querySelector('[name=end]').value || '') + "\n" +
        (form.querySelector('[name=content]').value || '') + "\n" +
        (form.querySelector('[name=phone]').value || '') + "\n" +
        (form.querySelector('[name=region]').value || '') + "\n" +
        (form.querySelector('[name=category]').value || '');
    }
  }
});