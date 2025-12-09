/* local-search.js - 简单的本地搜索实现，使用站点提供的 JSON 索引 */
(function () {
    const cfg = (window.GLOBAL_CONFIG && GLOBAL_CONFIG.local_search) || {}

    const fetchIndex = async path => {
        try {
            const res = await fetch(path)
            if (!res.ok) return []
            return await res.json()
        } catch (e) {
            console.error('Fetch local search index failed', e)
            return []
        }
    }

    const render = (results, container) => {
        if (!container) return
        if (!results.length) {
            container.innerHTML = '<div class="localsearch-empty">No results</div>'
            return
        }
        container.innerHTML = results.map(r => `
            <div class="ls-item">
                <a href="${r.url}">${r.title}</a>
                <p class="ls-snippet">${(r.content || '').slice(0, 200)}</p>
            </div>
        `).join('')
    }

    const LocalSearch = function () {
        this.index = []

        this.init = async function (opts = {}) {
            const path = opts.path || cfg.path || '/search.json'
            this.index = await fetchIndex(path)
            const input = document.getElementById(opts.input || 'search-input')
            const container = document.getElementById(opts.result || 'search-result')
            if (!input || !container) return

            input.addEventListener('input', btf.debounce(e => {
                const q = e.target.value.trim().toLowerCase()
                if (!q) return render([], container)
                const words = q.split(/\s+/).filter(Boolean)
                const results = this.index.filter(item => {
                    const hay = (item.title + ' ' + (item.content || '')).toLowerCase()
                    return words.every(w => hay.indexOf(w) !== -1)
                }).slice(0, opts.hitsPerPage || 10)
                render(results, container)
            }, 200))
        }
    }

    window.LocalSearch = LocalSearch
})()

