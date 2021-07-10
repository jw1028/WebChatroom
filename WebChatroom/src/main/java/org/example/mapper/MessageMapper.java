package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.base.BaseMapper;
import org.example.model.Message;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> selectByLastLogout(Date lastLogout);
}