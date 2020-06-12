package com.mtnz.sql.system.community;


import com.mtnz.controller.app.community.model.Community;
import com.mtnz.controller.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommunityMapper extends MyMapper<Community> {

    List<Community> selectByCommunity(Community community);

    List<Community> selectByType(@Param("userId")Long userId,@Param("type")Integer type);

    Integer talkCount(@Param("communityId") Long communityId);

}