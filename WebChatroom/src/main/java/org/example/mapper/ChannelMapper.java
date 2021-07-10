package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.base.BaseMapper;
import org.example.model.Channel;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ChannelMapper extends BaseMapper<Channel> {
}