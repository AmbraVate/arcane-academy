package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

@Component
public class ExpertSeeder1 extends AbstractChunkSeeder {

    public ExpertSeeder1(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                         QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {
        seedChunkXA();
        seedChunkXB();
        seedChunkXC();
        seedChunkXD();
        seedChunkXE();
    }

    // ── XA: Domain-Driven Design ───────────────────────────────────────────

    private void seedChunkXA() {
        chunk("XA", "Domain-Driven Design", "\uD83C\uDFF0", 29, LearnerPath.EXPERT, "PN");

        // XA1 — DDD Core Concepts
        subChunk("XA1", "XA", "Entities, Value Objects & Aggregates", 1, 60, "DddConcepts.java",
                "<p>How do you model the real world in code without losing its meaning?</p>",
                "<p><strong>Domain-Driven Design (DDD)</strong> is a strategy for tackling complex domains by keeping your code aligned with the business language.</p>"
                + "<p>Three core building blocks:</p>"
                + "<ul>"
                + "<li><strong>Entity</strong> — has a unique identity that persists over time. Two Orders with the same items are still different if they have different IDs.</li>"
                + "<li><strong>Value Object</strong> — defined entirely by its attributes. Two Money(10, USD) objects are equal. Immutable.</li>"
                + "<li><strong>Aggregate</strong> — a cluster of entities/value objects with one Aggregate Root that controls all access.</li>"
                + "</ul>"
                + "<pre><code>// Entity — identity matters\npublic class Order {\n    private final OrderId id;    // identity\n    private List&lt;OrderLine&gt; lines;\n    // equals/hashCode based on id only\n}\n\n// Value Object — attributes matter\npublic final class Money {\n    private final BigDecimal amount;\n    private final Currency currency;\n    // equals/hashCode based on fields\n    // no setters — immutable\n}\n\n// Aggregate Root controls invariants\npublic class Order { // Aggregate Root\n    public void addLine(Product p, int qty) {\n        if (this.status != OrderStatus.DRAFT)\n            throw new IllegalStateException(\"Cannot modify submitted order\");\n        lines.add(new OrderLine(p, qty));\n    }\n}</code></pre>",
                story(
                    n("You have reached the highest vault of the Academy — Expert tier. Here, spells don't just run; they model entire worlds."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Before you write a single class, ask: what IS this thing in the real world? Is it defined by its identity, or by its values? That question determines everything."),
                    e("Value Object equality", "Money a = new Money(new BigDecimal(\"10.00\"), Currency.getInstance(\"USD\"));\nMoney b = new Money(new BigDecimal(\"10.00\"), Currency.getInstance(\"USD\"));\nSystem.out.println(a.equals(b)); // true — same attributes")
                ),
                "<p>Create an immutable <code>Address</code> value object with fields <code>street</code>, <code>city</code>, <code>postcode</code>. Override <code>equals</code> and <code>hashCode</code>. Print whether two identical addresses are equal.</p>",
                "public class Address {\n    private final String street;\n    private final String city;\n    private final String postcode;\n\n    public Address(String street, String city, String postcode) {\n        this.street = street;\n        this.city = city;\n        this.postcode = postcode;\n    }\n\n    // TODO: override equals comparing all three fields\n    // TODO: override hashCode\n\n    public static void main(String[] args) {\n        Address a1 = new Address(\"10 Main St\", \"London\", \"SW1A 1AA\");\n        Address a2 = new Address(\"10 Main St\", \"London\", \"SW1A 1AA\");\n        System.out.println(a1.equals(a2));\n    }\n}\n",
                tests(test("Value equality", "null", "true")),
                "Explain the difference between an Entity and a Value Object. Why does immutability matter for Value Objects?");

        mcQuestion("XA1", QuestionTier.RECALL, "What distinguishes an Entity from a Value Object in DDD?",
                new String[]{"Entities have unique identity; Value Objects are defined by attributes", "Entities are immutable; Value Objects are mutable", "Entities are simpler; Value Objects are complex", "Value Objects have IDs; Entities do not"},
                "Entities have unique identity; Value Objects are defined by attributes",
                "Entity equality is based on ID. Value Object equality is based on all its attribute values.");

        mcQuestion("XA1", QuestionTier.APPLICATION, "An Aggregate Root enforces which key responsibility?",
                new String[]{"Maintains all business invariants for the aggregate", "Stores the aggregate in the database", "Handles all HTTP requests", "Provides getters for all fields"},
                "Maintains all business invariants for the aggregate",
                "The Aggregate Root is the single entry point — all mutations go through it so invariants are always checked.");

        tfQuestion("XA1", QuestionTier.RECALL, "Value Objects should be immutable — once created, their state never changes.",
                "True", "Immutability makes Value Objects safe to share and easy to reason about.");

        // XA2 — Bounded Contexts
        subChunk("XA2", "XA", "Bounded Contexts & Ubiquitous Language", 2, 60, "BoundedContexts.java",
                "<p>Why does the word 'Customer' mean something different in Sales vs. Support?</p>",
                "<p>In large systems, the same word means different things in different parts of the business. DDD handles this with <strong>Bounded Contexts</strong>.</p>"
                + "<p>A <strong>Bounded Context</strong> is an explicit boundary within which a particular domain model applies. Inside it, every term has one precise meaning — the <strong>Ubiquitous Language</strong>.</p>"
                + "<p>Context integration strategies:</p>"
                + "<ul>"
                + "<li><strong>Anti-Corruption Layer (ACL)</strong> — translates between two models so each stays clean</li>"
                + "<li><strong>Shared Kernel</strong> — two teams share a small subset of the model</li>"
                + "<li><strong>Published Language</strong> — a well-documented exchange format (e.g., JSON events)</li>"
                + "</ul>"
                + "<pre><code>// Sales context — Customer has credit limit\npackage com.example.sales;\npublic class Customer {\n    private CustomerId id;\n    private Money creditLimit;\n}\n\n// Support context — Customer has ticket history\npackage com.example.support;\npublic class Customer {\n    private CustomerId id;\n    private List&lt;Ticket&gt; tickets;\n}\n\n// ACL translates at the boundary\npublic class SalesCustomerAdapter {\n    public support.Customer translate(sales.Customer c) {\n        return new support.Customer(c.getId());\n    }\n}</code></pre>",
                story(
                    n("The Council of Archmages governs different domains — Finance, Combat, Alchemy. Each has its own lexicon. 'Reagent' means gold coin in Finance but a potion ingredient in Alchemy."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Never let one context's model pollute another. Use an Anti-Corruption Layer like a diplomatic translator — it speaks both languages so your core models don't have to.")
                ),
                "<p>Sketch two classes: <code>SalesOrder</code> (has <code>customerId</code>, <code>totalAmount</code>) and <code>ShippingOrder</code> (has <code>orderId</code>, <code>deliveryAddress</code>). Write a <code>translate</code> method that creates a <code>ShippingOrder</code> from a <code>SalesOrder</code>.</p>",
                "public class BoundedContextDemo {\n    static class SalesOrder {\n        String customerId;\n        double totalAmount;\n        String orderId;\n        SalesOrder(String orderId, String customerId, double totalAmount) {\n            this.orderId = orderId; this.customerId = customerId; this.totalAmount = totalAmount;\n        }\n    }\n    static class ShippingOrder {\n        String orderId;\n        String deliveryAddress;\n        ShippingOrder(String orderId, String deliveryAddress) {\n            this.orderId = orderId; this.deliveryAddress = deliveryAddress;\n        }\n    }\n    // TODO: write translate(SalesOrder so, String address) that returns ShippingOrder\n    public static void main(String[] args) {\n        SalesOrder so = new SalesOrder(\"O-001\", \"C-42\", 99.99);\n        ShippingOrder ship = translate(so, \"10 Main St\");\n        System.out.println(ship.orderId + \" -> \" + ship.deliveryAddress);\n    }\n    static ShippingOrder translate(SalesOrder so, String address) {\n        return null; // TODO\n    }\n}\n",
                tests(test("Translate", "null", "O-001 -> 10 Main St")),
                "Explain Bounded Contexts and Ubiquitous Language. Why can the same word have different meanings in different contexts?");

        mcQuestion("XA2", QuestionTier.RECALL, "What is the purpose of an Anti-Corruption Layer?",
                new String[]{"Translates between two domain models without polluting either", "Prevents SQL injection", "Encrypts messages between services", "Validates user input"},
                "Translates between two domain models without polluting either",
                "An ACL acts as a translator at context boundaries, keeping each model clean.");

        codeQuestion("XA2", QuestionTier.APPLICATION, QuestionType.SCENARIO,
                "Two microservices both have a `Product` class but with different fields. How should they communicate?",
                "// Service A: Product { id, name, price }\n// Service B: Product { sku, weight, warehouse }\n// They need to exchange data",
                "Use a published language (shared event/DTO format) or Anti-Corruption Layer in each service to translate between their internal models",
                "Each service should own its model; integration happens at boundaries via translation, not shared classes.");

        // XA3 — Domain Services & Application Services
        subChunk("XA3", "XA", "Domain Services & Application Services", 3, 60, "DomainServices.java",
                "<p>Some business logic doesn't naturally belong to any single entity — where does it go?</p>",
                "<p>DDD distinguishes two service types:</p>"
                + "<ul>"
                + "<li><strong>Domain Service</strong> — contains domain logic that spans multiple aggregates. Stateless. Named after business concepts. Lives in the domain layer.</li>"
                + "<li><strong>Application Service</strong> — orchestrates domain objects and infrastructure (repos, messaging). Handles use cases. Thin — no business logic.</li>"
                + "</ul>"
                + "<pre><code>// Domain Service — pure business logic\npublic class TransferService {\n    public void transfer(Account from, Account to, Money amount) {\n        from.debit(amount);   // business rule in Entity\n        to.credit(amount);    // business rule in Entity\n    }\n}\n\n// Application Service — orchestrates\npublic class TransferApplicationService {\n    private final AccountRepository accounts;\n    private final TransferService transfer;\n\n    public void execute(TransferCommand cmd) {\n        Account from = accounts.findById(cmd.fromId());\n        Account to   = accounts.findById(cmd.toId());\n        transfer.transfer(from, to, cmd.amount());\n        accounts.save(from);\n        accounts.save(to);\n    }\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "A domain service holds wisdom about the domain — it knows the rules. An application service is the logistics officer — it fetches resources and delegates work. Keep them apart."),
                    n("Confusing the two is a common mistake. An application service that contains business logic is a sign the logic belongs in the domain.")
                ),
                "<p>Implement a <code>PricingService</code> domain service that calculates the total price of an order: base price × quantity, minus 10% discount if quantity ≥ 10.</p>",
                "public class PricingService {\n    // TODO: calculateTotal(double unitPrice, int quantity)\n    // Apply 10% discount when quantity >= 10\n\n    public static void main(String[] args) {\n        PricingService svc = new PricingService();\n        System.out.println(svc.calculateTotal(20.0, 5));   // 100.0\n        System.out.println(svc.calculateTotal(20.0, 10));  // 180.0\n    }\n    double calculateTotal(double unitPrice, int quantity) {\n        return 0; // TODO\n    }\n}\n",
                tests(test("No discount", "null", "100.0"), test("With discount", "null", "180.0")),
                "What is the difference between a Domain Service and an Application Service? Give an example of each.");

        mcQuestion("XA3", QuestionTier.RECALL, "Where should a Domain Service live in the architecture?",
                new String[]{"In the domain layer, containing pure business logic", "In the infrastructure layer, accessing the database", "In the presentation layer, handling HTTP requests", "In the application layer, alongside controllers"},
                "In the domain layer, containing pure business logic",
                "Domain Services contain domain logic and must not depend on infrastructure.");

        tfQuestion("XA3", QuestionTier.RECALL, "Application Services should contain core business rules and calculations.",
                "False", "Application Services orchestrate — they should be thin. Business rules belong in Entities or Domain Services.");

        // XA4 — Repositories & Hexagonal Architecture
        subChunk("XA4", "XA", "Repositories & Hexagonal Architecture", 4, 60, "HexagonalArch.java",
                "<p>How do you keep your business logic independent of databases, frameworks, and external systems?</p>",
                "<p><strong>Repository Pattern (DDD)</strong>: A repository is a domain-layer interface that looks like a collection of aggregates. The implementation lives in infrastructure.</p>"
                + "<p><strong>Hexagonal Architecture (Ports & Adapters)</strong>: The domain is the hexagon. <em>Ports</em> are interfaces (inbound & outbound). <em>Adapters</em> implement those interfaces using real technology (HTTP, JPA, Redis).</p>"
                + "<pre><code>// Port (domain layer — pure interface)\npublic interface OrderRepository {\n    Optional&lt;Order&gt; findById(OrderId id);\n    void save(Order order);\n}\n\n// Adapter (infrastructure layer — Spring Data JPA impl)\n@Repository\npublic class JpaOrderRepository implements OrderRepository {\n    private final OrderJpaRepo jpa;\n    public Optional&lt;Order&gt; findById(OrderId id) {\n        return jpa.findById(id.value()).map(this::toDomain);\n    }\n    public void save(Order order) {\n        jpa.save(toEntity(order));\n    }\n}\n\n// Domain never imports JPA — dependency points inward</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "The hexagon keeps your domain pure. Think of it as a spell that works regardless of which grimoire it was cast from. Swap PostgreSQL for MongoDB — the domain never knows."),
                    n("The key rule: all dependencies point inward toward the domain. Frameworks depend on you, not the other way round.")
                ),
                "<p>Define an interface <code>ProductRepository</code> with <code>save(String name, double price)</code> and <code>findByName(String name)</code>. Implement it with an in-memory <code>HashMap</code>. Save two products and retrieve one by name.</p>",
                "import java.util.HashMap;\nimport java.util.Map;\n\npublic class HexDemo {\n    interface ProductRepository {\n        void save(String name, double price);\n        double findByName(String name);\n    }\n\n    static class InMemoryProductRepository implements ProductRepository {\n        private Map<String, Double> store = new HashMap<>();\n        // TODO: implement save and findByName\n        public void save(String name, double price) {}\n        public double findByName(String name) { return 0; }\n    }\n\n    public static void main(String[] args) {\n        ProductRepository repo = new InMemoryProductRepository();\n        repo.save(\"Wand\", 29.99);\n        repo.save(\"Spellbook\", 49.99);\n        System.out.println(repo.findByName(\"Spellbook\"));\n    }\n}\n",
                tests(test("Find product", "null", "49.99")),
                "Explain Hexagonal Architecture. Why are Ports and Adapters defined in separate layers?");

        mcQuestion("XA4", QuestionTier.RECALL, "In Hexagonal Architecture, where do dependencies point?",
                new String[]{"Inward — toward the domain", "Outward — toward infrastructure", "In both directions equally", "Only between adapters"},
                "Inward — toward the domain",
                "All dependencies point inward. Adapters depend on port interfaces; the domain depends on nothing external.");

        codeQuestion("XA4", QuestionTier.APPLICATION, QuestionType.DEBUGGING,
                "This breaks the Hexagonal Architecture rule. Why?",
                "// Domain entity\nimport org.springframework.data.jpa.repository.JpaRepository;\npublic class Order {\n    private JpaRepository<Order, Long> repo; // injected\n    public void save() { repo.save(this); }\n}",
                "The domain entity imports a Spring JPA interface — the domain depends on infrastructure. The repository port should be an interface defined in the domain layer.",
                "Domain objects must never import framework or infrastructure classes. The dependency inversion principle keeps the domain clean.");
    }

    // ── XB: Spring Framework Core ──────────────────────────────────────────

    private void seedChunkXB() {
        chunk("XB", "Spring Framework Core", "\uD83C\uDF31", 30, LearnerPath.EXPERT, "PN");

        // XB1 — IoC & DI
        subChunk("XB1", "XB", "IoC Container & Dependency Injection", 1, 60, "SpringDI.java",
                "<p>Why should a class create its own dependencies — and why is that a problem?</p>",
                "<p>The Spring <strong>IoC (Inversion of Control) Container</strong> manages object creation and wiring. You declare what you need; Spring provides it.</p>"
                + "<p><strong>Dependency Injection (DI)</strong> forms: Constructor injection (preferred), Setter injection, Field injection (<code>@Autowired</code> — avoid in production code).</p>"
                + "<pre><code>@Service\npublic class OrderService {\n    private final OrderRepository orderRepo;  // dependency\n    private final EmailService emailService;\n\n    // Constructor injection — dependencies explicit, easy to test\n    public OrderService(OrderRepository orderRepo, EmailService emailService) {\n        this.orderRepo = orderRepo;\n        this.emailService = emailService;\n    }\n\n    public void placeOrder(Order order) {\n        orderRepo.save(order);\n        emailService.sendConfirmation(order);\n    }\n}</code></pre>"
                + "<p>Spring sees <code>@Service</code>, creates one instance, finds the constructor, locates matching beans, and injects them automatically.</p>",
                story(
                    n("You have entered the realm of Spring — the most powerful framework in the Java kingdom."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Before Spring, wizards conjured every dependency themselves — a nightmare of tangled creation code. The IoC container conjures them for you, in the right order, every time."),
                    e("Field injection vs constructor injection", "// Bad — field injection hides dependencies\n@Service\npublic class OrderService {\n    @Autowired private OrderRepository repo; // hidden\n}\n\n// Good — constructor injection\n@Service\npublic class OrderService {\n    private final OrderRepository repo; // explicit, final\n    public OrderService(OrderRepository repo) { this.repo = repo; }\n}")
                ),
                "<p>Create a <code>GreetingService</code> that depends on a <code>MessageProvider</code> interface. Implement <code>MessageProvider</code> with a class that returns <code>\"Hello, Wizard!\"</code>. Use constructor injection. Print the greeting.</p>",
                "public class SpringDIDemo {\n    interface MessageProvider {\n        String getMessage();\n    }\n\n    static class WizardMessageProvider implements MessageProvider {\n        public String getMessage() {\n            return \"Hello, Wizard!\";\n        }\n    }\n\n    static class GreetingService {\n        private final MessageProvider provider;\n        // TODO: constructor injection\n        public GreetingService(MessageProvider provider) {\n            this.provider = provider;\n        }\n        public void greet() {\n            // TODO: print provider.getMessage()\n        }\n    }\n\n    public static void main(String[] args) {\n        GreetingService svc = new GreetingService(new WizardMessageProvider());\n        svc.greet();\n    }\n}\n",
                tests(test("Greeting", "null", "Hello, Wizard!")),
                "Explain the IoC principle. Why is constructor injection preferred over field injection?");

        mcQuestion("XB1", QuestionTier.RECALL, "Why is constructor injection preferred over field injection in Spring?",
                new String[]{"Dependencies are explicit, the object is always fully initialised, and it is easy to test", "Constructor injection is faster at runtime", "Field injection requires more annotations", "Spring only supports constructor injection"},
                "Dependencies are explicit, the object is always fully initialised, and it is easy to test",
                "Constructor injection makes dependencies visible and allows the class to be used outside Spring (e.g., in unit tests).");

        tfQuestion("XB1", QuestionTier.RECALL, "@Autowired on a field is generally recommended for production Spring code.",
                "False", "Field injection hides dependencies and makes testing harder. Constructor injection is preferred.");

        codeQuestion("XB1", QuestionTier.APPLICATION, QuestionType.SCENARIO,
                "You want to swap the database implementation in tests without changing your service. What pattern enables this?",
                "@Service\npublic class UserService {\n    private final UserRepository repo; // interface\n    public UserService(UserRepository repo) { this.repo = repo; }\n}",
                "Depend on the interface, use constructor injection. In tests, pass a mock or in-memory implementation. Spring injects the real JPA impl in production.",
                "This is the power of DI + programming to interfaces — easy substitution without changing the service.");

        // XB2 — Spring Beans & Configuration
        subChunk("XB2", "XB", "Spring Beans & Configuration", 2, 60, "SpringBeans.java",
                "<p>How does Spring know which classes to manage — and how do you configure complex objects?</p>",
                "<p>A <strong>Spring Bean</strong> is an object managed by the IoC container. Registered via:</p>"
                + "<ul>"
                + "<li><code>@Component</code> / <code>@Service</code> / <code>@Repository</code> / <code>@Controller</code> — stereotype annotations, auto-detected by component scan</li>"
                + "<li><code>@Bean</code> in a <code>@Configuration</code> class — for third-party objects or complex setup</li>"
                + "</ul>"
                + "<pre><code>// Auto-detected bean\n@Service\npublic class EmailService { ... }\n\n// Manual bean — third-party library\n@Configuration\npublic class AppConfig {\n    @Bean\n    public ObjectMapper objectMapper() {\n        ObjectMapper mapper = new ObjectMapper();\n        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);\n        return mapper; // Spring manages this instance\n    }\n\n    @Bean\n    @Scope(\"prototype\")  // new instance each time\n    public ReportGenerator reportGenerator() {\n        return new ReportGenerator();\n    }\n}</code></pre>"
                + "<p>Default scope is <strong>singleton</strong> — one instance per container.</p>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Use @Service/@Repository/@Component for your own classes. Use @Bean in @Configuration for objects you can't annotate — like ObjectMapper or HikariDataSource."),
                    n("Component scan is Spring Boot's magic — it automatically detects all @Component-annotated classes in your package and registers them as beans.")
                ),
                "<p>Write a <code>@Configuration</code> class with a <code>@Bean</code> method that returns a <code>StringBuilder</code> initialised with <code>\"Arcane: \"</code>. Then show (in a main method) how you would use it.</p>",
                "public class BeanDemo {\n    // Simulating @Configuration without Spring — show the concept\n    static class AppConfig {\n        // TODO: method that returns a StringBuilder pre-filled with \"Arcane: \"\n        static StringBuilder arcaneBuilder() {\n            return null; // TODO\n        }\n    }\n\n    public static void main(String[] args) {\n        StringBuilder sb = AppConfig.arcaneBuilder();\n        sb.append(\"Academy\");\n        System.out.println(sb.toString());\n    }\n}\n",
                tests(test("Builder", "null", "Arcane: Academy")),
                "When would you use @Bean in a @Configuration class instead of @Component? Give two examples.");

        mcQuestion("XB2", QuestionTier.RECALL, "What is the default bean scope in Spring?",
                new String[]{"Singleton — one instance per container", "Prototype — new instance each time", "Request — one per HTTP request", "Session — one per user session"},
                "Singleton — one instance per container",
                "Spring beans are singleton by default. Use @Scope(\"prototype\") for a new instance on each injection.");

        mcQuestion("XB2", QuestionTier.APPLICATION, "You need to configure a third-party HttpClient with custom timeout settings. Which approach is correct?",
                new String[]{"@Bean method in @Configuration returning the configured HttpClient", "@Component on the HttpClient class", "@Autowired the HttpClient directly", "Create it in each service that needs it"},
                "@Bean method in @Configuration returning the configured HttpClient",
                "You can't add @Component to a class you don't own. Use @Bean in @Configuration to configure and expose third-party instances.");

        // XB3 — Spring AOP
        subChunk("XB3", "XB", "Aspect-Oriented Programming", 3, 60, "SpringAop.java",
                "<p>How do you add logging, security, or transactions to hundreds of methods without touching each one?</p>",
                "<p><strong>AOP (Aspect-Oriented Programming)</strong> separates cross-cutting concerns from business logic.</p>"
                + "<p>Key concepts:</p>"
                + "<ul>"
                + "<li><strong>Aspect</strong> — a module of cross-cutting logic (e.g., logging)</li>"
                + "<li><strong>Pointcut</strong> — an expression that matches join points (e.g., all service methods)</li>"
                + "<li><strong>Advice</strong> — code to run at a pointcut (@Before, @After, @Around, @AfterReturning, @AfterThrowing)</li>"
                + "<li><strong>Join Point</strong> — specific execution point matched by a pointcut</li>"
                + "</ul>"
                + "<pre><code>@Aspect\n@Component\npublic class LoggingAspect {\n\n    @Around(\"execution(* com.example.service..*(..))\") // all service methods\n    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {\n        long start = System.currentTimeMillis();\n        Object result = pjp.proceed(); // run the actual method\n        long elapsed = System.currentTimeMillis() - start;\n        log.info(\"{} completed in {}ms\", pjp.getSignature(), elapsed);\n        return result;\n    }\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "AOP is like enchanting a doorway — every spell that passes through it gets logged, timed, or checked, without any spell knowing the enchantment exists. @Transactional works this way."),
                    n("Spring's @Transactional annotation is AOP in action — Spring wraps your method in a transaction proxy without you writing a single try/commit/rollback.")
                ),
                "<p>Simulate AOP manually: create a <code>ServiceProxy</code> that wraps a <code>Calculator</code> and logs before and after calling <code>add(int a, int b)</code>. Print the log and result.</p>",
                "public class AopDemo {\n    interface Calculator {\n        int add(int a, int b);\n    }\n    static class RealCalculator implements Calculator {\n        public int add(int a, int b) { return a + b; }\n    }\n    static class LoggingCalculator implements Calculator {\n        private final Calculator delegate;\n        LoggingCalculator(Calculator delegate) { this.delegate = delegate; }\n        public int add(int a, int b) {\n            // TODO: print \"Before: add\"\n            int result = delegate.add(a, b);\n            // TODO: print \"After: add\"\n            return result;\n        }\n    }\n    public static void main(String[] args) {\n        Calculator calc = new LoggingCalculator(new RealCalculator());\n        System.out.println(calc.add(3, 4));\n    }\n}\n",
                tests(test("AOP proxy", "null", "Before: add\nAfter: add\n7")),
                "Explain AOP. Name three real-world uses of AOP in Spring applications.");

        mcQuestion("XB3", QuestionTier.RECALL, "Which Spring annotation is implemented using AOP under the hood?",
                new String[]{"@Transactional", "@Component", "@Autowired", "@Value"},
                "@Transactional",
                "Spring's @Transactional wraps methods in a transaction via an AOP proxy — begin, proceed, commit/rollback.");

        tfQuestion("XB3", QuestionTier.APPLICATION, "@Around advice can prevent the target method from executing at all.",
                "True", "@Around receives a ProceedingJoinPoint — calling proceed() runs the method. You can choose not to call it.");

        // XB4 — Spring Events & Environment
        subChunk("XB4", "XB", "Spring Events & Environment Abstraction", 4, 50, "SpringEvents.java",
                "<p>How do loosely coupled components communicate without knowing about each other?</p>",
                "<p><strong>Spring Application Events</strong> provide an event bus within the IoC container:</p>"
                + "<pre><code>// Define event\npublic class OrderPlacedEvent {\n    private final Order order;\n    public OrderPlacedEvent(Order order) { this.order = order; }\n    public Order getOrder() { return order; }\n}\n\n// Publish event\n@Service\npublic class OrderService {\n    private final ApplicationEventPublisher events;\n    public void placeOrder(Order order) {\n        orderRepo.save(order);\n        events.publishEvent(new OrderPlacedEvent(order)); // fire and forget\n    }\n}\n\n// Listen for event\n@Component\npublic class EmailNotificationHandler {\n    @EventListener\n    public void onOrderPlaced(OrderPlacedEvent event) {\n        emailService.sendConfirmation(event.getOrder());\n    }\n}</code></pre>"
                + "<p><strong>Environment Abstraction</strong>: <code>@Value(\"${property.name}\")</code> and <code>@ConfigurationProperties</code> bind application.yml values into beans.</p>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Events are the whisper network of the Academy. OrderService announces an order was placed — it doesn't care who listens. Email, Inventory, Analytics — each handler subscribes independently."),
                    e("@Value usage", "@Value(\"${app.max-retries:3}\")\nprivate int maxRetries; // reads from application.yml, defaults to 3")
                ),
                "<p>Simulate Spring events without the framework: create an <code>EventBus</code> with <code>publish(String event)</code> and <code>subscribe(Consumer&lt;String&gt; handler)</code>. Publish <code>\"OrderPlaced\"</code> and print it from two subscribers.</p>",
                "import java.util.ArrayList;\nimport java.util.List;\nimport java.util.function.Consumer;\n\npublic class EventBusDemo {\n    static class EventBus {\n        private List<Consumer<String>> handlers = new ArrayList<>();\n        // TODO: subscribe(Consumer<String> handler)\n        // TODO: publish(String event) — calls all handlers\n        void subscribe(Consumer<String> handler) {}\n        void publish(String event) {}\n    }\n\n    public static void main(String[] args) {\n        EventBus bus = new EventBus();\n        bus.subscribe(e -> System.out.println(\"Email: \" + e));\n        bus.subscribe(e -> System.out.println(\"Inventory: \" + e));\n        bus.publish(\"OrderPlaced\");\n    }\n}\n",
                tests(test("Two subscribers", "null", "Email: OrderPlaced\nInventory: OrderPlaced")),
                "Explain Spring Application Events. What decoupling benefit do they provide over direct method calls?");

        mcQuestion("XB4", QuestionTier.RECALL, "What annotation marks a method as a Spring event listener?",
                new String[]{"@EventListener", "@Subscribe", "@Listen", "@Handle"},
                "@EventListener",
                "@EventListener on a method in a Spring bean registers it to receive matching application events.");

        tfQuestion("XB4", QuestionTier.RECALL, "@Value can inject values from application.yml into Spring beans.",
                "True", "@Value(\"${property.name}\") reads properties from the Spring Environment (application.yml, env vars, etc.).");
    }

    // ── XC: Spring Boot ────────────────────────────────────────────────────

    private void seedChunkXC() {
        chunk("XC", "Spring Boot", "\uD83D\uDE80", 31, LearnerPath.EXPERT, "XB");

        subChunk("XC1", "XC", "Spring Boot Auto-Configuration & Starters", 1, 60, "SpringBootIntro.java",
                "<p>Why does a Spring Boot app need only a few lines of code to start a full web server?</p>",
                "<p><strong>Spring Boot</strong> builds on Spring Framework with three key innovations:</p>"
                + "<ul>"
                + "<li><strong>Auto-configuration</strong> — inspects the classpath and configures beans automatically (see a JDBC driver? configure a DataSource)</li>"
                + "<li><strong>Starters</strong> — curated dependency sets. <code>spring-boot-starter-web</code> pulls in Tomcat + Spring MVC + Jackson.</li>"
                + "<li><strong>Opinionated defaults</strong> — sensible configuration you can override</li>"
                + "</ul>"
                + "<pre><code>@SpringBootApplication // = @Configuration + @EnableAutoConfiguration + @ComponentScan\npublic class AcademyApplication {\n    public static void main(String[] args) {\n        SpringApplication.run(AcademyApplication.class, args);\n    }\n}</code></pre>"
                + "<p>Auto-configuration classes are listed in <code>spring.factories</code> / <code>AutoConfiguration.imports</code> — Spring only activates them when conditions are met (<code>@ConditionalOnClass</code>, <code>@ConditionalOnMissingBean</code>).</p>",
                story(
                    n("Where Spring Framework hands you a blank spellbook, Spring Boot hands you one already filled with the most-used incantations — ready to override or extend."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Auto-configuration is not magic — it's convention. Spring sees jackson-databind on the classpath and configures an ObjectMapper for you. Add your own @Bean and Spring steps aside.")
                ),
                "<p>List the three meta-annotations that <code>@SpringBootApplication</code> combines. Then explain what each one does in one sentence each. Output the answers on separate lines.</p>",
                "public class SpringBootAnnotations {\n    public static void main(String[] args) {\n        // Print the three annotations that @SpringBootApplication combines\n        System.out.println(\"@Configuration\");\n        System.out.println(\"@EnableAutoConfiguration\");\n        System.out.println(\"@ComponentScan\");\n    }\n}\n",
                tests(test("Annotations", "null", "@Configuration\n@EnableAutoConfiguration\n@ComponentScan")),
                "Explain Spring Boot auto-configuration. How does Spring decide whether to apply an auto-configuration class?");

        mcQuestion("XC1", QuestionTier.RECALL, "What does @SpringBootApplication combine?",
                new String[]{"@Configuration, @EnableAutoConfiguration, @ComponentScan", "@Controller, @Service, @Repository", "@Bean, @Autowired, @Value", "@Transactional, @Cacheable, @Async"},
                "@Configuration, @EnableAutoConfiguration, @ComponentScan",
                "@SpringBootApplication is a composed annotation that enables component scanning, configuration, and auto-configuration in one.");

        tfQuestion("XC1", QuestionTier.RECALL, "Spring Boot auto-configuration can be overridden by providing your own @Bean.",
                "True", "Spring Boot uses @ConditionalOnMissingBean — if you define a bean of the same type, Spring Boot's auto-configured one backs off.");

        subChunk("XC2", "XC", "Application Properties & Profiles", 2, 50, "SpringProfiles.java",
                "<p>How do you configure your app differently for dev, test, and production without changing code?</p>",
                "<p>Spring Boot reads configuration from <code>application.yml</code> (or <code>.properties</code>). Use <strong>profiles</strong> to override settings per environment.</p>"
                + "<pre><code># application.yml (defaults)\nspring:\n  datasource:\n    url: jdbc:h2:mem:testdb\napp:\n  feature-flag: false\n\n# application-prod.yml (activated with --spring.profiles.active=prod)\nspring:\n  datasource:\n    url: jdbc:postgresql://prod-host/academy\napp:\n  feature-flag: true</code></pre>"
                + "<p>Bind properties to a class with <code>@ConfigurationProperties</code>:</p>"
                + "<pre><code>@ConfigurationProperties(prefix = \"app\")\n@Component\npublic class AppProperties {\n    private boolean featureFlag;\n    // getters/setters\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Profiles are the Academy's environmental seals. The same spell behaves differently in the training grounds vs. the field. Never hard-code environment-specific values — use application.yml.")
                ),
                "<p>Simulate profile-aware configuration: if a <code>String profile</code> is <code>\"prod\"</code>, print the prod DB URL; otherwise print the dev URL.</p>",
                "public class ProfileDemo {\n    static String getDatabaseUrl(String profile) {\n        // TODO: return \"jdbc:postgresql://prod/academy\" for \"prod\"\n        // return \"jdbc:h2:mem:devdb\" otherwise\n        return null;\n    }\n    public static void main(String[] args) {\n        System.out.println(getDatabaseUrl(\"dev\"));\n        System.out.println(getDatabaseUrl(\"prod\"));\n    }\n}\n",
                tests(test("Dev URL", "null", "jdbc:h2:mem:devdb\njdbc:postgresql://prod/academy")),
                "Explain Spring profiles. How would you configure different database URLs for dev and prod?");

        mcQuestion("XC2", QuestionTier.RECALL, "How do you activate a Spring profile at startup?",
                new String[]{"--spring.profiles.active=prod JVM argument or SPRING_PROFILES_ACTIVE env var", "@ActiveProfile annotation on the main class", "Setting profile=prod in application.yml", "Using @SpringBootApplication(profile=\"prod\")"},
                "--spring.profiles.active=prod JVM argument or SPRING_PROFILES_ACTIVE env var",
                "Profiles are activated via JVM args, environment variables, or application.yml's spring.profiles.active.");

        subChunk("XC3", "XC", "Actuator & Observability", 3, 50, "SpringActuator.java",
                "<p>How do you know if your production application is healthy — without reading log files?</p>",
                "<p><strong>Spring Boot Actuator</strong> exposes management endpoints over HTTP:</p>"
                + "<ul>"
                + "<li><code>/actuator/health</code> — UP/DOWN with component details</li>"
                + "<li><code>/actuator/metrics</code> — JVM memory, HTTP request counts, custom metrics</li>"
                + "<li><code>/actuator/info</code> — app version, build info</li>"
                + "<li><code>/actuator/env</code> — resolved properties (sensitive — restrict in production)</li>"
                + "</ul>"
                + "<pre><code># application.yml\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: health, info, metrics\n  endpoint:\n    health:\n      show-details: when-authorized</code></pre>"
                + "<p>Custom health indicator:</p>"
                + "<pre><code>@Component\npublic class DatabaseHealthIndicator implements HealthIndicator {\n    public Health health() {\n        boolean dbOk = checkDatabase();\n        return dbOk ? Health.up().build() : Health.down().withDetail(\"reason\", \"unreachable\").build();\n    }\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Actuator is your crystal ball for production systems. Load balancers ping /actuator/health every 30 seconds. If it returns DOWN, traffic stops routing to that instance automatically.")
                ),
                "<p>Simulate a health check: create a <code>HealthChecker</code> with a <code>boolean dbReachable</code> field. If reachable, print <code>UP</code>; otherwise print <code>DOWN: database unreachable</code>.</p>",
                "public class ActuatorDemo {\n    static class HealthChecker {\n        private boolean dbReachable;\n        HealthChecker(boolean dbReachable) { this.dbReachable = dbReachable; }\n        String check() {\n            // TODO: return \"UP\" or \"DOWN: database unreachable\"\n            return null;\n        }\n    }\n    public static void main(String[] args) {\n        System.out.println(new HealthChecker(true).check());\n        System.out.println(new HealthChecker(false).check());\n    }\n}\n",
                tests(test("Health", "null", "UP\nDOWN: database unreachable")),
                "What Actuator endpoints would you expose in production? Which would you restrict and why?");

        mcQuestion("XC3", QuestionTier.RECALL, "Which Actuator endpoint do load balancers typically use?",
                new String[]{"/actuator/health", "/actuator/metrics", "/actuator/info", "/actuator/env"},
                "/actuator/health",
                "Health is the standard liveness/readiness endpoint. Load balancers route traffic away from DOWN instances.");

        subChunk("XC4", "XC", "Build, Testing & Deployment", 4, 50, "SpringDeployment.java",
                "<p>How does a Spring Boot app get from developer laptop to production server?</p>",
                "<p>Spring Boot apps package as <strong>executable JARs</strong> (fat JARs) — self-contained with embedded Tomcat:</p>"
                + "<pre><code># Build\n./mvnw package -DskipTests\n\n# Run\njava -jar target/academy-1.0.jar --spring.profiles.active=prod\n</code></pre>"
                + "<p><strong>Docker</strong> packaging:</p>"
                + "<pre><code>FROM eclipse-temurin:21-jdk\nWORKDIR /app\nCOPY target/*.jar app.jar\nEXPOSE 8080\nENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]\n</code></pre>"
                + "<p><strong>Testing layers</strong>:</p>"
                + "<ul>"
                + "<li><code>@WebMvcTest</code> — controller layer only, no full context</li>"
                + "<li><code>@DataJpaTest</code> — JPA layer with in-memory DB</li>"
                + "<li><code>@SpringBootTest</code> — full integration test (slower)</li>"
                + "</ul>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "The fat JAR is your sealed spell tome — everything needed to cast the spell, contained in one artefact. Ship that JAR anywhere Java runs and the application comes alive.")
                ),
                "<p>Write a method <code>buildCommand(String profile)</code> that returns the correct <code>java -jar</code> command string for a given profile. E.g., <code>\"prod\"</code> → <code>\"java -jar app.jar --spring.profiles.active=prod\"</code>.</p>",
                "public class DeployDemo {\n    static String buildCommand(String profile) {\n        // TODO\n        return null;\n    }\n    public static void main(String[] args) {\n        System.out.println(buildCommand(\"prod\"));\n        System.out.println(buildCommand(\"dev\"));\n    }\n}\n",
                tests(test("Commands", "null", "java -jar app.jar --spring.profiles.active=prod\njava -jar app.jar --spring.profiles.active=dev")),
                "Explain the difference between @WebMvcTest, @DataJpaTest, and @SpringBootTest. When would you use each?");

        mcQuestion("XC4", QuestionTier.RECALL, "What is a 'fat JAR' in Spring Boot?",
                new String[]{"An executable JAR containing the app and all its dependencies including an embedded server", "A JAR with only source files", "A compressed ZIP of the project", "A WAR file deployed to a separate Tomcat"},
                "An executable JAR containing the app and all its dependencies including an embedded server",
                "Spring Boot packages everything — including Tomcat — into one runnable JAR. No external server required.");
    }

    // ── XD: REST APIs with Spring MVC ──────────────────────────────────────

    private void seedChunkXD() {
        chunk("XD", "REST APIs with Spring MVC", "\uD83C\uDF10", 32, LearnerPath.EXPERT, "XC");

        subChunk("XD1", "XD", "RESTful API Design Principles", 1, 60, "RestDesign.java",
                "<p>What makes an API 'RESTful' — and why does it matter?</p>",
                "<p><strong>REST (Representational State Transfer)</strong> is an architectural style for APIs using HTTP. Key constraints:</p>"
                + "<ul>"
                + "<li><strong>Stateless</strong> — each request contains all needed information; no server-side session</li>"
                + "<li><strong>Resource-based URLs</strong> — nouns, not verbs: <code>/orders</code> not <code>/getOrders</code></li>"
                + "<li><strong>HTTP verbs</strong> — GET (read), POST (create), PUT/PATCH (update), DELETE (remove)</li>"
                + "<li><strong>Status codes</strong> — 200 OK, 201 Created, 400 Bad Request, 404 Not Found, 409 Conflict, 500 Server Error</li>"
                + "</ul>"
                + "<pre><code>GET    /api/orders          → list all orders\nGET    /api/orders/{id}    → get one order\nPOST   /api/orders          → create new order\nPUT    /api/orders/{id}    → replace order\nPATCH  /api/orders/{id}    → partial update\nDELETE /api/orders/{id}    → delete order</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "REST is the language of the internet. Your API is a contract — break it carelessly and every client that relies on it breaks too. Design it thoughtfully before you write a line of code."),
                    n("A common mistake: using verbs in URLs. /api/createOrder is wrong. The verb is in the HTTP method. POST /api/orders is right.")
                ),
                "<p>Given these poorly designed endpoints, rewrite them following REST conventions. Print corrected lines.</p>",
                "public class RestDesignDemo {\n    public static void main(String[] args) {\n        // Bad: /api/getUser, /api/deleteUser/5, /api/createProduct\n        // TODO: print the correct REST URLs for each operation\n        // Get user with id 3, Delete user with id 5, Create product\n        System.out.println(\"GET /api/users/3\");\n        System.out.println(\"DELETE /api/users/5\");\n        System.out.println(\"POST /api/products\");\n    }\n}\n",
                tests(test("REST URLs", "null", "GET /api/users/3\nDELETE /api/users/5\nPOST /api/products")),
                "Explain the key REST constraints. What HTTP status code would you return when a resource is not found?");

        mcQuestion("XD1", QuestionTier.RECALL, "Which HTTP method should be used to partially update a resource?",
                new String[]{"PATCH", "PUT", "POST", "UPDATE"},
                "PATCH",
                "PATCH applies a partial update. PUT replaces the entire resource.");

        codeQuestion("XD1", QuestionTier.APPLICATION, QuestionType.SCENARIO,
                "A client sends a POST to create a user and the user already exists. What HTTP status code should the API return?",
                "POST /api/users\n{ \"username\": \"eldrin\" }  // already exists",
                "409 Conflict — indicates the request conflicts with the current state of the resource",
                "409 signals a duplicate/conflict. 400 is for malformed requests. 422 is for semantically invalid input.");

        subChunk("XD2", "XD", "@RestController & Request Mappings", 2, 60, "SpringController.java",
                "<p>How do HTTP requests get routed to Java methods in Spring?</p>",
                "<p>Spring MVC maps HTTP requests to handler methods using annotations:</p>"
                + "<pre><code>@RestController  // @Controller + @ResponseBody\n@RequestMapping(\"/api/orders\")\npublic class OrderController {\n\n    @GetMapping             // GET /api/orders\n    public List&lt;OrderDto&gt; list() { ... }\n\n    @GetMapping(\"/{id}\")    // GET /api/orders/5\n    public OrderDto getById(@PathVariable Long id) { ... }\n\n    @PostMapping            // POST /api/orders\n    @ResponseStatus(HttpStatus.CREATED)\n    public OrderDto create(@RequestBody CreateOrderRequest req) { ... }\n\n    @DeleteMapping(\"/{id}\") // DELETE /api/orders/5\n    @ResponseStatus(HttpStatus.NO_CONTENT)\n    public void delete(@PathVariable Long id) { ... }\n\n    @GetMapping(\"/search\")  // GET /api/orders/search?status=PENDING\n    public List&lt;OrderDto&gt; search(@RequestParam String status) { ... }\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "@RestController is the gateway spell. Every request enters here, gets unpacked from JSON, routed to the right method, and the result is packed back into JSON for the client.")
                ),
                "<p>Write a simple controller simulation: a class with a <code>getUser(long id)</code> method that returns <code>\"User: \" + id</code>, and a <code>createUser(String name)</code> method returning <code>\"Created: \" + name</code>. Call both from main.</p>",
                "public class ControllerDemo {\n    static class UserController {\n        // TODO: getUser(long id) returns \"User: \" + id\n        // TODO: createUser(String name) returns \"Created: \" + name\n        String getUser(long id) { return null; }\n        String createUser(String name) { return null; }\n    }\n    public static void main(String[] args) {\n        UserController ctrl = new UserController();\n        System.out.println(ctrl.getUser(42));\n        System.out.println(ctrl.createUser(\"Merlin\"));\n    }\n}\n",
                tests(test("Controller methods", "null", "User: 42\nCreated: Merlin")),
                "What is the difference between @Controller and @RestController? What does @ResponseBody do?");

        mcQuestion("XD2", QuestionTier.RECALL, "How do you extract a value from the URL path in Spring MVC?",
                new String[]{"@PathVariable — e.g. @GetMapping(\"/{id}\") with @PathVariable Long id", "@RequestParam — e.g. ?id=5", "@RequestBody — from the request body", "@Header — from HTTP headers"},
                "@PathVariable — e.g. @GetMapping(\"/{id}\") with @PathVariable Long id",
                "@PathVariable binds URI template variables. @RequestParam binds query string parameters.");

        subChunk("XD3", "XD", "Validation, DTOs & Global Error Handling", 3, 60, "RestValidation.java",
                "<p>What happens when a client sends invalid data — and how do you return clean error responses?</p>",
                "<p><strong>DTOs (Data Transfer Objects)</strong> decouple your API contract from your domain model.</p>"
                + "<p><strong>Bean Validation</strong> (JSR-380) annotates DTOs:</p>"
                + "<pre><code>public record CreateUserRequest(\n    @NotBlank String username,\n    @Email String email,\n    @Size(min = 8) String password\n) {}\n\n@PostMapping\npublic ResponseEntity&lt;UserDto&gt; create(@Valid @RequestBody CreateUserRequest req) {\n    // Spring validates req before this method runs\n    // Returns 400 automatically if invalid\n}\n</code></pre>"
                + "<p><strong>Global exception handler</strong>:</p>"
                + "<pre><code>@RestControllerAdvice\npublic class GlobalExceptionHandler {\n    @ExceptionHandler(MethodArgumentNotValidException.class)\n    @ResponseStatus(HttpStatus.BAD_REQUEST)\n    public Map&lt;String, String&gt; handleValidation(MethodArgumentNotValidException ex) {\n        return ex.getBindingResult().getFieldErrors().stream()\n            .collect(toMap(FieldError::getField, FieldError::getDefaultMessage));\n    }\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Never trust client input. The @Valid annotation is your first ward against chaos — it rejects malformed requests before they reach your business logic. Your GlobalExceptionHandler is the fallback ward.")
                ),
                "<p>Implement a <code>validate(String username, String email)</code> method: return <code>\"OK\"</code> if username is non-empty and email contains <code>\"@\"</code>; otherwise return <code>\"INVALID\"</code>.</p>",
                "public class ValidationDemo {\n    static String validate(String username, String email) {\n        // TODO: return \"OK\" if username non-blank AND email contains \"@\"\n        // otherwise return \"INVALID\"\n        return null;\n    }\n    public static void main(String[] args) {\n        System.out.println(validate(\"eldrin\", \"eldrin@academy.com\"));\n        System.out.println(validate(\"\", \"notanemail\"));\n    }\n}\n",
                tests(test("Valid", "null", "OK\nINVALID")),
                "Why use DTOs instead of passing domain entities directly to/from API endpoints?");

        mcQuestion("XD3", QuestionTier.RECALL, "What annotation triggers Bean Validation on a @RequestBody parameter?",
                new String[]{"@Valid (or @Validated)", "@NotNull", "@Validated only", "@Constraints"},
                "@Valid (or @Validated)",
                "@Valid triggers JSR-380 constraint validation on the annotated parameter. Spring returns 400 if constraints fail.");

        subChunk("XD4", "XD", "API Security & Documentation", 4, 50, "ApiSecurity.java",
                "<p>How do you secure your REST API and help clients understand how to use it?</p>",
                "<p><strong>JWT (JSON Web Token)</strong> authentication for stateless APIs:</p>"
                + "<pre><code>// Request flow\n// 1. POST /api/auth/login → {username, password}\n// 2. Server validates, returns JWT token\n// 3. Client sends: Authorization: Bearer &lt;token&gt; on every subsequent request\n// 4. Spring Security filter validates JWT on each request</code></pre>"
                + "<p>Spring Security config:</p>"
                + "<pre><code>@Bean\npublic SecurityFilterChain filterChain(HttpSecurity http) throws Exception {\n    return http\n        .csrf(AbstractHttpConfigurer::disable)  // REST APIs use tokens not cookies\n        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))\n        .authorizeHttpRequests(auth -> auth\n            .requestMatchers(\"/api/auth/**\").permitAll()\n            .anyRequest().authenticated())\n        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)\n        .build();\n}</code></pre>"
                + "<p><strong>OpenAPI/Swagger</strong>: add <code>springdoc-openapi</code> dependency → <code>/swagger-ui.html</code> auto-generated.</p>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "A public API without authentication is an unlocked treasury. JWT tokens are like signet rings — they prove who you are without requiring a stateful session on the server.")
                ),
                "<p>Simulate JWT structure: a token has format <code>header.payload.signature</code>. Write <code>extractPayload(String token)</code> that splits on <code>\".\"</code> and returns the middle part.</p>",
                "public class JwtDemo {\n    static String extractPayload(String token) {\n        // TODO: split by \".\" and return the second part (index 1)\n        return null;\n    }\n    public static void main(String[] args) {\n        String token = \"eyJhbGc.eyJ1c2VyIjoiZWxkcmluIn0.signature\";\n        System.out.println(extractPayload(token));\n    }\n}\n",
                tests(test("Payload", "null", "eyJ1c2VyIjoiZWxkcmluIn0")),
                "Explain JWT authentication flow. Why do REST APIs use stateless token auth rather than server-side sessions?");

        mcQuestion("XD4", QuestionTier.RECALL, "Why is CSRF protection typically disabled for REST APIs using JWT?",
                new String[]{"JWT is sent in the Authorization header, not a cookie, so CSRF attacks don't apply", "JWT is more secure than CSRF tokens", "REST APIs don't have forms to submit", "CSRF protection slows down API calls"},
                "JWT is sent in the Authorization header, not a cookie, so CSRF attacks don't apply",
                "CSRF exploits browser cookie auto-attachment. JWTs in Authorization headers are not auto-sent by browsers, so CSRF is not a threat.");
    }

    // ── XE: Spring Data JPA ────────────────────────────────────────────────

    private void seedChunkXE() {
        chunk("XE", "Spring Data JPA", "\uD83D\uDDC4\uFE0F", 33, LearnerPath.EXPERT, "XC", "PI");

        subChunk("XE1", "XE", "JPA Entities & Mapping", 1, 60, "JpaEntities.java",
                "<p>How does Java talk to a relational database without SQL everywhere?</p>",
                "<p><strong>JPA (Jakarta Persistence API)</strong> maps Java objects to database tables. Key annotations:</p>"
                + "<pre><code>@Entity\n@Table(name = \"orders\")\npublic class Order {\n    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n    private Long id;\n\n    @Column(nullable = false)\n    private String customerName;\n\n    @Enumerated(EnumType.STRING)\n    private OrderStatus status;\n\n    @CreationTimestamp\n    private LocalDateTime createdAt;\n\n    // getters/setters or use @Data (Lombok)\n}</code></pre>"
                + "<p>Hibernate auto-generates SQL: <code>ddl-auto: update</code> creates/alters tables on startup.</p>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "JPA is the bridge between Java's object world and the relational database world. Annotate your class correctly and Hibernate handles the translation — CREATE TABLE, INSERT, SELECT — all generated.")
                ),
                "<p>Create a plain Java class <code>Product</code> with fields: <code>Long id</code>, <code>String name</code>, <code>double price</code>. Override <code>toString()</code> returning <code>\"Product[id=1, name=Wand, price=29.99]\"</code>. Print an instance.</p>",
                "public class JpaEntityDemo {\n    static class Product {\n        Long id;\n        String name;\n        double price;\n        Product(Long id, String name, double price) {\n            this.id = id; this.name = name; this.price = price;\n        }\n        // TODO: toString() returning \"Product[id=\" + id + \", name=\" + name + \", price=\" + price + \"]\"\n        public String toString() { return null; }\n    }\n    public static void main(String[] args) {\n        System.out.println(new Product(1L, \"Wand\", 29.99));\n    }\n}\n",
                tests(test("toString", "null", "Product[id=1, name=Wand, price=29.99]")),
                "What JPA annotation generates the primary key automatically? What GenerationType options exist?");

        mcQuestion("XE1", QuestionTier.RECALL, "What does @GeneratedValue(strategy = GenerationType.IDENTITY) do?",
                new String[]{"Delegates primary key generation to the database's auto-increment column", "Generates UUIDs in Java", "Uses a database sequence", "Manually assigns IDs"},
                "Delegates primary key generation to the database's auto-increment column",
                "IDENTITY uses the database's built-in auto-increment (e.g., PostgreSQL SERIAL / MySQL AUTO_INCREMENT).");

        subChunk("XE2", "XE", "Spring Data Repositories", 2, 60, "SpringDataRepos.java",
                "<p>How do you query the database without writing a single SQL statement?</p>",
                "<p>Spring Data JPA generates repository implementations at runtime:</p>"
                + "<pre><code>public interface OrderRepository extends JpaRepository&lt;Order, Long&gt; {\n\n    // Spring generates SQL from method name\n    List&lt;Order&gt; findByStatus(OrderStatus status);\n    List&lt;Order&gt; findByCustomerNameContainingIgnoreCase(String name);\n    Optional&lt;Order&gt; findTopByOrderByCreatedAtDesc();\n\n    // Custom JPQL query\n    @Query(\"SELECT o FROM Order o WHERE o.totalAmount > :amount\")\n    List&lt;Order&gt; findExpensive(@Param(\"amount\") BigDecimal amount);\n\n    // Native SQL\n    @Query(value = \"SELECT * FROM orders WHERE status = 'PENDING' LIMIT 10\",\n           nativeQuery = true)\n    List&lt;Order&gt; findPendingNative();\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Derived query methods feel like magic — you name the method and Spring writes the SQL. Under the hood it parses your method name at startup and generates a JPQL query. Use @Query for anything complex.")
                ),
                "<p>Demonstrate derived query naming: write method signatures (as strings) for: find by email, find all where active is true, find top 5 ordered by createdAt descending. Print each.</p>",
                "public class DerivedQueryDemo {\n    public static void main(String[] args) {\n        // TODO: print the method signatures as strings\n        System.out.println(\"findByEmail(String email)\");\n        System.out.println(\"findByActiveTrue()\");\n        System.out.println(\"findTop5ByOrderByCreatedAtDesc()\");\n    }\n}\n",
                tests(test("Methods", "null", "findByEmail(String email)\nfindByActiveTrue()\nfindTop5ByOrderByCreatedAtDesc()")),
                "How does Spring Data JPA generate queries from method names? When should you use @Query instead?");

        mcQuestion("XE2", QuestionTier.RECALL, "What does JpaRepository provide over CrudRepository?",
                new String[]{"Pagination and sorting support (findAll(Pageable), findAll(Sort)) and batch operations", "More security features", "Better performance", "Support for NoSQL databases"},
                "Pagination and sorting support (findAll(Pageable), findAll(Sort)) and batch operations",
                "JpaRepository extends PagingAndSortingRepository which extends CrudRepository, adding pagination and sorting.");

        subChunk("XE3", "XE", "Relationships, Fetching & N+1 Problem", 3, 60, "JpaRelationships.java",
                "<p>How do you model database relationships in Java — and what is the N+1 query trap?</p>",
                "<p>JPA relationship annotations:</p>"
                + "<pre><code>@Entity\npublic class Order {\n    @OneToMany(mappedBy = \"order\", cascade = CascadeType.ALL, fetch = FetchType.LAZY)\n    private List&lt;OrderLine&gt; lines;\n}\n\n@Entity\npublic class OrderLine {\n    @ManyToOne(fetch = FetchType.LAZY)\n    @JoinColumn(name = \"order_id\")\n    private Order order;\n}</code></pre>"
                + "<p><strong>N+1 Problem</strong>: fetching 100 orders then lazily loading lines = 101 queries. Fix with <code>JOIN FETCH</code>:</p>"
                + "<pre><code>@Query(\"SELECT o FROM Order o LEFT JOIN FETCH o.lines WHERE o.status = :status\")\nList&lt;Order&gt; findWithLines(@Param(\"status\") OrderStatus status);</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "The N+1 problem is the silent performance killer. You fetch 100 orders. Then your code accesses each order's lines — 100 more queries. One query to fetch them all with JOIN FETCH is the cure.")
                ),
                "<p>Simulate the N+1 problem in pure Java: given a list of 3 order IDs, print the number of 'queries' needed if you fetch lines separately for each order (N+1), vs. one join query.</p>",
                "public class NPlusOneDemo {\n    public static void main(String[] args) {\n        int orderCount = 3;\n        // N+1 approach\n        int nPlusOneQueries = 1 + orderCount; // 1 for orders + N for lines\n        System.out.println(\"N+1 queries: \" + nPlusOneQueries);\n        // JOIN FETCH approach\n        int joinQueries = 1;\n        System.out.println(\"JOIN FETCH queries: \" + joinQueries);\n    }\n}\n",
                tests(test("Query counts", "null", "N+1 queries: 4\nJOIN FETCH queries: 1")),
                "Explain the N+1 query problem. How does JOIN FETCH solve it?");

        mcQuestion("XE3", QuestionTier.RECALL, "What is the default fetch type for @OneToMany?",
                new String[]{"LAZY — related entities are loaded on demand", "EAGER — related entities are always loaded", "NONE — must be specified manually", "BATCH — loaded in batches of 10"},
                "LAZY — related entities are loaded on demand",
                "@OneToMany defaults to LAZY. @ManyToOne defaults to EAGER. Prefer LAZY + JOIN FETCH for controlled loading.");

        subChunk("XE4", "XE", "Projections, Transactions & Auditing", 4, 50, "JpaAdvanced.java",
                "<p>How do you load only the fields you need — and track who changed data and when?</p>",
                "<p><strong>Projections</strong> select specific fields without loading the full entity:</p>"
                + "<pre><code>public interface OrderSummary {\n    Long getId();\n    String getCustomerName();\n    OrderStatus getStatus();\n}\n\nList&lt;OrderSummary&gt; findByStatus(OrderStatus status); // returns lightweight projections</code></pre>"
                + "<p><strong>@Transactional</strong> demarcates database transaction boundaries:</p>"
                + "<pre><code>@Service\n@Transactional(readOnly = true) // default for all methods\npublic class OrderQueryService {\n    @Transactional  // write transaction for this method\n    public Order createOrder(CreateOrderRequest req) { ... }\n}</code></pre>"
                + "<p><strong>Auditing</strong>:</p>"
                + "<pre><code>@EntityListeners(AuditingEntityListener.class)\npublic class Order {\n    @CreatedDate private LocalDateTime createdAt;\n    @LastModifiedDate private LocalDateTime updatedAt;\n    @CreatedBy private String createdBy;\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Never load a full entity when you only need two fields — projections are the scalpel to the entity's sledgehammer. And always annotate service methods with @Transactional — it defines your unit of work.")
                ),
                "<p>Demonstrate transaction simulation: create a <code>TransactionManager</code> with <code>beginTransaction()</code>, <code>commit()</code>, and <code>rollback()</code>. Run a successful transaction and print Begin, Commit. Then a failed one: Begin, Rollback.</p>",
                "public class TransactionDemo {\n    static class TransactionManager {\n        void beginTransaction() { System.out.println(\"Begin\"); }\n        void commit() { System.out.println(\"Commit\"); }\n        void rollback() { System.out.println(\"Rollback\"); }\n        void execute(boolean success) {\n            beginTransaction();\n            // TODO: commit if success, rollback otherwise\n        }\n    }\n    public static void main(String[] args) {\n        TransactionManager tm = new TransactionManager();\n        tm.execute(true);\n        tm.execute(false);\n    }\n}\n",
                tests(test("Transactions", "null", "Begin\nCommit\nBegin\nRollback")),
                "What does @Transactional(readOnly = true) do and why should read operations use it?");

        mcQuestion("XE4", QuestionTier.RECALL, "What is the benefit of using JPA interface projections?",
                new String[]{"Load only specific columns, reducing memory and query overhead", "Enable lazy loading", "Allow modification of entities", "Provide built-in caching"},
                "Load only specific columns, reducing memory and query overhead",
                "Projections tell JPA to SELECT only the needed columns. For list endpoints returning summaries, this is significantly more efficient.");

        tfQuestion("XE4", QuestionTier.RECALL, "@Transactional(readOnly = true) can improve performance for read-only operations.",
                "True", "readOnly=true hints to the JPA provider (e.g., Hibernate) and database driver to optimize for reads — no dirty checking, possible read replicas.");
    }
}
