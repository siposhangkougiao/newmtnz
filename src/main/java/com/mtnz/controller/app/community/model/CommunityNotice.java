package com.mtnz.controller.app.community.model;

import com.google.inject.internal.util.$Strings;

import javax.persistence.*;
import java.util.Date;

@Table(name = "community_notice")
public class CommunityNotice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "createTime")
  private Date createTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
