package com.peas.xinrui.api.file.entity;

public class UploadOptions {
    private String contentType;
    private AccessPermission permission;
    private String namespace;
    private int randomLength;
    private String fileName;
    private boolean privat;

    public String getContentType() {
        return contentType;
    }

    public UploadOptions setContentType(final String contentType) {
        this.contentType = contentType;
        return this;
    }

    public AccessPermission getPermission() {
        return permission;
    }

    public UploadOptions setPermission(final AccessPermission permission) {
        this.permission = permission;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public UploadOptions setNamespace(final String namespace) {
        this.namespace = namespace;
        return this;
    }

    public int getRandomLength() {
        return randomLength;
    }

    public UploadOptions setRandomLength(final int randomLength) {
        this.randomLength = randomLength;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public UploadOptions setFileName(final String fileName) {
        this.fileName = fileName;
        return this;
    }

    public enum AccessPermission {
        DEFAULT, PRIVATE, PUBLIC_READ
    }

    public boolean isPrivat() {
        return privat;
    }

    public UploadOptions setPrivat(final boolean privat) {
        this.privat = privat;
        return this;
    }

}
