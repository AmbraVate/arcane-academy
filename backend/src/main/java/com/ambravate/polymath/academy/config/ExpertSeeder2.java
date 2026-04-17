package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

@Component
public class ExpertSeeder2 extends AbstractChunkSeeder {

    public ExpertSeeder2(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                         QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {
        seedChunkXF();
        seedChunkXG();
        seedChunkXH();
        seedChunkXI();
        seedChunkXJ();
    }

    // ── XF: Spring Security ────────────────────────────────────────────────

    private void seedChunkXF() {
        chunk("XF", "Spring Security", "\uD83D\uDD12", 34, LearnerPath.EXPERT, "XC");

        subChunk("XF1", "XF", "Authentication vs Authorisation", 1, 60, "AuthConcepts.java",
                "<p>Before you secure anything, you must understand the two pillars of security — and why they are different.</p>",
                "<p><strong>Authentication</strong> — Who are you? Verify identity (login, JWT, OAuth2).<br>"
                + "<strong>Authorisation</strong> — What can you do? Check permissions after authentication.</p>"
                + "<p>Spring Security's architecture:</p>"
                + "<ul>"
                + "<li><code>SecurityFilterChain</code> — ordered filter chain that every request passes through</li>"
                + "<li><code>AuthenticationManager</code> — validates credentials</li>"
                + "<li><code>UserDetailsService</code> — loads user data for authentication</li>"
                + "<li><code>GrantedAuthority</code> — a permission string like <code>ROLE_ADMIN</code></li>"
                + "</ul>"
                + "<pre><code>@Bean\npublic SecurityFilterChain filterChain(HttpSecurity http) throws Exception {\n    return http\n        .authorizeHttpRequests(auth -> auth\n            .requestMatchers(\"/api/public/**\").permitAll()\n            .requestMatchers(\"/api/admin/**\").hasRole(\"ADMIN\")\n            .anyRequest().authenticated())\n        .build();\n}</code></pre>",
                story(
                    n("The gates of the Academy have two guardians. The first asks: who are you? The second asks: are you allowed here?"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Authentication without authorisation is a door with a lock but no keys. Authorisation without authentication is a VIP room with no guest list. You need both."),
                    e("Role check simulation", "String role = \"ADMIN\";\nif (role.equals(\"ADMIN\")) {\n    System.out.println(\"Access granted to admin panel\");\n} else {\n    System.out.println(\"Access denied\");\n}")
                ),
                "<p>Implement <code>checkAccess(String role, String resource)</code>: return <code>\"ALLOWED\"</code> if role is <code>\"ADMIN\"</code> for any resource, or role is <code>\"USER\"</code> for <code>\"public\"</code>. Otherwise <code>\"DENIED\"</code>.</p>",
                "public class AuthDemo {\n    static String checkAccess(String role, String resource) {\n        // TODO: ADMIN can access anything\n        // USER can only access \"public\"\n        // else DENIED\n        return null;\n    }\n    public static void main(String[] args) {\n        System.out.println(checkAccess(\"ADMIN\", \"admin-panel\"));\n        System.out.println(checkAccess(\"USER\", \"public\"));\n        System.out.println(checkAccess(\"USER\", \"admin-panel\"));\n    }\n}\n",
                tests(test("Access control", "null", "ALLOWED\nALLOWED\nDENIED")),
                "Explain the difference between authentication and authorisation. Give a real-world analogy for each.");

        mcQuestion("XF1", QuestionTier.RECALL, "In Spring Security, which component is responsible for loading user data during authentication?",
                new String[]{"UserDetailsService", "AuthenticationManager", "GrantedAuthority", "SecurityContextHolder"},
                "UserDetailsService",
                "UserDetailsService.loadUserByUsername() is called during authentication to fetch user credentials and roles.");

        tfQuestion("XF1", QuestionTier.RECALL, "Authorisation can happen before authentication in a Spring Security filter chain.",
                "False", "Authentication must establish who the user is before authorisation can check what they're allowed to do.");

        subChunk("XF2", "XF", "JWT Filter & Stateless Security", 2, 60, "JwtFilter.java",
                "<p>How does Spring Security validate a JWT on every API request without a session?</p>",
                "<p>For stateless REST APIs, implement a <code>OncePerRequestFilter</code> that:</p>"
                + "<ol>"
                + "<li>Extracts the JWT from <code>Authorization: Bearer &lt;token&gt;</code></li>"
                + "<li>Validates the signature and expiry</li>"
                + "<li>Loads the user and sets authentication in <code>SecurityContextHolder</code></li>"
                + "</ol>"
                + "<pre><code>@Component\npublic class JwtAuthFilter extends OncePerRequestFilter {\n    private final JwtService jwtService;\n    private final UserDetailsService userDetailsService;\n\n    @Override\n    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,\n                                     FilterChain chain) throws ServletException, IOException {\n        String authHeader = req.getHeader(\"Authorization\");\n        if (authHeader == null || !authHeader.startsWith(\"Bearer \")) {\n            chain.doFilter(req, res); return;\n        }\n        String token = authHeader.substring(7);\n        String username = jwtService.extractUsername(token);\n        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {\n            UserDetails ud = userDetailsService.loadUserByUsername(username);\n            if (jwtService.isValid(token, ud)) {\n                var auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());\n                SecurityContextHolder.getContext().setAuthentication(auth);\n            }\n        }\n        chain.doFilter(req, res);\n    }\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "The JWT filter is the Academy's enchanted gate — every request passes through it. It reads the signet ring, verifies the seal, and if valid, admits the wizard and marks their identity in the Security Context.")
                ),
                "<p>Simulate token extraction: given a header string like <code>\"Bearer abc.def.ghi\"</code>, extract the token part. Return null if the header doesn't start with <code>\"Bearer \"</code>.</p>",
                "public class JwtExtractDemo {\n    static String extractToken(String authHeader) {\n        // TODO: return null if null or not starting with \"Bearer \"\n        // otherwise return the substring after \"Bearer \"\n        return null;\n    }\n    public static void main(String[] args) {\n        System.out.println(extractToken(\"Bearer abc.def.ghi\"));\n        System.out.println(extractToken(\"Basic xyz\"));\n        System.out.println(extractToken(null));\n    }\n}\n",
                tests(test("Extract", "null", "abc.def.ghi\nnull\nnull")),
                "Explain the JWT authentication filter flow in Spring Security. What is SecurityContextHolder?");

        mcQuestion("XF2", QuestionTier.RECALL, "Why does a stateless JWT-based API set SessionCreationPolicy.STATELESS?",
                new String[]{"To prevent Spring from creating HTTP sessions — each request is self-contained", "To improve performance by disabling the database", "To avoid storing JWTs server-side", "To disable authentication entirely"},
                "To prevent Spring from creating HTTP sessions — each request is self-contained",
                "Stateless means no server-side session. The JWT carries all authentication info, so no session store is needed.");

        subChunk("XF3", "XF", "Method Security & OAuth2", 3, 60, "MethodSecurity.java",
                "<p>How do you secure individual methods — and allow users to log in with Google?</p>",
                "<p><strong>Method Security</strong> — secure individual methods with annotations:</p>"
                + "<pre><code>@EnableMethodSecurity  // in @Configuration\n\n@PreAuthorize(\"hasRole('ADMIN')\")  // checked before method runs\npublic void deleteUser(Long id) { ... }\n\n@PostAuthorize(\"returnObject.ownerId == authentication.principal.id\") // after\npublic Order getOrder(Long id) { ... }\n\n@Secured(\"ROLE_MODERATOR\")  // simpler — just checks role\npublic void moderateContent(Long id) { ... }</code></pre>"
                + "<p><strong>OAuth2 Login</strong> (Google/GitHub):</p>"
                + "<pre><code>// application.yml\nspring:\n  security:\n    oauth2:\n      client:\n        registration:\n          google:\n            client-id: ${GOOGLE_CLIENT_ID}\n            client-secret: ${GOOGLE_CLIENT_SECRET}\n\n// Spring auto-handles /oauth2/authorization/google redirect and callback\n// Implement OAuth2UserService to customise what happens after successful login</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Method security is your finest-grained ward. The filter chain protects URLs. @PreAuthorize protects individual operations with SpEL expressions — the most powerful access control in Spring.")
                ),
                "<p>Simulate @PreAuthorize: write <code>deleteUser(String callerRole, Long userId)</code> — only <code>\"ADMIN\"</code> can call it. Print <code>\"Deleted: \" + userId</code> or throw an <code>IllegalStateException(\"Access denied\")</code>.</p>",
                "public class MethodSecurityDemo {\n    static void deleteUser(String callerRole, Long userId) {\n        // TODO: throw IllegalStateException if not ADMIN\n        // else print \"Deleted: \" + userId\n    }\n    public static void main(String[] args) {\n        deleteUser(\"ADMIN\", 42L);\n        try {\n            deleteUser(\"USER\", 42L);\n        } catch (IllegalStateException e) {\n            System.out.println(e.getMessage());\n        }\n    }\n}\n",
                tests(test("Method security", "null", "Deleted: 42\nAccess denied")),
                "Explain the difference between @PreAuthorize and @PostAuthorize. When would you use each?");

        mcQuestion("XF3", QuestionTier.RECALL, "What annotation enables method-level security in Spring?",
                new String[]{"@EnableMethodSecurity on a @Configuration class", "@EnableSecurity on the main class", "@MethodSecurity on each method", "@Secured(true) on the controller"},
                "@EnableMethodSecurity on a @Configuration class",
                "@EnableMethodSecurity activates @PreAuthorize, @PostAuthorize, and @Secured annotations.");

        subChunk("XF4", "XF", "Password Encoding & CSRF", 4, 50, "PasswordSecurity.java",
                "<p>How should passwords be stored — and what is CSRF?</p>",
                "<p><strong>Password Encoding</strong>: Never store plain-text passwords. Use BCrypt:</p>"
                + "<pre><code>@Bean\npublic PasswordEncoder passwordEncoder() {\n    return new BCryptPasswordEncoder(); // work factor 10\n}\n\n// Registration\nString hash = passwordEncoder.encode(rawPassword);\nuser.setPasswordHash(hash);\n\n// Login verification\nboolean matches = passwordEncoder.matches(rawPassword, storedHash);\n</code></pre>"
                + "<p>BCrypt is adaptive — increase work factor as hardware gets faster.</p>"
                + "<p><strong>CSRF (Cross-Site Request Forgery)</strong>: An attacker tricks a user's browser into making authenticated requests. Mitigated with:</p>"
                + "<ul>"
                + "<li>CSRF tokens for form-based apps (Spring Security default for MVC apps)</li>"
                + "<li>Not applicable to JWT REST APIs (not cookie-based) — disable CSRF for REST</li>"
                + "<li>SameSite=Strict cookie policy as additional protection</li>"
                + "</ul>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Storing plain-text passwords is not just wrong — it's inexcusable. BCrypt turns a password into an irreversible hash with a built-in cost factor. Even if your database is stolen, passwords are safe.")
                ),
                "<p>Simulate BCrypt's concept: write <code>hashPassword(String raw)</code> that returns <code>\"hashed:\" + raw.length() + \":salt\"</code>, and <code>matches(String raw, String hash)</code> that checks if the hash starts with <code>\"hashed:\" + raw.length()</code>.</p>",
                "public class PasswordDemo {\n    static String hashPassword(String raw) {\n        // TODO: return \"hashed:\" + raw.length() + \":salt\"\n        return null;\n    }\n    static boolean matches(String raw, String hash) {\n        // TODO: check if hash starts with \"hashed:\" + raw.length()\n        return false;\n    }\n    public static void main(String[] args) {\n        String hash = hashPassword(\"secret\");\n        System.out.println(hash);\n        System.out.println(matches(\"secret\", hash));\n        System.out.println(matches(\"wrong\", hash));\n    }\n}\n",
                tests(test("Password hashing", "null", "hashed:6:salt\ntrue\nfalse")),
                "Why is BCrypt preferred over MD5 or SHA-256 for password storage?");

        mcQuestion("XF4", QuestionTier.RECALL, "Why should you never store plain-text passwords?",
                new String[]{"A database breach would expose all user passwords immediately", "Plain text is slower to compare", "Spring Security doesn't support it", "Databases don't accept plain text"},
                "A database breach would expose all user passwords immediately",
                "Hashing with BCrypt means a stolen database doesn't directly reveal passwords — attackers must brute-force each hash.");
    }

    // ── XG: Microservices ──────────────────────────────────────────────────

    private void seedChunkXG() {
        chunk("XG", "Microservices Architecture", "\uD83D\uDD27", 35, LearnerPath.EXPERT, "XD", "XE");

        subChunk("XG1", "XG", "Microservices vs Monolith", 1, 60, "MicroservicesIntro.java",
                "<p>Why do large companies break their applications into dozens of small services?</p>",
                "<p><strong>Monolith</strong>: one deployable unit. Simple to develop, deploy, and debug — but hard to scale independently.</p>"
                + "<p><strong>Microservices</strong>: small, independently deployable services, each owning its data and business logic.</p>"
                + "<p>Benefits vs. costs:</p>"
                + "<table style='border-collapse:collapse;width:100%'>"
                + "<tr><th>Microservices Benefits</th><th>Microservices Costs</th></tr>"
                + "<tr><td>Independent deployment</td><td>Distributed system complexity</td></tr>"
                + "<tr><td>Technology heterogeneity</td><td>Network latency between services</td></tr>"
                + "<tr><td>Fault isolation</td><td>Distributed transactions</td></tr>"
                + "<tr><td>Horizontal scaling per service</td><td>Operational overhead (K8s, etc.)</td></tr>"
                + "</table>"
                + "<p><strong>Key patterns</strong>: API Gateway, Service Discovery (Eureka), Circuit Breaker (Resilience4j).</p>",
                story(
                    n("The Academy once had a single Grand Spellbook. When it grew too large, they split it — the Combat Library, the Alchemy Vault, the History Archives. Each could grow and be updated independently."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Don't reach for microservices on day one. A well-structured monolith is often faster to build and easier to operate. Microservices trade development simplicity for operational flexibility — make that trade consciously.")
                ),
                "<p>Print a comparison: for each of 3 attributes, print whether a Monolith or Microservices is simpler. Use format: <code>\"[attribute]: Monolith|Microservices\"</code>.</p>",
                "public class ArchitectureComparison {\n    public static void main(String[] args) {\n        // deployment: Monolith simpler\n        System.out.println(\"Deployment: Monolith\");\n        // scaling specific parts: Microservices simpler\n        System.out.println(\"Granular scaling: Microservices\");\n        // debugging across network: Monolith simpler\n        System.out.println(\"Debugging: Monolith\");\n    }\n}\n",
                tests(test("Comparison", "null", "Deployment: Monolith\nGranular scaling: Microservices\nDebugging: Monolith")),
                "When would you choose a monolith over microservices? What is the main operational cost of microservices?");

        mcQuestion("XG1", QuestionTier.RECALL, "What is the primary benefit of the Circuit Breaker pattern in microservices?",
                new String[]{"Prevents cascading failures when a downstream service is unavailable", "Encrypts inter-service communication", "Routes traffic to the nearest service", "Scales services automatically"},
                "Prevents cascading failures when a downstream service is unavailable",
                "Circuit Breaker (e.g., Resilience4j) stops calls to a failing service and returns a fallback, preventing system-wide failure.");

        subChunk("XG2", "XG", "Inter-Service Communication", 2, 60, "ServiceCommunication.java",
                "<p>How do microservices talk to each other — and how do you handle failures gracefully?</p>",
                "<p>Two main communication styles:</p>"
                + "<p><strong>Synchronous (HTTP/REST)</strong>: Service A calls Service B and waits. Simple but creates coupling — if B is down, A fails.</p>"
                + "<pre><code>// Spring Boot: RestTemplate (legacy) or WebClient (reactive)\nWebClient client = WebClient.create(\"http://inventory-service\");\nInventoryStatus status = client.get()\n    .uri(\"/api/inventory/{id}\", productId)\n    .retrieve()\n    .bodyToMono(InventoryStatus.class)\n    .block();\n</code></pre>"
                + "<p><strong>Asynchronous (Message Queue)</strong>: Service A publishes a message to Kafka/RabbitMQ. Service B consumes when ready. Decoupled, resilient, but adds complexity.</p>"
                + "<pre><code>// Spring Kafka producer\nkafkaTemplate.send(\"order-placed\", orderId, orderEvent);\n\n// Consumer\n@KafkaListener(topics = \"order-placed\")\npublic void handleOrderPlaced(OrderEvent event) { ... }</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Synchronous calls are a wizard shouting across the hall for an answer. Async messaging is leaving a scroll in the mailbox — the recipient reads it when ready. Choose based on whether you need an immediate answer.")
                ),
                "<p>Simulate async messaging: create a simple <code>MessageQueue</code> with <code>publish(String message)</code> and <code>consume()</code>. Publish three messages and consume them in order.</p>",
                "import java.util.LinkedList;\nimport java.util.Queue;\n\npublic class MessageQueueDemo {\n    static class MessageQueue {\n        Queue<String> queue = new LinkedList<>();\n        // TODO: publish(String message) — add to queue\n        // TODO: consume() — poll from queue and print\n        void publish(String message) {}\n        void consume() {}\n    }\n    public static void main(String[] args) {\n        MessageQueue mq = new MessageQueue();\n        mq.publish(\"OrderPlaced\");\n        mq.publish(\"PaymentProcessed\");\n        mq.publish(\"OrderShipped\");\n        mq.consume();\n        mq.consume();\n        mq.consume();\n    }\n}\n",
                tests(test("Queue", "null", "OrderPlaced\nPaymentProcessed\nOrderShipped")),
                "Compare synchronous and asynchronous inter-service communication. When would you choose each?");

        mcQuestion("XG2", QuestionTier.RECALL, "Which communication pattern provides the best resilience when a downstream service is temporarily unavailable?",
                new String[]{"Asynchronous messaging (message queue)", "Synchronous REST calls", "Direct database access", "Shared memory"},
                "Asynchronous messaging (message queue)",
                "Message queues decouple producer from consumer. If the consumer is down, messages wait in the queue — no data loss.");

        subChunk("XG3", "XG", "Service Discovery & API Gateway", 3, 50, "ServiceDiscovery.java",
                "<p>With dozens of services running on dynamic IPs, how does Service A find Service B?</p>",
                "<p><strong>Service Discovery</strong>: Services register themselves at startup. Others look up addresses by name.</p>"
                + "<pre><code>// Eureka (Netflix OSS) — server\n@SpringBootApplication\n@EnableEurekaServer\npublic class DiscoveryServer { ... }\n\n// Service registering itself\n@SpringBootApplication\n@EnableDiscoveryClient\npublic class OrderService { ... }\n\n// application.yml\neureka:\n  client:\n    service-url:\n      defaultZone: http://discovery:8761/eureka</code></pre>"
                + "<p><strong>API Gateway</strong> (Spring Cloud Gateway): single entry point for all clients. Routes to correct service, handles cross-cutting: auth, rate limiting, CORS, logging.</p>"
                + "<pre><code># gateway routes\nspring:\n  cloud:\n    gateway:\n      routes:\n        - id: order-service\n          uri: lb://ORDER-SERVICE  # lb = load-balanced via Eureka\n          predicates:\n            - Path=/api/orders/**</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "The API Gateway is the Academy's front gate — all visitors enter here. It checks credentials, routes to the right tower, and shields the inner workings from the outside world.")
                ),
                "<p>Simulate routing: given a map of service routes, write <code>route(String path)</code> that returns the service name for a given path prefix. Return <code>\"unknown\"</code> if no match.</p>",
                "import java.util.LinkedHashMap;\nimport java.util.Map;\n\npublic class GatewayDemo {\n    static Map<String, String> routes = new LinkedHashMap<>();\n    static {\n        routes.put(\"/api/orders\", \"order-service\");\n        routes.put(\"/api/users\", \"user-service\");\n        routes.put(\"/api/products\", \"product-service\");\n    }\n    static String route(String path) {\n        // TODO: return service whose key is a prefix of path, else \"unknown\"\n        for (Map.Entry<String, String> e : routes.entrySet()) {\n            if (path.startsWith(e.getKey())) return e.getValue();\n        }\n        return \"unknown\";\n    }\n    public static void main(String[] args) {\n        System.out.println(route(\"/api/orders/5\"));\n        System.out.println(route(\"/api/users\"));\n        System.out.println(route(\"/api/unknown\"));\n    }\n}\n",
                tests(test("Routing", "null", "order-service\nuser-service\nunknown")),
                "What is an API Gateway? List three responsibilities it can handle for all services.");

        mcQuestion("XG3", QuestionTier.RECALL, "What does 'lb://' in Spring Cloud Gateway route configuration indicate?",
                new String[]{"Load-balanced — Spring Cloud will resolve the service via Service Discovery", "Local binding — the service is on localhost", "Library — a built-in Spring service", "Lightweight — a reduced-feature service"},
                "Load-balanced — Spring Cloud will resolve the service via Service Discovery",
                "lb://SERVICE-NAME tells the gateway to look up the service in Eureka and load-balance requests across instances.");

        subChunk("XG4", "XG", "Distributed Tracing & Saga Pattern", 4, 50, "DistributedPatterns.java",
                "<p>When a request spans five services, how do you trace it — and how do you handle transactions across services?</p>",
                "<p><strong>Distributed Tracing</strong> (Micrometer Tracing / Zipkin):</p>"
                + "<pre><code>// Each request gets a traceId; each service adds a spanId\n// spring.application.name=order-service\n// management.tracing.sampling.probability=1.0\n// All logs include traceId: 2024-01-15 INFO [order-service,abc123,def456] Order created\n</code></pre>"
                + "<p><strong>Saga Pattern</strong>: manages distributed transactions without 2PC (two-phase commit).</p>"
                + "<ul>"
                + "<li><strong>Choreography Saga</strong>: each service reacts to events and emits its own. No central coordinator.</li>"
                + "<li><strong>Orchestration Saga</strong>: a central saga orchestrator sends commands to each service.</li>"
                + "</ul>"
                + "<pre><code>// Choreography example\n// 1. OrderService publishes OrderCreated\n// 2. PaymentService listens → processes payment → publishes PaymentDone\n// 3. InventoryService listens → reserves items → publishes ItemsReserved\n// 4. If payment fails → PaymentService publishes PaymentFailed\n//    → OrderService listens → cancels order (compensating transaction)</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "In a distributed system, a single purchase involves five services. If step three fails, you must undo steps one and two — compensating transactions are the rollback mechanism of microservices.")
                ),
                "<p>Simulate a choreography saga step: given a list of saga steps (strings), process each in order. If a step contains <code>\"FAIL\"</code>, print <code>\"Compensating: \" + step</code> for all completed steps in reverse order.</p>",
                "import java.util.ArrayList;\nimport java.util.List;\n\npublic class SagaDemo {\n    public static void main(String[] args) {\n        String[] steps = {\"CreateOrder\", \"ProcessPayment\", \"FAIL:ReserveInventory\"};\n        List<String> completed = new ArrayList<>();\n        for (String step : steps) {\n            if (step.startsWith(\"FAIL\")) {\n                System.out.println(\"Failed at: \" + step);\n                for (int i = completed.size() - 1; i >= 0; i--) {\n                    System.out.println(\"Compensating: \" + completed.get(i));\n                }\n                break;\n            } else {\n                System.out.println(\"Completed: \" + step);\n                completed.add(step);\n            }\n        }\n    }\n}\n",
                tests(test("Saga", "null", "Completed: CreateOrder\nCompleted: ProcessPayment\nFailed at: FAIL:ReserveInventory\nCompensating: ProcessPayment\nCompensating: CreateOrder")),
                "Explain the Saga pattern. Why can't distributed microservices use traditional database transactions (2PC)?");

        mcQuestion("XG4", QuestionTier.RECALL, "What is a compensating transaction in the Saga pattern?",
                new String[]{"An action that undoes a previously completed step when a later step fails", "A transaction that runs concurrently to speed up processing", "A read-only query that checks saga state", "A retry mechanism for failed transactions"},
                "An action that undoes a previously completed step when a later step fails",
                "Compensating transactions are the distributed equivalent of rollback — they reverse the effects of already-committed steps.");
    }

    // ── XH: Docker & Containerisation ─────────────────────────────────────

    private void seedChunkXH() {
        chunk("XH", "Docker & Containerisation", "\uD83D\uDC33", 36, LearnerPath.EXPERT, "XC");

        subChunk("XH1", "XH", "Docker Fundamentals", 1, 60, "DockerFundamentals.java",
                "<p>Why does 'it works on my machine' stop being acceptable in a professional environment?</p>",
                "<p><strong>Docker</strong> packages an application and all its dependencies into a <strong>container</strong> — a lightweight, isolated runtime environment that runs identically everywhere.</p>"
                + "<p>Key concepts:</p>"
                + "<ul>"
                + "<li><strong>Image</strong> — an immutable snapshot built from a Dockerfile</li>"
                + "<li><strong>Container</strong> — a running instance of an image</li>"
                + "<li><strong>Layer</strong> — images are built in layers; unchanged layers are cached</li>"
                + "<li><strong>Registry</strong> — Docker Hub, ECR, GCR — stores and distributes images</li>"
                + "</ul>"
                + "<pre><code># Dockerfile for Spring Boot\nFROM eclipse-temurin:21-jre-alpine   # base image (small JRE)\nWORKDIR /app\nCOPY target/*.jar app.jar\nEXPOSE 8080\nENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]\n\n# Build & run\n# docker build -t academy-api:1.0 .\n# docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod academy-api:1.0</code></pre>",
                story(
                    n("The Academy ships apprentices to distant towers. But what if the tower uses a different version of Java? Docker solves this: the apprentice brings their entire environment with them."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Containers are sealed spell capsules. Everything needed to cast the spell — the Java runtime, the JAR, the configuration — sealed inside. Open the capsule anywhere and the spell runs exactly the same.")
                ),
                "<p>Write a method that generates a Docker run command string: <code>dockerRun(String image, int hostPort, int containerPort, String envVar)</code> returning <code>\"docker run -p {hostPort}:{containerPort} -e {envVar} {image}\"</code>.</p>",
                "public class DockerDemo {\n    static String dockerRun(String image, int hostPort, int containerPort, String envVar) {\n        // TODO: format the docker run command\n        return null;\n    }\n    public static void main(String[] args) {\n        System.out.println(dockerRun(\"academy-api:1.0\", 8080, 8080, \"SPRING_PROFILES_ACTIVE=prod\"));\n    }\n}\n",
                tests(test("Docker run", "null", "docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod academy-api:1.0")),
                "What is the difference between a Docker image and a Docker container? What is a layer cache?");

        mcQuestion("XH1", QuestionTier.RECALL, "Why should you use a JRE (not JDK) as your Docker base image in production?",
                new String[]{"JRE is smaller — it contains only the runtime, not compiler tools needed for development", "JDK doesn't work in containers", "JRE is faster than JDK", "JDK has security vulnerabilities"},
                "JRE is smaller — it contains only the runtime, not compiler tools needed for development",
                "Production containers only need to run the app, not compile it. eclipse-temurin:21-jre-alpine is ~90MB vs ~300MB for JDK.");

        subChunk("XH2", "XH", "Docker Compose & Multi-Container Apps", 2, 60, "DockerCompose.java",
                "<p>Your Spring Boot app needs PostgreSQL and Redis — how do you run all three together?</p>",
                "<p><strong>Docker Compose</strong> defines multi-container apps in YAML and starts them with one command:</p>"
                + "<pre><code># docker-compose.yml\nservices:\n  api:\n    build: ./backend\n    ports: [\"8080:8080\"]\n    environment:\n      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/academy\n      SPRING_DATASOURCE_USERNAME: academy\n      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}\n    depends_on:\n      db:\n        condition: service_healthy  # wait for DB\n\n  db:\n    image: postgres:16-alpine\n    environment:\n      POSTGRES_DB: academy\n      POSTGRES_USER: academy\n      POSTGRES_PASSWORD: ${DB_PASSWORD}\n    volumes:\n      - pgdata:/var/lib/postgresql/data  # persist data\n    healthcheck:\n      test: [\"CMD\", \"pg_isready\", \"-U\", \"academy\"]\n\nvolumes:\n  pgdata:\n</code></pre>"
                + "<pre><code>docker compose up --build   # start all services\ndocker compose down -v      # stop and remove volumes</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Docker Compose is your local cloud. Define every service your app needs — API, database, cache, message broker — and spin them all up with one command. Tear them down just as easily.")
                ),
                "<p>Generate a simplified Compose service definition. Write <code>composeService(String name, String image, int port)</code> returning a multi-line string in the format shown.</p>",
                "public class ComposeDemo {\n    static String composeService(String name, String image, int port) {\n        return name + \":\\n\" +\n               \"  image: \" + image + \"\\n\" +\n               \"  ports: [\\\"\" + port + \":\" + port + \"\\\"]\";\n    }\n    public static void main(String[] args) {\n        System.out.println(composeService(\"db\", \"postgres:16\", 5432));\n    }\n}\n",
                tests(test("Compose service", "null", "db:\n  image: postgres:16\n  ports: [\"5432:5432\"]")),
                "Explain Docker Compose depends_on with a health check. Why is it important for a Spring Boot app that needs a database?");

        mcQuestion("XH2", QuestionTier.RECALL, "Why use a named volume (e.g., pgdata) for a PostgreSQL container?",
                new String[]{"Data persists between container restarts — without it, the database is wiped when the container stops", "It speeds up queries", "It encrypts the data automatically", "Volumes are required for PostgreSQL to start"},
                "Data persists between container restarts — without it, the database is wiped when the container stops",
                "Container filesystems are ephemeral. Volumes mount a persistent storage location outside the container lifecycle.");

        subChunk("XH3", "XH", "Kubernetes Fundamentals", 3, 50, "KubernetesFundamentals.java",
                "<p>Docker Compose works on one machine. What manages containers across hundreds of servers?</p>",
                "<p><strong>Kubernetes (K8s)</strong> is the industry-standard container orchestration platform. Key abstractions:</p>"
                + "<ul>"
                + "<li><strong>Pod</strong> — smallest deployable unit; one or more containers sharing network/storage</li>"
                + "<li><strong>Deployment</strong> — manages replica sets; handles rolling updates and rollbacks</li>"
                + "<li><strong>Service</strong> — stable network endpoint for a set of pods (load balancing)</li>"
                + "<li><strong>ConfigMap/Secret</strong> — inject configuration and sensitive data</li>"
                + "<li><strong>Ingress</strong> — HTTP routing into the cluster from the outside world</li>"
                + "</ul>"
                + "<pre><code>apiVersion: apps/v1\nkind: Deployment\nmetadata:\n  name: academy-api\nspec:\n  replicas: 3\n  selector:\n    matchLabels:\n      app: academy-api\n  template:\n    spec:\n      containers:\n      - name: api\n        image: academy-api:1.0\n        ports: [{containerPort: 8080}]\n        envFrom:\n          - secretRef: {name: db-credentials}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Kubernetes is the Grand Army that deploys your containers across a thousand servers, heals failed pods automatically, and scales up under load. It is complex — but it is the foundation of modern cloud deployments.")
                ),
                "<p>Print the names of 5 Kubernetes resource types and one-line descriptions for each.</p>",
                "public class K8sDemo {\n    public static void main(String[] args) {\n        System.out.println(\"Pod: smallest deployable unit, one or more containers\");\n        System.out.println(\"Deployment: manages pod replicas and rolling updates\");\n        System.out.println(\"Service: stable network endpoint for a pod set\");\n        System.out.println(\"ConfigMap: inject non-sensitive configuration data\");\n        System.out.println(\"Secret: inject sensitive data like passwords\");\n    }\n}\n",
                tests(test("K8s resources", "null", "Pod: smallest deployable unit, one or more containers\nDeployment: manages pod replicas and rolling updates\nService: stable network endpoint for a pod set\nConfigMap: inject non-sensitive configuration data\nSecret: inject sensitive data like passwords")),
                "Explain the difference between a Kubernetes Deployment and a Service. What does 'replicas: 3' guarantee?");

        mcQuestion("XH3", QuestionTier.RECALL, "What does a Kubernetes Service provide?",
                new String[]{"A stable DNS name and load-balanced endpoint for a set of pods", "A way to deploy container images", "Persistent storage for pods", "Network isolation between namespaces"},
                "A stable DNS name and load-balanced endpoint for a set of pods",
                "Pods are ephemeral with changing IPs. A Service provides a stable address, load-balancing requests across healthy pods.");

        subChunk("XH4", "XH", "CI/CD Pipeline Concepts", 4, 50, "CiCdPipeline.java",
                "<p>How does code go from a developer's commit to running in production automatically and safely?</p>",
                "<p>A <strong>CI/CD pipeline</strong> automates build, test, and deploy:</p>"
                + "<pre><code># GitHub Actions — .github/workflows/deploy.yml\nname: Build and Deploy\non:\n  push:\n    branches: [main]\njobs:\n  build-and-deploy:\n    runs-on: ubuntu-latest\n    steps:\n      - uses: actions/checkout@v4\n      - uses: actions/setup-java@v4\n        with: { java-version: '21' }\n      - name: Build\n        run: ./mvnw package\n      - name: Test\n        run: ./mvnw test\n      - name: Build Docker image\n        run: docker build -t academy-api:${{ github.sha }} .\n      - name: Push to registry\n        run: docker push academy-api:${{ github.sha }}\n      - name: Deploy to K8s\n        run: kubectl set image deployment/academy-api api=academy-api:${{ github.sha }}</code></pre>"
                + "<p><strong>Strategies</strong>: Rolling update (zero downtime), Blue/Green (two prod environments), Canary (route 5% to new version).</p>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "A CI/CD pipeline is an enchanted conveyor belt. Code enters one end; a tested, deployed application exits the other. Human error is eliminated. Every commit is a potential release.")
                ),
                "<p>Write <code>pipelineStages()</code> that prints the 5 stages of a typical CI/CD pipeline in order.</p>",
                "public class CiCdDemo {\n    static void pipelineStages() {\n        System.out.println(\"1. Source: code commit triggers pipeline\");\n        System.out.println(\"2. Build: compile and package the application\");\n        System.out.println(\"3. Test: run unit, integration, and security tests\");\n        System.out.println(\"4. Package: build Docker image and push to registry\");\n        System.out.println(\"5. Deploy: update running containers in target environment\");\n    }\n    public static void main(String[] args) {\n        pipelineStages();\n    }\n}\n",
                tests(test("Pipeline stages", "null", "1. Source: code commit triggers pipeline\n2. Build: compile and package the application\n3. Test: run unit, integration, and security tests\n4. Package: build Docker image and push to registry\n5. Deploy: update running containers in target environment")),
                "Explain Blue/Green deployment. How does it achieve zero-downtime releases?");

        mcQuestion("XH4", QuestionTier.RECALL, "What is Continuous Deployment (CD) vs. Continuous Delivery?",
                new String[]{"Continuous Deployment auto-deploys every passing build to production; Continuous Delivery requires a manual approval gate", "They are the same thing", "Continuous Delivery deploys automatically; Delivery requires manual steps", "Neither involves automation"},
                "Continuous Deployment auto-deploys every passing build to production; Continuous Delivery requires a manual approval gate",
                "CD (Deployment) fully automates to production. Continuous Delivery automates to a staging environment but requires human approval for production.");
    }

    // ── XI: System Architecture & Design ──────────────────────────────────

    private void seedChunkXI() {
        chunk("XI", "System Architecture & Design", "\uD83C\uDFDB\uFE0F", 37, LearnerPath.EXPERT, "XG");

        subChunk("XI1", "XI", "Scalability & Availability Patterns", 1, 60, "ScalabilityPatterns.java",
                "<p>How do you design a system that stays fast under 10x traffic — and stays running when servers fail?</p>",
                "<p><strong>Scalability</strong>: ability to handle increased load.</p>"
                + "<ul>"
                + "<li><strong>Vertical scaling</strong> — bigger server (CPU/RAM). Simple but has limits.</li>"
                + "<li><strong>Horizontal scaling</strong> — more servers behind a load balancer. Unlimited but requires statelessness.</li>"
                + "</ul>"
                + "<p><strong>Availability patterns</strong>:</p>"
                + "<ul>"
                + "<li><strong>Load balancing</strong> — distribute traffic across instances</li>"
                + "<li><strong>Health checks</strong> — remove unhealthy instances automatically</li>"
                + "<li><strong>Redundancy</strong> — no single point of failure (database primary + replica)</li>"
                + "<li><strong>Circuit breaker</strong> — stop calls to failing services</li>"
                + "<li><strong>Retry with exponential backoff</strong> — retry transient failures with increasing delays</li>"
                + "</ul>"
                + "<p><strong>CAP Theorem</strong>: a distributed system can guarantee at most two of: Consistency, Availability, Partition tolerance.</p>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Designing for scale means assuming failure. Any server can die at any moment. Any network call can fail. Design your system so that individual failures are isolated, not catastrophic.")
                ),
                "<p>Implement exponential backoff: <code>backoffDelay(int attempt)</code> returns <code>baseDelay * 2^attempt</code> milliseconds (base = 100ms). Print delays for attempts 0-3.</p>",
                "public class BackoffDemo {\n    static long backoffDelay(int attempt) {\n        return (long)(100 * Math.pow(2, attempt));\n    }\n    public static void main(String[] args) {\n        for (int i = 0; i < 4; i++) {\n            System.out.println(\"Attempt \" + i + \": \" + backoffDelay(i) + \"ms\");\n        }\n    }\n}\n",
                tests(test("Backoff", "null", "Attempt 0: 100ms\nAttempt 1: 200ms\nAttempt 2: 400ms\nAttempt 3: 800ms")),
                "Explain the CAP theorem with an example. What does a system sacrifice when choosing AP over CP?");

        mcQuestion("XI1", QuestionTier.RECALL, "What does horizontal scaling require that vertical scaling does not?",
                new String[]{"Stateless application design — session state must be externalised (e.g., Redis)", "A more powerful JVM", "Database schema changes", "A different programming language"},
                "Stateless application design — session state must be externalised (e.g., Redis)",
                "With multiple instances, any request can hit any server. Server-side session storage won't work — use an external store like Redis.");

        subChunk("XI2", "XI", "Caching Strategies", 2, 60, "CachingStrategies.java",
                "<p>The database is your bottleneck — how do you serve 100x more reads without 100x the database load?</p>",
                "<p><strong>Caching</strong> stores frequently accessed data in fast memory (Redis, Memcached) instead of re-querying the database.</p>"
                + "<p>Common patterns:</p>"
                + "<ul>"
                + "<li><strong>Cache-Aside (Lazy Loading)</strong>: check cache → miss → load from DB → store in cache</li>"
                + "<li><strong>Write-Through</strong>: write to cache and DB simultaneously</li>"
                + "<li><strong>Write-Behind</strong>: write to cache immediately, async write to DB</li>"
                + "<li><strong>TTL (Time-to-Live)</strong>: cached data expires after N seconds to prevent stale data</li>"
                + "</ul>"
                + "<p>Spring Cache:</p>"
                + "<pre><code>@Cacheable(\"products\")       // cache result on first call\npublic Product getProduct(Long id) { return repo.findById(id).orElseThrow(); }\n\n@CacheEvict(\"products\")     // remove from cache on update\npublic void updateProduct(Product p) { repo.save(p); }\n</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Caching is like a wizard memorising a spell they cast often — rather than looking it up in the Tome every time. The risk: if the Tome changes, the memory is wrong. TTL and cache eviction are the solution.")
                ),
                "<p>Simulate cache-aside: <code>getProduct(int id)</code> — if id is in the <code>cache</code> map, print <code>\"Cache hit: \" + cached value</code>; otherwise print <code>\"Cache miss, loading: \" + id</code> and add it to cache.</p>",
                "import java.util.HashMap;\nimport java.util.Map;\n\npublic class CacheDemo {\n    static Map<Integer, String> cache = new HashMap<>();\n    static String getProduct(int id) {\n        if (cache.containsKey(id)) {\n            System.out.println(\"Cache hit: \" + cache.get(id));\n            return cache.get(id);\n        }\n        String loaded = \"Product-\" + id;\n        System.out.println(\"Cache miss, loading: \" + id);\n        cache.put(id, loaded);\n        return loaded;\n    }\n    public static void main(String[] args) {\n        getProduct(1);\n        getProduct(1);\n        getProduct(2);\n    }\n}\n",
                tests(test("Cache", "null", "Cache miss, loading: 1\nCache hit: Product-1\nCache miss, loading: 2")),
                "Explain cache invalidation challenges. What is a cache stampede and how can you prevent it?");

        mcQuestion("XI2", QuestionTier.RECALL, "What is a TTL (Time-to-Live) in caching?",
                new String[]{"The duration after which a cached entry expires and is removed", "The time taken to load data into cache", "The maximum cache size in megabytes", "The time to live before the server restarts"},
                "The duration after which a cached entry expires and is removed",
                "TTL prevents stale data by auto-expiring cache entries. After TTL, the next request re-fetches from the source.");

        subChunk("XI3", "XI", "Database Design & Sharding", 3, 50, "DatabaseDesign.java",
                "<p>When your database table has a billion rows, how do you keep queries fast?</p>",
                "<p>Strategies for large-scale database performance:</p>"
                + "<ul>"
                + "<li><strong>Indexing</strong> — B-tree indexes on frequently queried columns. <code>EXPLAIN ANALYZE</code> reveals slow queries.</li>"
                + "<li><strong>Read Replicas</strong> — route read traffic to replica databases; primary handles writes.</li>"
                + "<li><strong>Partitioning</strong> — split large tables into smaller ones by range, list, or hash.</li>"
                + "<li><strong>Sharding</strong> — distribute data across multiple database servers by shard key.</li>"
                + "<li><strong>CQRS (Command Query Responsibility Segregation)</strong> — separate read and write models for ultimate scalability.</li>"
                + "</ul>"
                + "<pre><code>-- Index on frequently queried column\nCREATE INDEX idx_orders_user_id ON orders(user_id);\nCREATE INDEX idx_orders_status_created ON orders(status, created_at DESC);\n\n-- EXPLAIN ANALYZE reveals query plan\nEXPLAIN ANALYZE SELECT * FROM orders WHERE user_id = 42;</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Indexing is the most impactful optimisation you will ever make. A table scan on 100 million rows takes minutes. A B-tree index lookup takes milliseconds. Always add indexes on foreign keys and filter columns.")
                ),
                "<p>Simulate sharding: given a <code>userId</code>, determine shard number using <code>userId % numShards</code>. Print shard assignment for users 0-4 with 3 shards.</p>",
                "public class ShardingDemo {\n    static int getShard(int userId, int numShards) {\n        return userId % numShards;\n    }\n    public static void main(String[] args) {\n        int numShards = 3;\n        for (int userId = 0; userId < 5; userId++) {\n            System.out.println(\"User \" + userId + \" -> Shard \" + getShard(userId, numShards));\n        }\n    }\n}\n",
                tests(test("Sharding", "null", "User 0 -> Shard 0\nUser 1 -> Shard 1\nUser 2 -> Shard 2\nUser 3 -> Shard 0\nUser 4 -> Shard 1")),
                "Explain CQRS. What problem does it solve in high-scale systems?");

        mcQuestion("XI3", QuestionTier.RECALL, "What is the primary purpose of a database read replica?",
                new String[]{"Offload read traffic from the primary, improving read scalability and write performance", "Store backup data in case of primary failure", "Improve write throughput", "Reduce storage costs"},
                "Offload read traffic from the primary, improving read scalability and write performance",
                "Read replicas replicate data from the primary. Routing reads to replicas reduces primary load, leaving it free for writes.");

        subChunk("XI4", "XI", "Event-Driven Architecture & Message Patterns", 4, 50, "EventDrivenArch.java",
                "<p>How do large systems coordinate complex workflows without tightly coupling every component?</p>",
                "<p><strong>Event-Driven Architecture (EDA)</strong>: components communicate via events rather than direct calls.</p>"
                + "<p>Core concepts:</p>"
                + "<ul>"
                + "<li><strong>Event Sourcing</strong>: store all state changes as immutable events. Replay them to reconstruct state.</li>"
                + "<li><strong>CQRS + Event Sourcing</strong>: commands produce events; read models are built by consuming events.</li>"
                + "<li><strong>Kafka</strong>: distributed event streaming platform. Events are durable (persisted), replayable.</li>"
                + "<li><strong>Outbox Pattern</strong>: write events to a DB outbox table atomically with the business transaction. A poller publishes them to Kafka, ensuring no events are lost.</li>"
                + "</ul>"
                + "<pre><code>// Outbox pattern — atomicity guarantee\n@Transactional\npublic void placeOrder(Order order) {\n    orderRepo.save(order);\n    outboxRepo.save(new OutboxEvent(\"OrderPlaced\", order.getId())); // same TX\n    // Poller reads outbox and publishes to Kafka asynchronously\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Event Sourcing is the ultimate audit log — instead of storing the current state, you store every change that led to it. Want to know an order's history? Replay its events. Want to rebuild a corrupted read model? Replay all events from the beginning.")
                ),
                "<p>Simulate event sourcing: apply a list of events to a balance. Events: <code>\"DEPOSIT:100\"</code>, <code>\"DEPOSIT:50\"</code>, <code>\"WITHDRAW:30\"</code>. Print final balance.</p>",
                "public class EventSourcingDemo {\n    public static void main(String[] args) {\n        String[] events = {\"DEPOSIT:100\", \"DEPOSIT:50\", \"WITHDRAW:30\"};\n        int balance = 0;\n        for (String event : events) {\n            String[] parts = event.split(\":\");\n            int amount = Integer.parseInt(parts[1]);\n            if (parts[0].equals(\"DEPOSIT\")) balance += amount;\n            else if (parts[0].equals(\"WITHDRAW\")) balance -= amount;\n        }\n        System.out.println(\"Final balance: \" + balance);\n    }\n}\n",
                tests(test("Event sourcing", "null", "Final balance: 120")),
                "Explain Event Sourcing. What advantage does it have over traditional state-based persistence?");

        mcQuestion("XI4", QuestionTier.RECALL, "What problem does the Outbox Pattern solve?",
                new String[]{"Ensures events are published to Kafka atomically with the database transaction — no events lost if the broker is down", "Speeds up message publishing", "Reduces database storage", "Enables event replay"},
                "Ensures events are published to Kafka atomically with the database transaction — no events lost if the broker is down",
                "Writing to an outbox table in the same transaction as the business operation guarantees the event is eventually published, even if Kafka is temporarily unavailable.");
    }

    // ── XJ: Advanced Testing ──────────────────────────────────────────────

    private void seedChunkXJ() {
        chunk("XJ", "Advanced Testing & Quality", "\uD83E\uDDEA", 38, LearnerPath.EXPERT, "XD", "XE");

        subChunk("XJ1", "XJ", "Test Pyramid & Integration Testing", 1, 60, "TestPyramid.java",
                "<p>You have 500 tests. Why are 400 fast and 100 slow — and is that the right ratio?</p>",
                "<p>The <strong>Test Pyramid</strong> describes the ideal test distribution:</p>"
                + "<ul>"
                + "<li><strong>Unit tests (base)</strong> — fast, isolated, many. Test one class in isolation with mocks.</li>"
                + "<li><strong>Integration tests (middle)</strong> — test component interactions (e.g., service + real DB). Slower, fewer.</li>"
                + "<li><strong>End-to-End tests (top)</strong> — full system via HTTP. Slowest, fewest. High maintenance cost.</li>"
                + "</ul>"
                + "<p>Spring Boot integration test slices:</p>"
                + "<pre><code>// @WebMvcTest — controller + MockMvc only (no JPA)\n@WebMvcTest(OrderController.class)\nclass OrderControllerTest {\n    @Autowired MockMvc mvc;\n    @MockBean OrderService orderService;\n\n    @Test\n    void getOrder_returnsOk() throws Exception {\n        given(orderService.findById(1L)).willReturn(new OrderDto(1L, \"PENDING\"));\n        mvc.perform(get(\"/api/orders/1\"))\n           .andExpect(status().isOk())\n           .andExpect(jsonPath(\"$.status\").value(\"PENDING\"));\n    }\n}\n\n// @DataJpaTest — JPA + H2 only (no web layer)\n@DataJpaTest\nclass OrderRepositoryTest {\n    @Autowired OrderRepository repo;\n    @Test void save_andFind() { ... }\n}</code></pre>",
                story(
                    n("The Test Pyramid is your testing strategy made visible. Invert it — many slow E2E tests and few unit tests — and your build takes 30 minutes. A good pyramid means a 2-minute build."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "A test that catches bugs in 10 milliseconds is worth ten tests that catch the same bug in 10 seconds. Optimise your pyramid by pushing logic down into unit-testable classes.")
                ),
                "<p>Write a unit test structure: a <code>Calculator</code> class with <code>add(int a, int b)</code>. Then a test class with an <code>add_returnsSum()</code> test. Simulate it by running the assertion inline.</p>",
                "public class TestPyramidDemo {\n    static class Calculator {\n        int add(int a, int b) { return a + b; }\n    }\n    static void assertEquals(int expected, int actual) {\n        if (expected != actual)\n            throw new AssertionError(\"Expected \" + expected + \" but got \" + actual);\n        System.out.println(\"PASS: add(3,4) = \" + actual);\n    }\n    public static void main(String[] args) {\n        Calculator calc = new Calculator();\n        assertEquals(7, calc.add(3, 4));\n        assertEquals(0, calc.add(-5, 5));\n    }\n}\n",
                tests(test("Tests pass", "null", "PASS: add(3,4) = 7\nPASS: add(3,4) = 0")),
                "Explain the Test Pyramid. Why should E2E tests be the minority of your test suite?");

        mcQuestion("XJ1", QuestionTier.RECALL, "What does @WebMvcTest do differently from @SpringBootTest?",
                new String[]{"Loads only the web layer (controllers, filters); service and repo layers must be mocked", "Runs the full application context including the database", "Tests only service classes", "Skips all tests in the class"},
                "Loads only the web layer (controllers, filters); service and repo layers must be mocked",
                "@WebMvcTest is a slice test — faster and focused. @SpringBootTest loads the full application context.");

        subChunk("XJ2", "XJ", "Contract Testing & Consumer-Driven Contracts", 2, 60, "ContractTesting.java",
                "<p>In a microservices system, how do you ensure two services don't break each other's API contract?</p>",
                "<p><strong>Contract Testing</strong> verifies that API producers and consumers agree on the interface.</p>"
                + "<p><strong>Pact</strong> (Consumer-Driven Contract Testing):</p>"
                + "<ol>"
                + "<li>Consumer writes a test that records the interaction (request + expected response) → generates a <em>pact file</em></li>"
                + "<li>Pact file is shared with the provider via a Pact Broker</li>"
                + "<li>Provider runs a verification test against the pact — ensures they still honour the contract</li>"
                + "</ol>"
                + "<pre><code>// Consumer test — defines expected interaction\n@ExtendWith(PactConsumerTestExt.class)\nclass OrderClientPactTest {\n    @Pact(consumer = \"OrderService\")\n    RequestResponsePact getUser(PactDslWithProvider builder) {\n        return builder\n            .given(\"user 1 exists\")\n            .uponReceiving(\"get user 1\")\n            .path(\"/api/users/1\").method(\"GET\")\n            .willRespondWith().status(200)\n            .body(newJsonBody(b -> b.numberType(\"id\").stringType(\"name\")).build())\n            .toPact();\n    }\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Integration tests can miss API contract breaks. Service A tests against a mock. Service B changes its response shape. Both pass their tests, but they break together in production. Contract tests catch this at build time.")
                ),
                "<p>Simulate contract verification: a <code>Contract</code> specifies an expected JSON field <code>\"userId\"</code>. Verify that a given response map contains that field. Print <code>PASS</code> or <code>FAIL: missing userId</code>.</p>",
                "import java.util.HashMap;\nimport java.util.Map;\n\npublic class ContractDemo {\n    static void verifyContract(Map<String, Object> response, String requiredField) {\n        if (response.containsKey(requiredField)) {\n            System.out.println(\"PASS\");\n        } else {\n            System.out.println(\"FAIL: missing \" + requiredField);\n        }\n    }\n    public static void main(String[] args) {\n        Map<String, Object> validResponse = new HashMap<>();\n        validResponse.put(\"userId\", 42);\n        validResponse.put(\"name\", \"Merlin\");\n\n        Map<String, Object> invalidResponse = new HashMap<>();\n        invalidResponse.put(\"id\", 42); // wrong field name\n\n        verifyContract(validResponse, \"userId\");\n        verifyContract(invalidResponse, \"userId\");\n    }\n}\n",
                tests(test("Contract", "null", "PASS\nFAIL: missing userId")),
                "Explain consumer-driven contract testing. Why is it better than shared integration tests for microservices?");

        mcQuestion("XJ2", QuestionTier.RECALL, "Who generates the contract (pact file) in Consumer-Driven Contract Testing?",
                new String[]{"The consumer — they define the minimum interface they need from the provider", "The provider — they publish their API spec", "A shared test team", "An automated tool inspecting production traffic"},
                "The consumer — they define the minimum interface they need from the provider",
                "Consumer-driven means the consumer owns the contract. They only specify what they need — the provider must satisfy at minimum that subset.");

        subChunk("XJ3", "XJ", "Performance Testing & Observability", 3, 50, "PerformanceTesting.java",
                "<p>Your app passes all tests but is slow under load. How do you find and fix the bottleneck?</p>",
                "<p><strong>Performance Testing Types</strong>:</p>"
                + "<ul>"
                + "<li><strong>Load test</strong> — expected peak load for sustained period (Gatling, k6, JMeter)</li>"
                + "<li><strong>Stress test</strong> — find breaking point by increasing load until failure</li>"
                + "<li><strong>Spike test</strong> — sudden massive traffic increase</li>"
                + "<li><strong>Soak test</strong> — normal load for 24-72 hours (find memory leaks)</li>"
                + "</ul>"
                + "<p><strong>Observability pillars</strong>:</p>"
                + "<ul>"
                + "<li><strong>Metrics</strong> — quantitative measurements (Prometheus + Grafana dashboards)</li>"
                + "<li><strong>Logs</strong> — timestamped events (ELK stack: Elasticsearch, Logstash, Kibana)</li>"
                + "<li><strong>Traces</strong> — request journey across services (Zipkin, Jaeger)</li>"
                + "</ul>"
                + "<pre><code>// Micrometer custom metric in Spring Boot\n@Autowired MeterRegistry registry;\n\nprivate final Counter orderCounter = registry.counter(\"orders.placed\");\n\npublic void placeOrder(Order order) {\n    orderRepo.save(order);\n    orderCounter.increment();\n}</code></pre>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Metrics, logs, and traces are the three eyes of production visibility. Metrics tell you something is wrong. Logs tell you what happened. Traces show you exactly where in the distributed system the problem occurred.")
                ),
                "<p>Simulate throughput measurement: given a list of response times (ms), calculate and print average response time and requests per second (assuming 1000ms window).</p>",
                "public class PerformanceDemo {\n    public static void main(String[] args) {\n        int[] responseTimes = {120, 95, 140, 110, 200, 85, 130};\n        int total = 0;\n        for (int t : responseTimes) total += t;\n        double avg = (double) total / responseTimes.length;\n        double rps = 1000.0 / avg;\n        System.out.printf(\"Avg response: %.0fms%n\", avg);\n        System.out.printf(\"Approx RPS: %.1f%n\", rps);\n    }\n}\n",
                tests(test("Performance metrics", "null", "Avg response: 126ms\nApprox RPS: 7.9")),
                "Explain the three pillars of observability. What is the difference between a metric and a trace?");

        mcQuestion("XJ3", QuestionTier.RECALL, "What type of performance test is best suited to detecting memory leaks?",
                new String[]{"Soak test — run normal load for an extended period (hours to days)", "Spike test — sudden traffic burst", "Stress test — find the breaking point", "Load test — expected peak load"},
                "Soak test — run normal load for an extended period (hours to days)",
                "Memory leaks only appear over time. A soak test runs long enough to observe gradual memory growth that would cause production outages.");

        subChunk("XJ4", "XJ", "Code Quality & Static Analysis", 4, 50, "CodeQuality.java",
                "<p>How do you maintain code quality consistently across a team of 20 engineers?</p>",
                "<p><strong>Code Quality Tools</strong>:</p>"
                + "<ul>"
                + "<li><strong>SonarQube/SonarCloud</strong> — static analysis: bugs, code smells, security vulnerabilities, code coverage</li>"
                + "<li><strong>Checkstyle</strong> — enforce code style rules (naming, braces, line length)</li>"
                + "<li><strong>SpotBugs</strong> — finds null pointer bugs, thread safety issues, bad API usage</li>"
                + "<li><strong>OWASP Dependency Check</strong> — scans dependencies for known CVEs</li>"
                + "</ul>"
                + "<p><strong>Code Review checklist</strong>: Does it work? Is it readable? Is it tested? Is it secure? Is it performant? Does it follow conventions?</p>"
                + "<p><strong>Technical Debt</strong>: the implied cost of shortcuts. Tracked and paid down deliberately — not ignored until it becomes unworkable.</p>",
                story(
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Code quality is not about following rules for their own sake. It is about making the codebase legible and safe for the next wizard who works in it — which is often you, three months from now."),
                    n("You have reached the final sub-chunk of the Expert tier. You now hold the foundations of a professional Java engineer: from variables to microservices, from algorithms to distributed systems. The journey continues — but the fundamentals are yours.")
                ),
                "<p>Compute a simple quality score: given counts of <code>bugs</code>, <code>codeSmells</code>, and <code>coverage</code> (%), print <code>\"Quality: GOOD\"</code> if bugs == 0 and coverage >= 80, <code>\"Quality: WARNING\"</code> if bugs == 0 and coverage >= 60, else <code>\"Quality: POOR\"</code>.</p>",
                "public class QualityDemo {\n    static String qualityScore(int bugs, int codeSmells, int coverage) {\n        if (bugs == 0 && coverage >= 80) return \"Quality: GOOD\";\n        if (bugs == 0 && coverage >= 60) return \"Quality: WARNING\";\n        return \"Quality: POOR\";\n    }\n    public static void main(String[] args) {\n        System.out.println(qualityScore(0, 5, 85));\n        System.out.println(qualityScore(0, 12, 65));\n        System.out.println(qualityScore(3, 20, 40));\n    }\n}\n",
                tests(test("Quality scores", "null", "Quality: GOOD\nQuality: WARNING\nQuality: POOR")),
                "What is technical debt? How should a team manage it sustainably over time?");

        mcQuestion("XJ4", QuestionTier.RECALL, "What does OWASP Dependency Check do?",
                new String[]{"Scans project dependencies for known security vulnerabilities (CVEs)", "Checks code style compliance", "Measures test coverage", "Validates API contracts"},
                "Scans project dependencies for known security vulnerabilities (CVEs)",
                "OWASP Dependency Check compares your dependency versions against the NVD (National Vulnerability Database) to flag known CVEs.");

        tfQuestion("XJ4", QuestionTier.RECALL, "Technical debt should always be addressed immediately when discovered.",
                "False", "Technical debt requires judgement. Paying it down is important, but it must be prioritised against feature work. Teams track and plan debt reduction deliberately.");
    }
}
