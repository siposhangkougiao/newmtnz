package com.mtnz.service.system.community.impl;

import com.mtnz.controller.app.community.model.CommunityNotice;
import com.mtnz.service.system.community.CommunityNoticeService;
import com.mtnz.sql.system.community.CommunityNoticeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class CommunityNoticeServiceImpl implements CommunityNoticeService {

    @Resource
    private CommunityNoticeMapper communityNoticeMapper;

    @Override
    public List<CommunityNotice> findList() {
        return communityNoticeMapper.findList();
    }

    @Override
    public void addNotice(CommunityNotice communityNotice) {
        communityNoticeMapper.insertSelective(communityNotice);
    }

    @Override
    public void deleteNotice(Integer id) {
        communityNoticeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateNotice(CommunityNotice communityNotice) {
        communityNoticeMapper.updateByPrimaryKey(communityNotice);
    }
}
