package com.arcane.academy.config;

import com.arcane.academy.model.Boss;
import com.arcane.academy.model.Quest;
import com.arcane.academy.repository.BossRepository;
import com.arcane.academy.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Chapter VI — The Capstone Forge
 * Students build a complete Task Manager application step by step,
 * ending with a project they can show to employers.
 */
@Component
@RequiredArgsConstructor
public class Ch6Seeder {
    private final QuestRepository repo;
    private final BossRepository bossRepo;

    void seed() {

        save("ch6-q1","The Blueprint","Chapter VI · Quest 1","Project Planning",6,1,200,"TaskManager.java",
          s(
            n("The Archmage's tower. At the very top, overlooking the entire Academy, sits the Chamber of Final Works. Every wizard who has ever graduated left something here: not a quiz answer, not a practice exercise, but a real creation. Something that exists. Something they built. Now it is your turn."),
            d("⚡","npc","The Archmage","s-npc","Exercises teach you the moves. Projects show you can play the game. Every spell you have learned — variables, loops, methods, classes — comes together here. You will build a Task Manager: a program that stores, lists, and manages tasks."),
            d("🧙","mentor","Master Velan","s-mentor","We start at the beginning: a Task class. In real software development, you always model your data first. What does a task have? A title. A description. A completion status. A unique ID."),
            e("Task Class — Full Model",
              "<span class='kw'>class</span> <span class='type'>Task</span> {\n    <span class='kw'>private int</span>     id;\n    <span class='kw'>private</span> <span class='type'>String</span>  title;\n    <span class='kw'>private</span> <span class='type'>String</span>  description;\n    <span class='kw'>private boolean</span> completed;\n    <span class='kw'>private static int</span> nextId = <span class='num'>1</span>;  <span class='cm'>// auto-incrementing ID</span>\n\n    <span class='type'>Task</span>(<span class='type'>String</span> title, <span class='type'>String</span> description) {\n        <span class='kw'>this</span>.id          = nextId++;\n        <span class='kw'>this</span>.title        = title;\n        <span class='kw'>this</span>.description  = description;\n        <span class='kw'>this</span>.completed    = <span class='kw'>false</span>;\n    }\n\n    <span class='kw'>void</span> complete() { <span class='kw'>this</span>.completed = <span class='kw'>true</span>; }\n\n    <span class='kw'>void</span> display() {\n        <span class='type'>String</span> status = completed ? <span class='str'>\"[DONE]\"</span> : <span class='str'>\"[OPEN]\"</span>;\n        System.out.println(id + <span class='str'>\". \"</span> + status + <span class='str'>\" \"</span> + title);\n    }\n}"),
            d("🧙","mentor","Master Velan","s-mentor","Notice <em>static int nextId</em> — a static field belongs to the class itself, not to any individual object. Every Task shares it, so each new Task gets a unique ID automatically. The <em>?</em> colon in display() is a ternary operator: a compact if-else that evaluates to one of two values."),
            d("⚡","npc","The Archmage","s-npc","Create the Task class and prove it works. Create two tasks and display both. This is the foundation everything else is built upon."),
            n("The ternary operator <code>completed ? \"[DONE]\" : \"[OPEN]\"</code> is new — it means: if completed is true, use \"[DONE]\", otherwise use \"[OPEN]\". It's shorthand for a simple if-else.")
          ),
          "Create the <strong>Task</strong> class with:<br>• Private fields: <code>id</code>, <code>title</code>, <code>description</code>, <code>completed</code><br>• A <code>static int nextId = 1</code> that auto-increments<br>• Constructor setting id, title, description, and completed=false<br>• <code>display()</code> printing: <strong>\"[id]. [OPEN/DONE] [title]\"</strong><br><br>In main: create two tasks and call display on each.<br><strong>1. [OPEN] Learn Java<br>2. [OPEN] Build a project</strong>",
          "Use <code>this.id = nextId++;</code> in the constructor. For display: <code>String status = completed ? \"[DONE]\" : \"[OPEN]\";</code> then print <code>id + \". \" + status + \" \" + title</code>",
          "// Define Task class above this\n\n\npublic class TaskManager {\n    public static void main(String[] args) {\n        // Create two tasks and display each\n\n    }\n}\n",
          "Two tasks materialise in the Chamber of Final Works, their IDs assigned automatically. The Archmage nods. \"The model is correct. Now we build on it.\"",
          tests(test("Task 1","null","1. [OPEN] Learn Java"),test("Task 2","null","2. [OPEN] Build a project")));

        save("ch6-q2","The Task Registry","Chapter VI · Quest 2","ArrayList & Methods",6,2,200,"TaskManager.java",
          s(
            n("The first stone is laid. Now the Task Manager needs to store tasks and provide operations on them. A real application doesn't just create objects — it organises them into collections and provides ways to add, list, and update them."),
            d("🧙","mentor","Master Velan","s-mentor","We build a <em>TaskManager</em> class that wraps an ArrayList of Tasks. Each operation becomes a method: addTask, listTasks, completeTask. This is how professional software is structured — behaviour grouped with the data it operates on."),
            e("TaskManager Class Shell",
              "<span class='kw'>import</span> java.util.ArrayList;\n\n<span class='kw'>class</span> <span class='type'>TaskManager</span> {\n    <span class='kw'>private</span> <span class='type'>ArrayList</span>&lt;<span class='type'>Task</span>&gt; tasks = <span class='kw'>new</span> <span class='type'>ArrayList</span>&lt;&gt;();\n\n    <span class='kw'>void</span> addTask(<span class='type'>String</span> title, <span class='type'>String</span> description) {\n        tasks.add(<span class='kw'>new</span> <span class='type'>Task</span>(title, description));\n    }\n\n    <span class='kw'>void</span> listTasks() {\n        <span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < tasks.size(); i++) {\n            tasks.get(i).display();\n        }\n    }\n\n    <span class='kw'>void</span> completeTask(<span class='type'>int</span> id) {\n        <span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < tasks.size(); i++) {\n            <span class='kw'>if</span> (tasks.get(i).getId() == id) {\n                tasks.get(i).complete();\n            }\n        }\n    }\n}"),
            d("🧙","mentor","Master Velan","s-mentor","Notice that completeTask loops through all tasks and finds the one with the matching ID. This is called a <em>linear search</em> — checking every element until you find what you need. For small lists it works well."),
            d("⚡","npc","The Archmage","s-npc","Build the TaskManager with those three methods. Add three tasks, list them, complete one, then list again to show the status changed."),
            n("You'll need a getId() method on Task too — add <code>int getId() { return id; }</code> to the Task class.")
          ),
          "Build a <code>TaskManager</code> class with <code>addTask</code>, <code>listTasks</code>, and <code>completeTask(int id)</code>.<br><br>In main: add <em>\"Learn Java\"</em>, <em>\"Build a project\"</em>, <em>\"Get a job\"</em>. List them. Complete task 2. List again.<br><br>Second listing should show:<br><strong>1. [OPEN] Learn Java<br>2. [DONE] Build a project<br>3. [OPEN] Get a job</strong>",
          "Add <code>int getId() { return id; }</code> to Task. TaskManager holds <code>ArrayList&lt;Task&gt; tasks</code>. completeTask loops to find the matching ID then calls .complete().",
          "import java.util.ArrayList;\n\nclass Task {\n    private static int nextId = 1;\n    private int id;\n    private String title;\n    private String description;\n    private boolean completed;\n\n    Task(String title, String description) {\n        this.id = nextId++;\n        this.title = title;\n        this.description = description;\n        this.completed = false;\n    }\n    int getId() { return id; }\n    void complete() { this.completed = true; }\n    void display() {\n        String status = completed ? \"[DONE]\" : \"[OPEN]\";\n        System.out.println(id + \". \" + status + \" \" + title);\n    }\n}\n\n// Write TaskManager class here\n\n\npublic class TaskManager2 {\n    public static void main(String[] args) {\n        // Create TaskManager, add 3 tasks, list, complete #2, list again\n\n    }\n}\n",
          "The registry holds three tasks. When task 2 is completed, it lights green in the display. The Archmage: \"A working registry. This is the core of every task management system ever written.\"",
          tests(test("DONE task 2","null","2. [DONE] Build a project"),test("OPEN task 1","null","1. [OPEN] Learn Java"),test("OPEN task 3","null","3. [OPEN] Get a job")));

        save("ch6-q3","The Exception Ward","Chapter VI · Quest 3","Exception Handling in Projects",6,3,200,"TaskManager.java",
          s(
            n("The Task Manager works — but only when used correctly. What happens when someone tries to complete a task that doesn't exist? What if someone passes a negative ID? Real software must handle these situations gracefully instead of crashing."),
            d("🧙","mentor","Master Velan","s-mentor","We introduce exception handling. Rather than crashing with an ugly error, your completeTask method should detect the problem and respond helpfully. If the ID doesn't exist, print a clear message instead of silently doing nothing."),
            e("Defensive completeTask",
              "<span class='kw'>void</span> completeTask(<span class='type'>int</span> id) {\n    <span class='kw'>if</span> (id <= <span class='num'>0</span>) {\n        System.out.println(<span class='str'>\"Error: ID must be positive\"</span>);\n        <span class='kw'>return</span>;\n    }\n    <span class='kw'>boolean</span> found = <span class='kw'>false</span>;\n    <span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < tasks.size(); i++) {\n        <span class='kw'>if</span> (tasks.get(i).getId() == id) {\n            tasks.get(i).complete();\n            found = <span class='kw'>true</span>;\n        }\n    }\n    <span class='kw'>if</span> (!found) {\n        System.out.println(<span class='str'>\"Task \"</span> + id + <span class='str'>\" not found\"</span>);\n    }\n}"),
            d("🧙","mentor","Master Velan","s-mentor","The <em>return</em> keyword inside a void method exits it immediately — like a trapdoor. We use it here to bail out early if the input is clearly invalid. The <em>found</em> boolean tracks whether we successfully found the task."),
            d("⚡","npc","The Archmage","s-npc","Test three scenarios: complete a valid task, try to complete an ID that doesn't exist, and try a negative ID. The program should handle all three without crashing."),
            n("This pattern — validate input, track whether you found what you looked for, report clearly — is used in virtually every professional codebase.")
          ),
          "Extend your TaskManager's <code>completeTask</code> with validation:<br><br>• If id &lt;= 0: print <strong>\"Error: ID must be positive\"</strong><br>• If no task has that id: print <strong>\"Task [id] not found\"</strong><br><br>In main: add 2 tasks, complete task 1 (success), try task 99 (not found), try id -1 (error).<br><br>Expected output includes:<br><strong>Task 99 not found<br>Error: ID must be positive</strong>",
          "Use a <code>boolean found = false;</code> flag. After the loop, check <code>if (!found)</code>. Validate at the start: <code>if (id <= 0) { ... return; }</code>",
          "import java.util.ArrayList;\n\nclass Task {\n    private static int nextId = 1;\n    private int id; private String title; private String description; private boolean completed;\n    Task(String t, String d) { this.id=nextId++; title=t; description=d; completed=false; }\n    int getId() { return id; }\n    void complete() { completed=true; }\n    void display() { System.out.println(id+\". \"+(completed?\"[DONE]\":\"[OPEN]\")+\" \"+title); }\n}\n\nclass TaskManagerV3 {\n    private ArrayList<Task> tasks = new ArrayList<>();\n    void addTask(String t, String d) { tasks.add(new Task(t,d)); }\n    void listTasks() { for(int i=0;i<tasks.size();i++) tasks.get(i).display(); }\n\n    // Update this method with validation\n    void completeTask(int id) {\n        // Add validation and found tracking here\n\n    }\n}\n\npublic class TaskManagerDefensive {\n    public static void main(String[] args) {\n        TaskManagerV3 tm = new TaskManagerV3();\n        tm.addTask(\"Learn Java\", \"Study the language\");\n        tm.addTask(\"Build project\", \"Apply the skills\");\n        tm.listTasks();\n        tm.completeTask(1);   // should work\n        tm.completeTask(99);  // not found\n        tm.completeTask(-1);  // error\n        tm.listTasks();\n    }\n}\n",
          "The Archmage tests edge cases methodically. Each invalid input is caught and explained. No crash. \"This is the mark of a professional: handling failure as carefully as success.\"",
          tests(test("Not found msg","null","Task 99 not found"),test("Error msg","null","Error: ID must be positive"),test("Task 1 done","null","1. [DONE] Learn Java")));

        save("ch6-q4","The Final Forge","Chapter VI · Quest 4","Complete Project",6,4,300,"FinalTaskManager.java",
          s(
            n("The Chamber of Final Works. The Archmage stands before a blank wall — the place where your creation will be inscribed permanently alongside those of every wizard who came before you. This is the final quest of the course. Put everything together."),
            d("⚡","npc","The Archmage","s-npc","A complete Task Manager. All features, all validation, properly encapsulated. This is what you will show to employers. Not exercises. This."),
            d("🧙","mentor","Master Velan","s-mentor","Everything you have learned comes together here: classes, encapsulation, ArrayList, for loops, method design, defensive programming. The full Task class with private fields and getters. The full TaskManager with all three operations working correctly."),
            e("What the Complete Solution Looks Like",
              "<span class='cm'>// Task: private fields, constructor, getters, display, complete</span>\n<span class='cm'>// TaskManager: ArrayList, addTask, listTasks, completeTask(with validation)</span>\n<span class='cm'>// main: create manager, add 4 tasks, list, complete 2 and 4, list again</span>\n\n<span class='cm'>// Expected output (second listing):</span>\n<span class='cm'>// 1. [OPEN] Write unit tests</span>\n<span class='cm'>// 2. [DONE] Read the docs</span>\n<span class='cm'>// 3. [OPEN] Review pull requests</span>\n<span class='cm'>// 4. [DONE] Fix the bug</span>"),
            d("⚡","npc","The Archmage","s-npc","Four tasks. Two completions. Then a full listing showing the correct status for each. After this, your Task Manager goes on GitHub — a real project on your real profile."),
            d("🧙","mentor","Master Velan","s-mentor","One piece of advice before you begin: write the Task class first and get it right, then write TaskManager, then write main. Always build from the inside out. Data model first. Operations second. Entry point last.")
          ),
          "Build the <strong>complete Task Manager</strong>. Task must have private fields with proper getters. TaskManager must have addTask, listTasks, and completeTask with validation.<br><br>In main: add 4 tasks — <em>\"Write unit tests\"</em>, <em>\"Read the docs\"</em>, <em>\"Review pull requests\"</em>, <em>\"Fix the bug\"</em>. Complete tasks 2 and 4. Print the final listing.<br><br>Expected final output:<br><strong>1. [OPEN] Write unit tests<br>2. [DONE] Read the docs<br>3. [OPEN] Review pull requests<br>4. [DONE] Fix the bug</strong>",
          "Build Task with private fields and getters. Build TaskManager with ArrayList. Make sure completeTask validates and uses a found flag. Work from inside out: Task → TaskManager → main.",
          "import java.util.ArrayList;\n\n// Build your complete Task class here\n\n\n// Build your complete TaskManager class here\n\n\npublic class FinalTaskManager {\n    public static void main(String[] args) {\n        // Create TaskManager\n        // Add 4 tasks\n        // Complete tasks 2 and 4\n        // List all tasks\n    }\n}\n",
          "Your code compiles. The output is correct. The wall lights up with your name and the date. The Archmage places a hand on your shoulder. \"You are no longer a student. You have built something real. Put this on GitHub tonight.\"",
          tests(test("Task 1 OPEN","null","1. [OPEN] Write unit tests"),test("Task 2 DONE","null","2. [DONE] Read the docs"),test("Task 3 OPEN","null","3. [OPEN] Review pull requests"),test("Task 4 DONE","null","4. [DONE] Fix the bug")));

        // Chapter VI boss — Project Defence
        bossRepo.save(com.arcane.academy.model.Boss.builder()
            .id("ch6-boss").name("The Project Examiner").glyph("📋").chapterNumber(6).xpReward(400)
            .intro("The Project Examiner reviews your Task Manager with a critical eye. \"I've seen a thousand half-finished projects. Let's see if yours holds up to real questions.\"")
            .questionsJson("[{\"id\":\"c6q1\",\"type\":\"multiple_choice\",\"question\":\"Why should Task fields be private rather than public?\",\"options\":[\"Private fields run faster\",\"To prevent invalid values being set directly from outside the class\",\"Java requires it\",\"Private fields use less memory\"],\"correct\":\"To prevent invalid values being set directly from outside the class\",\"explanation\":\"Encapsulation protects data integrity. If id were public, anything could change it to -1. With private + a getter, only valid access is possible.\"},{\"id\":\"c6q2\",\"type\":\"be_the_compiler\",\"question\":\"What does the ternary operator produce here?\\n\\nboolean done = true;\\nString status = done ? \\\"[DONE]\\\" : \\\"[OPEN]\\\";\\nSystem.out.println(status);\",\"options\":[\"[OPEN]\",\"[DONE]\",\"true\",\"Error\"],\"correct\":\"[DONE]\",\"explanation\":\"The ternary operator evaluates the condition before ?. If true it returns the first value, if false the second. done is true so [DONE] is returned.\"},{\"id\":\"c6q3\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to check if a list is empty before processing:\",\"code\":\"if (tasks.______() == 0) {\\n    System.out.println(\\\"No tasks\\\");\\n}\",\"correct\":\"size\",\"explanation\":\"ArrayList uses .size() to return its element count, unlike arrays which use .length.\"},{\"id\":\"c6q4\",\"type\":\"multiple_choice\",\"question\":\"What is a 'static' field like nextId used for in the Task class?\",\"options\":[\"It makes the field faster\",\"It belongs to the class itself and is shared across all instances\",\"It prevents the field from being changed\",\"It makes the field accessible from other classes\"],\"correct\":\"It belongs to the class itself and is shared across all instances\",\"explanation\":\"A static field exists once per class, not once per object. All Task objects share nextId, so each new task automatically gets a unique ID.\"},{\"id\":\"c6q5\",\"type\":\"be_the_compiler\",\"question\":\"If you add 3 tasks then call completeTask(99), what happens with this defensive implementation?\\n\\nvoid completeTask(int id) {\\n    boolean found = false;\\n    for(int i=0; i < tasks.size(); i++) {\\n        if(tasks.get(i).getId() == id) {\\n            tasks.get(i).complete();\\n            found = true;\\n        }\\n    }\\n    if(!found) System.out.println(\\\"Task \\\" + id + \\\" not found\\\");\\n}\",\"options\":[\"Crash with exception\",\"Print 'Task 99 not found'\",\"Do nothing silently\",\"Complete all tasks\"],\"correct\":\"Print 'Task 99 not found'\",\"explanation\":\"found stays false because no task has ID 99. After the loop, !found is true so the error message prints. This is defensive programming.\"}]")
            .build());
    }

    private void save(String id, String title, String eyebrow, String topic,
                      int ch, int ord, int xp, String file,
                      String story, String problem, String hint, String starter,
                      String win, String tests) {
        repo.save(Quest.builder().id(id).title(title).eyebrow(eyebrow).topic(topic)
            .chapterNumber(ch).orderInChapter(ord).xpReward(xp).filename(file)
            .storyJson(story).problemHtml(problem).hint(hint)
            .starterCode(starter).winStory(win).testCasesJson(tests).build());
    }
    private String s(String... b) { return "[" + String.join(",", b) + "]"; }
    private String n(String t) { return "{\"type\":\"narration\",\"text\":\"" + esc(t) + "\"}"; }
    private String d(String av, String cls, String sp, String sc, String t) {
        return "{\"type\":\"dialogue\",\"av\":\""+av+"\",\"cls\":\""+cls+"\",\"speaker\":\""+sp+"\",\"sCls\":\""+sc+"\",\"text\":\""+esc(t)+"\"}";
    }
    private String e(String label, String code) {
        return "{\"type\":\"example\",\"speaker\":\""+esc(label)+"\",\"text\":\""+esc(code)+"\"}";
    }
    private String tests(String... ts) { return "[" + String.join(",", ts) + "]"; }
    private String test(String label, String input, String expected) {
        return "{\"label\":\""+label+"\",\"input\":"+("null".equals(input)?"null":"\""+esc(input)+"\"")+",\"expected\":\""+esc(expected)+"\"}";
    }
    private String esc(String s) {
        return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\t","\\t");
    }
}
