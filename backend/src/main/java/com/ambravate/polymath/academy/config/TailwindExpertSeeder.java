package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Tailwind CSS — Chunk TW-C: Expert tier
 *
 * Design tokens, building a design system, accessibility,
 * performance, large-application architecture, and real-world UI patterns.
 *
 * Prereq: TW-B Practitioner.
 */
@Deprecated
@Profile("never")
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

        Chunk twc = Chunk.builder()
                .id("tw-c")
                .title("Expert Tailwind")
                .glyph("\uD83C\uDFDB\uFE0F")
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

    // ── TW-C1: Design Tokens ─────────────────────────────────────────────────

    private void seedTwC1() {
        subChunk(
            "tw-c1", "tw-c", "Design Tokens", 1, 90, "index.html",

            // Hook
            "<p>Out of the box, Tailwind's palette is named by colour \u2014 <code>blue-500</code>, <code>purple-700</code>. "
            + "In a real product you need names by <em>intent</em>: <code>brand</code>, <code>surface</code>, <code>danger</code>. "
            + "That shift is what separates a hobby project from a system that can survive a rebrand.</p>",

            // Explanation — story-driven dialogue with Archmage Solomon
            "<p>The Archmage's library is colder than the rest of the tower. Scrolls line the shelves in perfect alphabetical order. "
            + "Solomon doesn't look up when you enter \u2014 he is marking a token page with a silver nib.</p>"

            + "<div class=\"dialogue\"><p><strong>Archmage Solomon:</strong> \u201CAt scale, <code>blue-500</code> is a lie. "
            + "It claims the colour is blue when what you <em>mean</em> is: this is the brand. Name by meaning. "
            + "Then when the brand changes \u2014 and it will \u2014 your code does not.\u201D</p></div>"

            + "<p>He slides a parchment across the table. It is a <code>tailwind.config.js</code>.</p>"

            + "<pre><code>// tailwind.config.js\n"
            + "module.exports = {\n"
            + "  theme: {\n"
            + "    extend: {\n"
            + "      colors: {\n"
            + "        brand:   { DEFAULT: '#6D28D9', light: '#A78BFA', dark: '#4C1D95' },\n"
            + "        surface: 'var(--surface)',\n"
            + "        text:    'var(--text)',\n"
            + "        danger:  '#DC2626',\n"
            + "      },\n"
            + "      fontFamily: { display: ['Cinzel', 'serif'] },\n"
            + "      spacing:    { '18': '4.5rem' },\n"
            + "    },\n"
            + "  },\n"
            + "};</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201COnce this is in place, <code>bg-brand</code>, "
            + "<code>text-brand-light</code>, <code>bg-surface</code>, and <code>font-display</code> all become first-class utilities. "
            + "Every file in the kingdom suddenly speaks the same language.\u201D</p></div>"

            + "<p>He taps the parchment and lists the three token <em>layers</em> every serious system uses.</p>"

            + "<ol>"
            + "<li><strong>Primitive tokens</strong> \u2014 raw values. <code>purple-700</code>, <code>4rem</code>, <code>14px</code>. "
            + "Powerful, but nothing about them describes <em>why</em>.</li>"
            + "<li><strong>Semantic tokens</strong> \u2014 intent. <code>brand</code>, <code>surface</code>, <code>danger</code>, "
            + "<code>text-muted</code>. These are what components should consume.</li>"
            + "<li><strong>Component tokens</strong> \u2014 per-component overrides. <code>button-primary-bg</code>, "
            + "<code>input-border</code>. Use sparingly; most of the time semantic is enough.</li>"
            + "</ol>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CThe rule is simple. A <code>&lt;Button&gt;</code> component "
            + "should never reach for a primitive. It should ask for <code>brand</code>, or <code>danger</code>, "
            + "and trust the tokens to resolve to something sensible. When a primitive leaks into a component, your design system has already broken.\u201D</p></div>"

            + "<p>He gestures to a second scroll \u2014 a custom plugin that introduces a utility the core library never shipped.</p>"

            + "<pre><code>// tailwind.config.js\n"
            + "const plugin = require('tailwindcss/plugin');\n"
            + "\n"
            + "plugins: [\n"
            + "  plugin(({ addUtilities }) =&gt; {\n"
            + "    addUtilities({\n"
            + "      '.text-balance': { 'text-wrap': 'balance' },\n"
            + "      '.text-pretty':  { 'text-wrap': 'pretty'  },\n"
            + "    });\n"
            + "  }),\n"
            + "];</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CPlugins are how you ship system-wide utilities that don't exist in core. "
            + "If your team writes the same arbitrary value three times, make it a utility. If your team writes the same component three times, "
            + "make <em>that</em> a utility \u2014 but we will speak of components next.\u201D</p></div>",

            // Story beats
            story(
                n("You've been summoned to the Archmage's library. Towering shelves hold the Academy's "
                  + "<em>design constitution</em> \u2014 tokens that every spell in the kingdom must honour."),
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "Name by meaning, not by appearance. When the palette changes, code that speaks in meaning survives. "
                  + "Code that speaks in <code>bg-blue-500</code> must be rewritten."),
                e("Semantic config",
                  "colors: { brand: { DEFAULT: '#6D28D9', light: '#A78BFA' } }")
            ),

            // Practice brief
            "<p>Assume the config extends colours so that <code>bg-brand</code> and <code>text-brand-light</code> are valid. "
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
            "<p><code>theme.extend.colors</code> is the canonical place \u2014 it generates all matching utilities automatically.</p>");

        mcQuestion("tw-c1", QuestionTier.APPLICATION,
            "<p>Which token layer should a reusable <code>&lt;Button&gt;</code> component reference directly?</p>",
            new String[]{"Semantic tokens", "Primitive colour values", "Hex codes", "Inline styles"},
            "Semantic tokens",
            "<p>Semantic tokens (brand, surface, danger) decouple components from raw palette values so rebrands and new themes stay cheap.</p>");

        scenarioQuestion("tw-c1",
            "<p>A team spreads <code>bg-purple-700</code> across 400 files. Marketing changes the brand colour. What's the fix, and what would have prevented it?</p>",
            "They must find-and-replace 400 files. A semantic token like bg-brand in theme.extend would have made it a one-line change.",
            "<p>Colour-named classes tie code to a palette. Semantic tokens tie code to intent \u2014 which is what actually persists across rebrands.</p>");
    }

    // ── TW-C2: Building a Design System ─────────────────────────────────────

    private void seedTwC2() {
        subChunk(
            "tw-c2", "tw-c", "Building a Design System", 2, 100, "index.html",

            // Hook
            "<p>Tokens are the vocabulary. A <strong>design system</strong> is the grammar \u2014 the components, variants, "
            + "and patterns that make a thousand pages feel like one product. In this chapter Solomon shows you how to "
            + "turn Tailwind into a team-scale authoring layer.</p>",

            // Explanation — continuous narrative, introduces Master Loras (component architect)
            "<p>The library doors open again. A second figure has joined Solomon at the workbench: a thin woman in a leather apron, "
            + "sleeves rolled, chalk on her fingers. Solomon inclines his head.</p>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CThis is <strong>Master Loras</strong>. "
            + "She builds the components every team in the Academy uses. If I draw up the treaty, she enforces it.\u201D</p></div>"

            + "<div class=\"dialogue\"><p><strong>Master Loras:</strong> \u201CA design system is not a folder of components. "
            + "It is the <em>set of decisions a team has already made</em> so that no one has to re-decide them on Tuesday at 4pm. "
            + "Let me show you the three tools you'll reach for in every real React codebase.\u201D</p></div>"

            + "<p>She chalks the first on the workbench slate.</p>"

            + "<p><strong>1. <code>clsx</code> \u2014 conditional class strings.</strong></p>"

            + "<pre><code>import clsx from 'clsx';\n"
            + "\n"
            + "&lt;button className={clsx(\n"
            + "  'px-4 py-2 rounded font-medium',\n"
            + "  isActive &amp;&amp; 'bg-brand text-white',\n"
            + "  disabled &amp;&amp; 'opacity-50 cursor-not-allowed',\n"
            + ")}&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Loras:</strong> \u201CNo more string-concatenation soup. "
            + "<code>clsx</code> accepts objects, arrays, and booleans, and hands you a clean class string.\u201D</p></div>"

            + "<p><strong>2. <code>tailwind-merge</code> \u2014 conflict resolution when classes collide.</strong></p>"

            + "<pre><code>import { twMerge } from 'tailwind-merge';\n"
            + "\n"
            + "// Component defines px-4, but a caller passes px-8:\n"
            + "const className = twMerge('px-4 py-2', props.className);\n"
            + "// twMerge('px-4 px-8')  \u2192  'px-8'    \u2713 last wins, intelligently</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Loras:</strong> \u201CWhen a component accepts a <code>className</code> prop, "
            + "conflicting utilities will pile up. Tailwind's cascade doesn't help you here \u2014 both classes are in the string, "
            + "and whichever compiled later wins. <code>twMerge</code> knows which utilities belong to the same group and keeps only the last. "
            + "Without it, callers can't reliably override a component.\u201D</p></div>"

            + "<p><strong>3. <code>class-variance-authority</code> (CVA) \u2014 typed variants declared once.</strong></p>"

            + "<pre><code>import { cva, type VariantProps } from 'class-variance-authority';\n"
            + "\n"
            + "const button = cva(\n"
            + "  'inline-flex items-center justify-center rounded font-medium transition',\n"
            + "  {\n"
            + "    variants: {\n"
            + "      intent: {\n"
            + "        primary: 'bg-brand text-white hover:bg-brand-dark',\n"
            + "        danger:  'bg-danger text-white hover:brightness-110',\n"
            + "        ghost:   'bg-transparent text-text hover:bg-surface',\n"
            + "      },\n"
            + "      size: {\n"
            + "        sm: 'px-3 py-1 text-sm',\n"
            + "        md: 'px-4 py-2',\n"
            + "        lg: 'px-6 py-3 text-lg',\n"
            + "      },\n"
            + "    },\n"
            + "    defaultVariants: { intent: 'primary', size: 'md' },\n"
            + "  }\n"
            + ");\n"
            + "\n"
            + "type ButtonProps = VariantProps&lt;typeof button&gt;;\n"
            + "\n"
            + "&lt;button className={button({ intent: 'danger', size: 'lg' })}&gt;Delete&lt;/button&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Loras:</strong> \u201CCVA is the whole variant lattice of a component in one place. "
            + "It types itself from the config \u2014 TypeScript knows <code>intent</code> is <code>'primary' | 'danger' | 'ghost'</code> "
            + "and will refuse anything else. This one pattern eliminates 80 percent of the churn in a growing UI library.\u201D</p></div>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CCompose them. <code>cva</code> declares the variants, "
            + "<code>clsx</code> layers in runtime conditions, <code>twMerge</code> resolves conflicts when callers override. "
            + "This triad is the professional authoring layer for Tailwind in React.\u201D</p></div>"

            + "<p>Loras pulls down a second slate \u2014 a minimal component library every team eventually builds.</p>"

            + "<ul>"
            + "<li><strong>Button</strong> (primary / secondary / ghost / danger \u00D7 sm/md/lg)</li>"
            + "<li><strong>Input</strong> (text / email / password with error state)</li>"
            + "<li><strong>Card</strong> (surface, title, body, footer)</li>"
            + "<li><strong>Alert</strong> (info / success / warning / danger)</li>"
            + "<li><strong>Modal / Dialog</strong> (built on Headless UI or Radix)</li>"
            + "<li><strong>Nav</strong> (top-bar, side-bar variants)</li>"
            + "<li><strong>Table</strong> (sortable columns, responsive collapse)</li>"
            + "</ul>"

            + "<div class=\"dialogue\"><p><strong>Loras:</strong> \u201CSeven primitives cover ninety percent of a product. "
            + "Build them once, tokenise them with CVA, document them with Storybook or a plain MDX page, and the rest of the team stops reinventing buttons.\u201D</p></div>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CA design system without a docs page is a private convention. "
            + "Storybook, Ladle, or even a single <code>/design</code> route in the app \u2014 pick one. Undocumented systems drift within a quarter.\u201D</p></div>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "A design system is a compact between authors and users. Tokens are the treaty. Components are the enforcement."),
                d("\uD83D\uDC69\u200D\uD83D\uDD2C", "loras", "Master Loras", "mentor",
                  "Three tools: <code>clsx</code> for conditionals, <code>twMerge</code> for overrides, <code>cva</code> for variants. Learn the triad, "
                  + "stop re-deciding the same questions every sprint."),
                e("A typed button with CVA",
                  "const button = cva('rounded font-medium', { variants: { intent: { primary: 'bg-brand text-white' } } });")
            ),

            // Practice brief
            "<p>Style a button that matches CVA's <code>primary + md</code> output. On <code>.btn</code> apply: "
            + "<code>bg-brand</code>, <code>text-white</code>, <code>rounded</code>, <code>font-medium</code>, "
            + "<code>px-4</code>, <code>py-2</code>, <code>transition</code>.</p>",

            // Starter
            "<button class=\"btn\">Submit</button>",

            // Tests
            tests(
                twTest("Brand background", ".btn", "bg-brand"),
                twTest("White text", ".btn", "text-white"),
                twTest("Rounded", ".btn", "rounded"),
                twTest("Medium weight", ".btn", "font-medium"),
                twTest("Horizontal padding", ".btn", "px-4"),
                twTest("Vertical padding", ".btn", "py-2"),
                twTest("Transitions smoothly", ".btn", "transition")
            ),

            "Why does a team that accepts <code>className</code> props need <code>tailwind-merge</code>? Walk through what breaks without it when a caller passes <code>px-8</code> to a component whose internal class string contains <code>px-4</code>.",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c2", QuestionTier.RECALL,
            "<p>What problem does <code>tailwind-merge</code> solve?</p>",
            new String[]{"Conflicts when class strings contain duplicate utilities from the same group", "Minifying CSS output", "Generating dynamic class names at runtime", "Typechecking Tailwind classes"},
            "Conflicts when class strings contain duplicate utilities from the same group",
            "<p><code>twMerge</code> understands Tailwind's utility groups and keeps the last winning class for each group.</p>");

        mcQuestion("tw-c2", QuestionTier.APPLICATION,
            "<p>Which library gives you typed variants for a component (e.g. <code>intent: 'primary' | 'danger'</code>) in one declaration?</p>",
            new String[]{"class-variance-authority (CVA)", "clsx", "tailwind-merge", "classnames"},
            "class-variance-authority (CVA)",
            "<p>CVA declares the entire variant lattice once and types itself through <code>VariantProps</code>.</p>");

        scenarioQuestion("tw-c2",
            "<p>A team hand-rolls a <code>&lt;Dialog&gt;</code> component with focus-trap, escape-to-close, and ARIA roles. Six months later an accessibility audit fails on all of them. What should they have done instead?</p>",
            "Use Headless UI or Radix UI for the unstyled, accessible primitives, and style them with Tailwind \u2014 do not reinvent dialog/menu/combobox accessibility.",
            "<p>Accessible dialog, menu, and combobox primitives are hard and well-solved. Style Headless UI or Radix with Tailwind instead of re-implementing focus management and ARIA wiring from scratch.</p>");
    }

    // ── TW-C3: Accessibility ─────────────────────────────────────────────────

    private void seedTwC3() {
        subChunk(
            "tw-c3", "tw-c", "Accessibility in Depth", 3, 90, "index.html",

            // Hook
            "<p>Tailwind will happily let you ship an inaccessible interface. It's the author's job to choose classes "
            + "that serve <em>every</em> reader \u2014 keyboard, screen-reader, low-vision, and reduced-motion users alike.</p>",

            // Explanation — continuous story with Solomon
            "<p>Solomon leads you down a spiral stair to a smaller room lit by a single lamp. A blindfolded scribe sits at the desk, "
            + "tracing a page with her fingertips. She does not look up, but her quill keeps pace with Solomon's voice as he reads aloud.</p>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CA spell that works for only some readers is a spell half-cast. "
            + "Every utility you add is a <em>choice</em> about whose experience matters. Start with four habits and you will cover ninety percent of accessibility harm.\u201D</p></div>"

            + "<p><strong>Habit one: prefer <code>focus-visible:</code> over <code>focus:</code>.</strong></p>"

            + "<pre><code>// too loud \u2014 shows a ring on every mouse click\n"
            + "&lt;button class=\"focus:ring-2 focus:ring-blue-500\"&gt;\n"
            + "\n"
            + "// just right \u2014 shows for keyboard users only\n"
            + "&lt;button class=\"focus-visible:ring-2 focus-visible:ring-blue-500\"&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201C<code>focus:</code> fires on every click. Mouse users see a ring they didn't ask for; "
            + "designers hide it; keyboard users lose it too. <code>focus-visible:</code> only appears when the browser believes the focus came from the keyboard. "
            + "Use it always.\u201D</p></div>"

            + "<p><strong>Habit two: contrast.</strong></p>"

            + "<p>WCAG AA requires <strong>4.5:1</strong> for body text and <strong>3:1</strong> for large text (18pt+ or 14pt bold). "
            + "Tailwind's 50\u2013300 shades rarely clear this on white. A quick rule of thumb:</p>"

            + "<ul>"
            + "<li><code>text-slate-500</code> on white \u2014 <em>fails</em> AA</li>"
            + "<li><code>text-slate-600</code> on white \u2014 passes AA for body</li>"
            + "<li><code>text-slate-700</code> on white \u2014 passes AAA</li>"
            + "</ul>"

            + "<p><strong>Habit three: screen-reader-only text for icon-only controls.</strong></p>"

            + "<pre><code>&lt;button aria-label=\"Close dialog\"&gt;\n"
            + "  &lt;svg aria-hidden=\"true\" /&gt;\n"
            + "  &lt;span class=\"sr-only\"&gt;Close dialog&lt;/span&gt;\n"
            + "&lt;/button&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201C<code>sr-only</code> removes the text from the screen without removing it from the accessibility tree. "
            + "The icon is decorative \u2014 <code>aria-hidden=\"true\"</code> \u2014 but the label survives for the scribe who reads with her fingers.\u201D</p></div>"

            + "<p><strong>Habit four: respect <code>prefers-reduced-motion</code>.</strong></p>"

            + "<pre><code>&lt;div class=\"motion-safe:animate-bounce motion-reduce:animate-none\"&gt;\n"
            + "&lt;div class=\"transition motion-reduce:transition-none\"&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CAnimation causes pain for readers with vestibular disorders. "
            + "<code>motion-safe:</code> and <code>motion-reduce:</code> let you opt in to motion only for users who haven't asked the system to spare them.\u201D</p></div>"

            + "<p><strong>ARIA variants.</strong> Tailwind ships <code>aria-*</code> variants so you can style from state without JavaScript class-toggling.</p>"

            + "<pre><code>&lt;button aria-expanded=\"false\"\n"
            + "  class=\"bg-surface aria-expanded:bg-brand aria-expanded:text-white\"&gt;\n"
            + "  Menu\n"
            + "&lt;/button&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CThe source of truth is the attribute. The style follows. "
            + "No React state duplicates what the DOM already knows.\u201D</p></div>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "A spell half-cast reaches half the readers. <code>focus-visible:</code>, contrast discipline, <code>sr-only</code>, and "
                  + "<code>motion-reduce:</code> \u2014 these are the spell, not its decoration."),
                e("Icon-only button that screen readers can still use",
                  "&lt;button&gt;&lt;svg aria-hidden=\"true\" /&gt;&lt;span class=\"sr-only\"&gt;Close&lt;/span&gt;&lt;/button&gt;")
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

        mcQuestion("tw-c3", QuestionTier.RECALL,
            "<p>What does the <code>sr-only</code> utility do?</p>",
            new String[]{"Visually hides content but keeps it in the accessibility tree", "Removes content from the DOM", "Hides content from screen readers", "Makes content visible only in print"},
            "Visually hides content but keeps it in the accessibility tree",
            "<p><code>sr-only</code> uses CSS that is invisible on screen while still announced by screen readers.</p>");

        mcQuestion("tw-c3", QuestionTier.APPLICATION,
            "<p>Which is the accessible minimum contrast ratio (WCAG AA) for body text?</p>",
            new String[]{"4.5:1", "3:1", "7:1", "2:1"},
            "4.5:1",
            "<p>WCAG AA requires 4.5:1 for normal-size body text; 3:1 for large text or UI chrome.</p>");

        tfQuestion("tw-c3", QuestionTier.RECALL,
            "<p><code>motion-reduce:</code> applies when the user has set <code>prefers-reduced-motion</code>.</p>",
            "True",
            "<p><code>motion-reduce:</code> maps to the <code>prefers-reduced-motion: reduce</code> media query.</p>");
    }

    // ── TW-C4: Performance & Bundle Size ────────────────────────────────────

    private void seedTwC4() {
        subChunk(
            "tw-c4", "tw-c", "Performance & Bundle Size", 4, 85, "index.html",

            // Hook
            "<p>Tailwind ships no CSS for classes you don't use \u2014 but only if your <code>content</code> globs are right. "
            + "A misconfigured project can leak megabytes of unused CSS, or worse, silently drop styles that worked in dev.</p>",

            // Explanation — continuous narrative
            "<p>Solomon climbs a ladder to the topmost shelf and pulls down a thin, well-thumbed volume titled simply <em>The Scanner</em>. "
            + "He opens to the first page.</p>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CTailwind's great virtue is that it emits nothing you do not use. "
            + "Its great trap is that it decides what you use by <em>reading your source files as text</em>. "
            + "If the scanner can't see a class string, that class does not exist.\u201D</p></div>"

            + "<p><strong>The content contract.</strong></p>"

            + "<pre><code>// tailwind.config.js\n"
            + "content: [\n"
            + "  './index.html',\n"
            + "  './src/**/*.{ts,tsx,jsx,vue,svelte}',\n"
            + "],</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CForget a path and the styles from those files vanish from production. "
            + "Too broad and the scanner walks node_modules and slows your builds. Be specific. Be complete.\u201D</p></div>"

            + "<p><strong>The dynamic-class trap.</strong></p>"

            + "<pre><code>// BAD \u2014 bg-${color}-500 never appears in source as a full string\n"
            + "const cls = `bg-${color}-500`;\n"
            + "\n"
            + "// GOOD \u2014 full class strings exist for the scanner to find\n"
            + "const palette = {\n"
            + "  red:   'bg-red-500',\n"
            + "  blue:  'bg-blue-500',\n"
            + "  green: 'bg-green-500',\n"
            + "};\n"
            + "const cls = palette[color];</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CThe symptom is always the same \u2014 works in dev, vanishes in production, "
            + "or appears at first but goes missing when you remove a similar-looking class from an unrelated file. "
            + "Always write full class strings somewhere the scanner can read them.\u201D</p></div>"

            + "<p><strong>Safelist \u2014 for the truly dynamic.</strong></p>"

            + "<pre><code>// tailwind.config.js\n"
            + "safelist: [\n"
            + "  'bg-brand', 'text-white',\n"
            + "  { pattern: /bg-(red|blue|green)-(400|500|600)/ },\n"
            + "],</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CReserve <code>safelist</code> for classes that are genuinely decided at runtime \u2014 "
            + "a theme toggle reading from a user setting, for example. Overuse it and you're back to shipping CSS you don't use.\u201D</p></div>"

            + "<p><strong>Purge and JIT.</strong> Modern Tailwind (v3+) runs JIT by default. CSS is generated <em>on demand</em> as the scanner sees classes. "
            + "The production build contains only the utilities you reference \u2014 typically 10\u201320&nbsp;KB gzipped for a real app.</p>"

            + "<p><strong>Critical CSS.</strong> With JIT, most apps don't need a separate critical-CSS pass. SSR frameworks "
            + "(Next.js, Remix) inline above-the-fold CSS automatically when configured.</p>"

            + "<p><strong>Avoiding utility bloat.</strong> If you catch yourself writing the same ten-utility combination in four files, "
            + "that's a component waiting to be born \u2014 <em>not</em> a case for <code>@apply</code>. Extract a React/Vue component; "
            + "share class strings through CVA, not through a CSS class.</p>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CPerformance is a <em>consequence</em> of discipline here, "
            + "not a separate concern. Correct content globs, no interpolated classes, components over repeated utility strings \u2014 "
            + "do these three things and the bundle takes care of itself.\u201D</p></div>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "The scanner reads your source as text. If it cannot see the full class name, the class does not exist. "
                  + "Every production bug in a Tailwind app eventually reduces to a variant of this one truth."),
                e("Safe dynamic class pattern",
                  "const palette = { red: 'bg-red-500', blue: 'bg-blue-500' }; const cls = palette[userChoice];")
            ),

            // Practice brief
            "<p>Build a production-safe banner that uses only full, static class strings the JIT scanner can see. "
            + "On the <code>.banner</code> apply: <code>bg-brand</code>, <code>text-white</code>, <code>p-4</code>, <code>rounded-md</code>, <code>shadow-sm</code>.</p>",

            // Starter
            "<div class=\"banner\">\n"
            + "  Welcome back, wizard.\n"
            + "</div>",

            // Tests
            tests(
                twTest("Brand background", ".banner", "bg-brand"),
                twTest("White text", ".banner", "text-white"),
                twTest("Padding", ".banner", "p-4"),
                twTest("Rounded", ".banner", "rounded-md"),
                twTest("Subtle shadow", ".banner", "shadow-sm")
            ),

            "Explain why <code>bg-${color}-500</code> breaks Tailwind's JIT. How would you fix it without resorting to <code>safelist</code>?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c4", QuestionTier.RECALL,
            "<p>Where does Tailwind learn which classes to emit?</p>",
            new String[]{"The content glob list in tailwind.config.js", "Automatically, by scanning every file on disk", "The package.json dependencies", "A runtime plugin"},
            "The content glob list in tailwind.config.js",
            "<p>The <code>content</code> array tells the JIT scanner which files to read for class names.</p>");

        scenarioQuestion("tw-c4",
            "<p>Styles work in development but the production build is missing background colours generated via <code>bg-${color}-500</code>. What's the cause and cleanest fix?</p>",
            "The JIT scanner can't see interpolated class fragments. Replace with a static map like { red: 'bg-red-500', blue: 'bg-blue-500' } so full class strings appear in source.",
            "<p>The scanner uses plain string matching. Interpolation hides the full class name. Mapping to full strings is cleaner than <code>safelist</code> for known values.</p>");
    }

    // ── TW-C5: Large Application Architecture ───────────────────────────────

    private void seedTwC5() {
        subChunk(
            "tw-c5", "tw-c", "Large Application Architecture", 5, 95, "index.html",

            // Hook
            "<p>Utilities scale beautifully when you have a small app and three contributors. They rot quietly when you have "
            + "fifty contributors, two hundred components, and no shared conventions. This chapter is the set of rules that keeps "
            + "a Tailwind codebase readable after its first year.</p>",

            // Explanation — continuous narrative; Solomon + Loras return
            "<p>You return to the workbench. Master Loras has pinned a single sheet of parchment to the beam above it: "
            + "<em>The Archmage's Rules for Tailwind at Scale</em>. Solomon reads them aloud, one by one.</p>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CThese aren't style preferences. "
            + "Each one is the residue of a real project that rotted when the rule was broken. Ignore them at your team's cost.\u201D</p></div>"

            + "<p><strong>Rule 1 \u2014 Components before <code>@apply</code>.</strong></p>"

            + "<pre><code>// AVOID \u2014 class abstraction in CSS\n"
            + ".btn-primary { @apply bg-brand text-white px-4 py-2 rounded; }\n"
            + "\n"
            + "// PREFER \u2014 component abstraction in your framework\n"
            + "&lt;Button intent=\"primary\"&gt;Save&lt;/Button&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Loras:</strong> \u201C<code>@apply</code> moves the class string out of the JSX \u2014 "
            + "and out of your CVA variant system, your design-token awareness, your IDE autocomplete. "
            + "Use <code>@apply</code> only for unavoidable global targets: form-reset, prose content, third-party widgets you can't wrap.\u201D</p></div>"

            + "<p><strong>Rule 2 \u2014 Arbitrary values are a last resort.</strong></p>"

            + "<pre><code>// AVOID \u2014 scattered arbitrary values\n"
            + "&lt;div class=\"mt-[17px] text-[#5A3D8C] w-[742px]\"&gt;\n"
            + "\n"
            + "// PREFER \u2014 extend theme, then use the utility\n"
            + "// tailwind.config.js: spacing: { '4.25': '17px' }, colors: { accent: '#5A3D8C' }\n"
            + "&lt;div class=\"mt-4.25 text-accent w-[742px]\"&gt;  // only the truly one-off value stays bracketed</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CArbitrary values are an escape hatch, not a style. "
            + "Every <code>mt-[17px]</code> is a decision the design system didn't make. "
            + "One or two are fine. Twenty are a signal that your theme needs extending.\u201D</p></div>"

            + "<p><strong>Rule 3 \u2014 Order utilities consistently.</strong></p>"

            + "<p>Adopt <code>prettier-plugin-tailwindcss</code>. It orders classes automatically by category (layout \u2192 box-model \u2192 "
            + "typography \u2192 colour \u2192 state variants). Reviewers stop arguing about class order; diffs become readable.</p>"

            + "<p><strong>Rule 4 \u2014 One source of truth for each design decision.</strong></p>"

            + "<ul>"
            + "<li>Colours live in <code>tailwind.config.js</code> \u2014 never in inline styles, never in ad-hoc CSS.</li>"
            + "<li>Spacing scale lives in the config \u2014 never as arbitrary values in markup.</li>"
            + "<li>Typography ramp lives in the config \u2014 never duplicated as custom classes.</li>"
            + "</ul>"

            + "<p><strong>Rule 5 \u2014 Co-locate component CSS.</strong></p>"

            + "<p>Tailwind classes live in the same file as the component that uses them. Don't scatter a component's styling across three locations "
            + "(JSX markup, a module CSS file, and a global <code>@apply</code> block). One file, one component, one place the styling lives.</p>"

            + "<p><strong>Rule 6 \u2014 Enforce accessibility at the component boundary.</strong></p>"

            + "<p>If <code>&lt;IconButton&gt;</code> exists, make <code>aria-label</code> a <em>required</em> prop. "
            + "If <code>&lt;Heading level={n}&gt;</code> exists, make <code>level</code> required. "
            + "The component layer is where you force good decisions, not a linter rule that devs can ignore.</p>"

            + "<p><strong>Rule 7 \u2014 Document tokens, not components first.</strong></p>"

            + "<p>Your design system's <code>/design</code> or Storybook page should open with <em>tokens</em>: colour swatches, type scale, "
            + "spacing ramp. A new contributor needs to see the vocabulary before the grammar.</p>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CThe test of a mature Tailwind codebase is simple: "
            + "can a new engineer build a feature correctly on day one without asking a single styling question? "
            + "If the answer is yes, these rules are being kept. If no, you now know where to start.\u201D</p></div>"

            + "<p><strong>A small folder layout that tends to survive.</strong></p>"

            + "<pre><code>src/\n"
            + "  ui/                  \u2190 design-system primitives\n"
            + "    Button.tsx         \u2190 CVA variants inline\n"
            + "    Input.tsx\n"
            + "    Card.tsx\n"
            + "    Alert.tsx\n"
            + "    index.ts           \u2190 barrel export\n"
            + "  features/            \u2190 product surfaces composing ui/\n"
            + "    quests/\n"
            + "    reviews/\n"
            + "  lib/\n"
            + "    cn.ts              \u2190 export const cn = (...xs) =&gt; twMerge(clsx(xs))\n"
            + "  styles/\n"
            + "    globals.css        \u2190 @tailwind directives + tiny @apply resets\n"
            + "tailwind.config.js     \u2190 tokens live here\n"
            + "design-docs/           \u2190 MDX or Storybook</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Loras:</strong> \u201COne helper you'll write on day one of every project is <code>cn</code>. "
            + "It composes <code>clsx</code> and <code>twMerge</code> so every component override resolves correctly. "
            + "Import it everywhere.\u201D</p></div>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "Seven rules. Each is the residue of a real codebase that rotted. Components before <code>@apply</code>. "
                  + "Arbitrary values sparingly. Consistent order. One source of truth. Co-locate. Enforce a11y at the boundary. Document tokens first."),
                d("\uD83D\uDC69\u200D\uD83D\uDD2C", "loras", "Master Loras", "mentor",
                  "Write <code>cn</code> on day one: <code>twMerge(clsx(xs))</code>. Use it everywhere. Your override story is now free.")
            ),

            // Practice brief
            "<p>Build a <code>.btn-primary</code> the <em>component</em> way \u2014 full utility classes on the element, no <code>@apply</code> "
            + "and no arbitrary values. Apply: <code>bg-brand</code>, <code>text-white</code>, <code>font-medium</code>, "
            + "<code>px-4</code>, <code>py-2</code>, <code>rounded</code>, <code>hover:bg-brand-dark</code>, "
            + "<code>focus-visible:ring-2</code>.</p>",

            // Starter
            "<button class=\"btn-primary\">Save changes</button>",

            // Tests
            tests(
                twTest("Brand background", ".btn-primary", "bg-brand"),
                twTest("White text", ".btn-primary", "text-white"),
                twTest("Medium weight", ".btn-primary", "font-medium"),
                twTest("Horizontal padding", ".btn-primary", "px-4"),
                twTest("Vertical padding", ".btn-primary", "py-2"),
                twTest("Rounded", ".btn-primary", "rounded"),
                twTest("Darker on hover", ".btn-primary", "hover:bg-brand-dark"),
                twTest("Keyboard focus ring", ".btn-primary", "focus-visible:ring-2")
            ),

            "A teammate proposes defining every button in the app with <code>@apply bg-brand text-white px-4 py-2 rounded</code>. Make the case against it. What do you lose, and what should you do instead?",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c5", QuestionTier.RECALL,
            "<p>When is <code>@apply</code> a reasonable choice?</p>",
            new String[]{"For unavoidable global targets (prose, form resets, third-party widgets you can't wrap)", "As the default way to define component styles", "To avoid long class strings in JSX", "To keep CSS files feeling traditional"},
            "For unavoidable global targets (prose, form resets, third-party widgets you can't wrap)",
            "<p>For component abstraction, prefer actual components with CVA variants. Reserve <code>@apply</code> for places where a component wrapper isn't possible.</p>");

        mcQuestion("tw-c5", QuestionTier.APPLICATION,
            "<p>Which tool automatically orders Tailwind classes in source files so reviewers stop arguing about diffs?</p>",
            new String[]{"prettier-plugin-tailwindcss", "eslint-plugin-tailwind", "stylelint", "postcss-sorting"},
            "prettier-plugin-tailwindcss",
            "<p>The official Prettier plugin reorders classes by category and keeps teams consistent without manual effort.</p>");

        scenarioQuestion("tw-c5",
            "<p>A codebase is full of <code>mt-[17px]</code>, <code>w-[742px]</code>, <code>text-[#5A3D8C]</code>. What does this indicate, and what should the team do?</p>",
            "It's a sign the theme isn't keeping up with real design decisions. Extend the config (spacing, colors, typography) so those values become named tokens, and reserve arbitrary values for genuine one-offs.",
            "<p>Arbitrary values are an escape hatch. When they accumulate, the design system is being bypassed. Extending the theme re-centralises those decisions.</p>");
    }

    // ── TW-C6: Real-World Patterns ──────────────────────────────────────────

    private void seedTwC6() {
        subChunk(
            "tw-c6", "tw-c", "Real-World Patterns", 6, 100, "index.html",

            // Hook
            "<p>The final test of an archmage is not knowing utilities \u2014 it's assembling them into the handful of UI patterns "
            + "every product ships: navigation, modals, dashboards, forms, tables. These are the shapes customers actually touch.</p>",

            // Explanation — continuous narrative; tour through the patterns
            "<p>Solomon leads you out of the library and onto a balcony overlooking the Academy courtyard. "
            + "From here you can see the <em>Pattern Gardens</em> \u2014 a collection of small living examples, "
            + "each a UI pattern rendered in stone and ivy so students can study it by walking through.</p>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CFive patterns will carry you through most products you ship. "
            + "Learn them well and you can reason about any new screen as a composition of these shapes.\u201D</p></div>"

            + "<p><strong>1. Responsive navigation bar.</strong></p>"

            + "<pre><code>&lt;nav class=\"flex items-center justify-between px-6 py-4 bg-surface shadow-sm\"&gt;\n"
            + "  &lt;a href=\"/\" class=\"font-display text-xl text-brand\"&gt;Arcana&lt;/a&gt;\n"
            + "\n"
            + "  &lt;ul class=\"hidden md:flex items-center gap-6 text-sm\"&gt;\n"
            + "    &lt;li&gt;&lt;a href=\"/topics\"  class=\"hover:text-brand\"&gt;Topics&lt;/a&gt;&lt;/li&gt;\n"
            + "    &lt;li&gt;&lt;a href=\"/reviews\" class=\"hover:text-brand\"&gt;Reviews&lt;/a&gt;&lt;/li&gt;\n"
            + "    &lt;li&gt;&lt;a href=\"/profile\" class=\"hover:text-brand\"&gt;Profile&lt;/a&gt;&lt;/li&gt;\n"
            + "  &lt;/ul&gt;\n"
            + "\n"
            + "  &lt;button class=\"md:hidden p-2 rounded hover:bg-surface/80\" aria-label=\"Open menu\"&gt;\u2630&lt;/button&gt;\n"
            + "&lt;/nav&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CThe desktop menu is <code>hidden md:flex</code>. "
            + "The hamburger is <code>md:hidden</code>. The nav itself is always flex, always justifying between brand and controls. "
            + "This one pattern is a hundred real navbars.\u201D</p></div>"

            + "<p><strong>2. Sidebar + content shell.</strong></p>"

            + "<pre><code>&lt;div class=\"min-h-screen grid grid-cols-1 md:grid-cols-[240px_1fr]\"&gt;\n"
            + "  &lt;aside class=\"hidden md:block bg-surface border-r p-4\"&gt;\n"
            + "    &lt;!-- nav links --&gt;\n"
            + "  &lt;/aside&gt;\n"
            + "  &lt;main class=\"p-6\"&gt;&lt;!-- page content --&gt;&lt;/main&gt;\n"
            + "&lt;/div&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CA fixed-width sidebar beside a fluid content column \u2014 "
            + "<code>grid-cols-[240px_1fr]</code>. On mobile the sidebar vanishes and the content takes the full width. "
            + "This is the skeleton of every dashboard you'll ever build.\u201D</p></div>"

            + "<p><strong>3. Modal / dialog.</strong></p>"

            + "<pre><code>&lt;!-- Overlay --&gt;\n"
            + "&lt;div class=\"fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50\"&gt;\n"
            + "  &lt;!-- Dialog --&gt;\n"
            + "  &lt;div role=\"dialog\" aria-modal=\"true\" aria-labelledby=\"dlg-title\"\n"
            + "       class=\"w-full max-w-md bg-surface rounded-xl shadow-xl p-6\"&gt;\n"
            + "    &lt;h2 id=\"dlg-title\" class=\"text-lg font-semibold\"&gt;Confirm&lt;/h2&gt;\n"
            + "    &lt;p class=\"mt-2 text-sm text-muted\"&gt;This action cannot be undone.&lt;/p&gt;\n"
            + "    &lt;div class=\"mt-6 flex justify-end gap-2\"&gt;\n"
            + "      &lt;button class=\"px-4 py-2 rounded hover:bg-surface/60\"&gt;Cancel&lt;/button&gt;\n"
            + "      &lt;button class=\"px-4 py-2 rounded bg-danger text-white hover:brightness-110\"&gt;Delete&lt;/button&gt;\n"
            + "    &lt;/div&gt;\n"
            + "  &lt;/div&gt;\n"
            + "&lt;/div&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CThe overlay is <code>fixed inset-0 bg-black/50</code> \u2014 "
            + "a full-screen darkening layer that centres the dialog. The dialog itself uses <code>max-w-md</code> so it stays readable, "
            + "<code>rounded-xl</code> and <code>shadow-xl</code> for elevation, and \u2014 crucial \u2014 <code>role=\"dialog\"</code> and "
            + "<code>aria-modal=\"true\"</code>. Use Headless UI or Radix in production; this shows you the shape.\u201D</p></div>"

            + "<p><strong>4. Card grid / dashboard stats.</strong></p>"

            + "<pre><code>&lt;section class=\"grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4\"&gt;\n"
            + "  &lt;div class=\"bg-surface rounded-xl p-6 shadow-sm\"&gt;\n"
            + "    &lt;p class=\"text-sm text-muted\"&gt;Total XP&lt;/p&gt;\n"
            + "    &lt;p class=\"mt-2 text-3xl font-semibold\"&gt;12,480&lt;/p&gt;\n"
            + "  &lt;/div&gt;\n"
            + "  &lt;!-- 3 more stat cards --&gt;\n"
            + "&lt;/section&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201COne column on phones, two on tablets, four on desktop. "
            + "Utility rows for label and value. This is ninety percent of every dashboard shipped in the last decade.\u201D</p></div>"

            + "<p><strong>5. Responsive data table.</strong></p>"

            + "<pre><code>&lt;!-- Table on tablet+ --&gt;\n"
            + "&lt;table class=\"hidden md:table w-full text-sm\"&gt;\n"
            + "  &lt;thead class=\"text-left text-muted border-b\"&gt;\n"
            + "    &lt;tr&gt;&lt;th class=\"py-2\"&gt;Topic&lt;/th&gt;&lt;th&gt;XP&lt;/th&gt;&lt;th&gt;Rank&lt;/th&gt;&lt;/tr&gt;\n"
            + "  &lt;/thead&gt;\n"
            + "  &lt;tbody class=\"divide-y\"&gt;\n"
            + "    &lt;tr&gt;&lt;td class=\"py-2\"&gt;Java&lt;/td&gt;&lt;td&gt;8,100&lt;/td&gt;&lt;td&gt;Mage&lt;/td&gt;&lt;/tr&gt;\n"
            + "  &lt;/tbody&gt;\n"
            + "&lt;/table&gt;\n"
            + "\n"
            + "&lt;!-- Card list on mobile --&gt;\n"
            + "&lt;ul class=\"md:hidden space-y-3\"&gt;\n"
            + "  &lt;li class=\"bg-surface rounded-lg p-4\"&gt;\n"
            + "    &lt;p class=\"font-medium\"&gt;Java&lt;/p&gt;\n"
            + "    &lt;p class=\"text-sm text-muted\"&gt;8,100 XP \u00b7 Mage&lt;/p&gt;\n"
            + "  &lt;/li&gt;\n"
            + "&lt;/ul&gt;</code></pre>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CTables collapse poorly on phones. "
            + "Render a table <code>hidden md:table</code> and a card list <code>md:hidden</code>. "
            + "Two views, one dataset \u2014 your mobile experience stops being a zoomed-out spreadsheet.\u201D</p></div>"

            + "<p>Solomon turns from the balcony.</p>"

            + "<div class=\"dialogue\"><p><strong>Solomon:</strong> \u201CEvery product you ship is a recombination of these five shapes, "
            + "plus the forms you already know and the components you've already built. "
            + "You are no longer an apprentice.\u201D</p></div>",

            // Story
            story(
                d("\uD83E\uDDD9\u200D\u2642\uFE0F", "archmage", "Archmage Solomon", "mentor",
                  "Five patterns: responsive nav, sidebar shell, modal, card grid, responsive table. "
                  + "Every product is a recombination of these shapes. Learn them and you can read any screen."),
                e("Responsive stat grid",
                  "&lt;section class=\"grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4\"&gt;...&lt;/section&gt;")
            ),

            // Practice brief
            "<p>Build a stat card that fits inside a dashboard grid. On <code>.stat</code> apply: "
            + "<code>bg-surface</code>, <code>rounded-xl</code>, <code>p-6</code>, <code>shadow-sm</code>, <code>flex</code>, "
            + "<code>flex-col</code>, <code>gap-2</code>.</p>",

            // Starter
            "<article class=\"stat\">\n"
            + "  <p class=\"text-sm text-muted\">Total XP</p>\n"
            + "  <p class=\"text-3xl font-semibold\">12,480</p>\n"
            + "</article>",

            // Tests
            tests(
                twTest("Surface background", ".stat", "bg-surface"),
                twTest("Large radius", ".stat", "rounded-xl"),
                twTest("Padding", ".stat", "p-6"),
                twTest("Subtle shadow", ".stat", "shadow-sm"),
                twTest("Flex layout", ".stat", "flex"),
                twTest("Stacks vertically", ".stat", "flex-col"),
                twTest("Internal spacing", ".stat", "gap-2")
            ),

            "Pick one pattern \u2014 nav, sidebar, modal, card grid, or responsive table. Explain the responsive decision it hinges on "
            + "(which utility switches behaviour at which breakpoint, and why).",

            SubChunkPracticeType.TAILWIND
        );

        mcQuestion("tw-c6", QuestionTier.RECALL,
            "<p>Which pair of classes is idiomatic for a hamburger menu that appears only on small screens?</p>",
            new String[]{"md:hidden on the button, hidden md:flex on the desktop menu", "sm:block on the button, lg:hidden on the menu", "flex md:none on both", "mobile: and desktop: variants"},
            "md:hidden on the button, hidden md:flex on the desktop menu",
            "<p>Mobile-first: start hidden on the desktop menu, reveal with <code>md:flex</code>; start visible on the hamburger, hide with <code>md:hidden</code>.</p>");

        mcQuestion("tw-c6", QuestionTier.APPLICATION,
            "<p>Which utility class creates the darkened, full-screen overlay behind a modal dialog?</p>",
            new String[]{"fixed inset-0 bg-black/50", "absolute top-0 bg-black", "fixed bg-surface opacity-50", "sticky inset-0 bg-black"},
            "fixed inset-0 bg-black/50",
            "<p><code>fixed inset-0</code> pins to all four edges of the viewport; <code>bg-black/50</code> is a 50%-opacity black overlay.</p>");

        scenarioQuestion("tw-c6",
            "<p>A responsive dashboard ships with a <code>&lt;table&gt;</code> of user stats. On mobile it scrolls horizontally and users complain. What's the idiomatic Tailwind fix?</p>",
            "Render a <table class='hidden md:table'> for tablet and up, and a <ul class='md:hidden'> card list for mobile, backed by the same dataset.",
            "<p>Tables rarely collapse well. The two-view pattern \u2014 table on wide screens, cards on narrow \u2014 gives mobile users a native-feeling list without losing the desktop table.</p>");
    }
}
