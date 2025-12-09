(() => {
    // utils.js - Hexo Butterfly helpers (精简并整合以适配 JSP 项目)
    const btfFn = {
        debounce: (func, wait = 0, immediate = false) => {
            let timeout
            return (...args) => {
                const later = () => {
                    timeout = null
                    if (!immediate) func(...args)
                }
                const callNow = immediate && !timeout
                clearTimeout(timeout)
                timeout = setTimeout(later, wait)
                if (callNow) func(...args)
            }
        },

        throttle: (func, wait = 300) => {
            let timeout = null
            let previous = 0
            return (...args) => {
                const now = Date.now()
                const remaining = wait - (now - previous)
                if (remaining <= 0) {
                    if (timeout) {
                        clearTimeout(timeout)
                        timeout = null
                    }
                    previous = now
                    func.apply(this, args)
                } else if (!timeout) {
                    timeout = setTimeout(() => {
                        previous = Date.now()
                        timeout = null
                        func.apply(this, args)
                    }, remaining)
                }
            }
        },

        // 简易 DOM 辅助
        wrap: (element, tag, attrs = {}) => {
            if (!element) return
            const wrapper = document.createElement(tag)
            Object.keys(attrs).forEach(k => wrapper.setAttribute(k, attrs[k]))
            element.parentNode.insertBefore(wrapper, element)
            wrapper.appendChild(element)
            return wrapper
        },

        addEventListenerPjax: (el, type, fn, opts) => {
            if (!el) return
            el.addEventListener(type, fn, opts)
        },

        getScript: src => new Promise((resolve, reject) => {
            const s = document.createElement('script')
            s.src = src
            s.async = true
            s.onload = () => resolve()
            s.onerror = () => reject(new Error(`Failed to load ${src}`))
            document.head.appendChild(s)
        }),

        // lightbox loader (兼容 medium-zoom / fancybox)
        loadLightbox: imgs => {
            if (!imgs || !imgs.length) return
            // 如果已引入 mediumZoom，绑定即可；否则跳过
            if (typeof mediumZoom === 'function') {
                try {
                    mediumZoom(Array.from(imgs))
                } catch (e) {
                    console.warn('mediumZoom init failed', e)
                }
            }
        },

        // 保存到 localStorage（带过期）
        saveToLocal: {
            set: (key, value, days) => {
                try {
                    const obj = { value }
                    if (days) obj.expiry = Date.now() + days * 24 * 3600 * 1000
                    localStorage.setItem(key, JSON.stringify(obj))
                } catch (e) { }
            },
            get: key => {
                try {
                    const raw = localStorage.getItem(key)
                    if (!raw) return null
                    const obj = JSON.parse(raw)
                    if (obj.expiry && Date.now() > obj.expiry) {
                        localStorage.removeItem(key)
                        return null
                    }
                    return obj.value
                } catch (e) {
                    return null
                }
            }
        },

        // 简单加载状态控制器
        setLoading: {
            add: el => { if (el) el.classList.add('loading') },
            remove: el => { if (el) el.classList.remove('loading') }
        },

        // 计算元素距离页面顶部的距离
        getEleTop: ele => {
            let offset = 0
            let el = ele
            while (el) {
                offset += el.offsetTop || 0
                el = el.offsetParent
            }
            return offset
        },

        getScrollPercent: (currentTop, container = document.body) => {
            const h = container.scrollHeight - window.innerHeight
            if (h <= 0) return 100
            return Math.max(0, Math.min(100, Math.round(currentTop / h * 100)))
        },

        // 显示简易提示（当 Snackbar 未配置时）
        snackbarShow: (text) => {
            if (window.Snackbar && typeof Snackbar.show === 'function') {
                Snackbar.show({ text, pos: 'bottom-center', duration: 2000 })
                return
            }
            // fallback 简易提示
            let el = document.getElementById('btf-snackbar')
            if (!el) {
                el = document.createElement('div')
                el.id = 'btf-snackbar'
                el.style.cssText = 'position:fixed;left:50%;bottom:20px;transform:translateX(-50%);background:rgba(0,0,0,0.7);color:#fff;padding:8px 12px;border-radius:4px;z-index:99999;opacity:0;transition:opacity .2s'
                document.body.appendChild(el)
            }
            el.textContent = text
            el.style.opacity = 1
            setTimeout(() => { el.style.opacity = 0 }, 1800)
        },

        // 全局函数注册（用于在 PJAX 等场景中注册回调）
        addGlobalFn: (name, fn, key) => {
            window.globalFn = window.globalFn || {}
            window.globalFn[name] = window.globalFn[name] || {}
            if (key) window.globalFn[name][key] = fn
            else window.globalFn[name] = fn
        },

        updateAnchor: hash => {
            if (!hash) return
            history.replaceState(null, null, hash)
        },

        // 判断元素是否隐藏
        isHidden: ele => ele.offsetParent === null || window.getComputedStyle(ele).display === 'none',

        // 小工具
        diffDate: (dateStr, isShort = false) => {
            try {
                const date = new Date(dateStr)
                const diff = Date.now() - date.getTime()
                const days = Math.floor(diff / (24 * 3600 * 1000))
                if (isShort) return `${days} 天前`
                return `${days} 天`
            } catch (e) {
                return dateStr
            }
        }
    }

    // 合并到全局 btf
    window.btf = Object.assign({}, window.btf || {}, btfFn)
})()
