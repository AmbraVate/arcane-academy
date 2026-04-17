package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

/**
 * Tailwind CSS — Chunk TW-C: Design-System Archmage (Expert tier)
 *
 * Config deep-dive, accessibility, performance, design systems,
 * framework integration, and production hardening.
 *
 * Prereq: TW-B Practitioner.
 */
@Component
public class TailwindExpertSeeder extends AbstractChunkSeeder {

    public TailwindExpertSeeder(ChunkRepository chunkRepository,
                                 SubChunkRepository subChunkRepository,
                                 QuestionRepository questionRepository,
                                 RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {

        // ── Chunk TW-C: Design-System Archmage ────────────────────────────────
        Chunk twc = Chunk.builder()
                .id("tw-c")
                .title("Design-System Archmage")
                .glyph("🏛️")
                .sortOrder(3)
                .prerequisiteIds("[\"tw-b\"]")
                .tier(LearnerPath.EXPERT)
                .topicId("tailwind")
                .build();
        chunkRepository.save(twc);

        seedTwC1();
        seedTwC2();
        seedTwC3();
        seedTwC4();
        seedTwC5();
        seedTwC6();
    }

    private String twTest(String label, String selector, String requiredClass) {
        return "{\"label\":\"" + esc(label)
                + "\",\"selector\":\"" + esc(selector)
                + "\",\"requiredClass\":\"" + esc(requiredClass) + "\"}";
    }

    // ── TW-C1: Config Deep-Dive & Semantic Tokens ────────────────────────────

    private void seedTwC1() {
        subChunk(
            "tw-c1", "tw-c", "Config & Semantic Tokens", 1, 90, "index.html",

            // Hook
            "<p>Out of the box, Tailwind's palette is named by colour (<code>blue-500</code>). In a real product you "
            + "need names by <em>intent</em> (<code>brand</code>, <code>surface</code>, <code>danger</code>). "
            + "That's what <code>tailwind.config.js</code> unlocks.</p>",

            // Explanation
            "<h3>theme.extend</h3>"
            + "<pre><code>// tailwind.config.js\n"
            + "module.exports = {\n"
            + "  theme: {\n"
            + "    extend: {\n"
            + "      colors: {\n"
            + "        brand: { DEFAULT: '#6D28D9', light: '#A78BFA', dark: '#4C1D95' },\n"
            + "        surface: 'var(--surface)',\n"
            + "        danger: '#DC2626',\n"
            + "      },\n"
            + "      fontFamily: { display: ['Cinzel', 'serif'] },\n"
            + "      spacing: { '18': '4.5rem' },\n"
            + "    },\n"
            + "  },\n"
            + "};</code></pre>"
            + "<p>Now <code>bg-brand</code>, <code>text-brand-light</code>, <code>bg-surface</code>, and "
            + "<code>font-display</code> all work as first-class utilities.</p>"
            + "<h3>Why Semantic Names Beat Colour Names</h3>"
            + "<ul>"
            + "<li><code>bg-blue-500</code> describes <em>what it looks like</em>. Rebranding breaks every file.</li>"
            + "<li><code>bg-brand</code> describes <em>what it means</em>. Rebranding is a one-line config change.</li>"
            + "</ul>"
            + "<h3>Custom Plugins (brief)</h3>"
            + "<pre><code>plugin(({ addUtilities }) =&gt; {\n"
            + "  addUtilities({ '.text-balance': { 'text-wrap': 'balance' } });\n"
            + "})</code></pre>"
            + "<p>Plugins let you ship project-wide utilities that don't exist in core.</p>",

            // Story
            story(
                n("You've been summoned to the Archmage's library. Towering shelves hold the Academy's "
                  + "<em>design constitution</em> — tokens that every spell in the kingdom must honour."),
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "At scale, <code>blue-500</code> is a lie. It says \"the colour is blue\" when you mean \"the colour is the brand.\" "
                  + "Name by intent. Then, when the brand changes, your code does not."),
                e("Semantic config",
                  "colors: { brand: { DEFAULT: '#6D28D9', light: '#A78BFA' } }")
            ),

            // Practice brief
            "<p>Assume this project's config extends colours so that <code>bg-brand</code> and <code>text-brand-light</code> are valid. "
            + "Apply to the <code>.card</code>: <code>bg-brand</code>, <code>text-brand-light</code>, <code>p-6</code>, <code>rounded-xl</code>.</p>",

            // Starter
            "<div class=\"card\">\n"
            + "  <h3>The Arcane Order</h3>\n"
            + "  <p>Colours named by intent.</p>\n"
            + "</div>",

            // Tests
            tests(
                twTest("Uses brand background", ".card", "bg-brand"),
                twTest("Uses brand-light text", ".card", "text-brand-light"),
                twTest("Has padding", ".card", "p-6"),
                twTest("Rounded", ".card", "rounded-xl")
            ),

            "Why is <code>bg-brand</code> better than <code>bg-purple-700</code> at scale? What breaks if you spread colour-named classes across a 400-file codebase and then rebrand?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c1", QuestionTier.RECALL,
            "<p>Where do you define project-specific colours in Tailwind?</p>",
            new String[]{"tailwind.config.js under theme.extend.colors", "In index.css :root", "In a separate colors.js", "Inside each component"},
            "tailwind.config.js under theme.extend.colors",
            "<p><code>theme.extend.colors</code> is the canonical place — it generates all matching utilities automatically.</p>");

        scenarioQuestion("tw-c1",
            "<p>A team spreads <code>bg-purple-700</code> across 400 files. Marketing changes the brand colour. What's the fix, and what would have prevented it?</p>",
            "They must find-and-replace 400 files. A semantic token like bg-brand in theme.extend would have made it a one-line change.",
            "<p>Colour-named classes tie code to a palette. Semantic tokens tie code to intent — which is what actually persists across rebrands.</p>");
    }

    // ── TW-C2: Accessibility in Depth ────────────────────────────────────────

    private void seedTwC2() {
        subChunk(
            "tw-c2", "tw-c", "Accessibility in Depth", 2, 90, "index.html",

            // Hook
            "<p>Tailwind will happily let you build an inaccessible UI. It's the author's job to choose classes "
            + "that serve every user — including keyboard, screen-reader, low-vision, and reduced-motion users.</p>",

            // Explanation
            "<h3>Focus-Visible, Not Focus</h3>"
            + "<p>Prefer <code>focus-visible:</code> over <code>focus:</code> so rings appear for keyboard users but not on mouse click.</p>"
            + "<h3>Contrast</h3>"
            + "<p>Aim for WCAG AA: 4.5:1 for body text, 3:1 for large text. Tailwind's 50–300 shades rarely clear it on white.</p>"
            + "<h3>Screen-Reader-Only Text</h3>"
            + "<pre><code>&lt;button&gt;\n"
            + "  &lt;svg /&gt;\n"
            + "  &lt;span class=\"sr-only\"&gt;Close dialog&lt;/span&gt;\n"
            + "&lt;/button&gt;</code></pre>"
            + "<p><code>sr-only</code> hides content visually but keeps it available to assistive tech.</p>"
            + "<h3>Motion</h3>"
            + "<pre><code>motion-safe:animate-bounce\n"
            + "motion-reduce:transition-none</code></pre>"
            + "<h3>ARIA</h3>"
            + "<p>Tailwind provides <code>aria-*</code> variants: <code>aria-expanded:bg-slate-800</code> styles when "
            + "<code>aria-expanded=\"true\"</code>. These let you reflect state visually without JS toggling classes.</p>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "A spell that works for only some readers is a spell half-cast. The <code>sr-only</code> class, "
                  + "<code>focus-visible:</code>, and contrast discipline are not decorations — they are the spell itself."),
                e("Icon-only button that screen readers can still use",
                  "&lt;button&gt;&lt;svg /&gt;&lt;span class=\"sr-only\"&gt;Close&lt;/span&gt;&lt;/button&gt;")
            ),

            // Practice brief
            "<p>Make the icon-only button accessible. On the <code>.close-label</code> span apply <code>sr-only</code>. "
            + "On the <code>.btn-close</code> button apply <code>focus-visible:ring-2</code> and <code>focus-visible:ring-blue-500</code>.</p>",

            // Starter
            "<button class=\"btn-close p-2 rounded\">\n"
            + "  <svg width=\"16\" height=\"16\"></svg>\n"
            + "  <span class=\"close-label\">Close dialog</span>\n"
            + "</button>",

            // Tests
            tests(
                twTest("Close label is screen-reader only", ".close-label", "sr-only"),
                twTest("Button shows focus ring for keyboard", ".btn-close", "focus-visible:ring-2"),
                twTest("Ring colour is blue", ".btn-close", "focus-visible:ring-blue-500")
            ),

            "Explain why <code>focus-visible:</code> is a better default than <code>focus:</code> for interactive elements. What UX problem does <code>focus:</code> cause that <code>focus-visible:</code> solves?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c2", QuestionTier.RECALL,
            "<p>What does the <code>sr-only</code> utility do?</p>",
            new String[]{"Visually hides content but keeps it in the accessibility tree", "Removes content from the DOM", "Hides content in screen readers", "Makes content visible only in print"},
            "Visually hides content but keeps it in the accessibility tree",
            "<p><code>sr-only</code> uses CSS that is invisible on screen while still announced by screen readers.</p>");

        mcQuestion("tw-c2", QuestionTier.APPLICATION,
            "<p>Which is the accessible minimum contrast ratio (WCAG AA) for body text?</p>",
            new String[]{"4.5:1", "3:1", "7:1", "2:1"},
            "4.5:1",
            "<p>WCAG AA requires 4.5:1 for normal-size body text; 3:1 for large text or UI chrome.</p>");

        tfQuestion("tw-c2", QuestionTier.RECALL,
            "<p><code>motion-reduce:</code> applies when the user has set prefers-reduced-motion.</p>",
            "True",
            "<p><code>motion-reduce:</code> maps to the <code>prefers-reduced-motion: reduce</code> media query.</p>");
    }

    // ── TW-C3: Performance & Bundle Size ─────────────────────────────────────

    private void seedTwC3() {
        subChunk(
            "tw-c3", "tw-c", "Performance & Bundle Size", 3, 85, "index.html",

            // Hook
            "<p>Tailwind ships no CSS for classes you don't use — but only if your <code>content</code> globs are right. "
            + "A misconfigured project can leak megabytes of unused CSS into production.</p>",

            // Explanation
            "<h3>The Content Globs Contract</h3>"
            + "<pre><code>// tailwind.config.js\n"
            + "content: [\n"
            + "  './index.html',\n"
            + "  './src/**/*.{ts,tsx,jsx,vue}',\n"
            + "],</code></pre>"
            + "<p>Tailwind scans these files for class names and <strong>only emits CSS for classes it sees</strong>. "
            + "Forget a path → missing styles. Too broad → unnecessary work.</p>"
            + "<h3>Dynamic Class Names — The Trap</h3>"
            + "<p>Tailwind uses <strong>static string matching</strong>. This breaks silently:</p>"
            + "<pre><code>// BAD — bg-${color}-500 is never in the source\n"
            + "const cls = `bg-${color}-500`;\n"
            + "\n"
            + "// GOOD — full class strings exist in source for the scanner\n"
            + "const map = {\n"
            + "  red: 'bg-red-500',\n"
            + "  blue: 'bg-blue-500',\n"
            + "};</code></pre>"
            + "<h3>Safelist</h3>"
            + "<p>When truly dynamic:</p>"
            + "<pre><code>safelist: [{ pattern: /bg-(red|blue|green)-(400|500|600)/ }]</code></pre>"
            + "<h3>Critical CSS</h3>"
            + "<p>With JIT, Tailwind already produces minimal CSS. Most apps don't need a separate critical-CSS pass — "
            + "but SSR frameworks (Next.js) inline above-the-fold CSS automatically.</p>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "Tailwind's power is that it emits nothing you do not use. But if your <code>content</code> globs miss a file, "
                  + "that file's styles vanish. If you interpolate class names, the scanner can't see them — and they vanish too. "
                  + "Always write full class strings."),
                e("Safe dynamic class pattern",
                  "const color = { red: 'bg-red-500', blue: 'bg-blue-500' }[userChoice];")
            ),

            // Practice brief
            "<p>Conceptually apply the configuration rules. For this exercise, add the "
            + "<code>.banner</code> element: <code>bg-brand</code>, <code>text-white</code>, <code>p-4</code>, "
            + "<code>rounded-md</code>. (All full, static class strings — exactly what the JIT scanner can see.)</p>",

            // Starter
            "<div class=\"banner\">\n"
            + "  Welcome back, wizard.\n"
            + "</div>",

            // Tests
            tests(
                twTest("Brand background", ".banner", "bg-brand"),
                twTest("White text", ".banner", "text-white"),
                twTest("Padding", ".banner", "p-4"),
                twTest("Rounded", ".banner", "rounded-md")
            ),

            "Explain why <code>bg-${color}-500</code> breaks Tailwind's JIT. How would you fix it without resorting to <code>safelist</code>?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c3", QuestionTier.RECALL,
            "<p>Where does Tailwind learn which classes to emit?</p>",
            new String[]{"The content glob list in tailwind.config.js", "Automatically, by scanning every file", "The package.json dependencies", "A runtime plugin"},
            "The content glob list in tailwind.config.js",
            "<p>The <code>content</code> array tells the JIT scanner which files to read for class names.</p>");

        scenarioQuestion("tw-c3",
            "<p>Styles work in development but the production build is missing background colours generated via <code>bg-${color}-500</code>. What's the cause and cleanest fix?</p>",
            "The JIT scanner can't see interpolated class fragments. Replace with a static map like { red: 'bg-red-500', blue: 'bg-blue-500' } so full class strings appear in source.",
            "<p>The scanner uses plain string matching. Interpolation hides the full class name. Mapping to full strings is cleaner than <code>safelist</code> for known values.</p>");
    }

    // ── TW-C4: Design Systems ────────────────────────────────────────────────

    private void seedTwC4() {
        subChunk(
            "tw-c4", "tw-c", "Design Systems at Scale", 4, 90, "index.html",

            // Hook
            "<p>A <strong>design system</strong> is the set of tokens, components, and patterns that keep a product "
            + "coherent as it grows. Tailwind is a superb authoring layer for one — if you use the config as the "
            + "single source of truth.</p>",

            // Explanation
            "<h3>Token Layers</h3>"
            + "<ol>"
            + "<li><strong>Primitive tokens</strong>: raw values — <code>purple-700</code>, <code>4rem</code>.</li>"
            + "<li><strong>Semantic tokens</strong>: intent — <code>brand</code>, <code>surface</code>, <code>danger</code>.</li>"
            + "<li><strong>Component tokens</strong>: per-component — <code>button-primary-bg</code>.</li>"
            + "</ol>"
            + "<p>Components should consume <strong>semantic</strong> tokens, never primitives directly.</p>"
            + "<h3>Multi-Theme via CSS Variables</h3>"
            + "<pre><code>:root          { --surface: #FFFFFF; --text: #0F172A; }\n"
            + ".dark          { --surface: #0F172A; --text: #F1F5F9; }\n"
            + ".theme-sepia   { --surface: #FDF6E3; --text: #586E75; }</code></pre>"
            + "<pre><code>// tailwind.config.js\n"
            + "colors: {\n"
            + "  surface: 'var(--surface)',\n"
            + "  text: 'var(--text)',\n"
            + "}</code></pre>"
            + "<p>Now <code>bg-surface text-text</code> auto-adapts to any theme class on <code>&lt;html&gt;</code>.</p>"
            + "<h3>Documentation is the Product</h3>"
            + "<p>A design system without a docs page is a private convention. Tools: Storybook, Ladle, or a plain MDX page per component.</p>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "A design system is a <em>compact</em> between authors and users: here is how we name things, "
                  + "here is the shape of our buttons, here is our contrast. Tokens are the treaty. Components are the enforcement."),
                e("Theme-aware card",
                  "&lt;div class=\"bg-surface text-text p-6 rounded-xl\"&gt;Adapts automatically.&lt;/div&gt;")
            ),

            // Practice brief
            "<p>Style a theme-aware card. On <code>.themed</code> apply: "
            + "<code>bg-surface</code>, <code>text-text</code>, <code>p-6</code>, <code>rounded-xl</code>, <code>shadow-sm</code>.</p>",

            // Starter
            "<div class=\"themed\">\n"
            + "  <h3>Adaptive card</h3>\n"
            + "  <p>Surface and text tokens do the work.</p>\n"
            + "</div>",

            // Tests
            tests(
                twTest("Surface background", ".themed", "bg-surface"),
                twTest("Semantic text colour", ".themed", "text-text"),
                twTest("Padding", ".themed", "p-6"),
                twTest("Radius", ".themed", "rounded-xl"),
                twTest("Subtle shadow", ".themed", "shadow-sm")
            ),

            "Explain the three token layers (primitive, semantic, component). Why should components consume semantic tokens rather than primitives?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c4", QuestionTier.RECALL,
            "<p>Which token layer should a <code>&lt;Button&gt;</code> component reference directly?</p>",
            new String[]{"Semantic tokens", "Primitive colour values", "Pixel-exact shades", "Inline styles"},
            "Semantic tokens",
            "<p>Semantic tokens (brand, surface, danger) decouple components from raw palette values so rebrands and new themes stay cheap.</p>");

        scenarioQuestion("tw-c4",
            "<p>A product must support light, dark, and a brand-partner theme. Which approach lets one set of components serve all three?</p>",
            "Define CSS variables per theme class on <html>, map Tailwind colours to those variables, so semantic utilities like bg-surface auto-adapt.",
            "<p>The CSS-variable pattern is the idiomatic multi-theme solution: components stay identical; themes are just variable sets.</p>");
    }

    // ── TW-C5: Framework Integration ─────────────────────────────────────────

    private void seedTwC5() {
        subChunk(
            "tw-c5", "tw-c", "Framework Integration Patterns", 5, 85, "index.html",

            // Hook
            "<p>Tailwind in a real React codebase means three libraries you'll use in every project: "
            + "<code>clsx</code>, <code>tailwind-merge</code>, and <code>class-variance-authority</code> (CVA). "
            + "Each solves a different class-string problem.</p>",

            // Explanation
            "<h3>clsx — Conditional Classes</h3>"
            + "<pre><code>import clsx from 'clsx';\n"
            + "&lt;button className={clsx('px-4 py-2', isActive &amp;&amp; 'bg-blue-600 text-white')}&gt;</code></pre>"
            + "<h3>tailwind-merge — Conflict Resolution</h3>"
            + "<p>When users pass a <code>className</code> prop, conflicting utilities can appear twice:</p>"
            + "<pre><code>// both px-4 and px-8 end up in the class list — which wins?\n"
            + "&lt;Button className=\"px-8\" /&gt;  // internal: px-4 py-2</code></pre>"
            + "<p><code>twMerge</code> deduplicates intelligently: <code>twMerge('px-4 px-8')</code> → <code>'px-8'</code>.</p>"
            + "<h3>CVA — Typed Variants</h3>"
            + "<pre><code>import { cva } from 'class-variance-authority';\n"
            + "const button = cva('rounded font-medium', {\n"
            + "  variants: {\n"
            + "    intent: { primary: 'bg-blue-600 text-white', danger: 'bg-red-600 text-white' },\n"
            + "    size:   { sm: 'px-3 py-1 text-sm', md: 'px-4 py-2', lg: 'px-6 py-3 text-lg' },\n"
            + "  },\n"
            + "  defaultVariants: { intent: 'primary', size: 'md' },\n"
            + "});\n"
            + "&lt;button className={button({ intent: 'danger', size: 'lg' })}&gt;</code></pre>"
            + "<h3>Headless Libraries</h3>"
            + "<p><strong>Headless UI</strong> and <strong>Radix UI</strong> provide unstyled, accessible primitives "
            + "(dialogs, menus, tabs) that you then style with Tailwind. Don't reinvent a11y from scratch.</p>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "Three allies will serve you in every real project: <code>clsx</code> for conditional incantations, "
                  + "<code>tailwind-merge</code> to resolve clashing spells, and <code>cva</code> to declare the full variant lattice "
                  + "of a component in one place."),
                e("A typed button with CVA",
                  "const button = cva('rounded', { variants: { size: { sm: 'px-3 py-1', md: 'px-4 py-2' } } });")
            ),

            // Practice brief
            "<p>Style a button that matches CVA's <code>primary + md</code> output. On <code>.btn</code> apply: "
            + "<code>bg-blue-600</code>, <code>text-white</code>, <code>rounded</code>, <code>font-medium</code>, "
            + "<code>px-4</code>, <code>py-2</code>.</p>",

            // Starter
            "<button class=\"btn\">Submit</button>",

            // Tests
            tests(
                twTest("Blue background", ".btn", "bg-blue-600"),
                twTest("White text", ".btn", "text-white"),
                twTest("Rounded", ".btn", "rounded"),
                twTest("Medium weight", ".btn", "font-medium"),
                twTest("Horizontal padding", ".btn", "px-4"),
                twTest("Vertical padding", ".btn", "py-2")
            ),

            "Why does a team that accepts <code>className</code> props need <code>tailwind-merge</code>? What bug does it prevent?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c5", QuestionTier.RECALL,
            "<p>What problem does <code>tailwind-merge</code> solve?</p>",
            new String[]{"Conflicts when class strings contain duplicate utilities", "Minifying CSS output", "Generating dynamic class names at runtime", "Typechecking Tailwind classes"},
            "Conflicts when class strings contain duplicate utilities",
            "<p><code>twMerge</code> understands Tailwind's utility groups and keeps the last winning class for each group.</p>");

        scenarioQuestion("tw-c5",
            "<p>A team hand-rolls a <code>&lt;Dialog&gt;</code> component with focus-trap, escape-to-close, and ARIA roles. Six months later a11y audit fails. What should they have done instead?</p>",
            "Use Headless UI or Radix UI for the unstyled, accessible primitives, and style them with Tailwind — do not reinvent a11y.",
            "<p>Accessible dialog/menu/combobox primitives are hard. Libraries like Headless UI and Radix ship them tested and unstyled for you to skin with Tailwind.</p>");
    }

    // ── TW-C6: Production Hardening ──────────────────────────────────────────

    private void seedTwC6() {
        subChunk(
            "tw-c6", "tw-c", "Production Hardening", 6, 100, "index.html",

            // Hook
            "<p>A design system survives first contact with reality when it handles the unglamorous cases: "
            + "RTL languages, print stylesheets, email templates, and visual regressions that sneak in between releases.</p>",

            // Explanation
            "<h3>RTL (Right-to-Left) Support</h3>"
            + "<p>Swap directional utilities with logical equivalents:</p>"
            + "<pre><code>ml-4 → ms-4   (margin-inline-start)\n"
            + "mr-4 → me-4   (margin-inline-end)\n"
            + "pl-4 → ps-4   (padding-inline-start)\n"
            + "text-left → text-start</code></pre>"
            + "<p>These flip automatically when <code>&lt;html dir=\"rtl\"&gt;</code>.</p>"
            + "<h3>Print Styles</h3>"
            + "<pre><code>print:hidden        → removed from print\n"
            + "print:text-black    → force black ink\n"
            + "print:shadow-none   → drop shadows for paper</code></pre>"
            + "<h3>Email-Safe Subsets</h3>"
            + "<p>Email clients don't support Tailwind's output directly. Use <strong>Maizzle</strong> or a similar "
            + "framework that inlines a safe subset and applies MSO-compatible markup.</p>"
            + "<h3>Visual Regression in CI</h3>"
            + "<p>Tools like <strong>Chromatic</strong>, <strong>Percy</strong>, or Playwright's snapshot mode catch "
            + "unintentional design shifts when a class string changes. A design system without visual-regression CI "
            + "erodes silently.</p>"
            + "<h3>Container Queries</h3>"
            + "<p>For truly component-local responsiveness (card adapts to its wrapper, not the viewport):</p>"
            + "<pre><code>&lt;div class=\"@container\"&gt;\n"
            + "  &lt;div class=\"@md:grid-cols-2 grid\"&gt;...&lt;/div&gt;\n"
            + "&lt;/div&gt;</code></pre>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "An archmage finishes what an apprentice leaves half-done. RTL. Print. Email. "
                  + "Visual regression CI. These are the tests of a <em>complete</em> system — and the reason teams trust your work."),
                e("Directional utility that respects RTL",
                  "&lt;span class=\"ms-4\"&gt;Reads right in RTL, left in LTR.&lt;/span&gt;")
            ),

            // Practice brief
            "<p>Build a production-aware card. On <code>.shell</code> apply: "
            + "<code>ms-4</code>, <code>me-4</code>, <code>p-6</code>, <code>rounded-xl</code>, "
            + "<code>shadow-md</code>, <code>print:shadow-none</code>.</p>",

            // Starter
            "<div class=\"shell bg-white\">\n"
            + "  <h3>Ready for the real world</h3>\n"
            + "  <p>RTL, print, visual regression — all accounted for.</p>\n"
            + "</div>",

            // Tests
            tests(
                twTest("RTL-safe start margin", ".shell", "ms-4"),
                twTest("RTL-safe end margin", ".shell", "me-4"),
                twTest("Padding", ".shell", "p-6"),
                twTest("Radius", ".shell", "rounded-xl"),
                twTest("Screen shadow", ".shell", "shadow-md"),
                twTest("Print drops the shadow", ".shell", "print:shadow-none")
            ),

            "Pick one — RTL, print, email, or visual-regression CI — and explain why a design system ignoring it will quietly rot over time.",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c6", QuestionTier.RECALL,
            "<p>Which class is the RTL-safe replacement for <code>ml-4</code>?</p>",
            new String[]{"ms-4", "mr-4", "mx-4", "margin-4"},
            "ms-4",
            "<p><code>ms-*</code> (margin-inline-start) flips correctly between LTR and RTL.</p>");

        mcQuestion("tw-c6", QuestionTier.APPLICATION,
            "<p>Which utility removes a drop shadow only on printed output?</p>",
            new String[]{"print:shadow-none", "shadow-print", "media-print:shadow-none", "no-print-shadow"},
            "print:shadow-none",
            "<p>Tailwind's <code>print:</code> variant targets the print media query.</p>");

        scenarioQuestion("tw-c6",
            "<p>A team ships weekly without visual regression tests. After three months, a subtle spacing drift across the product has made the UI look inconsistent. What's the fix going forward?</p>",
            "Add a visual regression step to CI (Chromatic, Percy, or Playwright snapshots) so unintended design shifts fail the build.",
            "<p>Design systems rot silently without visual-regression CI — small class-string changes accumulate. Automated snapshots catch this at review time.</p>",
            "tw-b6");
    }
}
