---
id: se-jun-m7-06
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m7
moduleTitle: "Module 7: Design Patterns"
moduleGlyph: "🏗️"
moduleSortOrder: 7
topicSlug: observer_pattern
topicTitle: "Observer Pattern"
topicSortOrder: 6
lesson: observer_pattern
title: "Observer Pattern"
sortOrder: 6
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [singleton_pattern]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly defines Subject (Observable), Observer interface, and ConcreteObserver roles"
    - "Shows subscribe/unsubscribe methods and the notify loop"
    - "Distinguishes Observer from Pub/Sub (direct reference vs message broker)"
    - "Identifies at least one memory leak risk and how to mitigate it"
    - "Demonstrates or describes a Spring ApplicationEvent equivalent"
  keywords: [subject, observer, subscribe, notify, decouple, event, listener, weak, Spring, publish, leak, interface]
  modelAnswer: |
    public interface QuestObserver {
        void onQuestCompleted(String questName, int xpEarned);
    }
    
    public class QuestTracker {
        private final List<QuestObserver> observers = new ArrayList<>();
        
        public void subscribe(QuestObserver observer) {
            observers.add(observer);
        }
        
        public void unsubscribe(QuestObserver observer) {
            observers.remove(observer);
        }
        
        public void completeQuest(String questName, int xp) {
            // business logic...
            notifyObservers(questName, xp);
        }
        
        private void notifyObservers(String questName, int xp) {
            for (QuestObserver observer : List.copyOf(observers)) {
                observer.onQuestCompleted(questName, xp);
            }
        }
    }
    
    // Concrete observers
    public class XpAwardService implements QuestObserver {
        public void onQuestCompleted(String questName, int xpEarned) {
            System.out.println("Awarding " + xpEarned + " XP");
        }
    }
    
    public class AchievementService implements QuestObserver {
        public void onQuestCompleted(String questName, int xpEarned) {
            System.out.println("Checking achievements for: " + questName);
        }
    }
guidedSteps:
  - id: obs-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In the Observer pattern, when the Subject changes state, what does it do?
    inputConfig:
      options:
        - "It calls a specific observer by name"
        - "It notifies all registered observers by calling their update method"
        - "It publishes an event to a message queue"
        - "It reads from all observers to gather their state"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It notifies all registered observers by calling their update method"]
      rejectedFeedback: "The Subject maintains a list of Observer references and iterates through them, calling each observer's update/notify method. It does not know which specific observer it is calling — only that each conforms to the Observer interface. This is what makes the pattern decoupled."
    hint: "The Subject holds a collection of observers and calls each one in sequence."
    reflectionPrompt: "Why is it important that the Subject doesn't know the specific types of its observers?"
  - id: obs-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The Observer pattern describes a ___ relationship: one Subject can have many Observers that are all notified when the Subject changes.
    inputConfig:
      placeholder: "relationship type"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["one-to-many", "1-to-many", "one to many"]
      rejectedFeedback: "Observer is a one-to-many relationship: one Subject, potentially many Observers. Each observer reacts to the same event independently. This is what makes it powerful for event-driven systems — a single event can trigger multiple unrelated reactions."
    hint: "How many Subjects? How many Observers?"
    reflectionPrompt: "Can you think of a real-world one-to-many notification system (e.g., a newsletter, a stock price feed)?"
  - id: obs-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the memory leak risk in the Observer pattern and describe one way to prevent it.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [unsubscribe, remove, weak, reference, leak, garbage, collect, deregister, memory]
      rejectedFeedback: "If an observer subscribes but is never unsubscribed, the Subject holds a strong reference to it. This prevents the garbage collector from reclaiming the observer's memory even if nothing else references it. Fix: always call unsubscribe() when done, use WeakReference in the observer list, or use an IoC container that manages listener lifecycle automatically."
    hint: "What happens to an object that is referenced by the Subject's list but should otherwise be garbage collected?"
    reflectionPrompt: "In a long-running application like a server, how could forgotten observer subscriptions accumulate into a serious problem over time?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key difference between the Observer pattern and Pub/Sub (publish-subscribe)?"
    options:
      - "Observer uses interfaces; Pub/Sub uses abstract classes"
      - "In Observer, the Subject has direct references to observers; in Pub/Sub, a broker decouples publishers from subscribers"
      - "Observer is synchronous; Pub/Sub is always asynchronous"
      - "They are identical — just different names for the same pattern"
    correctIndex: 1
    feedback: "In Observer, the Subject directly calls observer methods — it must hold references to them. In Pub/Sub, a message broker (like Kafka or a Spring ApplicationEventPublisher) sits between publisher and subscriber. The publisher doesn't know who subscribes. This extra layer of decoupling enables distributed, async architectures."
  - type: MULTIPLE_CHOICE
    question: "In Spring, what annotation on a method marks it as a listener for ApplicationEvents?"
    options:
      - "@Observer"
      - "@Subscribe"
      - "@EventListener"
      - "@Listen"
    correctIndex: 2
    feedback: "@EventListener marks a method as an application event listener in Spring. Combined with ApplicationEventPublisher.publishEvent(), this is Spring's built-in Observer/event system, handling registration and notification automatically within the IoC container."
retrieval:
  recall: "Name the three participants in the Observer pattern and describe each role in one sentence."
  explain: "How does the Observer pattern reduce coupling between the Subject and its observers? What would the coupling look like without the pattern?"
  mistakeId:
    code: |
      public class StockMarket {
          private PriceDisplay display;
          private AlertService alert;
          
          public StockMarket(PriceDisplay display, AlertService alert) {
              this.display = display;
              this.alert = alert;
          }
          
          public void priceChanged(double newPrice) {
              display.update(newPrice);
              alert.check(newPrice);
          }
      }
    answer: "StockMarket is directly coupled to PriceDisplay and AlertService — it must be modified whenever a new observer is added (e.g., adding a Logger or a ReportService). Fix: introduce a StockObserver interface with an onPriceChanged(double) method. StockMarket holds a List<StockObserver> with subscribe/unsubscribe methods. Any new observer simply implements the interface and subscribes — StockMarket never changes."
---

# Hook

Your quest system just completed a quest. Now you need to award XP to the player, check if any achievements were unlocked, update the leaderboard, send a notification, and log the event. How does your `QuestService` know about all of those things? Do you inject all five services into `QuestService`? What happens when you add a sixth?

The Observer pattern solves exactly this problem. The Subject (QuestService) does not need to know about XpService, AchievementService, LeaderboardService, or any of the others. Instead, each of those services *subscribes* to quest completion events. When a quest completes, QuestService notifies all subscribers — whoever they are, however many there are — without knowing their identities.

> Reflection: Think about a news website. When a story is published, it notifies subscribers by email, sends a push notification to an app, and posts to social media. How would you design that without Observer? What problems do you run into?

# Lore Introduction

Every morning, the Academy's Herald sends out a proclamation from the Archmage. In the old system, the Herald had a list of every guild, chamber, and dormitory — and hand-delivered a copy to each. When a new guild was founded, the Herald had to be personally informed and the list updated. It was bureaucratic, fragile, and slow.

Veylan introduced the Notice Board. The Herald posts a single proclamation. Anyone who cares has registered with the Notice Board. When a proclamation appears, the Board notifies all registered parties. Adding a new guild means registering with the Board — the Herald never changes.

The Herald is the Subject. The guilds are Observers. The Notice Board is the notification mechanism.

# Core Learning

## Concept Introduction

The **Observer pattern** defines a one-to-many dependency between objects. When the Subject changes state, all registered Observers are notified automatically. The Subject and Observers are loosely coupled — the Subject knows only that its observers implement the Observer interface.

Three participants:
- **Subject (Observable)** — maintains the list of observers; provides subscribe/unsubscribe; notifies on state change
- **Observer interface** — declares the update method all observers must implement
- **ConcreteObserver** — implements a specific reaction to the Subject's event

```java
// Observer interface
public interface QuestObserver {
    void onQuestCompleted(String questName, int xpEarned);
}

// Subject
public class QuestTracker {
    private final List<QuestObserver> observers = new ArrayList<>();

    public void subscribe(QuestObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(QuestObserver observer) {
        observers.remove(observer);
    }

    public void completeQuest(String questName, int xp) {
        // core business logic
        System.out.println("Quest completed: " + questName);
        // notify all observers
        notifyObservers(questName, xp);
    }

    private void notifyObservers(String questName, int xp) {
        // copy to avoid ConcurrentModificationException if an observer unsubscribes during notification
        for (QuestObserver obs : List.copyOf(observers)) {
            obs.onQuestCompleted(questName, xp);
        }
    }
}

// ConcreteObservers
public class XpAwardService implements QuestObserver {
    public void onQuestCompleted(String questName, int xpEarned) {
        System.out.println("Awarding " + xpEarned + " XP for: " + questName);
    }
}

public class AchievementService implements QuestObserver {
    public void onQuestCompleted(String questName, int xpEarned) {
        System.out.println("Checking achievements for: " + questName);
    }
}
```

Usage:

```java
QuestTracker tracker = new QuestTracker();
tracker.subscribe(new XpAwardService());
tracker.subscribe(new AchievementService());

tracker.completeQuest("Defeat the Dragon", 500);
// Output:
// Quest completed: Defeat the Dragon
// Awarding 500 XP for: Defeat the Dragon
// Checking achievements for: Defeat the Dragon
```

## Why It Matters

Without Observer, `QuestTracker` would need direct references to `XpAwardService`, `AchievementService`, and every future listener. Each new subscriber requires modifying `QuestTracker` — a clear violation of Open/Closed. Observer makes `QuestTracker` closed for modification and open for extension by new observers.

## Worked Examples

**Spring ApplicationEvent:**

```java
// Event class
public class QuestCompletedEvent {
    private final String questName;
    private final int xp;
    public QuestCompletedEvent(String questName, int xp) {
        this.questName = questName; this.xp = xp;
    }
    public String getQuestName() { return questName; }
    public int getXp() { return xp; }
}

// Publisher (Subject equivalent)
@Service
public class QuestService {
    private final ApplicationEventPublisher publisher;
    public QuestService(ApplicationEventPublisher publisher) { this.publisher = publisher; }

    public void completeQuest(String name, int xp) {
        // business logic...
        publisher.publishEvent(new QuestCompletedEvent(name, xp));
    }
}

// Listener (Observer equivalent)
@Component
public class XpAwardService {
    @EventListener
    public void handle(QuestCompletedEvent event) {
        System.out.println("Awarding " + event.getXp() + " XP");
    }
}
```

Spring handles registration automatically via `@EventListener`. This is Observer (within a JVM) not Pub/Sub (which would use an external broker like Kafka).

## Common Mistakes

**Memory leaks from unsubscribed observers.** If a Subject holds a strong reference to an observer, the observer cannot be garbage collected even if it is "done". Always call `unsubscribe()` or use a framework that manages lifecycle.

**Modifying the observer list during notification.** If an observer calls `unsubscribe()` during the notification loop, a `ConcurrentModificationException` occurs. Use `List.copyOf(observers)` before iterating.

**Ordering dependencies between observers.** Observers should be independent. If observer B depends on observer A having already run, you have a hidden ordering dependency that will cause bugs as the system grows.

## Mental Model

Think of a YouTube channel subscription. The channel (Subject) publishes videos. Subscribers (Observers) receive notifications. The channel does not know who is subscribed — it just publishes. A new subscriber can sign up without the channel knowing. A subscriber can unsubscribe without disrupting others. The channel and subscriber are decoupled through the subscription mechanism.

## Mini Summary

- Observer defines a one-to-many relationship: one Subject, many Observers.
- Subject notifies all registered observers when it changes state.
- Subject and Observer are decoupled — Subject only knows the Observer interface.
- Memory leaks occur when observers are never unsubscribed; always provide unsubscribe.
- Spring's @EventListener is Observer-pattern built into the IoC container.

# Guided Practice Quest

**Quest: The Academy Notice Board**

The Academy's Herald must notify all registered guilds when a new proclamation is issued. You must demonstrate understanding of the Observer pattern: the subscribe/notify loop, decoupling, and memory leak prevention.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

The Academy needs an exam result system. When a result is published (`ExamTracker.publishResult(String student, int score)`), three things should happen: `GradeRecorder` stores the result, `BadgeService` checks if the student earned a distinction badge, and `ParentNotifier` sends a notification if the score is below 50.

Implement the `ExamObserver` interface, the `ExamTracker` Subject, and the three ConcreteObserver classes. Then write a reflection (minimum 100 words) covering:
1. How adding a fourth observer (e.g., a LeaderboardService) would be done
2. What memory leak risk exists in your implementation and how you would mitigate it
3. How you would convert this to a Spring ApplicationEvent approach

# Integration

**Connecting to Psychology — Attentional Systems and Event Saliency**

Human attention operates on an event-driven model. The brain does not continuously poll the environment — "is that spider still there? is that spider still there?" — it waits for salient changes (movement, sound, novelty) and then triggers a response. Psychologists call this the orienting response: automatic attention reallocation triggered by environmental change.

Observer-based software architectures mirror this attentional model. Instead of services continuously polling a Subject for changes (CPU-intensive, wasteful), they register interest and are notified when relevant state changes occur. The notification is the attentional trigger.

Psychological research also shows that too many simultaneous attentional triggers cause cognitive overload — the brain cannot meaningfully process all events at once. Systems with uncontrolled Observer chains face the same problem: a single event triggers dozens of observers, some of which trigger further events, producing cascading notification storms that are difficult to reason about and debug.

> Reflection: How does the analogy between attentional systems and event-driven software help you think about the right granularity for events? Should every property change in a Subject trigger a notification, or should notifications be reserved for meaningful state transitions?

# Lore Conclusion

The Notice Board had transformed the Academy's administrative life. When Veylan posted a proclamation about the upcoming Winter Solstice Tournament, every guild hall, dormitory, and training chamber received the notification within minutes. No Herald had been redirected. No list had been updated.

But an old Runesmith, Petra, pulled Veylan aside after the board's first month of operation. "Three observers never unregistered," she said. "Two guilds that dissolved and a training chamber that was sealed. Their notification crystals still hang on the board, consuming ether, receiving messages no one reads." Veylan nodded gravely. "A lesson for every pattern," he said. "What you register, you must also deregister. Subscribe with intention. Unsubscribe with discipline."

---
