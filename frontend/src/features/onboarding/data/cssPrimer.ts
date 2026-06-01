// Static CSS/HTML primer content — imported directly by CssPrimerPage and PrerequisiteCheckPage.
// No backend seeder needed; this is reference material, not spaced-repetition content.

export interface PrimerQuestion {
  id: string
  text: string
  options: string[]
  correctIndex: number
  explanation: string
}

export interface PrimerSection {
  id: string
  title: string
  emoji: string
  explanationHtml: string
  questions: PrimerQuestion[]
}

// ── Section 1: HTML Structure ─────────────────────────────────────────────────

const HTML_SECTION: PrimerSection = {
  id: 'html',
  title: 'HTML: The Skeleton of the Web',
  emoji: '📄',
  explanationHtml: `
<h2>What HTML Does</h2>
<p>HTML (HyperText Markup Language) describes the <strong>structure</strong> of a page — what content exists and what role each piece plays. A browser reads your HTML and builds a tree of elements from it.</p>

<h3>Elements and Tags</h3>
<p>An element is an opening tag, optional content, and a closing tag:</p>
<pre><code>&lt;p&gt;This is a paragraph.&lt;/p&gt;
&lt;h1&gt;This is a top-level heading.&lt;/h1&gt;
&lt;button&gt;Click me&lt;/button&gt;</code></pre>
<p>Some elements are <em>self-closing</em> — they have no inner content:</p>
<pre><code>&lt;img src="logo.png" alt="Logo" /&gt;
&lt;input type="text" placeholder="Enter name" /&gt;</code></pre>

<h3>Attributes</h3>
<p>Attributes modify an element's behaviour. They go inside the opening tag as <code>name="value"</code> pairs:</p>
<pre><code>&lt;a href="https://example.com"&gt;Visit&lt;/a&gt;   &lt;!-- href: destination URL --&gt;
&lt;img src="cat.jpg" alt="A cat" /&gt;          &lt;!-- src: image path; alt: screen-reader text --&gt;
&lt;input type="email" required /&gt;            &lt;!-- type: field behaviour; required: validation --&gt;</code></pre>

<h3>class and id</h3>
<p>Two attributes you'll use constantly in CSS and JavaScript:</p>
<ul>
  <li><code>class</code> — assigns one or more reusable style names to an element. Multiple elements can share the same class.</li>
  <li><code>id</code> — a unique identifier for a single element on the page.</li>
</ul>
<pre><code>&lt;div class="card"&gt;…&lt;/div&gt;
&lt;div class="card featured"&gt;…&lt;/div&gt;   &lt;!-- two classes: card AND featured --&gt;
&lt;nav id="main-nav"&gt;…&lt;/nav&gt;          &lt;!-- unique; no other element has this id --&gt;</code></pre>

<h3>Nesting and the Tree</h3>
<p>Elements nest inside each other to form a <em>parent → child</em> tree. Indentation makes nesting readable:</p>
<pre><code>&lt;ul&gt;           &lt;!-- parent list --&gt;
  &lt;li&gt;Apples&lt;/li&gt;
  &lt;li&gt;Oranges&lt;/li&gt;
&lt;/ul&gt;

&lt;div class="card"&gt;
  &lt;h3&gt;Card title&lt;/h3&gt;
  &lt;p&gt;Card body text.&lt;/p&gt;
  &lt;button&gt;Read more&lt;/button&gt;
&lt;/div&gt;</code></pre>

<h3>Common Elements Quick Reference</h3>
<table style="border-collapse:collapse;width:100%;font-size:13px;">
  <thead>
    <tr style="border-bottom:1px solid var(--border)">
      <th style="text-align:left;padding:6px 10px">Element</th>
      <th style="text-align:left;padding:6px 10px">Purpose</th>
    </tr>
  </thead>
  <tbody>
    <tr><td style="padding:5px 10px"><code>div</code></td><td style="padding:5px 10px">Generic block container</td></tr>
    <tr><td style="padding:5px 10px"><code>span</code></td><td style="padding:5px 10px">Generic inline container</td></tr>
    <tr><td style="padding:5px 10px"><code>p</code></td><td style="padding:5px 10px">Paragraph</td></tr>
    <tr><td style="padding:5px 10px"><code>h1–h6</code></td><td style="padding:5px 10px">Headings (h1 largest)</td></tr>
    <tr><td style="padding:5px 10px"><code>a</code></td><td style="padding:5px 10px">Link (needs <code>href</code>)</td></tr>
    <tr><td style="padding:5px 10px"><code>img</code></td><td style="padding:5px 10px">Image (needs <code>src</code>, <code>alt</code>)</td></tr>
    <tr><td style="padding:5px 10px"><code>ul / ol / li</code></td><td style="padding:5px 10px">Lists</td></tr>
    <tr><td style="padding:5px 10px"><code>button</code></td><td style="padding:5px 10px">Clickable button</td></tr>
    <tr><td style="padding:5px 10px"><code>input</code></td><td style="padding:5px 10px">Form field</td></tr>
    <tr><td style="padding:5px 10px"><code>nav / header / main / footer / section</code></td><td style="padding:5px 10px">Semantic landmarks</td></tr>
  </tbody>
</table>
  `,
  questions: [
    {
      id: 'html-q1',
      text: 'Which attribute assigns a reusable CSS style name to an HTML element?',
      options: ['id', 'class', 'name', 'style'],
      correctIndex: 1,
      explanation: 'The class attribute assigns one or more names used by CSS to target that element. id is unique per page; style applies inline CSS directly; name is used for form fields.',
    },
    {
      id: 'html-q2',
      text: 'You want a paragraph to belong to two style groups: "card" and "highlighted". Which is correct?',
      options: [
        '<p class="card" class="highlighted">',
        '<p id="card highlighted">',
        '<p class="card highlighted">',
        '<p style="card highlighted">',
      ],
      correctIndex: 2,
      explanation: 'Multiple classes are space-separated inside a single class attribute. You cannot have two class attributes; id is for unique identifiers; style takes CSS property:value pairs, not class names.',
    },
    {
      id: 'html-q3',
      text: 'What does the alt attribute on an <img> element do?',
      options: [
        'Sets the image dimensions',
        'Provides alternative text for screen readers and when the image fails to load',
        'Links the image to another page',
        'Applies a CSS filter to the image',
      ],
      correctIndex: 1,
      explanation: 'alt text is read by screen readers (accessibility) and displayed by the browser when an image cannot be loaded. It is required for accessible HTML.',
    },
    {
      id: 'html-q4',
      text: 'Which of these correctly nests a button inside a card div?',
      options: [
        '<button><div class="card">Click</div></button>',
        '<div class="card"><button>Click</button></div>',
        '<div class="card" /><button>Click</button>',
        '<card><button>Click</button></card>',
      ],
      correctIndex: 1,
      explanation: 'The button is a child of the card div — div opens, button is inside, div closes. Self-closing div is not standard HTML. <card> is not a valid HTML element.',
    },
  ],
}

// ── Section 2: CSS Selectors & Properties ────────────────────────────────────

const CSS_BASICS_SECTION: PrimerSection = {
  id: 'css-basics',
  title: 'CSS: Selectors and Properties',
  emoji: '🖌️',
  explanationHtml: `
<h2>What CSS Does</h2>
<p>CSS (Cascading Style Sheets) controls the <strong>appearance</strong> of HTML elements — colour, size, spacing, typography, and more. A CSS rule has two parts: a <em>selector</em> (what to target) and one or more <em>declarations</em> (what to apply).</p>

<pre><code>selector {
  property: value;
  property: value;
}</code></pre>

<h3>The Three Core Selectors</h3>
<pre><code>/* 1. Element selector — targets every &lt;p&gt; */
p {
  color: #333;
}

/* 2. Class selector — targets every element with class="card" */
.card {
  background: white;
  border-radius: 8px;
}

/* 3. ID selector — targets the single element with id="hero" */
#hero {
  font-size: 48px;
}</code></pre>

<h3>Combining Selectors</h3>
<pre><code>/* Descendant: any &lt;p&gt; inside a .card */
.card p {
  font-size: 14px;
}

/* Multiple classes: element has BOTH classes */
.btn.primary {
  background: blue;
}

/* Pseudo-class: on hover */
button:hover {
  opacity: 0.85;
}</code></pre>

<h3>Common Properties</h3>
<table style="border-collapse:collapse;width:100%;font-size:13px;">
  <thead>
    <tr style="border-bottom:1px solid var(--border)">
      <th style="text-align:left;padding:6px 10px">Property</th>
      <th style="text-align:left;padding:6px 10px">Controls</th>
      <th style="text-align:left;padding:6px 10px">Example</th>
    </tr>
  </thead>
  <tbody>
    <tr><td style="padding:5px 10px"><code>color</code></td><td style="padding:5px 10px">Text colour</td><td style="padding:5px 10px"><code>color: #1a1a2e</code></td></tr>
    <tr><td style="padding:5px 10px"><code>background</code></td><td style="padding:5px 10px">Background fill</td><td style="padding:5px 10px"><code>background: #f0f4f8</code></td></tr>
    <tr><td style="padding:5px 10px"><code>font-size</code></td><td style="padding:5px 10px">Text size</td><td style="padding:5px 10px"><code>font-size: 16px</code></td></tr>
    <tr><td style="padding:5px 10px"><code>font-weight</code></td><td style="padding:5px 10px">Bold / normal</td><td style="padding:5px 10px"><code>font-weight: 700</code></td></tr>
    <tr><td style="padding:5px 10px"><code>padding</code></td><td style="padding:5px 10px">Space <em>inside</em> border</td><td style="padding:5px 10px"><code>padding: 16px</code></td></tr>
    <tr><td style="padding:5px 10px"><code>margin</code></td><td style="padding:5px 10px">Space <em>outside</em> border</td><td style="padding:5px 10px"><code>margin: 0 auto</code></td></tr>
    <tr><td style="padding:5px 10px"><code>border</code></td><td style="padding:5px 10px">Element outline</td><td style="padding:5px 10px"><code>border: 1px solid #ccc</code></td></tr>
    <tr><td style="padding:5px 10px"><code>border-radius</code></td><td style="padding:5px 10px">Rounded corners</td><td style="padding:5px 10px"><code>border-radius: 8px</code></td></tr>
    <tr><td style="padding:5px 10px"><code>width / height</code></td><td style="padding:5px 10px">Dimensions</td><td style="padding:5px 10px"><code>width: 100%</code></td></tr>
    <tr><td style="padding:5px 10px"><code>opacity</code></td><td style="padding:5px 10px">Transparency (0–1)</td><td style="padding:5px 10px"><code>opacity: 0.6</code></td></tr>
  </tbody>
</table>

<h3>The Cascade — Why It's Called That</h3>
<p>When multiple rules target the same element, CSS resolves conflicts with specificity:</p>
<ol>
  <li><strong>Inline styles</strong> (<code>style="…"</code>) — highest specificity</li>
  <li><strong>ID selectors</strong> (<code>#foo</code>) — very specific</li>
  <li><strong>Class selectors</strong> (<code>.bar</code>) — moderately specific</li>
  <li><strong>Element selectors</strong> (<code>p</code>) — least specific</li>
</ol>
<p>When specificity ties, the <em>last rule in the file wins</em>.</p>
  `,
  questions: [
    {
      id: 'css-q1',
      text: 'Which CSS selector targets every element with class="highlight"?',
      options: ['#highlight', '.highlight', 'highlight', 'element.highlight'],
      correctIndex: 1,
      explanation: 'Class selectors use a dot prefix: .highlight. # is for IDs. "highlight" alone would target an element type named highlight (which doesn\'t exist in HTML). element.highlight would need a real element name before the dot.',
    },
    {
      id: 'css-q2',
      text: 'Given <p class="intro"> — which rule applies a font size to it?',
      options: [
        '#intro { font-size: 18px; }',
        '.intro { font-size: 18px; }',
        'intro { font-size: 18px; }',
        '.p { font-size: 18px; }',
      ],
      correctIndex: 1,
      explanation: 'The element has a class of "intro", so the class selector .intro applies. #intro would need id="intro". intro without a prefix is an element selector. .p would target elements with class="p".',
    },
    {
      id: 'css-q3',
      text: 'What does the CSS rule .card p { color: gray; } target?',
      options: [
        'Any element with class "card p"',
        'Every <p> element that is a descendant of an element with class "card"',
        'Only <p> elements that are direct children of a .card element',
        'Any element with class "card" that is inside a <p>',
      ],
      correctIndex: 1,
      explanation: 'A space between selectors means "descendant" — the second element anywhere inside the first. .card p means "any <p> inside .card at any nesting depth". The > symbol would limit it to direct children only.',
    },
    {
      id: 'css-q4',
      text: 'Two rules both target the same <h2>: one with element selector h2 { color: red } and one with class selector .title { color: blue }. The h2 has class="title". What colour is the heading?',
      options: [
        'Red — element selectors always win',
        'Blue — class selectors have higher specificity than element selectors',
        'Purple — the colours blend',
        'Red — the first rule defined wins',
      ],
      correctIndex: 1,
      explanation: 'Class selectors (.title) have higher specificity than element selectors (h2). When both match the same element, the more specific rule wins — regardless of order in the file.',
    },
  ],
}

// ── Section 3: Box Model & Flexbox ───────────────────────────────────────────

const CSS_LAYOUT_SECTION: PrimerSection = {
  id: 'css-layout',
  title: 'The Box Model and Flexbox',
  emoji: '📦',
  explanationHtml: `
<h2>The CSS Box Model</h2>
<p>Every HTML element is rendered as a rectangular box. The box has four layers, from inside out:</p>

<div style="text-align:center;margin:20px 0;font-size:13px;">
  <div style="display:inline-block;background:#f1f5f9;border:2px dashed #94a3b8;padding:20px;border-radius:8px;">
    <div style="background:#e2e8f0;border:1px dashed #64748b;padding:12px;border-radius:4px;color:#475569;font-weight:600;">
      margin (outside — pushes neighbours away)
      <div style="background:#cbd5e1;margin:8px 0;padding:10px;border-radius:4px;color:#334155;">
        border (the visible edge)
        <div style="background:#94a3b8;padding:8px;border-radius:4px;color:#1e293b;">
          padding (inside — between border and content)
          <div style="background:#64748b;padding:6px;border-radius:4px;color:white;">
            content (text, images, children)
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<pre><code>.card {
  padding: 20px;       /* space inside: content is 20px away from the border */
  border: 1px solid #ccc;
  margin: 16px;        /* space outside: neighbours are 16px away */
}</code></pre>

<h3>display: block vs inline</h3>
<ul>
  <li><strong>block</strong> — takes up the full width of its container, starts on a new line (<code>div</code>, <code>p</code>, <code>h1</code>, <code>section</code>)</li>
  <li><strong>inline</strong> — only as wide as its content, flows in line with text (<code>span</code>, <code>a</code>, <code>strong</code>)</li>
  <li><strong>inline-block</strong> — inline flow, but respects width/height/padding like a block</li>
  <li><strong>none</strong> — element is hidden and takes up no space</li>
</ul>

<h3>Flexbox: One-Dimensional Layouts</h3>
<p>Apply <code>display: flex</code> to a <em>parent</em> element. Its <em>children</em> become flex items laid out in a row (by default).</p>

<pre><code>.nav {
  display: flex;
  flex-direction: row;       /* default — left to right */
  justify-content: space-between; /* horizontal spacing */
  align-items: center;       /* vertical alignment */
  gap: 16px;                 /* space between items */
}</code></pre>

<h3>Key Flex Properties</h3>
<table style="border-collapse:collapse;width:100%;font-size:13px;">
  <thead>
    <tr style="border-bottom:1px solid var(--border)">
      <th style="text-align:left;padding:6px 10px">Property</th>
      <th style="text-align:left;padding:6px 10px">Values</th>
      <th style="text-align:left;padding:6px 10px">Effect</th>
    </tr>
  </thead>
  <tbody>
    <tr><td style="padding:5px 10px"><code>flex-direction</code></td><td style="padding:5px 10px"><code>row</code> / <code>column</code></td><td style="padding:5px 10px">Direction of main axis</td></tr>
    <tr><td style="padding:5px 10px"><code>justify-content</code></td><td style="padding:5px 10px"><code>flex-start</code> / <code>center</code> / <code>space-between</code> / <code>flex-end</code></td><td style="padding:5px 10px">Alignment along main axis</td></tr>
    <tr><td style="padding:5px 10px"><code>align-items</code></td><td style="padding:5px 10px"><code>flex-start</code> / <code>center</code> / <code>stretch</code></td><td style="padding:5px 10px">Alignment along cross axis</td></tr>
    <tr><td style="padding:5px 10px"><code>gap</code></td><td style="padding:5px 10px"><code>8px</code> / <code>1rem</code> / etc.</td><td style="padding:5px 10px">Space between flex children</td></tr>
    <tr><td style="padding:5px 10px"><code>flex-wrap</code></td><td style="padding:5px 10px"><code>wrap</code> / <code>nowrap</code></td><td style="padding:5px 10px">Whether items wrap to new line</td></tr>
    <tr><td style="padding:5px 10px"><code>flex: 1</code></td><td style="padding:5px 10px">(on a child)</td><td style="padding:5px 10px">Child grows to fill remaining space</td></tr>
  </tbody>
</table>

<h3>Putting It Together</h3>
<pre><code>&lt;!-- A nav bar: logo on left, links on right --&gt;
&lt;nav style="display:flex; justify-content:space-between; align-items:center; padding:0 24px;"&gt;
  &lt;span&gt;MyApp&lt;/span&gt;
  &lt;div style="display:flex; gap:16px;"&gt;
    &lt;a href="/"&gt;Home&lt;/a&gt;
    &lt;a href="/about"&gt;About&lt;/a&gt;
  &lt;/div&gt;
&lt;/nav&gt;</code></pre>

<p>This is the exact pattern Tailwind CSS utilities like <code>flex justify-between items-center gap-4</code> map to — one utility class per CSS property.</p>
  `,
  questions: [
    {
      id: 'layout-q1',
      text: 'An element has padding: 20px, border: 2px solid black, and margin: 10px. Which controls the space INSIDE the border, between the border and the content?',
      options: ['margin', 'border', 'padding', 'gap'],
      correctIndex: 2,
      explanation: 'Padding is the space inside the border — it pushes content away from the border edge. Margin is outside the border, pushing other elements away. Gap is a flexbox/grid property for spacing between children.',
    },
    {
      id: 'layout-q2',
      text: 'You want two divs side-by-side with equal spacing between them. Which CSS applies to their parent?',
      options: [
        'float: left on each child div',
        'display: flex; gap: 16px on the parent',
        'display: inline on each child div',
        'margin: auto on each child div',
      ],
      correctIndex: 1,
      explanation: 'display: flex on the parent creates a flex container, and gap sets consistent spacing between the flex children. float is an older layout technique with tricky side effects. inline on divs removes block flow but doesn\'t give you easy gap control.',
    },
    {
      id: 'layout-q3',
      text: 'A flex container has justify-content: space-between and three children. What happens?',
      options: [
        'All three children are centered in a cluster',
        'First child at start, last child at end, middle child centred — maximum space between them',
        'All children are spaced equally from the start edge',
        'Children are stacked in a column with space between rows',
      ],
      correctIndex: 1,
      explanation: 'space-between distributes items so the first is at the start, the last is at the end, and remaining space is evenly divided between them. It\'s the classic "logo left, nav right" pattern.',
    },
    {
      id: 'layout-q4',
      text: 'What does display: none do to an element?',
      options: [
        'Makes it transparent but keeps its space in the layout',
        'Removes it from the layout entirely — it takes up no space and is invisible',
        'Makes it an inline element',
        'Hides it until the user hovers over it',
      ],
      correctIndex: 1,
      explanation: 'display: none removes the element from the render tree — no space is reserved and it is invisible. visibility: hidden is what makes something transparent while preserving its space. display: none is commonly used to conditionally show/hide UI elements.',
    },
  ],
}

export const CSS_PRIMER_SECTIONS: PrimerSection[] = [
  HTML_SECTION,
  CSS_BASICS_SECTION,
  CSS_LAYOUT_SECTION,
]

// ── Quick Quiz (used by PrerequisiteCheckPage) ────────────────────────────────
// 5 questions drawn from the primer — one per major concept.

export const PREREQ_QUIZ_QUESTIONS: PrimerQuestion[] = [
  {
    id: 'quiz-1',
    text: 'Which HTML attribute assigns a reusable CSS style name to an element?',
    options: ['id', 'class', 'name', 'style'],
    correctIndex: 1,
    explanation: 'class assigns reusable style names. id is unique per page. style applies inline CSS. name is for form fields.',
  },
  {
    id: 'quiz-2',
    text: 'Which CSS selector targets every element with class="highlight"?',
    options: ['#highlight', '.highlight', 'highlight', '*highlight'],
    correctIndex: 1,
    explanation: 'Class selectors use a dot prefix: .highlight. # is for ID selectors.',
  },
  {
    id: 'quiz-3',
    text: 'Which CSS property adds space INSIDE an element\'s border (between border and content)?',
    options: ['margin', 'padding', 'gap', 'border-spacing'],
    correctIndex: 1,
    explanation: 'padding is inside the border. margin is outside. gap spaces flex/grid children.',
  },
  {
    id: 'quiz-4',
    text: 'You apply display: flex to a parent div. What changes about its children?',
    options: [
      'They become invisible',
      'They lay out in a row (by default) as flex items',
      'They each take up 100% width',
      'They inherit the parent\'s font size',
    ],
    correctIndex: 1,
    explanation: 'display: flex turns the parent into a flex container. Its direct children become flex items and lay out horizontally by default.',
  },
  {
    id: 'quiz-5',
    text: 'What does margin: 0 auto do on a block element with a fixed width?',
    options: [
      'Removes all padding',
      'Centers the element horizontally within its parent',
      'Makes the element\'s height automatic',
      'Sets margin to the browser default',
    ],
    correctIndex: 1,
    explanation: 'margin: 0 auto sets top/bottom margin to 0 and left/right to "auto" — the browser splits the remaining space equally on both sides, centering the element.',
  },
]

// Topics that require the CSS prerequisite check before the main diagnostic
export const CSS_PREREQ_TOPICS = new Set(['tailwind', 'react'])

// localStorage key for tracking completion per user+topic
export function prereqStorageKey(userId: string, domainId: string): string {
  return `arcane-prereq-${userId}-${domainId}`
}

export type PrereqStatus = 'passed-knowledge' | 'passed-quiz' | 'completed-primer' | 'skipped'
