const fs = require('fs');

const chunk = {
  id: "rx-a",
  title: "React Foundation — The Component Forge",
  glyph: "⚛",
  sortOrder: 1,
  tier: "FOUNDATION",
  topicId: "react",
  prerequisites: [],
  subChunks: [

    // ── RX-A1: The Component Model ───────────────────────────────────────
    {
      id: "rx-a1",
      title: "The Component Model",
      sortOrder: 1,
      xpReward: 75,
      practiceType: "REACT",
      filename: "App.jsx",
      hookHtml: "<p>The web started with static documents — HTML files that looked the same for every visitor. Then JavaScript let us change things dynamically, but that meant hunting through the page to find elements and update them by hand. The bigger the page, the worse it got.</p><p>React's answer: describe <em>what the UI should look like given your data</em>, and let React figure out how to update the page. The tool for this is the <strong>component</strong> — a reusable, composable function that returns a piece of UI.</p>",
      explanationHtml: `<p>Vex the Interface Weaver's workshop smells of old parchment and new electricity. Spell-diagrams cover the walls — not of fire or ice, but of <em>interfaces</em>: boxes within boxes, arrows showing data flow.</p>

<div class="dialogue mentor"><p><strong>Vex the Interface Weaver:</strong> "Before React, building a page that could change meant writing HTML to describe the initial state, then writing JavaScript to hunt through that HTML and manually update it when data changed. Imagine rewriting a novel every time you changed one sentence. We found a better way."</p></div>

<h3>JSX — JavaScript with HTML syntax</h3>

<p>JSX looks like HTML, but it is JavaScript. A build tool (Babel or esbuild) transforms your JSX into calls to <code>React.createElement()</code>:</p>

<pre><code>// What you write:
function Greeting() {
  return &lt;h1&gt;Hello, Arcane Academy!&lt;/h1&gt;;
}

// What it compiles to (you never write this):
function Greeting() {
  return React.createElement('h1', null, 'Hello, Arcane Academy!');
}</code></pre>

<p>You write the readable version. The machine handles the translation.</p>

<h3>A component is just a function</h3>

<p>Any function that returns JSX is a React component — with two rules:</p>
<ul>
  <li>The function name must start with a <strong>capital letter</strong> (React uses this to tell your components apart from HTML tags)</li>
  <li>It must return <strong>JSX or null</strong></li>
</ul>

<pre><code>// Both of these are valid components:
function PageTitle() {
  return &lt;h1&gt;Welcome to the Academy&lt;/h1&gt;;
}

const Badge = () =&gt; &lt;span className="badge"&gt;Expert&lt;/span&gt;;</code></pre>

<p><code>&lt;div&gt;</code> is an HTML tag. <code>&lt;Div&gt;</code> would be a component named Div. Capitalisation is how React tells the difference.</p>

<h3>Four JSX rules you must know</h3>

<p><strong>1. Single root element.</strong> A component can only return one top-level element. If you need siblings, wrap them in a <code>&lt;div&gt;</code> or an empty fragment <code>&lt;&gt;…&lt;/&gt;</code>.</p>

<pre><code>// ❌ Two root elements — syntax error
function Wrong() {
  return (
    &lt;h1&gt;Title&lt;/h1&gt;
    &lt;p&gt;Subtitle&lt;/p&gt;
  );
}

// ✅ Fragment wraps both without adding a DOM node
function Right() {
  return (
    &lt;&gt;
      &lt;h1&gt;Title&lt;/h1&gt;
      &lt;p&gt;Subtitle&lt;/p&gt;
    &lt;/&gt;
  );
}</code></pre>

<p><strong>2. Self-close void tags.</strong> <code>&lt;img /&gt;</code>, <code>&lt;input /&gt;</code>, <code>&lt;br /&gt;</code> — HTML allows these unclosed; JSX does not.</p>

<p><strong>3. <code>className</code>, not <code>class</code>.</strong> <code>class</code> is a reserved word in JavaScript. In JSX, use <code>className</code>.</p>

<p><strong>4. Expressions in <code>{}</code>.</strong> Any JavaScript expression can be embedded inside JSX with curly braces.</p>

<pre><code>function UserCard() {
  const name = 'Archmage Eldrin';
  const yearsActive = 2024 - 1987;
  return (
    &lt;div className="card"&gt;
      &lt;h2&gt;{name}&lt;/h2&gt;
      &lt;p&gt;{yearsActive} years of experience&lt;/p&gt;
    &lt;/div&gt;
  );
}</code></pre>

<div class="dialogue mentor"><p><strong>Vex:</strong> "Notice the parentheses around the multi-line return. Without them, JavaScript's automatic semicolon insertion will silently return <code>undefined</code> if the opening tag isn't on the same line as <code>return</code>. Make the parens a habit."</p></div>`,
      storyBeats: [
        {
          type: "narration",
          text: "Vex the Interface Weaver's workshop is cluttered with diagrams — boxes nested inside boxes, arrows tracing paths between them. On the central workbench: a single glowing screen showing a page that keeps changing without a hand touching it."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "Before React, you wrote HTML to describe the starting state of a page, then wrote JavaScript to hunt through that HTML and mutate it when something changed. The bigger the page, the more hunting. The more hunting, the more bugs. We found a better way: describe what the UI <em>should</em> look like given your data — and let React handle the updating."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "The unit of composition is the component. A component is a function. It takes data in. It returns JSX — a description of what to render. That's it. Everything in React is built from components calling other components."
        },
        {
          type: "example",
          speaker: "Your first component",
          text: "function Greeting() {\n  return <h1>Hello, Arcane Academy!</h1>;\n}"
        }
      ],
      guidedPracticeHtml: "<p>Write a <code>Greeting</code> component that renders two elements:</p><ul><li>An <code>&lt;h1&gt;</code> containing exactly: <strong>Hello, Arcane Academy!</strong></li><li>A <code>&lt;p&gt;</code> containing exactly: <strong>Your journey begins here.</strong></li></ul><p>Both must be returned from a single component. The starter code returns <code>null</code> — replace it with the correct JSX.</p>",
      guidedPracticeStarterCode: "export default function Greeting() {\n  // TODO: return JSX with an <h1> and a <p>\n  // Wrap them in a fragment <></> or a <div>\n  return null;\n}",
      guidedPracticeTests: [
        { label: "Renders h1 with correct text", selector: "h1", requiredText: "Hello, Arcane Academy!" },
        { label: "Renders paragraph with correct text", selector: "p", requiredText: "Your journey begins here." }
      ],
      soloPracticeHtml: "<p>Build a <code>&lt;PageHeader&gt;</code> component from scratch — no scaffold. Render a <code>&lt;header&gt;</code> element containing an <code>&lt;h1&gt;</code> with <strong>The Arcane Academy</strong> and a <code>&lt;p&gt;</code> with <strong>Master many arts. Forget nothing.</strong> No props yet — hardcode the values. Focus on getting the JSX structure right.</p>",
      feynmanPrompt: "Explain JSX to someone who only knows HTML. What is it, why does it look like HTML if it isn't, and what does it actually become when the code runs? Include what happens to a JavaScript expression like <code>{user.name}</code> embedded inside JSX.",
      questions: [
        {
          id: "rxa1q1",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "What does JSX compile to when your code runs?",
          options: [
            "Calls to React.createElement()",
            "Raw HTML strings injected directly into the DOM",
            "JSON objects sent to the server",
            "CSS class declarations"
          ],
          answer: "Calls to React.createElement()",
          explanation: "JSX is syntactic sugar. A build tool (Babel, esbuild, or the React compiler) transforms every JSX element into a React.createElement() call. Writing JSX is purely a developer convenience — the browser never sees JSX directly."
        },
        {
          id: "rxa1q2",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "In JSX, which attribute replaces the HTML `class` attribute?",
          options: ["className", "classname", "class-name", "htmlClass"],
          answer: "className",
          explanation: "`class` is a reserved keyword in JavaScript (used for ES6 classes). JSX, being JavaScript, must use `className` instead. Similarly, `for` on a label element becomes `htmlFor` in JSX."
        },
        {
          id: "rxa1q3",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "React distinguishes your components from HTML tags using:",
          options: [
            "A capital first letter on the component name",
            "A special `component={}` attribute on the element",
            "Wrapping the name in double angle brackets like <<MyComp>>",
            "A React.component() wrapper function call"
          ],
          answer: "A capital first letter on the component name",
          explanation: "When React sees <div>, it renders an HTML div. When it sees <MyCard>, the capital letter tells React to look for a function named MyCard and call it. Lowercase = HTML tag. Capitalised = your component. This is the entire convention."
        },
        {
          id: "rxa1q4",
          type: "MULTIPLE_CHOICE",
          tier: "APPLICATION",
          minPath: "FOUNDATION",
          prompt: "This component has a syntax error:\n\nfunction Header() {\n  return (\n    <h1>Title</h1>\n    <p>Subtitle</p>\n  );\n}\n\nWhat are the valid fixes?",
          options: [
            "Wrap both in a fragment: <><h1>Title</h1><p>Subtitle</p></>",
            "Add a comma between the elements",
            "Return an array: return [<h1 key=\"t\">Title</h1>, <p key=\"s\">Subtitle</p>]",
            "Both A and C are valid fixes"
          ],
          answer: "Both A and C are valid fixes",
          explanation: "A component must have a single root — but that can be a fragment (<>…</>) which adds no DOM node, or an array of keyed elements. Fragment syntax is more common and readable. Both are correct; both pass JSX's single-root requirement."
        }
      ]
    },

    // ── RX-A2: Props ─────────────────────────────────────────────────────
    {
      id: "rx-a2",
      title: "Props: Feeding Components Data",
      sortOrder: 2,
      xpReward: 75,
      practiceType: "REACT",
      filename: "App.jsx",
      hookHtml: "<p>A component with hardcoded values is barely better than static HTML. Props are what make components reusable — you pass different data each time you render the component, and it produces different output. Same blueprint, different buildings.</p>",
      explanationHtml: `<p>Vex pins two card components side-by-side on the workbench. They look identical in structure — but one shows Archmage Eldrin, the other Lyra the Scribe.</p>

<div class="dialogue mentor"><p><strong>Vex:</strong> "Same component. Different props. This is the fundamental economy of React — write the shape once, fill it with data as many times as you need."</p></div>

<h3>Props are the function's arguments</h3>

<p>Props are passed as JSX attributes and received as a single object argument — conventionally destructured immediately:</p>

<pre><code>// Passing props:
&lt;ProfileCard name="Archmage Eldrin" role="Chief Architect" /&gt;

// Receiving as object (works, but verbose):
function ProfileCard(props) {
  return &lt;div&gt;{props.name} — {props.role}&lt;/div&gt;;
}

// Receiving destructured (preferred):
function ProfileCard({ name, role }) {
  return &lt;div&gt;{name} — {role}&lt;/div&gt;;
}</code></pre>

<h3>Prop types — everything is valid</h3>

<pre><code>&lt;Card
  title="Quest Complete"         {/* string — quotes work */}
  xp={150}                       {/* number — needs {} */}
  completed={true}               {/* boolean — needs {} */}
  tags={['React', 'hooks']}     {/* array — needs {} */}
  author={{ name: 'Vex' }}      {/* object — needs {} */}
  onDismiss={handleDismiss}     {/* function — needs {} */}
/&gt;

{/* Shorthand: active is equivalent to active={true} */}
&lt;Badge active /&gt;</code></pre>

<h3>Default prop values</h3>

<pre><code>function Badge({ label, colour = 'gray' }) {
  return &lt;span className={\`badge badge-\${colour}\`}&gt;{label}&lt;/span&gt;;
}
// If colour is not passed, it defaults to 'gray'</code></pre>

<h3>The children prop</h3>

<p>Content nested between a component's opening and closing tags becomes the <code>children</code> prop:</p>

<pre><code>function Card({ children }) {
  return &lt;div className="card"&gt;{children}&lt;/div&gt;;
}

// The h2 and p become Card's children:
&lt;Card&gt;
  &lt;h2&gt;Quest Log&lt;/h2&gt;
  &lt;p&gt;Three quests completed today.&lt;/p&gt;
&lt;/Card&gt;</code></pre>

<p>This is how wrapper and layout components work — they render their children without knowing what's inside them.</p>

<h3>Props are read-only</h3>

<p>A component must never modify its own props. Props flow down from parent to child — this one-way flow is what makes React apps predictable.</p>

<pre><code>// ❌ Never mutate a prop:
function Broken({ count }) {
  count = count + 1; // undefined behaviour
  return &lt;p&gt;{count}&lt;/p&gt;;
}

// ✅ Derive a new local variable instead:
function Fixed({ count }) {
  const displayCount = count + 1;
  return &lt;p&gt;{displayCount}&lt;/p&gt;;
}</code></pre>`,
      storyBeats: [
        {
          type: "narration",
          text: "Vex picks up two identical card frames and sets them side-by-side. Same structure. Different names, different faces inside."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "A component with hardcoded content is barely more useful than a static file. Props are what make components reusable. Pass different data each time you render the component; get different output. Same mould, different castings."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "Props are the function's arguments. Pass them as JSX attributes — strings use quotes, everything else uses curly braces. Receive them destructured in the function signature. They are read-only: your component describes what to render given the props, it never modifies them."
        },
        {
          type: "example",
          speaker: "Passing and destructuring props",
          text: "<ProfileCard name=\"Archmage Eldrin\" xp={4200} active />\n\nfunction ProfileCard({ name, xp, active = false }) {\n  return <div>{name} — {xp} XP</div>;\n}"
        }
      ],
      guidedPracticeHtml: "<p>Build a <code>&lt;ProfileCard&gt;</code> component that accepts three props:</p><ul><li><code>name</code> (string) — rendered in an <code>&lt;h3&gt;</code></li><li><code>role</code> (string) — rendered in a <code>&lt;p&gt;</code></li><li><code>active</code> (boolean, default <code>false</code>) — when <code>true</code>, render <code>&lt;span data-testid=\"status\"&gt;Online&lt;/span&gt;</code>; when <code>false</code>, render nothing for that span</li></ul><p>Wrap everything in <code>&lt;div className=\"profile-card\"&gt;</code>.</p>",
      guidedPracticeStarterCode: "export default function ProfileCard({ name, role, active = false }) {\n  // TODO: render name in h3, role in p\n  // TODO: render <span data-testid=\"status\">Online</span> when active is true\n  return null;\n}",
      guidedPracticeTests: [
        { label: "Renders .profile-card wrapper", selector: ".profile-card", exists: true },
        { label: "Renders name in h3", selector: "h3", requiredText: "Archmage Eldrin" },
        { label: "Renders role in p", selector: "p", requiredText: "Chief Architect" },
        { label: "Shows Online when active=true", selector: "[data-testid='status']", requiredText: "Online", props: { name: "Archmage Eldrin", role: "Chief Architect", active: true } },
        { label: "Hides status when active=false", selector: "[data-testid='status']", exists: false, props: { name: "Archmage Eldrin", role: "Chief Architect", active: false } }
      ],
      soloPracticeHtml: "<p>Build a <code>&lt;StatCard&gt;</code> component from scratch. Accept three props: <code>label</code> (string), <code>value</code> (number or string), and <code>unit</code> (string, optional — default empty string). Render the value prominently and the label + unit as supporting text. Example usage: <code>&lt;StatCard label=\"Quests completed\" value={42} unit=\"today\" /&gt;</code>.</p>",
      feynmanPrompt: "Explain React's one-way data flow to a colleague from a jQuery background. Why can't components modify their own props? What does 'data flows down' mean in practice, and why does this constraint make apps easier to debug?",
      questions: [
        {
          id: "rxa2q1",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "Which statement about React props is true?",
          options: [
            "Props are read-only — a component must never modify them",
            "Props can be modified directly as long as the change is local",
            "Props are automatically synced back to the parent when modified",
            "Props are only available in class components, not function components"
          ],
          answer: "Props are read-only — a component must never modify them",
          explanation: "Props are owned by the parent that passes them. Modifying props inside a component breaks React's one-way data flow and creates unpredictable behaviour. If you need a derived value, create a local variable. If you need to change data in response to user input, that is what state is for."
        },
        {
          id: "rxa2q2",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "What does the `children` prop contain?",
          options: [
            "The JSX content nested between a component's opening and closing tags",
            "An array of all child components registered in the app",
            "The rendered DOM children of the component's root element",
            "Props passed from child components up to their parent"
          ],
          answer: "The JSX content nested between a component's opening and closing tags",
          explanation: "When you write <Card><h2>Title</h2></Card>, React passes the <h2>Title</h2> JSX as the `children` prop to Card. The Card component renders it with {children}. This enables wrapper components that render arbitrary content without knowing what it is."
        },
        {
          id: "rxa2q3",
          type: "MULTIPLE_CHOICE",
          tier: "APPLICATION",
          minPath: "FOUNDATION",
          prompt: "You need to pass the number 42 as a prop named `score`. Which syntax is correct?",
          options: [
            "<ScoreBoard score={42} />",
            "<ScoreBoard score=\"42\" />",
            "<ScoreBoard score=42 />",
            "<ScoreBoard score=(42) />"
          ],
          answer: "<ScoreBoard score={42} />",
          explanation: "String props use quotes: score=\"high\". All other JavaScript values — numbers, booleans, arrays, objects, expressions — use curly braces: score={42}. Writing score=\"42\" passes the string \"42\", not the number 42, which would break any arithmetic that uses the prop."
        },
        {
          id: "rxa2q4",
          type: "MULTIPLE_CHOICE",
          tier: "DISCRIMINATION",
          minPath: "FOUNDATION",
          prompt: "When is the `children` prop preferable over an explicit `content` prop?",
          options: [
            "When the content is itself JSX — other components, mixed elements, or arbitrary markup",
            "When the content is a plain string that never changes",
            "Children is always preferable — explicit content props are an antipattern",
            "When performance matters — children are rendered lazily"
          ],
          answer: "When the content is itself JSX — other components, mixed elements, or arbitrary markup",
          explanation: "An explicit content=\"some text\" prop works well for simple strings. But if the content might include other components, formatted text, icons, or nested elements, children is the right choice. It allows the parent to compose arbitrary JSX inside the wrapper without the wrapper needing to declare every possible content shape."
        }
      ]
    },

    // ── RX-A3: State with useState ───────────────────────────────────────
    {
      id: "rx-a3",
      title: "State and useState",
      sortOrder: 3,
      xpReward: 100,
      practiceType: "REACT",
      filename: "App.jsx",
      hookHtml: "<p>Props let components display data. But what about data that <em>changes</em> — a counter, a typed input, a toggle? That is state: data owned by a component that, when updated, causes the component to re-render and show the new value. <code>useState</code> is the hook that gives function components memory.</p>",
      explanationHtml: `<h3>Why regular variables don't work</h3>

<p>Updating a regular variable inside a component does not cause React to re-render. The screen stays frozen:</p>

<pre><code>// ❌ This doesn't work — clicking changes the variable
//    but React never re-renders, so the display stays at 0
function BrokenCounter() {
  let count = 0;
  return (
    &lt;div&gt;
      &lt;p&gt;{count}&lt;/p&gt;
      &lt;button onClick={() =&gt; count++}&gt;+&lt;/button&gt;
    &lt;/div&gt;
  );
}</code></pre>

<h3>useState — syntax and behaviour</h3>

<pre><code>import { useState } from 'react';

function Counter() {
  const [count, setCount] = useState(0);

  return (
    &lt;div&gt;
      &lt;p data-testid="count"&gt;{count}&lt;/p&gt;
      &lt;button onClick={() =&gt; setCount(count + 1)}&gt;+&lt;/button&gt;
      &lt;button onClick={() =&gt; setCount(count - 1)}&gt;-&lt;/button&gt;
    &lt;/div&gt;
  );
}</code></pre>

<p><code>useState(0)</code> returns a two-element array. Destructuring gives them names:</p>
<ul>
  <li><code>count</code> — the current value (initially <code>0</code>)</li>
  <li><code>setCount</code> — the setter. Calling it stores the new value and schedules a re-render</li>
</ul>

<p>On re-render, <code>useState(0)</code> is called again but React returns the <em>current stored value</em>, not <code>0</code>. The initial value is only used on the very first render.</p>

<h3>The functional update pattern</h3>

<p>When the next value depends on the previous, pass a function to the setter:</p>

<pre><code>// ⚠️ May read a stale value if multiple updates are batched:
setCount(count + 1);

// ✅ Always receives the latest queued value:
setCount(prev =&gt; prev + 1);</code></pre>

<h3>Controlled inputs</h3>

<p>A controlled input has its value driven by React state:</p>

<pre><code>function NameInput() {
  const [name, setName] = useState('');

  return (
    &lt;input
      value={name}
      onChange={(e) =&gt; setName(e.target.value)}
    /&gt;
  );
}</code></pre>

<p>Every keystroke fires <code>onChange</code>, which calls <code>setName</code>, which triggers a re-render that sets the input's value. The input only ever shows what React's state contains — not what the user typed directly.</p>

<div class="dialogue mentor"><p><strong>Vex:</strong> "The controlled input pattern feels circular at first — the input updates state, state updates the input. But this is React's power: you always know exactly what the input contains, because it is just a variable. No hunting through the DOM."</p></div>

<h3>State is local and private</h3>

<p>State belongs to the component instance that declares it. Render the same component twice and each has its own independent state. A parent cannot read a child's state directly. (Sharing state between components is covered in a later lesson.)</p>`,
      storyBeats: [
        {
          type: "narration",
          text: "Vex places a brass counter on the workbench — a wheel that should click when turned. But no matter how hard you turn it, the display never changes."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "This is your first React trap: storing data in a regular variable inside a component. The variable changes when you click, but React doesn't know about it — so it never re-renders. The screen stays frozen. Props gave us data coming in. State gives the component memory of its own."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "useState returns two things: the current value and a setter function. Call the setter with the new value. React stores it, schedules a re-render, and the component runs again — this time returning the new value from useState instead of the initial one."
        },
        {
          type: "example",
          speaker: "useState — the core pattern",
          text: "const [count, setCount] = useState(0);\n\n// Calling the setter triggers a re-render:\n<button onClick={() => setCount(count + 1)}>+</button>"
        }
      ],
      guidedPracticeHtml: "<p>Build a <code>&lt;Counter&gt;</code> component with three elements:</p><ul><li><code>&lt;p data-testid=\"count\"&gt;</code> — displays the current count, starting at <code>0</code></li><li><code>&lt;button data-testid=\"increment\"&gt;+&lt;/button&gt;</code> — adds 1 to the count</li><li><code>&lt;button data-testid=\"decrement\"&gt;-&lt;/button&gt;</code> — subtracts 1 from the count</li></ul>",
      guidedPracticeStarterCode: "import { useState } from 'react';\n\nexport default function Counter() {\n  // TODO: declare a state variable for count, starting at 0\n  return (\n    <div>\n      {/* TODO: display the count in a <p data-testid=\"count\"> */}\n      {/* TODO: + button that increments */}\n      {/* TODO: - button that decrements */}\n    </div>\n  );\n}",
      guidedPracticeTests: [
        { label: "Shows initial count of 0", selector: "[data-testid='count']", requiredText: "0" },
        { label: "Clicking + shows 1", action: "click", selector: "[data-testid='increment']", then: { selector: "[data-testid='count']", requiredText: "1" } },
        { label: "Clicking - from 0 shows -1", action: "click", selector: "[data-testid='decrement']", then: { selector: "[data-testid='count']", requiredText: "-1" } }
      ],
      soloPracticeHtml: "<p>Build a <code>&lt;ToggleSwitch&gt;</code> component from scratch — no scaffold. Render a <code>&lt;button data-testid=\"toggle\"&gt;</code> whose text alternates between <strong>ON</strong> and <strong>OFF</strong> each time it is clicked. Start with ON. Choose your own state variable name and structure.</p>",
      feynmanPrompt: "Explain useState to a colleague who understands regular JavaScript variables. Why can't you just use `let count = 0` inside a component? What does useState do differently, and what specifically happens under the hood when you call the setter function?",
      questions: [
        {
          id: "rxa3q1",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "What does `useState(0)` return?",
          options: [
            "An array: [currentValue, setterFunction]",
            "The number 0 directly",
            "An object: { value: 0, set: fn }",
            "A Promise that resolves to the current state"
          ],
          answer: "An array: [currentValue, setterFunction]",
          explanation: "useState returns a two-element array. Element 0 is the current state value; element 1 is the setter function. Destructuring — const [count, setCount] = useState(0) — gives readable names to both. The naming convention is [value, setValue]."
        },
        {
          id: "rxa3q2",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "When you call a state setter (e.g. `setCount(5)`), what happens?",
          options: [
            "React stores the new value and schedules a re-render of the component",
            "The component immediately re-renders synchronously in the same call stack",
            "The new value is stored but only re-renders on the next user event",
            "React updates only the specific DOM node that renders the state value"
          ],
          answer: "React stores the new value and schedules a re-render of the component",
          explanation: "Calling a setter schedules a re-render — it is asynchronous. React may batch multiple state updates before re-rendering. On the next render, useState returns the new stored value instead of the initial one."
        },
        {
          id: "rxa3q3",
          type: "MULTIPLE_CHOICE",
          tier: "APPLICATION",
          minPath: "FOUNDATION",
          prompt: "A component has `const [name, setName] = useState('')`. A developer writes `name = 'Alice'` directly. What is the result?",
          options: [
            "A runtime TypeError: assignment to a constant variable",
            "The variable updates locally but React never re-renders",
            "React detects the mutation and re-renders automatically",
            "The change persists and is available on the next render"
          ],
          answer: "A runtime TypeError: assignment to a constant variable",
          explanation: "useState destructures with const, so direct assignment throws a TypeError. Even with let, bypassing the setter means React never knows about the change — no re-render occurs. Always use the setter function."
        },
        {
          id: "rxa3q4",
          type: "MULTIPLE_CHOICE",
          tier: "DISCRIMINATION",
          minPath: "FOUNDATION",
          prompt: "You have a Like button. Which setter call is safer and why?\n\nA: setLikes(likes + 1)\nB: setLikes(prev => prev + 1)",
          options: [
            "B — it receives the latest queued value even if multiple updates are batched in one event",
            "A — it is simpler and React always processes updates synchronously",
            "Both are identical — React guarantees `likes` always reflects the latest value",
            "A for counters; B is only needed for objects and arrays"
          ],
          answer: "B — it receives the latest queued value even if multiple updates are batched in one event",
          explanation: "When React batches multiple state updates, the `likes` value captured in a closure may be stale. The functional form prev => prev + 1 receives the most recent queued value from React's internal update queue. For a single click handler it rarely matters in practice, but the functional form avoids a class of hard-to-reproduce bugs."
        }
      ]
    },

    // ── RX-A4: Lists and Conditional Rendering ───────────────────────────
    {
      id: "rx-a4",
      title: "Lists and Conditional Rendering",
      sortOrder: 4,
      xpReward: 100,
      practiceType: "REACT",
      filename: "App.jsx",
      hookHtml: "<p>Real UIs are data-driven: a list of tasks, a feed of posts, a set of notifications. React renders arrays with <code>.map()</code> and shows or hides UI with conditional rendering — ternaries, short-circuit <code>&amp;&amp;</code>, and early returns. Combine them and you can represent any state a UI might be in.</p>",
      explanationHtml: `<h3>Rendering lists with .map()</h3>

<p>To render an array of items, transform each one into JSX with <code>.map()</code>:</p>

<pre><code>const quests = [
  { id: 1, title: 'Study JSX' },
  { id: 2, title: 'Build a component' },
  { id: 3, title: 'Learn useState' },
];

function QuestLog() {
  return (
    &lt;ul&gt;
      {quests.map(quest =&gt; (
        &lt;li key={quest.id}&gt;{quest.title}&lt;/li&gt;
      ))}
    &lt;/ul&gt;
  );
}</code></pre>

<h3>The key prop — why it matters</h3>

<p>Every element in a rendered list needs a <code>key</code> prop. React uses keys to efficiently update the DOM: it matches keys between renders to know what was added, removed, or moved.</p>

<pre><code>// ✅ Use a stable, unique ID — best choice:
{items.map(item =&gt; &lt;li key={item.id}&gt;{item.name}&lt;/li&gt;)}

// ⚠️ Array index — only safe for static lists that never reorder:
{items.map((item, i) =&gt; &lt;li key={i}&gt;{item.name}&lt;/li&gt;)}</code></pre>

<p>If you filter, reorder, or prepend items and use index as key, React may incorrectly reuse DOM nodes — causing display glitches or wrong state in child components.</p>

<h3>Three patterns for conditional rendering</h3>

<p><strong>1. Ternary — show one thing or another:</strong></p>
<pre><code>{isLoggedIn ? &lt;Dashboard /&gt; : &lt;LoginForm /&gt;}</code></pre>

<p><strong>2. &amp;&amp; short-circuit — show something or nothing:</strong></p>
<pre><code>{hasError &amp;&amp; &lt;ErrorBanner message={error} /&gt;}</code></pre>

<div class="dialogue mentor"><p><strong>Vex:</strong> "The &amp;&amp; operator has a trap. <code>{items.length &amp;&amp; &lt;List /&gt;}</code> looks right, but when <code>items.length</code> is 0, React renders the number <strong>0</strong> — because 0 is falsy but not null. Fix it: <code>{items.length &gt; 0 &amp;&amp; &lt;List /&gt;}</code>."</p></div>

<p><strong>3. Early return — skip rendering the component entirely:</strong></p>
<pre><code>function UserGreeting({ user }) {
  if (!user) return null;
  return &lt;p&gt;Welcome back, {user.name}&lt;/p&gt;;
}</code></pre>

<h3>Combining: list with empty state</h3>

<pre><code>function TaskList({ tasks }) {
  if (tasks.length === 0) {
    return &lt;p data-testid="empty"&gt;No tasks yet. Add one above.&lt;/p&gt;;
  }
  return (
    &lt;ul&gt;
      {tasks.map(task =&gt; (
        &lt;li key={task.id} className={task.done ? 'done' : ''}&gt;
          {task.text}
        &lt;/li&gt;
      ))}
    &lt;/ul&gt;
  );
}</code></pre>`,
      storyBeats: [
        {
          type: "narration",
          text: "The workbench now holds a scroll covered in quest titles — dozens of them. Vex points to it."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "Data arrives as arrays. Your job is to transform each item into JSX. .map() is the tool — the same .map() you know from JavaScript, but returning JSX elements instead of new values."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "Each rendered element needs a key prop — a stable, unique identifier. React uses keys to reconcile what changed between renders. Without stable keys, React re-renders entire lists instead of only the items that moved."
        },
        {
          type: "example",
          speaker: "List with empty state",
          text: "function QuestLog({ quests }) {\n  if (quests.length === 0) return <p>No active quests.</p>;\n  return (\n    <ul>\n      {quests.map(q => <li key={q.id}>{q.title}</li>)}\n    </ul>\n  );\n}"
        }
      ],
      guidedPracticeHtml: "<p>Build a <code>&lt;TaskList tasks /&gt;</code> component. <code>tasks</code> is an array of <code>{ id, text, done }</code> objects.</p><ul><li>If <code>tasks</code> is empty, render <code>&lt;p data-testid=\"empty\"&gt;No tasks yet.&lt;/p&gt;</code></li><li>Otherwise render a <code>&lt;ul&gt;</code> with a <code>&lt;li key={task.id}&gt;</code> for each task</li><li>Each <code>&lt;li&gt;</code> should have <code>className=\"done\"</code> when <code>task.done</code> is <code>true</code></li></ul>",
      guidedPracticeStarterCode: "export default function TaskList({ tasks }) {\n  // TODO: show empty state when tasks.length === 0\n  // TODO: otherwise render a <ul> with <li> per task\n  // TODO: add className=\"done\" to completed tasks\n  return null;\n}",
      guidedPracticeTests: [
        { label: "Shows empty message for empty array", selector: "[data-testid='empty']", requiredText: "No tasks yet.", props: { tasks: [] } },
        { label: "Renders ul for non-empty tasks", selector: "ul", exists: true, props: { tasks: [{ id: 1, text: "Study JSX", done: false }] } },
        { label: "Renders correct number of list items", selector: "li", count: 3, props: { tasks: [{ id: 1, text: "A", done: false }, { id: 2, text: "B", done: false }, { id: 3, text: "C", done: true }] } },
        { label: "Completed task has done class", selector: "li.done", exists: true, props: { tasks: [{ id: 1, text: "Done task", done: true }] } }
      ],
      soloPracticeHtml: "<p>Build a <code>&lt;FeaturedItems items /&gt;</code> component from scratch. <code>items</code> is an array of <code>{ id, name, featured }</code> objects. Render <em>only</em> items where <code>featured === true</code>. If no featured items exist (or the array is empty), show <code>&lt;p data-testid=\"none\"&gt;Nothing featured yet.&lt;/p&gt;</code>. No scaffold provided.</p>",
      feynmanPrompt: "Explain to a colleague why React requires a `key` prop on list items. What problem does it solve? What breaks specifically when you use array index as a key and the list gets filtered or reordered?",
      questions: [
        {
          id: "rxa4q1",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "What is the primary purpose of the `key` prop on list items?",
          options: [
            "It helps React identify which items changed, were added, or removed between renders",
            "It makes the item focusable for keyboard navigation",
            "It generates a unique CSS selector for styling each item",
            "It is required by JSX syntax but has no functional effect"
          ],
          answer: "It helps React identify which items changed, were added, or removed between renders",
          explanation: "React uses keys to reconcile the previous render's list with the new one. With stable keys, React can add, remove, or reorder individual items efficiently. Without keys (or with unstable keys like array indices on a reordered list), React may re-render every item even when only one changed."
        },
        {
          id: "rxa4q2",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "Using array index as a key is problematic specifically when:",
          options: [
            "The list can be reordered, filtered, or have items prepended",
            "The list contains more than 10 items",
            "The list items are strings rather than objects",
            "The list is rendered inside another list"
          ],
          answer: "The list can be reordered, filtered, or have items prepended",
          explanation: "Index-as-key breaks when list order changes. Deleting item 0 shifts every item down — React thinks item 1 became item 0 (same key, different data) and may incorrectly reuse DOM nodes, causing display glitches or wrong state in child components."
        },
        {
          id: "rxa4q3",
          type: "MULTIPLE_CHOICE",
          tier: "APPLICATION",
          minPath: "FOUNDATION",
          prompt: "What does this render when `items` is an empty array?\n\n`{items.length && <ItemList items={items} />}`",
          options: [
            "The number 0 on screen — items.length is 0, which is falsy but React still renders numbers",
            "Nothing — 0 is falsy so the && short-circuits to nothing",
            "An empty <ItemList /> component",
            "A React warning but no visible output"
          ],
          answer: "The number 0 on screen — items.length is 0, which is falsy but React still renders numbers",
          explanation: "React renders numbers. When items.length is 0, the && expression evaluates to 0 (the falsy left operand), and React renders the digit 0 on screen. Fix: use an explicit boolean — {items.length > 0 && <ItemList />} or {!!items.length && <ItemList />}."
        },
        {
          id: "rxa4q4",
          type: "MULTIPLE_CHOICE",
          tier: "DISCRIMINATION",
          minPath: "FOUNDATION",
          prompt: "When is an early return (`if (!data) return null`) preferable to a ternary inside JSX?",
          options: [
            "When the component has nothing to render in the falsy case — no need for an else branch",
            "Early returns are always preferable to ternaries for readability",
            "When the condition involves async data loading",
            "Ternaries are always preferable because they keep the render path in one place"
          ],
          answer: "When the component has nothing to render in the falsy case — no need for an else branch",
          explanation: "A ternary requires both branches: condition ? <A /> : <B />. If the falsy case is null, you get condition ? <A /> : null — verbose. An early return if (!condition) return null expresses intent clearly and keeps the main render path clean. Use ternary when both branches render meaningful UI; use early return when one branch is 'render nothing'."
        }
      ]
    },

    // ── RX-A5: Events and Forms ──────────────────────────────────────────
    {
      id: "rx-a5",
      title: "Events and Forms",
      sortOrder: 5,
      xpReward: 100,
      practiceType: "REACT",
      filename: "App.jsx",
      hookHtml: "<p>Components that display data are useful. Components that <em>respond</em> to users are what makes an application. Events and forms are the input layer — clicks, keystrokes, and submissions that drive state changes and trigger actions.</p>",
      explanationHtml: `<h3>Event handlers in React</h3>

<p>React uses camelCase event props. Pass a function <em>reference</em> — not a function call:</p>

<pre><code>&lt;!-- HTML: --&gt;
&lt;button onclick="handleClick()"&gt;Click&lt;/button&gt;

&lt;!-- JSX — function reference, not a call: --&gt;
&lt;button onClick={handleClick}&gt;Click&lt;/button&gt;

&lt;!-- Common mistake — this calls the function during render: --&gt;
&lt;button onClick={handleClick()}&gt;WRONG&lt;/button&gt;</code></pre>

<pre><code>function AlertButton() {
  function handleClick() {
    alert('Spell cast!');
  }
  return (
    &lt;&gt;
      &lt;button onClick={handleClick}&gt;Named handler&lt;/button&gt;
      &lt;button onClick={() =&gt; alert('Inline!')}&gt;Arrow function&lt;/button&gt;
    &lt;/&gt;
  );
}</code></pre>

<h3>The event object</h3>

<p>React passes a synthetic event object to your handler. The most-used property is <code>e.target.value</code> — the current value of the element that triggered the event:</p>

<pre><code>function TextInput() {
  const [value, setValue] = useState('');
  return (
    &lt;input
      value={value}
      onChange={(e) =&gt; setValue(e.target.value)}
    /&gt;
  );
}</code></pre>

<h3>Forms and e.preventDefault()</h3>

<p>By default, form submission causes a full page reload. In React, handle submissions in JavaScript:</p>

<pre><code>function SearchForm() {
  const [query, setQuery] = useState('');

  function handleSubmit(e) {
    e.preventDefault(); // Stop the page reload
    console.log('Searching for:', query);
  }

  return (
    &lt;form onSubmit={handleSubmit}&gt;
      &lt;input
        value={query}
        onChange={(e) =&gt; setQuery(e.target.value)}
        placeholder="Search..."
      /&gt;
      &lt;button type="submit"&gt;Search&lt;/button&gt;
    &lt;/form&gt;
  );
}</code></pre>

<h3>Lifting state — passing callbacks to children</h3>

<p>When a form adds items to a list and both are separate components, the parent owns the list state and passes an <code>onAdd</code> callback down:</p>

<pre><code>function App() {
  const [tasks, setTasks] = useState([]);

  function handleAdd(text) {
    setTasks(prev =&gt; [...prev, { id: Date.now(), text, done: false }]);
  }

  return (
    &lt;&gt;
      &lt;AddTaskForm onAdd={handleAdd} /&gt;
      &lt;TaskList tasks={tasks} /&gt;
    &lt;/&gt;
  );
}

function AddTaskForm({ onAdd }) {
  const [text, setText] = useState('');

  function handleSubmit(e) {
    e.preventDefault();
    if (!text.trim()) return; // Guard: reject empty submissions
    onAdd(text.trim());
    setText('');             // Clear the input after adding
  }

  return (
    &lt;form onSubmit={handleSubmit}&gt;
      &lt;input value={text} onChange={(e) =&gt; setText(e.target.value)} /&gt;
      &lt;button type="submit"&gt;Add&lt;/button&gt;
    &lt;/form&gt;
  );
}</code></pre>

<div class="dialogue mentor"><p><strong>Vex:</strong> "Two things to never forget: <code>e.preventDefault()</code> on every form submit — always — and clear the input after a successful submission. An input that doesn't clear is a broken form, every time."</p></div>`,
      storyBeats: [
        {
          type: "narration",
          text: "A parchment form and quill appear on the workbench. Vex nods towards them."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "Displaying data is the easy half. Receiving it is the other half. Clicks, keystrokes, form submissions — React handles these with event props: onClick, onChange, onSubmit. Same browser events, camelCase names."
        },
        {
          type: "dialogue",
          av: "⚗️",
          cls: "weaver",
          speaker: "Vex the Interface Weaver",
          sCls: "mentor",
          text: "Two rules. First: pass a function reference, not a call. onClick={handleClick} — not onClick={handleClick()}. Second: on form submit, always call e.preventDefault() or the browser reloads the page and everything you've built is gone."
        },
        {
          type: "example",
          speaker: "Controlled form input — the complete pattern",
          text: "const [text, setText] = useState('');\n\n<form onSubmit={handleSubmit}>\n  <input\n    value={text}\n    onChange={e => setText(e.target.value)}\n  />\n  <button type=\"submit\">Add</button>\n</form>"
        }
      ],
      guidedPracticeHtml: "<p>Build an <code>&lt;AddItemForm onAdd /&gt;</code> component. <code>onAdd</code> is a callback function. The form should:</p><ul><li>Render a <code>&lt;form&gt;</code> with a <code>&lt;input data-testid=\"input\" type=\"text\"&gt;</code> and a <code>&lt;button data-testid=\"submit\" type=\"submit\"&gt;Add&lt;/button&gt;</code></li><li>On submit: call <code>onAdd(text.trim())</code> and clear the input</li><li>If the input is empty or only whitespace, do not call <code>onAdd</code></li></ul>",
      guidedPracticeStarterCode: "import { useState } from 'react';\n\nexport default function AddItemForm({ onAdd }) {\n  const [text, setText] = useState('');\n\n  function handleSubmit(e) {\n    // TODO: prevent default\n    // TODO: guard against empty text\n    // TODO: call onAdd with trimmed text\n    // TODO: clear the input\n  }\n\n  return (\n    <form onSubmit={handleSubmit}>\n      {/* TODO: controlled input (value + onChange) */}\n      {/* TODO: submit button */}\n    </form>\n  );\n}",
      guidedPracticeTests: [
        { label: "Renders text input", selector: "[data-testid='input']", exists: true },
        { label: "Renders submit button", selector: "[data-testid='submit']", exists: true },
        { label: "Calls onAdd with typed text on submit", sequence: [{ action: "type", selector: "[data-testid='input']", value: "Learn React" }, { action: "click", selector: "[data-testid='submit']" }], onAddCalledWith: "Learn React" },
        { label: "Clears input after submit", sequence: [{ action: "type", selector: "[data-testid='input']", value: "Test" }, { action: "click", selector: "[data-testid='submit']" }], then: { selector: "[data-testid='input']", value: "" } },
        { label: "Does not call onAdd for empty input", sequence: [{ action: "click", selector: "[data-testid='submit']" }], onAddNotCalled: true }
      ],
      soloPracticeHtml: "<p>Build a complete <code>&lt;NoteApp&gt;</code> component from scratch. It manages a list of notes in state. At the top: a form with a text input and an Add button. Below: a <code>&lt;ul&gt;</code> of submitted notes as <code>&lt;li&gt;</code> elements. When there are no notes, show <code>&lt;p data-testid=\"empty\"&gt;No notes yet.&lt;/p&gt;</code>. Submitting adds to the list and clears the input. No scaffold provided — you have all the pieces from this chapter.</p>",
      feynmanPrompt: "Explain the controlled input pattern. What makes an input 'controlled' in React? What two props does it require and why? What breaks if you provide <code>value</code> without <code>onChange</code>?",
      questions: [
        {
          id: "rxa5q1",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "What is the difference between `onClick={handleClick}` and `onClick={handleClick()}`?",
          options: [
            "The first passes the function to be called on click; the second calls the function immediately during render",
            "They are identical — React normalises both forms",
            "The second is correct; the first would pass undefined",
            "The first only works for arrow functions; the second works for named functions"
          ],
          answer: "The first passes the function to be called on click; the second calls the function immediately during render",
          explanation: "onClick expects a function reference, not a return value. handleClick() calls the function right away and passes whatever it returns (often undefined) as the handler. This means your logic runs on every render, not on click. Always pass the reference: onClick={handleClick}."
        },
        {
          id: "rxa5q2",
          type: "MULTIPLE_CHOICE",
          tier: "RECALL",
          minPath: "FOUNDATION",
          prompt: "What does `e.preventDefault()` do in a form's onSubmit handler?",
          options: [
            "Stops the browser's default behaviour of sending an HTTP request and reloading the page",
            "Prevents the form from running its built-in field validation",
            "Stops the event from bubbling up to parent elements",
            "Clears all form inputs automatically"
          ],
          answer: "Stops the browser's default behaviour of sending an HTTP request and reloading the page",
          explanation: "Browsers submit forms via HTTP by default, which reloads the page. In a React SPA, you handle submissions in JavaScript. e.preventDefault() cancels the reload so your onSubmit handler runs instead."
        },
        {
          id: "rxa5q3",
          type: "MULTIPLE_CHOICE",
          tier: "APPLICATION",
          minPath: "FOUNDATION",
          prompt: "An input has `value={name}` from state but no `onChange` handler. What happens when the user types?",
          options: [
            "The input appears frozen — React overwrites the user's input with the state value on every keystroke",
            "The input works normally — value is only used as the initial value",
            "React throws a runtime error immediately",
            "The input updates visually but state doesn't change"
          ],
          answer: "The input appears frozen — React overwrites the user's input with the state value on every keystroke",
          explanation: "A controlled input's value is always what React says it is. Without onChange, the state never updates, so React keeps resetting the input to the current state value on every render. The input appears frozen. React also logs a warning: 'You provided a value prop without an onChange handler.'"
        },
        {
          id: "rxa5q4",
          type: "MULTIPLE_CHOICE",
          tier: "DISCRIMINATION",
          minPath: "FOUNDATION",
          prompt: "A form component needs to add submitted items to a list rendered by a sibling component. Where should the list state live?",
          options: [
            "In the parent of both components — it owns the state and passes props/callbacks down",
            "In the form component — it knows what's being added",
            "In the list component — it knows how to render items",
            "In a global variable — state shared between siblings should be global"
          ],
          answer: "In the parent of both components — it owns the state and passes props/callbacks down",
          explanation: "When siblings share state, lift it to their closest common ancestor. The parent holds the state, passes items to the list as a prop, and passes an onAdd callback to the form. The form calls onAdd on submit; the parent updates state; React re-renders the list. This is 'lifting state up' — React's core pattern for sibling communication before needing Context."
        }
      ]
    }
  ]
};

fs.writeFileSync(
  'D:/Coding/Real_Projects/arcane-academy/arcane-academy/backend/src/main/resources/content/react/rx-a.json',
  JSON.stringify(chunk, null, 2)
);
console.log('rx-a.json written — size:', JSON.stringify(chunk).length, 'chars');
