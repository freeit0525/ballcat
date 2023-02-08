package com.hccake.ballcat.common.websocket.distribute;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.common.websocket.WebSocketMessageSender;
import com.hccake.ballcat.common.websocket.session.WebSocketSessionStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

/**
 * @author hccake
 */
@Slf4j
public abstract class AbstractMessageDistributor implements MessageDistributor {

	private final WebSocketSessionStore webSocketSessionStore;

	protected AbstractMessageDistributor(WebSocketSessionStore webSocketSessionStore) {
		this.webSocketSessionStore = webSocketSessionStore;
	}

	/**
	 * 对当前服务中的 websocket 连接做消息推送
	 * @param messageDO 消息实体
	 */
	protected void doSend(MessageDO messageDO) {

		// 是否广播发送
		Boolean needBroadcast = messageDO.getNeedBroadcast();

		// 获取待发送的 sessionKeys
		Collection<Object> sessionKeys;
		if (needBroadcast != null && needBroadcast) {
			sessionKeys = webSocketSessionStore.getSessionKeys();
		}
		else {
			sessionKeys = messageDO.getSessionKeys();
		}
		if (CollectionUtil.isEmpty(sessionKeys)) {
			log.warn("发送 websocket 消息，却没有找到对应 sessionKeys, messageDo: {}", messageDO);
			return;
		}

		String messageText = messageDO.getMessageText();
		Boolean onlyOneClientInSameKey = messageDO.getOnlyOneClientInSameKey();

		for (Object sessionKey : sessionKeys) {
			Collection<WebSocketSession> sessions = webSocketSessionStore.getSessions(sessionKey);
			if (CollectionUtil.isNotEmpty(sessions)) {
				// 相同 sessionKey 的客户端只推送一次操作
				if (onlyOneClientInSameKey != null && onlyOneClientInSameKey) {
					WebSocketSession wsSession = CollectionUtil.get(sessions, 0);
					WebSocketMessageSender.send(wsSession, messageText);
					continue;
				}
				for (WebSocketSession wsSession : sessions) {
					WebSocketMessageSender.send(wsSession, messageText);
				}
			}
		}
	}

}
