(() => {
    const bno = Number(document.body.dataset.bno);
    const listElement = document.querySelector('#reply-list');
    const countElement = document.querySelector('#reply-count');
    const statusElement = document.querySelector('#reply-status');
    const formElement = document.querySelector('#reply-form');
    const reloadButton = document.querySelector('#reload-replies');

    if (!bno || !listElement || !formElement) {
        return;
    }

    const formatDate = (value) => {
        if (!value) return '-';
        return new Intl.DateTimeFormat('ko-KR', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        }).format(new Date(value));
    };

    const request = async (url, options = {}) => {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...(options.headers || {})
            },
            ...options
        });

        if (!response.ok) {
            let message = `요청에 실패했습니다. (${response.status})`;
            try {
                const errorBody = await response.json();
                message = errorBody.message || message;
            } catch (_) {
                // JSON 오류 응답이 아니면 기본 메시지를 사용한다.
            }
            throw new Error(message);
        }

        if (response.status === 204) {
            return null;
        }
        return response.json();
    };

    const showStatus = (message = '', type = '') => {
        statusElement.textContent = message;
        statusElement.className = `reply-status ${type}`.trim();
    };

    const makeButton = (text, className, onClick) => {
        const button = document.createElement('button');
        button.type = 'button';
        button.textContent = text;
        button.className = className;
        button.addEventListener('click', onClick);
        return button;
    };

    const createReplyItem = (item) => {
        const article = document.createElement('article');
        article.className = 'reply-item';

        const head = document.createElement('div');
        head.className = 'reply-head';

        const identity = document.createElement('div');
        const writer = document.createElement('strong');
        writer.textContent = item.replyer;
        const date = document.createElement('span');
        date.textContent = formatDate(item.modDate || item.regDate);
        identity.append(writer, date);

        const actions = document.createElement('div');
        actions.className = 'reply-actions';
        actions.append(
            makeButton('수정', 'text-button', () => modifyReply(item)),
            makeButton('삭제', 'text-button danger-text', () => deleteReply(item.rno))
        );

        head.append(identity, actions);

        const content = document.createElement('p');
        content.className = 'reply-content';
        content.textContent = item.reply;

        article.append(head, content);
        return article;
    };

    const render = (items) => {
        listElement.replaceChildren();
        countElement.textContent = String(items.length);

        if (items.length === 0) {
            const empty = document.createElement('div');
            empty.className = 'reply-empty';
            empty.textContent = '아직 댓글이 없습니다. 첫 댓글을 남겨 보세요.';
            listElement.append(empty);
            return;
        }

        items.forEach(item => listElement.append(createReplyItem(item)));
    };

    const loadReplies = async () => {
        showStatus('댓글을 불러오는 중입니다.');
        try {
            const items = await request(`/replies/board/${bno}`);
            render(items);
            showStatus('');
        } catch (error) {
            showStatus(error.message, 'error');
        }
    };

    const modifyReply = async (item) => {
        const changed = window.prompt('수정할 댓글 내용을 입력하세요.', item.reply);
        if (changed === null) return;
        if (!changed.trim()) {
            showStatus('댓글 내용을 입력하세요.', 'error');
            return;
        }

        try {
            await request(`/replies/${item.rno}`, {
                method: 'PATCH',
                body: JSON.stringify({ reply: changed.trim() })
            });
            showStatus('댓글이 수정되었습니다.', 'success');
            await loadReplies();
        } catch (error) {
            showStatus(error.message, 'error');
        }
    };

    const deleteReply = async (rno) => {
        if (!window.confirm('댓글을 삭제할까요?')) return;

        try {
            await request(`/replies/${rno}`, { method: 'DELETE' });
            showStatus('댓글이 삭제되었습니다.', 'success');
            await loadReplies();
        } catch (error) {
            showStatus(error.message, 'error');
        }
    };

    formElement.addEventListener('submit', async (event) => {
        event.preventDefault();

        const replyerInput = document.querySelector('#replyer');
        const replyInput = document.querySelector('#reply');
        const replyer = replyerInput.value.trim();
        const reply = replyInput.value.trim();

        if (!replyer || !reply) {
            showStatus('작성자와 댓글 내용을 모두 입력하세요.', 'error');
            return;
        }

        try {
            await request('/replies', {
                method: 'POST',
                body: JSON.stringify({ bno, replyer, reply })
            });
            replyInput.value = '';
            showStatus('댓글이 등록되었습니다.', 'success');
            await loadReplies();
        } catch (error) {
            showStatus(error.message, 'error');
        }
    });

    reloadButton?.addEventListener('click', loadReplies);
    loadReplies();
})();
