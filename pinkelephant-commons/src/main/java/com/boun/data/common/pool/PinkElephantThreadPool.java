package com.boun.data.common.pool;

public enum PinkElephantThreadPool {

    EMAIL_POOL(10, 10);
    
    private ThreadPoolImpl instance;
    
    private PinkElephantThreadPool(int corePoolSize, int maxPoolSize) {
        this.instance = new ThreadPoolImpl(corePoolSize, maxPoolSize);
    }
    
    public void runTask(Runnable task) {
        instance.runTask(task);
    }
    
    public void shutdown() {
        instance.shutDown();
    }
}
