package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

/**
 * Tailwind CSS — Chunk TW-B: Layout Weaver (Practitioner tier)
 *
 * Composition, real-world layouts, responsiveness, dark mode, forms,
 * animation, component extraction.
 *
 * Prereq: TW-A Foundation.
 */
@Component
public class TailwindPractitionerSeeder extends AbstractChunkSeeder {

    public TailwindPractitionerSeeder(ChunkRepository chunkRepository,
                                       SubChunkRepository subChunkRepository,
                                       QuestionRepository questionRepository,
                                       RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {

        // ── Chunk TW-B: Layout Weaver ─────────────────────────────────────────
        Chunk twb = Chunk.builder()
                .id("tw-b")
                .title("Layout Weaver")
                .glyph("🧵")
                .sortOrder(2)
                .prerequisiteIds("[\"tw-a\"]")
                .tier(LearnerPath.PRACTITIONER)
                .topicId("tailwind")
                .build();
        chunkRepository.save(twb);

        seedTwB1();
        seedTwB2();
        seedTwB3();
        seedTwB4();
        seedTwB5();
        seedTwB6();
    }

    // Tailwind test spec — same format as TailwindSeeder
    private String twTest(String label, String selector, String requiredClass) {
        return "{\"label\":\"" + esc(label)
                + "\",\"selector\":\"" + esc(selector)
                + "\",\"requiredClass\":\"" + esc(requiredClass) + "\"}";
    }

    // ── TW-B1: Flex & Grid — Composing Layouts ───────────────────────────────

    private void seedTwB1() {
        subChunk(
            "tw-b1", "tw-b", "Flex & Grid Layouts", 1, 75, "index.html",

            // Hook
            "<p>Utility classes alone don't make a page — <em>arrangement</em> does. "
            + "Flexbox is for one-dimensional rows or columns. Grid is for two-dimensional plans. "
            + "Tailwind gives both a precise utility vocabulary.</p>",

            // Explanation
            "<h3>Flexbox</h3>"
            + "<pre><code>flex              → display: flex\n"
            + "flex-col          → column direction\n"
            + "items-center      → align-items: center (cross axis)\n"
            + "justify-between   → justify-content: space-between (main axis)\n"
            + "gap-4             → 16px gap between items\n"
            + "flex-1            → child grows to fill</code></pre>"
            + "<h3>Grid</h3>"
            + "<pre><code>grid              → display: grid\n"
            + "grid-cols-3       → 3 equal columns\n"
            + "grid-cols-[200px_1fr] → sidebar + content\n"
            + "col-span-2        → child spans 2 columns\n"
            + "gap-6             → 24px gap between cells</code></pre>"
            + "<h3>When Flex vs Grid?</h3>"
            + "<ul>"
            + "<li>Row of nav items, aligned buttons → <strong>flex</strong></li>"
            + "<li>Card gallery, dashboard, form layout → <strong>grid</strong></li>"
            + "<li>Both have <code>gap</code> — use it instead of margin tricks.</li>"
            + "</ul>",

            // Story
            story(
                n("Lyra unrolls a blueprint — a storefront with a header, a sidebar, a main stage, and a footer."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Grid plans the rooms. Flex arranges what stands inside them. Choose the right tool for the axis of your problem."),
                e("A three-card row",
                  "&lt;div class=\"grid grid-cols-3 gap-4\"&gt;\n  &lt;div&gt;Card&lt;/div&gt;&lt;div&gt;Card&lt;/div&gt;&lt;div&gt;Card&lt;/div&gt;\n&lt;/div&gt;"),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "<code>gap</code> replaces margin gymnastics. Use it.")
            ),

            // Practice brief
            "<p>Build a three-card gallery:</p>"
            + "<ul>"
            + "<li>The <code>.gallery</code> element should have: <code>grid</code>, <code>grid-cols-3</code>, <code>gap-4</code>.</li>"
            + "</ul>",

            // Starter
            "<div class=\"gallery\">\n"
            + "  <div class=\"bg-white p-4 rounded shadow\">Card 1</div>\n"
            + "  <div class=\"bg-white p-4 rounded shadow\">Card 2</div>\n"
            + "  <div class=\"bg-white p-4 rounded shadow\">Card 3</div>\n"
            + "</div>",

            // Tests
            tests(
                twTest("Gallery uses grid display", ".gallery", "grid"),
                twTest("Gallery has three columns", ".gallery", "grid-cols-3"),
                twTest("Gallery has 16px gap", ".gallery", "gap-4")
            ),

            "When would you reach for flex instead of grid, and vice versa? Give a real UI example for each.",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b1", QuestionTier.RECALL,
            "<p>Which Tailwind class makes three equal columns?</p>",
            new String[]{"grid-cols-3", "cols-3", "grid-3", "columns-3"},
            "grid-cols-3",
            "<p><code>grid-cols-3</code> creates three equal-fraction (1fr each) columns.</p>");

        mcQuestion("tw-b1", QuestionTier.APPLICATION,
            "<p>You want a nav bar with the logo pinned left and links pinned right. Which combination?</p>",
            new String[]{"flex justify-between items-center", "grid grid-cols-2", "flex flex-col", "inline-block"},
            "flex justify-between items-center",
            "<p>Flex with <code>justify-between</code> pushes first and last children to opposite ends; <code>items-center</code> vertically aligns them.</p>");

        tfQuestion("tw-b1", QuestionTier.RECALL,
            "<p>The <code>gap</code> utility works on both flex and grid containers.</p>",
            "True",
            "<p>Modern CSS supports <code>gap</code> on flex as well as grid, and Tailwind's <code>gap-*</code> utilities reflect that.</p>");
    }

    // ── TW-B2: Responsive Design ─────────────────────────────────────────────

    private void seedTwB2() {
        subChunk(
            "tw-b2", "tw-b", "Responsive Design", 2, 75, "index.html",

            // Hook
            "<p>A design that only works on your laptop is unfinished. Tailwind's breakpoint prefixes let you "
            + "apply utilities <em>only</em> at certain widths — mobile-first by default.</p>",

            // Explanation
            "<h3>Breakpoints</h3>"
            + "<pre><code>sm:  ≥ 640px   — large phone\n"
            + "md:  ≥ 768px   — tablet\n"
            + "lg:  ≥ 1024px  — laptop\n"
            + "xl:  ≥ 1280px  — desktop\n"
            + "2xl: ≥ 1536px  — large desktop</code></pre>"
            + "<h3>Mobile-First Rule</h3>"
            + "<p>Unprefixed utilities apply at every size. Breakpoint prefixes apply at that size <em>and above</em>. "
            + "Design for small screens first, then layer larger-screen adjustments:</p>"
            + "<pre><code>&lt;div class=\"grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4\"&gt;</code></pre>"
            + "<p>One column on phone, two on tablet, three on laptop+. No media queries written by hand.</p>"
            + "<h3>Hiding and Showing</h3>"
            + "<pre><code>hidden md:block   → hidden on phone, visible on tablet+\n"
            + "md:hidden         → shown on phone, hidden on tablet+</code></pre>",

            // Story
            story(
                n("Lyra holds a parchment up to the light. It changes size as she tilts it."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "A spell must work in every hand it's cast from — the phone in a pocket, the tablet on a lap, the scroll on a lectern."),
                e("Responsive columns",
                  "&lt;div class=\"grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4\"&gt;"),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Always start mobile-first. Unprefixed = smallest. Prefixes = larger and up.")
            ),

            // Practice brief
            "<p>Make the gallery responsive. On the <code>.gallery</code> element set:</p>"
            + "<ul>"
            + "<li><code>grid</code></li>"
            + "<li><code>grid-cols-1</code> — one column by default (mobile)</li>"
            + "<li><code>md:grid-cols-2</code> — two columns on tablet</li>"
            + "<li><code>lg:grid-cols-3</code> — three columns on laptop</li>"
            + "<li><code>gap-4</code></li>"
            + "</ul>",

            // Starter
            "<div class=\"gallery\">\n"
            + "  <div class=\"bg-white p-4 rounded\">A</div>\n"
            + "  <div class=\"bg-white p-4 rounded\">B</div>\n"
            + "  <div class=\"bg-white p-4 rounded\">C</div>\n"
            + "</div>",

            // Tests
            tests(
                twTest("Grid display", ".gallery", "grid"),
                twTest("Mobile: one column", ".gallery", "grid-cols-1"),
                twTest("Tablet: two columns", ".gallery", "md:grid-cols-2"),
                twTest("Laptop: three columns", ".gallery", "lg:grid-cols-3"),
                twTest("Gap", ".gallery", "gap-4")
            ),

            "Explain mobile-first in your own words. Why are unprefixed utilities the smallest-screen styles rather than the largest?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b2", QuestionTier.RECALL,
            "<p>At what minimum viewport width does <code>md:</code> activate?</p>",
            new String[]{"768px", "640px", "1024px", "480px"},
            "768px",
            "<p>Tailwind's default <code>md</code> breakpoint is 768px and up.</p>");

        mcQuestion("tw-b2", QuestionTier.APPLICATION,
            "<p>What does <code>hidden md:flex</code> mean?</p>",
            new String[]{"Hidden on phones, flex on tablets and up", "Always hidden", "Flex on phones, hidden on tablets", "Broken — cannot combine"},
            "Hidden on phones, flex on tablets and up",
            "<p><code>hidden</code> applies by default; <code>md:flex</code> overrides at ≥768px, flipping display from none to flex.</p>");

        tfQuestion("tw-b2", QuestionTier.RECALL,
            "<p>Tailwind breakpoints are <em>min-width</em> based.</p>",
            "True",
            "<p>Prefixes activate when the viewport is at least that wide. This is what enables the mobile-first pattern.</p>");
    }

    // ── TW-B3: Dark Mode ─────────────────────────────────────────────────────

    private void seedTwB3() {
        subChunk(
            "tw-b3", "tw-b", "Dark Mode", 3, 70, "index.html",

            // Hook
            "<p>A dark theme is no longer a novelty — it's an expectation. Tailwind's <code>dark:</code> "
            + "variant lets you layer a dark palette on top of your light one with almost no duplication.</p>",

            // Explanation
            "<h3>Two Strategies</h3>"
            + "<p>In <code>tailwind.config.js</code>:</p>"
            + "<pre><code>darkMode: 'media'  → follows OS preference\n"
            + "darkMode: 'class'  → add class=\"dark\" to &lt;html&gt; to toggle</code></pre>"
            + "<p><strong>Class strategy</strong> is preferred in production — it lets users override the OS choice.</p>"
            + "<h3>Applying Dark Styles</h3>"
            + "<pre><code>&lt;div class=\"bg-white dark:bg-slate-900 text-gray-900 dark:text-gray-100\"&gt;</code></pre>"
            + "<p>The unprefixed class is the light-theme value; <code>dark:</code> overrides in dark mode.</p>"
            + "<h3>Token-Driven Theming</h3>"
            + "<p>For larger apps, define CSS variables and reference them with <code>bg-[var(--surface)]</code> — "
            + "but in Foundation/Practitioner work, the <code>dark:</code> variant is usually enough.</p>",

            // Story
            story(
                n("The Scriptorium dims as Lyra lowers the candles. The parchments shift their glow to match."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Magic that works by day must also work by night. The <code>dark:</code> prefix lets us write both in one breath."),
                e("A card that adapts",
                  "&lt;div class=\"bg-white dark:bg-slate-900 text-gray-900 dark:text-gray-100 p-6 rounded-xl\"&gt;\n  Arcane Academy\n&lt;/div&gt;")
            ),

            // Practice brief
            "<p>Make the <code>.card</code> adapt to dark mode. Apply: "
            + "<code>bg-white</code>, <code>dark:bg-slate-900</code>, <code>text-gray-900</code>, <code>dark:text-gray-100</code>.</p>",

            // Starter
            "<div class=\"card p-6 rounded-xl shadow\">\n"
            + "  <h3>Arcane Academy</h3>\n"
            + "  <p>A theme for every hour.</p>\n"
            + "</div>",

            // Tests
            tests(
                twTest("Light background", ".card", "bg-white"),
                twTest("Dark background override", ".card", "dark:bg-slate-900"),
                twTest("Light text colour", ".card", "text-gray-900"),
                twTest("Dark text override", ".card", "dark:text-gray-100")
            ),

            "Explain the trade-off between <code>darkMode: 'media'</code> and <code>darkMode: 'class'</code>. Which would you choose for a product, and why?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b3", QuestionTier.RECALL,
            "<p>Which Tailwind prefix applies a style only in dark mode?</p>",
            new String[]{"dark:", "night:", "theme-dark:", "@dark"},
            "dark:",
            "<p>The <code>dark:</code> variant is Tailwind's dark-mode override.</p>");

        mcQuestion("tw-b3", QuestionTier.APPLICATION,
            "<p>Which config lets users toggle dark mode independently of their OS?</p>",
            new String[]{"darkMode: 'class'", "darkMode: 'media'", "darkMode: 'toggle'", "darkMode: true"},
            "darkMode: 'class'",
            "<p>The <code>'class'</code> strategy keys dark mode to a <code>.dark</code> class on the root element, which you can toggle from JS.</p>");

        tfQuestion("tw-b3", QuestionTier.APPLICATION,
            "<p>You must duplicate every single class with a <code>dark:</code> prefix to support dark mode.</p>",
            "False",
            "<p>Only colour-related and surface classes typically need dark overrides. Spacing, layout, and typography usually stay identical.</p>");
    }

    // ── TW-B4: Forms ─────────────────────────────────────────────────────────

    private void seedTwB4() {
        subChunk(
            "tw-b4", "tw-b", "Forms & Validation States", 4, 70, "index.html",

            // Hook
            "<p>Forms are where users give you their time. An input with no focus ring, no error styling, "
            + "no disabled treatment is a form that fights its user. Tailwind's state variants cover all three.</p>",

            // Explanation
            "<h3>A Solid Base Input</h3>"
            + "<pre><code>&lt;input class=\"\n"
            + "  w-full px-3 py-2\n"
            + "  border border-gray-300 rounded-md\n"
            + "  focus:border-blue-500 focus:ring-2 focus:ring-blue-200\n"
            + "  disabled:bg-gray-100 disabled:cursor-not-allowed\"&gt;</code></pre>"
            + "<h3>Error State</h3>"
            + "<p>Flip border and ring colours when the field is invalid (usually toggled by a class from JS):</p>"
            + "<pre><code>class=\"... border-red-500 focus:ring-red-200\"</code></pre>"
            + "<h3>The @tailwindcss/forms Plugin</h3>"
            + "<p>Official plugin that resets browser defaults so inputs, selects, and checkboxes look consistent "
            + "across browsers before you style them. Add it early in a project.</p>"
            + "<h3>Labels & Accessibility</h3>"
            + "<p>Always pair inputs with a <code>&lt;label&gt;</code>. Tailwind doesn't replace HTML semantics.</p>",

            // Story
            story(
                n("Lyra sets down a quill. <em>\"A form,\"</em> she says, <em>\"is a conversation. Make it clear when the door is open, when it is closed, and when the user has stumbled.\"</em>"),
                e("A focused, safe input",
                  "&lt;input class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-200 focus:border-blue-500\" /&gt;")
            ),

            // Practice brief
            "<p>Style the <code>.field</code> input. Apply: "
            + "<code>w-full</code>, <code>border</code>, <code>border-gray-300</code>, "
            + "<code>rounded-md</code>, <code>focus:ring-2</code>, <code>focus:border-blue-500</code>.</p>",

            // Starter
            "<form class=\"max-w-sm p-6 bg-white rounded-xl\">\n"
            + "  <label for=\"email\" class=\"block text-sm mb-1\">Email</label>\n"
            + "  <input id=\"email\" class=\"field px-3 py-2\" type=\"email\" />\n"
            + "</form>",

            // Tests
            tests(
                twTest("Full-width input", ".field", "w-full"),
                twTest("Has border", ".field", "border"),
                twTest("Border colour", ".field", "border-gray-300"),
                twTest("Rounded corners", ".field", "rounded-md"),
                twTest("Focus ring", ".field", "focus:ring-2"),
                twTest("Focus border colour", ".field", "focus:border-blue-500")
            ),

            "Why is visible focus state especially critical on form inputs? What happens to a keyboard user if you remove it?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b4", QuestionTier.RECALL,
            "<p>Which combination gives an input a visible focus state?</p>",
            new String[]{"focus:ring-2 focus:border-blue-500", "hover:border-blue-500", "outline-none", "active:bg-blue-500"},
            "focus:ring-2 focus:border-blue-500",
            "<p>The <code>focus:</code> variant changes styles when the element is focused. Ring + border shift is the idiomatic pattern.</p>");

        scenarioQuestion("tw-b4",
            "<p>A developer applies <code>outline-none</code> to every input to \"clean up\" the UI but adds no replacement focus style. What's broken?</p>",
            "Keyboard users can no longer see which input is focused; accessibility is broken.",
            "<p>Removing the outline without providing a <code>focus:ring-*</code> replacement destroys keyboard accessibility. Always ensure a visible focus indicator exists.</p>");
    }

    // ── TW-B5: Transitions & Animation ───────────────────────────────────────

    private void seedTwB5() {
        subChunk(
            "tw-b5", "tw-b", "Transitions & Animation", 5, 70, "index.html",

            // Hook
            "<p>Motion is feedback. A button that fades its colour over 150ms feels premium. One that snaps is cheap. "
            + "Tailwind's transition utilities make both a one-line decision.</p>",

            // Explanation
            "<h3>Transition Basics</h3>"
            + "<pre><code>transition          → transition all properties\n"
            + "transition-colors   → only colour-related\n"
            + "transition-transform → only transforms\n"
            + "duration-150        → 150ms\n"
            + "duration-300        → 300ms (default feel)\n"
            + "ease-in-out         → smoother curve\n"
            + "delay-100           → 100ms delay</code></pre>"
            + "<h3>Transform Utilities</h3>"
            + "<pre><code>scale-95    → shrink to 95%\n"
            + "scale-105   → grow to 105%\n"
            + "rotate-3    → 3-degree rotation\n"
            + "translate-y-1 → 4px downward shift</code></pre>"
            + "<h3>Built-in Animations</h3>"
            + "<pre><code>animate-spin    → spinner\n"
            + "animate-pulse   → skeleton placeholder\n"
            + "animate-bounce  → attention nudge</code></pre>"
            + "<h3>Accessibility</h3>"
            + "<p>Respect reduced-motion users:</p>"
            + "<pre><code>motion-safe:animate-bounce\n"
            + "motion-reduce:transition-none</code></pre>",

            // Story
            story(
                n("Lyra taps a glowing rune. It brightens over a heartbeat, not a flash."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Motion is the difference between a page that feels alive and one that feels like a poster. "
                  + "But give reduced-motion users the choice — never force animation on them."),
                e("Hover lift",
                  "&lt;button class=\"transition-transform duration-150 hover:scale-105\"&gt;Hover&lt;/button&gt;")
            ),

            // Practice brief
            "<p>Make the <code>.btn</code> feel alive. Apply: "
            + "<code>transition-transform</code>, <code>duration-150</code>, <code>hover:scale-105</code>.</p>",

            // Starter
            "<button class=\"btn bg-purple-600 text-white px-6 py-3 rounded-lg\">\n"
            + "  Cast\n"
            + "</button>",

            // Tests
            tests(
                twTest("Transform transition", ".btn", "transition-transform"),
                twTest("150ms duration", ".btn", "duration-150"),
                twTest("Hover lift", ".btn", "hover:scale-105")
            ),

            "Why should animations respect <code>prefers-reduced-motion</code>? What kinds of users benefit?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b5", QuestionTier.RECALL,
            "<p>Which class adds a 300ms transition duration?</p>",
            new String[]{"duration-300", "transition-300", "ease-300", "time-300"},
            "duration-300",
            "<p>Tailwind's duration utilities use ms values: <code>duration-75</code>, <code>duration-150</code>, <code>duration-300</code>, etc.</p>");

        mcQuestion("tw-b5", QuestionTier.APPLICATION,
            "<p>You want a skeleton-loading box. Which built-in animation fits best?</p>",
            new String[]{"animate-pulse", "animate-spin", "animate-bounce", "animate-ping"},
            "animate-pulse",
            "<p><code>animate-pulse</code> is the classic skeleton-loader shimmer — subtle opacity cycling.</p>");

        tfQuestion("tw-b5", QuestionTier.APPLICATION,
            "<p><code>motion-reduce:transition-none</code> disables the transition for users who have opted into reduced motion.</p>",
            "True",
            "<p>The <code>motion-reduce:</code> variant targets <code>prefers-reduced-motion: reduce</code>.</p>");
    }

    // ── TW-B6: Component Extraction ──────────────────────────────────────────

    private void seedTwB6() {
        subChunk(
            "tw-b6", "tw-b", "Component Extraction", 6, 80, "index.html",

            // Hook
            "<p>Your button class string is 14 utilities long and repeated in 20 places. "
            + "It's tempting to <code>@apply</code> everything into a <code>.btn</code> class — "
            + "but that's usually the <em>wrong</em> answer.</p>",

            // Explanation
            "<h3>Option 1: Component Abstraction (Preferred)</h3>"
            + "<p>Extract the repetition into a <strong>framework component</strong>:</p>"
            + "<pre><code>function Button({ children }) {\n"
            + "  return &lt;button className=\"bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded\"&gt;\n"
            + "    {children}\n"
            + "  &lt;/button&gt;;\n"
            + "}</code></pre>"
            + "<p>Utilities stay in HTML; the component is the reusable unit. This is the Tailwind team's recommended path.</p>"
            + "<h3>Option 2: @apply (Use Sparingly)</h3>"
            + "<p>When you can't have components — plain HTML, email templates, third-party embed — "
            + "<code>@apply</code> bundles utilities into a class in your CSS file:</p>"
            + "<pre><code>.btn-primary {\n"
            + "  @apply bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded;\n"
            + "}</code></pre>"
            + "<h3>When NOT to @apply</h3>"
            + "<ul>"
            + "<li>If your stack has components — use components.</li>"
            + "<li>If you're tempted to <code>@apply</code> layout utilities like <code>flex</code> or <code>grid-cols-3</code> — resist. These belong on the instance.</li>"
            + "<li>If the class wraps fewer than ~4 utilities — just repeat them.</li>"
            + "</ul>"
            + "<h3>Variants with CVA</h3>"
            + "<p>For React, <code>class-variance-authority</code> (CVA) gives you clean multi-variant buttons "
            + "without writing a custom CSS class. See the Expert tier for the full pattern.</p>",

            // Story
            story(
                n("Lyra eyes a wall of duplicated button incantations across the Scriptorium."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Apprentices reach for <code>@apply</code> too fast. The deeper answer is almost always: "
                  + "make a <em>component</em>. Utilities stay on the element; the component is the reuse unit."),
                e("A button component",
                  "function Button({ children }) {\n  return &lt;button className=\"bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded\"&gt;{children}&lt;/button&gt;;\n}")
            ),

            // Practice brief
            "<p>Pretend this HTML will be rendered by a component. Apply the full utility string on the "
            + "<code>.btn</code> element: <code>bg-blue-600</code>, <code>hover:bg-blue-700</code>, "
            + "<code>text-white</code>, <code>px-4</code>, <code>py-2</code>, <code>rounded</code>.</p>",

            // Starter
            "<button class=\"btn\">Submit</button>",

            // Tests
            tests(
                twTest("Background", ".btn", "bg-blue-600"),
                twTest("Hover background", ".btn", "hover:bg-blue-700"),
                twTest("Text white", ".btn", "text-white"),
                twTest("Horizontal padding", ".btn", "px-4"),
                twTest("Vertical padding", ".btn", "py-2"),
                twTest("Rounded", ".btn", "rounded")
            ),

            "Explain when you'd reach for <code>@apply</code> vs extracting a component. Why is \"component first\" the Tailwind team's recommendation?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b6", QuestionTier.RECALL,
            "<p>Which is Tailwind's recommended reuse strategy in a React or Vue app?</p>",
            new String[]{"Create a component that wraps the utilities", "Always use @apply", "Copy-paste the class string", "Move styles to a separate CSS file"},
            "Create a component that wraps the utilities",
            "<p>Tailwind's official guidance is to extract repeated UI into components (React/Vue/Svelte), not CSS classes.</p>");

        scenarioQuestion("tw-b6",
            "<p>A team wraps a 30-utility button string into <code>.btn</code> with <code>@apply</code>, then adds 12 modifier classes (<code>.btn-sm</code>, <code>.btn-danger</code>, …). Six months later, what problem emerges?</p>",
            "They have rebuilt a traditional CSS component library on top of Tailwind — losing its composition benefits and ending up with the worst of both worlds.",
            "<p><code>@apply</code> at scale recreates the complexity Tailwind was meant to avoid. A React component with CVA (or similar) is almost always cleaner.</p>");
    }
}
