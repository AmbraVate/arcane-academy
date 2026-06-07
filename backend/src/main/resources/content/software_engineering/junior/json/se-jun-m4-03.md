---
id: se-jun-m4-03
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m4
moduleTitle: "Module 4: APIs & Networking"
moduleGlyph: "🌐"
moduleSortOrder: 4
topicSlug: json
topicTitle: "JSON"
topicSortOrder: 3
lesson: json
title: "JSON"
sortOrder: 3
difficulty: 2
estimatedMinutes: 30
xpReward: 60
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m4-02]
integrationDomains: [crud_apis, orms]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes valid JSON with correct syntax (quoted keys, correct value types)"
    - "Maps a JSON object to a Java POJO with matching field names"
    - "Uses @JsonProperty to map a JSON key to a different Java field name"
    - "Serialises a Java object to JSON string using ObjectMapper"
    - "Deserialises a JSON string into a Java object using ObjectMapper.readValue()"
  keywords: [JSON, ObjectMapper, serialisation, deserialisation, "@JsonProperty", POJO, Jackson, readValue, writeValueAsString, field mapping, array, object, "null"]
  modelAnswer: |
    import com.fasterxml.jackson.annotation.JsonProperty;
    import com.fasterxml.jackson.databind.ObjectMapper;

    public record SpellDto(
        String name,
        int power,
        @JsonProperty("mana_cost") int manaCost
    ) {}

    // Serialise Java → JSON
    ObjectMapper mapper = new ObjectMapper();
    SpellDto spell = new SpellDto("Fireball", 80, 35);
    String json = mapper.writeValueAsString(spell);
    // {"name":"Fireball","power":80,"mana_cost":35}

    // Deserialise JSON → Java
    String input = "{\"name\":\"Ice\",\"power\":45,\"mana_cost\":20}";
    SpellDto result = mapper.readValue(input, SpellDto.class);
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Create a WizardDto Java record (or class) that maps to this JSON: {\"id\":1,\"name\":\"Aldric\",\"mana_pool\":250,\"active\":true}. Use @JsonProperty for the snake_case field mana_pool."
    inputConfig:
      language: java
      starterCode: |
        import com.fasterxml.jackson.annotation.JsonProperty;

        // Create WizardDto to map to:
        // {"id":1,"name":"Aldric","mana_pool":250,"active":true}
        public record WizardDto(
            // fields here
        ) {}
    markingRule: "Record has id (int or long), name (String), manaPool with @JsonProperty(\"mana_pool\") annotation, active (boolean), all correct types"
    hint: "Use @JsonProperty(\"mana_pool\") on the manaPool field to map the snake_case JSON key to the camelCase Java name."
    reflectionPrompt: "Why do JSON APIs often use snake_case while Java convention uses camelCase? How does @JsonProperty bridge this gap?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Use ObjectMapper to (a) serialise a WizardDto to a JSON string and (b) deserialise a JSON string back to a WizardDto. Handle the JsonProcessingException."
    inputConfig:
      language: java
      starterCode: |
        import com.fasterxml.jackson.databind.ObjectMapper;

        ObjectMapper mapper = new ObjectMapper();
        WizardDto wizard = new WizardDto(1, "Seraphine", 300, true);

        // (a) Serialise to JSON string
        String json = /* ... */;
        System.out.println(json);

        // (b) Deserialise back to WizardDto
        String input = "{\"id\":2,\"name\":\"Brynn\",\"mana_pool\":150,\"active\":false}";
        WizardDto fromJson = /* ... */;
        System.out.println(fromJson.name());
    markingRule: "Uses mapper.writeValueAsString(wizard) for serialisation, mapper.readValue(input, WizardDto.class) for deserialisation, handles or declares JsonProcessingException"
    hint: "mapper.writeValueAsString(object) and mapper.readValue(jsonString, TargetClass.class) — both throw JsonProcessingException (a checked exception)."
    reflectionPrompt: "What happens if the JSON has an unknown field that does not exist on the Java class? How can you configure Jackson to ignore unknown fields?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which JSON syntax is valid?"
    options:
      - "{name: 'Fireball', power: 80}"
      - "{\"name\": \"Fireball\", \"power\": 80}"
      - "{\"name\": \"Fireball\"; \"power\": 80}"
      - "[\"name\": \"Fireball\"]"
    correctIndex: 1
    feedback: "JSON requires double-quoted keys and string values. Single quotes are not valid JSON. Fields are separated by commas, not semicolons. Arrays use [] for ordered lists of values, not key-value pairs. Option 2 is the only valid JSON."
  - type: MULTIPLE_CHOICE
    question: "What does @JsonProperty(\"mana_cost\") on a Java field do?"
    options:
      - "Makes the field required in JSON input"
      - "Maps the Java field to/from the JSON key 'mana_cost' during serialisation/deserialisation"
      - "Validates that the field contains a cost value"
      - "Excludes the field from the JSON output"
    correctIndex: 1
    feedback: "@JsonProperty tells Jackson to use the specified string as the JSON key name instead of the Java field name. This bridges snake_case JSON (mana_cost) with camelCase Java (manaCost). It applies both to serialisation (Java→JSON) and deserialisation (JSON→Java)."
retrieval:
  recall: "What are the six JSON value types? Give a Java type that maps to each."
  explain: "Explain the difference between serialisation and deserialisation in the context of JSON and Java."
  mistakeId:
    code: |
      ObjectMapper mapper = new ObjectMapper();
      String json = "{\"name\":\"Fireball\",\"unknownField\":\"ignored\"}";
      SpellDto spell = mapper.readValue(json, SpellDto.class);
      // Throws: UnrecognizedPropertyException!
    answer: "By default Jackson throws an error when JSON contains fields not present on the target class. Fix: annotate the class with @JsonIgnoreProperties(ignoreUnknown = true) or configure the mapper: mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false). In Spring Boot this is often set globally in application.properties: spring.jackson.deserialization.fail-on-unknown-properties=false"
---

# Hook

JSON is the language of the web. Every REST API you call sends and receives JSON. Every frontend framework you use parses JSON. Every mobile app communicates in JSON. It is a simple format — objects and arrays of text, numbers, booleans, and nulls — but understanding how Java maps to and from JSON, and how Jackson handles the translation automatically in Spring Boot, is essential for every API you will build.

# Lore Introduction

The Academy's inter-school data exchange was a disaster. Each school sent messages in a different format: one used XML, one used custom binary, one sent comma-separated strings. Parsing each format required custom code per school. When the Academy mandated JSON as the single exchange format, integration code shrunk from hundreds of lines to a handful of annotations. The same `ObjectMapper` that the first school used also worked for the hundredth. Format standardisation with JSON did not just simplify parsing — it eliminated an entire category of integration bugs.

# Core Learning

## Concept Introduction

**JSON Syntax:**
JSON (JavaScript Object Notation) has six value types:
- `string`: `"Fireball"` — always double-quoted
- `number`: `80` or `3.14` — no quotes
- `boolean`: `true` or `false` — lowercase
- `null`: `null` — lowercase
- `object`: `{"key": value, ...}` — unordered key-value pairs
- `array`: `[value, value, ...]` — ordered list of values

**JSON Object example:**
```json
{
  "name": "Fireball",
  "power": 80,
  "manaCost": 35,
  "active": true,
  "tags": ["fire", "offensive"],
  "caster": {
    "id": 1,
    "name": "Aldric"
  },
  "description": null
}
```

**Mapping JSON to Java:**
| JSON type | Java type |
|---|---|
| string | `String` |
| number (integer) | `int`, `long`, `Integer` |
| number (decimal) | `double`, `float`, `BigDecimal` |
| boolean | `boolean`, `Boolean` |
| array | `List<T>`, `T[]` |
| object | Java class / record |
| null | `null` (for objects), or use `Optional` |

**Jackson — the Java JSON library:**
Jackson is the industry-standard Java JSON library (included by default in Spring Boot).

**Key annotations:**
- `@JsonProperty("json_key")` — map to/from a specific JSON key name
- `@JsonIgnore` — exclude field from serialisation/deserialisation
- `@JsonIgnoreProperties(ignoreUnknown = true)` — ignore unknown JSON fields

**ObjectMapper:**
```java
ObjectMapper mapper = new ObjectMapper();

// Java → JSON (serialisation)
String json = mapper.writeValueAsString(object);

// JSON → Java (deserialisation)
MyClass result = mapper.readValue(json, MyClass.class);

// JSON array → Java List
List<MyClass> list = mapper.readValue(jsonArray,
    mapper.getTypeFactory().constructCollectionType(List.class, MyClass.class));
```

## Why It Matters

Spring Boot uses Jackson automatically for REST controllers: when you return a Java object from a `@GetMapping` method, Spring serialises it to JSON. When a `@PostMapping` receives a request body with `@RequestBody`, Spring deserialises the JSON to your Java class. Understanding the mapping means you control exactly what JSON your API produces and accepts. `@JsonProperty` is how you bridge Java naming conventions (camelCase) with JSON API conventions (snake_case), which is a real-world necessity when integrating with third-party APIs.

## Worked Examples

**Example 1 — JSON to Java POJO mapping**

```json
{
  "spell_name": "Fireball",
  "power_level": 80,
  "mana_cost": 35,
  "is_aoe": true
}
```

```java
import com.fasterxml.jackson.annotation.JsonProperty;

public class SpellDto {
    @JsonProperty("spell_name")
    private String spellName;

    @JsonProperty("power_level")
    private int powerLevel;

    @JsonProperty("mana_cost")
    private int manaCost;

    @JsonProperty("is_aoe")
    private boolean aoe;

    // getters and setters (or use a record with @JsonProperty on components)
}
```

**Example 2 — Serialisation and deserialisation with ObjectMapper**

```java
import com.fasterxml.jackson.databind.ObjectMapper;

ObjectMapper mapper = new ObjectMapper();

// Serialise
SpellDto spell = new SpellDto("Fireball", 80, 35, true);
String json = mapper.writeValueAsString(spell);
// {"spell_name":"Fireball","power_level":80,"mana_cost":35,"is_aoe":true}

// Deserialise
String input = "{\"spell_name\":\"Ice Shard\",\"power_level\":45,\"mana_cost\":20,\"is_aoe\":false}";
SpellDto fromJson = mapper.readValue(input, SpellDto.class);
System.out.println(fromJson.getSpellName()); // Ice Shard
```

**Example 3 — Spring Boot controller with automatic JSON conversion**

```java
@RestController
@RequestMapping("/api/spells")
public class SpellController {

    // Return value is automatically serialised to JSON
    @GetMapping("/{id}")
    public SpellDto getSpell(@PathVariable Long id) {
        return spellService.findById(id); // Jackson serialises this
    }

    // Request body is automatically deserialised from JSON
    @PostMapping
    public SpellDto createSpell(@RequestBody CreateSpellRequest request) {
        return spellService.create(request); // Jackson deserialised 'request'
    }
}
```

## Common Mistakes

- **Using single quotes instead of double quotes.** JSON requires double quotes for all strings and keys. `{'name': 'Fireball'}` is not valid JSON.
- **Missing `@JsonProperty` for snake_case JSON keys.** Without it, `mana_cost` in JSON will not map to `manaCost` in Java — the field remains null.
- **Not handling unknown fields.** By default Jackson throws `UnrecognizedPropertyException` for JSON keys not on the Java class. Add `@JsonIgnoreProperties(ignoreUnknown = true)` to the class.
- **Using `new ObjectMapper()` in every method.** `ObjectMapper` is thread-safe and expensive to create. Declare it as a singleton (Spring's auto-configured `ObjectMapper` bean, or a static final field).
- **Confusing serialisation direction.** Serialisation is Java → JSON (writing). Deserialisation is JSON → Java (reading). Keep the direction straight.

## Mental Model

Think of JSON as a universal language both Java and the client speak. Your Java objects are like books in English. JSON is the Esperanto translation — a common format both sides understand. Serialisation is translation from English to Esperanto (Java to JSON). Deserialisation is translation from Esperanto back to English (JSON to Java). Jackson is the translator. `@JsonProperty` is the dictionary that maps words with different spellings in each language (snake_case ↔ camelCase).

## Mini Summary

- JSON has six types: string, number, boolean, null, object, array.
- All JSON keys and string values use double quotes.
- Jackson (`ObjectMapper`) handles Java ↔ JSON conversion.
- `@JsonProperty("key")` maps a Java field to a specific JSON key name.
- `@JsonIgnoreProperties(ignoreUnknown = true)` prevents errors from extra JSON fields.
- In Spring Boot, `@RequestBody` and `@ResponseBody` use Jackson automatically.

# Guided Practice Quest

Complete the two steps: create a `WizardDto` record with correct types and `@JsonProperty` for the snake_case field, then use `ObjectMapper` to serialise a Java object to JSON and deserialise a JSON string back to the Java type.

# Solo Practice Quest

Build a `SpellExchangeService` class. Define a `SpellPayload` class that maps to this JSON contract from a third-party API:
```json
{"spell_id": 1, "display_name": "Fireball", "power_points": 80, "cast_cost": 35, "is_unlocked": true}
```
Use `@JsonProperty` for all snake_case fields. Implement `import(String jsonString)` that deserialises a JSON string to `SpellPayload` and returns it. Implement `export(SpellPayload payload)` that serialises to a JSON string. Implement `importList(String jsonArray)` that deserialises a JSON array of SpellPayload objects to `List<SpellPayload>`. Handle exceptions appropriately. Write a `main` method that demonstrates a round-trip (Java → JSON → Java).

# Integration

JSON is the format that binds every module together. In **CRUD APIs**, your Spring Boot controllers use `@RequestBody` (Jackson deserialises JSON to Java) and return Java objects (Jackson serialises to JSON). In **ORMs**, entity fields map to database columns — the JSON ↔ Java ↔ database mapping chain is the full data flow of a REST API. In **Testing**, you will write test assertions on JSON response bodies. In the frontend, the React components consume the JSON your backend produces. Every field name, every type, every `@JsonProperty` annotation affects the contract between your backend and its clients.

**Integration question:** Your Java entity has a field `passwordHash: String`. This field should never appear in the JSON response your API returns. How would you ensure it is excluded from serialisation?

# Lore Conclusion

The Academy's exchange format is now universally understood. Every school sends JSON, every school receives JSON, and the same `ObjectMapper` handles it all. `@JsonProperty` bridges the naming gap between Java conventions and API contracts. Unknown fields are ignored gracefully. Round-trip serialisation and deserialisation are tested and trusted. The hundreds of lines of custom format parsers are gone. In their place: a handful of annotations and a standard library that does the translation. Format standardisation, done well, is invisible. Nobody notices it because nothing breaks.
