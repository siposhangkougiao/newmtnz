package com.mtnz.sql.system.community;


import com.github.pagehelper.PageInfo;
import com.mtnz.controller.app.community.model.Community;
import com.mtnz.controller.app.community.model.CommunityReport;
import com.mtnz.controller.base.MyMapper;
import com.mtnz.entity.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommunityReportMapper extends MyMapper<CommunityReport> {

    List<Map<String,Object>> getCommunityReportList();

}