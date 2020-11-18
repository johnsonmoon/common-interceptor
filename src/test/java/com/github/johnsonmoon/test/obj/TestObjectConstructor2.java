package com.github.johnsonmoon.test.obj;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Create by xuyh at 2020/2/17 16:56.
 */
public class TestObjectConstructor2 {
    private AtomicLong flag;
    private String abc;

    public TestObjectConstructor2(AtomicLong flag, String abc) {
        this.flag = flag;
        this.abc = abc;
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

    public void setFlag(AtomicLong flag) {
        this.flag = flag;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }
}
