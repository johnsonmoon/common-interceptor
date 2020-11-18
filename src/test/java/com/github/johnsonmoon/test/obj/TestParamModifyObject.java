package com.github.johnsonmoon.test.obj;

/**
 * Create by xuyh at 2020/2/17 23:40.
 */
public class TestParamModifyObject {
    private String abc;
    private String def;

    public TestParamModifyObject(String abc, String def) {
        this.abc = abc;
        this.def = def;
    }

    public void modifyField(String abc, String def) {
        this.abc = abc;
        this.def = def;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    @Override
    public String toString() {
        return "TestParamModifyObject{" +
                "abc='" + abc + '\'' +
                ", def='" + def + '\'' +
                '}';
    }
}
