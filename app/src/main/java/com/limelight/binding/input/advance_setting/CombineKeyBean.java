package com.limelight.binding.input.advance_setting;

public class CombineKeyBean {
    private String Id;
    private String name;
    private short[] keyValue;
    private String[] keyName;
    private Long createTime;

    public CombineKeyBean(String id, String name, short[] keyValue, String[] keyName, Long createTime) {
        Id = id;
        this.name = name;
        this.keyValue = keyValue;
        this.keyName = keyName;
        this.createTime = createTime;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short[] getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(short[] keyValue) {
        this.keyValue = keyValue;
    }

    public String[] getKeyName() {
        return keyName;
    }

    public void setKeyName(String[] keyName) {
        this.keyName = keyName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
