package cn.edu.sdu.wh.lqy.lingxi.blog.service.impl;

import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.OptionVoMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.OptionVo;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.OptionVoExample;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * options表的service
 */
@Service
public class OptionServiceImpl implements IOptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionServiceImpl.class);

    @Autowired
    private OptionVoMapper optionVoMapper;

    @Override
    public void insertOption(OptionVo optionVo) {
        LOGGER.debug("Enter insertOption method:optionVo={}", optionVo);
        optionVoMapper.insertSelective(optionVo);
        LOGGER.debug("Exit insertOption method.");
    }

    @Override
    @Transactional
    public void insertOption(String name, String value) {
        LOGGER.debug("Enter insertOption method:name={},value={}", name, value);
        OptionVo optionVo = new OptionVo();
        optionVo.setName(name);
        optionVo.setValue(value);
        if (optionVoMapper.selectByPrimaryKey(name) == null) {
            optionVoMapper.insertSelective(optionVo);
        } else {
            optionVoMapper.updateByPrimaryKeySelective(optionVo);
        }
        LOGGER.debug("Exit insertOption method.");
    }

    @Override
    @Transactional
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::insertOption);
        }
    }

    @Override
    public OptionVo getOptionByName(String name) {
        return optionVoMapper.selectByPrimaryKey(name);
    }

    @Override
    public List<OptionVo> getOptions() {
        return optionVoMapper.selectByExample(new OptionVoExample());
    }
}
