---
id: fe-sen-m6-02
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m6
moduleTitle: "Module 6: Advanced Accessibility"
moduleGlyph: "♿"
moduleSortOrder: 6
topicSlug: complex_interactions
topicTitle: "Complex Interactions"
topicSortOrder: 2
lesson: complex_interactions
title: "Complex Interactions"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains focus trapping in modals and how to implement it
    - Describes what ARIA live regions are and when to use them
    - Explains focus management after route changes in SPAs
    - Describes keyboard navigation patterns for menus and dialogs (ARIA APG)
    - Synthesises a systematic approach to accessible complex UI components
  keywords: [focus trap, modal, dialog, aria-modal, aria-live, polite, assertive, focus management, SPA, route change, keyboard, aria-expanded, menu, combobox, APG]
  modelAnswer: |
    Modals require focus trapping: when a modal opens, focus must move inside it and be prevented from reaching content behind it until the modal closes. On close, focus returns to the trigger element. Implementation: use a library like focus-trap-react, or manually intercept Tab/Shift+Tab to cycle within the dialog. ARIA: role='dialog', aria-modal='true', aria-labelledby pointing to the dialog title.

    ARIA live regions announce dynamic content changes to screen readers without focus movement. aria-live='polite' queues the announcement (after current speech finishes). aria-live='assertive' interrupts immediately (for critical errors). Common uses: form validation messages, toast notifications, status updates, search result counts.

    In SPAs, route changes don't reload the page — screen readers don't know the content changed. After navigation, focus management is required: move focus to the page heading or a skip-to-content link, and announce the new page title to screen readers (update document.title and use a live region).

    The ARIA Authoring Practices Guide (APG) documents keyboard patterns for complex widgets: menus (arrow keys navigate, Enter/Space activate, Escape closes), comboboxes (arrow keys move through options, Enter selects), tabs (arrow keys move between tabs), dialogs (Escape closes). Following these patterns ensures consistent, expected behaviour across applications.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A modal opens. A keyboard user presses Tab repeatedly. Focus moves through the modal content and then... continues to interactive elements behind the modal (in the dimmed page). What accessibility requirement is violated?"
    options:
      - "The modal doesn't have a close button"
      - "Focus trapping — keyboard focus must be contained within the modal while it is open"
      - "The modal background is not dark enough"
      - "Tab order is incorrect inside the modal"
    correctIndex: 1
    feedback: "Focus must be trapped inside the modal. A sighted keyboard user can see the modal boundary; a blind keyboard user navigating by Tab would not know they've left the modal. When Tab reaches the last focusable element in the modal, it should wrap back to the first. When Shift+Tab reaches the first, it should wrap to the last. On close, focus returns to the trigger."
  - type: SHORT_TEXT
    prompt: "A form shows a success message 'Your changes have been saved' that appears dynamically after submission. A screen reader user submits the form. How do you ensure they hear the confirmation?"
    hint: "What ARIA attribute makes dynamic content changes audible to screen readers?"
  - type: FILL_BLANK
    prompt: "aria-live='polite' announces ___ the current speech finishes. aria-live='assertive' ___ the current announcement."
    answer: "after; interrupts"
    hint: "Polite waits; assertive is urgent."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A user navigates in a React SPA from the Home page to the Settings page. What should happen for screen reader accessibility?"
    options:
      - "Nothing — the browser handles page change announcements automatically"
      - "Focus should move to the new page heading and document.title should update"
      - "A live region should announce 'Page loaded' automatically"
      - "The screen reader detects URL changes and announces them"
    correctIndex: 1
    feedback: "Browser-native page loads trigger a full page reload — screen readers re-read the title and begin at the top. SPA navigation is invisible to assistive technology. Best practice: update document.title, move focus to the page's main heading (or skip-to-content region), and use a visually-hidden aria-live region to announce the page title. Libraries like @reach/skip-nav and react-router provide patterns for this."
  - type: MULTIPLE_CHOICE
    question: "A dropdown menu should allow keyboard users to navigate options with arrow keys. What ARIA role and keyboard pattern does this require?"
    options:
      - "role='select' with standard Tab navigation"
      - "role='menu' with items as role='menuitem', arrow keys navigate, Enter/Space activate, Escape closes"
      - "role='list' with listitem children"
      - "No ARIA needed — native select handles keyboard navigation"
    correctIndex: 1
    feedback: "ARIA menus follow the menu interaction pattern from the APG: role='menu' container, role='menuitem' items, arrow keys navigate up/down, Enter/Space activates, Escape closes and returns focus to the trigger. Tab moves to the next focusable element outside the menu, not within it. This is the expected pattern that screen reader and keyboard users rely on."
retrieval:
  recall: "Name four types of complex interactions that require explicit focus management."
  explain: "What is a live region and when would you choose assertive vs polite?"
  mistakeId:
    code: |
      // Dialog closes on outside click
      // But focus is not returned anywhere
      function Dialog({ onClose }) {
        return (
          <div role="dialog" aria-modal="true">
            <button onClick={onClose}>Close</button>
            {/* content */}
          </div>
        );
      }
      // On close: onClose() removes the dialog from DOM
    answer: "When the dialog closes, focus is lost — it returns to the document body, nowhere useful. The user must re-navigate from the top of the page. Fix: store a ref to the trigger element before opening the dialog, then restore focus on close: triggerRef.current?.focus(). This returns the keyboard user to their position in the page. Additionally: the dialog should receive focus when it opens (move to the dialog heading or first interactive element)."
---

# Hook

A keyboard-only user opens a modal. They complete the task. They close the modal. Focus goes to the document body. They're at the top of the page. They have to Tab through 47 elements to get back to where they were.

For a sighted user, clicking anywhere resumes their position. For a keyboard user, lost focus means starting over.

Focus management isn't optional — it's the user experience.

# Lore Introduction

*"The apprentice opens the restricted archive, completes their research, and returns to the library,"* the Head Librarian explains. *"But if the portal closes without returning them to their reading table — they must walk the entire library again."*

She marks the return point on a map. *"This mark is your focus return. Mandatory. Non-negotiable."*

# Core Learning

## Concept Introduction

**Complex interactions require explicit accessibility implementation:**

**1. Modal / Dialog Focus Trapping:**
```tsx
import FocusTrap from 'focus-trap-react';

function Modal({ isOpen, onClose, triggerRef }) {
  useEffect(() => {
    if (!isOpen) {
      // Return focus to trigger on close
      triggerRef.current?.focus();
    }
  }, [isOpen]);

  if (!isOpen) return null;

  return (
    <FocusTrap>
      <div
        role="dialog"
        aria-modal="true"
        aria-labelledby="dialog-title"
      >
        <h2 id="dialog-title">Confirm Delete</h2>
        <p>This cannot be undone.</p>
        <button onClick={onClose}>Cancel</button>
        <button>Delete</button>
      </div>
    </FocusTrap>
  );
}
```

**2. ARIA Live Regions (dynamic content):**
```tsx
function StatusMessage({ message }) {
  return (
    <div
      aria-live="polite"
      aria-atomic="true"
      className="sr-only" // visually hidden, but announced by screen readers
    >
      {message}
    </div>
  );
}

// Usage: message changes → screen reader announces it
<StatusMessage message={status} />
```

**3. SPA Route Change Focus Management:**
```tsx
// After navigation, announce and focus
function RouteChangeAnnouncer() {
  const location = useLocation();
  const [announcement, setAnnouncement] = useState('');
  const headingRef = useRef(null);

  useEffect(() => {
    document.title = getCurrentPageTitle(location);
    setAnnouncement(`Navigated to ${getCurrentPageTitle(location)}`);
    headingRef.current?.focus();
  }, [location]);

  return (
    <div aria-live="polite" className="sr-only">{announcement}</div>
  );
}
```

**4. ARIA Menu Pattern (keyboard navigation):**
```tsx
// Arrow keys navigate, Enter/Space activate, Escape closes
// Follows ARIA APG Menu pattern
```

## Common Mistakes

- **Not returning focus after modal close.** Keyboard users lose their position.
- **Using aria-live='assertive' for non-urgent updates.** It interrupts — use only for errors and critical alerts.
- **Forgetting role='dialog' and aria-modal.** Without these, screen readers don't know a dialog is open — they read the full page.
- **Not testing with a real keyboard and screen reader.** Code that looks correct may still be broken in practice.

## Mini Summary

- ✔ Modals: trap focus inside + return focus to trigger on close
- ✔ ARIA live regions: polite for status updates, assertive for errors
- ✔ SPA routing: manage focus + update document.title on every route change
- ✔ Complex widgets: follow ARIA APG keyboard patterns (menu, combobox, tabs)
- ✔ Test with keyboard and screen reader — not just automated tools

# Guided Practice Quest

Work through the guided steps to practise identifying focus management issues and live region use cases.

# Solo Practice Quest

Design the complete accessible implementation for a search autocomplete (combobox) component: the input field, the dropdown of suggestions, and keyboard navigation. Reference the ARIA APG combobox pattern. Describe: ARIA roles, keyboard interactions, focus behaviour, and what screen readers announce at each stage.

# Integration

**Psychology — Mental Models and Interaction Consistency**

Keyboard and screen reader users develop mental models of how interfaces work — predictable interaction patterns that become automatic. When a modal doesn't trap focus, it violates the user's mental model (modals are closed containers). When arrow keys navigate a menu, it matches the model. When escape closes a dropdown, it matches the model. When focus isn't returned after modal close, the model is broken — the user must rebuild their spatial orientation. This is the WCAG principle of Predictability applied to complex interactions: users should be able to predict what happens based on standard patterns. ARIA APG patterns are the codification of these expectations — implement them correctly and users' mental models hold. Deviate from them and every interaction requires conscious effort.

# Lore Conclusion

*"The portal returns the apprentice to their reading table,"* the Head Librarian says, watching the modal close and focus return to the trigger button. *"Their place is preserved. Their work continues without interruption."*

---
