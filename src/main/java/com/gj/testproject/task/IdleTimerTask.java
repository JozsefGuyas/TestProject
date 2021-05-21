
package com.gj.testproject.task;

import java.util.Timer;
import java.util.TimerTask;


public class IdleTimerTask {
    
    private Timer timer;
    
    private int timeout = 0;
    
    private IdleTimerListener idleTimerListener;
    
    private boolean isNotRunning(){
        return timer == null;
    }
    
    private boolean isRunning() {
        return timer != null;
    }
    
    private void initTimer(int timeout) {
        if (isNotRunning()) {
            timer = new Timer("IdleTimerTask");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                     fireOnIdleTimeout();
                }
            }, timeout * 1000);
        }
    }
    
    public synchronized void start(int timeout) {
        this.timeout = timeout;
        this.initTimer(timeout);
    }
    
    public void stop() {
        
        if (isRunning()) {
            
            timer.cancel();
            timer.purge();
            timer = null;
            
        }
        
    }
    
    public void reset() {
        
        if (isRunning()) {
            
            stop();
            initTimer(timeout);            
        }
        
    }
    
    public void setOnIdleTimerListener(IdleTimerListener idleTimerListener) {
        this.idleTimerListener = idleTimerListener;
    }
    
    private void fireOnIdleTimeout() {
        if (this.idleTimerListener != null) {
            this.idleTimerListener.onIdleTimeout();
        }
    }
    
}
