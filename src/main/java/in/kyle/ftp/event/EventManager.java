package in.kyle.ftp.event;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kyle on 9/5/2015.
 */
public class EventManager {
    
    private List<HoldListener> listeners;
    
    public EventManager() {
        this.listeners = new ArrayList();
    }
    
    public void register(Object o) {
        for (Method m : o.getClass().getDeclaredMethods()) {
            if (m.getParameterCount() == 1) {
                Class type = m.getParameterTypes()[0];
                if (ProgramEvent.class.isAssignableFrom(type)) {
                    HoldListener holdListener = new HoldListener(o, m, type);
                    listeners.add(holdListener);
                }
            }
        }
    }
    
    public void unregister(Object o) {
        Iterator<HoldListener> listenerIterator = listeners.iterator();
        while (listenerIterator.hasNext()) {
            HoldListener listener = listenerIterator.next();
            if (o.equals(listener.getObject())) {
                listenerIterator.remove();
            }
        }
    }
    
    public void fire(ProgramEvent event) {
        listeners.stream().filter(l->l.getEvent().equals(event.getClass())).forEach(holdListener -> {
            try {
                holdListener.getMethod().invoke(holdListener.getObject(), event);
            } catch (Exception e) {
                new Error(e);
                e.printStackTrace();
            }
        });    
    }
    
    @Data
    private static class HoldListener {
        private final Object object;
        private final Method method;
        private final Class event;
    }
}
