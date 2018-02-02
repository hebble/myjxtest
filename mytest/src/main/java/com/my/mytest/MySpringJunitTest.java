package com.my.mytest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weixin4j.Followers;
import org.weixin4j.Menu;
import org.weixin4j.User;
import org.weixin4j.Weixin;
import org.weixin4j.WeixinException;
import org.weixin4j.http.OAuthToken;
import org.weixin4j.menu.ClickButton;
import org.weixin4j.menu.LocationSelectButton;
import org.weixin4j.menu.PicPhotoOrAlbumButton;
import org.weixin4j.menu.PicSysPhotoButton;
import org.weixin4j.menu.PicWeixinButton;
import org.weixin4j.menu.ScancodePushButton;
import org.weixin4j.menu.ScancodeWaitMsgButton;
import org.weixin4j.menu.SingleButton;
import org.weixin4j.menu.ViewButton;
import org.weixin4j.ticket.TicketType;

import com.alibaba.fastjson.JSON;
import com.my.MyApplication;
import com.my.pojo.MyWeixinAccount;
import com.my.utils.weixin.qrcode.QrCodeAPI;
import com.my.utils.weixin.qrcode.QrCodeCreate;
import com.my.utils.weixin.token.AccessTokenAPI;
import com.my.utils.weixin.user.UserAPI;
import com.my.utils.weixin.user.UserInfo;
import com.my.utils.weixin.user.UserList;
import com.my.utils.weixin.user.UserList.Data;
import com.my.utils.weixin4j.WeixinManager;

@RunWith(SpringJUnit4ClassRunner.class) // 指定运行的主类
@SpringBootTest(classes=MyApplication.class) // 指定SpringBoot
public class MySpringJunitTest {

	@Autowired
	private MyWeixinAccount myWeixinAccount;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private AccessTokenAPI accessTokenAPI;
	@Autowired
	private WeixinManager weixinManager;
	@Autowired
	private Environment environment;
	
	@Test
	public void testEnvironment() {
		String property = environment.getProperty("spring.redis.timeout");
		System.out.println("porperty="+property);
	}
	
	/**
	 * 获取accessToken
	 */
	@Test
	public void test1() {
		String accessToken = accessTokenAPI.get(myWeixinAccount.getAppid(), myWeixinAccount.getAppsecret());
		System.out.println("accessToken="+accessToken);
	}
	
	/**
	 * 获取二维码
	 */
	@Test
	public void test2() {
		String accessToken = accessTokenAPI.get(myWeixinAccount.getAppid(), myWeixinAccount.getAppsecret());
		QrCodeCreate qrCodeCreate = QrCodeAPI.create(accessToken, "123");
		String qrCodeUrl = QrCodeAPI.qrCodeUrl(qrCodeCreate.getTicket());
		System.out.println("qrCodeUrl="+qrCodeUrl);
	}
	
	/**
	 * 获取用户列表
	 */
	@Test
	public void test3() {
		String accessToken = accessTokenAPI.get(myWeixinAccount.getAppid(), myWeixinAccount.getAppsecret());
		UserList userList = UserAPI.get(accessToken, "");
		Data data = userList.getData();
		List<String> openids = data.openid;
		int i = 1;
		List<UserInfo> list = new ArrayList<>();
		for (String openid : openids) {
			UserInfo info = UserAPI.info(accessToken, openid);
			list.add(info);
			System.out.println("----------------"+i+"-----------------"+JSON.toJSONString(info));
			i++;
		}
		System.out.println("======================================");
		System.out.println(JSON.toJSONString(list));
	}
	
	/**
	 * weixin4j
	 * @throws WeixinException
	 */
	@Test
	public void test4() throws WeixinException {
		 //1.获取微信操作对象  
        Weixin weixin = weixinManager.getWeixin();
        //接下来就可以调用Weixin对象的其他方法了  
        //3.获取关注者列表  
        Followers followers = weixin.getUserList(null); 
        //打印：关注者总数  
        System.out.println("关注者总数：" + followers.getTotal());  
        //打印：本次获取关注者记录数量  
        System.out.println("本次获取关注者数量：" + followers.getCount());  
        //打印：关注者openId数据  
        org.weixin4j.Data data = followers.getData();  
        if (data != null) {  
            //获取openId集合  
            List<String> openIdList = data.getOpenid(); 
            System.out.println("openId个数:"+openIdList.size());
            //打印：前3条记录  
            for (int i = 0; i < openIdList.size(); i++) {  
                System.out.println("第" + i + "条 " + openIdList.get(i));  
                String openId = openIdList.get(i);
//                weixin.customSendContent(openId, "你好,这是我的测试");
                User userInfo = weixin.getUserInfo(openId);
                System.out.println("==================userinfo="+JSON.toJSONString(userInfo));
            }  
        }  
	}
	
	@Test
	public void test5() throws WeixinException {
		Weixin weixin = weixinManager.getWeixin();
		OAuthToken oAuthToken = weixin.getOAuthToken();
		
		weixin.createQrcode(TicketType.QR_SCENE, "E:\\myQrcode.png", 123, 60*60);
	}
	
	/**
	 * 测试创建微信菜单
	 * @throws WeixinException
	 */
	@Test
	public void createMenu() throws WeixinException {
		Menu menu = new Menu();
		// 一级菜单
		SingleButton oneButton1 = new SingleButton();
		SingleButton oneButton2 = new SingleButton();
		SingleButton oneButton3 = new SingleButton();
		oneButton1.setName("主页");
		oneButton2.setName("个人中心");
		oneButton3.setName("我的");
		List<SingleButton> menuList = new ArrayList<>();
		
		// 第一个菜单
		List<SingleButton> twoButtonList1 = new ArrayList<>();
		ClickButton clickButton = new ClickButton();
		clickButton.setName("点我点我");
		clickButton.setKey("hello");
		PicWeixinButton picWeixinButton = new PicWeixinButton();
		picWeixinButton.setName("微信相册");
		picWeixinButton.setKey("weixin photo");
		twoButtonList1.add(clickButton);
		twoButtonList1.add(picWeixinButton);
		
		oneButton1.setSubButton(twoButtonList1);
		
		//第二个菜单
		List<SingleButton> twoButtonList2 = new ArrayList<>();
		ViewButton viewButton =new ViewButton();
		viewButton.setName("去百度");
		viewButton.setUrl("https://www.baidu.com");
		ScancodePushButton scancodePushButton = new ScancodePushButton();
		scancodePushButton.setName("扫一扫");
		scancodePushButton.setKey("scan");
		ScancodeWaitMsgButton scancodeWaitMsgButton = new ScancodeWaitMsgButton();
		scancodeWaitMsgButton.setName("扫一扫(消息接受中)");
		scancodeWaitMsgButton.setKey("scan wait");
		PicSysPhotoButton picSysPhotoButton = new PicSysPhotoButton();
		picSysPhotoButton.setName("拍照上传");
		picSysPhotoButton.setKey("take photo");
		PicPhotoOrAlbumButton picPhotoOrAlbumButton = new PicPhotoOrAlbumButton();
		picPhotoOrAlbumButton.setName("拍照或者相册");
		picPhotoOrAlbumButton.setKey("photo");
		twoButtonList2.add(viewButton);
		twoButtonList2.add(scancodePushButton);
		twoButtonList2.add(scancodeWaitMsgButton);
//		twoButtonList2.add(picSysPhotoButton);
//		twoButtonList2.add(picPhotoOrAlbumButton);
		oneButton2.setSubButton(twoButtonList2);
		
		//第三个菜单
		List<SingleButton> twoButtonList3 = new ArrayList<>();
		LocationSelectButton locationSelectButton = new LocationSelectButton();
		locationSelectButton.setName("位置选择");
		locationSelectButton.setKey("address");
		twoButtonList3.add(locationSelectButton);
		oneButton3.setSubButton(twoButtonList3);
		
		// 添加所有菜单
		menuList.add(oneButton1);
		menuList.add(oneButton2);
		menuList.add(oneButton3);
		menu.setButton(menuList);
		Weixin weixin = weixinManager.getWeixin();
		weixin.createMenu(menu);
		System.out.println("创建菜单成功!");
	}
	
	/**
	 * 获取菜单
	 * @throws WeixinException 
	 */
	@Test
	public void getMenu() throws WeixinException {
		Weixin weixin = weixinManager.getWeixin();
		System.out.println("============================================");
		System.out.println("获取菜单");
		Menu menu = weixin.getMenu();
		System.out.println("获取到的菜单:"+JSON.toJSONString(menu));
		System.out.println(Math.round(-11.5));
	}
}
