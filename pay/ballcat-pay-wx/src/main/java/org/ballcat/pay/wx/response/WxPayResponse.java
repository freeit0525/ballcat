/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.pay.wx.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.pay.wx.enums.ResponseCode;
import org.ballcat.pay.wx.enums.TradeType;

/**
 * @author lingting 2021/2/1 11:38
 */
@Data
@Accessors(chain = true)
public class WxPayResponse {

	public static WxPayResponse of(Map<String, String> res) {
		return JsonUtils.toObj(JsonUtils.toJson(res), WxPayResponse.class).setRaw(res);
	}

	/**
	 * 返回状态码. 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 */
	@JsonProperty("return_code")
	private ResponseCode returnCode;

	/**
	 * 返回信息. 返回信息，如非空，为错误原因: 1.签名失败 2.参数格式校验错误
	 */
	@JsonProperty("return_msg")
	private String returnMsg;

	/**
	 * 应用APPID. 调用接口提交的应用ID
	 */
	@JsonProperty("appid")
	private String appId;

	/**
	 * 商户号. 调用接口提交的商户号
	 */
	@JsonProperty("mch_id")
	private String mchId;

	/**
	 * 设备号. 调用接口提交的终端设备号，
	 */
	@JsonProperty("device_info")
	private String deviceInfo;

	/**
	 * 随机字符串. 微信返回的随机字符串
	 */
	@JsonProperty("nonce_str")
	private String nonceStr;

	/**
	 * 签名. 微信返回的签名，详见签名算法
	 */
	@JsonProperty("sign")
	private String sign;

	/**
	 * 业务结果. SUCCESS/FAIL
	 */
	@JsonProperty("result_code")
	private ResponseCode resultCode;

	/**
	 * 错误代码. 详细参见第6节错误列表
	 */
	@JsonProperty("err_code")
	private String errCode;

	/**
	 * 错误代码描述. 错误返回的信息描述
	 */
	@JsonProperty("err_code_des")
	private String errCodeDes;

	/**
	 * 交易类型. 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
	 */
	@JsonProperty("trade_type")
	private TradeType tradeType;

	/**
	 * 预支付交易会话标识. 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	 */
	@JsonProperty("prepay_id")
	private String prepayId;

	/**
	 * 沙箱专用密钥
	 */
	@JsonProperty("sandbox_signkey")
	private String sandboxSignKey;

	/**
	 * 原生支付返回的二维码
	 * @see TradeType#NATIVE
	 */
	@JsonProperty("code_url")
	private String codeUrl;

	/**
	 * h5 支付
	 * @see TradeType#MWEB
	 */
	@JsonProperty("mweb_url")
	private String mWebUrl;

	/**
	 * 返回的原始数据
	 */
	private Map<String, String> raw;

}
