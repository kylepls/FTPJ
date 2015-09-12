package in.kyle.ftp.random;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Kyle on 9/12/2015.
 */
public class FileWatcher {
    
    private static WatchService watchService;
    
    private File directory;
    private Thread watcherThread;
    private AtomicBoolean running;
    private DirectoryEvent directoryEvent;
    
    public FileWatcher(DirectoryEvent directoryEvent, File directory) throws IOException {
        this.running = new AtomicBoolean(true);
        this.directoryEvent = directoryEvent;
        
        if (watchService == null) {
            watchService = FileSystems.getDefault().newWatchService();
        }
        
        this.directory = directory;
        
        startWatcher();
    }
    
    public void stop() {
        running.set(false);
        watcherThread.stop();
    }
    
    public void setDirectory(File file) throws IOException {
        this.directory = file;
        watcherThread.stop();
        startWatcher();
    }
    
    private void startWatcher() throws IOException {
        directory.toPath().register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
    
        if (watcherThread == null) {
            watcherThread = new Thread(() -> {
                while (running.get()) {
                    WatchKey key;
                    try {
                        key = watchService.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        continue;
                    }
                
                    key.pollEvents().forEach(watchEvent -> {
                        WatchEvent.Kind<?> kind = watchEvent.kind();
                    
                        if (kind != StandardWatchEventKinds.OVERFLOW) {
                            directoryEvent.run();
                        }
                    });
                
                    key.reset();
                }
            });
            //watcherThread.start();
        }
    }
    
    public interface DirectoryEvent {
        void run();
    }
}
