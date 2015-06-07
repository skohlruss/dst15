package dst.ass3.aop.impl;

import dst.ass3.aop.IPluginExecutable;
import dst.ass3.aop.IPluginExecutor;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by pavol on 7.6.2015.
 */
public class PluginExecutor implements IPluginExecutor {

    private static final String JAR_FILE_EXTENSION = ".jar";
    private static final String CLASS_FILE_EXTENSION = ".class";

    private static final Long SCAN_PERIOD_TIME = 500l;


    private Map<File, Map<File, Long>> watchedDirs = new ConcurrentHashMap<>();

    Future<?> watcherFuture;
    private final ScheduledExecutorService watcherScheduler = Executors.newScheduledThreadPool(1);
    ExecutorService pluginsExecutorService = Executors.newCachedThreadPool();


    @Override
    public void monitor(File dir) {
        watchedDirs.put(dir, new ConcurrentHashMap<File, Long>());
    }

    @Override
    public void stopMonitoring(File dir) {
        watchedDirs.remove(dir);
    }

    @Override
    public void start() {
        if (watcherFuture == null) {
            watcherFuture = watcherScheduler.scheduleAtFixedRate(
                    new WatcherDirectories(),
                    0, SCAN_PERIOD_TIME, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void stop() {
        if (watcherFuture !=  null) {
            watcherFuture.cancel(true);
        }

    }

    /**
     * watch changes in directories
     *
     * First implemented as WatcherService, but there were problem on windows (locked files)
     */
    private class WatcherDirectories implements Runnable {

        @Override
        public void run() {
            for (Map.Entry<File, Map<File, Long>> entry : watchedDirs.entrySet()) {
                File watchedDir = entry.getKey();
                Map<File, Long> watchedFiles = entry.getValue();

                for (File fileOfWatchedDir: watchedDir.listFiles(new FileNameFilter(JAR_FILE_EXTENSION))) {
                    Long lastModified = fileOfWatchedDir.lastModified();
                    Long previousLastModified = watchedFiles.get(fileOfWatchedDir);

                    if (previousLastModified == null || lastModified > previousLastModified) {
                        if (!(fileOfWatchedDir.canWrite() || fileOfWatchedDir.canRead())) {
                            continue;
                        }
                        try {
                            System.out.println("Loading .jar:" + fileOfWatchedDir.toPath());
                            loadJar(fileOfWatchedDir);
                        } catch (IOException e) {
                            System.out.println("couldn't load classes from:" + fileOfWatchedDir.toPath());
                            e.printStackTrace();
                        }
                    }

                    watchedFiles.put(fileOfWatchedDir, lastModified);
                }
            }

        }
    }

    private void loadJar(File jar) throws IOException {
        try(JarFile jarFile = new JarFile(jar)) {
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

                    pluginsExecutorService.submit(runnable);
                }
            }

        } catch (IOException |
                ClassNotFoundException ex) {
            System.out.println("Couldn't load jar file");
            ex.printStackTrace();
        }
    }

    public static class FileNameFilter implements FilenameFilter {

        private String extension;

        public FileNameFilter(String extension) {
            this.extension = extension;
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(extension);
        }
    }
}
