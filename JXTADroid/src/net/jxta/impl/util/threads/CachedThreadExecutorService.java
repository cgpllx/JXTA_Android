package net.jxta.impl.util.threads;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * Wrapping class offering a cached thread pool executor service - guards against termination
 * through the standard shutdown() and shutdownNow() methods, to allow for distribution to
 * untrusted code (typically from a refactor of timer usage or code which created it's own threads).
 * Additionally, will record some simple metrics on tasks submitted to the executor to report
 * on tasks which take an excessive amount of time to complete.
 */
public class CachedThreadExecutorService implements ExecutorService {

    static final Logger LOG = Logger.getLogger(CachedThreadExecutorService.class.getName());

    private final ExecutorService cachedExecutorService;

    public CachedThreadExecutorService() {
        this(null);
    }

    public CachedThreadExecutorService(ThreadFactory inTF) {

        // Initialization
        if (inTF!=null) {
            cachedExecutorService = Executors.newCachedThreadPool();
        } else {
            cachedExecutorService = Executors.newCachedThreadPool(inTF);
        }

    }

    @Override
	public void shutdown() {
        throw new IllegalStateException("shutdown cannot be called on a shared thread pool executor");
    }
	
    public void shutdownShared() {
        if (cachedExecutorService != null) {
            cachedExecutorService.shutdown();
        }
    }
	
    @Override
	public List<Runnable> shutdownNow() {
        throw new IllegalStateException("shutdown cannot be called on a shared thread pool executor");
    }

    public void shutdownNowShared() {
        if (cachedExecutorService != null) {
            cachedExecutorService.shutdownNow();
        }
    }

    @Override
	public void execute(Runnable command) {
        cachedExecutorService.execute(command);
    }

    @Override
	public <T> Future<T> submit(Callable<T> task) {
        return cachedExecutorService.submit(task);
    }

    @Override
	public Future<?> submit(Runnable task) {
        return cachedExecutorService.submit(task);
    }

    @Override
	public <T extends Object> java.util.concurrent.Future<T> submit(Runnable task, T result) {
        return cachedExecutorService.submit(task, result);
    }

    @Override
	public boolean isShutdown() {
        return cachedExecutorService.isShutdown();
    }

    @Override
	public boolean isTerminated() {
        return cachedExecutorService.isTerminated();
    }

    @Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return cachedExecutorService.awaitTermination(timeout, unit);
    }

    @Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return cachedExecutorService.invokeAll(tasks);
    }

    @Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return cachedExecutorService.invokeAll(tasks, timeout, unit);
    }

    @Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return cachedExecutorService.invokeAny(tasks);
    }

    @Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return cachedExecutorService.invokeAny(tasks, timeout, unit);
    }

}