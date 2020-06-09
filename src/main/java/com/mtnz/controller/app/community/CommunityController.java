package com.mtnz.controller.app.community;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mtnz.controller.app.community.model.Community;
import com.mtnz.controller.app.community.model.CommunityComments;
import com.mtnz.controller.app.community.model.CommunityNotice;
import com.mtnz.controller.app.community.model.CommunityReport;
import com.mtnz.controller.app.store.model.StoreLose;
import com.mtnz.controller.base.BaseController;
import com.mtnz.controller.base.Result;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.entity.Page;
import com.mtnz.service.system.banner.BannerService;
import com.mtnz.service.system.community.CommunityNoticeService;
import com.mtnz.service.system.community.CommunityService;
import com.mtnz.service.system.store.StoreService;
import com.mtnz.util.PageData;
import org.apache.poi.util.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "app/community")
public class CommunityController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(CommunityController.class);

    @Resource
    private CommunityService communityService;
    @Resource
    private BannerService bannerService;
    @Resource
    private CommunityNoticeService communityNoticeService;


    /**
     *查询生意圈列表
     * @param community
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public Result select(Community community){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(community));

        Result result = new Result(0,"成功");
        try {
            PageInfo pageInfo = communityService.select(community);
            PageData pd = new PageData();
            List<PageData> banner  = bannerService.findList("BannerMapper.findList",pd);
            List<CommunityNotice> noticeList = communityNoticeService.findList();
            HashMap<String,Object> map = new HashMap();
            map.put("banner",banner);
            map.put("community",pageInfo);
            map.put("noticeList",noticeList);

            result.setData(map);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        logger.error("返回的参数：{}",JSONObject.toJSONString(result));
        return result;
    }

    /**
     *发布动态
     * @param community
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    public Result insert(@RequestBody Community community){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(community));
        Result result = new Result(0,"成功");
        try {
            communityService.insert(community);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;
    }


    /**
     *修改状态（点赞，删除，修改）
     * @param community
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @Produces(MediaType.APPLICATION_JSON)
    public Result update(@RequestBody Community community){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(community));
        Result result = new Result(0,"成功");
        try {
            communityService.update(community);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;
    }

    /**
     * 添加评论
     * @param communityComments
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Result insertcomments(@RequestBody CommunityComments communityComments){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(communityComments));
        Result result = new Result(0,"成功");
        try {
            communityService.insertcomments(communityComments);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;
    }

    /**
     * 修改评论（点赞，修改，删除）
     * @param communityComments
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Result updatecomments(@RequestBody CommunityComments communityComments){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(communityComments));
        Result result = new Result(0,"成功");
        try {
            communityService.updatecomments(communityComments);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;
    }

    /**
     * 查询详情
     * @param community
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public Result selectdetail(Community community){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(community));
        Result result = new Result(0,"成功");
        try {
            Community bean = communityService.selectdetail(community);
            result.setData(bean);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;
    }

    /**
     * 投诉添加
     * @param communityReport
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/report")
    @Produces(MediaType.APPLICATION_JSON)
    public Result insertReport(@RequestBody CommunityReport communityReport){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(communityReport));
        Result result = new Result(0,"成功");
        try {
            communityService.insertReport(communityReport);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;
    }

    /**
     * 七牛获取上传用的token
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/getToken")
    @Produces(MediaType.APPLICATION_JSON)
    public Result getoken(){
        //logger.error("接收到的参数：{}",JSONObject.toJSONString(communityComments));
        Result result = new Result(0,"成功");
        try {
            String token = communityService.getoken();
            result.setData(token);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;
    }

    /**
     * 新增公告
     * @param
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/addNotice")
    @Produces(MediaType.APPLICATION_JSON)
    public Result addNotice(@RequestBody CommunityNotice communityNotice){

        logger.error("接收到的参数：{}",JSONObject.toJSONString(communityNotice));
        Result result = new Result(0,"成功");
        try {
            communityNoticeService.addNotice(communityNotice);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;

    }

    /**
     * 修改公告
     * @param
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/updateNotice")
    @Produces(MediaType.APPLICATION_JSON)
    public Result updateNotice(@RequestBody CommunityNotice communityNotice){

        logger.error("接收到的参数：{}",JSONObject.toJSONString(communityNotice));
        Result result = new Result(0,"成功");
        try {
            communityNoticeService.updateNotice(communityNotice);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;

    }

    /**
     * 删除公告
     * @param
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value = "/deleteNotice")
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteNotice(String id){

        logger.error("接收到的参数：{}",JSONObject.toJSONString(id));
        Result result = new Result(0,"成功");
        try {
            communityNoticeService.deleteNotice( Integer.valueOf(id));
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;

    }

    /**
     * 上传banner图
     * @param pageData
     * @return
     */
    @RequestMapping(value="/addBanner",method = RequestMethod.POST)
    public Result addBanner(@RequestBody PageData pageData){
        //logger.error("接收到的参数：{}", JSONObject.toJSONString(pageData));
        Result result = new Result(0,"成功");
        try {
            bannerService.save(pageData);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;

    }

    /**
     * 删除banner图
     * @param
     * @return
     */
    @RequestMapping(value="/deleteBanner",method = RequestMethod.POST)
    public Result addBanner(@RequestParam("id") Long id){
        //logger.error("接收到的参数：{}", JSONObject.toJSONString(pageData));
        Result result = new Result(0,"成功");
        try {
            bannerService.delete(id);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;

    }

    /**
     * 获取文章举报列表
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value="/getCommunityReportList",method = RequestMethod.POST)
    public Result getCommunityReportList(Integer pageNumber,Integer pageSize){
        //logger.error("接收到的参数：{}", JSONObject.toJSONString());
        Result result = new Result(0,"成功");
        try {
            if(pageNumber==null||pageSize==null){
                pageNumber =1;
                pageSize=10;
            }
            PageHelper.startPage(pageNumber,pageSize);
            List<Map<String,Object>> reportList = communityService.getCommunityReportList();
            PageInfo pageInfo = new PageInfo(reportList);
            if(pageInfo !=null){
                result.setData(pageInfo);
            }
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;
    }

    /**
     * 设置文章举报显示/不显示
     * @param communityReport
     * @return
     */
    @RequestMapping(value="/updateStatus",method = RequestMethod.POST)
    public Result updateStatus(@RequestBody CommunityReport communityReport){

        Result result = new Result(0,"成功");
        try {
            communityService.updateStatus(communityReport);
        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;
    }



}
