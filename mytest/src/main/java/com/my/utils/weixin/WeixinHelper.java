/**
 * 
 */
package com.my.utils.weixin;

import com.my.utils.weixin.qrcode.QrCodeAPI;
import com.my.utils.weixin.token.AccessTokenAPI;
import com.my.utils.weixin.user.UserAPI;

/**
 * @author taozui
 */
public class WeixinHelper{

	private WeixinHelper(){

	}

	public static final UserAPI user = new UserAPI();

	public static final AccessTokenAPI accessToken = new AccessTokenAPI();

	public static final QrCodeAPI qrcode = new QrCodeAPI();

//	public static final TicketAPI ticket = new TicketAPI();

}
