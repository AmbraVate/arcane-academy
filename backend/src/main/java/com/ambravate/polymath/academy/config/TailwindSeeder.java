package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @deprecated Content migrated to resources/content/tailwind/tw-a.json
 *             and loaded by JsonContentSeeder. This class is kept for
 *             reference during migration of TW-B and TW-C, then will be deleted.
 */
@Deprecated
@Profile("never")

/**
 * Tailwind CSS — Chunk TW-A: Basics (Foundation tier)
 *
 * Mirrors the "Basic" tier of the Tailwind roadmap:
 *   1. What Tailwind Is (utility-first mindset)
 *   2. Installation & Setup
 *   3. Spacing utilities
 *   4. Typography & Colours
 *   5. Borders, Radius & Basic Layout
 *
 * State variants, responsive design, and component extraction live in TW-B.
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

        Chunk twa = Chunk.builder()
                .id("tw-a")
                .title("Tailwind Basics")
                .glyph("\uD83C\uDFA8")
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

    private String twTest(String label, String selector, String requiredClass) {
        return "{\"label\":\"" + esc(label)
                + "\",\"selector\":\"" + esc(selector)
                + "\",\"requiredClass\":\"" + esc(requiredClass) + "\"}";
    }

    // ── TW-A1: What Tailwind Is ──────────────────────────────────────────────

    private void seedTwA1() {
        subChunk(
            "tw-a1", "tw-a", "What Tailwind Is", 1, 50, "index.html",

            "<p>Tailwind is a <strong>utility-first</strong> CSS framework. Instead of inventing class names "
            + "(<code>.card</code>, <code>.primary-button</code>) and writing CSS in a separate file, you compose "
            + "small, single-purpose classes \u2014 <code>p-4</code>, <code>bg-gray-100</code>, <code>rounded-lg</code>, "
            + "<code>shadow</code> \u2014 directly in your HTML.</p>",

            "<p>Lyra the Scribe unfurls a long scroll. <em>\u201CBefore you inscribe anything, see why we compose glyphs at all. Here is the old way.\u201D</em></p>"
            + "<p>She taps a dusty grimoire. <em>\u201CTraditional CSS asks you to name every incantation in a separate book:\u201D</em></p>"
            + "<pre><code>/* styles.css */\n"
            + ".card {\n"
            + "  background-color: white;\n"
            + "  padding: 16px;\n"
            + "  border-radius: 8px;\n"
            + "  box-shadow: 0 2px 8px rgba(0,0,0,0.1);\n"
            + "}</code></pre>"
            + "<p><em>\u201CHours lost inventing names. <code>.special-card</code>. <code>.blue-thing-we-made-on-a-Thursday</code>. And two scribes pick the same name for different spells \u2014 chaos.\u201D</em></p>"
            + "<p>She sets down a slim handbook. <em>\u201CThe Tailwind way. Compose glyphs directly on the element:\u201D</em></p>"
            + "<pre><code>&lt;div class=\"bg-white p-4 rounded-lg shadow\"&gt;Content&lt;/div&gt;</code></pre>"
            + "<p><em>\u201CFour glyphs, four decisions. <code>bg-white</code> \u2014 background. <code>p-4</code> \u2014 padding. <code>rounded-lg</code> \u2014 corners. <code>shadow</code> \u2014 drop shadow. No names to invent. No second book to hunt for.\u201D</em></p>"
            + "<p>She counts the virtues on her fingers:</p>"
            + "<ul>"
            + "<li><strong>No naming paralysis</strong> \u2014 the glyphs are already named.</li>"
            + "<li><strong>A curated palette</strong> \u2014 every colour and spacing value comes from one system.</li>"
            + "<li><strong>Stay at the parchment</strong> \u2014 you never leave your HTML.</li>"
            + "<li><strong>Predictable magic</strong> \u2014 a glyph does the same thing everywhere.</li>"
            + "</ul>"
            + "<p><em>\u201COne last pattern. Every glyph follows the same shape \u2014 <code>property-value</code>. Learn the pattern and you can guess new glyphs before you\u2019ve seen them.\u201D</em></p>"
            + "<pre><code>bg-blue-500    \u2192 background-color: blue (shade 500)\n"
            + "text-white     \u2192 color: white\n"
            + "p-4            \u2192 padding: 1rem (16px)\n"
            + "rounded-md     \u2192 border-radius: 6px\n"
            + "font-bold      \u2192 font-weight: 700</code></pre>"
            + "<p><em>\u201CProperty. Value. That is the whole Academy in three words. Now \u2014 the card below needs four glyphs. Inscribe them.\u201D</em></p>",

            story(
                n("You arrive at the Academy\u2019s <em>Scriptorium</em>, a vast hall where enchanters inscribe visual spells. "
                  + "Lyra the Scribe looks up from her work."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Ah \u2014 a new apprentice. Tell me, when you cast a spell, do you invent a new name for every rune combination? "
                  + "No. We use <strong>glyphs</strong>: small, focused symbols that each do one thing."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Tailwind works the same way. Utility classes are glyphs. Compose them on the element itself.")
            ),

            "<p>Style this card with Tailwind utilities. Add to the <code>.card</code> element:</p>"
            + "<ul>"
            + "<li><code>bg-white</code> \u2014 white background</li>"
            + "<li><code>p-6</code> \u2014 padding all sides</li>"
            + "<li><code>rounded-xl</code> \u2014 rounded corners</li>"
            + "<li><code>shadow-md</code> \u2014 medium drop shadow</li>"
            + "</ul>",

            "<div class=\"card\">\n"
            + "  <h2>Arcane Academy</h2>\n"
            + "  <p>Your utility-first journey begins here.</p>\n"
            + "</div>",

            tests(
                twTest("White background", ".card", "bg-white"),
                twTest("Padding", ".card", "p-6"),
                twTest("Rounded corners", ".card", "rounded-xl"),
                twTest("Drop shadow", ".card", "shadow-md")
            ),

            "Explain utility-first CSS in your own words. Why is composing small classes better than inventing component class names for most cases?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-a1", QuestionTier.RECALL,
            "<p>Which term describes Tailwind\u2019s approach to styling?</p>",
            new String[]{"Utility-first", "Semantic CSS", "Inline styles", "CSS-in-JS"},
            "Utility-first",
            "<p>Tailwind composes many small, single-purpose classes directly in HTML \u2014 the <em>utility-first</em> approach.</p>");

        mcQuestion("tw-a1", QuestionTier.RECALL,
            "<p>Which Tailwind class adds a white background?</p>",
            new String[]{"bg-white", "background-white", "color-white", "white-bg"},
            "bg-white",
            "<p>Background colour utilities follow <code>bg-{color}</code>.</p>");

        tfQuestion("tw-a1", QuestionTier.RECALL,
            "<p>In Tailwind you write a custom class name for every component.</p>",
            "False",
            "<p>Utility-first means composing pre-built classes directly in HTML. Custom class names are rare.</p>");

        mcQuestion("tw-a1", QuestionTier.APPLICATION,
            "<p>Which set applies rounded corners, a white background, and padding?</p>",
            new String[]{"rounded bg-white p-4", ".rounded .bg-white .p-4", "border-radius background padding", "style=\"border-radius: 4px\""},
            "rounded bg-white p-4",
            "<p>Tailwind classes are space-separated in <code>class</code>. No leading dots, no raw CSS.</p>");
    }

    // ── TW-A2: Installation & Setup ──────────────────────────────────────────

    private void seedTwA2() {
        subChunk(
            "tw-a2", "tw-a", "Installation & Setup", 2, 50, "index.html",

            "<p>Tailwind emits CSS only for classes you actually use \u2014 but it can only do that if the build tool "
            + "is configured correctly. Understanding the setup stops \u201Cthe styles aren\u2019t applying!\u201D "
            + "before it ever starts.</p>",

            "<p>Lyra leads you through a side door into the Academy\u2019s <em>Foundry</em>. An older mage in workshop apron greets you. "
            + "<em>\u201CMaester Cordell,\u201D</em> Lyra says. <em>\u201CHe handles setup. Spells don\u2019t reach the page without him.\u201D</em></p>"
            + "<p>Cordell opens a toolbox. <em>\u201CTailwind has three common install paths. Learn all three \u2014 you\u2019ll meet each in the wild.\u201D</em></p>"
            + "<h3>1. Tailwind CLI</h3>"
            + "<p>The simplest \u2014 no build pipeline beyond Tailwind itself:</p>"
            + "<pre><code>npm install -D tailwindcss\n"
            + "npx tailwindcss init\n"
            + "npx tailwindcss -i ./src/input.css -o ./dist/output.css --watch</code></pre>"
            + "<h3>2. PostCSS</h3>"
            + "<p>Tailwind as a PostCSS plugin, alongside autoprefixer. Standard for hand-rolled build setups:</p>"
            + "<pre><code>npm install -D tailwindcss postcss autoprefixer\n"
            + "// postcss.config.js\n"
            + "module.exports = { plugins: { tailwindcss: {}, autoprefixer: {} } };</code></pre>"
            + "<h3>3. Framework-Specific (Vite / Next.js / Angular / Vue)</h3>"
            + "<p>Most real projects live here. For Vite + React:</p>"
            + "<pre><code>npm install -D tailwindcss postcss autoprefixer\n"
            + "npx tailwindcss init -p</code></pre>"
            + "<p>Generates <code>tailwind.config.js</code> and <code>postcss.config.js</code>. Import Tailwind\u2019s layers in your main CSS file:</p>"
            + "<pre><code>/* src/index.css */\n"
            + "@tailwind base;\n"
            + "@tailwind components;\n"
            + "@tailwind utilities;</code></pre>"
            + "<h3>The content Array</h3>"
            + "<p>Cordell taps the config file. <em>\u201CThis is the one line every apprentice forgets. Tailwind scans these files for class names. If a path is missing here, those classes silently produce nothing.\u201D</em></p>"
            + "<pre><code>// tailwind.config.js\n"
            + "content: [\n"
            + "  './index.html',\n"
            + "  './src/**/*.{ts,tsx,jsx,vue}',\n"
            + "],</code></pre>"
            + "<p><em>\u201CMissing styles in production? Check this array first. It is the answer nine times out of ten.\u201D</em></p>",

            story(
                n("Lyra pushes open a heavy door. The smell of hot metal greets you \u2014 the <em>Foundry</em>, "
                  + "where raw incantations are forged into things the page can actually run."),
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "maester", "Maester Cordell", "mentor",
                  "Every spell in the Academy passes through here. If your glyphs don\u2019t appear on the page, "
                  + "the fault is almost never the glyphs. It\u2019s the setup \u2014 the <em>content</em> array, a missing import, a wrong path."),
                e("Tailwind imports in your main CSS",
                  "@tailwind base;\n@tailwind components;\n@tailwind utilities;"),
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "maester", "Maester Cordell", "mentor",
                  "Remember \u2014 the <code>content</code> array tells the scanner where to look. Miss a path, miss the styles.")
            ),

            "<p>Assume the project is set up correctly \u2014 Tailwind is installed, imports are in place, and the "
            + "<code>content</code> array covers this file. Style the <code>.panel</code> with: "
            + "<code>bg-slate-50</code>, <code>p-4</code>, <code>rounded-md</code>, <code>border</code>, "
            + "<code>border-slate-200</code>.</p>",

            "<div class=\"panel\">\n"
            + "  <h3>Setup Complete</h3>\n"
            + "  <p>Tailwind is watching your files.</p>\n"
            + "</div>",

            tests(
                twTest("Background applied", ".panel", "bg-slate-50"),
                twTest("Padding applied", ".panel", "p-4"),
                twTest("Rounded", ".panel", "rounded-md"),
                twTest("Border", ".panel", "border"),
                twTest("Border colour", ".panel", "border-slate-200")
            ),

            "You\u2019ve deployed a site and three specific classes aren\u2019t applying in production, though they work locally. Name the single most likely cause and the exact file you\u2019d check.",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-a2", QuestionTier.RECALL,
            "<p>Which three directives belong in your main CSS file when using Tailwind?</p>",
            new String[]{"@tailwind base; @tailwind components; @tailwind utilities;",
                         "@import tailwind;",
                         "@use tailwind/core;",
                         "require('tailwind');"},
            "@tailwind base; @tailwind components; @tailwind utilities;",
            "<p>These three layer directives load Tailwind\u2019s resets, component layer, and utility classes.</p>");

        mcQuestion("tw-a2", QuestionTier.APPLICATION,
            "<p>What is the <code>content</code> array in <code>tailwind.config.js</code> for?</p>",
            new String[]{"Tells Tailwind which files to scan for class names",
                         "Lists fonts to load",
                         "Sets the default theme values",
                         "Registers PostCSS plugins"},
            "Tells Tailwind which files to scan for class names",
            "<p>Tailwind only emits CSS for classes it sees \u2014 the scanner uses <code>content</code> to know where to look.</p>");

        tfQuestion("tw-a2", QuestionTier.APPLICATION,
            "<p>If a class works in development but is missing in production, the <code>content</code> globs are the first place to check.</p>",
            "True",
            "<p>Missing production styles almost always trace back to a file that the <code>content</code> globs don\u2019t cover.</p>");

        scenarioQuestion("tw-a2",
            "<p>A new team-mate says \u201Cnone of my Tailwind classes are working.\u201D Their file is under <code>./app/components/</code> but the config\u2019s content array only lists <code>./src/**/*</code>. What\u2019s the fix?</p>",
            "Add './app/**/*.{ts,tsx}' (or similar) to the content array so Tailwind scans the new directory.",
            "<p>The JIT scanner only emits CSS for files it scans. Extend the <code>content</code> array to cover the new directory.</p>");
    }

    // ── TW-A3: Spacing Utilities ─────────────────────────────────────────────

    private void seedTwA3() {
        subChunk(
            "tw-a3", "tw-a", "Spacing Utilities", 3, 50, "index.html",

            "<p>Space guides the eye. Padding sits <em>inside</em> an element; margin sits <em>outside</em>. "
            + "Tailwind gives both a consistent numeric scale so every gap on your page feels intentional.</p>",

            "<p>Lyra rolls out a long scroll and weights it with two brass rings. <em>\u201CSpace comes in two kinds \u2014 inside a thing, and between things. Scribes call them <strong>padding</strong> and <strong>margin</strong>.\u201D</em></p>"
            + "<p>She presses a palm flat to the parchment. <em>\u201CPadding pushes space <em>inside</em> an element. Use <code>p-</code> glyphs.\u201D</em></p>"
            + "<pre><code>p-4      \u2192 padding: 1rem (all sides)\n"
            + "px-4     \u2192 left + right\n"
            + "py-4     \u2192 top + bottom\n"
            + "pt-4, pb-4, pl-4, pr-4 \u2192 one side each</code></pre>"
            + "<p>She draws a second card and wedges a gap between them. <em>\u201CMargin is the space <em>outside</em> \u2014 between your element and its neighbours. Same shape, <code>m-</code> prefix.\u201D</em></p>"
            + "<pre><code>m-4      \u2192 margin: 1rem (all sides)\n"
            + "mx-4, my-4, mt-4, mb-4, ml-4, mr-4\n"
            + "mx-auto  \u2192 horizontally centre the element</code></pre>"
            + "<p><em>\u201C<code>mx-auto</code> is the scribe\u2019s secret for centring \u2014 give a max width, then auto-margins on either side.\u201D</em></p>"
            + "<h3>The Scale</h3>"
            + "<p><em>\u201CNever invent a number. The scale is fixed: each step is 4 pixels, and the whole Academy obeys it.\u201D</em></p>"
            + "<pre><code>0  \u2192 0px\n"
            + "1  \u2192 4px\n"
            + "2  \u2192 8px\n"
            + "4  \u2192 16px\n"
            + "8  \u2192 32px\n"
            + "16 \u2192 64px</code></pre>"
            + "<h3>Gap (between children)</h3>"
            + "<p>For flex and grid containers, <code>space-y-4</code> or <code>gap-4</code> sets even spacing between children without per-item margins.</p>"
            + "<pre><code>&lt;div class=\"flex flex-col space-y-4\"&gt;...&lt;/div&gt;\n"
            + "&lt;div class=\"grid grid-cols-3 gap-4\"&gt;...&lt;/div&gt;</code></pre>",

            story(
                n("Lyra rolls out a long scroll and carefully weights its corners. Runes must breathe, she explains, or the spell will suffocate."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Padding pushes space inward. Margin pushes space outward. When every scribe picks from the same scale, every page in the library feels like it belongs to the same library. That is the trick of a design system."),
                e("Centred card with breathing room",
                  "&lt;div class=\"mx-auto max-w-lg p-6 mt-8\"&gt;\n  A card with air.\n&lt;/div&gt;")
            ),

            "<p>Arrange a centred, breathing card. Apply:</p>"
            + "<ul>"
            + "<li><code>.wrapper</code> \u2192 <code>py-8</code></li>"
            + "<li><code>.card</code> \u2192 <code>mx-auto</code>, <code>max-w-md</code>, <code>p-6</code></li>"
            + "</ul>",

            "<div class=\"wrapper\">\n"
            + "  <div class=\"card bg-white rounded-lg\">\n"
            + "    <h2>The Ancient Tome</h2>\n"
            + "    <p>Wisdom, centred and breathing.</p>\n"
            + "  </div>\n"
            + "</div>",

            tests(
                twTest("Wrapper vertical padding", ".wrapper", "py-8"),
                twTest("Card horizontally centred", ".card", "mx-auto"),
                twTest("Card max width", ".card", "max-w-md"),
                twTest("Card padding", ".card", "p-6")
            ),

            "Explain the difference between padding and margin in your own words. How does the numeric scale (4, 8, 16) relate to actual pixels?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-a3", QuestionTier.RECALL,
            "<p>Which class adds padding on all four sides?</p>",
            new String[]{"p-4", "pad-4", "padding-4", "space-4"},
            "p-4",
            "<p><code>p-4</code> sets <code>padding: 1rem</code> on every side.</p>");

        mcQuestion("tw-a3", QuestionTier.RECALL,
            "<p>Which class applies margin only on the left?</p>",
            new String[]{"ml-4", "margin-left-4", "m-left-4", "lm-4"},
            "ml-4",
            "<p>Directional margins: l=left, r=right, t=top, b=bottom, x=horizontal, y=vertical.</p>");

        tfQuestion("tw-a3", QuestionTier.RECALL,
            "<p><code>mx-auto</code> centres an element horizontally (given a defined width).</p>",
            "True",
            "<p><code>mx-auto</code> sets auto margins on left and right, centring block elements with a known width.</p>");

        mcQuestion("tw-a3", QuestionTier.APPLICATION,
            "<p>How many pixels does <code>p-8</code> produce?</p>",
            new String[]{"32px", "8px", "16px", "64px"},
            "32px",
            "<p>Each unit is 4px. <code>p-8</code> = 8 \u00D7 4px = 32px.</p>");
    }

    // ── TW-A4: Typography & Colours ──────────────────────────────────────────

    private void seedTwA4() {
        subChunk(
            "tw-a4", "tw-a", "Typography & Colours", 4, 50, "index.html",

            "<p>Typography and colour are the voice of your interface. Size, weight, and shade communicate hierarchy before a single word is read. Tailwind gives you a tight scale for all three.</p>",

            "<p>A second scribe enters, sleeves ink-stained. <em>\u201CSable handles colour and scale here,\u201D</em> Lyra says. <em>\u201CListen well.\u201D</em></p>"
            + "<p>Sable lays out a ruler marked with named steps. <em>\u201CFirst rule: text size is a ladder, and every rung has a name.\u201D</em></p>"
            + "<pre><code>text-xs    \u2192 12px\n"
            + "text-sm    \u2192 14px\n"
            + "text-base  \u2192 16px  (default)\n"
            + "text-lg    \u2192 18px\n"
            + "text-xl    \u2192 20px\n"
            + "text-2xl   \u2192 24px\n"
            + "text-4xl   \u2192 36px</code></pre>"
            + "<p><em>\u201CNever reach for an arbitrary number. Climb the ladder.\u201D</em></p>"
            + "<h3>Weight</h3>"
            + "<pre><code>font-light    \u2192 300\n"
            + "font-normal   \u2192 400\n"
            + "font-medium   \u2192 500\n"
            + "font-semibold \u2192 600\n"
            + "font-bold     \u2192 700</code></pre>"
            + "<h3>Colour</h3>"
            + "<p>Sable opens a row of ink pots from palest to darkest. <em>\u201CEvery hue has shades from 50 to 900. Fifty is barely a whisper; nine hundred is nearly black.\u201D</em></p>"
            + "<pre><code>text-gray-500    \u2192 medium grey\n"
            + "text-blue-600    \u2192 medium-dark blue\n"
            + "text-red-500     \u2192 medium red\n"
            + "text-green-700   \u2192 dark green\n"
            + "text-white / text-black</code></pre>"
            + "<p>Background colours follow the same pattern with <code>bg-</code>:</p>"
            + "<pre><code>bg-blue-500    \u2192 background blue-500\n"
            + "bg-gray-100    \u2192 light grey surface</code></pre>"
            + "<h3>Putting It Together</h3>"
            + "<pre><code>&lt;h1 class=\"text-4xl font-bold text-gray-900\"&gt;Welcome&lt;/h1&gt;\n"
            + "&lt;p class=\"text-base text-gray-600\"&gt;Subtitle here&lt;/p&gt;</code></pre>"
            + "<p><em>\u201CLarge, bold, dark \u2014 the eye goes there first. Smaller, softer \u2014 second. That is hierarchy, for free.\u201D</em></p>",

            story(
                n("Lyra leads you through a silk curtain into the <em>Calligraphy Chamber</em>. Shelves of ink pots line the walls."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Size, weight, and colour tell the reader what matters. I\u2019ll leave you with Sable \u2014 they know this chamber best."),
                d("\uD83E\uDDD1\u200D\uD83C\uDFA8", "illuminator", "Sable the Illuminator", "mentor",
                  "Two ladders and one palette. That\u2019s all typography is.")
            ),

            "<p>Style the heading and subtitle:</p>"
            + "<ul>"
            + "<li><code>.heading</code> \u2192 <code>text-4xl</code>, <code>font-bold</code>, <code>text-gray-900</code></li>"
            + "<li><code>.subtitle</code> \u2192 <code>text-base</code>, <code>text-gray-500</code></li>"
            + "</ul>",

            "<div class=\"p-8\">\n"
            + "  <h1 class=\"heading\">Arcane Academy</h1>\n"
            + "  <p class=\"subtitle\">Master the utility-first art</p>\n"
            + "</div>",

            tests(
                twTest("Heading large", ".heading", "text-4xl"),
                twTest("Heading bold", ".heading", "font-bold"),
                twTest("Heading dark grey", ".heading", "text-gray-900"),
                twTest("Subtitle base size", ".subtitle", "text-base"),
                twTest("Subtitle muted grey", ".subtitle", "text-gray-500")
            ),

            "Explain how Tailwind\u2019s size ladder and colour scale make a design system possible. What goes wrong when developers pick arbitrary sizes and colours?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-a4", QuestionTier.RECALL,
            "<p>Which class makes text bold?</p>",
            new String[]{"font-bold", "text-bold", "bold", "weight-bold"},
            "font-bold",
            "<p>Weight uses <code>font-{weight}</code>.</p>");

        mcQuestion("tw-a4", QuestionTier.RECALL,
            "<p>Which class is 18px text?</p>",
            new String[]{"text-lg", "font-large", "size-lg", "text-large"},
            "text-lg",
            "<p>Tailwind\u2019s size scale: xs, sm, base, lg, xl, 2xl, etc.</p>");

        mcQuestion("tw-a4", QuestionTier.RECALL,
            "<p>Which shade is darkest in Tailwind\u2019s palette?</p>",
            new String[]{"900", "500", "100", "800"},
            "900",
            "<p>Shades run 50 (lightest) to 900 (darkest).</p>");

        codeQuestion("tw-a4", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
            "<p>What colour does this element use for its text?</p>",
            "<p class=\"text-blue-600 font-semibold text-sm\">Hello</p>",
            "Medium blue",
            "<p><code>text-blue-600</code> is medium-dark blue. Shade 600 is deeper than the midpoint 500.</p>");
    }

    // ── TW-A5: Borders, Radius & Basic Layout ────────────────────────────────

    private void seedTwA5() {
        subChunk(
            "tw-a5", "tw-a", "Borders, Radius & Basic Layout", 5, 60, "index.html",

            "<p>Borders, corners, shadows, and a handful of layout glyphs \u2014 that\u2019s all you need to turn raw content into recognisable UI. Flexbox and grid mastery come later; in the basics you just need the words to say <em>\u201Cthis is a row,\u201D</em> <em>\u201Cfill the screen,\u201D</em> <em>\u201Ccentre the children.\u201D</em></p>",

            "<p>From the back of the workshop, Master Ember approaches \u2014 leather apron, chisel at their belt. They set three tools on the bench. <em>\u201CEdge. Corner. Shadow. That is what gives a shape physicality.\u201D</em></p>"
            + "<h3>Borders</h3>"
            + "<pre><code>border          \u2192 1px solid\n"
            + "border-2        \u2192 2px\n"
            + "border-t        \u2192 top edge only\n"
            + "border-gray-300 \u2192 colour the border\n"
            + "border-dashed   \u2192 dashed style</code></pre>"
            + "<h3>Rounded Corners</h3>"
            + "<pre><code>rounded-none / rounded-sm / rounded / rounded-md / rounded-lg /\n"
            + "rounded-xl / rounded-2xl / rounded-full  (fully circular)</code></pre>"
            + "<h3>Shadows</h3>"
            + "<pre><code>shadow-sm / shadow / shadow-md / shadow-lg / shadow-xl /\n"
            + "shadow-2xl / shadow-none</code></pre>"
            + "<p><em>\u201CMatch the shadow to how far above the page the element should feel. A light card wants <code>shadow-sm</code>. A modal that has leapt forward wants <code>shadow-2xl</code>.\u201D</em></p>"
            + "<h3>Basic Layout \u2014 A First Taste</h3>"
            + "<p>Ember nods toward Lyra, who steps back in. <em>\u201CYou\u2019ll go deep on layout in the next chapter. For now, four glyphs you\u2019ll use every day:\u201D</em></p>"
            + "<pre><code>flex              \u2192 display: flex  (arrange children in a row)\n"
            + "items-center      \u2192 vertically centre flex children\n"
            + "justify-center    \u2192 horizontally centre flex children\n"
            + "w-full / h-screen \u2192 full width / full viewport height</code></pre>"
            + "<pre><code>&lt;div class=\"flex items-center justify-center h-screen\"&gt;\n"
            + "  &lt;div class=\"border rounded-xl shadow-md p-6\"&gt;I\u2019m a card.&lt;/div&gt;\n"
            + "&lt;/div&gt;</code></pre>"
            + "<p><em>\u201CA card, centred on the page, with a real edge and a real weight. That is the whole vocabulary of \u2018looks like software.\u2019\u201D</em></p>",

            story(
                n("An older mage emerges from the back of the Scriptorium, leather apron dusted with stone. Tools hang from their belt."),
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "carver", "Master Ember", "mentor",
                  "A shape needs three enchantments to feel real: a <strong>border</strong>, a <strong>corner</strong>, and a <strong>shadow</strong>."),
                e("A real object, centred on the page",
                  "&lt;div class=\"flex items-center justify-center h-screen\"&gt;\n  &lt;div class=\"border rounded-xl shadow-md p-6\"&gt;Card&lt;/div&gt;\n&lt;/div&gt;"),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Four layout glyphs for now \u2014 <code>flex</code>, <code>items-center</code>, <code>justify-center</code>, <code>h-screen</code>. You\u2019ll meet the rest in the next chapter.")
            ),

            "<p>Build a centred, bordered card:</p>"
            + "<ul>"
            + "<li><code>.stage</code> \u2192 <code>flex</code>, <code>items-center</code>, <code>justify-center</code>, <code>h-screen</code></li>"
            + "<li><code>.card</code> \u2192 <code>border</code>, <code>border-gray-200</code>, <code>rounded-xl</code>, <code>shadow-md</code>, <code>p-6</code></li>"
            + "</ul>",

            "<div class=\"stage\">\n"
            + "  <div class=\"card bg-white\">\n"
            + "    <h3>Avatar</h3>\n"
            + "    <p>Bordered, rounded, shadowed, centred.</p>\n"
            + "  </div>\n"
            + "</div>",

            tests(
                twTest("Stage uses flex", ".stage", "flex"),
                twTest("Stage centres vertically", ".stage", "items-center"),
                twTest("Stage centres horizontally", ".stage", "justify-center"),
                twTest("Stage fills viewport", ".stage", "h-screen"),
                twTest("Card has border", ".card", "border"),
                twTest("Card border colour", ".card", "border-gray-200"),
                twTest("Card rounded", ".card", "rounded-xl"),
                twTest("Card shadow", ".card", "shadow-md"),
                twTest("Card padding", ".card", "p-6")
            ),

            "Explain what each of these glyph families does in one sentence: <code>border-*</code>, <code>rounded-*</code>, <code>shadow-*</code>, <code>flex</code>. Why are these the minimum vocabulary for \u2018looks like a real interface\u2019?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-a5", QuestionTier.RECALL,
            "<p>Which class produces a fully circular element?</p>",
            new String[]{"rounded-full", "rounded-circle", "rounded-xl", "border-full"},
            "rounded-full",
            "<p><code>rounded-full</code> gives a perfect circle on square elements.</p>");

        mcQuestion("tw-a5", QuestionTier.RECALL,
            "<p>Which class removes a drop shadow?</p>",
            new String[]{"shadow-none", "no-shadow", "shadow-0", "shadow-off"},
            "shadow-none",
            "<p><code>shadow-none</code> sets <code>box-shadow: none</code>.</p>");

        mcQuestion("tw-a5", QuestionTier.APPLICATION,
            "<p>Which set centres a child both vertically and horizontally in a full-viewport container?</p>",
            new String[]{"flex items-center justify-center h-screen",
                         "grid place-content-start h-full",
                         "block text-center mt-auto",
                         "inline-flex"},
            "flex items-center justify-center h-screen",
            "<p>Flex with <code>items-center</code> (cross-axis) and <code>justify-center</code> (main-axis), plus <code>h-screen</code> for viewport height.</p>");

        tfQuestion("tw-a5", QuestionTier.APPLICATION,
            "<p><code>w-full</code> makes an element fill its parent\u2019s width.</p>",
            "True",
            "<p><code>w-full</code> is <code>width: 100%</code>.</p>");
    }
}
