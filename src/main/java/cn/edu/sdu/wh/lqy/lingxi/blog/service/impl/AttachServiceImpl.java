package cn.edu.sdu.wh.lqy.lingxi.blog.service.impl;

import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.AttachMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Attach;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.AttachVoExample;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IAttachService;
import cn.edu.sdu.wh.lqy.lingxi.blog.utils.DateKit;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttachServiceImpl implements IAttachService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttachServiceImpl.class);

    @Autowired
    private AttachMapper attachMapper;

    @Override
    public PageInfo<Attach> getAttachs(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        AttachVoExample attachVoExample = new AttachVoExample();
        attachVoExample.setOrderByClause("id desc");
        List<Attach> attaches = attachMapper.selectByExample(attachVoExample);
        return new PageInfo<>(attaches);
    }

    @Override
    public Attach selectById(Integer id) {
        if(null != id){
            return attachMapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    @Transactional
    public void save(String fname, String fkey, String ftype, Integer author) {
        Attach attach = new Attach();
        attach.setFname(fname);
        attach.setAuthorId(author);
        attach.setFkey(fkey);
        attach.setFtype(ftype);
        attach.setCreated(DateKit.getCurrentUnixTime());
        attachMapper.insertSelective(attach);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (null != id) {
            attachMapper.deleteByPrimaryKey( id);
        }
    }
}
