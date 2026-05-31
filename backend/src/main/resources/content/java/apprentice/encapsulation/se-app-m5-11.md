---
id: se-app-m5-11
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m5
moduleTitle: "Module 5: Object Thinking Foundations"
moduleGlyph: "🔷"
moduleSortOrder: 5
topicSlug: encapsulation
topicTitle: "Encapsulation"
topicSortOrder: 3
lesson: getters_and_setters
title: "Getters & Setters"
sortOrder: 11
difficulty: 2
estimatedMinutes: 23
xpReward: 60
practiceType: JAVA
questType: PRACTICE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m5-10]
integrationDomains: [philosophy, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly writes a getter that returns a private field value"
    - "Correctly writes a setter that validates before applying the new value"
    - "Follows the getName() / setName() naming convention"
    - "Setter includes at least one validation condition before assigning"
    - "Demonstrates that the getter and setter allow controlled external interaction with the private field"
  keywords: [getter, setter, getName, setName, return, validate, private, controlled access, convention]
  modelAnswer: |
    class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }

        public int getAge() { return age; }

        public void setAge(int age) {
            if (age >= 0 && age <= 150) {
                this.age = age;
            }
        }
    }

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Add a getter called getName() to this Person class that returns the private name field."
    inputConfig:
      language: java
      starterCode: "class Person {\n    private String name;\n    private int age;\n\n    public Person(String name, int age) {\n        this.name = name;\n        this.age = age;\n    }\n\n    // Add getName() getter here\n}\n"
      expectedPattern: "public\\s+String\\s+getName\\s*\\(\\s*\\)"
    markingRule: REGEX_MATCH
    hint: "A getter returns the field type and is named get + FieldName (capitalised first letter)."
    reflectionPrompt: "Why does a getter return the field value rather than just printing it?"

  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Add a setter called setAge(int age) that only sets the age if the new value is between 0 and 150."
    inputConfig:
      language: java
      starterCode: "class Person {\n    private String name;\n    private int age;\n\n    public Person(String name, int age) {\n        this.name = name;\n        this.age = age;\n    }\n\n    public String getName() { return name; }\n\n    // Add setAge() with validation here\n}\n"
      expectedPattern: "public\\s+void\\s+setAge\\s*\\(\\s*int"
    markingRule: REGEX_MATCH
    hint: "Use: if (age >= 0 && age <= 150) { this.age = age; }"
    reflectionPrompt: "What happens to the old age value if the new value fails validation? Is that the right behaviour?"

  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Create a Person object. Try setting age to -5 (should be ignored), then set age to 25 and print getName() and getAge() to verify."
    inputConfig:
      language: java
      starterCode: "class Person {\n    private String name;\n    private int age;\n    public Person(String name, int age) { this.name = name; this.age = age; }\n    public String getName() { return name; }\n    public int getAge() { return age; }\n    public void setAge(int age) { if (age >= 0 && age <= 150) this.age = age; }\n}\n\n// Create person, test setAge(-5), then setAge(25), then print\n"
      expectedPattern: "setAge\\s*\\(\\s*-\\s*5|setAge\\s*\\(-5"
    markingRule: REGEX_MATCH
    hint: "Create: Person p = new Person('Alice', 30); then call p.setAge(-5); and p.setAge(25);"
    reflectionPrompt: "What does this demonstrate about the power of a setter over a public field?"

microCheckpoint:
  - question: "What is the correct name for a getter that returns a private field called 'score'?"
    options:
      - "returnScore()"
      - "score()"
      - "getScore()"
      - "readScore()"
    correctIndex: 2
    feedback: "Correct — the Java convention for getters is 'get' + field name with capital first letter: getScore()."

  - question: "What is the key advantage of a setter over a public field?"
    options:
      - "A setter is faster than a public field"
      - "A setter uses less memory"
      - "A setter can validate the new value before applying it, preventing invalid state"
      - "A setter is required by the Java compiler"
    correctIndex: 2
    feedback: "Yes — a setter can check that the new value is valid before assigning it to the field, which a public field cannot do."

retrieval:
  recall: "What is the naming convention for a getter and setter for a field called 'health'?"
  explain: "Explain why a setter provides better protection than a public field, using the example of an age field that must be between 0 and 150."
  mistakeId:
    code: |
      class Dog {
          private String name;

          public void getName() {
              System.out.println(name);
          }

          public String setName(String name) {
              this.name = name;
              return name;
          }
      }
    answer: "Two mistakes: (1) getName() should return the name (return type String, with 'return name;'), not print it — callers need the value. (2) setName() should return void, not the String — its job is to set the field, not return a value."
---

# Hook

Private fields are secure, but that creates a new problem: if no external code can read or change them, how does anything useful get done? The answer is a pair of controlled access spells. A **getter** reads a private field and hands back its value. A **setter** changes a private field — but only after checking that the new value is valid. Together they form the controlled public interface of your object: an open window with a bouncer on duty.

# Lore Introduction

The Academy's Sanctioned Access Protocol grants two types of approved interactions with a warded construct: the Reading Invocation, which queries the current state of an essence fragment without altering it, and the Binding Invocation, which attempts to alter a fragment — but only if the attempt passes the construct's validation runes. Any attempt to alter essence that fails the validation is silently discarded, leaving the construct in its last valid state. This is the contract between a construct and the world: I will tell you what you need to know, and I will accept valid changes. Everything else is rejected.

# Core Learning

## Concept Introduction

**Getter** — a `public` method that returns the value of a `private` field.

Naming convention: `get` + field name (first letter capitalised)
- `name` → `getName()`
- `balance` → `getBalance()`
- `age` → `getAge()`

```java
public String getName() {
    return name;
}
```

**Setter** — a `public` method that sets the value of a `private` field, usually after validating the input.

Naming convention: `set` + field name (first letter capitalised)
- `name` → `setName(String name)`
- `age` → `setAge(int age)`

```java
public void setName(String name) {
    if (name != null && !name.isEmpty()) {
        this.name = name;
    }
}
```

## Why It Matters

A getter gives external code **read access** without exposing the field directly. The field stays private; the getter provides a safe route to its value.

A setter gives external code **write access** but with a gatekeeper: the validation logic inside the setter. A public field has no gatekeeper — anyone can write any value. A setter can reject invalid values before they corrupt the object.

## Worked Examples

```java
class Dog {
    private String name;
    private int age;

    public Dog(String name, int age) {
        this.name = name;
        this.age  = age;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // Setter with validation
    public void setAge(int age) {
        if (age >= 0 && age <= 30) {  // dogs don't live past 30
            this.age = age;
        }
        // If validation fails, do nothing — age stays unchanged
    }
}

Dog rex = new Dog("Rex", 3);
System.out.println(rex.getName()); // Rex
System.out.println(rex.getAge());  // 3

rex.setAge(-5);                    // rejected by validation
System.out.println(rex.getAge());  // 3 — unchanged

rex.setAge(5);                     // accepted
System.out.println(rex.getAge());  // 5
```

## Common Mistakes

- **Getter prints instead of returning**: `void getName() { System.out.println(name); }` — callers need the value returned so they can use it. Always `return` from a getter.
- **Setter returns a value**: `String setName(String name)` — setters are `void`. Their job is to set the field, not return anything.
- **No validation in setter**: A setter with no validation check is slightly better than a public field (it creates an extension point), but it does not actually protect the state yet.
- **Not using `this.name = name`**: Without `this`, the assignment goes to the parameter, not the field.

## Mental Model

Think of a getter as a **one-way mirror**: you can see through it (read the field), but you cannot touch anything on the other side. A setter is a **letterbox**: you can post a letter (set a value), but the construct inside decides whether to open it or push it back. Both give controlled access — neither exposes the room itself.

## Mini Summary

- ✔ Getters return a private field value; named `getFieldName()` with return type matching the field.
- ✔ Setters assign to a private field with validation; named `setFieldName(Type value)` with `void` return.
- ✔ Getters should `return` the value — not print it.
- ✔ Setters should be `void` — not return a value.
- ✔ Validation inside a setter prevents invalid state that a public field cannot prevent.

# Guided Practice Quest

Work through the sidebar steps to add `getName()`, `setAge()` (with validation), and test that invalid values are rejected while valid values are accepted.

# Solo Practice Quest

**Spell: The Warded Construct**

Write a fully encapsulated `BankAccount` class:
- Private fields: `owner` (String), `balance` (double)
- Constructor: sets both fields (reject negative balance with balance staying 0)
- Getters: `getOwner()`, `getBalance()`
- No setter for `owner` (it should never change)
- A `deposit(double amount)` method (validates: amount must be > 0)
- A `withdraw(double amount)` method (validates: amount > 0 AND amount <= balance)

Create two accounts, perform valid and invalid operations, and print balances to show validation works.

# Integration

**Philosophy connection — the principle of least authority**

In political philosophy, the principle of least authority states that individuals and institutions should hold only the powers they need to perform their roles — no more. Setters apply this principle in code: external objects are granted only the authority to change values within approved ranges, using approved methods. The object itself retains sovereign control over its own state. This is not just good engineering — it reflects a deeper philosophical principle about power, responsibility, and trust between components.

**Mathematics connection — partial functions and domain restriction**

A mathematical function can be defined over a restricted domain: `f(x)` where `x ∈ [0, 150]` is only defined for ages between 0 and 150. Inputs outside this domain are undefined. A setter with validation is the programmatic equivalent: it implements a partial function whose domain is restricted to valid inputs. Inputs outside the domain are silently rejected. The setter transforms the free-for-all of direct field access into a properly bounded, domain-restricted function.

**Question:** A `Temperature` class stores temperature in Celsius. Its setter `setCelsius(double temp)` validates that `temp` is above absolute zero (-273.15). Express this constraint as a mathematical domain restriction, then explain what happens in the setter when an invalid value is passed and why this is better than throwing an exception at the caller's location.

# Lore Conclusion

Your constructs are now fully warded. Private fields, public constructors, public methods, getters that read safely, and setters that validate before changing — you have assembled the full pattern of encapsulation. This is the contract your construct makes with the world: I will give you what you need to know. I will accept valid changes. All else is rejected. Module 5 is complete. You have moved from imagining objects to building them with professional discipline. In Module 6 you will face the adversary that every programmer must master: the corrupted rune. The bug. And you will learn to hunt it with systematic precision.
