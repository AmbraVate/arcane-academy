---
id: de-app-m6-09
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m6
moduleTitle: "Module 6: Database Design Foundations"
moduleGlyph: "📐"
moduleSortOrder: 6
topicSlug: design_practice
topicTitle: "Design Practice"
topicSortOrder: 2
lesson: designing_store_database
title: "Designing a Store Database"
sortOrder: 9
difficulty: 3
estimatedMinutes: 35
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m6-08]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Identifies the core entities in a retail store system
    - Correctly separates orders from order lines (one-to-many)
    - Places the unit_price in order_lines to record the price at time of purchase
    - Applies appropriate PKs, FKs, and constraints
    - Reflects on the price-at-time-of-purchase problem and its solution
  keywords: [order, order_lines, product, customer, price, historical, snapshot, FK, one-to-many, stock, inventory]
  modelAnswer: |
    A store database needs: customers (customer_id, name, email), products (product_id, name, category_id, price, stock_qty), categories (category_id, name), orders (order_id, customer_id FK, order_date, status, total_amount), order_lines (line_id, order_id FK, product_id FK, quantity, unit_price). The key design decision is storing unit_price in order_lines — not reading it from the products table at report time. If a product's price changes after an order is placed, the recorded price must remain the original. This is the price-snapshot pattern: order_lines records the price at the time of purchase.
guidedSteps:
  - id: de-app-m6-09-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A customer places an order for 3 items. The order table has: order_id, customer_id, order_date. Where does the quantity and product information for each item go?
    inputConfig:
      options:
        - "Directly in the orders table — one column per product"
        - "In an order_lines table — one row per item, with order_id FK, product_id FK, and quantity"
        - "In the products table — the ordered quantity is stored with the product"
        - "In the customers table — customers track what they have ordered"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["In an order_lines table — one row per item, with order_id FK, product_id FK, and quantity"]
      rejectedFeedback: "An order contains multiple items, so the line items must be stored in a separate table: order_lines. Each row in order_lines represents one product line on an order: order_id (which order), product_id (which product), quantity, and unit_price. Orders to order_lines is one-to-many — one order, many lines. Storing multiple items in the orders table would violate 1NF (repeating groups)."
    hint: "An order for 3 items needs 3 rows in a table — each row is one item line."
    reflectionPrompt: "What is the purpose of the total_amount column in orders if the line-by-line amounts are in order_lines?"
  - id: de-app-m6-09-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The unit_price in order_lines should be the price ________ the order was placed, not the current product price.
    inputConfig:
      placeholder: "at the time"
    markingRule:
      matchMode: CONTAINS
      accepted: ["at the time", "when", "at purchase", "historical", "snapshot", "at order time"]
      rejectedFeedback: "The unit_price in order_lines must record the price at the moment of purchase — a price snapshot. If the product price changes after the order, the order history must remain accurate. If you read unit_price from the products table at report time, the revenue calculation changes retroactively whenever the price changes — historically incorrect. Recording the price in order_lines at order creation time locks in the historical record."
    hint: "The price in an order_lines row is fixed at the moment the order was placed — it should never change."
    reflectionPrompt: "How would you calculate total revenue for orders placed in January, if order_lines stores the price-at-time-of-purchase?"
  - id: de-app-m6-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what the stock_qty column in the products table represents and what query you would run to check if there is sufficient stock before placing an order.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [stock, available, quantity, inventory, check, WHERE, SELECT, product_id, UPDATE, reduce, decrement]
      rejectedFeedback: "stock_qty represents the current number of units available in inventory. Before placing an order, the application would run: SELECT stock_qty FROM products WHERE product_id = ? and compare against the requested quantity. If stock_qty >= requested_quantity, the order can proceed. After placing the order, the application updates: UPDATE products SET stock_qty = stock_qty - ordered_quantity WHERE product_id = ?. (In production, this would be wrapped in a transaction to prevent race conditions.)"
    hint: "Think about the two operations: checking if enough stock exists, then reducing it after the order is confirmed."
    reflectionPrompt: "What problem arises if two customers simultaneously try to order the last unit of a product? What database concept solves this?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why is unit_price stored in order_lines rather than looked up from the products table at query time?"
    options:
      - "Because products table is too slow to join"
      - "To preserve the historical price at the time of purchase — product prices can change"
      - "Because order_lines needs more columns than products can provide"
      - "It is conventional but not functionally necessary"
    correctIndex: 1
    feedback: "Prices change. If you calculated revenue by joining to the current products.price, all historical revenue calculations would change every time a price is updated — historically incorrect. Recording unit_price in order_lines at the time of the order creates an immutable snapshot of what was charged. This is fundamental to financial data integrity."
  - type: MULTIPLE_CHOICE
    question: "What does a row in the order_lines table represent?"
    options:
      - "One order placed by a customer"
      - "One product in the store's catalogue"
      - "One line item — a specific product and quantity within a specific order"
      - "One customer's total purchases"
    correctIndex: 2
    feedback: "Each row in order_lines represents one line item: one product within one order, with quantity and price. An order for 5 different products has 5 rows in order_lines. The orders table has one row per order (header data: customer, date, status); order_lines has one row per product per order (detail data: what was actually ordered)."
retrieval:
  recall: "Write the complete store database schema with all tables, columns, PKs, and FKs."
  explain: "Explain the price-snapshot pattern: why is unit_price stored in order_lines instead of read from products at query time?"
  mistakeId:
    code: "SELECT ol.quantity * p.price AS revenue FROM order_lines ol JOIN products p ON ol.product_id = p.product_id"
    answer: "Using p.price (current product price) to calculate revenue is wrong when prices change over time. If a product was sold for £10 last year but now costs £15, this query retroactively recalculates all last year's revenue at £15 — historically incorrect. The correct query uses ol.unit_price: SELECT ol.quantity * ol.unit_price AS revenue FROM order_lines ol. The unit_price in order_lines was recorded at the time of the sale and must never be updated."
---

# Hook

An online store is the most common database design scenario in software development. Every e-commerce application — from a small boutique to a major retailer — shares the same core schema: customers, products, orders, and order lines. But there are critical design decisions hiding in the details, particularly around how prices are stored.

This lesson designs a complete store database, focusing on the price-snapshot pattern and the orders/order_lines separation.

# Lore Introduction

"The Merchant Guild needs a sales system," the Guild Treasurer said. "Customers, products, orders — and it must be able to produce accurate revenue reports even after prices change." Master Selvaris wrote the orders table, then paused. "The price question," she said. "If a sword costs 50 gold today but 60 gold next month, what should a revenue report show for swords sold this month?" The Treasurer thought. "Fifty gold — the price we actually charged." Selvaris nodded. "So we cannot read the price from the products table at report time. We must store the price at the moment of the sale — in the order line. Fixed. Immutable. The historical record of what was charged." She wrote unit_price into order_lines. "This is the price-snapshot pattern. Financial data integrity depends on it."

# Core Learning

## Concept Introduction

### Step 1: Identify Entities

```
Customers    — who is buying
Products     — what is being sold
Categories   — how products are organised
Orders       — a purchase transaction (header record)
Order Lines  — individual items within an order (detail records)
```

### Step 2: Identify Relationships

```
Customers → Orders:       One-to-many (customer places many orders)
Orders → Order Lines:     One-to-many (order has many line items)
Products → Order Lines:   One-to-many (product appears in many order lines)
Categories → Products:    One-to-many (category has many products)
```

### Step 3: Design the Tables

```sql
-- Categories
CREATE TABLE categories (
    category_id   SERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL UNIQUE,
    description   TEXT
);

-- Customers
CREATE TABLE customers (
    customer_id   SERIAL PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(255) UNIQUE NOT NULL,
    phone         VARCHAR(20),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products
CREATE TABLE products (
    product_id    SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    description   TEXT,
    category_id   INTEGER REFERENCES categories(category_id),
    unit_price    DECIMAL(10, 2) NOT NULL,    -- current price
    stock_qty     INTEGER NOT NULL DEFAULT 0,
    sku           VARCHAR(50) UNIQUE
);

-- Orders (header — one per transaction)
CREATE TABLE orders (
    order_id      SERIAL PRIMARY KEY,
    customer_id   INTEGER NOT NULL REFERENCES customers(customer_id),
    order_date    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status        VARCHAR(20) NOT NULL DEFAULT 'pending',
                  -- pending, confirmed, shipped, delivered, cancelled
    total_amount  DECIMAL(10, 2),    -- denormalised total for quick access
    shipping_address TEXT
);

-- Order Lines (detail — one per product per order)
CREATE TABLE order_lines (
    line_id       SERIAL PRIMARY KEY,
    order_id      INTEGER NOT NULL REFERENCES orders(order_id),
    product_id    INTEGER NOT NULL REFERENCES products(product_id),
    quantity      INTEGER NOT NULL CHECK (quantity > 0),
    unit_price    DECIMAL(10, 2) NOT NULL   -- price at time of purchase — immutable
);
```

### The Price-Snapshot Pattern

```
products.unit_price   = current selling price (changes when you update it)
order_lines.unit_price = price at the time of purchase (never changes)

When a sale is made:
  INSERT INTO order_lines (order_id, product_id, quantity, unit_price)
  VALUES (?, ?, ?, (SELECT unit_price FROM products WHERE product_id = ?));
  -- Copies the current price into the order line — snapshots it

Revenue calculation — always uses order_lines.unit_price:
  SELECT SUM(quantity * unit_price) FROM order_lines WHERE order_id = ?;
  -- Correct regardless of price changes after the sale
```

### The total_amount Denormalisation

```sql
-- orders.total_amount is a denormalised computed column for performance
-- It should equal SUM(ol.quantity * ol.unit_price) for that order
-- Set when the order is placed, never recalculated after

-- Verifying total_amount is consistent:
SELECT o.order_id,
       o.total_amount AS recorded_total,
       SUM(ol.quantity * ol.unit_price) AS calculated_total,
       o.total_amount - SUM(ol.quantity * ol.unit_price) AS discrepancy
FROM orders o
JOIN order_lines ol ON o.order_id = ol.order_id
GROUP BY o.order_id, o.total_amount
HAVING ABS(o.total_amount - SUM(ol.quantity * ol.unit_price)) > 0.01;
```

### Sample Reporting Queries

```sql
-- Monthly revenue
SELECT
    EXTRACT(YEAR FROM o.order_date)   AS year,
    EXTRACT(MONTH FROM o.order_date)  AS month,
    COUNT(DISTINCT o.order_id)        AS orders,
    SUM(ol.quantity * ol.unit_price)  AS revenue
FROM orders o
JOIN order_lines ol ON o.order_id = ol.order_id
WHERE o.status != 'cancelled'
GROUP BY EXTRACT(YEAR FROM o.order_date), EXTRACT(MONTH FROM o.order_date)
ORDER BY year, month;

-- Best-selling products by revenue
SELECT
    p.name,
    c.name                              AS category,
    SUM(ol.quantity)                    AS units_sold,
    SUM(ol.quantity * ol.unit_price)    AS revenue
FROM order_lines ol
JOIN products p ON ol.product_id = p.product_id
JOIN categories c ON p.category_id = c.category_id
GROUP BY p.product_id, p.name, c.name
ORDER BY revenue DESC
LIMIT 10;

-- Low stock alert
SELECT p.name, p.stock_qty, p.unit_price
FROM products p
WHERE p.stock_qty < 10
ORDER BY p.stock_qty ASC;
```

### 3NF Verification

```
order_lines:
  unit_price → depends on (order_id, product_id) combination ✓
               NOT on product alone (would change with price updates)
               NOT looked up from products at query time
  customer_name → NOT in order_lines (looked up via orders.customer_id → customers) ✓
  product_name → NOT in order_lines (looked up via product_id → products) ✓
```

## Common Mistakes

- **Reading price from products table for revenue reports**: Retroactively changes historical revenue when prices update. Always use order_lines.unit_price.
- **Putting all line items in the orders table**: `SELECT product1, product2, product3 FROM orders` violates 1NF (repeating groups) and limits orders to a fixed number of items.
- **Not recording price in order_lines at all**: Then you must join to products to get the price, introducing the retroactive price change problem.
- **Updating order_lines.unit_price after it is set**: This is a historical record. Unit_price in order_lines should be treated as immutable once the order is placed.

## Mental Model

Think of orders and order_lines like an invoice. An invoice header (orders table) records: who the customer is, when it was issued, the overall status. The invoice line items (order_lines table) record: what was sold, how many, and at what price. The price on the invoice is the price charged — you would never change an issued invoice because the product's price went up later. The same immutability applies to order_lines.unit_price.

## Mini Summary

- ✔ Orders (header) and order_lines (detail) are separate tables — one-to-many
- ✔ `order_lines.unit_price` records the price at time of purchase — immutable snapshot
- ✔ Never use `products.unit_price` for historical revenue calculations
- ✔ `orders.total_amount` is a denormalised convenience field — verify it matches order_lines
- ✔ Stock control: `stock_qty` in products, decremented when an order is confirmed

# Guided Practice Quest

Work through the guided steps to design the order_lines table correctly, understand the price-snapshot pattern, verify that the schema is in 3NF, and write the revenue report query using the correct price source.

# Solo Practice Quest

Extend the store database to support the following business requirements: (1) discount codes — a code can give a percentage discount applied to an entire order, (2) product reviews — customers can leave one review per product they have purchased, with a rating (1-5) and review text, (3) addresses — customers can have multiple saved shipping addresses; each order references one, (4) returns — some order lines are returned after delivery, with a return_date and reason. For each extension: (a) identify new tables and columns required, (b) write the schema changes with PKs and FKs, (c) confirm 3NF, (d) write one SQL query using the new tables. Then write a complete data quality report query that checks for any inconsistency between orders.total_amount and the sum of order_lines amounts.

# Integration

**Mathematics**: The store schema illustrates the principle of data immutability for historical records — a key concept in append-only data systems. The order_lines.unit_price snapshot corresponds to the mathematical notion of a timestamp function: f: (product_id, timestamp) → price. At order creation time, this function is evaluated and the result stored. Subsequent evaluations of f at later timestamps return different values (price changes), but the stored value is the evaluation at purchase time. This is the foundation of temporal databases and event sourcing: immutable records of what happened at a specific time, rather than mutable current state. The SUM(quantity × unit_price) over order_lines is then an exact integral of the price function over the purchased quantities.

**Sciences (Operations Research — Inventory Management)**: The store schema is the standard model for inventory management systems, which apply operations research techniques to stock control. The Economic Order Quantity (EOQ) model calculates optimal reorder quantities based on demand rate, ordering cost, and holding cost. The demand rate is computed from the store database: `SELECT product_id, SUM(quantity) / COUNT(DISTINCT date_trunc('day', order_date)) AS avg_daily_demand FROM order_lines JOIN orders ON order_lines.order_id = orders.order_id GROUP BY product_id`. This query — using the normalised order_lines schema with historical prices — computes the input data for EOQ calculations. Well-designed store databases are the foundation of supply chain optimisation.

# Lore Conclusion

"The Merchant Guild's sales system is complete," the Treasurer confirmed, reviewing the reports. "Revenue for last month. Best-selling items. Low stock alerts. Historical price accuracy even after updates." Master Selvaris closed the schema file. "Three design systems practiced: library, school, store. Three different domains, but the same principles apply each time: normalise to 3NF, store relationship attributes in junction tables, record historical values at the time they occur." She turned to her Apprentice. "You can now design a database for any straightforward domain by applying these principles systematically. Identify entities. Identify relationships. Design keys. Check for normalisation violations. The schema follows from the principles." She paused. "The test of a design is not elegance — it is whether the important queries are simple. If your queries are complex because your schema is wrong, redesign the schema."

---
