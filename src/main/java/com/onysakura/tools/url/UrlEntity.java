package com.onysakura.tools.url;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UrlEntity {

    @Id
    @Column(length = 10, columnDefinition = "varchar(10) COMMENT '编码'")
    private String code;

    @Column(length = 1024, columnDefinition = "varchar(1024) COMMENT '原始地址'")
    private String url;

    public UrlEntity() {
    }

    public UrlEntity(String code, String url) {
        this.code = code;
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
