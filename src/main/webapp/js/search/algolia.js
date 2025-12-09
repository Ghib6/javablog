/* algolia.js - 简化的 Algolia 集成，若未配置则为空实现 */
(function () {
    const cfg = (window.GLOBAL_CONFIG && GLOBAL_CONFIG.algolia) || null

    const renderResults = (hits, container) => {
        container.innerHTML = hits.map(hit => `
            <div class="algolia-hit">
                <a href="${hit.url}">${hit.title || hit.url}</a>
                <p class="algolia-snippet">${hit._snippetResult && hit._snippetResult.content ? hit._snippetResult.content.value : (hit.content || '')}</p>
            </div>
        `).join('')
    }

    window.AlgoliaSearch = {
        init: function () {
            if (!cfg || !cfg.appId || !cfg.apiKey || !cfg.indexName) return

            const input = document.getElementById('search-input')
            const resultContainer = document.getElementById('search-result')
            if (!input || !resultContainer) return

            const client = (window.algoliasearch) ? algoliasearch(cfg.appId, cfg.apiKey) : null
            if (!client) {
                console.warn('algoliasearch not found; please include Algolia client if you want remote search')
                return
            }
            const index = client.initIndex(cfg.indexName)

            input.addEventListener('input', btf.debounce(async e => {
                const q = e.target.value.trim()
                if (!q) {
                    resultContainer.innerHTML = ''
                    return
                }
                try {
                    const res = await index.search(q, { hitsPerPage: cfg.hitsPerPage || 10 })
                    renderResults(res.hits, resultContainer)
                } catch (err) {
                    console.error('Algolia search error', err)
                }
            }, 200))
        }
    }
})()
