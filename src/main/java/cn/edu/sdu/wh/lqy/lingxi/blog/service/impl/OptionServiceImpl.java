package cn.edu.sdu.wh.lqy.lingxi.blog.service.impl;

import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.OptionVoMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Option;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.OptionVoExample;
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
    public void insertOption(Option option) {
        LOGGER.debug("Enter insertOption method:option={}", option);
        optionVoMapper.insertSelective(option);
        LOGGER.debug("Exit insertOption method.");
    }

    @Override
    @Transactional
    public void insertOption(String name, String value) {
        LOGGER.debug("Enter insertOption method:name={},value={}", name, value);
        Option option = new Option();
        option.setName(name);
        option.setValue(value);
        if (optionVoMapper.selectByPrimaryKey(name) == null) {
            optionVoMapper.insertSelective(option);
        } else {
            optionVoMapper.updateByPrimaryKeySelective(option);
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
    public Option getOptionByName(String name) {
        return optionVoMapper.selectByPrimaryKey(name);
    }

    @Override
    public List<Option> getOptions() {
        return optionVoMapper.selectByExample(new OptionVoExample());
    }
}
