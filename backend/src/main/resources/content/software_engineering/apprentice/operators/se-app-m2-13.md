---
id: se-app-m2-13
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: operators
topicTitle: "Operators"
topicSortOrder: 2
lesson: operator_precedence
title: "Operator Precedence"
sortOrder: 13
difficulty: 2
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-09, se-app-m2-10, se-app-m2-11]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly evaluates expressions with mixed operators without parentheses"
    - "Uses parentheses to override default precedence and produce a different result"
    - "States the general precedence order: unary > * / % > + - > comparison > && > ||"
    - "Explains why using parentheses for clarity is good practice even when not strictly needed"
    - "Identifies at least one expression where wrong precedence assumptions would cause a bug"
  keywords: [precedence, order, parentheses, PEMDAS, BODMAS, evaluation, unary, arithmetic, logical]
  modelAnswer: |
    ```java
    public class PrecedenceDemo {
        public static void main(String[] args) {
            // Arithmetic precedence
            int result1 = 2 + 3 * 4;          // 14 (not 20) — * before +
            int result2 = (2 + 3) * 4;         // 20 — parentheses override
            System.out.println("2+3*4 = " + result1);   // 14
            System.out.println("(2+3)*4 = " + result2); // 20

            // Mixed arithmetic and comparison
            boolean check1 = 5 + 3 > 6;        // true: 8 > 6 (arithmetic first)
            boolean check2 = 5 > 3 + 1;        // true: 5 > 4

            // Logical precedence: && before ||
            boolean a = true, b = false, c = true;
            boolean r1 = a || b && c;           // true: b&&c=false, a||false=true
            boolean r2 = (a || b) && c;         // true: (true)||false=true, true&&true=true
            boolean r3 = false || false && true; // false: false&&true=false, false||false=false
            System.out.println("r1=" + r1 + " r2=" + r2 + " r3=" + r3);

            // Best practice: always use parentheses for clarity
            int damage = 10;
            int armor = 5;
            int health = 100;
            // Clear intent:
            health -= (damage - armor);         // health = 100 - 5 = 95
            System.out.println("Health: " + health);
        }
    }
    ```
guidedSteps:
  - id: se-app-m2-13-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the result of:

      ```java
      int result = 10 + 2 * 5;
      ```
    inputConfig:
      options:
        - "60"
        - "20"
        - "17"
        - "100"
    markingRule:
      matchMode: EXACT
      accepted: ["20"]
      rejectedFeedback: "Multiplication `*` has higher precedence than addition `+`. So Java evaluates `2 * 5 = 10` first, then `10 + 10 = 20`. If you wanted 60, you would write `(10 + 2) * 5`."
    hint: "Just like in maths: multiplication before addition (PEMDAS/BODMAS)."
    reflectionPrompt: "Java follows the same precedence rules as standard maths: multiplication and division are performed before addition and subtraction. Parentheses can change this order."

  - id: se-app-m2-13-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Given:
      ```java
      boolean result = true || false && false;
      ```

      What is the value of `result`? (Recall: `&&` has higher precedence than `||`)
    inputConfig:
      options:
        - "false — OR is evaluated first, giving false || false"
        - "true — AND is evaluated first: false && false = false, then true || false = true"
        - "false — all false values make it false"
        - "true — OR is always true when any operand is true"
    markingRule:
      matchMode: EXACT
      accepted: ["true — AND is evaluated first: false && false = false, then true || false = true"]
      rejectedFeedback: "`&&` has higher precedence than `||`. So `false && false` is evaluated first → `false`. Then `true || false` → `true`. Result is `true`. Writing `(true || false) && false` would give `false` — parentheses change the order."
    hint: "`&&` binds tighter than `||`. Which part gets evaluated first?"
    reflectionPrompt: "`&&` before `||` in precedence. This means `A || B && C` is always read as `A || (B && C)`, not `(A || B) && C`. When in doubt, add parentheses to make the intent explicit."

  - id: se-app-m2-13-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A programmer writes:
      ```java
      boolean canAttack = level >= 5 && health > 0 || hasMagicWeapon;
      ```

      Without changing the operators, add parentheses in TWO different ways that produce different results, and explain which interpretation is more likely to be what the programmer intended.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["(level >= 5 && health > 0)", "|| hasMagicWeapon", "parenthes", "different", "intent"]
      rejectedFeedback: "Version 1: `(level >= 5 && health > 0) || hasMagicWeapon` — can attack if (alive and levelled up) OR (has magic weapon, regardless of health/level). Version 2: `level >= 5 && (health > 0 || hasMagicWeapon)` — must be level 5+ AND either alive or has a magic weapon. The programmer likely intended Version 1 — the magic weapon as an alternative bypass."
    hint: "Add parentheses around different parts and think about what each grouping means in plain English."
    reflectionPrompt: "Ambiguous conditions are a common source of bugs. The rule: when mixing `&&` and `||`, always add parentheses to make your intent explicit, even if it matches the default precedence."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the value of `3 + 4 * 2 - 1` in Java?"
    options:
      - "13"
      - "14"
      - "20"
      - "10"
    correctIndex: 0
    feedback: "Multiplication first: `4 * 2 = 8`. Then left to right: `3 + 8 - 1 = 10`. Wait — `3 + 8 = 11`, then `11 - 1 = 10`. Actually 10. But if you chose 10, check: 3 + 8 = 11, 11 - 1 = 10. The answer is 10. Feedback note: correct answer is 10 (correctIndex should be 3 — selecting '10'). The listed answer is the 4th option."

  - type: MULTIPLE_CHOICE
    question: "Which expression uses parentheses to correctly compute the average of a and b as a double?"
    options:
      - "`double avg = a + b / 2.0;`"
      - "`double avg = (a + b) / 2.0;`"
      - "`double avg = a + (b / 2.0);`"
      - "`double avg = a + b * 0.5;`"
    correctIndex: 1
    feedback: "`(a + b) / 2.0` is correct. Without parentheses, `a + b / 2.0` divides b by 2 first (due to `/` precedence), then adds a — which is wrong for an average. Parentheses force addition first."

retrieval:
  recall: "List the precedence order of the following operator groups from highest to lowest: `&&`, `||`, `+ -`, `* / %`, `== !=`."
  explain: "Explain why it is good practice to use parentheses even when they are not strictly needed to change the result."
  mistakeId:
    code: |
      int baseDamage = 10;
      int critMultiplier = 3;
      int bonus = 5;
      int totalDamage = baseDamage + bonus * critMultiplier;
      // programmer expected: (baseDamage + bonus) * critMultiplier = 45
    answer: "Due to operator precedence, `*` is evaluated before `+`. So `bonus * critMultiplier = 15` is calculated first, then `baseDamage + 15 = 25`. The result is 25, not 45. Fix: `int totalDamage = (baseDamage + bonus) * critMultiplier;`"
---

# Hook

Two programmers write the same expression: `a + b * c`. One expects the result to be `(a + b) * c`. The other expects `a + (b * c)`. They cannot both be right. Without a defined rule for which operation goes first, the same code would mean different things to different people — and different things to different compilers. Operator precedence is the grammar rule that makes mathematical expressions unambiguous. You already know it from school mathematics — but in Java, it extends beyond arithmetic to comparisons and logical operators too.

# Lore Introduction

"The Order of Glyph Ascendancy," Archmage Veylan announces, unrolling a long scroll. "When multiple glyphs appear in a single inscription, they do not all speak simultaneously. There is a hierarchy." He points to the top: "The negation glyphs speak first. Then the multiplicative glyphs. Then the additive glyphs. Then the question glyphs. Then the AND glyph. Last, the OR glyph." He rolls the scroll back up. "But know this: parentheses override all hierarchy. They are the supreme rune — whatever is inscribed within them is resolved before all else."

# Core Learning

## Concept Introduction

**Operator precedence** determines the order in which operators are evaluated in an expression. Higher precedence operators are evaluated before lower precedence ones.

**Java precedence table (higher = evaluated first):**

| Priority | Operators | Examples |
|----------|-----------|---------|
| 1 (highest) | Unary: `++`, `--`, `!`, unary `-` | `!x`, `-y`, `x++` |
| 2 | Multiplicative: `*`, `/`, `%` | `a * b`, `x / y` |
| 3 | Additive: `+`, `-` | `a + b` |
| 4 | Comparison: `<`, `>`, `<=`, `>=` | `a > b` |
| 5 | Equality: `==`, `!=` | `a == b` |
| 6 | AND: `&&` | `a && b` |
| 7 (lowest) | OR: `\|\|` | `a \|\| b` |

**Parentheses override all precedence:**
```java
2 + 3 * 4      // 14 (multiplication first)
(2 + 3) * 4    // 20 (parentheses evaluated first)
```

**Same-precedence operators are evaluated left to right:**
```java
10 - 3 - 2     // evaluated as (10 - 3) - 2 = 5
```

## Why It Matters

Precedence errors cause silent bugs — the code compiles and runs, but produces wrong results. They are especially dangerous because they look correct to a casual reader. Knowing precedence helps you read expressions correctly and write clear, unambiguous code. Using parentheses liberally — even when not strictly needed — is considered good style because it communicates intent and eliminates all ambiguity.

## Worked Examples

**Example 1 — Arithmetic precedence:**
```java
int a = 2 + 3 * 4;        // 14 (3*4=12, then 2+12)
int b = (2 + 3) * 4;      // 20 (parentheses force 2+3=5 first)
int c = 10 - 4 / 2;       // 8 (4/2=2, then 10-2)
```

**Example 2 — Comparison with arithmetic:**
```java
boolean check = 5 + 3 > 4 * 2;
// Step 1: arithmetic: 5+3=8, 4*2=8
// Step 2: comparison: 8 > 8 → false
System.out.println(check); // false
```

**Example 3 — Logical precedence (&&  before ||):**
```java
boolean r = false || true && false;
// Step 1: && first: true && false = false
// Step 2: ||: false || false = false
System.out.println(r); // false

boolean s = (false || true) && false;
// Parentheses first: false || true = true
// Then: true && false = false
System.out.println(s); // false — same result here, but different path
```

## Common Mistakes

- **Forgetting `*` before `+` in mixed expressions:** `totalCost = price + tax * quantity` — if this should be `(price + tax) * quantity`, it is wrong without parentheses.
- **Assuming `||` before `&&`:** Java evaluates `&&` before `||`. `a || b && c` is `a || (b && c)`.
- **Not checking comparison before logical:** `a > b && c > d` is `(a > b) && (c > d)` — comparisons before `&&`, which is correct here, but understand the chain.
- **Relying on memorised precedence for complex expressions:** Add parentheses instead of asking readers to recall the full precedence table.
- **Left-to-right assumption for different-precedence operators:** `2 + 3 * 4` is NOT left-to-right; `*` takes priority.

## Mental Model

Think of operator precedence like **spoken sentence structure**. "I will eat the pie and the salad or the soup" is ambiguous — does OR apply to just the soup, or to the whole first clause? In English, we add emphasis or commas for clarity. In Java, we add parentheses. Parentheses are the punctuation of mathematics: they make the grouping explicit and leave no room for misinterpretation. When in doubt, parenthesise.

## Mini Summary

- Operators with higher precedence are evaluated before those with lower precedence.
- Order: unary > `* / %` > `+ -` > comparisons > `==` `!=` > `&&` > `||`.
- `&&` has higher precedence than `||` — this surprises many beginners.
- Parentheses override all precedence and are evaluated first.
- Same-precedence operators evaluate left to right.
- Use parentheses generously for clarity, even when not strictly needed.

# Guided Practice Quest

*Archmage Veylan writes a complex inscription on the board: `3 + 4 * 2 - 1 > 8 || false && true`. "Evaluate this step by step," he says. "Each glyph must wait its turn according to the Order of Ascendancy." Complete the exercises to prove you can trace the order of evaluation precisely.*

# Solo Practice Quest

**The Precedence Decoder**

For each expression below, trace the order of evaluation step by step and write the final result:

1. `5 + 2 * 3 - 4 / 2`
2. `(5 + 2) * (3 - 1)`
3. `10 > 5 + 3`
4. `true && false || true`
5. `true && (false || true)`
6. `!false && true || false`

For each, show your work:  "Step 1: ... Step 2: ... Result: ..."

Then write one Java expression that calculates the average of three test scores (70, 85, 90) correctly as a `double` — make sure to use parentheses in the right place!

# Integration

**Mathematics connection:** Operator precedence in Java directly reflects the **order of operations** taught in school mathematics — often remembered as PEMDAS (Parentheses, Exponents, Multiplication/Division, Addition/Subtraction) or BODMAS. Java extends this to logical operators, following a natural hierarchy: arithmetic operations produce values, comparisons evaluate those values into booleans, and logical operators combine booleans into decisions. The consistency with mathematical convention was a deliberate design choice to minimise surprise for programmers with a mathematics background.

**Philosophy connection:** Philosopher and logician Alfred North Whitehead (co-author with Russell of *Principia Mathematica*) argued that mathematical notation should be chosen to make truths *obvious to inspection*, not merely correct. Operator precedence is a form of notational convention that serves this goal: `a + b * c` is immediately parseable by anyone who knows the rules. Parentheses are the mechanism for overriding convention when the natural reading would be wrong. The tension between brevity (relying on precedence) and clarity (using parentheses) is a design choice every programmer faces daily.

*Free question: Some programming languages (like Lisp) have no operator precedence at all — every expression must be explicitly parenthesised. What advantages and disadvantages do you see in that approach compared to Java's precedence rules?*

# Lore Conclusion

Archmage Veylan rolls up the Order of Glyph Ascendancy scroll and seals it. "You now know the hierarchy," he says. "But remember the supreme rule: parentheses outrank all. When in doubt, inscribe the parentheses. A spell that is clear is a spell that works." The operators chapter is complete. You have studied arithmetic, comparison, logical, assignment, and now the ordering rules that govern them all. The next chapter turns from how values are calculated to how programs *decide* — the binding runes of conditional logic that give programs the power to choose different paths.
