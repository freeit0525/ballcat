package com.hccake.extend.pay.wx.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.extend.pay.wx.WxPay;
import com.hccake.extend.pay.wx.enums.ResponseCode;
import com.hccake.extend.pay.wx.enums.TradeType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.Map;

/**
 * @author lingting 2021/2/25 15:43
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WxPayCallback {

	private String transactionId;

	private String nonceStr;

	private String bankType;

	private String openid;

	private String sign;

	private String feeType;

	private String mchId;

	private BigInteger cashFee;

	private String outTradeNo;

	private String appid;

	private BigInteger totalFee;

	private TradeType tradeType;

	private ResponseCode resultCode;

	private String timeEnd;

	private String isSubscribe;

	private ResponseCode returnCode;

	public static WxPayCallback of(Map<String, String> res) {
		return JsonUtils.toObj(JsonUtils.toJson(res), WxPayCallback.class).setRaw(res);
	}

	/**
	 * 返回的原始数据
	 */
	private Map<String, String> raw;

	/**
	 * 验签
	 * @param wxPay 微信支付信息
	 * @return boolean
	 */
	public boolean checkSign(WxPay wxPay) {
		return wxPay.checkSign(this);
	}

}
