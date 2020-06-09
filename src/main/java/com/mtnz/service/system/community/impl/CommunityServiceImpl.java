package com.mtnz.service.system.community.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mtnz.controller.app.community.model.Community;
import com.mtnz.controller.app.community.model.CommunityComments;
import com.mtnz.controller.app.community.model.CommunityReport;
import com.mtnz.controller.app.community.model.CommunityUser;
import com.mtnz.controller.app.store.model.Store;
import com.mtnz.controller.app.store.model.StoreUser;
import com.mtnz.controller.app.user.model.SysAppUser;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.entity.Page;
import com.mtnz.service.system.banner.BannerService;
import com.mtnz.service.system.community.CommunityService;
import com.mtnz.sql.system.community.CommunityCommentsMapper;
import com.mtnz.sql.system.community.CommunityMapper;
import com.mtnz.sql.system.community.CommunityReportMapper;
import com.mtnz.sql.system.community.CommunityUserMapper;
import com.mtnz.sql.system.store.StoreMapper;
import com.mtnz.sql.system.store.StoreUserMapper;
import com.mtnz.sql.system.user.SysAppUserNewMapper;
import com.mtnz.util.DateUtil;
import com.mtnz.util.MyTimesUtil;
import com.mtnz.util.PageData;
import com.mtnz.util.QiniuUtils;
import com.sun.corba.se.impl.ior.ObjectAdapterIdNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CommunityServiceImpl implements CommunityService {

    @Resource
    CommunityMapper communityMapper;

    @Resource
    CommunityCommentsMapper communityCommentsMapper;

    @Resource
    SysAppUserNewMapper sysAppUserNewMapper;

    @Resource
    CommunityUserMapper communityUserMapper;

    @Resource
    CommunityReportMapper communityReportMapper;

    @Autowired
    private StoreUserMapper storeUserMapper;

    /**
     * 查询生意圈列表
     * @param community
     * @return
     */
    @Override
    public PageInfo select(Community community) {
        PageHelper.startPage(community.getPageNumber(),community.getPageSize());
        Example CommunityExample = new Example(Community.class);
        CommunityExample.setOrderByClause("id desc");
        List<Community> list = communityMapper.selectByExample(CommunityExample);
        List<Long> ulist = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setViewTimeOne(com.mtnz.util.DateUtil.dateFormat(list.get(i).getCreatTime()));
            if(!ulist.contains(list.get(i).getUserId())){
                ulist.add(list.get(i).getUserId());
            }
            CommunityComments bean = new CommunityComments();
            bean.setCommunityId(list.get(i).getId());
            bean.setIsDelete(0);
            List<CommunityComments> commentsList = communityCommentsMapper.select(bean);
            for (int j = 0; j < commentsList.size(); j++) {
                if(!ulist.contains(commentsList.get(j).getUserId())){
                    ulist.add(commentsList.get(j).getUserId());
                }
            }

            Map<String,Object> map = storeUserMapper.findStoreIsPass(list.get(i).getUserId());
            if(map==null){
                list.get(i).setIsPass(0);
            }else {
                list.get(i).setIsPass((Integer)map.get("is_pass"));
            }

            list.get(i).setCommentsList(commentsList);
            SysAppUser sysAppUser =sysAppUserNewMapper.selectByPrimaryKey(list.get(i).getUserId());
            list.get(i).setReleaseName(sysAppUser.getName());
            list.get(i).setNickName(sysAppUser.getNickName());
            list.get(i).setHeader(sysAppUser.getHeader());
            list.get(i).setSignature(sysAppUser.getSignature());
        }
        Map<Long,String> map = new HashMap();
        if(ulist.size()>0){
            Example example = new Example(SysAppUser.class);
            example.and().andIn("uid",ulist);
            List<SysAppUser> sysAppUsers = sysAppUserNewMapper.selectByExample(example);
            for (int i = 0; i < sysAppUsers.size(); i++) {
                map.put(sysAppUsers.get(i).getUid(),sysAppUsers.get(i).getName());
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if(map.containsKey(list.get(i).getUserId())){
                list.get(i).setReleaseName(map.get(list.get(i).getUserId()));
            }
            for (int j = 0; j < list.get(i).getCommentsList().size(); j++) {
                CommunityComments comments = list.get(i).getCommentsList().get(j);
                if(map.containsKey(comments.getUserId())){
                    list.get(i).getCommentsList().get(j).setCommentsName(map.get(comments.getUserId()));
                }
            }
            CommunityUser communityUser = new CommunityUser();
            communityUser.setCommunityId(list.get(i).getId());
            communityUser.setUserId(community.getUserId());
            List<CommunityUser> communityUsers = communityUserMapper.select(communityUser);
            for (int j = 0; j < communityUsers.size(); j++) {
                if(communityUsers.get(j).getType()==1){
                    list.get(i).setIspraise(1);
                }else if(communityUsers.get(j).getType()==2){
                    list.get(i).setIsget(1);
                }else if(communityUsers.get(j).getType()==3){
                    list.get(i).setIstalk(1);
                }

            }
        }
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 发布动态
     * @param community
     */
    @Override
    public void insert(Community community) {
        communityMapper.insertSelective(community);
    }

    /**
     * 修改状态（点赞，删除，修改）
     * @param community
     */
    @Override
    public void update(Community community) {
        //修改点赞数量
        if(community.getPraise()!=null){
            Community community1 = communityMapper.selectByPrimaryKey(community.getId());
            community.setPraise(community.getPraise() + community1.getPraise());
            if(community.getPraise()>0){//加赞
                CommunityUser communitybean = new CommunityUser();
                communitybean.setStatus(1);
                communitybean.setType(1);
                communitybean.setUserId(community.getMakerId());
                communitybean.setCommunityId(community.getId());
                if(communityUserMapper.selectCount(communitybean)>0){
                    throw new ServiceException(-103,"不可重复点赞",null);
                }
                CommunityUser communityUser = new CommunityUser();
                communityUser.setUserId(community.getMakerId());
                communityUser.setType(1);
                communityUser.setStatus(1);
                communityUser.setCommunityId(community.getId());
                communityUserMapper.insertSelective(communityUser);
            }else {//减赞
                CommunityUser communityUser = new CommunityUser();
                communityUser.setUserId(community.getMakerId());
                communityUser.setStatus(1);
                communityUser.setType(1);
                communityUser.setCommunityId(community.getId());
                communityUserMapper.delete(communityUser);
            }

        }
        //修改收藏数量
        if(community.getCollection()!=null){
            Community community1 = communityMapper.selectByPrimaryKey(community.getId());
            community.setCollection(community.getCollection() + community1.getCollection());
            if(community.getCollection()>0){//加收藏
                CommunityUser communitybean = new CommunityUser();
                communitybean.setStatus(1);
                communitybean.setType(2);
                communitybean.setUserId(community.getMakerId());
                communitybean.setCommunityId(community.getId());
                if(communityUserMapper.selectCount(communitybean)>0){
                    throw new ServiceException(-103,"不可重复收藏",null);
                }
                CommunityUser communityUser = new CommunityUser();
                communityUser.setUserId(community.getMakerId());
                communityUser.setType(2);
                communityUser.setStatus(1);
                communityUser.setCommunityId(community.getId());
                communityUserMapper.insertSelective(communityUser);
            }else {//减收藏
                CommunityUser communityUser = new CommunityUser();
                communityUser.setUserId(community.getMakerId());
                communityUser.setStatus(1);
                communityUser.setType(2);
                communityUser.setCommunityId(community.getId());
                communityUserMapper.delete(communityUser);
            }

        }
        communityMapper.updateByPrimaryKeySelective(community);
    }

    /**
     * 添加评论
     * @param communityComments
     */
    @Override
    public void insertcomments(CommunityComments communityComments) {
        CommunityUser communityUser = new CommunityUser();
        communityUser.setUserId(communityComments.getUserId());
        communityUser.setType(3);
        communityUser.setStatus(1);
        communityUser.setCommunityId(communityComments.getCommunityId());
        communityUserMapper.insertSelective(communityUser);

        communityCommentsMapper.insertSelective(communityComments);
    }

    /**
     * 修改评论（点赞，修改，删除）
     * @param communityComments
     */
    @Override
    public void updatecomments(CommunityComments communityComments) {
        if(communityComments.getPraise()!=null){
            CommunityComments comments = communityCommentsMapper.selectByPrimaryKey(communityComments.getId());
            communityComments.setPraise(comments.getPraise()+communityComments.getPraise());

            //修改点赞数量
            if(communityComments.getPraise()>0){//加赞
                CommunityUser communityUser = new CommunityUser();
                communityUser.setUserId(communityComments.getMakerId());
                communityUser.setType(1);
                communityUser.setStatus(2);
                communityUser.setCommunityId(communityComments.getCommunityId());
                communityUserMapper.insertSelective(communityUser);
            }else {//减赞
                CommunityUser communityUser = new CommunityUser();
                communityUser.setUserId(communityComments.getMakerId());
                communityUser.setStatus(2);
                communityUser.setType(1);
                communityUserMapper.delete(communityUser);
            }
        }
        communityCommentsMapper.updateByPrimaryKeySelective(communityComments);
    }

    /**
     * 七牛获取上传用的token
     * @return
     */
    @Override
    public String getoken() {

        return QiniuUtils.getUpToken();
    }

    /**
     * 查询详情
     * @param bean
     * @return
     */
    @Override
    public Community selectdetail(Community bean) {
        Community community = communityMapper.selectByPrimaryKey(bean.getId());
        CommunityComments comments = new CommunityComments();
        comments.setIsDelete(0);
        comments.setCommunityId(bean.getId());
        CommunityUser userBean = new CommunityUser();
        userBean.setUserId(bean.getMakerId());
        userBean.setCommunityId(bean.getId());
        userBean.setStatus(1);
        List<CommunityUser> userList = communityUserMapper.select(userBean);
        for (int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getType()==1){
                community.setIspraise(1);
            }else if(userList.get(i).getType()==2){
                community.setIsget(1);
            }else if(userList.get(i).getType()==3){
                community.setIstalk(1);
            }
        }
        PageHelper.startPage(bean.getPageNumber(),bean.getPageSize());
        List<CommunityComments> list = communityCommentsMapper.select(comments);
        SysAppUser sysAppUser1 = sysAppUserNewMapper.selectByPrimaryKey(community.getUserId());
        community.setReleaseName(sysAppUser1.getName());
        for (int i = 0; i < list.size(); i++) {
            SysAppUser sysAppUser2 = sysAppUserNewMapper.selectByPrimaryKey(list.get(i).getUserId());
            list.get(i).setCommentsName(sysAppUser2.getName());
            CommunityUser communityUser= new CommunityUser();
            communityUser.setUserId(list.get(i).getUserId());
            communityUser.setStatus(2);
            communityUser.setType(1);
            if(communityUserMapper.selectCount(communityUser)>0){
                list.get(i).setIspraise(1);
            }
        }
        PageInfo pageInfo = new PageInfo(list);
        community.setCommentsList(list);
        community.setPageInfo(pageInfo);
        return community;
    }

    /**
     * 投诉添加
     * @param communityReport
     */
    @Override
    @Transactional
    public void insertReport(CommunityReport communityReport) {
        CommunityReport communityReport1 = new CommunityReport();
        communityReport1.setUserId(communityReport.getUserId());
        communityReport1.setType(communityReport.getType());
        if(communityReport.getType()==1){
            communityReport1.setCommunityId(communityReport.getCommunityId());
        }
        if (communityReport.getType()==2){
            communityReport1.setCommentsId(communityReport.getCommentsId());
        }
        if(communityReportMapper.selectCount(communityReport1)>0){
            throw new ServiceException(-103,"已举报，无需重复举报",null);
        }
        communityReportMapper.insertSelective(communityReport);
    }


    @Override
    public List<Map<String, Object>> getCommunityReportList() {
        return communityReportMapper.getCommunityReportList();
    }

    public void updateStatus(CommunityReport communityReport){
       communityReportMapper.updateByPrimaryKey(communityReport);
    }

}
