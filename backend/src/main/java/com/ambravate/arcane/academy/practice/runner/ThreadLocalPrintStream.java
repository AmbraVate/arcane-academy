package com.ambravate.arcane.academy.practice.runner;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A PrintStream that routes each thread's writes to a per-thread target.
 *
 * <p>Installed once as {@code System.out} and {@code System.err} at application
 * startup by {@link JavaCodeRunner}. Before running student code on a thread,
 * call {@link #capture(PrintStream)}; call {@link #release()} in a finally block
 * to restore the fallback.
 *
 * <p>Uses {@link InheritableThreadLocal} so any threads spawned <em>inside</em>
 * student code inherit the same capture target instead of writing to the server log.
 */
public class ThreadLocalPrintStream extends PrintStream {

    private final InheritableThreadLocal<PrintStream> perThread = new InheritableThreadLocal<>();
    private final PrintStream fallback;

    public ThreadLocalPrintStream(PrintStream fallback) {
        super(OutputStream.nullOutputStream(), true);
        this.fallback = fallback;
    }

    /** Direct this thread (and any threads it spawns) to {@code target}. */
    public void capture(PrintStream target) {
        perThread.set(target);
    }

    /** Remove the per-thread target; subsequent writes fall through to {@link #fallback}. */
    public void release() {
        perThread.remove();
    }

    private PrintStream current() {
        PrintStream ps = perThread.get();
        return ps != null ? ps : fallback;
    }

    // ── Core write methods — everything else in PrintStream delegates to these ──

    @Override
    public void write(int b) {
        current().write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        current().write(b, off, len);
    }

    @Override
    public void flush() {
        current().flush();
    }

    @Override
    public boolean checkError() {
        return current().checkError();
    }

    @Override
    public void close() {
        // Never close — this stream lives for the lifetime of the JVM.
    }
}
