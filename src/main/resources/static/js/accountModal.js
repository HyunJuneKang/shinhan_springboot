    (function () {
    var overlay = document.getElementById('detailModalOverlay');
    var body = document.getElementById('modalBody');
    var closeBtn = document.getElementById('modalCloseBtn');
    var currentId = null;

    function openModal() {
    overlay.hidden = false;
    document.body.style.overflow = 'hidden';
}

    function closeModal() {
    overlay.hidden = true;
    body.innerHTML = '';
    document.body.style.overflow = '';
    currentId = null;
}

    function esc(v) {
    return String(v == null ? '' : v)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

    // JSON 데이터를 detail.html과 동일한 구조의 HTML 문자열로 변환
    function renderDetail(data) {
    var attachmentsHtml = (data.attachments && data.attachments.length)
    ? data.attachments.map(function (att) {
    var sizeKb = Math.floor(att.fileSize / 1024).toLocaleString();
    return ''
    + '<tr>'
    +   '<td>' + esc(att.originalFilename) + '</td>'
    +   '<td>' + esc(att.fileType) + '</td>'
    +   '<td>' + sizeKb + ' KB</td>'
    +   '<td>'
    +     '<a href="/account/download/' + att.id + '">다운로드</a> '
    +     '<form action="/account/attachment/delete/' + att.id + '" method="post" style="display:inline" onsubmit="return confirm(\'파일을 삭제하시겠습니까?\')">'
    +       '<input type="hidden" name="accountId" value="' + data.id + '">'
    +       '<button type="submit" style="color:red;border:none;background:none;cursor:pointer;">삭제</button>'
    +     '</form>'
    +   '</td>'
    + '</tr>';
}).join('')
    : '<tr><td colspan="4">첨부된 서류가 없습니다.</td></tr>';

    return ''
    + '<div id="accountDetailRoot">'
    +   '<div class="form-wrap">'
    +     '<form action="/account/update.do" method="post">'
    +       '<input type="text" name="id" value="' + data.id + '" placeholder="id" readonly>'
    +       '<input type="text" name="accountNo" value="' + esc(data.accountNo) + '" placeholder="계좌번호">'
    +       '<input type="text" name="ownerName" value="' + esc(data.ownerName) + '" placeholder="예금주">'
    +       '<input type="number" name="balance" value="' + data.balance + '" placeholder="초기 잔액">'
    +       '<select name="accountType">'
    +         '<option value="SAVINGS"' + (data.accountType === 'SAVINGS' ? ' selected' : '') + '>저축예금</option>'
    +         '<option value="CHECKING"' + (data.accountType === 'CHECKING' ? ' selected' : '') + '>당좌예금</option>'
    +       '</select>'
    +       '<button type="submit">수정</button>'
    +     '</form>'
    +   '</div>'
    +   '<a href="/account/list.do" class="js-back-to-list">← 목록으로</a>'
    + '</div>';
}

    // JSON을 받아 모달에 렌더링
    function loadDetail(id) {
    currentId = id;
    body.innerHTML = '<p class="modal-loading">불러오는 중...</p>';
    openModal();

    fetch('/api/account/' + id)
    .then(function (res) {
    if (!res.ok) throw new Error('요청 실패');
    return res.json();
})
    .then(function (data) {
    body.innerHTML = renderDetail(data);
    bindModalContent();
})
    .catch(function () {
    body.innerHTML = '<p>상세 정보를 불러오는 중 오류가 발생했습니다.</p>';
});
}

    function bindModalContent() {
    body.querySelectorAll('form').forEach(function (form) {
    var onsubmitAttr = form.getAttribute('onsubmit') || '';
    var needsConfirm = onsubmitAttr.indexOf('confirm') !== -1;
    form.removeAttribute('onsubmit');

    form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (needsConfirm && !confirm('파일을 삭제하시겠습니까?')) {
    return;
}

    var fd = new FormData(form);
    fetch(form.getAttribute('action'), {
    method: (form.getAttribute('method') || 'POST').toUpperCase(),
    body: fd
})
    .then(function (res) {
    if (!res.ok) throw new Error('요청 실패');
    if (form.getAttribute('action').indexOf('/update.do') !== -1) {
    location.reload();
} else if (currentId) {
    loadDetail(currentId);
}
})
    .catch(function () {
    alert('처리 중 오류가 발생했습니다.');
});
});
});

    var backLink = body.querySelector('.js-back-to-list');
    if (backLink) {
    backLink.addEventListener('click', function (e) {
    e.preventDefault();
    closeModal();
});
}
}

    document.addEventListener('click', function (e) {
    var trigger = e.target.closest('.js-detail-modal');
    if (trigger) {
    loadDetail(trigger.getAttribute('data-id'));
}
});

    closeBtn.addEventListener('click', closeModal);
    overlay.addEventListener('click', function (e) {
    if (e.target === overlay) closeModal();
});
    document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape' && !overlay.hidden) closeModal();
});
})();

/*
즉시실행함수 사용이유
1. 전역 스코프 오염 방지, 캡슐화
2. 정의와 동시에 실행되므로, 페이지 로드 시
getElementById로 요소를 잡고 addEventListener를 등록하는 초기화 코드가
스크립트 로드 즉시 한 번 실행되게 하는 역할
 */
