'use strict';

// ── 현재 검색 키워드 · 페이지 ─────────────────
let currentKeyword = '';
let currentPage    = 0;

// ════════════════════════════════════════════
// 모달 — 게시글 등록 / 수정
// ════════════════════════════════════════════

// 등록 모달 열기
function openWriteModal() {
    document.getElementById('modalTitle').textContent   = '글쓰기';
    document.getElementById('modalPostId').value        = '';
    document.getElementById('modalPostTitle').value     = '';
    document.getElementById('modalPostContent').value   = '';
    document.getElementById('modalSaveBtn').textContent = '등록';
    document.getElementById('postModal').style.display  = 'flex';
}

// 수정 모달 열기
function openEditModal(id, title, content) {
    document.getElementById('modalTitle').textContent   = '게시글 수정';
    document.getElementById('modalPostId').value        = id;
    document.getElementById('modalPostTitle').value     = title;
    document.getElementById('modalPostContent').value   = content;
    document.getElementById('modalSaveBtn').textContent = '수정';
    document.getElementById('postModal').style.display  = 'flex';
}

// 모달 닫기
function closeModal(event) {
    // 오버레이 클릭 시 닫기 (모달 박스 클릭은 무시)
    if (event && event.target !== event.currentTarget) return;
    document.getElementById('postModal').style.display = 'none';
}

// ESC 키로 닫기
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        document.getElementById('postModal').style.display = 'none';
    }
});

// ════════════════════════════════════════════
// 게시글 CRUD
// ════════════════════════════════════════════

// 등록 / 수정 저장
async function savePost() {
    const id      = document.getElementById('modalPostId').value;
    const title   = document.getElementById('modalPostTitle').value.trim();
    const content = document.getElementById('modalPostContent').value.trim();

    if (!title)   { alert('제목을 입력해주세요.'); return; }
    if (!content) { alert('내용을 입력해주세요.'); return; }

    const isEdit  = !!id;
    const url     = isEdit ? `/board/${id}` : '/board/save';
    const method  = isEdit ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify({ title, content })
        });
        if (!res.ok) throw new Error('요청 실패');

        closeModal();
        alert(isEdit ? '수정되었습니다.' : '등록되었습니다.');
        location.reload();   // 목록 갱신
    } catch (e) {
        alert('오류 발생: ' + e.message);
    }
}

// 게시글 삭제
async function deletePost(id) {
    if (!confirm('게시글을 삭제하시겠습니까?\n댓글도 함께 삭제됩니다.'))
        return;

    try {
        const res = await fetch(`/board/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('삭제 실패');
        alert('삭제되었습니다.');
        location.reload();
    } catch (e) {
        alert('오류 발생: ' + e.message);
    }
}

// ════════════════════════════════════════════
// 댓글 토글
// ════════════════════════════════════════════

async function toggleComment(postId, btn) {
    const area = document.getElementById(`comment-area-${postId}`);
    const isOpen = area.style.display !== 'none';

    if (isOpen) {
        area.style.display = 'none';
        btn.classList.remove('open');
        btn.innerHTML = btn.innerHTML.replace('▲', '▼');
    } else {
        area.style.display = 'block';
        btn.classList.add('open');
        btn.innerHTML = btn.innerHTML.replace('▼', '▲');
        await loadComments(postId);  // 열릴 때 최신 댓글 로드
    }
}

// ════════════════════════════════════════════
// 댓글 CRUD
// ════════════════════════════════════════════

// 댓글 목록 조회
async function loadComments(postId) {
    try {
        const res      = await fetch(`/api/comment?postId=${postId}`);
        const comments = await res.json();
        renderComments(postId, comments);
    } catch (e) {
        console.error('댓글 조회 실패', e);
    }
}

// 댓글 렌더링
function renderComments(postId, comments) {
    const list = document.getElementById(`comment-list-${postId}`);

    if (!comments.length) {
        list.innerHTML = '<p style="color:#bbb;font-size:13px;padding:8px 0">첫 번째 댓글을 작성해보세요!</p>';
        return;
    }

    list.innerHTML = comments.map(c => `
        <div class="comment-item" id="ci-${c.id}">
            <div class="comment-item-header">
                <span class="comment-writer">${c.writer}</span>
                <span class="comment-date">${formatDate(c.createdAt)}</span>
            </div>
            <div class="comment-content" id="cc-${c.id}">
                ${escapeHtml(c.content)}
            </div>
            <div class="comment-item-actions" id="ca-${c.id}">
                <button class="c-edit"
                  onclick="editCommentUI(${c.id}, '${escapeJs(c.content)}',
                           ${postId})">수정</button>
                <button class="c-del"
                  onclick="deleteComment(${c.id}, ${postId})">삭제</button>
            </div>
        </div>
    `).join('');
}

// 댓글 등록
async function saveComment(postId) {
    const textarea = document.getElementById(`comment-input-${postId}`);
    const content  = textarea.value.trim();
    if (!content) { alert('댓글을 입력해주세요.'); return; }

    try {
        const res = await fetch('/api/comment', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content, postId })
        });
        if (!res.ok) throw new Error('등록 실패');
        textarea.value = '';
        await loadComments(postId);
        // 댓글 수 뱃지 갱신
        updateBadge(postId, 1);
    } catch (e) {
        alert('오류 발생: ' + e.message);
    }
}

// 댓글 수정 UI 전환
function editCommentUI(id, original, postId) {
    const contentEl = document.getElementById(`cc-${id}`);
    const actionsEl = document.getElementById(`ca-${id}`);

    contentEl.innerHTML = `
        <div class="comment-edit-area">
            <textarea id="ce-${id}" rows="2">${original}</textarea>
        </div>`;
    actionsEl.innerHTML = `
        <button class="c-edit"
                onclick="updateComment(${id}, ${postId})">저장</button>
        <button class="c-del"
                onclick="loadComments(${postId})">취소</button>`;
}

// 댓글 수정 저장
async function updateComment(id, postId) {
    const content = document.getElementById(`ce-${id}`).value.trim();
    if (!content) { alert('내용을 입력해주세요.'); return; }

    try {
        const res = await fetch(`/api/comment/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content })
        });
        if (!res.ok) throw new Error('수정 실패');
        await loadComments(postId);
    } catch (e) {
        alert('오류 발생: ' + e.message);
    }
}

// 댓글 삭제
async function deleteComment(id, postId) {
    if (!confirm('댓글을 삭제하시겠습니까?')) return;

    try {
        const res = await fetch(`/api/comment/${id}`, {
            method: 'DELETE'
        });
        if (!res.ok) throw new Error('삭제 실패');
        await loadComments(postId);
        updateBadge(postId, -1);
    } catch (e) {
        alert('오류 발생: ' + e.message);
    }
}

// 댓글 수 뱃지 갱신
function updateBadge(postId, delta) {
    const card   = document.querySelector(`.post-card[data-id="${postId}"]`);
    if (!card) return;
    const badge  = card.querySelector('.post-badge');
    const toggle = card.querySelector('.comment-toggle-btn');

    // 현재 댓글 수 파악 (없으면 0)
    let count = badge
        ? parseInt(badge.textContent.replace('💬','').trim()) || 0
        : 0;
    count = Math.max(0, count + delta);

    if (count > 0) {
        if (badge) badge.textContent = `💬 ${count}`;
        else {
            const b = document.createElement('span');
            b.className   = 'post-badge';
            b.textContent = `💬 ${count}`;
            card.querySelector('.post-title-wrap').appendChild(b);
        }
    } else {
        badge && badge.remove();
    }

    // 토글 버튼 텍스트도 갱신
    if (toggle) {
        toggle.innerHTML =
            toggle.innerHTML.replace(/\d+개/, `${count}개`);
    }
}

// ════════════════════════════════════════════
// 검색 / 페이징
// ════════════════════════════════════════════

function searchPost() {
    currentKeyword = document.getElementById('searchKeyword').value.trim();
    currentPage    = 0;
    location.href  =
        `/board?page=0&keyword=${encodeURIComponent(currentKeyword)}`;
}

function resetSearch() {
    location.href = '/board';
}

function loadPage(page) {
    const keyword = document.getElementById('searchKeyword').value.trim();
    location.href =
        `/board?page=${page}&keyword=${encodeURIComponent(keyword)}`;
}

// ════════════════════════════════════════════
// 유틸
// ════════════════════════════════════════════

function formatDate(dateStr) {
    if (!dateStr) return '';
    const d   = new Date(dateStr);
    const pad = n => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())} `
        + `${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

function escapeHtml(str) {
    return str.replace(/&/g,'&amp;')
        .replace(/</g,'&lt;')
        .replace(/>/g,'&gt;')
        .replace(/"/g,'&quot;');
}

function escapeJs(str) {
    return str.replace(/\\/g,'\\\\')
        .replace(/'/g,"\\'")
        .replace(/\n/g,'\\n')
        .replace(/\r/g,'');
}