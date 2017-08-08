package com.shareshenghuo.app.user.networkapi;

import com.shareshenghuo.app.user.BuildConfig;

public class Api {

	public static int SUCCEED = 0;

	// public static String HOST = "http://erma.h5h5h5.cn/";
	// public static String BASE_URL = HOST +
	// "oneCity-api-client-interface/oneCity/service/";

	// public static String HOSTERMA = "http://113.106.93.161:8081/";//新测试

//	public static String HOSTERMA = "https://testcity.qvs007.com/";// 测试
//	public static String HOST = "https://testapi.qvs007.com/";
//	public static String wxHOST = "http://testapi.qvs007.com/";

	// public static String HOSTERMA = "http://192.xl168.18.190:8080/";//本地
	// public static String HOST = "http://192.168.18.190:8080/";

	// TODO 秀儿原api地址
//	public static String HOSTERMA = "https://ermacity.qvs007.com/";
//	public static String HOST = "https://ermaapi.qvs007.com/";
//	public static String wxHOST = "http://ermaapi.qvs007.com/";
//	public static String HOSWAP = "https://ermawap.qvs007.com/";

	public static String URL = BuildConfig.HTTP_BASE_URL;
	public static String IMG_HOST = BuildConfig.HTTP_IMAGE_BASE_URL;
	public static String HOSTERMA = URL;
	public static String HOST = URL;
	public static String wxHOST = URL;
	public static String HOSWAP = BuildConfig.HTTP_WAP_URL;
	
	public static String BASE_URL = HOST + "oneCity/service/";

	// public static String api_id = "";
	// public static String api_secret = "";
	public static String test_utl = wxHOST+"oneCity/service/";// 仅供测试。项目投资


	// // TODO: 2017/7/20 新api接口
	// 实名认证
	// 提交身份证信息
	public static String API_SUBMIT_IDCARD_INFORMATION = URL + "oneCity/service/getServiceSubmitIdCardInformation";

	public static String WAP_URL_REGIST_LICENSE = BuildConfig.HTTP_WAP_URL + "views/module/banner/fuwu.html";


	public static String GETBANNER = BASE_URL + "findSystemTotal";// 数据展示
	public static String GETBUNBER = BASE_URL + "findUserFilialPiety";// 分数展示
	public static String INTEGRALLIST = BASE_URL + "findIntegralLogList";// 积分列表
	public static String INTEGRALLISTNEW = BASE_URL + "findIntegralLogListNew";// 供应链积分列表
	public static String NEWINTEGRALLIST = BASE_URL + "newFindIntegralLogList";// 积分列表

	public static String FRACTIONLIST = BASE_URL + "findFilialPietyLogList";// 秀心列表
	public static String FRACTIONLISTNEW = BASE_URL
			+ "findFilialPietyLogListNew";// 供应链秀心列表
	public static String NEWFRACTIONLIST = BASE_URL
			+ "newFindFilialPietyLogList";// 秀心列表

	// 我的分红权,秀心购买,入股
	public static String GETXFRGXMLIST = "UserMoneyInvestmentProjectListController";// 获取秀点入股项目列表
	public static String GETXXRGXMLIST = "FilialInvestmentProjectListController";// 获取秀心入股项目列表

	public static String GETXX_XF_LIST = BASE_URL
			+ "GetInvestmentListController";// 获取秀心秀点流水
	
	public static String BUYXX="FilialInvestmentController";//购买秀心
	public static String BUYXF="UserMoneyInvestmentController";//购买秀点

	public static String OBEDIENCELIST = BASE_URL + "findUserMoneyLogList";// 秀点列表
	public static String OBEDIENCELISTNEW = BASE_URL
			+ "findUserMoneyLogListNew";// 秀点列表
	public static String NEWMEMBERELIST = BASE_URL + "findUserInfoList";// 会员列表
	public static String CONSUMPTIONLIST = BASE_URL + "findConsumptionSeries";// 消费系列
	public static String CONSUMPTIONLISTNEW = BASE_URL
			+ "findConsumptionSeriesNew";// 消费系列
	public static String FINDSHOPPAYCLSINFLIST = BASE_URL
			+ "findShopPayclsInfList";// 营业额
	public static String GENERCODELIST = BASE_URL + "genErcode";// 二维码
	public static String FINDDAYPAYLIST = BASE_URL + "findDayPayList";// 营业额
	public static String BANKLIST = BASE_URL + "tfSupportBank";// 银行列表
	public static String FICATION = BASE_URL + "userIdentification";// 实名认证
	public static String AREAQUERY = BASE_URL + "areaQuery";// 省市区
	public static String MYCONSUMPTION = BASE_URL + "findUserPayclsInfList";// 消费
	public static String MYCONSUMPTIONNEW = BASE_URL
			+ "findUserPayclsInfListNew";// 供应链消费
	public static String USERBAND = BASE_URL + "userBand";// 绑定
	public static String USERWITHDRAW = BASE_URL + "userWithDraw";// 兑换
	public static String CONSUMERSWITHDRAW = BASE_URL + "consumersWithDraw";// 兑换

	public static String CONSUMERSWITHDRAW1 = BASE_URL + "multipleWithDraw";
	public static String CONSUMERSWITHDRAW1NEW = BASE_URL
			+ "multipleWithDrawNew";// 供应链兑换
	// public static String FINDUSERFILIALPIETY =
	// BASE_URL+"findUserFilialPiety";//再消费
	// public static String FINDUSERMONEYLOGLIST =
	// BASE_URL+"findUserMoneyLogList";//

	public static String ACCOUNTINFOQUERY = BASE_URL + "userAccountInfoQuery";// 兑换前置查询
	public static String GENEROLIST = BASE_URL + "genericOtherInfoList";// 兑换配置
	public static String SETPAYPASSWORD = BASE_URL + "getServiceSetPayPassword";// 设置支付密码
	public static String UPDATEPAYPASSWORD = BASE_URL + "updatePayPassword";// 修改支付密码
	public static String WITHDRAWLISTQUERY = BASE_URL + "withDrawListQuery";// 兑换记录
	public static String WITHDRAWSINGLEQUERY = BASE_URL + "withDrawSingleQuery";// 兑换详情
	public static String USERCARDNOCHANGE = BASE_URL + "userCardNoChange";// 银行卡

	public static String MYOILLIST = BASE_URL + "getUserOilCardList";// 我的油卡列表
	public static String OILINTEGRALLIST = BASE_URL
			+ "oilCardIntegralLogController";// 油卡积分
	public static String OILRECHARGELIST = BASE_URL
			+ "cardRechargeRecordQrcode";// 油卡充值
	public static String OILCARDSALESPIPELINECONTROLLER = BASE_URL
			+ "oilCardSalesPipelineController";// 销售流水
	public static String ADDOILCARD = BASE_URL + "userOilCardBinding";// 添加油卡
	public static String USEROILCARDUNBUNDLING = BASE_URL
			+ "userOilCardUnbundling";// 解绑油卡
	public static String GENOILCARDERCODE = BASE_URL + "genOilCardErcode";// 油卡qr
	public static String OILCARDINTEGRALLOGCONTROLLER = BASE_URL
			+ "oilCardHomePageQrcode";//
	public static String GETOILCARDCHANNEL = BASE_URL + "getOilCardChannel";// 石油通道
	public static String GETGOODSDETAIL = BASE_URL + "getGoodsDetail";// 商品详情

	public static String GETFILTERCONFIGURELISTBYTYPE = BASE_URL
			+ "getFilterConfigureListByType";// 搜索
	public static String GETANNOUNCEMENT = BASE_URL + "getAnnouncement";// 公告

	// 实名
	// public static String USERLOGIN = BASE_URL+"userLogin";
	public static String SUBMITIDCARDINFO = BASE_URL + "submitIdCardInfo";
	public static String SUBMITIDCARDINFO1 = BASE_URL + "submitIdCardInfo1";
	public static String FACEVERIFICATION = BASE_URL + "faceVerification";
	public static String FACEVERIFICATION1 = BASE_URL + "faceVerification1";

	// 充值
	public static String QUERYSHOPINFOCONTROLLER = BASE_URL
			+ "QueryShopInfoController";
	public static String FENFUPAYGETPARAM = BASE_URL + "FenfuPayGetParam";
	public static String QRCODEUNIFIEDPAY = BASE_URL + "qrCodeUnifiedPay";

	// 下单
	public static String BANKCARDQUICKPAY = BASE_URL + "bankCardQuickPay";
	public static String BANKCARDQUICKPAYCONFIRM = BASE_URL
			+ "bankCardQuickPayConfirm";
	public static String SMSRESEND = BASE_URL + "smsResend";

	public static String GETWITHDRAWCARDS = BASE_URL + "getWithDrawCards";

	// 绑卡
	public static String PAYBINDCARD = BASE_URL + "payBindCard";
	public static String FINDBINDCARDS = BASE_URL + "findBindCards";
	public static String GETPAYSUPPORTCARDS = BASE_URL + "getPaySupportCards";
	public static String GETBANKNAME = BASE_URL + "getBankName";
	public static String DELETEBINDCARDS = BASE_URL + "deleteBindCards";

	// 登录注册模块
	public static String URL_GET_VCODE = BASE_URL + "sendSMSVerification";
	public static String URL_REGISTER = BASE_URL + "userRegist";
	public static String URL_RETRIEVE_PWD = BASE_URL + "userFindPassword";
	public static String URL_LOGIN_NORMAL = BASE_URL + "userLogin";
	public static String URL_LOGIN_QUICK = BASE_URL + "userFastLogin";
	public static String URL_LOGIN_OTHER = BASE_URL + "user3rdLogin";
	public static String URL_UPDATE_USER = BASE_URL + "updateUserInfo";

	// 首页部分
	public static String URL_CATEGORY_LIST = BASE_URL
			+ "getShopTypeListByChild";
	public static String URL_HOME_PROD_SHOP = BASE_URL
			+ "getShopInfoListByHomePage";
	public static String URL_HOME_PROD = BASE_URL
			+ "getProductInfoListByHomePage";
	public static String URL_HOME_SHOP = BASE_URL + "getShopInfoListByHomePage";
	public static String URL_FLASH_SALE_PROD = BASE_URL
			+ "getProductInfoListByLimit";
	public static String URL_CITY_LIST = BASE_URL + "getCityInfoList";
	public static String URL_SEARCH_CITY = BASE_URL + "searchCity";
	public static String URL_PROD_FORMAT = BASE_URL + "getProductFormatList";

	// 商家模块
	public static String URL_SHOP_LIST = BASE_URL + "getShopList";
	public static String GETSHOPLISTNEW = BASE_URL + "getShopListNew";

	// 转让
	public static String INVESTMENTCONTROLLER = BASE_URL
			+ "getInvestmentConfigController";
	public static String GETPROJECTCONTROLLER = BASE_URL
			+ "InvestmentProjectController";
	public static String GETUSERORSHOPNAME = BASE_URL
			+ "getUserOrShopNameController";
	public static String GENERATECONTROLLER = BASE_URL
			+ "InvestmentGenerateController";
	public static String GENERATECONTROLLERNEW = BASE_URL
			+ "InvestmentGenerateNewController";// 产业链转让
	public static String INVESTMENTCONTROLLERLIST = BASE_URL
			+ "InvestmentController";// 转让记录

	public static String URL_SHOP_DETAIL = BASE_URL + "getShopDetails";
	public static String URL_SHOP_COMMENTS = BASE_URL + "getShopCommentList";
	public static String URL_SUBMIT_COMMENT = BASE_URL + "addShopComment";
	public static String URL_ADD_SHOP_WRONG = BASE_URL + "addShopWrong";
	public static String URL_RECEIVE_CARD = BASE_URL + "addShopAndUserCard";
	public static String URL_RECEIVE_COUPON = BASE_URL + "exchangeCoupon";
	public static String URL_SHOP_COUPON = BASE_URL + "getCouponListByShop";
	public static String URL_SHOP_ACTIV = BASE_URL + "getShopActiveList";
	public static String URL_JOIN_ACTIV = BASE_URL + "joinActive";
	public static String URL_SHOP_PHOTO = BASE_URL + "getShopPhotoInfoList";
	public static String URL_SEARCH_SHOP = BASE_URL + "getShopInfoListBySearch";
	public static String URL_PROD_TYPE = BASE_URL + "getProductTypeList";
	public static String FINDALLGOODSTYPELIST = BASE_URL
			+ "findAllGoodsTypeList";// 功能同上
	public static String URL_PROD_LIST = BASE_URL + "getProductList";
	public static String FINDGOODSLISTFORGOODTYPE = BASE_URL
			+ "findGoodsListForGoodType";// 功能同上
	public static String FINDGOODSTYPELISTBYSHOPID = BASE_URL
			+ "findGoodsTypeListByShopId";// 功能同上

	public static String URL_PROD_DETAIL = BASE_URL + "getProductInfoDetails";
	public static String URL_PROD_COMMENTS = BASE_URL + "getProductCommentList";
	public static String URL_CITY_ACTIV_LIST = BASE_URL
			+ "getShopActiveListByHomePage";
	public static String URL_ACTIV_TYPE_LIST = BASE_URL + "getActiveTypeList";
	public static String URL_COLLECT = BASE_URL + "addUserCollect";
	public static String URL_SHOP_QUICK_PAY = BASE_URL + "addQuickPay";

	// 生活圈
	public static String URL_ARTICLE_LIST = BASE_URL + "getLifeCircleList";
	public static String URL_ARTICLE_DETAIL = BASE_URL + "getLifeCircleDetails";
	public static String URL_ADD_ARTICLE = BASE_URL + "addLifeCircle";
	public static String URL_SEARCH_ARTICLE = BASE_URL + "searchLifeCircle";
	public static String URL_ARTICLE_COMMENTS = BASE_URL
			+ "getLifeCircleCommentList";
	public static String URL_ADD_COMMENT = BASE_URL + "addLifeCircleComment";
	public static String URL_ARTICLE_LIKE = BASE_URL + "addUserLikeLog";
	public static String URL_CIRCLE_LIST = BASE_URL + "circleList";
	public static String URL_CIRCLE_DETAIL = BASE_URL + "circleDetails";
	public static String URL_CIRCLE_JOIN = BASE_URL + "circleJoin";
	public static String URL_CIRCLE_QUIT = BASE_URL + "circleDel";
	public static String URL_CIRCLR_MY = BASE_URL + "myCircleList";
	public static String URL_CIRCLE_CREATE = BASE_URL + "circleCreate";
	public static String URL_CIRCLE_EDIT = BASE_URL + "circleUpdate";
	public static String URL_CIRCLE_FROM_IM = BASE_URL + "circleSearch";

	// 购物车
	public static String URL_ADD_TO_CART = BASE_URL + "addShopCar";
	public static String URL_CART_LIST = BASE_URL + "getShopCarList";
	public static String URL_UPDATE_CART = BASE_URL + "updateShopCar";

	// 订单模块
	public static String URL_ADD_PAY_LOG = BASE_URL + "addUserPayLog";
	public static String URL_CHECK_PROD_ZHEKOU = BASE_URL + "checkOrderProduct";
	public static String URL_ADD_ORDER = BASE_URL + "addOrderInfo";
	public static String URL_UPDATE_ORDER = BASE_URL + "updateOrderInfo";
	public static String URL_ORDER_LIST = BASE_URL + "getOrderInfoList";
	public static String URL_ORDER_DETAIL = BASE_URL + "getOrderInfoDetails";
	public static String URL_COMMENT_ORDER = BASE_URL + "addProductComment";
	public static String URL_ORDER_STATUS = BASE_URL + "getOrderHistoryList";

	// 个人中心模块
	public static String URL_GET_USERINFO = BASE_URL + "getUserInfoDetails";
	public static String URL_MY_COUPON = BASE_URL + "getCouponList";
	public static String URL_MY_CARDS = BASE_URL + "getShopAndUserCardList";
	public static String URL_RECHARGE_CONSUME_LOG = BASE_URL
			+ "getUserCardUseLogList";
	public static String URL_WALLET_RECHARGE_LOG = BASE_URL + "getUserPayList";
	public static String URL_WITHDRAW_LOG = BASE_URL + "getWithdrawalsLogsList";
	public static String URL_VALIDATE_PAY_PWD = BASE_URL
			+ "verificationPayPassword";
	public static String URL_WITHDRAW = BASE_URL + "addWithdrawalsLogs";
	public static String URL_EXCHANGE_LOG = BASE_URL + "getExchangeGiftLogList";
	public static String URL_POINT_LOG = BASE_URL + "getUserPointLogList";
	public static String URL_POINT_RULE = BASE_URL + "getPointList";
	public static String URL_FAVORITY_LIST = BASE_URL + "getUserCollectList";
	public static String URL_FIND_SHOP = BASE_URL + "addUserFindShop";
	public static String URL_FIND_SHOP_LIST = BASE_URL + "getUserFindShopList";
	public static String URL_SIGN = BASE_URL + "userSign";
	public static String URL_CHECK_VCODE = BASE_URL + "verificationMobile";

	// 定位
	public static String URL_CITY_BY_LATLNG = BASE_URL + "getAddress";
	public static String URL_AREA_LIST = BASE_URL + "getAreaInfoList";

	// 地址管理
	public static String URL_ADDR_LIST = BASE_URL + "getUserAddressList";
	public static String URL_ADDR_NEW = BASE_URL + "addUserAddress";
	public static String URL_ADDR_EDIT = BASE_URL + "updateUserAddressList";
	public static String URL_ADDR_DEL = BASE_URL + "delUserAddress";
	public static String URL_ADDR_DEFAULT = BASE_URL
			+ "getUserAddressByDefault";

	// 其他
	public static String URL_BANNER_LIST = BASE_URL + "getBannerList";
	public static String URL_UPLOAD_FILE = BASE_URL + "fileUpload";
	// public static String URL_UPLOAD_FILE = BASE_URL +
	// "weedfsFileUpload";//替换上面的地址

	public static String URL_WX_PAY_PARAMS = BASE_URL + "getWinxinPayParam";
	// public static String URL_NOTIFY_COUNT = BASE_URL + "getNoticeInfoCount";
	public static String NOTICEANDFEEDCOUNTCONTROLLER = BASE_URL
			+ "NoticeAndFeedCountController";
	public static String URL_SYS_MSG = BASE_URL + "getNoticeInfoList";
	public static String FEEDBACKLIST = BASE_URL + "FeedbackList";
	public static String URL_ADD_CONTACT = BASE_URL + "addShopAndUserIm";
	public static String URL_CONTACT_LIST = BASE_URL + "getShopAndUserImList";
	public static String URL_DEL_CONTACT = BASE_URL + "delShopAndUserIm";
	public static String URL_USER_IM_LIST = BASE_URL + "searchUserImList";
	public static String URL_VERSION_INFO = BASE_URL + "getVersion";
	public static String URL_FEEDBACK = BASE_URL + "addFeedback";
	public static String SEARCHBANK = BASE_URL + "BankCodeInfoController";

	public static String UPDATEUSERPOSITION = BASE_URL + "updateUserPosition";

	// WAP
	public static String URL_RECOMMEND = HOST
			+ "oneCity-wap/views/module/banner/wapDetails.html";
	public static String URL_ABOUT = HOSWAP + "views/page/client/about.html";
	public static String URL_BANNER_DETAIL = HOST
			+ "oneCity-wap/views/module/banner/bannerDetails.html";
	public static String URL_POINT_SHOP = HOST
			+ "oneCity-wap/views/page/client/pointShop.html";
	public static String URL_ACTIV_DETAIL = HOST
			+ "oneCity-wap/views/page/client/activeDetails.html";
	public static String URL_PROD_DETAIL_SHARE = HOST
			+ "oneCity-wap/views/page/client/productDetails.html";
	public static String URL_PROD_DETAIL_WAP = HOST
			+ "oneCity-wap/views/page/client/productDetailsByClient.html";
	// public static String URL_SHOP_INTRODUCE = HOST +
	// "oneCity-wap/views/page/client/shopDetailsByClient.html";
	public static String URL_SHOP_INTRODUCE = HOSWAP
			+ "views/page/client/shopDetailsByClient.html";
	public static String URL_CARD_DETAIL = HOST
			+ "oneCity-wap/views/page/client/userCardDetails.html";
	public static String URL_UNION_PAY = HOST
			+ "ACPSample_B2C/form_6_2_FrontConsume";
	public static String OILCARDRULE = HOSWAP
			+ "views/module/banner/oilCardRule.html";
	public static String URL_HELP = HOSWAP + "views/help/help.html";
//	public static String CUSTOMERSERVICE = "https://a1.7x24cc.com/phone_webChat.html?accountId=N000000009135&chatId=emxx-189ec300-cbd1-11e6-9a8a-d7e7516e91d6";
	public static String CUSTOMERSERVICE = HOST + "phone_webChat.html?accountId=N000000009135&chatId=emxx-189ec300-cbd1-11e6-9a8a-d7e7516e91d6";
	public static String MONEY_EXPLAIN = HOST + "static/html/moneyExplain.html";

	public static String INTEGRALPRO = HOST
			+ "static/html/integralExplain.html";

//	public static String car = "https://api.ermaucar.com/ucar/index.html?mobile=";
	public static String car = HOST + "ucar/index.html?mobile=";
	public static String carpay = HOST + "oneCity/unionPayNetCat/toIndex?userId=";
	// 58221

	// 分享
	public static String URL_SHARE_PROD = HOST
			+ "oneCity-wap/views/page/client/productDetails.html";
	public static String URL_SHARE_SHOP = HOST
			+ "oneCity-wap/views/page/client/shopDetails.html";
	// public static String URL_SHARE_APP = HOST +
	// "oneCity-wap/views/module/banner/share.html";

	public static String URL_SHARE_APP = wxHOST
			+ "oneCity/share/registerHtml?usertype=1";// 分享
//	public static String STUPLOAD_IMAGE = "https://v1-auth-api.visioncloudapi.com/resources/upload_image";// st
	public static String STUPLOAD_IMAGE = HOST + "resources/upload_image";// st
//	public static String SHOP = "http://m.qvs007.com/mobile/passport/api_login";
	public static String SHOP = HOST +"mobile/passport/api_login";


}
