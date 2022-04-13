package com.peas.xinrui.api.sms.model;

public class ValCode {
    private Long key;
    private String code;
    private Byte sessionType;
    private Byte accountType;
    private String account;

    public ValCode() {

    }

    public ValCode(String code) {
        this.code = code;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Byte getSessionType() {
        return sessionType;
    }

    public void setSessionType(Byte sessionType) {
        this.sessionType = sessionType;
    }

    public Byte getAccountType() {
        return accountType;
    }

    public void setAccountType(Byte accountType) {
        this.accountType = accountType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

}