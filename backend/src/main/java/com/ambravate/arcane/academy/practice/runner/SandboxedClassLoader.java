package com.ambravate.arcane.academy.practice.runner;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

/**
 * A ClassLoader that isolates student code from the host application and
 * blocks specific dangerous API packages.
 *
 * <h3>Isolation</h3>
 * Parent is set to {@code null} (the bootstrap classloader). This means student
 * code can only see {@code java.*} / {@code javax.*} classes — it cannot reach
 * Spring beans, JPA repositories, database connections, or any application class.
 *
 * <h3>API blocking</h3>
 * Several standard-library entry-points are blocked at load time:
 * <ul>
 *   <li>File I/O ({@code java.io.File}, {@code FileInputStream}, etc.)</li>
 *   <li>Network ({@code java.net.*})</li>
 *   <li>NIO filesystem ({@code java.nio.file.*})</li>
 *   <li>Reflection ({@code java.lang.reflect.*}) — prevents bypassing blocked classes</li>
 *   <li>Process spawning ({@code Runtime}, {@code ProcessBuilder})</li>
 * </ul>
 *
 * <h3>Known limitations</h3>
 * {@code System.exit()} cannot be blocked via a ClassLoader in Java 21 (SecurityManager
 * was removed). This is mitigated by a pre-compile source scan in {@link JavaCodeRunner}.
 * Tight infinite loops ({@code while(true) {}}) cannot be interrupted via
 * {@code Thread.interrupt()} — the 5-second executor timeout is the backstop.
 */
public class SandboxedClassLoader extends URLClassLoader {

    private static final Set<String> BLOCKED_EXACT = Set.of(
            "java.io.File",
            "java.io.FileDescriptor",
            "java.io.FileInputStream",
            "java.io.FileOutputStream",
            "java.io.FileReader",
            "java.io.FileWriter",
            "java.io.RandomAccessFile",
            "java.lang.Runtime",
            "java.lang.ProcessBuilder"
    );

    private static final Set<String> BLOCKED_PREFIX = Set.of(
            "java.net.",
            "java.nio.file.",
            "java.lang.reflect.",
            "com.ambravate.",
            "org.springframework."
    );

    public SandboxedClassLoader(URL[] classPath) {
        // null parent = bootstrap classloader only; application classpath is invisible.
        super(classPath, null);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        assertNotBlocked(name);
        return super.loadClass(name, resolve);
    }

    private void assertNotBlocked(String name) {
        if (BLOCKED_EXACT.contains(name)) {
            throw new SecurityException("'" + name + "' is not available in the sandbox.");
        }
        for (String prefix : BLOCKED_PREFIX) {
            if (name.startsWith(prefix)) {
                throw new SecurityException("'" + name + "' is not available in the sandbox.");
            }
        }
    }
}
