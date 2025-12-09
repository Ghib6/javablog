document.addEventListener('DOMContentLoaded', () => {
    let headerContentWidth, $nav
    let mobileSidebarOpen = false

    const adjustMenu = init => {
        const getAllWidth = ele => Array.from(ele).reduce((width, i) => width + i.offsetWidth, 0)

        if (init) {
            const blogInfoWidth = getAllWidth(document.querySelector('#blog-info > a').children)
            const menusWidth = getAllWidth(document.getElementById('menus').children)
            headerContentWidth = blogInfoWidth + menusWidth
            $nav = document.getElementById('nav')
        }

        const hideMenuIndex = window.innerWidth <= 768 || headerContentWidth > $nav.offsetWidth - 120
        $nav.classList.toggle('hide-menu', hideMenuIndex)
    }

    document.addEventListener('DOMContentLoaded', () => {
        let headerContentWidth, $nav
        let mobileSidebarOpen = false

        const adjustMenu = init => {
            const getAllWidth = ele => Array.from(ele).reduce((width, i) => width + i.offsetWidth, 0)

            if (init) {
                const blogInfoWidth = getAllWidth(document.querySelector('#blog-info > a').children)
                const menusWidth = getAllWidth(document.getElementById('menus').children)
                headerContentWidth = blogInfoWidth + menusWidth
                $nav = document.getElementById('nav')
            }

            const hideMenuIndex = window.innerWidth <= 768 || headerContentWidth > $nav.offsetWidth - 120
            $nav.classList.toggle('hide-menu', hideMenuIndex)
        }

        // 初始化header
        const initAdjust = () => {
            adjustMenu(true)
            $nav.classList.add('show')
        }

        // sidebar menus
        const sidebarFn = {
            open: () => {
                btf.overflowPaddingR.add()
                btf.animateIn(document.getElementById('menu-mask'), 'to_show 0.5s')
                document.getElementById('sidebar-menus').classList.add('open')
                mobileSidebarOpen = true
            },
            close: () => {
                btf.overflowPaddingR.remove()
                btf.animateOut(document.getElementById('menu-mask'), 'to_hide 0.5s')
                document.getElementById('sidebar-menus').classList.remove('open')
                mobileSidebarOpen = false
            }
        }

        /**
         * 首頁top_img底下的箭頭
         */
        const scrollDownInIndex = () => {
            const handleScrollToDest = () => {
                btf.scrollToDest(document.getElementById('content-inner').offsetTop, 300)
            }

            const $scrollDownEle = document.getElementById('scroll-down')
            $scrollDownEle && btf.addEventListenerPjax($scrollDownEle, 'click', handleScrollToDest)
        }

        /**
         * 代碼
         * 只適用於Hexo默認的代碼渲染
         */
        const addHighlightTool = () => {
            const highLight = GLOBAL_CONFIG.highlight
            if (!highLight) return

            const { highlightCopy, highlightLang, highlightHeightLimit, highlightFullpage, highlightMacStyle, plugin } = highLight
            const isHighlightShrink = GLOBAL_CONFIG_SITE.isHighlightShrink
            const isShowTool = highlightCopy || highlightLang || isHighlightShrink !== undefined || highlightFullpage || highlightMacStyle
            const $figureHighlight = plugin === 'highlight.js' ? document.querySelectorAll('figure.highlight') : document.querySelectorAll('pre[class*="language-"]')

            if (!((isShowTool || highlightHeightLimit) && $figureHighlight.length)) return

            const isPrismjs = plugin === 'prismjs'
            const highlightShrinkClass = isHighlightShrink === true ? 'closed' : ''
            const highlightShrinkEle = isHighlightShrink !== undefined ? '<i class="fas fa-angle-down expand"></i>' : ''
            const highlightCopyEle = highlightCopy ? '<div class="copy-notice"></div><i class="fas fa-paste copy-button"></i>' : ''
            const highlightMacStyleEle = '<div class="macStyle"><div class="mac-close"></div><div class="mac-minimize"></div><div class="mac-maximize"></div></div>'
            const highlightFullpageEle = highlightFullpage ? '<i class="fa-solid fa-up-right-and-down-left-from-center fullpage-button"></i>' : ''

            const alertInfo = (ele, text) => {
                if (GLOBAL_CONFIG.Snackbar !== undefined) {
                    btf.snackbarShow(text)
                } else {
                    ele.textContent = text
                    ele.style.opacity = 1
                    setTimeout(() => { ele.style.opacity = 0 }, 800)
                }
            }

            const copy = async (text, ctx) => {
                try {
                    await navigator.clipboard.writeText(text)
                    alertInfo(ctx, GLOBAL_CONFIG.copy.success)
                } catch (err) {
                    console.error('Failed to copy: ', err)
                    alertInfo(ctx, GLOBAL_CONFIG.copy.noSupport)
                }
            }

            // click events
            const highlightCopyFn = (ele, clickEle) => {
                const $buttonParent = ele.parentNode
                $buttonParent.classList.add('copy-true')
                const preCodeSelector = isPrismjs ? 'pre code' : 'table .code pre'
                const codeElement = $buttonParent.querySelector(preCodeSelector)
                if (!codeElement) return
                copy(codeElement.innerText, clickEle.previousElementSibling)
                $buttonParent.classList.remove('copy-true')
            }

            const highlightShrinkFn = ele => ele.classList.toggle('closed')

            const codeFullpage = (item, clickEle) => {
                const wrapEle = item.closest('figure.highlight')
                const isFullpage = wrapEle.classList.toggle('code-fullpage')

                document.body.style.overflow = isFullpage ? 'hidden' : ''
                clickEle.classList.toggle('fa-down-left-and-up-right-to-center', isFullpage)
                clickEle.classList.toggle('fa-up-right-and-down-left-from-center', !isFullpage)
            }

            const highlightToolsFn = e => {
                const $target = e.target.classList
                const currentElement = e.currentTarget
                if ($target.contains('expand')) highlightShrinkFn(currentElement)
                else if ($target.contains('copy-button')) highlightCopyFn(currentElement, e.target)
                else if ($target.contains('fullpage-button')) codeFullpage(currentElement, e.target)
            }

            const expandCode = e => e.currentTarget.classList.toggle('expand-done')

            // 獲取隱藏狀態下元素的真實高度
            const getActualHeight = item => {
                const hiddenElements = new Map()

                const fix = () => {
                    let current = item
                    while (current !== document.body && current != null) {
                        if (window.getComputedStyle(current).display === 'none') {
                            hiddenElements.set(current, current.getAttribute('style') || '')
                        }
                        current = current.parentNode
                    }

                    const style = 'visibility: hidden !important; display: block !important;'
                    hiddenElements.forEach((originalStyle, elem) => {
                        elem.setAttribute('style', originalStyle ? originalStyle + ';' + style : style)
                    })
                }

                const restore = () => {
                    hiddenElements.forEach((originalStyle, elem) => {
                        if (originalStyle === '') elem.removeAttribute('style')
                        else elem.setAttribute('style', originalStyle)
                    })
                }

                fix()
                const height = item.offsetHeight
                restore()
                return height
            }

            const createEle = (lang, item) => {
                const fragment = document.createDocumentFragment()

                if (isShowTool) {
                    const hlTools = document.createElement('div')
                    hlTools.className = `highlight-tools ${highlightShrinkClass}`
                    hlTools.innerHTML = highlightMacStyleEle + highlightShrinkEle + lang + highlightCopyEle + highlightFullpageEle
                    btf.addEventListenerPjax(hlTools, 'click', highlightToolsFn)
                    fragment.appendChild(hlTools)
                }

                if (highlightHeightLimit && getActualHeight(item) > highlightHeightLimit + 30) {
                    const ele = document.createElement('div')
                    ele.className = 'code-expand-btn'
                    ele.innerHTML = '<i class="fas fa-angle-double-down"></i>'
                    btf.addEventListenerPjax(ele, 'click', expandCode)
                    fragment.appendChild(ele)
                }

                isPrismjs ? item.parentNode.insertBefore(fragment, item) : item.insertBefore(fragment, item.firstChild)
            }

            $figureHighlight.forEach(item => {
                let langName = ''
                if (isPrismjs) btf.wrap(item, 'figure', { class: 'highlight' })

                if (!highlightLang) {
                    createEle('', item)
                    return
                }

                if (isPrismjs) {
                    langName = item.getAttribute('data-language') || 'Code'
                } else {
                    langName = item.getAttribute('class').split(' ')[1]
                    if (langName === 'plain' || langName === undefined) langName = 'Code'
                }
                createEle(`<div class="code-lang">${langName}</div>`, item)
            })
        }

        /**
         * PhotoFigcaption
         */
        const addPhotoFigcaption = () => {
            if (!GLOBAL_CONFIG_SITE.isPhotoFigcaption) return
            document.querySelectorAll('#article-container img').forEach(item => {
                const altValue = item.title || item.alt
                if (!altValue) return
                const ele = document.createElement('div')
                ele.className = 'img-alt text-center'
                ele.textContent = altValue
                item.insertAdjacentElement('afterend', ele)
            })
        }

        /**
         * Lightbox
         */
        const runLightbox = () => {
            btf.loadLightbox(document.querySelectorAll('#article-container img:not(.no-lightbox)'))
        }

        /**
         * justified-gallery 圖庫排版
         */

        const fetchUrl = async url => {
            try {
                const response = await fetch(url)
                return await response.json()
            } catch (error) {
                console.error('Failed to fetch URL:', error)
                return []
            }
        }

        const runJustifiedGallery = (container, data, config) => {
            const { isButton, limit, firstLimit, tabs } = config

            const dataLength = data.length
            const maxGroupKey = Math.ceil((dataLength - firstLimit) / limit + 1)

            // Gallery configuration
            const igConfig = {
                gap: 5,
                isConstantSize: true,
                sizeRange: [150, 600],
                // useResizeObserver: true,
                // observeChildren: true,
                useTransform: true
                // useRecycle: false
            }

            const ig = new InfiniteGrid.JustifiedInfiniteGrid(container, igConfig)
            let isLayoutHidden = false

            // Utility functions
            const sanitizeString = str => (str && str.replace(/"/g, '&quot;')) || ''

            const createImageItem = item => {
                const alt = item.alt ? `alt="${sanitizeString(item.alt)}"` : ''
                const title = item.title ? `title="${sanitizeString(item.title)}"` : ''
                return `<div class="item">
                    <img src="${item.url}" data-grid-maintained-target="true" ${alt} ${title} />
                </div>`
            }

            const getItems = (nextGroupKey, count, isFirst = false) => {
                const startIndex = isFirst ? (nextGroupKey - 1) * count : (nextGroupKey - 2) * count + firstLimit
                return data.slice(startIndex, startIndex + count).map(createImageItem)
            }

            // Load more button
            const addLoadMoreButton = container => {
                const button = document.createElement('button')
                button.innerHTML = `${GLOBAL_CONFIG.infinitegrid.buttonText}<i class="fa-solid fa-arrow-down"></i>`

                button.addEventListener('click', () => {
                    button.remove()
                    btf.setLoading.add(container)
                    appendItems(ig.getGroups().length + 1, limit)
                }, { once: true })

                container.insertAdjacentElement('afterend', button)
            }

            const appendItems = (nextGroupKey, count, isFirst) => {
                ig.append(getItems(nextGroupKey, count, isFirst), nextGroupKey)
            }

            // Event handlers
            const handleRenderComplete = e => {
                if (tabs) {
                    const parentNode = container.parentNode
                    if (isLayoutHidden) {
                        parentNode.style.visibility = 'visible'
                    }
                    if (container.offsetHeight === 0) {
                        parentNode.style.visibility = 'hidden'
                        isLayoutHidden = true
                    }
                }

                const { updated, isResize, mounted } = e
                if (!updated.length || !mounted.length || isResize) return

                btf.loadLightbox(container.querySelectorAll('img:not(.medium-zoom-image)'))

                if (ig.getGroups().length === maxGroupKey) {
                    btf.setLoading.remove(container)
                    !tabs && ig.off('renderComplete', handleRenderComplete)
                    return
                }

                if (isButton) {
                    btf.setLoading.remove(container)
                    addLoadMoreButton(container)
                }
            }

            const handleRequestAppend = btf.debounce(e => {
                const nextGroupKey = (+e.groupKey || 0) + 1

                if (nextGroupKey === 1) appendItems(nextGroupKey, firstLimit, true)
                else appendItems(nextGroupKey, limit)

                if (nextGroupKey === maxGroupKey) ig.off('requestAppend', handleRequestAppend)
            }, 300)

            btf.setLoading.add(container)
            ig.on('renderComplete', handleRenderComplete)

            if (isButton) {
                appendItems(1, firstLimit, true)
            } else {
                ig.on('requestAppend', handleRequestAppend)
                ig.renderItems()
            }

            btf.addGlobalFn('pjaxSendOnce', () => ig.destroy())
        }

        const addJustifiedGallery = async (elements, tabs = false) => {
            if (!elements.length) return

            const initGallery = async () => {
                for (const element of elements) {
                    if (btf.isHidden(element) || element.classList.contains('loaded')) continue

                    const config = {
                        isButton: element.getAttribute('data-button') === 'true',
                        limit: parseInt(element.getAttribute('data-limit'), 10),
                        firstLimit: parseInt(element.getAttribute('data-first'), 10),
                        tabs
                    }

                    const container = element.firstElementChild
                    const content = container.textContent
                    container.textContent = ''
                    element.classList.add('loaded')

                    try {
                        const data = element.getAttribute('data-type') === 'url' ? await fetchUrl(content) : JSON.parse(content)
                        runJustifiedGallery(container, data, config)
                    } catch (error) {
                        console.error('Gallery data parsing failed:', error)
                    }
                }
            }

            if (typeof InfiniteGrid === 'function') {
                await initGallery()
            } else {
                await btf.getScript(`${GLOBAL_CONFIG.infinitegrid.js}`).then(initGallery)
            }
        }

        /**
         * rightside scroll percent
         */
        const rightsideScrollPercent = currentTop => {
            const scrollPercent = btf.getScrollPercent(currentTop, document.body)
            const goUpElement = document.getElementById('go-up')

            if (scrollPercent < 95) {
                goUpElement.classList.add('show-percent')
                goUpElement.querySelector('.scroll-percent').textContent = scrollPercent
            } else {
                goUpElement.classList.remove('show-percent')
            }
        }

        /**
         * 滾動處理
         */
        const scrollFn = () => {
            const $rightside = document.getElementById('rightside')
            const innerHeight = window.innerHeight + 56
            let initTop = 0
            const $header = document.getElementById('page-header')
            const isChatBtn = typeof chatBtn !== 'undefined'
            const isShowPercent = GLOBAL_CONFIG.percent.rightside

            // 檢查文檔高度是否小於視窗高度
            const checkDocumentHeight = () => {
                if (document.body.scrollHeight <= innerHeight) {
                    $rightside.classList.add('rightside-show')
                    return true
                }
                return false
            }

            // 如果文檔高度小於視窗高度,直接返回
            if (checkDocumentHeight()) return

            // find the scroll direction
            const scrollDirection = currentTop => {
                const result = currentTop > initTop // true is down & false is up
                initTop = currentTop
                return result
            }

            let flag = ''
            const scrollTask = btf.throttle(() => {
                const currentTop = window.scrollY || document.documentElement.scrollTop
                const isDown = scrollDirection(currentTop)
                if (currentTop > 56) {
                    if (flag === '') {
                        $header.classList.add('nav-fixed')
                        $rightside.classList.add('rightside-show')
                    }

                    if (isDown) {
                        if (flag !== 'down') {
                            $header.classList.remove('nav-visible')
                            isChatBtn && window.chatBtn.hide()
                            flag = 'down'
                        }
                    } else {
                        if (flag !== 'up') {
                            $header.classList.add('nav-visible')
                            isChatBtn && window.chatBtn.show()
                            flag = 'up'
                        }
                    }
                } else {
                    flag = ''
                    if (currentTop === 0) {
                        $header.classList.remove('nav-fixed', 'nav-visible')
                    }
                    $rightside.classList.remove('rightside-show')
                }

                isShowPercent && rightsideScrollPercent(currentTop)
                checkDocumentHeight()
            }, 300)

            btf.addEventListenerPjax(window, 'scroll', scrollTask, { passive: true })
        }

        /**
        * toc,anchor
        */
        const scrollFnToDo = () => {
            const isToc = GLOBAL_CONFIG_SITE.isToc
            const isAnchor = GLOBAL_CONFIG.isAnchor
            const $article = document.getElementById('article-container')

            if (!($article && (isToc || isAnchor))) return

            let $tocLink, $cardToc, autoScrollToc, $tocPercentage, isExpand

            if (isToc) {
                const $cardTocLayout = document.getElementById('card-toc')
                $cardToc = $cardTocLayout.querySelector('.toc-content')
                $tocLink = $cardToc.querySelectorAll('.toc-link')
                $tocPercentage = $cardTocLayout.querySelector('.toc-percentage')
                isExpand = $cardToc.classList.contains('is-expand')

                // toc元素點擊
                const tocItemClickFn = e => {
                    const target = e.target.closest('.toc-link')
                    if (!target) return

                    e.preventDefault()
                    btf.scrollToDest(btf.getEleTop(document.getElementById(decodeURI(target.getAttribute('href')).replace('#', ''))), 300)
                    if (window.innerWidth < 900) {
                        $cardTocLayout.classList.remove('open')
                    }
                }

                btf.addEventListenerPjax($cardToc, 'click', tocItemClickFn)

                autoScrollToc = item => {
                    const sidebarHeight = $cardToc.clientHeight
                    const itemOffsetTop = item.offsetTop
                    const itemHeight = item.clientHeight
                    const scrollTop = $cardToc.scrollTop
                    const offset = itemOffsetTop - scrollTop
                    const middlePosition = (sidebarHeight - itemHeight) / 2

                    if (offset !== middlePosition) {
                        $cardToc.scrollTop = scrollTop + (offset - middlePosition)
                    }
                }

                // 處理 hexo-blog-encrypt 事件
                $cardToc.style.display = 'block'
            }

            // find head position & add active class
            const $articleList = $article.querySelectorAll('h1,h2,h3,h4,h5,h6')
            let detectItem = ''

            const findHeadPosition = top => {
                if (top === 0) return false

                let currentId = ''
                let currentIndex = ''

                for (let i = 0; i < $articleList.length; i++) {
                    const ele = $articleList[i]
                    if (top > btf.getEleTop(ele) - 80) {
                        const id = ele.id
                        currentId = id ? '#' + encodeURI(id) : ''
                        currentIndex = i
                    } else {
                        break
                    }
                }

                if (detectItem === currentIndex) return

                if (isAnchor) btf.updateAnchor(currentId)

                detectItem = currentIndex

                if (isToc) {
                    $cardToc.querySelectorAll('.active').forEach(i => i.classList.remove('active'))

                    if (currentId) {
                        const currentActive = $tocLink[currentIndex]
                        currentActive.classList.add('active')

                        setTimeout(() => autoScrollToc(currentActive), 0)

                        if (!isExpand) {
                            let parent = currentActive.parentNode
                            while (!parent.matches('.toc')) {
                                if (parent.matches('li')) parent.classList.add('active')
                                parent = parent.parentNode
                            }
                        }
                    }
                }
            }

            // main of scroll
            const tocScrollFn = btf.throttle(() => {
                const currentTop = window.scrollY || document.documentElement.scrollTop
                if (isToc && GLOBAL_CONFIG.percent.toc) {
                    $tocPercentage.textContent = btf.getScrollPercent(currentTop, $article)
                }
                findHeadPosition(currentTop)
            }, 100)

            btf.addEventListenerPjax(window, 'scroll', tocScrollFn, { passive: true })
        }

        const handleThemeChange = mode => {
            const globalFn = window.globalFn || {}
            const themeChange = globalFn.themeChange || {}
            if (!themeChange) {
                return
            }

            Object.keys(themeChange).forEach(key => {
                const themeChangeFn = themeChange[key]
                if (['disqus', 'disqusjs'].includes(key)) {
                    setTimeout(() => themeChangeFn(mode), 300)
                } else {
                    themeChangeFn(mode)
                }
            })
        }
