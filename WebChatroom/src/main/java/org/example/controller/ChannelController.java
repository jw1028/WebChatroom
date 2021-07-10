package org.example.controller;

import org.example.mapper.ChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChannelController {
    @Autowired
    private ChannelMapper channelMapper;


    @RequestMapping(value = "/channelList", method = RequestMethod.GET)
    public Object query(){
        return channelMapper.selectAll();
    }
}
