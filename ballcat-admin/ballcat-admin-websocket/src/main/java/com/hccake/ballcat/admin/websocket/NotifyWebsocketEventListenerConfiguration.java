package com.hccake.ballcat.admin.websocket;

import com.hccake.ballcat.admin.websocket.listener.NotifyWebsocketEventListener;
import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.notify.handler.NotifyInfoDelegateHandler;
import com.hccake.ballcat.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.notify.service.UserAnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@ConditionalOnClass({ NotifyWebsocketEventListener.class, UserAnnouncementService.class })
@Configuration(proxyBeanMethods = false)
public class NotifyWebsocketEventListenerConfiguration {

	private final MessageDistributor messageDistributor;

	@Bean
	public NotifyWebsocketEventListener notifyWebsocketEventListener(
			NotifyInfoDelegateHandler<? super NotifyInfo> notifyInfoDelegateHandler) {
		return new NotifyWebsocketEventListener(messageDistributor, notifyInfoDelegateHandler);
	}

}
