package com.arcane.academy.config;

import com.arcane.academy.repository.QuestRepository;
import org.springframework.stereotype.Component;

// ══════════════════════════════════════════════════════════════════════════════
// CHAPTER IV — THE GRAND GRIMOIRE (OOP)
// ══════════════════════════════════════════════════════════════════════════════
@Component
public class Ch4Seeder extends AbstractChapterSeeder {

    public Ch4Seeder(QuestRepository questRepository) {
        super(questRepository);
    }

    @Override
    public void seed() {

        q("ch4-q1","The Golem Foundry","Chapter IV · Quest 1","Classes & Objects",4,1,150,"GolemFoundry.java",
          story(
            n("The Golem Foundry. Wizards here don't just write spells — they create beings. Every golem starts as a blueprint, then is brought to life as a unique individual. This is the heart of object-oriented programming."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>class</em> is the blueprint. An <em>object</em> is one specific instance. You can create many objects from one class, each with different field values. Think of 'Wizard' as the class; Aldric is one specific Wizard object."),
            e("Worked Example — Class & Object","<span class='kw'>class</span> <span class='type'>Wizard</span> {\n    <span class='type'>String</span> name;\n    <span class='type'>int</span>    level;\n\n    <span class='type'>Wizard</span>(<span class='type'>String</span> name, <span class='type'>int</span> level) {\n        <span class='kw'>this</span>.name  = name;   <span class='cm'>// 'this' = the field</span>\n        <span class='kw'>this</span>.level = level;\n    }\n\n    <span class='kw'>void</span> describe() {\n        <span class='kw'>System</span>.out.println(name + <span class='str'>\" level \"</span> + level);\n    }\n}\n\n<span class='cm'>// Creating objects</span>\n<span class='type'>Wizard</span> w = <span class='kw'>new</span> <span class='type'>Wizard</span>(<span class='str'>\"Aldric\"</span>, <span class='num'>5</span>);\nw.describe();  <span class='cm'>// Aldric level 5</span>"),
            d("⚙️","npc","Foundry Golem G-1","s-npc","BLUEPRINT REQUIRED. FIELDS: NAME AND LEVEL. CONSTRUCTOR ACCEPTS BOTH. METHOD: PRINT SELF-DESCRIPTION.")
          ),
          "Create class <strong>Wizard</strong> with <code>name</code>, <code>level</code>, constructor, and <code>describe()</code> printing: <strong>\"Wizard [name] is level [level].\"</strong><br><br>Create two: <code>Wizard(\"Aldric\", 5)</code> and <code>Wizard(\"Zara\", 9)</code> and call describe on each.",
          "Define class above the main class. Use <code>this.name = name;</code> in constructor.",
          "// Define Wizard class here\n\npublic class GolemFoundry {\n    public static void main(String[] args) {\n        // Create two Wizard objects and call describe()\n\n    }\n}\n",
          "G-1 booms: \"BLUEPRINTS ACCEPTED. OBJECTS INSTANTIATED. WELL DONE.\"",
          tests(test("Aldric","null","Wizard Aldric is level 5."),test("Zara","null","Wizard Zara is level 9.")));

        q("ch4-q2","The Seal of Encapsulation","Chapter IV · Quest 2","Encapsulation",4,2,160,"Encapsulation.java",
          story(
            n("The Vault of Secrets. Not all knowledge should be freely accessible. Powerful wizards protect their inner workings — exposing only what others need to interact with."),
            d("🧙","mentor","Master Velan","s-mentor","Make a field <em>private</em> and only code inside the class can access it directly. Provide a <em>getter</em> to read it and a <em>setter</em> to write it. The setter can validate the new value before accepting it — this is the power of encapsulation."),
            e("Worked Example — Encapsulation","<span class='kw'>class</span> <span class='type'>Wizard</span> {\n    <span class='kw'>private</span> <span class='type'>int</span> level;\n\n    <span class='kw'>public int</span> getLevel() {\n        <span class='kw'>return</span> level;\n    }\n\n    <span class='kw'>public void</span> setLevel(<span class='type'>int</span> level) {\n        <span class='kw'>if</span> (level > <span class='num'>0</span>) <span class='kw'>this</span>.level = level;\n        <span class='cm'>// invalid values are silently ignored</span>\n    }\n}"),
            d("🔒","npc","Vault Keeper Sable","s-npc","A wizard's level must never go negative. The setter must enforce this. I'll test that invalid values are rejected without error — they're simply ignored.")
          ),
          "Create <strong>Wizard</strong> with <em>private</em> <code>name</code> and <code>level</code>. Add <code>getName()</code>, <code>getLevel()</code>, <code>setLevel(int)</code> (ignores negatives).<br><br>In main: create Wizard(\"Aldric\", 5), call setLevel(-1), print name then level. Output: <strong>Aldric</strong> then <strong>5</strong>.",
          "Setter: <code>if (level > 0) this.level = level;</code> — negative values ignored.",
          "public class Encapsulation {\n    static class Wizard {\n        private String name;\n        private int level;\n        Wizard(String name, int level) { this.name = name; this.level = level; }\n        // Add getName(), getLevel(), setLevel()\n\n    }\n    public static void main(String[] args) {\n        Wizard w = new Wizard(\"Aldric\", 5);\n        w.setLevel(-1);\n        System.out.println(w.getName());\n        System.out.println(w.getLevel());\n    }\n}\n",
          "\"Properly guarded,\" Sable says. \"The field cannot be corrupted from outside.\"",
          tests(test("Name=Aldric","null","Aldric"),test("Level=5","null","5")));

        q("ch4-q3","The Order of Lineage","Chapter IV · Quest 3","Inheritance",4,3,170,"Inheritance.java",
          story(
            n("The Hall of Lineage. Every wizard order shares common ground — name, level, the ability to describe themselves. Writing the same code twice is a cardinal sin."),
            d("🧙","mentor","Master Velan","s-mentor","<em>Inheritance</em> lets one class extend another, gaining all its fields and methods. Use <em>extends</em>. In the child constructor, call <em>super()</em> first to run the parent constructor. Use <em>@Override</em> to replace a parent method with your own version."),
            e("Worked Example — Inheritance","<span class='kw'>class</span> <span class='type'>Animal</span> {\n    <span class='type'>String</span> name;\n    <span class='type'>Animal</span>(<span class='type'>String</span> n) { name = n; }\n    <span class='kw'>void</span> speak() { <span class='kw'>System</span>.out.println(<span class='str'>\"...\"</span>); }\n}\n\n<span class='kw'>class</span> <span class='type'>Dog</span> <span class='kw'>extends</span> <span class='type'>Animal</span> {\n    <span class='type'>Dog</span>(<span class='type'>String</span> n) { <span class='kw'>super</span>(n); }  <span class='cm'>// calls Animal constructor</span>\n\n    @Override\n    <span class='kw'>void</span> speak() { <span class='kw'>System</span>.out.println(name + <span class='str'>\" barks\"</span>); }\n}"),
            d("🧝","npc","Enchantress Lyra","s-npc","Create a BattleMage that extends Wizard. It adds a weapon field and overrides describe to mention the weapon.")
          ),
          "Given <code>Wizard</code> base class, create <strong>BattleMage</strong> extending Wizard with <code>String weapon</code>. Override <code>describe()</code>: <strong>\"BattleMage [name] wields [weapon].\"</strong><br><br>In main: BattleMage(\"Kael\", 7, \"Flameblade\").describe() and Wizard(\"Zara\", 3).describe()",
          "Use <code>class BattleMage extends Wizard</code> with <code>super(name, level)</code> in constructor.",
          "class Wizard {\n    String name; int level;\n    Wizard(String name, int level) { this.name = name; this.level = level; }\n    void describe() { System.out.println(\"Wizard \" + name + \" is level \" + level + \".\"); }\n}\n\n// Write BattleMage here\n\npublic class Inheritance {\n    public static void main(String[] args) {\n        // Create both and call describe\n\n    }\n}\n",
          "\"The lineage is complete,\" the Hall intones.",
          tests(test("BattleMage","null","BattleMage Kael wields Flameblade."),test("Wizard","null","Wizard Zara is level 3.")));

        q("ch4-q4","The Polymorphic Mirrors","Chapter IV · Quest 4","Polymorphism",4,4,180,"Polymorphism.java",
          story(
            n("The Mirror Gallery. Each mirror holds a different type of wizard. When you call their name, each responds in their own way — even though from the outside they're all just 'wizards'."),
            d("🧙","mentor","Master Velan","s-mentor","This is <em>polymorphism</em>. A child object can be stored in a parent-type variable. When you call a method, Java looks at the actual object's type at runtime and calls the correct overridden version. This is how you write flexible code that works with any subtype."),
            e("Worked Example — Polymorphism","<span class='type'>Wizard</span>[] gallery = {\n    <span class='kw'>new</span> <span class='type'>Wizard</span>(<span class='str'>\"Zara\"</span>, <span class='num'>3</span>),\n    <span class='kw'>new</span> <span class='type'>BattleMage</span>(<span class='str'>\"Kael\"</span>, <span class='num'>7</span>, <span class='str'>\"Sword\"</span>)\n};\n\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < gallery.length; i++) {\n    gallery[i].describe();  <span class='cm'>// calls the right version</span>\n}"),
            d("🪞","npc","Mirror Keeper Illen","s-npc","Add a Healer class with a spell field. Store Wizard, BattleMage, Healer in one array. Loop and call describe — three different outputs from one loop.")
          ),
          "Add <code>Healer</code> class with <code>spell</code> field. Override describe → <strong>\"Healer [name] casts [spell].\"</strong><br><br>Store all three in <code>Wizard[]</code> array, loop calling describe. Expected:<br><strong>Wizard Zara is level 3.<br>BattleMage Kael wields Flameblade.<br>Healer Mira casts Rejuvenate.</strong>",
          "Declare <code>Wizard[] gallery = { new Wizard(...), new BattleMage(...), new Healer(...) };</code>",
          "class Wizard {\n    String name; int level;\n    Wizard(String n, int l) { name=n; level=l; }\n    void describe() { System.out.println(\"Wizard \"+name+\" is level \"+level+\".\"); }\n}\nclass BattleMage extends Wizard {\n    String weapon;\n    BattleMage(String n, int l, String w) { super(n,l); weapon=w; }\n    @Override void describe() { System.out.println(\"BattleMage \"+name+\" wields \"+weapon+\".\"); }\n}\n// Write Healer class here\n\npublic class Polymorphism {\n    public static void main(String[] args) {\n        // Create Wizard array with all three types\n\n    }\n}\n",
          "\"One call, three answers,\" Illen whispers. \"That is polymorphism.\"",
          tests(test("Wizard Zara","null","Wizard Zara is level 3."),test("BattleMage Kael","null","BattleMage Kael wields Flameblade."),test("Healer Mira","null","Healer Mira casts Rejuvenate.")));

        q("ch4-q5","The Abstract Sanctum","Chapter IV · Quest 5","Abstract Classes",4,5,200,"AbstractSanctum.java",
          story(
            n("The Sanctum of Forms. Some blueprints are too general to build directly. You can't make a generic 'Shape' — only specific shapes. These incomplete blueprints are abstract classes."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>abstract class</em> uses the <em>abstract</em> keyword. It can have abstract methods — declared but not implemented, ending with a semicolon. Any concrete subclass must implement all abstract methods. You cannot create an object of an abstract class directly."),
            e("Worked Example — Abstract Class","<span class='kw'>abstract class</span> <span class='type'>Shape</span> {\n    <span class='kw'>abstract double</span> area();  <span class='cm'>// no body — subclass must provide it</span>\n}\n\n<span class='kw'>class</span> <span class='type'>Circle</span> <span class='kw'>extends</span> <span class='type'>Shape</span> {\n    <span class='type'>double</span> radius;\n    <span class='type'>Circle</span>(<span class='type'>double</span> r) { radius = r; }\n\n    @Override\n    <span class='type'>double</span> area() { <span class='kw'>return</span> Math.PI * radius * radius; }\n}"),
            d("📐","npc","Architect Tessara","s-npc","Abstract Shape with area(). Circle (radius) and Rectangle (width, height). Both extend Shape. I need the area of each printed.")
          ),
          "Create abstract <strong>Shape</strong> with <code>abstract double area()</code>. Create <strong>Circle</strong> and <strong>Rectangle</strong>.<br><br>In main print:<br>• Circle radius 5: <strong>78.5</strong><br>• Rectangle 4×6: <strong>24.0</strong>",
          "Circle area: <code>Math.PI * radius * radius</code>. Round: <code>Math.round(circle.area()*10)/10.0</code>",
          "// Write abstract Shape, Circle, and Rectangle\n\npublic class AbstractSanctum {\n    public static void main(String[] args) {\n        Shape circle = new Circle(5);\n        Shape rect = new Rectangle(4, 6);\n        System.out.println(Math.round(circle.area() * 10) / 10.0);\n        System.out.println(rect.area());\n    }\n}\n",
          "\"78.5 and 24.0. Geometry is magic made precise,\" Tessara confirms.",
          tests(test("Circle=78.5","null","78.5"),test("Rectangle=24.0","null","24.0")));
    }
}
