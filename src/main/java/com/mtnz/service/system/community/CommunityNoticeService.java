package com.mtnz.service.system.community;

import com.mtnz.controller.app.community.model.CommunityNotice;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommunityNoticeService {


    /**
     * 查询列表
     * @return
     */
    public List<CommunityNotice> findList();

    /**
     * 新增公告
     * @param communityNotice
     */
    public void addNotice(CommunityNotice communityNotice);

    public void deleteNotice(Integer id);

    public void updateNotice(CommunityNotice communityNotice);


}
