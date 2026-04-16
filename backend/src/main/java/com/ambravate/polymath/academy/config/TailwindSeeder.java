package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

/**
 * Tailwind CSS — Chunk TW-A: Utility-First Fundamentals
 *
 * The guided practice exercises use Java's System.out.println() to inscribe Tailwind
 * class names, testing conceptual knowledge through the existing code runner.
 */
@Component
public class TailwindSeeder extends AbstractChunkSeeder {

    public TailwindSeeder(ChunkRepository chunkRepository,
                          SubChunkRepository subChunkRepository,
                          QuestionRepository questionRepository,
                          RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {

        // ── Chunk TW-A: Utility-First Fundamentals ────────────────────────────
        Chunk twa = Chunk.builder()
                .id("tw-a")
                .title("Utility-First Fundamentals")
                .glyph("🎨")
                .sortOrder(1)
                .prerequisiteIds("[]")
                .tier(LearnerPath.FOUNDATION)
                .topicId("tailwind")
                .build();
        chunkRepository.save(twa);

        seedTwA1();
        seedTwA2();
        seedTwA3();
    }

    // ── TW-A1: The Utility-First Philosophy ──────────────────────────────────

    private void seedTwA1() {
        subChunk(
            "tw-a1", "tw-a", "The Utility-First Philosophy", 1, 50, "TailwindPhilosophy.java",

            // Hook
            "<p>Imagine building a website by writing a custom CSS class for every element — "
            + "<code>.my-special-header</code>, <code>.blue-button-with-padding</code>, <code>.card-with-shadow</code>. "
            + "You spend more time naming things than building them.</p>"
            + "<p>Tailwind flips this idea on its head. Instead of inventing class names, you compose small, "
            + "single-purpose <strong>utility classes</strong> directly in your HTML. "
            + "The result? You style faster, stay consistent, and never leave your HTML file.</p>",

            // Explanation
            "<h3>What is Utility-First CSS?</h3>"
            + "<p>Traditional CSS asks you to write styles like this:</p>"
            + "<pre><code>/* styles.css */\n"
            + ".card {\n"
            + "  background-color: white;\n"
            + "  padding: 16px;\n"
            + "  border-radius: 8px;\n"
            + "  box-shadow: 0 2px 8px rgba(0,0,0,0.1);\n"
            + "}</code></pre>"
            + "<pre><code>&lt;!-- HTML --&gt;\n"
            + "&lt;div class=\"card\"&gt;Content&lt;/div&gt;</code></pre>"
            + "<p>With Tailwind, you apply those same styles using pre-built utilities directly in HTML:</p>"
            + "<pre><code>&lt;div class=\"bg-white p-4 rounded-lg shadow\"&gt;Content&lt;/div&gt;</code></pre>"
            + "<p>Each class does exactly one thing: <code>bg-white</code> sets the background, "
            + "<code>p-4</code> adds padding, <code>rounded-lg</code> rounds corners, <code>shadow</code> adds the drop shadow.</p>"
            + "<h3>Why Does This Work?</h3>"
            + "<ul>"
            + "<li><strong>No naming paralysis</strong> — Tailwind's classes are descriptive by design</li>"
            + "<li><strong>Consistent design system</strong> — every colour, spacing, and size comes from a curated scale</li>"
            + "<li><strong>No context switching</strong> — you stay in your HTML/JSX, not jumping to a separate CSS file</li>"
            + "<li><strong>Predictable output</strong> — a class always does the same thing, everywhere</li>"
            + "</ul>"
            + "<h3>The Core Naming Pattern</h3>"
            + "<p>Most Tailwind utilities follow the pattern: <code>property-value</code></p>"
            + "<pre><code>bg-blue-500    → background-color: blue (shade 500)\n"
            + "text-white     → color: white\n"
            + "p-4            → padding: 1rem (16px)\n"
            + "rounded-md     → border-radius: 6px\n"
            + "font-bold      → font-weight: 700</code></pre>",

            // Story
            story(
                n("You arrive at the Arcane Academy's <em>Scriptorium</em> — a vast hall where enchanters inscribe "
                  + "visual spells onto parchment. The senior scribe, Lyra, looks up from her work."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Ah, a new student. Tell me — when you write a spell, do you invent a new name for every rune combination? "
                  + "That would take forever. We use <strong>glyphs</strong>: small, focused symbols that each do one thing."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Tailwind CSS works the same way. Instead of naming a whole incantation, you compose "
                  + "pre-made glyphs — <em>utility classes</em> — directly on your element. "
                  + "<code>bg-white</code> for white background. <code>p-4</code> for padding. "
                  + "<code>shadow</code> for the drop effect."),
                e("Composing glyphs",
                  "&lt;div class=\"bg-white p-4 rounded-lg shadow\"&gt;\n  Your content here\n&lt;/div&gt;"),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "The Academy calls this the <strong>utility-first philosophy</strong>. "
                  + "Each glyph is precise, reusable, and consistent. Now tell me — "
                  + "what class would make text appear in white?")
            ),

            // Guided practice instructions
            "<p>The Academy's Inscription Room records class names to test your knowledge. "
            + "Use <code>System.out.println()</code> to inscribe the correct Tailwind class for each task:</p>"
            + "<ol>"
            + "<li>Print the class that sets a <strong>white background</strong>: <code>bg-white</code></li>"
            + "<li>Print the class that adds <strong>padding on all sides</strong> of size 4: <code>p-4</code></li>"
            + "<li>Print the class that adds a <strong>drop shadow</strong>: <code>shadow</code></li>"
            + "</ol>",

            // Starter code
            "// Inscribe each Tailwind class on a new line\nSystem.out.println(\"\");\nSystem.out.println(\"\");\nSystem.out.println(\"\");\n",

            // Tests
            tests(
                test("White background class", "null", "bg-white"),
                test("Padding-4 class", "null", "p-4"),
                test("Shadow class", "null", "shadow")
            ),

            // Feynman
            "Explain the utility-first philosophy of Tailwind CSS. Why is it different from traditional CSS, and what problem does it solve?"
        );

        // TW-A1 Questions
        mcQuestion("tw-a1", QuestionTier.RECALL,
            "<p>Which term describes Tailwind's approach to styling?</p>",
            new String[]{"Utility-first", "Semantic CSS", "Inline styles", "CSS-in-JS"},
            "Utility-first",
            "<p>Tailwind uses a <em>utility-first</em> approach: small, single-purpose classes "
            + "are composed together directly in HTML rather than creating custom class names.</p>");

        mcQuestion("tw-a1", QuestionTier.RECALL,
            "<p>Which Tailwind class adds a white background to an element?</p>",
            new String[]{"bg-white", "background-white", "color-white", "white-bg"},
            "bg-white",
            "<p>Tailwind's background colour utilities follow the pattern <code>bg-{color}</code>. "
            + "<code>bg-white</code> sets <code>background-color: white</code>.</p>");

        tfQuestion("tw-a1", QuestionTier.RECALL,
            "<p>In Tailwind, you write custom CSS class names for each component you create.</p>",
            "False",
            "<p>Tailwind's utility-first approach means you compose pre-built single-purpose classes "
            + "directly in your HTML. You rarely write custom class names.</p>");

        mcQuestion("tw-a1", QuestionTier.APPLICATION,
            "<p>A developer wants to give an element rounded corners, a white background, and some padding. "
            + "Which set of Tailwind classes achieves this?</p>",
            new String[]{"rounded bg-white p-4", ".rounded .bg-white .p-4", "border-radius background padding", "style=\"border-radius: 4px\""},
            "rounded bg-white p-4",
            "<p>Tailwind classes are space-separated in the <code>class</code> attribute. "
            + "<code>rounded</code> adds border-radius, <code>bg-white</code> adds white background, "
            + "<code>p-4</code> adds padding. The dot-prefix and raw CSS syntax are not Tailwind.</p>");
    }

    // ── TW-A2: Text & Colour Utilities ───────────────────────────────────────

    private void seedTwA2() {
        subChunk(
            "tw-a2", "tw-a", "Text & Colour Utilities", 2, 50, "TextColour.java",

            // Hook
            "<p>Typography is the voice of your interface. Size, weight, and colour communicate "
            + "hierarchy, emphasis, and meaning before the user reads a single word.</p>"
            + "<p>Tailwind gives you a precise, consistent scale for text — no more guessing what "
            + "<code>font-size: 1.125rem</code> means at a glance.</p>",

            // Explanation
            "<h3>Text Size</h3>"
            + "<p>Use <code>text-{size}</code> to control font size:</p>"
            + "<pre><code>text-xs    → 12px\n"
            + "text-sm    → 14px\n"
            + "text-base  → 16px  (default)\n"
            + "text-lg    → 18px\n"
            + "text-xl    → 20px\n"
            + "text-2xl   → 24px\n"
            + "text-4xl   → 36px</code></pre>"
            + "<h3>Font Weight</h3>"
            + "<p>Use <code>font-{weight}</code> to control boldness:</p>"
            + "<pre><code>font-light    → font-weight: 300\n"
            + "font-normal   → font-weight: 400\n"
            + "font-medium   → font-weight: 500\n"
            + "font-semibold → font-weight: 600\n"
            + "font-bold     → font-weight: 700</code></pre>"
            + "<h3>Text Colour</h3>"
            + "<p>Use <code>text-{color}-{shade}</code> to set text colour. Tailwind's palette uses shades from 50 (lightest) to 900 (darkest):</p>"
            + "<pre><code>text-gray-500    → medium grey\n"
            + "text-blue-600    → medium blue\n"
            + "text-red-500     → medium red\n"
            + "text-green-700   → dark green\n"
            + "text-white       → white\n"
            + "text-black       → black</code></pre>"
            + "<h3>Combining Them</h3>"
            + "<pre><code>&lt;h1 class=\"text-4xl font-bold text-gray-900\"&gt;Welcome&lt;/h1&gt;\n"
            + "&lt;p class=\"text-base font-normal text-gray-600\"&gt;Subtitle text here&lt;/p&gt;</code></pre>",

            // Story
            story(
                n("Lyra leads you to the Academy's <em>Calligraphy Chamber</em>, where every inscription must "
                  + "be crafted with precision. The wrong size or shade and the spell loses its power."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Typography is the first magic students learn here. "
                  + "In Tailwind, size follows a scale: <code>text-sm</code>, <code>text-base</code>, "
                  + "<code>text-lg</code>, <code>text-xl</code>... Each step larger, each name precise."),
                e("Heading incantation",
                  "&lt;h1 class=\"text-3xl font-bold text-gray-900\"&gt;\n  The Arcane Academy\n&lt;/h1&gt;"),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Colour follows the pattern <code>text-{colour}-{shade}</code>. "
                  + "A shade of 500 is balanced — not too light, not too dark. "
                  + "900 is near black. 100 is barely a whisper of colour."),
                n("She gestures to a parchment displaying a perfectly styled heading — large, bold, deep grey. "
                  + "Your task is to replicate it.")
            ),

            // Guided practice
            "<p>Inscribe the Tailwind classes that produce the following text styles:</p>"
            + "<ol>"
            + "<li>Print the class for <strong>large text</strong> (18px): <code>text-lg</code></li>"
            + "<li>Print the class for <strong>bold font weight</strong>: <code>font-bold</code></li>"
            + "<li>Print the class for <strong>medium grey text</strong> (shade 500): <code>text-gray-500</code></li>"
            + "</ol>",

            // Starter code
            "// Inscribe each class name on a new line\nSystem.out.println(\"\");\nSystem.out.println(\"\");\nSystem.out.println(\"\");\n",

            // Tests
            tests(
                test("Large text class", "null", "text-lg"),
                test("Bold font class", "null", "font-bold"),
                test("Medium grey text class", "null", "text-gray-500")
            ),

            // Feynman
            "Explain how Tailwind's text sizing and colour system works. How do the class names make the design scale predictable?"
        );

        // TW-A2 Questions
        mcQuestion("tw-a2", QuestionTier.RECALL,
            "<p>Which Tailwind class makes text bold?</p>",
            new String[]{"font-bold", "text-bold", "bold", "weight-bold"},
            "font-bold",
            "<p>Font weight utilities use the pattern <code>font-{weight}</code>. "
            + "<code>font-bold</code> sets <code>font-weight: 700</code>.</p>");

        mcQuestion("tw-a2", QuestionTier.RECALL,
            "<p>Which Tailwind class produces large text (18px)?</p>",
            new String[]{"text-lg", "font-large", "size-lg", "text-large"},
            "text-lg",
            "<p>Text size utilities follow the pattern <code>text-{size}</code> using Tailwind's named scale: "
            + "xs, sm, base, lg, xl, 2xl, etc.</p>");

        mcQuestion("tw-a2", QuestionTier.RECALL,
            "<p>What colour shade is darkest in Tailwind's palette?</p>",
            new String[]{"900", "500", "100", "800"},
            "900",
            "<p>Tailwind's colour shades run from 50 (lightest) to 900 (darkest). "
            + "<code>text-gray-900</code> is nearly black.</p>");

        codeQuestion("tw-a2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
            "<p>What text colour does this element have?</p>",
            "&lt;p class=\"text-blue-600 font-semibold text-sm\"&gt;Hello&lt;/p&gt;",
            "Medium blue",
            "<p><code>text-blue-600</code> applies a medium-to-dark blue from Tailwind's blue scale. "
            + "Shade 600 is deeper than the midpoint 500.</p>");
    }

    // ── TW-A3: Spacing with Margin & Padding ─────────────────────────────────

    private void seedTwA3() {
        subChunk(
            "tw-a3", "tw-a", "Spacing with Margin & Padding", 3, 50, "Spacing.java",

            // Hook
            "<p>Space is design. The gap between elements isn't empty — it's breathing room that guides "
            + "the eye and communicates relationships.</p>"
            + "<p>Tailwind's spacing utilities give you a consistent scale from 0 to 96, so every space "
            + "in your interface feels intentional.</p>",

            // Explanation
            "<h3>Padding</h3>"
            + "<p>Padding adds space <em>inside</em> an element, between its content and its border.</p>"
            + "<pre><code>p-4      → padding: 1rem (all sides)\n"
            + "px-4     → padding-left + padding-right: 1rem\n"
            + "py-4     → padding-top + padding-bottom: 1rem\n"
            + "pt-4     → padding-top: 1rem\n"
            + "pb-4     → padding-bottom: 1rem\n"
            + "pl-4     → padding-left: 1rem\n"
            + "pr-4     → padding-right: 1rem</code></pre>"
            + "<h3>Margin</h3>"
            + "<p>Margin adds space <em>outside</em> an element, separating it from neighbours.</p>"
            + "<pre><code>m-4      → margin: 1rem (all sides)\n"
            + "mx-4     → margin-left + margin-right: 1rem\n"
            + "my-4     → margin-top + margin-bottom: 1rem\n"
            + "mt-4     → margin-top: 1rem\n"
            + "mb-4     → margin-bottom: 1rem\n"
            + "ml-4     → margin-left: 1rem\n"
            + "mr-4     → margin-right: 1rem\n"
            + "mx-auto  → margin-left + right: auto (horizontally centres the element)</code></pre>"
            + "<h3>The Spacing Scale</h3>"
            + "<p>Each number maps to a multiple of 0.25rem (4px):</p>"
            + "<pre><code>0  → 0px\n"
            + "1  → 4px\n"
            + "2  → 8px\n"
            + "4  → 16px\n"
            + "8  → 32px\n"
            + "16 → 64px</code></pre>"
            + "<h3>Example</h3>"
            + "<pre><code>&lt;div class=\"mx-auto max-w-lg p-6 mt-8\"&gt;\n"
            + "  Centred card with padding and top margin\n"
            + "&lt;/div&gt;</code></pre>",

            // Story
            story(
                n("The final lesson in the Scriptorium: <em>spatial arrangement</em>. "
                  + "Lyra rolls out a long scroll and begins placing glyphs with careful deliberation."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Every rune on this scroll needs breathing room. Too tight and the magic bleeds together. "
                  + "Too loose and the reader loses the thread. Tailwind's spacing scale keeps everything in harmony."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Padding pushes space <em>inside</em> — <code>p-4</code> on all sides, "
                  + "<code>px-6</code> only on left and right. "
                  + "Margin pushes space <em>outside</em> — <code>mt-8</code> adds space above, "
                  + "<code>mx-auto</code> centres the element horizontally."),
                e("Centred scroll with spacing",
                  "&lt;div class=\"mx-auto max-w-2xl p-8 mt-12\"&gt;\n  The ancient tome rests here\n&lt;/div&gt;"),
                n("You study the arrangement. Each number represents a step on the scale — 4 means 16px, "
                  + "8 means 32px. The pattern clicks into place.")
            ),

            // Guided practice
            "<p>Inscribe the Tailwind spacing classes for each scenario:</p>"
            + "<ol>"
            + "<li>Print the class for <strong>padding of 4 on all sides</strong>: <code>p-4</code></li>"
            + "<li>Print the class for <strong>horizontal margin auto</strong> (centres element): <code>mx-auto</code></li>"
            + "<li>Print the class for <strong>top margin of 8</strong>: <code>mt-8</code></li>"
            + "</ol>",

            // Starter code
            "// Inscribe each class name on a new line\nSystem.out.println(\"\");\nSystem.out.println(\"\");\nSystem.out.println(\"\");\n",

            // Tests
            tests(
                test("Padding all sides class", "null", "p-4"),
                test("Horizontal auto margin class", "null", "mx-auto"),
                test("Top margin 8 class", "null", "mt-8")
            ),

            // Feynman
            "Explain the difference between padding and margin in Tailwind. How does the spacing scale (numbers like 4, 8) relate to actual pixel values?"
        );

        // TW-A3 Questions
        mcQuestion("tw-a3", QuestionTier.RECALL,
            "<p>Which Tailwind class adds padding of 4 on all sides?</p>",
            new String[]{"p-4", "pad-4", "padding-4", "space-4"},
            "p-4",
            "<p>Padding utilities use the pattern <code>p-{size}</code> for all sides. "
            + "<code>p-4</code> sets <code>padding: 1rem</code> on every side.</p>");

        mcQuestion("tw-a3", QuestionTier.RECALL,
            "<p>Which Tailwind class adds margin only on the left?</p>",
            new String[]{"ml-4", "margin-left-4", "m-left-4", "lm-4"},
            "ml-4",
            "<p>Directional margin utilities use the pattern <code>m{direction}-{size}</code>. "
            + "Direction shorthand: l=left, r=right, t=top, b=bottom, x=horizontal, y=vertical.</p>");

        tfQuestion("tw-a3", QuestionTier.RECALL,
            "<p>In Tailwind, <code>mx-auto</code> centres an element horizontally.</p>",
            "True",
            "<p><code>mx-auto</code> sets <code>margin-left: auto</code> and <code>margin-right: auto</code>, "
            + "which centres block elements when combined with a defined width.</p>");

        mcQuestion("tw-a3", QuestionTier.APPLICATION,
            "<p>How many pixels does <code>p-8</code> add as padding?</p>",
            new String[]{"32px", "8px", "16px", "64px"},
            "32px",
            "<p>Each Tailwind spacing unit is 4px. So <code>p-8</code> = 8 × 4px = 32px (2rem).</p>");
    }
}
