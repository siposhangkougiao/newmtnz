package com.mtnz.service.system.community;


import com.github.pagehelper.PageInfo;
import com.mtnz.controller.app.community.model.Community;
import com.mtnz.controller.app.community.model.CommunityComments;
import com.mtnz.controller.app.community.model.CommunityReport;
import com.mtnz.entity.Page;

import java.util.List;
import java.util.Map;

public interface CommunityService {

    /**
     * 查询生意圈列表
     * @param community
     * @return
     */
    PageInfo select(Community community) throws Exception;

    /**
     * 发布动态
     * @param community
     */
    void insert(Community community);

    /**
     * 修改状态（点赞，删除，修改）
     * @param community
     */
    void update(Community community);

    /**
     * 添加评论
     * @param communityComments
     */
    void insertcomments(CommunityComments communityComments);

    /**
     * 修改评论（点赞，修改，删除）
     * @param communityComments
     */
    void updatecomments(CommunityComments communityComments);

    /**
     * 七牛获取上传用的token
     * @return
     */
    String getoken();

    /**
     * 查询详情
     * @param id
     * @return
     */
    Community selectdetail(Community id);

    /**
     * 投诉添加
     * @param communityReport
     */
    void insertReport(CommunityReport communityReport);

    List<Map<String,Object>> getCommunityReportList();

    public void updateStatus(CommunityReport communityReport);

}
