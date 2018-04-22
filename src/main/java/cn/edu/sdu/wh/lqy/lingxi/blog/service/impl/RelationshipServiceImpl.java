package cn.edu.sdu.wh.lqy.lingxi.blog.service.impl;

import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.RelationshipMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.RelationshipVoExample;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.RelationshipVoKey;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IRelationshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipServiceImpl implements IRelationshipService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationshipServiceImpl.class);

    @Autowired
    private RelationshipMapper relationshipMapper;

    @Override
    public void deleteById(Integer cid, Integer mid) {
        RelationshipVoExample relationshipVoExample = new RelationshipVoExample();
        RelationshipVoExample.Criteria criteria = relationshipVoExample.createCriteria();
        if (cid != null) {
            criteria.andCidEqualTo(cid);
        }
        if (mid != null) {
            criteria.andMidEqualTo(mid);
        }
        relationshipMapper.deleteByExample(relationshipVoExample);
    }

    @Override
    public List<RelationshipVoKey> getRelationshipById(Integer cid, Integer mid) {
        RelationshipVoExample relationshipVoExample = new RelationshipVoExample();
        RelationshipVoExample.Criteria criteria = relationshipVoExample.createCriteria();
        if (cid != null) {
            criteria.andCidEqualTo(cid);
        }
        if (mid != null) {
            criteria.andMidEqualTo(mid);
        }
        return relationshipMapper.selectByExample(relationshipVoExample);
    }

    @Override
    public void insertVo(RelationshipVoKey relationshipVoKey) {
        relationshipMapper.insert(relationshipVoKey);
    }

    @Override
    public Long countById(Integer cid, Integer mid) {
        LOGGER.debug("Enter countById method:cid={},mid={}",cid,mid);
        RelationshipVoExample relationshipVoExample = new RelationshipVoExample();
        RelationshipVoExample.Criteria criteria = relationshipVoExample.createCriteria();
        if (cid != null) {
            criteria.andCidEqualTo(cid);
        }
        if (mid != null) {
            criteria.andMidEqualTo(mid);
        }
        long num = relationshipMapper.countByExample(relationshipVoExample);
        LOGGER.debug("Exit countById method return num={}",num);
        return num;
    }
}
