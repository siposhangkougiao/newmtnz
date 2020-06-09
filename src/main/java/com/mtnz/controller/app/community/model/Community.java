package com.mtnz.controller.app.community.model;


import com.github.pagehelper.PageInfo;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@Table(name = "community")
public class Community {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "content")
  private String content;

  @Column(name = "imges")
  private String imges;

  @Column(name = "praise")
  private Integer praise;

  @Column(name = "collection")
  private Integer collection;

  @Column(name = "creat_time")
  private Date creatTime;

  @Column(name = "is_delete")
  private Integer isDelete;

  @Column(name = "talk_type")
  private Integer talkType;

  @Column(name = "prefect")
  private Integer prefect;

  @Column(name = "title")
  private String title;


  /**
   * 参与人
   */
  @Transient
  private Long makerId;

  /**
   * 是否审核
   */
  @Transient
  private Integer isPass;

  /**
   * 是否评论
   */
  @Transient
  private Integer istalk=0;

  /**
   * 是否收藏
   */
  @Transient
  private Integer isget=0;

  /**
   * 是否点赞
   */
  @Transient
  private Integer ispraise=0;

  @Transient
  private Integer pageNumber=1;

  @Transient
  private Integer pageSize=15;

  /**
   * 发布人名称
   */
  @Transient
  private String releaseName;

  @Transient
  private String nickName;

  @Transient
  private String header;

  @Transient
  private String signature;

  @Transient
  private Integer type;

  @Transient
  private List<CommunityComments> commentsList = new ArrayList<>();

  @Transient
  private PageInfo<CommunityComments> pageInfo =new PageInfo<>();


  @Transient
  private String viewTimeOne;

  @Transient
  private String viewTimeTwo;

  public String getViewTimeOne() {
    return viewTimeOne;
  }

  public void setViewTimeOne(String viewTimeOne) {
    this.viewTimeOne = viewTimeOne;
  }

  public String getViewTimeTwo() {
    return viewTimeTwo;
  }

  public void setViewTimeTwo(String viewTimeTwo) {
    this.viewTimeTwo = viewTimeTwo;
  }

  public Integer getPrefect() {
    return prefect;
  }

  public void setPrefect(Integer prefect) {
    this.prefect = prefect;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public PageInfo<CommunityComments> getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(PageInfo<CommunityComments> pageInfo) {
    this.pageInfo = pageInfo;
  }

  public Integer getCollection() {
    return collection;
  }

  public void setCollection(Integer collection) {
    this.collection = collection;
  }

  public Long getMakerId() {
    return makerId;
  }

  public void setMakerId(Long makerId) {
    this.makerId = makerId;
  }

  public Integer getIstalk() {
    return istalk;
  }

  public void setIstalk(Integer istalk) {
    this.istalk = istalk;
  }

  public Integer getIsget() {
    return isget;
  }

  public void setIsget(Integer isget) {
    this.isget = isget;
  }

  public Integer getIspraise() {
    return ispraise;
  }

  public void setIspraise(Integer ispraise) {
    this.ispraise = ispraise;
  }

  public Integer getTalkType() {
    return talkType;
  }

  public void setTalkType(Integer talkType) {
    this.talkType = talkType;
  }



  public String getReleaseName() {
    return releaseName;
  }

  public void setReleaseName(String releaseName) {
    this.releaseName = releaseName;
  }

  public List<CommunityComments> getCommentsList() {
    return commentsList;
  }

  public void setCommentsList(List<CommunityComments> commentsList) {
    this.commentsList = commentsList;
  }

  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getImges() {
    return imges;
  }

  public void setImges(String imges) {
    this.imges = imges;
  }

  public Integer getPraise() {
    return praise;
  }

  public void setPraise(Integer praise) {
    this.praise = praise;
  }

  public Date getCreatTime() {
    return creatTime;
  }

  public void setCreatTime(Date creatTime) {
    this.creatTime = creatTime;
  }

  public Integer getIsDelete() {
    return isDelete;
  }

  public void setIsDelete(Integer isDelete) {
    this.isDelete = isDelete;
  }
}
