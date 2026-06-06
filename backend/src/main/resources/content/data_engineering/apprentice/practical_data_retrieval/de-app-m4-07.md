---
id: de-app-m4-07
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m4
moduleTitle: "Module 4: Joining Information"
moduleGlyph: "🔗"
moduleSortOrder: 4
topicSlug: practical_data_retrieval
topicTitle: "Practical Data Retrieval"
topicSortOrder: 2
lesson: product_catalogues
title: "Product Catalogues"
sortOrder: 7
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m4-06]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Writes a JOIN between products and categories
    - Joins order_lines to products to compute sales per product
    - Uses LEFT JOIN to show products that have never been ordered
    - Combines multiple joins with GROUP BY for product sales reports
    - Reflects on the typical product catalogue schema and its foreign key structure
  keywords: [product, category, order_lines, supplier, JOIN, LEFT JOIN, GROUP BY, sales, catalogue, inventory]
  modelAnswer: |
    Product catalogue queries typically join products to categories (a many-to-one relationship via category_id), to suppliers, and to order_lines (via product_id) for sales reporting. LEFT JOIN products to order_lines shows all products including those never ordered. INNER JOIN filters to only products with sales. GROUP BY product_id with SUM(quantity) and SUM(revenue) produces sales summaries. The most common catalogue query is the product listing with category name, inventory status, and sales figures — all from a multi-table join.
guidedSteps:
  - id: de-app-m4-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `products` table has a `category_id` FK. A `categories` table has `category_id` PK and `name`. Which JOIN produces a product list with each product's category name?
    inputConfig:
      options:
        - "products LEFT JOIN categories ON products.category_id = products.category_id"
        - "products INNER JOIN categories ON products.category_id = categories.category_id"
        - "categories INNER JOIN products ON categories.name = products.name"
        - "products INNER JOIN categories ON categories.category_id = products.product_id"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["products INNER JOIN categories ON products.category_id = categories.category_id"]
      rejectedFeedback: "The correct ON clause links the FK in products (category_id) to the PK in categories (category_id): ON products.category_id = categories.category_id. INNER JOIN is appropriate if all products have a category (common requirement). Option A joins products.category_id to itself — wrong. Option C joins on name — wrong. Option D mixes category_id with product_id — wrong."
    hint: "The FK in the child table (products.category_id) links to the PK in the parent table (categories.category_id)."
    reflectionPrompt: "When would you use LEFT JOIN instead of INNER JOIN here — what business scenario would require it?"
  - id: de-app-m4-07-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To find the total quantity sold per product, you join products to order_lines and use: `SELECT p.name, ________(ol.quantity) AS total_sold FROM products p JOIN order_lines ol ON p.product_id = ol.product_id GROUP BY p.product_id, p.name;`
    inputConfig:
      placeholder: "SUM"
    markingRule:
      matchMode: CONTAINS
      accepted: [SUM, sum]
      rejectedFeedback: "SUM(ol.quantity) totals the quantity column across all order_lines rows for each product group. The GROUP BY p.product_id, p.name produces one row per product. SUM totals the quantities from all order lines for that product. This is the core product sales aggregation pattern: join order_lines to products, GROUP BY product, SUM the quantities."
    hint: "Which aggregate function adds up numeric values across rows?"
    reflectionPrompt: "How would you also include products that have never been sold (showing 0 quantity)?"
  - id: de-app-m4-07-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, describe what a product catalogue report typically needs from a multi-table join and why a single flat table for all this data would be a problem.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [category, supplier, inventory, sales, join, normalised, redundancy, update, separate, flat]
      rejectedFeedback: "A product catalogue report needs product name and attributes, category name (from categories table), supplier name (from suppliers table), current stock level, and sales figures (from order_lines). Storing all this in one flat table would mean repeating category and supplier details in every product row — changing a category name would require updating potentially thousands of product rows. Normalisation stores each fact once and uses joins to reconstruct the combined view at query time."
    hint: "Think about what information a product listing page needs that isn't in the products table itself."
    reflectionPrompt: "If you change a category name from 'Electronics' to 'Consumer Electronics' in a flat table with 50,000 products, how many rows need updating?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which query correctly shows all products with their category name AND total units sold, including products never ordered?"
    options:
      - "SELECT p.name, c.name, SUM(ol.quantity) FROM products p JOIN categories c ON p.category_id = c.category_id JOIN order_lines ol ON p.product_id = ol.product_id GROUP BY p.product_id, p.name, c.name;"
      - "SELECT p.name, c.name, SUM(ol.quantity) FROM products p JOIN categories c ON p.category_id = c.category_id LEFT JOIN order_lines ol ON p.product_id = ol.product_id GROUP BY p.product_id, p.name, c.name;"
      - "SELECT p.name, c.name, COUNT(ol.line_id) FROM products p LEFT JOIN categories c ON p.category_id = c.category_id LEFT JOIN order_lines ol ON p.product_id = ol.product_id GROUP BY p.product_id, p.name, c.name;"
      - "SELECT p.name, c.name, SUM(ol.quantity) FROM categories c JOIN products p ON c.category_id = p.category_id LEFT JOIN order_lines ol ON p.product_id = ol.product_id GROUP BY p.product_id, p.name, c.name;"
    correctIndex: 1
    feedback: "Option B is correct: INNER JOIN to categories (all products have a category), LEFT JOIN to order_lines (preserves products with no orders), SUM(ol.quantity) correctly returns NULL for unmatched products (wrap in COALESCE for 0). Option A uses INNER JOIN to order_lines — excludes unordered products. Options C and D have issues with COUNT vs SUM or table order."
  - type: MULTIPLE_CHOICE
    question: "In a three-table join: products → order_lines → orders, which is the correct join sequence?"
    options:
      - "FROM products JOIN orders ON products.product_id = orders.order_id JOIN order_lines ON orders.order_id = order_lines.line_id"
      - "FROM products JOIN order_lines ON products.product_id = order_lines.product_id JOIN orders ON order_lines.order_id = orders.order_id"
      - "FROM orders JOIN products ON orders.order_id = products.product_id JOIN order_lines ON products.product_id = order_lines.order_id"
      - "FROM order_lines JOIN products ON order_lines.line_id = products.product_id"
    correctIndex: 1
    feedback: "Option B follows the foreign key chain correctly: products links to order_lines via product_id, then order_lines links to orders via order_id. Each JOIN uses the correct FK→PK pair. Option A joins products directly to orders (no such FK exists). Option C reverses the key relationships incorrectly. Option D only joins two tables and uses the wrong column."
retrieval:
  recall: "Write a product sales report: product name, category, total units sold, total revenue, ordered by revenue descending — including products with zero sales."
  explain: "Describe the typical schema for a product catalogue and the foreign keys that link products to categories, suppliers, and order_lines."
  mistakeId:
    code: "SELECT p.name, c.name FROM products p, categories c"
    answer: "This is an implicit Cartesian join — no join condition, so every product is matched with every category (if there are 1,000 products and 20 categories, the result has 20,000 rows). The correct query uses an explicit JOIN with ON: SELECT p.name, c.name FROM products p JOIN categories c ON p.category_id = c.category_id. Always use explicit JOIN ... ON syntax."
---

# Hook

Product catalogues are the second most common join scenario after customers and orders. A product in the database has a category (from a categories table), a supplier (from a suppliers table), and sales history (from order_lines). Retrieving a meaningful product listing requires joining all of these.

This lesson applies join skills to product catalogue queries — building towards the kind of data a product page, inventory report, or sales dashboard would need.

# Lore Introduction

"The Alchemist's Society wants a product catalogue," the Merchant said. "Each item listed with its ingredient category, its supplier, its current stock, and how many units have been sold." Master Selvaris examined the schema: `items`, `categories`, `suppliers`, `order_lines`. "Four tables," she said. "One JOIN for categories, one for suppliers, one LEFT JOIN for order_lines — because items that have never been sold should still appear." She wrote the query. "Every item is on the report. Sold items show quantities. Unsold items show zero. Categories and suppliers appear by name, not ID." The Merchant reviewed it. "This would have taken a day to compile manually." Selvaris ran it. "Three seconds."

# Core Learning

## Concept Introduction

### The Standard Product Schema

```sql
categories:  category_id (PK), name, description
suppliers:   supplier_id (PK), name, contact_email, country
products:    product_id (PK), name, description, category_id (FK), supplier_id (FK),
             unit_price, stock_qty
order_lines: line_id (PK), order_id (FK), product_id (FK), quantity, unit_price
```

### Product with Category Name

```sql
-- Products with their category name
SELECT
    p.product_id,
    p.name              AS product_name,
    p.unit_price,
    p.stock_qty,
    c.name              AS category_name
FROM products AS p
INNER JOIN categories AS c ON p.category_id = c.category_id
ORDER BY c.name, p.name;
```

### Products with Sales Data (LEFT JOIN for zero-sale products)

```sql
-- All products with total units sold and revenue
SELECT
    p.product_id,
    p.name                                  AS product_name,
    c.name                                  AS category_name,
    COALESCE(SUM(ol.quantity), 0)           AS total_units_sold,
    COALESCE(SUM(ol.quantity * ol.unit_price), 0) AS total_revenue
FROM products AS p
INNER JOIN categories AS c ON p.category_id = c.category_id
LEFT JOIN order_lines AS ol ON p.product_id = ol.product_id
GROUP BY p.product_id, p.name, c.name
ORDER BY total_revenue DESC;
```

### Products with Full Details (Three+ Table Join)

```sql
-- Full product listing: product + category + supplier + sales stats
SELECT
    p.product_id,
    p.name                                  AS product_name,
    c.name                                  AS category,
    s.name                                  AS supplier,
    p.unit_price,
    p.stock_qty,
    COALESCE(SUM(ol.quantity), 0)           AS total_sold,
    COALESCE(SUM(ol.quantity * ol.unit_price), 0) AS revenue
FROM products AS p
INNER JOIN categories AS c  ON p.category_id  = c.category_id
INNER JOIN suppliers AS s   ON p.supplier_id  = s.supplier_id
LEFT JOIN order_lines AS ol ON p.product_id   = ol.product_id
GROUP BY p.product_id, p.name, c.name, s.name, p.unit_price, p.stock_qty
ORDER BY revenue DESC;
```

### Low Stock with Sales Rate

```sql
-- Products low on stock sorted by how fast they are selling
SELECT
    p.product_id,
    p.name,
    p.stock_qty,
    c.name                              AS category,
    COALESCE(SUM(ol.quantity), 0)       AS total_sold_ever,
    COALESCE(SUM(ol.quantity), 0) * 1.0 / NULLIF(p.stock_qty, 0) AS stock_consumption_ratio
FROM products AS p
INNER JOIN categories AS c ON p.category_id = c.category_id
LEFT JOIN order_lines AS ol ON p.product_id = ol.product_id
WHERE p.stock_qty < 50
GROUP BY p.product_id, p.name, p.stock_qty, c.name
ORDER BY stock_consumption_ratio DESC;
```

### Products Never Ordered

```sql
-- Products that have never appeared in any order
SELECT p.product_id, p.name, p.unit_price, c.name AS category
FROM products AS p
INNER JOIN categories AS c ON p.category_id = c.category_id
LEFT JOIN order_lines AS ol ON p.product_id = ol.product_id
WHERE ol.line_id IS NULL
ORDER BY c.name, p.name;
```

## Common Mistakes

- **Forgetting to GROUP BY all non-aggregate SELECT columns**: When joining products to categories (two column names) plus aggregating, GROUP BY must include all non-aggregate columns: `GROUP BY p.product_id, p.name, c.name`.
- **INNER JOIN to order_lines excludes unsold products**: If you want all products on the report, LEFT JOIN to order_lines.
- **Multiplying unit_price from the wrong table**: Products have a list unit_price; order_lines have the actual sold unit_price (may differ due to discounts). Use `ol.unit_price` for revenue calculations.
- **Not using COALESCE on SUM after LEFT JOIN**: `SUM(ol.quantity)` returns NULL for unsold products, not 0. Use COALESCE for display.

## Mental Model

Think of a product catalogue report as a spreadsheet where each row is a product. Columns come from different tables: product details from products, category name from categories, supplier name from suppliers, and sales figures from order_lines. Joining is the act of filling in those columns from their source tables. LEFT JOIN to order_lines ensures products with no sales still get a row — their sales columns just show NULL (or 0 after COALESCE). The GROUP BY collapses the many order_lines rows for each product into one summary row.

## Mini Summary

- ✔ `products INNER JOIN categories` adds category name to each product row
- ✔ `products LEFT JOIN order_lines` preserves unsold products in the result
- ✔ `GROUP BY p.product_id, p.name, c.name` collapses multiple order lines to one product row
- ✔ Use `COALESCE(SUM(ol.quantity), 0)` for 0 instead of NULL for unsold products
- ✔ Chain multiple JOINs to build a complete product report from multiple tables

# Guided Practice Quest

Work through the guided steps to join products to categories, add LEFT JOIN to order_lines for sales data, handle zero-sale products with COALESCE, and identify products that have never been ordered.

# Solo Practice Quest

Using `products` (product_id, name, category_id, supplier_id, unit_price, stock_qty), `categories` (category_id, name), `suppliers` (supplier_id, name, country), `order_lines` (line_id, order_id, product_id, quantity, unit_price, created_at): write six queries: (1) complete product listing with category and supplier name, (2) all products with total units sold and revenue (0 for unsold), (3) best-selling products by revenue, top 10, (4) products that have never been ordered, (5) total revenue per category, (6) supplier performance report: supplier name, number of products, total revenue from their products, average product price. For each, specify which join type you used and why.

# Integration

**Mathematics**: Product catalogue joins implement a chain of relational compositions with different cardinalities. Products to categories is a many-to-one join (each product has one category). Products to order_lines is one-to-many (each product can have many order lines). The GROUP BY on product_id then applies the summation homomorphism: f: (product × [order_line]) → (product × summary). In database theory, this is a hierarchical aggregation — the join expands the relation, and GROUP BY compresses it back, but with computed summary attributes replacing the expanded detail. The mathematical structure is identical to a group action: the group is the partition by product_id, the action is SUM/COUNT on the orbit.

**Sciences (Ecology — Species Inventory)**: Biodiversity monitoring joins species records to observation logs exactly as products join to order_lines. `SELECT s.species_name, h.habitat_name, SUM(o.individual_count) AS total_observed FROM species s JOIN habitats h ON s.habitat_id = h.habitat_id LEFT JOIN observations o ON s.species_id = o.species_id GROUP BY s.species_id, s.species_name, h.habitat_name` — a species inventory with observation totals. Species never observed (LEFT JOIN null) appear with zero counts. This is the direct ecological analogue of the product catalogue query: species = products, habitats = categories, observations = order_lines.

# Lore Conclusion

The Alchemist's Society catalogue was complete: 847 items across 23 ingredient categories, 12 suppliers, with total units sold and revenue per item. Items never sold were visible — flagged for removal or repricing. Top sellers were ranked. Low stock items were sorted by how fast they were being consumed. "One query gives the complete picture," the Merchant said. Master Selvaris closed the session. "That is the product catalogue pattern. INNER JOIN for required lookups (categories, suppliers). LEFT JOIN for optional sales data. GROUP BY to collapse lines to one product row. COALESCE to show zeros instead of NULLs. Repeat this pattern and you will write product reports correctly every time."

---
