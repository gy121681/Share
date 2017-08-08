package com.shareshenghuo.app.shop.networkapi;

import com.shareshenghuo.app.shop.BuildConfig;

public class Api {

	public static int SUCCEED = 0;

	// public static String HOST = "http://erma.h5h5h5.cn/";
	// public static String BASE_URL = HOST +
	// "oneCity-api-client-interface/oneCity/service/";

	// public static String HOST = "http://113.106.93.161:8081/";//新测试

	// public static String HOSTERMA = "https://testcity.qvs007.com/";// 测试
	// public static String HOST = "https://testapi.qvs007.com/";
	// public static String wxHOST = "http://testapi.qvs007.com/";
	
//	public static String HOSTERMA = "https://ermacity.qvs007.com/";
//	public static String HOST = "https://ermaapi.qvs007.com/";
//	public static String wxHOST = "http://ermaapi.qvs007.com/";// 正式环境
//	public static String HOSWAP = "https://ermawap.qvs007.com/";

	public static String URL = BuildConfig.HTTP_BASE_URL;//内网
	public static String IMG_HOST = BuildConfig.HTTP_IMAGE_BASE_URL;//图片服务器地址
	public static String HOSTERMA = IMG_HOST;
	public static String HOST = URL;
	public static String wxHOST = URL;// 正式环境
	public static String HOSWAP = BuildConfig.HTTP_WAP_URL;
	
	public static String BASE_URL = HOST + "oneCity/service/";

	public static String MONEY_EXPLAIN = HOST + "static/html/moneyExplain.html";// 秀点说明?

	// 转让
	public static String INVESTMENTCONTROLLER = BASE_URL + "getInvestmentConfigController";
	public static String GETPROJECTCONTROLLER = BASE_URL + "InvestmentProjectController";
	public static String GETUSERORSHOPNAME = BASE_URL + "getUserOrShopNameController";
	public static String GENERATECONTROLLER = BASE_URL + "InvestmentGenerateController";
	public static String GENERATECONTROLLERNEW = BASE_URL + "InvestmentGenerateNewController";// 产业链转让

	public static String GETBANNER = BASE_URL + "findSystemTotal";// 数据展示
	public static String GETBUNBER = BASE_URL + "findUserFilialPiety";// 分数展示
	public static String INTEGRALLIST = BASE_URL + "findIntegralLogList";// 积分列表
	public static String INTEGRALLISTNEW = BASE_URL + "findIntegralLogListNew";// 供应链积分列表
	public static String FRACTIONLIST = BASE_URL + "findFilialPietyLogList";// 秀心列表
	public static String FRACTIONLISTNEW = BASE_URL + "findFilialPietyLogListNew";// 供应链秀心列表
	public static String OBEDIENCELIST = BASE_URL + "findUserMoneyLogList";// 秀点列表
	public static String OBEDIENCELISTNEW = BASE_URL + "findUserMoneyLogListNew";// 秀点列表
	public static String NEWMEMBERELIST = BASE_URL + "findUserInfoList";// 会员列表
	public static String CONSUMPTIONLIST = BASE_URL + "findConsumptionSeries";// 消费系列
	public static String CONSUMPTIONLISTNEW = BASE_URL + "findConsumptionSeriesNew";// 消费系列
	public static String FINDSHOPPAYCLSINFLIST = BASE_URL + "findShopPayclsInfList";// 营业额
	public static String FINDSHOPPAYCLSINFLISTNEW = BASE_URL + "findShopPayclsInfListNew";// 营业额
	public static String GENERCODELIST = BASE_URL + "genErcode";// 二维码
	public static String GENERCODELISTNew = BASE_URL + "genErcodeNew";// 二维码
	public static String FINDDAYPAYLIST = BASE_URL + "findDayPayList";// 营业额
	public static String FINDDAYPAYLISTNEW = BASE_URL + "findDayPayListNew";// 营业额
	public static String USERWITHDRAW = BASE_URL + "userWithDraw";// 兑换
	public static String USERWITHDRAW1 = BASE_URL + "multipleWithDraw";// 兑换
	public static String USERWITHDRAW1NEW = BASE_URL + "multipleWithDrawNew";// 兑换
	public static String ACCOUNTINFOQUERY = BASE_URL + "userAccountInfoQuery";// 兑换前置查询
	public static String GENEROLIST = BASE_URL + "genericOtherInfoList";// 兑换配置
	public static String GENEROLISTNEW = BASE_URL + "genericOtherInfoListNew";// 兑换配置
	public static String SETPAYPASSWORD = BASE_URL + "setPayPassword";// 设置支付密码
	public static String UPDATEPAYPASSWORD = BASE_URL + "updatePayPassword";// 修改支付密码
	public static String WITHDRAWLISTQUERY = BASE_URL + "withDrawListQuery";// 兑换记录
	public static String WITHDRAWSINGLEQUERY = BASE_URL + "withDrawSingleQuery";// 兑换详情
	public static String GETSHOPDETAILS = BASE_URL + "getShopDetails";
	public static String GETSHOPPHOTOINFOLIST = BASE_URL + "getShopPhotoInfoList";
	public static String BANKLIST = BASE_URL + "tfSupportBank";// 银行列表
	public static String FICATION = BASE_URL + "userIdentification";// 实名认证
	public static String USERCARDNOCHANGE = BASE_URL + "userCardNoChange";// 银行卡
	// public static String RECOMMENDEDMEMBER = BASE_URL+"findUserInfoList";//推荐

	public static String SHOPMANAGE = BASE_URL + "findGoodsList";// 商品管理
	public static String SHOPCATEGORY = BASE_URL + "findGoodsTypeListByShopId";// 商品分类
	public static String ADDSHOPCATEGORY = BASE_URL + "addGoodsType";// 商品分类
	public static String ADDGOODS = BASE_URL + "addGoods";// 商品分类
	public static String GETGOODSINFOBYID = BASE_URL + "getGoodsInfoById";// 商品分类
	public static String UPDATEGOODS = BASE_URL + "updateGoods";// 修改
	public static String FINDGOODSTYPEGOODSLIST = BASE_URL + "findGoodsTypeGoodsList";// 分类下商品
	public static String DELETEGOODSTYPE = BASE_URL + "deleteGoodsType";// 删除分类
	public static String UPDATEGOODSTYPE = BASE_URL + "updateGoodsType";// 修改分类
	public static String SETGOODSTYPESORT = BASE_URL + "setGoodsTypeSort";// 排序分类
	public static String BATCHDELETEGOODSBYIDS = BASE_URL + "batchDeleteGoodsByIds";// 排序分类
	public static String SETGOODSSORT = BASE_URL + "setGoodsSort";// 排序商品
	public static String BATCHTYPEGOODS = BASE_URL + "batchTypeGoods";// 分类商品
	public static String FINDALLGOODSTYPELIST = BASE_URL + "findAllGoodsTypeList";// 分类商品

	public static String GETSHOPDIFFERENTPHOTOTYPELIST = BASE_URL + "getShopDifferentPhotoTypeList";// 图片列表
	public static String GETSHOPINFO = BASE_URL + "getShopInfo";// 店铺信息
	public static String UPDATESHOPINFO = BASE_URL + "updateShopInfo";// 店铺上传

	// 下单
	public static String BANKCARDQUICKPAY = BASE_URL + "bankCardQuickPay";
	public static String BANKCARDQUICKPAYCONFIRM = BASE_URL + "bankCardQuickPayConfirm";
	public static String SMSRESEND = BASE_URL + "smsResend";

	// 充值
	public static String QUERYSHOPINFOCONTROLLER = BASE_URL + "QueryShopInfoController";
	public static String FENFUPAYGETPARAM = BASE_URL + "FenfuPayGetParam";
	public static String QRCODEUNIFIEDPAY = BASE_URL + "qrCodeUnifiedPay";
	// 绑卡
	public static String PAYBINDCARD = BASE_URL + "payBindCard";
	public static String FINDBINDCARDS = BASE_URL + "findBindCards";
	public static String GETPAYSUPPORTCARDS = BASE_URL + "getPaySupportCards";
	public static String GETBANKNAME = BASE_URL + "getBankName";

	// 绑卡
	public static String DELETEBINDCARDS = BASE_URL + "deleteBindCards";

	// public static String SHOPTYPELIST =
	// BASE_URL+"findGoodsTypeListByShopId";//商品分类

	public static String URL_SHARE_APP = wxHOST + "oneCity/share/registerHtml?usertype=2";// 分享

	// http://ermaapi.qvs007.com/oneCity/share/registerHtml?usertype=用户类型&sign=手机号加密
	public static String GETANNOUNCEMENT = BASE_URL + "getAnnouncement";// 公告
	public static String URL_HELP = HOSWAP + "views/shophelp/help.html";

	// 定位
	public static String URL_CITY_BY_LATLNG = BASE_URL + "getAddress";
	public static String URL_AREA_LIST = BASE_URL + "getAreaInfoList";

	public static String URL_LOGIN = BASE_URL + "shopLogin";
	public static String URL_GET_VCODE = BASE_URL + "sendSMSVerification";
	public static String URL_BIND_MOBILE = BASE_URL + "shopBandMobile";
	public static String URL_RETRIEVE_PWD = BASE_URL + "shopFindPassword";
	public static String URL_RECHAR_CONSUME = BASE_URL + "addUserCardUseLog";
	public static String URL_BANNER_LIST = BASE_URL + "getBannerList";
	public static String URL_PROD_TYPE = BASE_URL + "getProductTypeList";
	public static String URL_PROD_LIST = BASE_URL + "getProductList";
	public static String URL_VIP_LIST = BASE_URL + "getUserListByShop";
	public static String URL_CARD_USE_LOG = BASE_URL + "getUserCardUseLogList";
	public static String URL_STATISTIC_LIST = BASE_URL + "getStatistics";
	public static String URL_STATISTIC_CONSUME = BASE_URL + "getRechargeStatistics";
	public static String URL_STATISTIC_COUPON = BASE_URL + "getRechargeStatisticsByCoupon";
	public static String URL_STATISTIC_CONSUME_DETAIL = BASE_URL + "getRechargeStatisticsDetails";
	public static String URL_STATISTIC_COUPON_DETAIL = BASE_URL + "getRechargeStatisticsDetailsByCoupon";
	public static String URL_STATISTIC_VIP = BASE_URL + "getRechargeStatisticsByCollect";
	public static String URL_SHOP_DETAIL = BASE_URL + "getShopDetailsById";
	public static String URL_UPLOAD_FILE = BASE_URL + "fileUpload";
	public static String URL_UPDATE_SHOP = BASE_URL + "updateShop";
	public static String URL_INCOME_LOG = BASE_URL + "getShopIncomeList";
	public static String URL_WITHDRAW = BASE_URL + "addWithdrawalsLogs";
	public static String URL_ORDER_LIST = BASE_URL + "getOrderInfoListByShop";
	public static String URL_UPDATE_ORDER_STATUS = BASE_URL + "UpdateOrderInfoByShop";
	public static String URL_NOTIFY_COUNT = BASE_URL + "getNoticeInfoCount";
	public static String URL_SYS_MSG = BASE_URL + "getNoticeInfoList";
	public static String URL_ADD_CONTACT = BASE_URL + "addShopAndUserIm";
	public static String URL_CONTACT_LIST = BASE_URL + "getShopAndUserImList";
	public static String URL_DEL_CONTACT = BASE_URL + "delShopAndUserIm";
	public static String URL_ADD_COMMENT = BASE_URL + "addLifeCircleComment";
	public static String URL_USER_IM_LIST = BASE_URL + "searchUserImList";
	public static String URL_PUSH_MSG = BASE_URL + "addPushHistory";
	public static String URL_USER_LEVEL = BASE_URL + "getUserLevel";
	public static String URL_PUSH_LOG = BASE_URL + "getPushHistoryList";
	public static String URL_FEEDBACK = BASE_URL + "addFeedback";
	public static String URL_VERSION_INFO = BASE_URL + "getVersion";
	public static String URL_CHECK_VCODE = BASE_URL + "verificationMobile";
	public static String URL_CALC_DISCOUNT = BASE_URL + "getZheKou";
	public static String SEARCHBANK = BASE_URL + "BankCodeInfoController";
	public static String UPDATEBANK = BASE_URL + "UpdateShopBankController";

	public static String URL_ABOUT = HOSWAP + "views/page/client/about.html";
	public static String FEEDBACKLIST = BASE_URL + "FeedbackList";
	public static String NOTICEANDFEEDCOUNTCONTROLLER = BASE_URL + "NoticeAndFeedCountController";
//	public static String CUSTOMERSERVICE = "https://a1.7x24cc.com/phone_webChat.html?accountId=N000000009135&chatId=emxx-189ec300-cbd1-11e6-9a8a-d7e7516e91d6";
	public static String CUSTOMERSERVICE = HOST + "phone_webChat.html?accountId=N000000009135&chatId=emxx-189ec300-cbd1-11e6-9a8a-d7e7516e91d6";

	// 录单
	public static String GETSHOPORDERDETAILS = BASE_URL + "getShopOrderDetail";
	public static String GETSHOPORDERDETAILSNEW = BASE_URL + "getShopOrderDetailNew";
	public static String RISKCHECK = BASE_URL + "riskCheck";
	public static String RISKCHECKNEW = BASE_URL + "riskCheckNew";

	public static String GETUSERINFOBYACCOUNTS = BASE_URL + "getUserInfoByAccount";
	public static String ADDSHOPPAYCLSINFS = BASE_URL + "addShopPayclsInf";
	public static String ADDSHOPPAYCLSINFSNEW = BASE_URL + "addShopPayclsInfNew";
	public static String SHOPPAYCLSINFLISTS = BASE_URL + "shopPayclsInfList";
	public static String SHOPPAYCLSINFLISTSNEW = BASE_URL + "shopPayclsInfListNew";
	public static String DELETESHOPPAYCLSINFBYIDS = BASE_URL + "deleteShopPayclsInfById";
	public static String DELETESHOPPAYCLSINFBYIDSNEW = BASE_URL + "deleteShopPayclsInfByIdNew";

	// 商家收款
	public static String PAYCHANNELS = BASE_URL + "payPlatform_payChannels";
	public static String POINTSLST = BASE_URL + "payPlatform_pointsLst";
	public static String WECHATPAYD = BASE_URL + "payPlatform_wechatPayD";
	public static String ALIPAYD = BASE_URL + "payPlatform_aliPayD";
	public static String BALANCEPAY = BASE_URL + "payPlatform_balancePay";
	public static String QUICKPAY = BASE_URL + "payPlatform_quickPay";

	public static String INVESTMENTCONTROLLERLIST = BASE_URL + "ReceiveInvestmentController";// 转让记录

	// public static String DOWNTMK = BASE_URL+"payPlatform_downTMK";
	// public static String DOWNWK = BASE_URL+"payPlatform_downWK";
	// public static String POSPAY = BASE_URL+"payPlatform_posPay";

//	public static String nhost = "http://113.106.93.161:8088/";
	public static String nhost = HOST;
	public static String ntran = "mng/";
	public static String DOWNTMK = nhost + ntran + "199105.tran";
	public static String DOWNWK = nhost + ntran + "199020.tran";
	public static String POSPAY = nhost + ntran + "199005.tran";

	public static String ELESIGN = BASE_URL + "payPlatform_eleSign";

	// -----------------------------------测试省市区
	public static String AREAQUERY = BASE_URL + "areaQuery";// 省市区

	// 我的分红权,秀心购买,入股
	public static String GETXFRGXMLIST = "UserMoneyInvestmentProjectListController";// 获取秀点入股项目列表
	public static String GETXXRGXMLIST = "FilialInvestmentProjectListController";// 获取秀心入股项目列表

	public static String GETXX_XF_LIST = BASE_URL + "GetInvestmentListController";// 获取秀心秀点流水

	public static String BUYXX = "FilialInvestmentController";// 购买秀心
	public static String BUYXF = "UserMoneyInvestmentController";// 购买秀点
	public static String test_utl = wxHOST + "oneCity/service/";// 仅供测试。项目投资

}
