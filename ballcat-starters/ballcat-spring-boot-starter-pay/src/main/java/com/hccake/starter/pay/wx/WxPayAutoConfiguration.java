package com.hccake.starter.pay.wx;

import com.hccake.extend.pay.wx.WxPay;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2021/1/25 11:30
 */
@AutoConfiguration
@ConditionalOnClass(WxPay.class)
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(WxPay.class)
	public WxPay wxPay(WxPayProperties properties) {
		// 获取配置
		WxPayProperties.Config config = properties.isSandbox() ? properties.getDev() : properties.getProd();

		WxPay wxPay = new WxPay(config.getAppId(), config.getMchId(), config.getMckKey(), properties.isSandbox());

		wxPay.setReturnUrl(config.getReturnUrl());
		wxPay.setNotifyUrl(config.getNotifyUrl());
		return wxPay;
	}

}
