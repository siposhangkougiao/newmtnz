package com.mtnz.sql.system.community;


import com.mtnz.controller.app.community.model.CommunityNotice;
import com.mtnz.controller.app.community.model.CommunityUser;
import com.mtnz.controller.base.MyMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface CommunityNoticeMapper extends MyMapper<CommunityNotice> {



    @Select("select id,title,content,createTime from community_notice")
    List<CommunityNotice> findList();



}