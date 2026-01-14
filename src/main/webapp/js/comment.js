// /js/comment.js

// 페이지가 처음 로드되었을 때 모든 로직을 실행합니다.
document.addEventListener('DOMContentLoaded', () => {
    
    // JSP에서 data-* 속성으로 전달한 값을 JS 변수로 가져옵니다.
    const mainElement = document.querySelector('main');
    if (!mainElement) return; // main 태그가 없으면 실행 중단

    const volunteerId = mainElement.dataset.volunteerId;
    const contextPath = mainElement.dataset.contextPath;

    // volunteerId가 없으면 댓글 관련 로직을 실행하지 않습니다.
    if (!volunteerId) return;

    // =====================================================================
    // ======================== 함수 정의 (Functions) ========================
    // =====================================================================

    /**
     * 댓글 목록을 비동기(AJAX)로 새로고침하는 함수
     * @param {number} page - 불러올 페이지 번호 (기본값: 1)
     */
    function loadCommentList(page = 1) {
        const listContainer = document.getElementById('comment-list-container');
        if (!listContainer) return;
        
        // contextPath를 사용하여 올바른 URL을 생성합니다.
        const url = `${contextPath}/listComment.do?volunteerId=${volunteerId}&page=${page}`;

        fetch(url)
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.text(); // ListCommentAction이 반환하는 HTML 텍스트를 받음
            })
            .then(html => {
                listContainer.innerHTML = html; // 컨테이너의 내용을 새 HTML로 교체
                attachCommentEventListeners(); // 새로 생성된 버튼들에도 이벤트를 다시 걸어줌
            })
            .catch(error => console.error('댓글 목록 로딩 중 오류 발생:', error));
    }

    /**
     * 등록, 수정, 삭제 폼을 비동기(AJAX)로 제출하는 공통 함수
     * @param {Event} event - 폼 제출 이벤트 객체
     */
    async function handleFormSubmit(event) {
        event.preventDefault(); // 폼의 기본 제출(페이지 새로고침) 동작을 막음
        const form = event.target;
        const formData = new FormData(form);
        
        try {
            const response = await fetch(form.action, {
                method: 'POST',
                body: new URLSearchParams(formData) // 폼 데이터를 서버가 읽을 수 있는 형식으로 변환
            });

            if (!response.ok) {
                if (response.status === 401) { // 권한 없음 (로그인 필요)
                    alert("로그인이 필요합니다.");
                    // 로그인 페이지로 이동시키는 로직을 추가할 수 있습니다.
                    // window.location.href = `${contextPath}/loginView.do`;
                } else {
                    throw new Error(`서버 응답 오류: ${response.status}`);
                }
                return;
            }
            
            const result = await response.json(); // Action이 반환하는 JSON 데이터를 받음

            if (result.success) {
                loadCommentList(); // 성공 시, 댓글 목록을 1페이지부터 다시 로드
                if (form.id === 'comment-add-form') {
                    form.querySelector('textarea').value = ''; // 등록 폼의 내용은 비워줌
                }
            } else {
                alert(result.message || '요청 처리에 실패했습니다.');
            }
        } catch (error) {
            console.error('폼 제출 중 오류 발생:', error);
            alert('오류가 발생했습니다. 다시 시도해주세요.');
        }
    }

    /**
     * 페이지 내의 모든 댓글 관련(수정, 삭제, 페이지네이션) 요소에 이벤트 리스너를 설정하는 함수
     */
    function attachCommentEventListeners() {
        // 모든 '삭제' 폼에 비동기 제출 이벤트 연결
        document.querySelectorAll('form[action*="deleteComment.do"]').forEach(form => {
            // 이미 이벤트 리스너가 연결되어 있으면 중복 등록 방지
            if (form.dataset.listenerAttached) return;
            form.addEventListener('submit', function(event) {
                if (confirm('정말로 삭제하시겠습니까?')) {
                    handleFormSubmit(event);
                } else {
                    event.preventDefault(); // 확인을 누르지 않으면 폼 제출 취소
                }
            });
            form.dataset.listenerAttached = 'true';
        });

        // 모든 '수정' 폼에 비동기 제출 이벤트 연결
        document.querySelectorAll('form[action*="updateComment.do"]').forEach(form => {
            if (form.dataset.listenerAttached) return;
            form.addEventListener('submit', handleFormSubmit);
            form.dataset.listenerAttached = 'true';
        });
        
        // 페이지네이션 링크(<a>)가 페이지를 새로고침하지 않고 비동기로 댓글 목록을 불러오도록 설정
        document.querySelectorAll('.pagination a').forEach(a => {
            if (a.dataset.listenerAttached) return;
            a.addEventListener('click', (e) => {
                e.preventDefault(); // a 태그의 기본 동작(페이지 이동)을 막음
                try {
                    const url = new URL(a.href);
                    const page = url.searchParams.get('commentPage'); // URL에서 commentPage 파라미터 추출
                    loadCommentList(page);
                } catch (error) {
                    console.error('페이지네이션 링크 처리 중 오류:', error);
                }
            });
            a.dataset.listenerAttached = 'true';
        });
    }

    // =====================================================================
    // ===================== 초기화 코드 (Initialization) ====================
    // =====================================================================
    
    // 댓글 '등록' 폼에 비동기 제출 이벤트 연결
    const addForm = document.getElementById('comment-add-form');
    if (addForm) {
        addForm.addEventListener('submit', handleFormSubmit);
    }

    // 현재 화면에 있는 댓글들의 버튼에 이벤트 연결
    attachCommentEventListeners();
});