package com.ambravate.arcane.academy.tools;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;

/**
 * Authoring-time AI harness for generating Markdown lesson files.
 *
 * <p><strong>Usage:</strong>
 * <pre>
 *   java -cp ... LessonAuthoringHarness \
 *       --id         java_app_3 \
 *       --title      "Operators and Expressions" \
 *       --domain     java \
 *       --tier       APPRENTICE \
 *       --module-id  java-app-3 \
 *       --topic      operators_and_expressions \
 *       --sort       3 \
 *       [--out       path/to/output.md] \
 *       [--dry-run]
 * </pre>
 *
 * <p>Requires {@code ANTHROPIC_API_KEY} environment variable.
 *
 * <p>The tool calls Claude claude-opus-4-5 to generate a complete Markdown lesson conforming
 * to the Arcane Academy template (see {@link #SYSTEM_PROMPT}) and writes it to the
 * standard content tree path, or to the path given by {@code --out}.
 *
 * <p>Human review is always required before committing a generated lesson to the
 * repository. The harness is a <em>draft accelerator</em>, not a publication tool.
 *
 * <p><strong>Not part of the runtime application</strong> — this class is not
 * scanned by Spring and has no application context dependency.
 */
public final class LessonAuthoringHarness {

    private LessonAuthoringHarness() {}

    // ── Prompt template ───────────────────────────────────────────────────────

    private static final String SYSTEM_PROMPT = """
            You are a senior curriculum author for Arcane Academy — a gamified programming
            learning platform set in a fantasy world of arcane magic. Your role is to write
            a single Markdown lesson file that follows the Arcane Academy lesson template
            exactly.

            LESSON TEMPLATE RULES:
            1. Start with YAML frontmatter (between --- delimiters).
            2. The body has exactly these H1 sections in this order:
               - # Hook              (30-80 words; engaging opening tied to the lore)
               - # Lore Introduction (60-120 words; narrative context from the fantasy world)
               - # Core Learning     (contains H2 sub-sections below)
               - # Guided Practice Quest (what to do in the interactive steps)
               - # Solo Practice Quest   (open-ended challenge)
               - # Integration           (150-300 words; cross-domain connection)
               - # Lore Conclusion       (50-100 words; narrative payoff)
            3. Core Learning MUST contain EXACTLY these H2 sub-sections in order:
               - ## Concept Introduction  (clear definition + diagram/table if useful)
               - ## Why It Matters        (practical relevance; 60-100 words)
               - ## Worked Examples       (2-3 annotated Java code examples)
               - ## Common Mistakes       (3-5 bullet points; learner pitfalls)
               - ## Mental Model          (analogy or metaphor; 80-120 words)
               - ## Mini Summary          (3-5 bullet summary recap)
            4. guidedSteps in the frontmatter: 2-4 steps with id, inputType, instruction,
               markingRule, hint, and reflectionPrompt for each step.
            5. soloAssessment in the frontmatter with type RUBRIC_REFLECTION, 5 rubricItems,
               and a modelAnswer code block.
            6. Lore style: Archmage Veylan is the wise mentor; the Academy is the setting;
               coding concepts map to magical disciplines. Keep lore immersive but not
               overwhelming — technical accuracy is paramount.
            7. APPRENTICE tier: 500-1200 body words (excluding code blocks). Use simple
               language, concrete examples, no assumed prior knowledge beyond what is stated.

            OUTPUT: The raw Markdown file content ONLY — no explanation, no preamble.
            Start with ---  (the opening frontmatter delimiter).
            """;

    // ── API call ──────────────────────────────────────────────────────────────

    private static String callClaude(String userMessage, String apiKey) throws Exception {
        String requestBody = """
                {
                  "model": "claude-opus-4-5",
                  "max_tokens": 4096,
                  "system": %s,
                  "messages": [{"role": "user", "content": %s}]
                }
                """.formatted(jsonString(SYSTEM_PROMPT), jsonString(userMessage));

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.anthropic.com/v1/messages"))
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .timeout(Duration.ofSeconds(120))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Anthropic API error " + response.statusCode() + ": " + response.body());
        }

        // Extract "text" from the first content block (simple parse, no full JSON lib needed)
        String body = response.body();
        int textStart = body.indexOf("\"text\":\"");
        if (textStart < 0) throw new IOException("Unexpected API response format: " + body);
        int contentStart = textStart + 8;
        // Walk forward to find unescaped closing quote
        StringBuilder sb = new StringBuilder();
        for (int i = contentStart; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '\\' && i + 1 < body.length()) {
                char next = body.charAt(i + 1);
                switch (next) {
                    case '"'  -> { sb.append('"');  i++; }
                    case 'n'  -> { sb.append('\n'); i++; }
                    case 't'  -> { sb.append('\t'); i++; }
                    case 'r'  -> { sb.append('\r'); i++; }
                    case '\\' -> { sb.append('\\'); i++; }
                    default   -> sb.append(c);
                }
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ── Main ──────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        Args a = Args.parse(args);

        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("ERROR: ANTHROPIC_API_KEY environment variable not set.");
            System.exit(1);
        }

        String userMessage = buildPrompt(a);

        System.out.println("Generating lesson '" + a.title + "' via Claude API...");
        String markdown = callClaude(userMessage, apiKey);

        if (a.dryRun) {
            System.out.println("\n--- DRY RUN OUTPUT ---\n");
            System.out.println(markdown);
            return;
        }

        Path outPath = a.out != null
                ? Path.of(a.out)
                : defaultPath(a);

        Files.createDirectories(outPath.getParent());
        Files.writeString(outPath, markdown, StandardCharsets.UTF_8);
        System.out.println("Lesson written to: " + outPath.toAbsolutePath());
        System.out.println("\nIMPORTANT: Review the generated file before committing.");
        System.out.println("Run the MarkdownLessonValidationTest to check compliance.");
    }

    private static String buildPrompt(Args a) {
        return """
                Generate a complete Arcane Academy Markdown lesson file with the following identity:

                - id:         %s
                - title:      %s
                - domainId:   %s
                - tier:       %s
                - moduleId:   %s
                - topicSlug:  %s
                - sortOrder:  %d
                - xpReward:   50
                - practiceType: NONE

                Topic brief: Teach a %s-level Java programming student about "%s".
                This is a standalone lesson; assume the student knows basic programming concepts
                but is new to this specific topic.

                Include:
                - All required body sections and Core Learning sub-sections.
                - 3 guidedSteps (mix of FILL_BLANK, SHORT_TEXT, MULTIPLE_CHOICE).
                - A RUBRIC_REFLECTION soloAssessment with 5 rubricItems and a Java modelAnswer.
                - An integrationDomains list with 1-2 cross-domain connections (e.g. psychology, mathematics).
                - A feynmanPrompt asking the learner to explain the concept to a non-programmer.

                The lore setting: Archmage Veylan teaches at the Arcane Academy. Coding concepts
                map to magical disciplines — e.g. variables are "rune vessels", methods are
                "incantations", arrays are "rune arrays / spell arrays".
                """.formatted(
                a.id, a.title, a.domain, a.tier, a.moduleId, a.topic, a.sort,
                a.tier.toLowerCase(), a.title);
    }

    private static Path defaultPath(Args a) {
        // content/{domain}/{tier-lowercase}/{topic}/{id}.md
        return Path.of("src", "main", "resources", "content",
                a.domain, a.tier.toLowerCase(), a.topic, a.id + ".md");
    }

    // ── Args ──────────────────────────────────────────────────────────────────

    record Args(
            String id, String title, String domain, String tier,
            String moduleId, String topic, int sort,
            String out, boolean dryRun
    ) {
        static Args parse(String[] args) {
            String id = null, title = null, domain = null, tier = "APPRENTICE";
            String moduleId = null, topic = null, out = null;
            int sort = 1;
            boolean dryRun = false;

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--id"        -> id       = args[++i];
                    case "--title"     -> title    = args[++i];
                    case "--domain"    -> domain   = args[++i];
                    case "--tier"      -> tier     = args[++i];
                    case "--module-id" -> moduleId = args[++i];
                    case "--topic"     -> topic    = args[++i];
                    case "--sort"      -> sort     = Integer.parseInt(args[++i]);
                    case "--out"       -> out      = args[++i];
                    case "--dry-run"   -> dryRun   = true;
                }
            }

            if (id == null || title == null || domain == null || moduleId == null || topic == null) {
                System.err.println("Usage: LessonAuthoringHarness --id ID --title TITLE " +
                        "--domain DOMAIN --module-id MODULE_ID --topic TOPIC " +
                        "[--tier TIER] [--sort N] [--out path] [--dry-run]");
                System.exit(1);
            }
            return new Args(id, title, domain, tier, moduleId, topic, sort, out, dryRun);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Encodes a Java string as a JSON string literal (with surrounding quotes). */
    private static String jsonString(String s) {
        return "\"" + s.replace("\\", "\\\\")
                       .replace("\"", "\\\"")
                       .replace("\n", "\\n")
                       .replace("\r", "\\r")
                       .replace("\t", "\\t") + "\"";
    }
}
