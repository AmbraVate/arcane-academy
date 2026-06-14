---
id: fe-jun-m6-09
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m6
moduleTitle: "Module 6: Component Design"
moduleGlyph: "🧩"
moduleSortOrder: 6
topicSlug: presentational_vs_container
topicTitle: "Presentational vs Container"
topicSortOrder: 3
lesson: applying_the_pattern
title: "Applying the Pattern"
sortOrder: 3
difficulty: 5
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-07, fe-jun-m6-08]
integrationDomains: [design, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Walks through the refactor of a monolithic component into presenter + container"
    - "Identifies what belongs in the presentational component vs the container"
    - "Acknowledges a situation where the split is not worth the overhead"
  keywords: [refactor, split, presentational, container, simple, overhead]
  modelAnswer: |
    To refactor a monolithic component, first identify all the rendering logic (JSX, conditional display, styling) — this becomes the presentational component receiving props. Then identify all the data concerns (fetches, store selects, effects) — this becomes the container passing props down. The split is valuable when both sides are non-trivial. For a small component that fetches one value and renders two lines, the pattern adds more complexity than it removes.
guidedSteps:
  - id: fe-jun-m6-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You are refactoring a ProductDetail component. It fetches a product by ID, formats the price, and renders the product card. What goes in the container?"
    inputConfig:
      options:
        - "The product card JSX and price formatting"
        - "Fetching the product by ID and passing it as a prop"
        - "All of it — containers should contain everything"
        - "Only the price formatting logic"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Fetching the product by ID and passing it as a prop"]
      rejectedFeedback: "The container handles data concerns — fetching the product. The presentational component receives the product and handles rendering (including formatting)."
    hint: "The container's job is to answer 'where does the data come from?' The presenter's job is 'how is the data displayed?'"
    reflectionPrompt: "Should price formatting go in the container or the presentational component? Why?"
  - id: fe-jun-m6-09-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Describe a situation where applying the presentational/container split would add more complexity than it removes."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [simple, small, trivial, one, few, overhead, unnecessary]
      rejectedFeedback: "The pattern adds value when both sides are substantial. For tiny components (one fetch, one element), the overhead of two files and an interface is not worth it."
    hint: "Think about a component that fetches a username and renders it in a single paragraph."
    reflectionPrompt: "What rule of thumb would you use to decide whether to apply the split?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "After the refactor, you need to swap the data source from REST to GraphQL. Which component do you change?"
    options:
      - "The presentational component"
      - "Both components equally"
      - "The container component"
      - "Neither — you add a new adapter component"
    correctIndex: 2
    feedback: "The container owns the data-fetching logic. The presentational component is unchanged — it still receives the same props regardless of where the data comes from."
retrieval:
  recall: "What are the two steps in refactoring a monolithic component into a presenter/container pair?"
  explain: "Why should price formatting logic go in the presentational component rather than the container?"
  mistakeId:
    code: |
      // Container passing raw API response directly
      const ProductContainer = () => {
        const { data } = useQuery(['product', id], fetchProduct);
        return <ProductCard rawApiResponse={data} />;
      };

      const ProductCard = ({ rawApiResponse }) => (
        <div>{rawApiResponse?.data?.attributes?.productName?.en ?? 'Unknown'}</div>
      );
    answer: "The container should transform the API response into a clean, simple props interface before passing it. The presentational component should receive name: string, not a raw API response. The container is responsible for the data transformation."
---

# Hook

You have inherited a 200-line component file. It fetches data, handles errors, sorts the results, and renders a complex card grid. You need to add a new visual state for "featured" products. But the logic and rendering are so entangled that changing the JSX risks breaking the fetch logic. You spend an hour understanding the file before you can make a 5-line change.

This lesson teaches you how to untangle it.

# Lore Introduction

A master artificer inherited a workbench from their predecessor. On it sat a single enormous apparatus: a furnace, a press, a cooling bath, and an engraving tool, all welded together. Improving the press required dismantling the furnace. The master's first act was to separate the components. Only then could each part be refined, tested, and improved independently.

Your refactor is the master's first act.

# Core Learning

## Concept Introduction

Applying the presentational/container pattern to an existing component is a concrete, repeatable process:

1. **Identify the rendering logic** (JSX, conditional display, styling choices) → this becomes the presentational component.
2. **Identify the data concerns** (fetches, store connections, effect hooks, data transformation for the API) → this becomes the container.
3. **Define the props interface** between them: what data does the presentational component need to render correctly?
4. **Wire them together:** the container passes the interface to the presentational component.

## Worked Example

Before — monolithic:

```tsx
const ProductDetailPage = ({ productId }: { productId: string }) => {
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch(`/api/products/${productId}`)
      .then(r => r.json())
      .then(data => { setProduct(data); setLoading(false); })
      .catch(() => { setError('Failed to load product'); setLoading(false); });
  }, [productId]);

  if (loading) return <div className="animate-pulse h-64 bg-gray-100 rounded" />;
  if (error) return <div className="text-red-500 p-4">{error}</div>;

  return (
    <div className="max-w-2xl mx-auto p-6">
      <img src={product!.imageUrl} alt={product!.name} className="w-full rounded-lg mb-6" />
      <h1 className="text-2xl font-bold mb-2">{product!.name}</h1>
      <p className="text-gray-600 mb-4">{product!.description}</p>
      <p className="text-xl font-semibold">
        {new Intl.NumberFormat('en-GB', { style: 'currency', currency: 'GBP' }).format(product!.price)}
      </p>
    </div>
  );
};
```

After — split:

```tsx
// 1. Presentational component — pure renderer
interface ProductDetailProps {
  name: string;
  description: string;
  imageUrl: string;
  formattedPrice: string;
}

const ProductDetail = ({ name, description, imageUrl, formattedPrice }: ProductDetailProps) => (
  <div className="max-w-2xl mx-auto p-6">
    <img src={imageUrl} alt={name} className="w-full rounded-lg mb-6" />
    <h1 className="text-2xl font-bold mb-2">{name}</h1>
    <p className="text-gray-600 mb-4">{description}</p>
    <p className="text-xl font-semibold">{formattedPrice}</p>
  </div>
);

// 2. Container — data concerns
const ProductDetailContainer = ({ productId }: { productId: string }) => {
  const { data: product, isLoading, isError } = useQuery(
    ['product', productId],
    () => fetchProduct(productId)
  );

  if (isLoading) return <div className="animate-pulse h-64 bg-gray-100 rounded" />;
  if (isError) return <div className="text-red-500 p-4">Failed to load product</div>;

  return (
    <ProductDetail
      name={product.name}
      description={product.description}
      imageUrl={product.imageUrl}
      formattedPrice={new Intl.NumberFormat('en-GB', {
        style: 'currency', currency: 'GBP'
      }).format(product.price)}
    />
  );
};
```

Now `ProductDetail` can be tested with simple props. Migrating from `fetch` + `useState` to React Query only changes `ProductDetailContainer`.

## When to Skip the Pattern

The split adds value when both components have meaningful complexity. It is overkill when:

- The component fetches one value and renders one element (5 lines total).
- The component is a leaf component with no children — the split produces two tiny files with a trivial props interface.
- The team is building a prototype where speed matters more than architecture.

The test: "Would separating these make either side easier to work with independently?" If the answer is no, keep them together.

## Common Mistakes

**Putting data transformation in the presentational component.** If `ProductDetail` receives a raw price and formats it internally, it is doing a tiny bit of data logic. The container should provide a clean, pre-formatted value (`formattedPrice: string`).

**Making the container re-render the presentational component with raw API data.** The container is responsible for transforming the API shape into the presentational component's props interface. Don't leak API response shapes into the UI layer.

**Creating containers for every component by default.** The pattern is a tool, not a rule. Apply it thoughtfully.

## Mini Summary

Refactoring to presenter/container is a two-step process: extract rendering into a presentational component with a clean props interface, and extract data concerns into a container that feeds that interface. The split pays off when both sides are substantial. Skip it for trivially simple components.

# Guided Practice Quest

Work through the steps to identify the split points in a monolithic component and design the props interface for the presentational side.

# Solo Practice Quest

Write a before/after sketch (prose or pseudo-code) of refactoring a `CommentThread` component that fetches comments for a post and renders them. Include:

1. What the monolithic component does
2. The props interface for the presentational `CommentList` component
3. What the `CommentListContainer` handles
4. One thing you would test on the presentational component alone

Write 5–8 sentences.

# Integration

**Philosophy — Abstraction Layers:** Clean Architecture (Robert C. Martin) defines layers that each do one kind of work: data access, business logic, presentation. The container/presenter split is a frontend instantiation of this layering. Each layer depends only on the interface of the layer beneath it — the presenter doesn't know or care about the data access implementation.

**Design — Design Mockups:** When a designer presents a mockup, they provide the component's visual contract: what data needs to be displayed and how. This is exactly the presentational component's props interface. Good communication between design and engineering involves agreeing on this interface before either builds their side — containers fill in the data, designers specify the rendering.

# Lore Conclusion

The master artificer's separated workbench became the most productive in the Academy. Each station — furnace, press, bath, engraver — could be maintained, replaced, and improved without disturbing the others. A new engraving tool was installed in an afternoon; previously it would have required days of careful dismantling. Build your component pairs with the same separability, and future changes will take minutes, not days.

---
