package com.example.pushdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 林杰炜 Linjw
 * @Title 内部推送结果
 * @date 2020/9/27 9:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushResult implements Serializable {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 推送平台返回的Id
     */
    private String messageId;

    /**
     * 失败原因
     */
    private String reason;
}
