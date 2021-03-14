package com.example.demo.vo;

import java.util.Date;

public class LessonVo implements java.io.Serializable{
    private Long id;
    private String name;
    private Date createdate;
    private Long subcount;

    private Long status;

    public LessonVo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getSubcount() {
        return subcount;
    }

    public void setSubcount(Long subcount) {
        this.subcount = subcount;
    }
}
