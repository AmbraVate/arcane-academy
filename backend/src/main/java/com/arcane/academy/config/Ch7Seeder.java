package com.arcane.academy.config;

import com.arcane.academy.repository.QuestRepository;
import org.springframework.stereotype.Component;

// ══════════════════════════════════════════════════════════════════════════════
// CHAPTER VII — THE INTERVIEW GAUNTLET
// Classic junior Java interview problems under timed conditions.
// ══════════════════════════════════════════════════════════════════════════════
@Component
public class Ch7Seeder extends AbstractChapterSeeder {

    public Ch7Seeder(QuestRepository questRepository) {
        super(questRepository);
    }

    @Override
    public void seed() {

        q("ch7-q1","FizzBuzz","Chapter VII · Quest 1","Classic Interview Problem",7,1,200,"FizzBuzz.java",
          story(
            n("The Interview Gauntlet — the final proving ground. Beyond this chamber lies the working world. Hundreds of thousands of developers have passed through rooms like this one, given exactly this problem, on exactly this kind of morning. The examiner across the table doesn't need you to explain it. They just need to see you solve it."),
            d("🧑‍💼","npc","The Examiner","s-npc","FizzBuzz. Print numbers 1 to 30. Multiples of 3: Fizz. Multiples of 5: Buzz. Multiples of both: FizzBuzz. Anything else: the number. You have five minutes."),
            d("🧙","mentor","Master Velan","s-mentor","The key is the order of checks. Test for divisibility by both 3 and 5 first — otherwise your code will print Fizz for 15 instead of FizzBuzz. Use the modulo operator <em>%</em> — it gives you the remainder after division. If <em>n % 3 == 0</em>, n divides evenly by 3."),
            e("Modulo Operator",
              "<span class='cm'>// % gives the remainder</span>\nSystem.out.println(<span class='num'>10</span> % <span class='num'>3</span>);  <span class='cm'>// 1 (10 = 3×3 + 1)</span>\nSystem.out.println(<span class='num'>9</span>  % <span class='num'>3</span>);  <span class='cm'>// 0 (9 = 3×3 + 0, divisible)</span>\nSystem.out.println(<span class='num'>15</span> % <span class='num'>3</span>);  <span class='cm'>// 0 (divisible by 3)</span>\nSystem.out.println(<span class='num'>15</span> % <span class='num'>5</span>);  <span class='cm'>// 0 (divisible by 5)</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Check divisibility by both first with &&, then by 3 alone, then by 5 alone, then use else for the number. This order guarantees FizzBuzz appears exactly where it should.")
          ),
          "Print numbers 1 to 30 using a for loop:<br>• Divisible by both 3 and 5 → print <strong>FizzBuzz</strong><br>• Divisible by 3 only → print <strong>Fizz</strong><br>• Divisible by 5 only → print <strong>Buzz</strong><br>• Otherwise → print the number<br><br>Check: 15 → <strong>FizzBuzz</strong>, 9 → <strong>Fizz</strong>, 10 → <strong>Buzz</strong>, 7 → <strong>7</strong>",
          "Order matters: check % 3 == 0 && % 5 == 0 FIRST. Then else if (% 3 == 0). Then else if (% 5 == 0). Then else.",
          "// FizzBuzz: print 1 to 30 with Fizz/Buzz/FizzBuzz rules\n\n",
          "The examiner watches the output scroll by. 15: FizzBuzz. 30: FizzBuzz. Every number correct. \"Clean. Correct. First try. Good.\" A tick goes on the pad.",
          tests(test("15=FizzBuzz","null","FizzBuzz"),test("9=Fizz","null","Fizz"),test("10=Buzz","null","Buzz"),test("7=7","null","7")));

        q("ch7-q2","Palindrome Check","Chapter VII · Quest 2","String Manipulation",7,2,200,"Palindrome.java",
          story(
            n("The examiner flips to the next page. A new problem."),
            d("🧑‍💼","npc","The Examiner","s-npc","Write a method that checks if a word is a palindrome. A palindrome reads the same forwards and backwards. 'racecar' is one. 'hello' is not. Return a boolean. Don't use any built-in reverse method."),
            d("🧙","mentor","Master Velan","s-mentor","The approach: compare the first character with the last, the second with the second-to-last, and so on, until you reach the middle. If any pair doesn't match, it's not a palindrome. If all pairs match, it is."),
            e("Two-Pointer Approach",
              "<span class='kw'>static boolean</span> isPalindrome(<span class='type'>String</span> s) {\n    <span class='type'>int</span> left  = <span class='num'>0</span>;\n    <span class='type'>int</span> right = s.length() - <span class='num'>1</span>;\n\n    <span class='kw'>while</span> (left < right) {\n        <span class='kw'>if</span> (s.charAt(left) != s.charAt(right)) {\n            <span class='kw'>return false</span>;  <span class='cm'>// mismatch found</span>\n        }\n        left++;\n        right--;\n    }\n    <span class='kw'>return true</span>;  <span class='cm'>// all pairs matched</span>\n}"),
            d("🧙","mentor","Master Velan","s-mentor","This is the <em>two-pointer technique</em> — a classic pattern that appears in dozens of interview problems. One pointer starts at the left, one at the right. They move toward the middle. When they meet or cross, you're done.")
          ),
          "Write a static method <strong>isPalindrome(String s)</strong> that returns true if s is a palindrome, false otherwise. Use the two-pointer approach — no StringBuilder.reverse().<br><br>In main:<br>• isPalindrome(\"racecar\") → print <strong>true</strong><br>• isPalindrome(\"hello\") → print <strong>false</strong><br>• isPalindrome(\"madam\") → print <strong>true</strong>",
          "Use two int variables left=0 and right=s.length()-1. Loop while left < right. If s.charAt(left) != s.charAt(right) return false. At end return true.",
          "public class Palindrome {\n\n    static boolean isPalindrome(String s) {\n        // Two-pointer approach\n        // Return true if palindrome, false otherwise\n\n    }\n\n    public static void main(String[] args) {\n        System.out.println(isPalindrome(\"racecar\"));\n        System.out.println(isPalindrome(\"hello\"));\n        System.out.println(isPalindrome(\"madam\"));\n    }\n}\n",
          "Three results. True. False. True. The examiner circles something on the pad. \"Correct algorithm. Good choice of approach.\"",
          tests(test("racecar=true","null","true"),test("hello=false","null","false"),test("madam=true","null","true")));

        q("ch7-q3","Fibonacci Sequence","Chapter VII · Quest 3","Loops & Sequences",7,3,200,"Fibonacci.java",
          story(
            n("The examiner's third question. The room is quiet. You are in the zone."),
            d("🧑‍💼","npc","The Examiner","s-npc","Print the first 10 numbers of the Fibonacci sequence. Each number is the sum of the two before it. Starts: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34."),
            d("🧙","mentor","Master Velan","s-mentor","The pattern: track two values — the previous and the current. Each iteration: print current, then compute next as previous + current, then shift: previous becomes current, current becomes next."),
            e("Fibonacci — Iterative Approach",
              "<span class='type'>int</span> prev = <span class='num'>0</span>, curr = <span class='num'>1</span>;\n\nSystem.out.println(prev);  <span class='cm'>// 0</span>\n\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < <span class='num'>9</span>; i++) {\n    System.out.println(curr);\n    <span class='type'>int</span> next = prev + curr;\n    prev = curr;\n    curr = next;\n}\n<span class='cm'>// prints: 0 1 1 2 3 5 8 13 21 34</span>")
          ),
          "Print the first <strong>10 Fibonacci numbers</strong>, one per line:<br><strong>0<br>1<br>1<br>2<br>3<br>5<br>8<br>13<br>21<br>34</strong>",
          "Start with prev=0 and curr=1. Print prev first, then loop 9 times: print curr, compute next = prev+curr, shift prev=curr, curr=next.",
          "// Print the first 10 Fibonacci numbers\n// 0, 1, 1, 2, 3, 5, 8, 13, 21, 34\n\n",
          "Ten numbers appear in sequence, each correctly the sum of the two before it. \"Iterative. Efficient. Correct.\"",
          tests(test("Starts 0","null","0"),test("Has 8","null","8"),test("Ends 34","null","34")));

        q("ch7-q4","Reverse an Array","Chapter VII · Quest 4","Array Manipulation",7,4,200,"ReverseArray.java",
          story(
            n("Question four."),
            d("🧑‍💼","npc","The Examiner","s-npc","Reverse an integer array in-place. No creating a second array. The original array should be reversed when you're done."),
            d("🧙","mentor","Master Velan","s-mentor","In-place reversal uses the two-pointer technique again. Swap the first and last elements, then the second and second-to-last, closing in until the pointers meet. You need a temporary variable to hold one value during the swap."),
            e("In-Place Swap",
              "<span class='type'>int</span>[] arr = {<span class='num'>1</span>,<span class='num'>2</span>,<span class='num'>3</span>,<span class='num'>4</span>,<span class='num'>5</span>};\n<span class='type'>int</span> left=<span class='num'>0</span>, right=arr.length-<span class='num'>1</span>;\n\n<span class='kw'>while</span>(left < right) {\n    <span class='type'>int</span> temp = arr[left];   <span class='cm'>// save left</span>\n    arr[left]  = arr[right]; <span class='cm'>// overwrite left with right</span>\n    arr[right] = temp;       <span class='cm'>// overwrite right with saved</span>\n    left++; right--;\n}\n<span class='cm'>// arr is now: 5,4,3,2,1</span>")
          ),
          "Reverse <code>int[] nums = {1, 2, 3, 4, 5};</code> in-place, then print all values on one line separated by spaces: <strong>5 4 3 2 1</strong>",
          "Use left=0, right=nums.length-1. Swap with a temp variable. After the loop, print all elements with a space between them.",
          "// Reverse the array in-place then print all values separated by spaces\nint[] nums = {1, 2, 3, 4, 5};\n\n",
          "\"5 4 3 2 1.\" The examiner marks it. \"In-place. No extra memory. Clean swap. Exactly right.\"",
          tests(test("Reversed","null","5 4 3 2 1")));

        q("ch7-q5","Count Word Frequency","Chapter VII · Quest 5","Real-World Problem",7,5,250,"WordFrequency.java",
          story(
            n("The final question. The hardest one. The examiner leans forward."),
            d("🧑‍💼","npc","The Examiner","s-npc","Given an array of words, count how many times each unique word appears. Print each word and its count. This is a real problem you will solve your first week on the job."),
            d("🧙","mentor","Master Velan","s-mentor","We use a <em>HashMap</em> — a data structure that maps keys to values. Each word is a key; its count is the value. For each word in the array, check if it's already in the map. If yes, increment the count. If no, add it with count 1."),
            e("HashMap for Frequency Count",
              "<span class='kw'>import</span> java.util.HashMap;\n\n<span class='type'>HashMap</span>&lt;<span class='type'>String</span>, <span class='type'>Integer</span>&gt; freq = <span class='kw'>new</span> <span class='type'>HashMap</span>&lt;&gt;();\n<span class='type'>String</span>[] words = {<span class='str'>\"cat\"</span>, <span class='str'>\"dog\"</span>, <span class='str'>\"cat\"</span>};\n\n<span class='kw'>for</span> (<span class='type'>int</span> i=<span class='num'>0</span>; i < words.length; i++) {\n    <span class='type'>String</span> w = words[i];\n    <span class='kw'>if</span> (freq.containsKey(w)) {\n        freq.put(w, freq.get(w) + <span class='num'>1</span>);\n    } <span class='kw'>else</span> {\n        freq.put(w, <span class='num'>1</span>);\n    }\n}\n\nSystem.out.println(freq.get(<span class='str'>\"cat\"</span>));  <span class='cm'>// 2</span>"),
            d("🧙","mentor","Master Velan","s-mentor","HashMap provides O(1) average-time lookup — finding any key takes the same time regardless of how many entries there are. This is why it's used constantly in real applications. You'll encounter it every week as a professional developer.")
          ),
          "Given <code>String[] words = {\"apple\",\"banana\",\"apple\",\"cherry\",\"banana\",\"apple\"};</code><br><br>Use a <code>HashMap</code> to count frequency. Print:<br><strong>apple: 3<br>banana: 2<br>cherry: 1</strong><br>(any order is fine — maps don't guarantee order)",
          "Import HashMap. Loop through words. Use containsKey to check if word exists. put(word, get(word)+1) if yes, put(word, 1) if no. Then print each entry.",
          "import java.util.HashMap;\n\npublic class WordFrequency {\n    public static void main(String[] args) {\n        String[] words = {\"apple\",\"banana\",\"apple\",\"cherry\",\"banana\",\"apple\"};\n\n        // Build frequency map\n\n\n        // Print each word and its count\n\n    }\n}\n",
          "Three entries appear. Apple: 3. Banana: 2. Cherry: 1. The examiner sets down the pen. \"HashMap. Correct approach. Linear time. You know your data structures.\" A long pause. \"We'll be in touch.\"",
          tests(test("apple: 3","null","apple: 3"),test("banana: 2","null","banana: 2"),test("cherry: 1","null","cherry: 1")));

        q("ch7-q6","The Complexity Oracle","Chapter VII · Quest 6","Algorithm Complexity",7,6,250,"ComplexityOracle.java",
          story(
            n("The final question. The examiner sets down the pen for a moment."),
            d("🧑‍💼","npc","The Examiner","s-npc","Before you go — Big O. I need to know you understand why algorithms have costs. This isn't about memorising formulas. It's about being able to look at code and reason about how it scales."),
            d("🧙","mentor","Master Velan","s-mentor","<em>Big O notation</em> describes how an algorithm's runtime grows as the input size (n) grows. Ignore constants and small terms — only the dominant term matters. The most common complexities you'll encounter every day are O(1), O(n), O(n²), and O(log n)."),
            e("The Four You Must Know",
              "<span class='cm'>// O(1) — constant time. Same speed regardless of n.</span>\n<span class='type'>int</span> first = arr[<span class='num'>0</span>]; <span class='cm'>// always one operation</span>\nmap.get(key);          <span class='cm'>// HashMap lookup</span>\n\n<span class='cm'>// O(n) — linear. One pass through n elements.</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i=<span class='num'>0</span>; i&lt;n; i++) { ... }\n\n<span class='cm'>// O(n²) — quadratic. Nested loops over n.</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i=<span class='num'>0</span>; i&lt;n; i++)\n    <span class='kw'>for</span> (<span class='type'>int</span> j=<span class='num'>0</span>; j&lt;n; j++) { ... }\n\n<span class='cm'>// O(log n) — logarithmic. Halves problem each step.</span>\n<span class='cm'>// e.g. binary search on a sorted array</span>"),
            d("🧑‍💼","npc","The Examiner","s-npc","The point is: if you have 10,000 elements, an O(n²) algorithm does 100 million operations. An O(n) algorithm does 10,000. An O(1) algorithm does 1. Choosing the right data structure and algorithm is how senior developers write code that scales."),
            e("Why it matters — concrete numbers",
              "<span class='cm'>// n = 1,000 elements:</span>\n<span class='cm'>// O(1):    1 operation</span>\n<span class='cm'>// O(n):    1,000 operations</span>\n<span class='cm'>// O(n²): 1,000,000 operations</span>\n\n<span class='cm'>// n = 1,000,000 elements:</span>\n<span class='cm'>// O(1):    1 operation</span>\n<span class='cm'>// O(n):    1,000,000 operations</span>\n<span class='cm'>// O(n²): 1,000,000,000,000 operations — unusable</span>")
          ),
          "Print the complexity of four operations (one per line):<br>1. <strong>O(1)</strong> — accessing an array element by index<br>2. <strong>O(n)</strong> — iterating every element once<br>3. <strong>O(n^2)</strong> — two nested loops over n elements<br>4. <strong>O(log n)</strong> — binary search on a sorted array<br><br>Just print those four strings exactly.",
          "This is a conceptual exercise — just print the four Big O strings. The goal is to memorise the classifications and associate them with the patterns.",
          "// Print the Big O complexity of each operation type\n// Just print the four complexity strings\n\n",
          "The examiner closes the notepad. \"You know your complexities. That's what separates developers who can reason about performance from those who can't.\" He stands. \"We'll call you.\"",
          tests(test("O(1)","null","O(1)"),test("O(n)","null","O(n)"),test("O(n^2)","null","O(n^2)"),test("O(log n)","null","O(log n)")));

        // ── Side quests ───────────────────────────────────────────────────────

        sq("ch7-sq1","The Space Oracle","Chapter VII · Side Quest","Space Complexity",7,90,80,"SpaceOracle.java",
          story(
            n("Back in the Interview Gauntlet. The examiner flips to a new page. \"Time complexity is half the story. What about <em>space complexity</em>?\""),
            d("🧑‍💼","npc","The Examiner","s-npc","<em>Space complexity</em> measures how much extra memory an algorithm uses as input size grows. Like time complexity, we use Big O notation — but now we are counting bytes, not operations."),
            e("Space Complexity Examples","O(1) — constant space: same memory regardless of input size.\\n      Swapping two variables with one temp variable.\\n\\nO(n) — linear space: memory grows with input.\\n      Copying an array, building a new list.\\n\\nO(n²) — quadratic space: a 2D grid/matrix sized n×n."),
            d("🧑‍💼","npc","The Examiner","s-npc","Every interview answer about an algorithm should mention <em>both</em> time and space complexity. Sometimes there is a trade-off: a faster algorithm needs more memory, or a memory-efficient one is slower. Recognising that trade-off shows senior-level thinking."),
            d("🧙","mentor","Master Velan","s-mentor","In Java, remember that creating a new array or ArrayList is O(n) space — you are allocating n new slots. Sorting with Java's built-in sort uses O(log n) extra space for the recursive call stack. Knowing what the standard library does under the hood makes you stand out.")
          ),
          "Demonstrate space complexity concepts:<br>1. Reverse a string <em>in-place</em> (O(1) extra space): use <code>new StringBuilder(word).reverse().toString()</code> on <code>\"arcane\"</code> — print reversed, then original (unchanged)<br>2. Build a doubled array (O(n) space) from <code>int[] scores = {90, 70, 80}</code> — multiply each by 2 and print as <strong>180,140,160</strong>",
          "StringBuilder reverse is O(1) extra space (reuses chars). New int[scores.length] is O(n) space. Print: reversed string, original string, then comma-joined doubled values.",
          "String word = \"arcane\";\n// 1. Reverse using StringBuilder (O(1) space)\n\n// Original is unchanged — print it too\n\nint[] scores = {90, 70, 80};\n// 2. Create doubled array (O(n) space), print as 180,140,160\n\n",
          "\"Reversed in-place. Doubled with extra memory. Both correct, different costs.\" The examiner notes: <em>understands space trade-offs</em>.",
          tests(test("reversed","null","enacra"),test("original","null","arcane"),test("doubled","null","180,140,160")));
    }
}
