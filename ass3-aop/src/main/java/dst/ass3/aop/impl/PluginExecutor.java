package dst.ass3.aop.impl;

import dst.ass3.aop.IPluginExecutable;
import dst.ass3.aop.IPluginExecutor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by pavol on 3.6.2015.
 */
public class PluginExecutor implements IPluginExecutor {

    private static final String JAR_FILE_EXTENSION = ".jar";
    private static final String CLASS_FILE_EXTENSION = ".class";

    private WatchService watcher;
    private Map<File, WatchKey> watchedDirs = new ConcurrentHashMap<>();

    ExecutorService executor = Executors.newCachedThreadPool();
    Future<?> executorFuture;

    public PluginExecutor() {
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException ex) {
            System.out.println("Unable to create filesystem watcher");
            ex.printStackTrace();
        }
    }

    @Override
    public void monitor(File dir) {
        WatchKey watchKey = null;
        try {
            watchKey = dir.toPath().register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            watchedDirs.put(dir, watchKey);
            System.out.println("starting to watch " + dir.toPath().toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to watch file " + dir.toPath().toAbsolutePath());
        }
    }

    @Override
    public void stopMonitoring(File dir) {
        WatchKey watchKey = watchedDirs.remove(dir);
        if (watchKey != null) {
            watchKey.cancel();
        }
    }

    @Override
    public void start() {
        if (executorFuture == null) {
            executorFuture = executor.submit(new Watcher());
        }
    }

    @Override
    public void stop() {
        if (executorFuture != null) {
//            try {
//                watcher.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            executorFuture.n;
            executorFuture.cancel(true);
            executor.shutdown();
        }
    }

    private void loadJar(File jar) {
        try {
            JarFile jarFile = new JarFile(jar);
            for (JarEntry jarEntry: Collections.list(jarFile.entries())) {
                if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(CLASS_FILE_EXTENSION)) {
                    continue;
                }

                URL[] urls = new URL[]{jar.toURI().toURL()};
                URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls);

                String className = jarEntry.getName().replace(CLASS_FILE_EXTENSION, "").replace("/", ".");
                System.out.println("jar entry, class " + className);

                Class clazz = urlClassLoader.loadClass(className);
                if (IPluginExecutable.class.isAssignableFrom(clazz)) {
                    final Class<? extends IPluginExecutable> newClass = (Class<? extends IPluginExecutable>)urlClassLoader.loadClass(className);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                newClass.newInstance().execute();
                            } catch (InstantiationException |
                                    IllegalAccessException ex) {
                                System.out.println("Could not instantiate class " + newClass.getName());
                                ex.printStackTrace();
                            }
                        }
                    };

                    executor.submit(runnable);
                }
            }

//            jarFile.close();
        } catch (IOException |
                ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private class Watcher implements Runnable {

        @SuppressWarnings("unchecked")
        private <T> WatchEvent<T> cast(WatchEvent<?> event) {
            return (WatchEvent<T>)event;
        }

        @Override
        public void run() {

            while (true) {
                WatchKey watchKey = null;
                try {
                    watchKey = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }

                boolean plugins_executed = false;
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == OVERFLOW) {
                        continue;
                    }

                    WatchEvent<Path> ev = cast(event);
                    Path fileModified = ev.context();

                    if (!fileModified.toString().endsWith(JAR_FILE_EXTENSION)) {
                        continue;
                    }
//                    plugins_executed = true;

                    Path directory = (Path)watchKey.watchable();
                    Path path = directory.resolve(fileModified);

                    System.out.println("Loading file " + path.toString());
                    loadJar(path.toFile());
                }

                watchKey.reset();
            }
        }
    }
}
