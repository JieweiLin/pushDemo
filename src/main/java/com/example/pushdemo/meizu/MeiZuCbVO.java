package com.example.pushdemo.meizu;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author linjw
 * @Title
 * @date 2020/1/3 14:16
 */
@Data
public class MeiZuCbVO {
    /**
     * 业务上传的自定义参数值
     */
    @JSONField(name = "param")
    private String param;
    /**
     * callback 类型 1-送达 2-点击
     */
    @JSONField(name = "type")
    private Integer type;
    /**
     * Push regId列表
     */
    @JSONField(name = "targets")
    private List<String> targets;
}
