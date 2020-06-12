package com.mtnz.sql.system.community;


import com.mtnz.controller.app.community.model.Community;
import com.mtnz.controller.app.community.model.CommunityUser;
import com.mtnz.controller.base.MyMapper;

public interface CommunityUserMapper extends MyMapper<CommunityUser> {

    CommunityUser selectCommOne(CommunityUser communityUser);

}