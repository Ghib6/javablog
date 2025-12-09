document.addEventListener('DOMContentLoaded', () => {
    const cfg = (window.GLOBAL_CONFIG && GLOBAL_CONFIG.translate) || {}
    const { defaultEncoding = 2, translateDelay = 300, msgToTraditionalChinese = '繁體', msgToSimplifiedChinese = '简体' } = cfg
    const targetEncodingCookie = 'translate-chn-cht'

    let currentEncoding = defaultEncoding
    let targetEncoding = Number(btf.saveToLocal.get(targetEncodingCookie)) || defaultEncoding
    const translateButtonObject = document.getElementById('translateLink')

    const setLang = () => {
        document.documentElement.lang = targetEncoding === 1 ? 'zh-TW' : 'zh-CN'
    }

    // 如果存在 OpenCC 或自定义转换器，调用它；否则直接返回原文
    const Traditionalized = txt => {
        if (!txt) return ''
        if (window.opencc && typeof opencc === 'function') return opencc(txt, 's2t')
        return txt
    }

    const Simplized = txt => {
        if (!txt) return ''
        if (window.opencc && typeof opencc === 'function') return opencc(txt, 't2s')
        return txt
    }

    const translateText = txt => {
        if (!txt) return ''
        if (currentEncoding === 1 && targetEncoding === 2) return Simplized(txt)
        if (currentEncoding === 2 && targetEncoding === 1) return Traditionalized(txt)
        return txt
    }

    const translateBody = (root) => {
        const nodes = root ? root.childNodes : document.body.childNodes
        for (const node of nodes) {
            if (node === translateButtonObject) continue
            if (node.nodeType === Node.ELEMENT_NODE) {
                const { title, alt, placeholder, value, type, tagName } = node
                if (title) node.title = translateText(title)
                if (alt) node.alt = translateText(alt)
                if (placeholder) node.placeholder = translateText(placeholder)
                if (tagName === 'INPUT' && value && type !== 'text' && type !== 'hidden') node.value = translateText(value)
                translateBody(node)
            } else if (node.nodeType === Node.TEXT_NODE) {
                node.data = translateText(node.data)
            }
        }
    }

    const translatePage = () => {
        if (targetEncoding === 1) {
            currentEncoding = 1
            targetEncoding = 2
            if (translateButtonObject) translateButtonObject.textContent = msgToTraditionalChinese
        } else {
            currentEncoding = 2
            targetEncoding = 1
            if (translateButtonObject) translateButtonObject.textContent = msgToSimplifiedChinese
        }
        btf.saveToLocal.set(targetEncodingCookie, targetEncoding, 2)
        setLang()
        setTimeout(() => translateBody(), translateDelay)
    }

    window.translateFn = {
        translatePage,
        Traditionalized,
        Simplized,
        translateInitialization: () => {
            setLang()
            if (translateButtonObject) {
                translateButtonObject.textContent = targetEncoding === 1 ? msgToTraditionalChinese : msgToSimplifiedChinese
                translateButtonObject.addEventListener('click', translatePage)
            }
        }
    }

    btf.addGlobalFn('pjaxComplete', window.translateFn.translateInitialization, 'translateInitialization')
    window.translateFn.translateInitialization()
})
