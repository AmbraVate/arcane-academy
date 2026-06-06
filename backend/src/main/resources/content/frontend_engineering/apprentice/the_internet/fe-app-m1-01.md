---
id: fe-app-m1-01
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
lesson: what_is_the_internet
title: "What is the Internet?"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines the Internet accurately in own words"
    - "Explains how data travels between devices"
    - "Gives a real-world example of a request and response"
    - "Distinguishes between the Internet and the Web"
    - "Uses accurate terminology (packet, protocol, server, client)"
  keywords: [internet, network, packet, protocol, server, client, web]
  modelAnswer: |
    The Internet is a global network of interconnected computers that communicate
    using standardised protocols. Data travels as packets routed through this
    network. The Web (World Wide Web) is one service that runs on top of the
    Internet, using HTTP to serve web pages.
guidedSteps:
  - id: fe-app-m1-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which best describes what the Internet is?
    inputConfig:
      options:
        - "A website owned by a single company"
        - "A global network of interconnected computers communicating via protocols"
        - "The same thing as the World Wide Web"
        - "A wireless signal broadcast from satellites"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A global network of interconnected computers communicating via protocols"]
      rejectedFeedback: "The Internet is an infrastructure — a global network. The Web is a service that runs on top of it. They are not the same thing."
    hint: "Think about the word 'inter-net' — interconnected networks."
    reflectionPrompt: "The Internet is infrastructure. The Web is one application built on top of it. Email, streaming, and DNS are others. Distinguishing layers is a key engineering habit."

  - id: fe-app-m1-01-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "Data travels across the Internet in small chunks called ___, which are reassembled at the destination."
    inputConfig:
      placeholder: "packets"
    markingRule:
      matchMode: CONTAINS
      accepted: [packet, packets]
      rejectedFeedback: "Data is broken into **packets** — small, independently routed chunks. They may travel different paths and arrive out of order, then be reassembled. This is why the Internet is resilient."
    hint: "Think of sending a book by tearing out pages and mailing them separately."
    reflectionPrompt: "Packets are the reason the Internet is fault-tolerant. If one route fails, packets find another. No single point of failure can bring down the whole network."

  - id: fe-app-m1-01-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the difference between the Internet and the World Wide Web to someone who has never studied computing.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [internet, web, network, service, http, browser]
      rejectedFeedback: "The Internet = the network (roads). The Web = one service using that network (one type of vehicle). Other vehicles: email, streaming, file transfer."
    hint: "Use the analogy of roads vs cars: roads are infrastructure, cars are services that use them."
    reflectionPrompt: "This distinction matters in engineering. When a web page doesn't load, the problem might be the web server, the HTTP protocol, DNS, routing — or the Internet itself. Knowing the layers helps you debug."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A packet travelling from London to Sydney takes a different route than the previous packet in the same message. Is this normal?"
    options:
      - "No — all packets in a message must travel the same route"
      - "Yes — packets are independently routed and reassembled at the destination"
      - "Yes — but only if the primary route is broken"
      - "No — this would corrupt the data"
    correctIndex: 1
    feedback: "Independent routing is a core feature, not a fault. Packets find the best available path at the moment they travel. Reassembly at the destination reconstructs the original message."
  - type: MULTIPLE_CHOICE
    question: "Which of these is a service that runs ON TOP OF the Internet, rather than being the Internet itself?"
    options:
      - "TCP/IP"
      - "The World Wide Web"
      - "DNS"
      - "Packet routing"
    correctIndex: 1
    feedback: "The Web is an application layer service. TCP/IP is a core Internet protocol. DNS is an Internet infrastructure service. All three run over Internet infrastructure — the Web is just the most visible."

retrieval:
  recall: "In one sentence, define the Internet without using the word 'internet'."
  explain: "Explain why breaking data into packets makes the Internet more reliable than sending data as one continuous stream."
  mistakeId:
    code: "The Internet and the World Wide Web are the same thing"
    answer: "The Internet is the global network infrastructure. The Web is one application that runs on top of it using HTTP. Email, streaming, and file transfer are other Internet applications that are not the Web."
---

# Hook

Every time you load a web page, type a message, or watch a video, something remarkable happens in milliseconds: your request leaves your device, travels through cables and radio waves spanning continents, reaches a server somewhere in the world, and returns with the answer — all before you finish blinking.

How does this work? And how do frontend engineers fit into it?

Understanding the network your code runs on isn't optional — it's the foundation of everything you build.

> Before you read on: think of one thing that could go wrong between your device and a web server. Just one. Write it down.

# Lore Introduction

The Arcane Academy's apprentices begin their study not at a desk, but at a window — looking out at the city's network of roads, bridges, and messengers.

*"Before you build for the Web,"* says Master Aelindra, the Frontend Architect, *"you must understand the Web itself. Not the pages. Not the code. The network underneath."*

She gestures to a map of glowing lines connecting towers across the realm.

*"Every request you make, every page you render — it crosses this network. To build well, you must understand what carries your work."*

# Core Learning

## Concept Introduction

The **Internet** is a global network of billions of interconnected devices — computers, phones, servers, routers — that communicate using shared, standardised rules called **protocols**.

| Term | Meaning |
|---|---|
| **Network** | A group of connected devices that can communicate |
| **Protocol** | A set of agreed rules for how communication happens |
| **Packet** | A small chunk of data sent across the network |
| **Router** | A device that forwards packets toward their destination |
| **Server** | A computer that provides services (web pages, data, etc.) |
| **Client** | A device that requests services |

## Why It Matters

Frontend engineers build for the Web — which runs on the Internet. Understanding how data travels, what can go wrong, and why latency happens makes you a better engineer. When a user's page loads slowly or fails entirely, the problem could be anywhere in this chain. You need to know the chain to diagnose the problem.

## Worked Examples

**Example 1 — Loading a web page:**
1. You type `example.com` into your browser (client)
2. Your device sends a DNS request: *"What is the IP address of example.com?"*
3. DNS responds with an IP address (e.g., `93.184.216.34`)
4. Your browser sends an HTTP request to that address
5. The server at that address receives the request and sends back HTML, CSS, JavaScript
6. Your browser renders the page

Each step is a separate transaction, each following a protocol, each potentially failing.

**Example 2 — Packet routing:**
A 50KB image is split into ~35 packets. Each packet is independently routed through the network — some might go via New York, others via Amsterdam. They arrive out of order and are reassembled into the complete image. This is why the Internet is resilient: no single failed link breaks the whole network.

## Common Mistakes

- **Thinking "the Internet" and "the Web" are the same.** The Internet is infrastructure. The Web is one application built on it.
- **Assuming all requests are fast.** Every network hop takes time. Frontend engineers account for latency.
- **Treating the network as reliable.** Packets are lost. Connections drop. Good frontend code handles failures gracefully.

## Mental Model

Think of the Internet as the **road system** of a city. Roads are infrastructure. Cars, buses, and bikes are services that use the roads. The Web is one type of vehicle. Email is another. Streaming is another. The road system (Internet) doesn't care what vehicle uses it — it just moves things from A to B.

## Mini Summary

- The Internet is a global network of devices communicating via protocols
- Data travels as packets, independently routed and reassembled
- The Web is one application that runs on the Internet — not the same thing
- Frontend engineers build for the Web, which means understanding the network underneath

# Guided Practice Quest

**The Network Cartographer**

The Academy's cartographers are mapping the realm's communication network. You've been asked to answer three questions about how data travels — not with code, but with understanding.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Choose one of the following scenarios and write 3–5 sentences explaining what happens at the network level:

1. You send a chat message that fails to arrive
2. A web page loads partially — some images appear, others don't
3. A video call is clear for a minute, then drops

Your explanation must include: packets, routing, and at least one protocol.

# Integration

**Connecting to Mathematics — Graph Theory and Network Topology**

The Internet is, at its core, a graph: nodes (devices) connected by edges (links). The study of graphs — how they're connected, how things travel through them, how resilient they are to node removal — is a branch of mathematics called graph theory.

The Internet's topology was deliberately designed to be a distributed graph rather than a hub-and-spoke network. In a hub-and-spoke network, removing the hub brings the whole system down. In a distributed graph, you can remove many nodes and packets still find routes. This was a deliberate Cold War-era design requirement: survive infrastructure loss.

What does this suggest about the relationship between mathematical structure and engineering resilience?

# Lore Conclusion

The map glows. The lines between towers pulse with activity — packets of light moving in every direction, finding their way through the network.

*"Good,"* says Master Aelindra. *"You understand the foundation. Now we will learn about the devices that receive and render those packets — the browsers that turn network responses into living pages."*

The first rune of the Frontend path is inscribed.
