package com.mtnz.controller.app.community.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "community_report")
public class CommunityReport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "community_id")
  private Long communityId;

  @Column(name = "comments_id")
  private Long commentsId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "content")
  private String content;

  @Column(name = "creat_time")
  private Date creatTime;

  @Column(name = "type")
  private Integer type;

  @Column(name = "status")
  private Integer status;

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Long getId() {
    return id;
  }

  public Long getCommentsId() {
    return commentsId;
  }

  public void setCommentsId(Long commentsId) {
    this.commentsId = commentsId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCommunityId() {
    return communityId;
  }

  public void setCommunityId(Long communityId) {
    this.communityId = communityId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreatTime() {
    return creatTime;
  }

  public void setCreatTime(Date creatTime) {
    this.creatTime = creatTime;
  }
}
