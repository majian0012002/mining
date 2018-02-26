package com.miller.mining.model;

public class Dictionary extends BaseModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1588944617875054086L;

	private Integer id;

    private String dicKey;

    private String dicValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDicKey() {
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey == null ? null : dicKey.trim();
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue == null ? null : dicValue.trim();
    }
}