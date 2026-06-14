---
id: fe-app-m1-02
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m1
moduleTitle: "Module 1: Understanding the Web"
moduleGlyph: "🌐"
moduleSortOrder: 1
topicSlug: the_internet
topicTitle: "The Internet"
topicSortOrder: 1
lesson: clients_and_servers
title: "Clients and Servers"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m1-01]
integrationDomains: [psychology, history]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines a client and a server in own words"
    - "Describes the request-response cycle accurately"
    - "Gives a concrete real-world example of a client and server"
    - "Explains what happens when a server is unavailable"
    - "Uses correct terminology (request, response, HTTP, status code)"
  keywords: [client, server, request, response, http, status, 200, 404]
  modelAnswer: |
    A client is any device or application that requests resources from another system.
    A server is a machine or program that listens for requests and returns responses.
    The request-response cycle is the fundamental unit of web communication: the client
    sends a request (e.g. GET /index.html), and the server replies with a response
    containing a status code and, if successful, the requested resource.
guidedSteps:
  - id: fe-app-m1-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      When you open a web page in your browser, your browser is acting as:
    inputConfig:
      options:
        - "A server — it serves the page to you"
        - "A client — it requests the page from a remote server"
        - "A router — it directs traffic to the right destination"
        - "A protocol — it defines how data is transferred"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A client — it requests the page from a remote server"]
      rejectedFeedback: "Your browser makes requests — it is always the client in this interaction. The server is the remote machine that holds and returns the page."
    hint: "Who asks, and who answers? The one asking is the client."
    reflectionPrompt: "The client-server model is asymmetric: clients initiate, servers respond. This shapes everything from API design to error handling in your frontend code."

  - id: fe-app-m1-02-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "When a server cannot find the requested resource, it returns HTTP status code ___."
    inputConfig:
      placeholder: "404"
    markingRule:
      matchMode: CONTAINS
      accepted: ["404", "404 Not Found"]
      rejectedFeedback: "**404 Not Found** is the standard HTTP status for a missing resource. 200 means success. 500 means server error. Status codes are how servers communicate outcomes to clients."
    hint: "This is the most famous HTTP status code. You've probably seen it on broken web pages."
    reflectionPrompt: "HTTP status codes are the server's language for communicating outcomes. Frontend engineers must handle them: show a 404 page, retry on 503, redirect on 301. Status codes are not just trivia — they drive UI logic."

  - id: fe-app-m1-02-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe what happens during the request-response cycle when you search for something on a website. Walk through the steps from pressing Enter to seeing results.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [request, response, server, client, browser, http]
      rejectedFeedback: "The cycle: client sends HTTP request → server receives and processes it → server sends HTTP response with status code and data → client renders the result. Each step is distinct and can fail."
    hint: "Think: who initiates? What does the server receive? What does it send back? What does the browser do with the response?"
    reflectionPrompt: "Every user interaction that involves the network triggers this cycle. As a frontend engineer, you write the code on the client side — but you must design for the full cycle, including errors and delays."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which HTTP status code indicates a successful response?"
    options:
      - "404"
      - "500"
      - "200"
      - "301"
    correctIndex: 2
    feedback: "200 OK means the request succeeded and the server is returning the requested resource. 404 = not found. 500 = server error. 301 = permanent redirect."
  - type: MULTIPLE_CHOICE
    question: "In the client-server model, which party initiates communication?"
    options:
      - "The server — it pushes data when it's ready"
      - "The client — it sends requests that the server responds to"
      - "Either party can initiate equally"
      - "The router — it coordinates both sides"
    correctIndex: 1
    feedback: "In standard HTTP, the client always initiates. The server listens and responds. This is why web servers are called 'request handlers' — they handle what clients send. (WebSockets change this, but that comes later.)"

retrieval:
  recall: "What are the two roles in the client-server model, and what does each one do?"
  explain: "Why does a frontend engineer need to understand HTTP status codes even though the server generates them?"
  mistakeId:
    code: "The server and client can both initiate requests at any time"
    answer: "In standard HTTP, only the client initiates requests. The server listens and responds. This asymmetry means the frontend must be the active party — polling, fetching, or reacting to user input to trigger communication."
---

# Hook

You click a button. Something happens. But what, exactly?

Between the moment you click and the moment you see a result, a precise, structured conversation happens between two parties — your browser and a server somewhere in the world. That conversation has rules, roles, and a defined vocabulary.

Understanding it is not background knowledge. It is your job.

> Before reading on: what do you think the word "server" actually means? Write down your current definition.

# Lore Introduction

In the Academy's great library, every apprentice learns two fundamental roles: the *Petitioner* and the *Custodian*.

The Petitioner approaches with a question. The Custodian holds the knowledge and responds.

*"This is the client-server model,"* explains Master Aelindra. *"Every web interaction follows this pattern. Your browser petitions. The server responds. Simple. Profound."*

She sets a sealed scroll on the table.

*"But what happens when the Custodian doesn't have what you seek? Or when the library is closed? A skilled engineer handles every outcome — not just the happy path."*

# Core Learning

## Concept Introduction

The **client-server model** is the architectural pattern underlying the entire web. Every web interaction involves two roles:

| Role | Definition | Example |
|---|---|---|
| **Client** | Requests resources or services | Browser, mobile app, CLI tool |
| **Server** | Listens for requests and returns responses | Web server, API server, file server |

The conversation between them follows a strict pattern: **request → response**.

### The HTTP Request

A client sends an HTTP request containing:
- **Method** — what to do (`GET`, `POST`, `PUT`, `DELETE`)
- **URL** — where to send it (`https://api.example.com/users`)
- **Headers** — metadata (content type, auth token, cookies)
- **Body** — optional data payload (for POST/PUT)

### The HTTP Response

The server replies with:
- **Status code** — the outcome (200 OK, 404 Not Found, 500 Server Error)
- **Headers** — metadata about the response
- **Body** — the requested resource (HTML, JSON, image, etc.)

### Common HTTP Status Codes

| Code | Meaning |
|---|---|
| `200 OK` | Success — resource returned |
| `201 Created` | Success — new resource created |
| `301 Moved Permanently` | Resource has a new URL |
| `400 Bad Request` | Client sent invalid data |
| `401 Unauthorized` | Authentication required |
| `403 Forbidden` | Authenticated but not permitted |
| `404 Not Found` | Resource doesn't exist |
| `500 Internal Server Error` | Server-side failure |
| `503 Service Unavailable` | Server temporarily down |

## Why It Matters

As a frontend engineer, you write client-side code. But your code must handle every server response — including failures. A 401 means show a login prompt. A 404 means show an error page. A 500 means tell the user something went wrong and maybe retry. Status codes drive your UI logic.

## Worked Examples

**Example 1 — A successful fetch:**
```
GET /api/products HTTP/1.1
Host: shop.example.com
Accept: application/json

→ HTTP/1.1 200 OK
Content-Type: application/json
[{"id": 1, "name": "Widget", "price": 9.99}]
```

**Example 2 — A missing resource:**
```
GET /api/products/9999 HTTP/1.1
Host: shop.example.com

→ HTTP/1.1 404 Not Found
Content-Type: application/json
{"error": "Product not found"}
```

Your frontend code must handle both of these. The happy path (200) and the error path (404) both need UI behaviour.

## Common Mistakes

- **Ignoring error responses.** Beginners fetch data and only write code for the success case. Production code handles 4xx and 5xx too.
- **Confusing 401 and 403.** 401 = not authenticated (you haven't logged in). 403 = authenticated but not permitted (you don't have access). Different user actions are required.
- **Treating servers as always available.** Servers go down. Networks fail. Good frontend code has fallbacks, loading states, and retry logic.

## Mental Model

Think of a client-server interaction like a **formal letter exchange**:
- You (client) write a letter with a specific question (request)
- The recipient (server) reads your letter and sends a reply (response)
- The reply includes an answer code: "Here it is (200)", "Not here (404)", "I'm busy (503)"
- You must read the code and respond appropriately — you can't just assume the answer arrived

## Mini Summary

- The client initiates all requests; the server listens and responds
- HTTP requests carry a method, URL, headers, and optional body
- HTTP responses carry a status code, headers, and optional body
- Status codes communicate outcomes — frontend engineers must handle them
- The request-response cycle is the fundamental unit of all web communication

# Guided Practice Quest

**The Custodian's Ledger**

The Academy's records room handles thousands of requests per day. Apprentices must understand how requests are fulfilled — and what to do when they aren't.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are building a product listing page for an online shop. Write a short description (4–6 sentences) of how your page should behave in each of these scenarios:

1. The server returns `200 OK` with a list of products
2. The server returns `404 Not Found`
3. The server returns `500 Internal Server Error`
4. The request times out with no response

For each, say what the user should see and why.

# Integration

**Connecting to History — The Client-Server Model and the Shift from Mainframes**

Before the client-server model dominated computing, the world ran on **mainframes**: powerful central machines that terminals connected to for all computation. The terminal did nothing on its own. All logic lived centrally.

The client-server model was a fundamental architectural shift. Clients gained processing power. Logic could be distributed. The Web extended this further — your browser (client) renders HTML, executes JavaScript, manages state. The server provides data and coordination, but the client does significant work.

This history explains why frontend engineering exists as a discipline. When clients were dumb terminals, there was no "frontend engineering" — just mainframe programming. The rise of the client created the role.

What does this suggest about how the distribution of responsibility between client and server affects the kinds of engineering problems that need solving?

# Lore Conclusion

The scroll is opened. Inside: a list of response codes, each with a story.

*"Every code is a message,"* says Master Aelindra. *"200 means: I have what you need. 404 means: I searched, but found nothing. 500 means: something went wrong on my end. A skilled engineer reads these messages and acts accordingly."*

She adds a second rune to the Frontend path.

*"Next, we examine what kinds of things clients request — and the difference between a simple website and a full application."*
