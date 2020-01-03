package com.example.pushdemo.getui;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author linjw
 * @Title 个推回执对象
 * @date 2020/1/3 15:21
 */
@Data
public class GetuiCallBackVO {
    /**
     * 个推Push appId
     */
    @JSONField(name = "appid")
    private String appId;
    /**
     * 接收者CID
     */
    @JSONField(name = "cid")
    private String cid;
    /**
     * 任务Id（即ContentId）
     */
    @JSONField(name = "taskid")
    private String taskId;
    /**
     * 消息Id
     */
    @JSONField(name = "msgid")
    private String msgId;
    /**
     * 结果码 200-成功 400-推送苹果接口失败 401-用户不存在 402-非活跃用户 500-系统内部异常
     */
    @JSONField(name = "code")
    private String code;
    /**
     * 推送结果描述
     */
    @JSONField(name = "desc")
    private String desc;
    /**
     * 签名认证
     * MD5(appId+cid+taskId+msgId+masterSecret)
     */
    @JSONField(name = "sign")
    private String sign;
    /**
     * 回执Id
     * actionid=0	个推消息到达
     * actionid=10000	个推通知展示
     * actionid=10010	个推通知点击
     * actionid=10030	个推透传到达
     * actionid=90000-90999	自定义回执
     * actionid=130000	魅族通知已发送
     * actionid=130010	魅族通知已点击
     * actionid=120000	小米通知已发送
     * actionid=120010	小米通知已点击
     * actionid=110000	华为成功下发
     * actionid=140000 OPPO到达
     * actionid=150000 VIVO到达
     *
     * actionid=10019	苹果点击
     * actionid=10009	苹果展示
     */
    @JSONField(name = "actionId")
    private String actionId;
    /**
     * 回执上传时间
     */
    @JSONField(name = "recvtime")
    private Long recvTime;
    /**
     * 根据配置返回
     */
    @JSONField(name = "alias")
    private String alias;
}
