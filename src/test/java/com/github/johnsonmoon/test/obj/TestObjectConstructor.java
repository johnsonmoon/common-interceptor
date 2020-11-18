package com.github.johnsonmoon.test.obj;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Create by xuyh at 2020/2/17 16:56.
 */
public class TestObjectConstructor {
    private AtomicLong flag;

    public TestObjectConstructor(AtomicLong flag) {
        this.flag = flag;
    }

    public Long getFlag() {
        return flag == null ? null : flag.get();
    }

    public void setFlag(Long flag) {
        if (this.flag == null) {
            this.flag = new AtomicLong();
        }
        this.flag.set(flag);
    }

    public void increment() {
        if (this.flag == null) {
            this.flag = new AtomicLong();
        }
        this.flag.incrementAndGet();
    }

    public void decrement() {
        if (this.flag == null) {
            this.flag = new AtomicLong();
        }
        this.flag.decrementAndGet();
    }
}
