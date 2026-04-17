package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

/**
 * Tailwind CSS — Chunk TW-A: Utility-First Fundamentals
 *
 * Guided practice exercises use a real HTML editor with live iframe preview.
 * Submissions are validated server-side with Jsoup: each test spec checks that
 * a CSS selector matches an element which carries the required Tailwind class.
 *
 * Test spec format (stored in guided_practice_tests_json):
 *   [{"label":"…","selector":".card","requiredClass":"bg-white"}, …]
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
        seedTwA4();
        seedTwA5();
    }

    // ── Tailwind test spec helper ─────────────────────────────────────────────
    // Builds a JSON object that TailwindPracticeService understands:
    // {"label":"…","selector":"…","requiredClass":"…"}
    private String twTest(String label, String selector, String requiredClass) {
        return "{\"label\":\"" + esc(label)
                + "\",\"selector\":\"" + esc(selector)
                + "\",\"requiredClass\":\"" + esc(requiredClass) + "\"}";
    }

    // ── TW-A1: The Utility-First Philosophy ──────────────────────────────────

    private void seedTwA1() {
        subChunk(
            "tw-a1", "tw-a", "The Utility-First Philosophy", 1, 50, "index.html",

            // Hook
            "<p>Imagine building a website by writing a custom CSS class for every element — "
            + "<code>.my-special-header</code>, <code>.blue-button-with-padding</code>, <code>.card-with-shadow</code>. "
            + "You spend more time naming things than building them.</p>"
            + "<p>Tailwind flips this idea on its head. Instead of inventing class names, you compose small, "
            + "single-purpose <strong>utility classes</strong> directly in your HTML. "
            + "The result? You style faster, stay consistent, and never leave your HTML file.</p>",

            // Explanation
            "<h3>What is Utility-First CSS?</h3>"
            + "<p>Traditional CSS asks you to write styles in a separate file:</p>"
            + "<pre><code>/* styles.css */\n"
            + ".card {\n"
            + "  background-color: white;\n"
            + "  padding: 16px;\n"
            + "  border-radius: 8px;\n"
            + "  box-shadow: 0 2px 8px rgba(0,0,0,0.1);\n"
            + "}</code></pre>"
            + "<p>With Tailwind, you apply those same styles using pre-built utilities directly in HTML:</p>"
            + "<pre><code>&lt;div class=\"bg-white p-4 rounded-lg shadow\"&gt;Content&lt;/div&gt;</code></pre>"
            + "<p>Each class does exactly one thing: <code>bg-white</code> sets the background, "
            + "<code>p-4</code> adds padding, <code>rounded-lg</code> rounds corners, <code>shadow</code> adds the drop shadow.</p>"
            + "<h3>Why Does This Work?</h3>"
            + "<ul>"
            + "<li><strong>No naming paralysis</strong> — Tailwind's classes are descriptive by design</li>"
            + "<li><strong>Consistent design system</strong> — every colour, spacing, and size comes from a curated scale</li>"
            + "<li><strong>No context switching</strong> — you stay in your HTML, not jumping to a separate CSS file</li>"
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
                  + "Each glyph is precise, reusable, and consistent. "
                  + "Now — inscribe a card using the utilities you've learned.")
            ),

            // Guided practice HTML (task brief shown to student)
            "<p>Style this card using Tailwind utility classes. Add the following classes to the <code>div</code> with class <code>card</code>:</p>"
            + "<ul>"
            + "<li><code>bg-white</code> — white background</li>"
            + "<li><code>p-6</code> — padding on all sides</li>"
            + "<li><code>rounded-xl</code> — rounded corners</li>"
            + "<li><code>shadow-md</code> — medium drop shadow</li>"
            + "</ul>"
            + "<p><strong>Tip:</strong> Add all four classes to the same element's <code>class</code> attribute, separated by spaces.</p>",

            // Starter HTML (displayed in the editor)
            "<div class=\"card\">\n"
            + "  <h2>Arcane Academy</h2>\n"
            + "  <p>Your utility-first journey begins here.</p>\n"
            + "</div>",

            // Tailwind test specs (Jsoup class-check format)
            tests(
                twTest("Card has white background", ".card", "bg-white"),
                twTest("Card has padding", ".card", "p-6"),
                twTest("Card has rounded corners", ".card", "rounded-xl"),
                twTest("Card has shadow", ".card", "shadow-md")
            ),

            // Feynman
            "Explain the utility-first philosophy of Tailwind CSS. Why is it different from traditional CSS, and what problem does it solve?",

            // Practice type: real HTML editor with Jsoup validation
            SubChunkPracticeType.TAILWIND
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
            "tw-a2", "tw-a", "Text & Colour Utilities", 2, 50, "index.html",

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
                  "&lt;h1 class=\"text-4xl font-bold text-gray-900\"&gt;\n  The Arcane Academy\n&lt;/h1&gt;"),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Colour follows the pattern <code>text-{colour}-{shade}</code>. "
                  + "A shade of 500 is balanced — not too light, not too dark. "
                  + "900 is near black. 100 is barely a whisper of colour."),
                n("She gestures to a parchment displaying a perfectly styled heading — large, bold, deep grey. "
                  + "Your task: recreate it using Tailwind classes.")
            ),

            // Guided practice HTML (task brief)
            "<p>Style the heading and subtitle using Tailwind text utilities:</p>"
            + "<ul>"
            + "<li>The element with class <code>heading</code> should have: <code>text-4xl</code>, <code>font-bold</code>, <code>text-gray-900</code></li>"
            + "<li>The element with class <code>subtitle</code> should have: <code>text-gray-500</code></li>"
            + "</ul>"
            + "<p><strong>Tip:</strong> Add multiple classes to the same element by separating them with spaces.</p>",

            // Starter HTML
            "<div class=\"p-8\">\n"
            + "  <h1 class=\"heading\">Arcane Academy</h1>\n"
            + "  <p class=\"subtitle\">Master the utility-first art</p>\n"
            + "</div>",

            // Tailwind test specs
            tests(
                twTest("Heading is large", ".heading", "text-4xl"),
                twTest("Heading is bold", ".heading", "font-bold"),
                twTest("Heading is dark grey", ".heading", "text-gray-900"),
                twTest("Subtitle is muted grey", ".subtitle", "text-gray-500")
            ),

            // Feynman
            "Explain how Tailwind's text sizing and colour system works. How do the class names make the design scale predictable?",

            SubChunkPracticeType.TAILWIND
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
            "<p>What text colour does this element produce?</p>",
            "<p class=\"text-blue-600 font-semibold text-sm\">Hello</p>",
            "Medium blue",
            "<p><code>text-blue-600</code> applies a medium-to-dark blue from Tailwind's blue scale. "
            + "Shade 600 is deeper than the midpoint 500.</p>");
    }

    // ── TW-A3: Spacing with Margin & Padding ─────────────────────────────────

    private void seedTwA3() {
        subChunk(
            "tw-a3", "tw-a", "Spacing with Margin & Padding", 3, 50, "index.html",

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
                  "Padding pushes space <em>inside</em> — <code>p-6</code> on all sides. "
                  + "Margin pushes space <em>outside</em> — <code>mt-8</code> adds space above, "
                  + "<code>mx-auto</code> centres the element horizontally."),
                e("Centred scroll with spacing",
                  "&lt;div class=\"wrapper py-8\"&gt;\n"
                  + "  &lt;div class=\"card mx-auto max-w-md p-6 mt-8\"&gt;\n"
                  + "    The ancient tome rests here\n"
                  + "  &lt;/div&gt;\n"
                  + "&lt;/div&gt;"),
                n("You study the arrangement. Each number represents a step on the scale — 4 means 16px, "
                  + "8 means 32px. The pattern clicks into place.")
            ),

            // Guided practice HTML (task brief)
            "<p>Apply spacing utilities to create a well-spaced, centred layout:</p>"
            + "<ul>"
            + "<li>The <code>.wrapper</code> div should have vertical padding: <code>py-8</code></li>"
            + "<li>The <code>.card</code> div should be horizontally centred: <code>mx-auto</code></li>"
            + "<li>The <code>.card</code> div should have a max width: <code>max-w-md</code></li>"
            + "<li>The <code>.card</code> div should have padding on all sides: <code>p-6</code></li>"
            + "</ul>"
            + "<p><strong>Tip:</strong> You can add all classes to an element at once, separated by spaces.</p>",

            // Starter HTML
            "<div class=\"wrapper\">\n"
            + "  <div class=\"card\">\n"
            + "    <h2>The Ancient Tome</h2>\n"
            + "    <p>Wisdom lives here, centred and breathing.</p>\n"
            + "  </div>\n"
            + "</div>",

            // Tailwind test specs
            tests(
                twTest("Wrapper has vertical padding", ".wrapper", "py-8"),
                twTest("Card is horizontally centred", ".card", "mx-auto"),
                twTest("Card has max width", ".card", "max-w-md"),
                twTest("Card has padding", ".card", "p-6")
            ),

            // Feynman
            "Explain the difference between padding and margin in Tailwind. How does the spacing scale (numbers like 4, 8) relate to actual pixel values?",

            SubChunkPracticeType.TAILWIND
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

    // ── TW-A4: Borders, Radius, Shadows & Rings ──────────────────────────────

    private void seedTwA4() {
        subChunk(
            "tw-a4", "tw-a", "Borders, Radius & Shadows", 4, 50, "index.html",

            // Hook
            "<p>Edges, corners and shadow — the three tools that turn a flat rectangle into an <em>object</em>. "
            + "A visible border says \"I am a discrete thing.\" A rounded corner says \"I am friendly.\" "
            + "A shadow says \"I float above the page.\"</p>"
            + "<p>Tailwind gives you a consistent vocabulary for all three.</p>",

            // Explanation
            "<h3>Borders</h3>"
            + "<pre><code>border          → 1px solid border\n"
            + "border-2        → 2px\n"
            + "border-t        → top edge only\n"
            + "border-gray-300 → colour the border\n"
            + "border-dashed   → dashed style</code></pre>"
            + "<h3>Rounded Corners</h3>"
            + "<pre><code>rounded-none → 0px\n"
            + "rounded-sm   → 2px\n"
            + "rounded      → 4px\n"
            + "rounded-md   → 6px\n"
            + "rounded-lg   → 8px\n"
            + "rounded-xl   → 12px\n"
            + "rounded-2xl  → 16px\n"
            + "rounded-full → fully round (pills, avatars)</code></pre>"
            + "<h3>Shadows</h3>"
            + "<pre><code>shadow-sm  → subtle\n"
            + "shadow     → default\n"
            + "shadow-md  → medium\n"
            + "shadow-lg  → pronounced\n"
            + "shadow-xl  → dramatic\n"
            + "shadow-2xl → heaviest\n"
            + "shadow-none → remove shadow</code></pre>"
            + "<h3>Rings</h3>"
            + "<p>Rings are outlines that don't shift layout — perfect for focus states:</p>"
            + "<pre><code>ring-2 ring-blue-500   → 2px blue ring around element</code></pre>",

            // Story
            story(
                n("Lyra hands you a piece of unadorned parchment. <em>\"Before it is a card,\"</em> she says, "
                  + "<em>\"it must know where it begins and ends.\"</em>"),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Three enchantments give an element its physicality: the <strong>border</strong> — its edge, "
                  + "the <strong>radius</strong> — its softness, and the <strong>shadow</strong> — its weight in the world."),
                e("A complete object",
                  "&lt;div class=\"border border-gray-200 rounded-xl shadow-md\"&gt;\n  I am a card.\n&lt;/div&gt;"),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "And when a user focuses something with their keyboard, use a <code>ring</code>, not a border — "
                  + "rings don't shift the layout.")
            ),

            // Guided practice brief
            "<p>Give the card element: <code>border</code>, <code>border-gray-200</code>, "
            + "<code>rounded-xl</code>, and <code>shadow-md</code>.</p>",

            // Starter
            "<div class=\"card bg-white p-6\">\n"
            + "  <h3>Avatar</h3>\n"
            + "  <p>Apply a border, rounded corners, and a shadow.</p>\n"
            + "</div>",

            // Tests
            tests(
                twTest("Card has a border", ".card", "border"),
                twTest("Border is light grey", ".card", "border-gray-200"),
                twTest("Corners are extra-large rounded", ".card", "rounded-xl"),
                twTest("Card has a medium shadow", ".card", "shadow-md")
            ),

            "Why use <code>ring</code> instead of <code>border</code> for focus states? What about the box model makes this matter?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-a4", QuestionTier.RECALL,
            "<p>Which class produces a fully circular element (e.g. an avatar)?</p>",
            new String[]{"rounded-full", "rounded-circle", "rounded-xl", "border-full"},
            "rounded-full",
            "<p><code>rounded-full</code> applies a very large border-radius, giving a perfect circle for square elements.</p>");

        mcQuestion("tw-a4", QuestionTier.RECALL,
            "<p>Which class removes a shadow?</p>",
            new String[]{"shadow-none", "no-shadow", "shadow-0", "shadow-off"},
            "shadow-none",
            "<p><code>shadow-none</code> sets <code>box-shadow: none</code>, overriding any inherited shadow.</p>");

        tfQuestion("tw-a4", QuestionTier.APPLICATION,
            "<p>A <code>ring-2</code> outline shifts surrounding elements the way a <code>border-2</code> does.</p>",
            "False",
            "<p>Rings render outside the element using box-shadow, so they don't change the layout box the way borders do.</p>");
    }

    // ── TW-A5: State Variants (hover, focus, active, disabled) ───────────────

    private void seedTwA5() {
        subChunk(
            "tw-a5", "tw-a", "State Variants", 5, 60, "index.html",

            // Hook
            "<p>A button that doesn't react to the cursor feels dead. A link that doesn't show focus is unusable "
            + "with a keyboard. <strong>State variants</strong> let a single utility apply only when the element "
            + "is in a certain state — without writing a line of CSS.</p>",

            // Explanation
            "<h3>Syntax</h3>"
            + "<p>Prefix any utility with a state variant and a colon:</p>"
            + "<pre><code>hover:bg-blue-700   → background on hover\n"
            + "focus:ring-2       → ring appears on focus\n"
            + "active:scale-95    → slight shrink while clicked\n"
            + "disabled:opacity-50 → greyed out when disabled</code></pre>"
            + "<h3>Common Variants</h3>"
            + "<ul>"
            + "<li><code>hover:</code> — pointer hovers the element</li>"
            + "<li><code>focus:</code> — element has keyboard focus</li>"
            + "<li><code>focus-visible:</code> — focus from keyboard (not mouse click)</li>"
            + "<li><code>active:</code> — element being pressed</li>"
            + "<li><code>disabled:</code> — element is disabled</li>"
            + "<li><code>group-hover:</code> — a parent with class <code>group</code> is hovered</li>"
            + "<li><code>peer-checked:</code> — a sibling with class <code>peer</code> is checked</li>"
            + "</ul>"
            + "<h3>Stacking Variants</h3>"
            + "<p>Chain multiple variants together:</p>"
            + "<pre><code>dark:hover:bg-slate-800   → dark mode AND hover\n"
            + "md:focus:ring-4          → medium screens AND focus</code></pre>"
            + "<h3>Transition — Make It Smooth</h3>"
            + "<p>State changes are jarring without a transition:</p>"
            + "<pre><code>transition-colors duration-150</code></pre>",

            // Story
            story(
                n("Lyra conjures a floating button on a scroll. You reach out to touch it — the button darkens, "
                  + "then brightens again as your finger retreats."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "A lifeless button is half-built. Tailwind gives you <em>state variants</em> — "
                  + "<code>hover:</code>, <code>focus:</code>, <code>active:</code> — prefixes that make a class "
                  + "apply only in that state."),
                e("A button with feedback",
                  "&lt;button class=\"bg-blue-600 hover:bg-blue-700 focus:ring-2 transition\"&gt;\n  Cast Spell\n&lt;/button&gt;"),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Never forget <code>focus:</code>. A button that only looks good on hover betrays every keyboard user.")
            ),

            // Practice brief
            "<p>Give the <code>.btn</code> element realistic interaction feedback. Add all of:</p>"
            + "<ul>"
            + "<li><code>bg-blue-600</code> — default background</li>"
            + "<li><code>hover:bg-blue-700</code> — darker on hover</li>"
            + "<li><code>focus:ring-2</code> — ring when keyboard-focused</li>"
            + "<li><code>transition</code> — smooth the change</li>"
            + "</ul>",

            // Starter
            "<button class=\"btn text-white px-4 py-2 rounded\">\n"
            + "  Cast Spell\n"
            + "</button>",

            // Tests
            tests(
                twTest("Button has base background", ".btn", "bg-blue-600"),
                twTest("Button darkens on hover", ".btn", "hover:bg-blue-700"),
                twTest("Button shows focus ring", ".btn", "focus:ring-2"),
                twTest("Button has a transition", ".btn", "transition")
            ),

            "Why is it important to provide <code>focus:</code> styles in addition to <code>hover:</code>? Think about users who don't use a mouse.",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-a5", QuestionTier.RECALL,
            "<p>Which variant applies styles only when the user hovers over the element?</p>",
            new String[]{"hover:", "mouseover:", "over:", "pointer:"},
            "hover:",
            "<p>Tailwind uses the <code>hover:</code> prefix for pointer-hover styles.</p>");

        mcQuestion("tw-a5", QuestionTier.RECALL,
            "<p>What does <code>disabled:opacity-50</code> do?</p>",
            new String[]{"Makes the element half-transparent when disabled", "Disables the element's opacity", "Sets opacity only in dark mode", "Removes the element from the DOM"},
            "Makes the element half-transparent when disabled",
            "<p>The <code>disabled:</code> variant applies <code>opacity: 0.5</code> only when the element is disabled — a common pattern for form controls.</p>");

        tfQuestion("tw-a5", QuestionTier.APPLICATION,
            "<p>Variants can be stacked — e.g. <code>md:hover:bg-blue-700</code> applies on hover at medium screens or larger.</p>",
            "True",
            "<p>Tailwind allows variant stacking. They read left-to-right: responsive first, then state.</p>");

        scenarioQuestion("tw-a5",
            "<p>A button has <code>bg-blue-600 hover:bg-blue-700</code> but no <code>transition</code>. What's the UX problem?</p>",
            "The colour change is instant and jarring; adding transition-colors smooths it.",
            "<p>Without a transition, the hover change flips instantly. Adding <code>transition-colors</code> (or just <code>transition</code>) and optionally <code>duration-150</code> makes the change feel polished.</p>");
    }
}
