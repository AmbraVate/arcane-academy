package com.ambravate.arcane.academy.content.seeder;

import java.util.List;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

/**
 * Converts Markdown content to HTML for storage in the database.
 *
 * Detection rule: a content string that begins with '<' (after trimming) is treated as
 * already-HTML and passed through unchanged. Everything else is parsed as Markdown.
 * This lets new lessons be authored in clean Markdown while existing HTML content
 * requires no migration.
 *
 * Extensions enabled: GFM tables, GFM strikethrough.
 */
@Component
public class MarkdownConverter {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownConverter() {
        var extensions = List.of(TablesExtension.create(), StrikethroughExtension.create());
        this.parser = Parser.builder().extensions(extensions).build();
        this.renderer = HtmlRenderer.builder().extensions(extensions).build();
    }

    /**
     * If {@code value} is null or blank, returns it unchanged.
     * If it starts with '<', it is already HTML — returned as-is.
     * Otherwise it is parsed as Markdown and rendered to HTML.
     */
    public String toHtml(String value) {
        if (value == null || value.isBlank()) return value;
        String trimmed = value.stripLeading();
        if (trimmed.startsWith("<")) return value;
        return renderer.render(parser.parse(value));
    }
}
