package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

/**
 * Tailwind CSS — Chunk TW-B: Advanced (Practitioner tier)
 *
 * Mirrors the "Intermediate + Advanced" tiers of the Tailwind roadmap:
 *   1. Responsive Design
 *   2. State Variants
 *   3. Flexbox & Grid
 *   4. Theme Config, Plugins & @apply
 *   5. Dark Mode & Multi-Theme
 *   6. Animations & Keyframes
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

        Chunk twb = Chunk.builder()
                .id("tw-b")
                .title("Advanced Tailwind")
                .glyph("\uD83E\uDDF5")
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

    private String twTest(String label, String selector, String requiredClass) {
        return "{\"label\":\"" + esc(label)
                + "\",\"selector\":\"" + esc(selector)
                + "\",\"requiredClass\":\"" + esc(requiredClass) + "\"}";
    }

    // ── TW-B1: Responsive Design ─────────────────────────────────────────────

    private void seedTwB1() {
        subChunk(
            "tw-b1", "tw-b", "Responsive Design", 1, 75, "index.html",

            "<p>A design that only works on your laptop is unfinished. Tailwind\u2019s breakpoint prefixes let you apply utilities <em>only</em> at certain widths \u2014 mobile-first by default.</p>",

            "<p>Lyra holds a parchment up to the light. It shifts between sizes as she tilts it. <em>\u201CA spell must work in every hand it\u2019s cast from,\u201D</em> she says. <em>\u201CPhone. Tablet. Lectern. One incantation for all three.\u201D</em></p>"
            + "<h3>Breakpoints</h3>"
            + "<pre><code>sm:  \u2265 640px   \u2014 large phone\n"
            + "md:  \u2265 768px   \u2014 tablet\n"
            + "lg:  \u2265 1024px  \u2014 laptop\n"
            + "xl:  \u2265 1280px  \u2014 desktop\n"
            + "2xl: \u2265 1536px  \u2014 large desktop</code></pre>"
            + "<h3>Mobile-First Discipline</h3>"
            + "<p><em>\u201CUnprefixed utilities are the smallest-screen styles. Breakpoint prefixes kick in at that width <strong>and above</strong>.\u201D</em> Design for small screens first, then layer on larger-screen adjustments:</p>"
            + "<pre><code>&lt;div class=\"grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4\"&gt;</code></pre>"
            + "<p>One column on phone, two on tablet, three on laptop+. No hand-written media queries.</p>"
            + "<h3>Scaling Other Properties</h3>"
            + "<pre><code>p-4 sm:p-6 md:p-10 lg:p-16   \u2192 padding grows with the screen\n"
            + "text-base md:text-lg lg:text-xl \u2192 type scale by breakpoint</code></pre>"
            + "<h3>Hiding and Showing</h3>"
            + "<pre><code>hidden md:block   \u2192 hidden on phone, visible on tablet+\n"
            + "md:hidden         \u2192 shown on phone, hidden on tablet+</code></pre>"
            + "<p>Useful for swapping a mobile drawer for a desktop nav without duplicating markup.</p>",

            story(
                n("Lyra holds a parchment up to the light. It changes size as she tilts it."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Start at the smallest screen. Add breakpoints only where the design must adapt. That is the mobile-first rule."),
                e("Responsive columns",
                  "&lt;div class=\"grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4\"&gt;")
            ),

            "<p>Make the gallery responsive. On <code>.gallery</code> apply:</p>"
            + "<ul>"
            + "<li><code>grid</code></li>"
            + "<li><code>grid-cols-1</code> \u2014 mobile</li>"
            + "<li><code>md:grid-cols-2</code> \u2014 tablet</li>"
            + "<li><code>lg:grid-cols-3</code> \u2014 laptop</li>"
            + "<li><code>gap-4</code></li>"
            + "</ul>"
            + "<p>Resize the preview panel to see each breakpoint fire.</p>",

            "<div class=\"gallery\">\n"
            + "  <div class=\"bg-white p-4 rounded\">A</div>\n"
            + "  <div class=\"bg-white p-4 rounded\">B</div>\n"
            + "  <div class=\"bg-white p-4 rounded\">C</div>\n"
            + "</div>",

            tests(
                twTest("Grid display", ".gallery", "grid"),
                twTest("Mobile: one column", ".gallery", "grid-cols-1"),
                twTest("Tablet: two columns", ".gallery", "md:grid-cols-2"),
                twTest("Laptop: three columns", ".gallery", "lg:grid-cols-3"),
                twTest("Gap between items", ".gallery", "gap-4")
            ),

            "Explain mobile-first in your own words. Why are unprefixed utilities the smallest-screen styles rather than the largest?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b1", QuestionTier.RECALL,
            "<p>At what minimum viewport width does <code>md:</code> activate?</p>",
            new String[]{"768px", "640px", "1024px", "480px"},
            "768px",
            "<p>Default <code>md</code> is 768px and up.</p>");

        mcQuestion("tw-b1", QuestionTier.APPLICATION,
            "<p>What does <code>hidden md:flex</code> mean?</p>",
            new String[]{"Hidden on phones, flex on tablet and up", "Always hidden", "Flex on phones, hidden on tablet", "Broken syntax"},
            "Hidden on phones, flex on tablet and up",
            "<p><code>hidden</code> is the base; <code>md:flex</code> overrides at \u2265768px.</p>");

        tfQuestion("tw-b1", QuestionTier.RECALL,
            "<p>Tailwind breakpoints are min-width based.</p>",
            "True",
            "<p>Prefixes activate at that width <em>and above</em> \u2014 the foundation of mobile-first design.</p>");

        scenarioQuestion("tw-b1",
            "<p>A designer hands you a layout: three columns on desktop, one column stacked on mobile. A junior writes <code>grid grid-cols-3 sm:grid-cols-1</code>. What\u2019s the bug?</p>",
            "The breakpoint logic is inverted \u2014 they set three columns as the base and one column at sm+. They should write grid-cols-1 md:grid-cols-3 so mobile gets the stack and larger screens get three columns.",
            "<p>Mobile-first means the base (unprefixed) value is for the smallest screen. Override at larger breakpoints, not smaller.</p>");
    }

    // ── TW-B2: State Variants ────────────────────────────────────────────────

    private void seedTwB2() {
        subChunk(
            "tw-b2", "tw-b", "State Variants", 2, 75, "index.html",

            "<p>A button that doesn\u2019t react to the cursor feels dead. A link with no focus ring is unusable with a keyboard. <strong>State variants</strong> let a single utility apply only when the element is in a certain state \u2014 no custom CSS needed.</p>",

            "<p>A younger mage slips in, bells chiming at their wrists. <em>\u201CVex handles reactions,\u201D</em> Lyra explains. <em>\u201CI\u2019ll let you two get on with it.\u201D</em></p>"
            + "<p>Vex tosses a sigil at the wall. It brightens when your eyes settle on it, dims when you look away. <em>\u201CStatic spells are dead spells. The trick: prefix a glyph with <strong>when</strong> it should fire.\u201D</em></p>"
            + "<h3>Syntax \u2014 variant:utility</h3>"
            + "<pre><code>hover:bg-blue-700    \u2192 fires on hover\n"
            + "focus:ring-2         \u2192 fires on focus\n"
            + "active:scale-95      \u2192 fires while pressed\n"
            + "disabled:opacity-50  \u2192 fires when disabled</code></pre>"
            + "<h3>Common Variants</h3>"
            + "<ul>"
            + "<li><code>hover:</code> \u2014 pointer over the element</li>"
            + "<li><code>focus:</code> \u2014 element focused</li>"
            + "<li><code>focus-visible:</code> \u2014 focused via keyboard (not mouse)</li>"
            + "<li><code>active:</code> \u2014 being pressed</li>"
            + "<li><code>disabled:</code> \u2014 element is disabled</li>"
            + "<li><code>group-hover:</code> \u2014 a parent with class <code>group</code> is hovered</li>"
            + "<li><code>peer-checked:</code> \u2014 a sibling with class <code>peer</code> is checked</li>"
            + "</ul>"
            + "<h3>Stacking</h3>"
            + "<pre><code>dark:hover:bg-slate-800   \u2192 dark mode AND hovered\n"
            + "md:focus:ring-4          \u2192 medium screens AND focused</code></pre>"
            + "<h3>Always Pair With Transition</h3>"
            + "<p>State changes without transitions feel cheap. Add <code>transition</code> and a duration:</p>"
            + "<pre><code>class=\"bg-blue-600 hover:bg-blue-700 transition-colors duration-150\"</code></pre>"
            + "<p><em>\u201CAnd never forget <code>focus:</code>. A button that only lights up on hover is invisible to keyboard users. That is a betrayal \u2014 and the Academy does not forgive it.\u201D</em></p>",

            story(
                n("A bell chimes from a side passage. A younger mage slips into the room, grinning."),
                d("\uD83D\uDD14", "animancer", "Vex the Animancer", "mentor",
                  "Hover, focus, press, disabled \u2014 that is where spells come alive. Prefix the glyph with the state. "
                  + "<code>hover:bg-blue-700</code>, <code>focus:ring-2</code>, <code>active:scale-95</code>."),
                e("Interactive button",
                  "&lt;button class=\"bg-blue-600 hover:bg-blue-700 focus:ring-2 transition\"&gt;Cast&lt;/button&gt;")
            ),

            "<p>Give the <code>.btn</code> realistic feedback. Apply:</p>"
            + "<ul>"
            + "<li><code>bg-blue-600</code></li>"
            + "<li><code>hover:bg-blue-700</code></li>"
            + "<li><code>focus:ring-2</code></li>"
            + "<li><code>transition</code></li>"
            + "</ul>",

            "<button class=\"btn text-white px-4 py-2 rounded\">\n  Cast Spell\n</button>",

            tests(
                twTest("Base background", ".btn", "bg-blue-600"),
                twTest("Hover darkens", ".btn", "hover:bg-blue-700"),
                twTest("Focus ring", ".btn", "focus:ring-2"),
                twTest("Has transition", ".btn", "transition")
            ),

            "Why is it critical to provide <code>focus:</code> styles in addition to <code>hover:</code>? Think about users who don\u2019t use a mouse.",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b2", QuestionTier.RECALL,
            "<p>Which variant fires when the user hovers over an element?</p>",
            new String[]{"hover:", "mouseover:", "over:", "pointer:"},
            "hover:",
            "<p>Tailwind uses <code>hover:</code>.</p>");

        mcQuestion("tw-b2", QuestionTier.RECALL,
            "<p>What does <code>disabled:opacity-50</code> do?</p>",
            new String[]{"Makes the element 50% transparent when disabled", "Disables the opacity property", "Applies only in dark mode", "Removes element from the DOM"},
            "Makes the element 50% transparent when disabled",
            "<p>The <code>disabled:</code> variant applies the utility only when the element is disabled.</p>");

        tfQuestion("tw-b2", QuestionTier.APPLICATION,
            "<p>You can stack variants: <code>md:hover:bg-blue-700</code> applies only on hover at \u2265md screens.</p>",
            "True",
            "<p>Tailwind allows stacking \u2014 read left to right: responsive, then state.</p>");

        scenarioQuestion("tw-b2",
            "<p>A button uses <code>bg-blue-600 hover:bg-blue-700</code> but no transition. Why does this feel cheap?</p>",
            "The colour change snaps instantly. Adding transition-colors duration-150 smooths it.",
            "<p>Transitions turn state changes into motion, which reads as polished. Without one, the flip is jarring.</p>");
    }

    // ── TW-B3: Flexbox & Grid ────────────────────────────────────────────────

    private void seedTwB3() {
        subChunk(
            "tw-b3", "tw-b", "Flexbox & Grid", 3, 80, "index.html",

            "<p>Utility classes alone don\u2019t make a page \u2014 <em>arrangement</em> does. Flexbox is one-dimensional (a row or a column). Grid is two-dimensional (rows and columns together). Tailwind gives both a precise utility vocabulary.</p>",

            "<p>Lyra unrolls a blueprint \u2014 a storefront with a header, a sidebar, a main stage, and a footer. <em>\u201CGrid plans the rooms,\u201D</em> she says. <em>\u201CFlex arranges what stands inside each one.\u201D</em></p>"
            + "<h3>Flexbox \u2014 One Axis</h3>"
            + "<pre><code>flex                 \u2192 display: flex\n"
            + "flex-col             \u2192 vertical stack\n"
            + "items-center         \u2192 cross-axis centre\n"
            + "justify-between      \u2192 space between children (main axis)\n"
            + "gap-4                \u2192 gap between children\n"
            + "flex-1               \u2192 child grows to fill</code></pre>"
            + "<pre><code>&lt;nav class=\"flex items-center justify-between p-4\"&gt;\n"
            + "  &lt;div&gt;Logo&lt;/div&gt;\n"
            + "  &lt;div class=\"flex gap-4\"&gt;\n"
            + "    &lt;a&gt;Home&lt;/a&gt; &lt;a&gt;Docs&lt;/a&gt; &lt;a&gt;Sign in&lt;/a&gt;\n"
            + "  &lt;/div&gt;\n"
            + "&lt;/nav&gt;</code></pre>"
            + "<h3>Grid \u2014 Two Axes</h3>"
            + "<pre><code>grid                  \u2192 display: grid\n"
            + "grid-cols-3           \u2192 3 equal columns\n"
            + "grid-cols-12          \u2192 12-column system\n"
            + "grid-cols-[200px_1fr] \u2192 fixed sidebar + flexible main\n"
            + "col-span-2            \u2192 child spans 2 columns\n"
            + "gap-6                 \u2192 cell gap</code></pre>"
            + "<pre><code>&lt;div class=\"grid grid-cols-12 gap-4\"&gt;\n"
            + "  &lt;aside class=\"col-span-3\"&gt;Sidebar&lt;/aside&gt;\n"
            + "  &lt;main class=\"col-span-9\"&gt;Content&lt;/main&gt;\n"
            + "&lt;/div&gt;</code></pre>"
            + "<h3>Choosing</h3>"
            + "<ul>"
            + "<li>Row of nav items, buttons in a toolbar \u2192 <strong>flex</strong></li>"
            + "<li>Card gallery, dashboard, two-column page layout \u2192 <strong>grid</strong></li>"
            + "<li>Both support <code>gap-*</code>. Always prefer <code>gap</code> over manual margins on children.</li>"
            + "</ul>",

            story(
                n("Lyra unrolls a blueprint. Walls, doorways, archways \u2014 and rooms with furniture inside."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Grid plans the walls. Flex arranges the furniture. Use each where it fits. And always \u2014 <em>always</em> \u2014 use <code>gap</code> instead of margin gymnastics."),
                e("A dashboard frame",
                  "&lt;div class=\"grid grid-cols-12 gap-4\"&gt;\n  &lt;aside class=\"col-span-3\"&gt;&lt;/aside&gt;\n  &lt;main class=\"col-span-9\"&gt;&lt;/main&gt;\n&lt;/div&gt;")
            ),

            "<p>Build a two-column dashboard shell:</p>"
            + "<ul>"
            + "<li><code>.shell</code> \u2192 <code>grid</code>, <code>grid-cols-12</code>, <code>gap-4</code></li>"
            + "<li><code>.side</code> \u2192 <code>col-span-3</code></li>"
            + "<li><code>.main</code> \u2192 <code>col-span-9</code></li>"
            + "</ul>",

            "<div class=\"shell p-4\">\n"
            + "  <aside class=\"side bg-white p-4 rounded\">Sidebar</aside>\n"
            + "  <main class=\"main bg-white p-4 rounded\">Main content</main>\n"
            + "</div>",

            tests(
                twTest("Shell uses grid", ".shell", "grid"),
                twTest("12 columns", ".shell", "grid-cols-12"),
                twTest("Gap", ".shell", "gap-4"),
                twTest("Sidebar spans 3", ".side", "col-span-3"),
                twTest("Main spans 9", ".main", "col-span-9")
            ),

            "When would you reach for flex over grid, and vice versa? Give a real UI example for each.",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b3", QuestionTier.RECALL,
            "<p>Which class makes three equal columns?</p>",
            new String[]{"grid-cols-3", "cols-3", "grid-3", "columns-3"},
            "grid-cols-3",
            "<p><code>grid-cols-3</code> creates three 1fr columns.</p>");

        mcQuestion("tw-b3", QuestionTier.APPLICATION,
            "<p>Nav with logo pinned left, links pinned right, vertically centred. Which combo?</p>",
            new String[]{"flex justify-between items-center", "grid grid-cols-2", "flex flex-col", "inline-block"},
            "flex justify-between items-center",
            "<p><code>justify-between</code> pushes first/last children apart; <code>items-center</code> aligns vertically.</p>");

        tfQuestion("tw-b3", QuestionTier.RECALL,
            "<p>The <code>gap</code> utility works on both flex and grid containers.</p>",
            "True",
            "<p>Modern CSS supports <code>gap</code> on flex as well as grid.</p>");

        mcQuestion("tw-b3", QuestionTier.APPLICATION,
            "<p>Which utility makes a flex child grow to fill remaining space?</p>",
            new String[]{"flex-1", "grow-full", "w-grow", "auto-fill"},
            "flex-1",
            "<p><code>flex-1</code> sets <code>flex: 1 1 0%</code> \u2014 the item grows to consume available space.</p>");
    }

    // ── TW-B4: Theme Config, Plugins & @apply ────────────────────────────────

    private void seedTwB4() {
        subChunk(
            "tw-b4", "tw-b", "Theme Config, Plugins & @apply", 4, 85, "index.html",

            "<p>Tailwind\u2019s defaults are a starting point, not the ceiling. <code>tailwind.config.js</code> lets you extend the palette, add your own fonts, register plugins, and pull repeating utility strings into component classes with <code>@apply</code>.</p>",

            "<p>Maester Cordell returns, this time with a battered config file open in front of you. <em>\u201CEvery serious project customises Tailwind. These are the three tools you\u2019ll reach for most.\u201D</em></p>"
            + "<h3>1. Extending the Theme</h3>"
            + "<pre><code>// tailwind.config.js\n"
            + "module.exports = {\n"
            + "  theme: {\n"
            + "    extend: {\n"
            + "      colors: {\n"
            + "        brand: { DEFAULT: '#6D28D9', light: '#A78BFA', dark: '#4C1D95' },\n"
            + "      },\n"
            + "      fontFamily: { display: ['Cinzel', 'serif'] },\n"
            + "      spacing: { '18': '4.5rem' },\n"
            + "      boxShadow: { soft: '0 4px 12px rgba(0,0,0,0.06)' },\n"
            + "    },\n"
            + "  },\n"
            + "};</code></pre>"
            + "<p>Now <code>bg-brand</code>, <code>text-brand-light</code>, <code>font-display</code>, <code>p-18</code>, and <code>shadow-soft</code> all work as first-class utilities.</p>"
            + "<h3>2. Official Plugins</h3>"
            + "<pre><code>npm install -D @tailwindcss/forms @tailwindcss/typography @tailwindcss/aspect-ratio\n"
            + "// tailwind.config.js\n"
            + "plugins: [\n"
            + "  require('@tailwindcss/forms'),       // sensible form-control defaults\n"
            + "  require('@tailwindcss/typography'),  // the `prose` class for long-form content\n"
            + "  require('@tailwindcss/aspect-ratio'),// aspect-w-16 aspect-h-9\n"
            + "]</code></pre>"
            + "<p><code>@tailwindcss/forms</code> resets browser-specific form control styling so your inputs look consistent before you style them. Add it early.</p>"
            + "<h3>3. Custom Utility Plugins</h3>"
            + "<pre><code>const plugin = require('tailwindcss/plugin');\n"
            + "plugins: [\n"
            + "  plugin(({ addUtilities }) =&gt; {\n"
            + "    addUtilities({\n"
            + "      '.bg-glass': { 'backdrop-filter': 'blur(8px)', 'background-color': 'rgba(255,255,255,0.5)' },\n"
            + "      '.text-balance': { 'text-wrap': 'balance' },\n"
            + "    });\n"
            + "  }),\n"
            + "]</code></pre>"
            + "<h3>4. @apply \u2014 Use Sparingly</h3>"
            + "<p>When a utility string repeats identically in many places and you can\u2019t make it a component, <code>@apply</code> bundles it into a class in your CSS file:</p>"
            + "<pre><code>/* input.css */\n"
            + "@layer components {\n"
            + "  .btn-primary {\n"
            + "    @apply px-4 py-2 bg-brand text-white rounded-md hover:bg-brand-dark transition;\n"
            + "  }\n"
            + "}</code></pre>"
            + "<p><strong>Component first, <code>@apply</code> second.</strong> If your stack supports components (React, Vue, Svelte), build a <code>&lt;Button&gt;</code> component rather than <code>.btn-primary</code>. Reserve <code>@apply</code> for plain HTML, email templates, MDX, or CMS-rendered markup.</p>",

            story(
                n("Cordell spreads a battered <code>tailwind.config.js</code> across the bench. <em>\u201CThis is where Tailwind stops being someone else\u2019s framework and becomes yours.\u201D</em>"),
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "maester", "Maester Cordell", "mentor",
                  "Extend the theme with your brand. Add plugins for forms and long-form content. Write custom utilities when you need them. And reach for <code>@apply</code> only when a real component won\u2019t do."),
                e("Theme + plugin + @apply together",
                  "// tailwind.config.js\ntheme: { extend: { colors: { brand: '#6D28D9' } } }\nplugins: [require('@tailwindcss/forms')]\n\n/* input.css */\n.btn-primary { @apply bg-brand text-white px-4 py-2 rounded; }")
            ),

            "<p>Assume this project\u2019s config extends colours so <code>bg-brand</code> is valid, and registers the forms plugin. Style the <code>.field</code> input:</p>"
            + "<ul>"
            + "<li><code>w-full</code>, <code>px-3</code>, <code>py-2</code></li>"
            + "<li><code>border</code>, <code>border-gray-300</code>, <code>rounded-md</code></li>"
            + "<li><code>focus:border-brand</code>, <code>focus:ring-2</code></li>"
            + "</ul>",

            "<form class=\"max-w-sm p-6 bg-white rounded-xl\">\n"
            + "  <label class=\"block text-sm mb-1\" for=\"email\">Email</label>\n"
            + "  <input id=\"email\" class=\"field\" type=\"email\" />\n"
            + "</form>",

            tests(
                twTest("Full width", ".field", "w-full"),
                twTest("Horizontal padding", ".field", "px-3"),
                twTest("Vertical padding", ".field", "py-2"),
                twTest("Border", ".field", "border"),
                twTest("Border colour", ".field", "border-gray-300"),
                twTest("Rounded", ".field", "rounded-md"),
                twTest("Focus uses brand colour", ".field", "focus:border-brand"),
                twTest("Focus ring", ".field", "focus:ring-2")
            ),

            "When would you reach for <code>@apply</code> vs extracting a component? Why does the Tailwind team recommend \u201Ccomponent first\u201D?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b4", QuestionTier.RECALL,
            "<p>Where do you add project-specific colours in Tailwind?</p>",
            new String[]{"tailwind.config.js under theme.extend.colors",
                         ":root in your CSS file",
                         "A separate colors.json",
                         "Inside each component"},
            "tailwind.config.js under theme.extend.colors",
            "<p><code>theme.extend.colors</code> is the canonical place \u2014 Tailwind generates matching utilities automatically.</p>");

        mcQuestion("tw-b4", QuestionTier.RECALL,
            "<p>Which official plugin adds sensible defaults to form controls?</p>",
            new String[]{"@tailwindcss/forms", "@tailwindcss/typography", "@tailwindcss/aspect-ratio", "@tailwindcss/line-clamp"},
            "@tailwindcss/forms",
            "<p>The forms plugin resets cross-browser form-control styling.</p>");

        tfQuestion("tw-b4", QuestionTier.APPLICATION,
            "<p>In a React project, you should usually prefer a <code>&lt;Button&gt;</code> component over a <code>.btn</code> class built with <code>@apply</code>.</p>",
            "True",
            "<p>Tailwind\u2019s official guidance: extract repeated UI into framework components, not CSS classes.</p>");

        scenarioQuestion("tw-b4",
            "<p>A team <code>@apply</code>-s 30 utilities into <code>.btn</code>, then adds 12 modifier classes (<code>.btn-sm</code>, <code>.btn-danger</code>). Six months later, what problem has emerged?</p>",
            "They rebuilt a traditional CSS component library on top of Tailwind \u2014 losing composition and ending up with the worst of both worlds. A React component with CVA would have been cleaner.",
            "<p><code>@apply</code> at scale recreates the complexity Tailwind was meant to avoid. Framework components (with CVA or similar) compose better.</p>");
    }

    // ── TW-B5: Dark Mode & Multi-Theme ───────────────────────────────────────

    private void seedTwB5() {
        subChunk(
            "tw-b5", "tw-b", "Dark Mode & Multi-Theme", 5, 80, "index.html",

            "<p>A dark theme is no longer a novelty \u2014 it\u2019s an expectation. Tailwind\u2019s <code>dark:</code> variant gives you overrides with almost no duplication. Combine it with CSS variables and you can ship <em>multiple</em> themes from the same utility classes.</p>",

            "<p>The Scriptorium dims as Lyra lowers the candles. The parchments shift their glow to match the lantern light. <em>\u201CMagic that works by day must work by night. And in the bright sun, the old sepia scrolls \u2014 those have their readers too.\u201D</em></p>"
            + "<h3>Two Dark-Mode Strategies</h3>"
            + "<pre><code>// tailwind.config.js\n"
            + "darkMode: 'media'   \u2192 follows the OS preference\n"
            + "darkMode: 'class'   \u2192 toggle by adding class=\"dark\" to &lt;html&gt;</code></pre>"
            + "<p><em>\u201C<strong>Class</strong> is the production choice,\u201D</em> Lyra says. <em>\u201CIt lets users override their OS choice. Media-query dark mode strips that control away.\u201D</em></p>"
            + "<h3>Applying dark: Styles</h3>"
            + "<pre><code>&lt;div class=\"bg-white dark:bg-slate-900\n"
            + "            text-gray-900 dark:text-gray-100\"&gt;</code></pre>"
            + "<p>Unprefixed = light theme. <code>dark:</code> = dark theme override. You rarely need to duplicate spacing, typography, or layout utilities \u2014 only the colour-related ones.</p>"
            + "<h3>Multi-Theme With CSS Variables</h3>"
            + "<p>For more than two themes (brand partners, seasonal themes, high-contrast mode), switch to CSS variables as the source of truth:</p>"
            + "<pre><code>/* index.css */\n"
            + ":root          { --surface: #FFFFFF; --text: #0F172A; --brand: #6D28D9; }\n"
            + ".dark          { --surface: #0F172A; --text: #F1F5F9; --brand: #A78BFA; }\n"
            + ".theme-sepia   { --surface: #FDF6E3; --text: #586E75; --brand: #CB4B16; }</code></pre>"
            + "<p>In the config, reference the variables:</p>"
            + "<pre><code>// tailwind.config.js\n"
            + "theme: {\n"
            + "  extend: {\n"
            + "    colors: {\n"
            + "      surface: 'var(--surface)',\n"
            + "      text:    'var(--text)',\n"
            + "      brand:   'var(--brand)',\n"
            + "    },\n"
            + "  },\n"
            + "}</code></pre>"
            + "<p>Now <code>bg-surface text-text</code> auto-adapts to whatever theme class is on <code>&lt;html&gt;</code>. You write components once; themes are just variable sets.</p>"
            + "<h3>Arbitrary-Value Escape Hatch</h3>"
            + "<pre><code>bg-[var(--surface)]   \u2192 inline CSS variable without touching the config</code></pre>",

            story(
                n("Lyra dims the candles one by one. The parchments shift their glow to match."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Two themes? The <code>dark:</code> prefix. Three or more? CSS variables. The authoring story stays the same \u2014 <code>bg-surface</code>, <code>text-text</code> \u2014 only the variable definitions change."),
                e("A card that adapts",
                  "&lt;div class=\"bg-white dark:bg-slate-900 text-gray-900 dark:text-gray-100 p-6 rounded-xl\"&gt;\n  Adapts to light and dark.\n&lt;/div&gt;")
            ),

            "<p>Make the <code>.card</code> adapt to dark mode. Apply: "
            + "<code>bg-white</code>, <code>dark:bg-slate-900</code>, <code>text-gray-900</code>, "
            + "<code>dark:text-gray-100</code>, <code>p-6</code>, <code>rounded-xl</code>.</p>",

            "<div class=\"card shadow-md\">\n"
            + "  <h3>Arcane Academy</h3>\n"
            + "  <p>A theme for every hour.</p>\n"
            + "</div>",

            tests(
                twTest("Light background", ".card", "bg-white"),
                twTest("Dark background override", ".card", "dark:bg-slate-900"),
                twTest("Light text", ".card", "text-gray-900"),
                twTest("Dark text override", ".card", "dark:text-gray-100"),
                twTest("Padding", ".card", "p-6"),
                twTest("Rounded", ".card", "rounded-xl")
            ),

            "Explain the trade-off between <code>darkMode: 'media'</code> and <code>darkMode: 'class'</code>. When would you reach for CSS variables instead of <code>dark:</code> overrides?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b5", QuestionTier.RECALL,
            "<p>Which prefix applies a utility only in dark mode?</p>",
            new String[]{"dark:", "night:", "theme-dark:", "@dark"},
            "dark:",
            "<p>Tailwind\u2019s dark-mode variant is <code>dark:</code>.</p>");

        mcQuestion("tw-b5", QuestionTier.APPLICATION,
            "<p>Which config lets users toggle dark mode independently of their OS?</p>",
            new String[]{"darkMode: 'class'", "darkMode: 'media'", "darkMode: 'toggle'", "darkMode: true"},
            "darkMode: 'class'",
            "<p>The class strategy keys dark mode to a <code>.dark</code> class on the root element, which JS can toggle.</p>");

        tfQuestion("tw-b5", QuestionTier.APPLICATION,
            "<p>You must duplicate every utility with a <code>dark:</code> prefix to support dark mode.</p>",
            "False",
            "<p>Only colour/surface utilities typically need dark overrides. Spacing and typography usually stay the same.</p>");

        scenarioQuestion("tw-b5",
            "<p>A product needs light, dark, and a brand-partner theme. Which approach lets one set of components serve all three?</p>",
            "Define CSS variables per theme class on <html>, map Tailwind colours to those variables, so utilities like bg-surface auto-adapt.",
            "<p>CSS variables are the idiomatic multi-theme solution: components stay identical; themes are just variable sets.</p>");
    }

    // ── TW-B6: Animations & Keyframes ────────────────────────────────────────

    private void seedTwB6() {
        subChunk(
            "tw-b6", "tw-b", "Animations & Keyframes", 6, 80, "index.html",

            "<p>Motion is feedback. A button that fades over 150ms feels premium. One that snaps feels cheap. Tailwind\u2019s transitions, transforms, and animations make polished motion a one-line decision \u2014 and custom keyframes give you project-specific motion without a single line of hand-written CSS.</p>",

            "<p>Lyra taps a glowing rune. It brightens over a heartbeat, not a flash. <em>\u201CMotion is the difference between a page that feels alive and one that feels like a poster,\u201D</em> she says. <em>\u201CBut give reduced-motion readers the choice. Never force motion on them.\u201D</em></p>"
            + "<h3>Transitions</h3>"
            + "<pre><code>transition           \u2192 transition all\n"
            + "transition-colors    \u2192 colour-related only\n"
            + "transition-transform \u2192 transforms only\n"
            + "duration-150         \u2192 150ms\n"
            + "ease-in-out          \u2192 smoother curve\n"
            + "delay-100            \u2192 100ms delay</code></pre>"
            + "<h3>Transforms</h3>"
            + "<pre><code>scale-95      \u2192 shrink to 95%\n"
            + "scale-105     \u2192 grow to 105%\n"
            + "rotate-3      \u2192 3-degree rotation\n"
            + "translate-y-1 \u2192 shift down 4px</code></pre>"
            + "<h3>Built-in Animations</h3>"
            + "<pre><code>animate-spin    \u2192 spinner\n"
            + "animate-pulse   \u2192 skeleton shimmer\n"
            + "animate-bounce  \u2192 attention nudge\n"
            + "animate-ping    \u2192 notification dot</code></pre>"
            + "<h3>Custom Keyframes</h3>"
            + "<p>Define project-specific animations in <code>tailwind.config.js</code>:</p>"
            + "<pre><code>theme: {\n"
            + "  extend: {\n"
            + "    keyframes: {\n"
            + "      fadeIn: { '0%': { opacity: 0 }, '100%': { opacity: 1 } },\n"
            + "      slideUp: { '0%': { transform: 'translateY(8px)', opacity: 0 },\n"
            + "                 '100%': { transform: 'translateY(0)', opacity: 1 } },\n"
            + "    },\n"
            + "    animation: {\n"
            + "      fadeIn:  'fadeIn 200ms ease-out',\n"
            + "      slideUp: 'slideUp 200ms ease-out',\n"
            + "    },\n"
            + "  },\n"
            + "}</code></pre>"
            + "<p>Then <code>animate-fadeIn</code> and <code>animate-slideUp</code> work as first-class utilities.</p>"
            + "<h3>Reduced Motion</h3>"
            + "<pre><code>motion-safe:animate-bounce    \u2192 only animates if user has not requested reduced motion\n"
            + "motion-reduce:transition-none \u2192 disables when user prefers reduced motion</code></pre>"
            + "<p>Always gate attention-grabbing animations with <code>motion-safe:</code>. Users with vestibular conditions, migraines, or simple preference should not be forced to see them.</p>",

            story(
                n("Lyra taps a rune on the wall. It brightens over a heartbeat \u2014 not a flash."),
                d("\uD83E\uDDD9\u200D\u2640\uFE0F", "mentor", "Lyra the Scribe", "mentor",
                  "Transition. Transform. Animation. Three words, and a button can breathe. But respect <code>prefers-reduced-motion</code> \u2014 some readers cannot tolerate the breathing."),
                e("A button that lifts",
                  "&lt;button class=\"transition-transform duration-150 hover:scale-105\"&gt;Hover me&lt;/button&gt;")
            ),

            "<p>Make the <code>.btn</code> feel alive. Apply:</p>"
            + "<ul>"
            + "<li><code>transition-transform</code></li>"
            + "<li><code>duration-150</code></li>"
            + "<li><code>hover:scale-105</code></li>"
            + "<li><code>motion-reduce:transition-none</code></li>"
            + "</ul>",

            "<button class=\"btn bg-purple-600 text-white px-6 py-3 rounded-lg\">\n  Cast\n</button>",

            tests(
                twTest("Transform transition", ".btn", "transition-transform"),
                twTest("Duration 150ms", ".btn", "duration-150"),
                twTest("Hover lift", ".btn", "hover:scale-105"),
                twTest("Reduced-motion users get no transition", ".btn", "motion-reduce:transition-none")
            ),

            "Why should animations respect <code>prefers-reduced-motion</code>? Name two kinds of users who benefit.",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-b6", QuestionTier.RECALL,
            "<p>Which class adds a 300ms transition duration?</p>",
            new String[]{"duration-300", "transition-300", "ease-300", "time-300"},
            "duration-300",
            "<p>Tailwind durations use ms values: 75, 150, 300, 500, 700, 1000.</p>");

        mcQuestion("tw-b6", QuestionTier.APPLICATION,
            "<p>Skeleton-loading box. Which built-in animation fits best?</p>",
            new String[]{"animate-pulse", "animate-spin", "animate-bounce", "animate-ping"},
            "animate-pulse",
            "<p><code>animate-pulse</code> is the classic skeleton shimmer \u2014 subtle opacity cycling.</p>");

        tfQuestion("tw-b6", QuestionTier.APPLICATION,
            "<p><code>motion-reduce:transition-none</code> disables the transition for users who have opted into reduced motion.</p>",
            "True",
            "<p><code>motion-reduce:</code> targets <code>prefers-reduced-motion: reduce</code>.</p>");

        scenarioQuestion("tw-b6",
            "<p>You want a custom <code>animate-fadeIn</code> utility for a modal open animation. Where do you define it and what are the two config keys you add?</p>",
            "In tailwind.config.js under theme.extend, add a keyframes entry (fadeIn with 0%/100% opacity stops) and a matching animation entry that references that keyframe.",
            "<p>Both <code>keyframes</code> and <code>animation</code> live under <code>theme.extend</code>. Tailwind generates <code>animate-fadeIn</code> from the pair.</p>");
    }
}
