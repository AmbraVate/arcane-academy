---
id: de-app-m1-02
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m1
moduleTitle: "Module 1: Understanding Data"
moduleGlyph: "📊"
moduleSortOrder: 1
topicSlug: what_is_data
topicTitle: "What is Data?"
topicSortOrder: 1
lesson: structured_and_unstructured_data
title: "Structured and Unstructured Data"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m1-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Accurately defines structured data and gives a real example
    - Accurately defines unstructured data and gives a real example
    - Correctly identifies semi-structured data as a middle ground
    - Explains at least one engineering challenge posed by unstructured data
    - Reflects on why most real-world data is a mixture of types
  keywords: [structured, unstructured, semi-structured, schema, rows, columns, JSON, text, image]
  modelAnswer: |
    Structured data is organised into a predefined schema of rows and columns, making it easy to query and analyse — a relational database table of customer orders is a classic example. Unstructured data has no predefined format; emails, images, and audio recordings are unstructured because their content cannot be mapped directly to rows and columns. Semi-structured data sits in between, using tags or markers like JSON or XML to impose some order without enforcing a rigid schema.
guidedSteps:
  - id: de-app-m1-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following is an example of structured data?
    inputConfig:
      options:
        - "A customer service email thread"
        - "A video recording of a product demonstration"
        - "A database table of customer orders with defined columns"
        - "A social media post with hashtags and images"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A database table of customer orders with defined columns"]
      rejectedFeedback: "Structured data lives in tables with predefined columns and data types. Emails, videos, and social posts are unstructured because they have no fixed schema."
    hint: "Think about which option has a clear, predictable format with defined fields."
    reflectionPrompt: "What makes a table 'structured' in a way that a folder of PDFs is not?"
  - id: de-app-m1-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "JSON and XML are examples of ________ data because they use tags or keys to organise content without enforcing a strict schema."
    inputConfig:
      placeholder: "semi-structured"
    markingRule:
      matchMode: CONTAINS
      accepted: [semi-structured, semi structured]
      rejectedFeedback: "JSON and XML are semi-structured — they have some organisation through key-value pairs or tags, but they do not enforce the rigid row-and-column schema of a relational table."
    hint: "This type of data sits between fully structured tables and completely free-form content."
    reflectionPrompt: "Why might a semi-structured format like JSON be useful for an API that different systems need to consume?"
  - id: de-app-m1-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, describe one engineering challenge that arises when working with unstructured data compared to structured data.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [schema, query, parse, extract, format, storage, index, search]
      rejectedFeedback: "Consider challenges like the inability to use SQL directly, the need to parse or extract meaning from free-form content, or the difficulty of indexing and searching unstructured files."
    hint: "Think about what you can and cannot do easily with a CSV file versus a folder of Word documents."
    reflectionPrompt: "How might you begin to make unstructured data more usable in an engineering pipeline?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which data type is best described as having a predefined schema with rows and columns?"
    options: ["Unstructured", "Semi-structured", "Structured", "Binary"]
    correctIndex: 2
    feedback: "Structured data is organised into a fixed schema — typically rows and columns in a relational table — making it straightforward to query with SQL."
  - type: MULTIPLE_CHOICE
    question: "A folder of MP3 audio files is an example of:"
    options: ["Structured data", "Semi-structured data", "Unstructured data", "Normalised data"]
    correctIndex: 2
    feedback: "Audio files have no predefined schema or fixed format that maps to rows and columns — they are unstructured data."
retrieval:
  recall: "In one sentence, define structured data and give one example."
  explain: "Explain why unstructured data presents greater engineering challenges than structured data."
  mistakeId:
    code: "JSON is structured data because it has a format"
    answer: "JSON is semi-structured. It uses key-value pairs for organisation but does not enforce a rigid schema — keys can be missing or nested arbitrarily, unlike a relational table."
---

# Hook

Open any company's data estate and you will find a startling diversity of shapes. In one corner: neatly arranged rows and columns of transactions, products, and customers. In another: thousands of scanned invoices, handwritten notes, audio recordings of support calls, and free-form customer feedback. All of it is "data" — but not all of it behaves the same way.

The distinction between **structured**, **unstructured**, and **semi-structured** data is one of the first architectural decisions a data engineer must understand. The type of data you are dealing with determines which tools you use, how you store it, how you query it, and how much work it takes to extract value from it.

Most real-world data estates contain all three types. What is your intuition about which type is hardest to work with — and why?

# Lore Introduction

"The Archive does not hold only scrolls," Master Selvaris said, leading her apprentice through a low archway into a cavernous chamber. One wall was filled with identical leather-bound ledgers, each page ruled into neat columns: names, dates, quantities. Another wall held bundles of loose parchment — letters, sketches, maps torn at the edges. In the centre, stacked on a circular table, were clay tablets covered in symbols arranged in clusters. "Three kinds of records," she said, touching each in turn. "The ledgers I can search in moments. The clay tablets take some decoding but follow a pattern. The loose parchment?" She paused. "It holds the richest knowledge of all — but only if you can find a way to read it."

# Core Learning

## Concept Introduction

| Type | Definition | Storage Format | Query Method | Example |
|------|-----------|---------------|--------------|---------|
| **Structured** | Data organised into a fixed schema of rows and columns | Relational databases, CSV, spreadsheets | SQL | Customer order table, payroll records |
| **Semi-structured** | Data with some organisation (tags, keys) but no rigid schema | JSON, XML, YAML, Parquet | JSONPath, XPath, custom parsers | API responses, log files, config files |
| **Unstructured** | Data with no predefined format or schema | Files, blobs, object storage | Full-text search, ML models, NLP | Emails, images, audio, video, PDFs |

## Why It Matters

The type of data determines your entire engineering approach:

- **Structured data** can be stored in relational databases and queried directly with SQL. It is the most engineer-friendly form.
- **Semi-structured data** needs parsing or flattening before it can be loaded into a relational table. Tools like Apache Spark, dbt, or database JSON functions handle this.
- **Unstructured data** requires specialised processing — optical character recognition (OCR) for images, speech-to-text for audio, natural language processing (NLP) for text — before any structured analysis is possible.

Understanding the type upfront prevents costly design mistakes. Trying to store unstructured data in a relational table, or assuming JSON is "just like a table," are classic apprentice errors.

## Worked Examples

**Example 1: E-commerce Platform**
- *Structured*: The `orders` table — `order_id`, `customer_id`, `total_amount`, `order_date` — is perfectly structured. SQL queries work instantly.
- *Semi-structured*: The product API returns JSON with nested objects: `{ "product_id": 42, "attributes": { "colour": "red", "size": "M" } }`. The schema varies per product type.
- *Unstructured*: Customer reviews typed in free text, plus photos of products uploaded by users. No fixed schema exists.

**Example 2: Healthcare System**
- *Structured*: Patient demographics table — `patient_id`, `date_of_birth`, `blood_type`.
- *Semi-structured*: HL7 FHIR records sent as JSON between hospital systems.
- *Unstructured*: Radiology images (DICOM files), audio recordings of consultations, scanned handwritten notes.

**Example 3: Financial Services**
- *Structured*: Transaction ledger with `transaction_id`, `amount`, `currency`, `timestamp`.
- *Semi-structured*: XML messages from SWIFT banking network.
- *Unstructured*: PDF regulatory filings, news articles scraped for sentiment analysis.

## Common Mistakes

- **Assuming all data is structured**: Many beginners expect everything to arrive as neat rows and columns. In practice, the majority of data generated worldwide is unstructured.
- **Treating JSON as structured**: JSON is *semi-structured*. Its schema can vary between records, and nested arrays cannot be directly mapped to flat relational tables without transformation.
- **Ignoring metadata for unstructured data**: Unstructured files (images, audio) often have metadata (file name, creation date, size) that *is* structured and can be stored relationally even if the content itself cannot.

## Mental Model

Think of a library. **Structured data** is the card catalogue — perfectly organised, every entry in the same format, instantly searchable. **Semi-structured data** is a set of index cards where each librarian used their own shorthand — the information is there and mostly consistent, but you have to know the conventions. **Unstructured data** is every book on the shelf — vast, rich, and full of knowledge, but you must read it to extract anything specific.

## Mini Summary

- ✔ Structured data has a fixed schema of rows and columns, queryable with SQL
- ✔ Semi-structured data (JSON, XML) has partial organisation but no enforced schema
- ✔ Unstructured data (images, audio, text) has no predefined format
- ✔ Each type requires different storage strategies, tools, and processing approaches
- ✔ Most real-world data estates contain all three types simultaneously

# Guided Practice Quest

Work through the guided steps to practise classifying data types correctly and articulating the key engineering differences between structured, semi-structured, and unstructured data.

# Solo Practice Quest

Choose an organisation you are familiar with (your employer, a university, a retailer, or a healthcare provider). Identify at least two examples of each data type (structured, semi-structured, and unstructured) that this organisation likely holds. For each example: name the data, describe how it is currently stored, explain what would need to happen to turn it into a structured, queryable form, and reflect on the engineering effort involved.

# Integration

**Sciences (Biology)**: Genome sequencing produces data that begins entirely unstructured — raw signal readings from a sequencer. Bioinformaticians apply algorithms to convert these signals into semi-structured formats (FASTQ, BAM files) and ultimately into structured tables of genetic variants. This mirrors exactly the data engineer's task of transforming raw, unstructured inputs into queryable structured records.

**Mathematics**: A matrix is a perfect example of structured data — every row has the same number of columns and every cell holds a numeric value. Vectors of different lengths, by contrast, are semi-structured. A collection of free-form text strings is unstructured from a mathematical standpoint — no algebraic operations apply until you encode the text (e.g., word embeddings), which is itself a structuring process.

# Lore Conclusion

Master Selvaris closed the door to the chamber and turned to face her apprentice. "Most who come to the Archive think their greatest challenge is finding the answer they seek. The real challenge is knowing which kind of record holds it — and how to read that kind of record at all." She handed over a worn notebook. "Before you design any system, you must first ask: what form does this knowledge take? Only then can you build the right vessel to hold it."

---
