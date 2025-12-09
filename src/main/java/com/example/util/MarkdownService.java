package com.example.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * 将 Markdown 文本转换为安全的 HTML，并提供简单的摘要能力。
 */
public class MarkdownService {
    private final Parser parser;
    private final HtmlRenderer renderer;
    private final Safelist safelist;

    public MarkdownService() {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
        this.safelist = Safelist.relaxed()
                .addTags("pre", "code", "table", "thead", "tbody", "tr", "th", "td")
                .addAttributes("a", "target", "rel")
                .addAttributes("img", "src", "alt", "title");
    }

    public String toSafeHtml(String markdown) {
        if (markdown == null) {
            markdown = "";
        }
        Node document = parser.parse(markdown);
        String rawHtml = renderer.render(document);
        return Jsoup.clean(rawHtml, safelist);
    }

    public String extractDescription(String html, int maxLen) {
        if (html == null) {
            return "";
        }
        String text = Jsoup.parse(html).text();
        if (text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen).trim() + "...";
    }
}
