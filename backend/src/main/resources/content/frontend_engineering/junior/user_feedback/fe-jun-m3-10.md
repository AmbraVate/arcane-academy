---
id: fe-jun-m3-10
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m3
moduleTitle: "Module 3: Events and Forms"
moduleGlyph: "📝"
moduleSortOrder: 3
topicSlug: user_feedback
topicTitle: "User Feedback"
topicSortOrder: 4
lesson: success_states
title: "Success States"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Replaces form with a confirmation message on success"
    - "Provides specific, positive confirmation (not generic 'Success')"
    - "Uses appropriate visual indicators (colour, icon, animation)"
    - "Offers a clear next action after success"
  keywords: [success, confirmation, replace, message, positive, clear, next-action, toast, inline]
  modelAnswer: |
    Success states should be specific ("Your message has been sent — we'll reply within 24 hours"),
    replace or clearly indicate the completed action, and offer a logical next step
    (return to home, create another, view dashboard). Options: replace the form entirely
    with a confirmation panel, show an inline success banner, or display a toast notification.
    The choice depends on the importance of the action.
guidedSteps:
  - id: fe-jun-m3-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      After a checkout form submits successfully, which success UX is best?
    inputConfig:
      options:
        - "Show a green border on the submit button"
        - "Replace the form with a full confirmation page showing order details and next steps"
        - "Log 'success' to the console"
        - "Show a small 'Done' tooltip that disappears after 2 seconds"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Replace the form with a full confirmation page showing order details and next steps"]
      rejectedFeedback: "For high-stakes actions (checkout, account creation), replace the entire form with a confirmation. The user needs: confirmation the action succeeded, what happens next, order/reference details, and a clear path forward. A tooltip is appropriate for low-stakes actions."
    hint: "Match the weight of the confirmation to the weight of the action."
    reflectionPrompt: "Success UI should match the stakes. Liking a post: a heart animation. Submitting a payment: full confirmation with order number, what to expect next, and a support contact. Scale the confirmation to the action's importance."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What makes a toast notification appropriate vs inappropriate?"
    options:
      - "Toasts are always appropriate — they are non-intrusive"
      - "Appropriate for low-stakes, easily reversible actions; inappropriate for high-stakes actions needing acknowledgement"
      - "Toasts are never appropriate — always use modals"
      - "Appropriate only for errors, not success"
    correctIndex: 1
    feedback: "Toast: 'File saved', 'Link copied', 'Item added to cart' — quick, non-intrusive, auto-dismissing. Not for: 'Payment processed', 'Account deleted' — actions that require the user to see and acknowledge. Toasts disappear; important confirmations should not."

retrieval:
  recall: "Describe three different success state patterns and when to use each."
  explain: "Why should success messages be specific rather than generic ('Success')?"
  mistakeId:
    code: "Showing 'Done!' as the only success feedback for a form that submitted a support ticket"
    answer: "Too vague. Show: 'Support ticket #4821 submitted. Our team will reply within 24 hours. Check your email for confirmation.' Specific information reduces anxiety and sets expectations."
---

# Hook

"Success." A word that tells the user nothing. "Your order #AB123 has been placed — delivery expected Thursday" tells them everything. The difference is the quality of success communication.

# Lore Introduction

*"An Academy acceptance letter that says merely 'Accepted',"* says Master Aelindra, *"is less valuable than one that says 'Accepted: Term begins 1 September, Report to Hall A, bring quill.' Specificity is the gift."*

# Core Learning

## Concept Introduction

```jsx
// Pattern: replace form with confirmation
function ContactForm() {
  const [submitted, setSubmitted] = useState(false);

  if (submitted) {
    return (
      <div className="confirmation">
        <CheckCircleIcon />
        <h2>Message sent!</h2>
        <p>We'll reply to your email within 2 business days.</p>
        <button onClick={() => setSubmitted(false)}>Send another</button>
      </div>
    );
  }
  return <form onSubmit={() => setSubmitted(true)}>...</form>;
}

// Pattern: inline banner (for smaller forms)
{success && <div role="status" className="success-banner">Saved successfully</div>}

// Pattern: toast (for low-stakes)
// Typically managed by a toast library (react-hot-toast, sonner)
toast.success('Link copied to clipboard');
```

**Success message checklist:**
- [ ] States what happened ("Order placed")
- [ ] Includes a reference (order number, ticket ID)
- [ ] Sets expectations ("reply in 24 hours")
- [ ] Offers a next action ("View order" / "Return home")

## Mini Summary
- ✔ Match confirmation weight to action weight
- ✔ Replace form for high-stakes; inline/toast for low-stakes
- ✔ Specific: state what happened, reference number, next steps
- ✔ Offer a logical next action after success

# Solo Practice Quest

Build a "contact us" form. On success: replace the form with a confirmation panel showing the submitted name, a reference number (Math.random for now), expected response time, and a "Send another message" button.

# Integration

**Psychology — Positive Reinforcement and Operant Conditioning:** Skinner's operant conditioning research showed that immediate, specific positive reinforcement increases the likelihood of behaviour repetition. A specific, warm success message after form submission is positive reinforcement for using the form. Generic or absent confirmations are a missed opportunity — and may actually discourage future use if users are uncertain their action succeeded.

# Lore Conclusion

*"The confirmation is the receipt. The receipt is the evidence. Evidence is trust."*

---
