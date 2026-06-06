---
id: fe-app-m1-04
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
lesson: domains_and_urls
title: "Domains and URLs"
sortOrder: 4
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m1-02]
integrationDomains: [mathematics, history]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names all parts of a URL correctly"
    - "Explains what DNS does"
    - "Distinguishes between a domain and an IP address"
    - "Explains what a path and query string are"
    - "Uses correct terminology (scheme, host, path, query, fragment)"
  keywords: [url, domain, dns, ip, path, query, scheme, protocol, fragment, subdomain]
  modelAnswer: |
    A URL (Uniform Resource Locator) is an address that uniquely identifies a resource on the Web.
    It has several parts: scheme (protocol, e.g. https), host (domain name), path (resource location),
    query string (key-value parameters), and fragment (in-page anchor). The domain name is a
    human-readable label. DNS (Domain Name System) translates domain names to IP addresses so
    routers can direct traffic to the correct server.
guidedSteps:
  - id: fe-app-m1-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In the URL `https://shop.example.com/products?category=shoes#reviews`, what is the **path**?
    inputConfig:
      options:
        - "`https`"
        - "`shop.example.com`"
        - "`/products`"
        - "`category=shoes`"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["/products"]
      rejectedFeedback: "The path is `/products` — it identifies the specific resource on the server. `https` is the scheme. `shop.example.com` is the host. `category=shoes` is the query string. `#reviews` is the fragment."
    hint: "The path comes after the host and before the `?`. It identifies which resource you want."
    reflectionPrompt: "Frontend engineers work with paths constantly — routing libraries use them to decide which component to render. Understanding URL structure is essential for building navigation."

  - id: fe-app-m1-04-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "DNS stands for Domain Name ___, and it translates human-readable domain names into numerical IP addresses."
    inputConfig:
      placeholder: "System"
    markingRule:
      matchMode: CONTAINS
      accepted: [System, system]
      rejectedFeedback: "**DNS — Domain Name System** — is the Internet's address book. It maps names like `example.com` to IP addresses like `93.184.216.34`. Without DNS, you'd need to memorise IP addresses to browse the web."
    hint: "Think of DNS as a phone book: you look up a name and it gives you a number."
    reflectionPrompt: "DNS is often the hidden cause of 'site not loading' errors. If DNS fails, the browser never even makes an HTTP request — it can't find the server's address. Frontend engineers often encounter DNS issues during deployment."

  - id: fe-app-m1-04-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Break this URL into its parts and name each one:
      `https://api.myapp.com/users/42?format=json#contact`
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [scheme, host, path, query, fragment, https, api, users]
      rejectedFeedback: "Scheme: `https`. Host: `api.myapp.com`. Path: `/users/42`. Query string: `format=json`. Fragment: `contact`. Each part serves a specific purpose in addressing the resource."
    hint: "Work left to right: scheme, host, path, ?, #"
    reflectionPrompt: "Being able to decompose a URL is a practical skill. When an API call fails, you read the URL to understand what was requested. When a route doesn't match, you check the path. URL anatomy is daily work for frontend engineers."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does the query string in a URL allow you to do?"
    options:
      - "Navigate to a specific section within a page"
      - "Pass key-value parameters to the server as part of the request"
      - "Specify the protocol to use"
      - "Identify the server's IP address"
    correctIndex: 1
    feedback: "Query strings pass parameters: `?page=2&sort=price`. They're commonly used for filtering, searching, and pagination. The fragment (`#section`) navigates within a page. The scheme (`https`) specifies the protocol."
  - type: MULTIPLE_CHOICE
    question: "A user types `google.com` into their browser. Which service resolves this to an IP address before the HTTP request is made?"
    options:
      - "The browser's cache only"
      - "DNS (Domain Name System)"
      - "HTTP"
      - "The web server itself"
    correctIndex: 1
    feedback: "DNS translates the human-readable domain to an IP address. The browser may have the answer cached, but the DNS system is ultimately what performs this translation. Without it, the browser has no address to connect to."

retrieval:
  recall: "List the five parts of a URL and give a one-word description of each."
  explain: "Why does DNS need to exist? What problem would arise if it didn't?"
  mistakeId:
    code: "The domain name and the IP address are the same thing"
    answer: "They are not the same. The domain name (e.g. example.com) is a human-readable label. The IP address (e.g. 93.184.216.34) is the actual network address. DNS maps one to the other. Domains can change which IP they point to — this is how servers can move without breaking links."
---

# Hook

Type `google.com` into your browser. Press Enter.

Before a single byte of the Google homepage reaches your screen, your browser must answer a fundamental question: *where is this?*

The address `google.com` is human-friendly. But the Internet routes packets to numbers, not names. Something must translate one into the other — and it must do so in milliseconds, billions of times per day.

Understanding URLs and DNS is understanding the addressing system of the entire Web.

> Write down a URL you use every day. Can you name each part of it right now?

# Lore Introduction

The Academy's postal system uses two kinds of addresses.

Citizens use names: *"Deliver this to the Cartographers' Hall, second floor, northern wing."* But the messenger pigeons navigate by coordinates: precise numerical locations on the realm's grid.

Somewhere, a translator converts one to the other.

*"This is DNS,"* says Master Aelindra, tracing the path on the map. *"Humans speak domain names. The network speaks IP addresses. The Domain Name System bridges the two."*

She adds a new symbol to the map: a structured address in five parts.

*"Learn to read a URL the way a cartographer reads a coordinate — precisely, completely, and without confusion."*

# Core Learning

## Concept Introduction

A **URL** (Uniform Resource Locator) is the complete address of a resource on the Web. Every URL has a defined structure:

```
https://shop.example.com/products?category=shoes#reviews
  │         │                │          │            │
scheme    host             path      query        fragment
```

| Part | Example | Meaning |
|---|---|---|
| **Scheme** | `https` | Protocol to use |
| **Host** | `shop.example.com` | Server address (domain) |
| **Path** | `/products` | Resource location on the server |
| **Query string** | `?category=shoes` | Key-value parameters |
| **Fragment** | `#reviews` | In-page anchor (not sent to server) |

### Anatomy of a Domain

```
shop.example.com
 │      │    │
subdomain  domain  TLD (top-level domain)
```

- **TLD** (`.com`, `.org`, `.co.uk`) — the root category
- **Domain** (`example`) — the registered name
- **Subdomain** (`shop`) — a prefix that can point to different servers

### What DNS Does

When you type a domain into your browser:
1. Browser checks its local cache
2. If not cached, asks the operating system
3. OS queries a DNS resolver (usually your ISP's or a public one like `8.8.8.8`)
4. The resolver finds the authoritative DNS server for that domain
5. Returns the IP address (e.g., `93.184.216.34`)
6. Browser makes the HTTP request to that IP

This entire process typically completes in 1–100ms.

## Why It Matters

Frontend engineers work with URLs constantly:
- **Routing** — client-side routers match URL paths to components
- **API calls** — you construct URLs to fetch data
- **Debugging** — you read URLs to understand what was requested
- **Links** — you write `href` attributes with correct paths
- **Query parameters** — used for search, filtering, pagination

Understanding URL structure is not optional — it's daily work.

## Worked Examples

**Example 1 — URL for a filtered search:**
```
https://shop.example.com/search?q=trainers&size=10&colour=black
```
- Scheme: `https`
- Host: `shop.example.com`
- Path: `/search`
- Query: `q=trainers&size=10&colour=black` (three parameters)

**Example 2 — URL for a specific user:**
```
https://api.myapp.com/v2/users/1234
```
- The ID `1234` is embedded in the path — this is a **path parameter**
- Useful for REST APIs where the resource is identified by ID

**Example 3 — Fragment navigation:**
```
https://docs.example.com/guide#installation
```
- The `#installation` fragment tells the browser to scroll to the element with `id="installation"`
- The fragment is NOT sent to the server — it's purely client-side

## Common Mistakes

- **Confusing query strings and path parameters.** `/users?id=42` and `/users/42` are both valid but mean different things. REST APIs typically use path parameters for resource IDs.
- **Thinking the fragment is sent to the server.** It isn't. Fragments are processed by the browser only.
- **Not encoding special characters in URLs.** Spaces and symbols must be percent-encoded (e.g., `%20` for space). Unencoded URLs cause errors.

## Mental Model

Think of a URL as a **postal address with extra information**:
- Country (scheme): the delivery system to use
- City and street (host): where the building is
- Floor and room (path): which specific resource inside
- Note attached (query): specific instructions about what you want
- Sticky note (fragment): where to look once you arrive

## Mini Summary

- A URL has five parts: scheme, host, path, query string, fragment
- DNS translates human-readable domain names into IP addresses
- Subdomains, domains, and TLDs structure the host portion
- Query strings pass parameters; fragments navigate within a page
- Frontend engineers work with URLs constantly — routing, API calls, links, debugging

# Guided Practice Quest

**The Address Decoder**

The Academy has received a series of encoded messages in URL format. Decode each one by identifying its parts — a critical skill for the cartographers' next mission.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are building a recipe search web application. Design the URL structure for the following pages:

1. The home page
2. A search for "chocolate cake" recipes
3. A specific recipe (with ID 847) titled "Double Chocolate Fudge Cake"
4. The same recipe, scrolled to the "Method" section
5. Recipes filtered by cuisine (Italian) and cooking time (under 30 minutes)

Write each URL and explain why you structured it that way.

# Integration

**Connecting to History — Naming Systems and Human Cognition**

Before DNS, the entire list of Internet hosts was maintained in a single text file called `HOSTS.TXT`, manually maintained at Stanford Research Institute and downloaded by anyone who needed it. By 1983, this system had become unmanageable — hundreds of new hosts were being added per month.

DNS was designed in 1983 by Paul Mockapetris as a distributed, hierarchical naming system. It was a response to a scaling problem: centralised naming cannot keep pace with exponential network growth.

This pattern — centralised systems giving way to distributed ones as scale increases — recurs throughout the history of computing and engineering. The Web's addressing system is a case study in how human-readable naming conventions evolve to meet technical constraints.

What does this history suggest about how engineering solutions must evolve when systems grow?

# Lore Conclusion

The map is updated. Every tower in the realm now has both a name and a coordinate — and the translation system between them is understood.

*"The URL is your compass,"* says Master Aelindra. *"It tells you where you are, where you're going, and what you're asking for. Learn to read it precisely and you will never be lost."*

The fourth rune of the Frontend path glows.

*"Next: the language of that request itself. HTTP — the protocol that carries every web interaction."*
