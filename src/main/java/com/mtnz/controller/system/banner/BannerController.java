package com.mtnz.controller.system.banner;

import com.alibaba.fastjson.JSONObject;
import com.mtnz.controller.app.community.model.Community;
import com.mtnz.controller.base.BaseController;
import com.mtnz.controller.base.Result;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.entity.Page;
import com.mtnz.service.system.banner.BannerService;
import com.mtnz.util.Const;
import com.mtnz.util.Jurisdiction;
import com.mtnz.util.PageData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
    Created by xxj on 2018\3\30 0030.  
 */
@Controller
@RequestMapping(value = "banner",produces = "text/html;charset=UTF-8")
public class BannerController extends BaseController{

    String menuUrl = "banner/list.do"; // 菜单地址(权限用)

    @Resource(name = "bannerService")
    private BannerService bannerService;


    /**
     * 列表
     */
    @RequestMapping(value="/list")
    public ModelAndView list(Page page){
        logBefore(logger, "列表Banner");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        try{
            pd = this.getPageData();
            //页面检索
            String KEYWORD = pd.getString("KEYWORD");
            if (null != KEYWORD && !"".equals(KEYWORD)) {
                KEYWORD = KEYWORD.trim();
                pd.put("KEYWORD", KEYWORD);
            }
            page.setPd(pd);
            List<PageData>	varList = bannerService.list(page);	//列出Banner列表
            mv.setViewName("system/banner/banner_list");
            mv.addObject("varList", varList);
            mv.addObject("pd", pd);
            mv.addObject(Const.SESSION_QX,this.getHC());	//按钮权限
        } catch(Exception e){
            logger.error(e.toString(), e);
        }
        return mv;
    }




    // ===================================================================================================

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }

    /* ===============================权限================================== */
    public Map<String, String> getHC() {
        Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
        Session session = currentUser.getSession();
        return (Map<String, String>) session.getAttribute(Const.SESSION_QX);
    }
    /* ===============================权限================================== */
}
