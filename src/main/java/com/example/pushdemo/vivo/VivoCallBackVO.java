package com.example.pushdemo.vivo;

import lombok.Data;

import java.util.Map;

/**
 * @author linjw
 * @Title VIVO Push 回调VO
 * @date 2020/1/3 11:44
 */
@Data
public class VivoCallBackVO {
    /**
     * 例如: {"taskId1":{"param":"param","targets":"regId"}}
     */
    private Map<String, VivoTaskVO> vivoTaskVOs;
}
