package com.daxueyuan.util;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: Sean
 * @Date: 2019/5/17 22:37
 */
@Slf4j
public class PushUtil {
    private static final String APP_KEY = "608b4698cd072a9d5c5c2601";
    private static final String MASTER_SECRET = "921eaa9c31f842556927d993";
    private static final String MESSAGE = "您的订单已被处理，请及时确认";

    public static void sendPush(String alias, String message) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jPushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);
        PushPayload payload = buildPushObject_all_alias_alert(alias, message);

        try {
            PushResult result = jPushClient.sendPush(payload);
            log.info("得到结果 " + result);
            System.out.println(result);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    private static PushPayload buildPushObject_all_alias_alert(String alias, String message) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(message))
                .build();
    }
}
