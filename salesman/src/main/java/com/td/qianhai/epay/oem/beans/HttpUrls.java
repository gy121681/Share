package com.td.qianhai.epay.oem.beans;

import com.td.qianhai.epay.oem.BuildConfig;

import java.io.Serializable;

public class HttpUrls implements Serializable {
	// public static final String HOST = "http://180.166.124.95:8092/posm/"; //
	// 生产环境
//	public static final String APPURL = "https://download.qhno1.com/ermapay.html";

	public static final String APPNAME = "秀儿支付";
	// public static final String APPURL =
	// "https://download.qhno1.com/down.html";
	// public static final String APPNAME = "钱海钱包";

	// public static final String APPURL=
	// "http://180.166.124.95:8092/posm/upload/QH_W_V1.6.apk";

	// public static final String APPNUM = "android_qh_1.0";//测试
	public static final String APPNUM = "android_empay_1.0";// 生产
	// public static final String APPNUM = "android_wndz_1.0";//为你定制

	// 测试
	// public static final String HOST = "http://119.147.71.133:30147/mpay/";
	// public static final String HOST_POSP =
	// "http://119.147.71.133:30147/mpay/";
	// public static final String HOST_PAY =
	// "http://119.147.71.133:30147/mpay/";
	// public static final String HOST_POSM =
	// "http://119.147.71.133:30147/posm/";
	// public static final String JAVAPAY_PAY = "http://120.25.57.194:8080/";
	//
	// public static final String SUFFIX = ".tran7";
	// public static final String SUFFIX_POSP = ".tran";
	// public static final String SUFFIX_MIDATC = ".tran7";
	// //
	// //测试
	// public static final String DISTRIBUTOR =
	// "http://120.25.57.194:8080/pmagt/oemRole/";
	// public static final String MEMBERUPGRADE =
	// "http://120.25.57.194:8080/pmagt/oemMember/";
	// public static final String MEMBERUPGRADESUFFIX = "?sign=";
	// public static final String MYPROFIT =
	// "http://120.25.57.194:8080/pmagt/oemRole/incomeList?sign=";
	// public static final String UNDERLINGURL =
	// "http://120.25.57.194:8080/mer/findMerByAgentId?"; //所属下级代理
	// public static final String RECOMMENDATION =
	// "http://120.25.57.194:8080/mer/recommandDetail?";
	// public static final String PROXYRETURN =
	// "http://120.25.57.194:8080/mer/shareDetail?";
	// public static final String MALL =
	// "http://172.168.30.112:8080/app/login?sign=";//商城
	// public static final String TOAVERTISING =
	// "http://120.25.57.194:8080/agtMessage/manageIndex?sign=";//广告发布
	// public static final String WECHATLIST =
	// "http://120.25.57.194:8080/ercode/getErcodeOrderList?";//总店
	// public static final String WECHATLISTDTL =
	// "http://120.25.57.194:8080/ercode/getErcodeOrderDtlList?";//总店详情
	// public static final String SHEARURL =
	// "http://120.25.57.194:8080/common/dow?sign=";
	// public static final String WECHATRECEIVE1 =
	// "http://120.25.57.194:8080/skb/bindReceiveLogin";//个人收款宝
	// public static final String SCREENINGURL =
	// "http://120.25.57.194:13020/qrcode/genErcode";//二维码收款
	// public static final String SKBDIRECTLOGIN =
	// "http://120.25.57.194:8080/skb/skbDirectLogin";//收款通用
	// public static final String SMALLTICKETLIST =
	// "http://120.25.57.194:8080/common/printOrderList";
	// public static final String TICKETLIST =
	// "http://120.25.57.194:8080/common/findOrderList";//小票列表
	// public static final String NEWPAY =
	// "http://120.25.57.194:8080/ffPay/payeeCardPay";//
	// public static final String NEWADDPAY =
	// "http://120.25.57.194:8080/ffPay/authenCardcode";//
	// public static final String TONEWPAY =
	// "https://mapi.sumapay.com/unionpayMobile/merchant.do";//

	// 新生产
//	public static final String HOST_POSM = "http://mng.qhno1.com/";
//	public static final String HOST = "http://mpay.qhno1.com/";
//	public static final String HOST_POSP = "http://mpay.qhno1.com/";
//	public static final String HOST_PAY = "http://mpay.qhno1.com/";
//	public static final String JAVAPAY_PAY = "http://javapay.qhno1.com/";
//
//	//原代码中的http地址
//	public static final String API ="http://ermaapi.qvs007.com/";//
//	public static final String QVS007 = "http://www.qvs007.com/";//
//	public static final String WAP_QIANHAIHG= "http://wap.qianhaihg.com/";
//	public static final String CITY = "http://ermacity.qvs007.com/";//图片


//	public static String URL = "http://api.shareshenghuo.com/xiuer-api-client-interface/";//内网
//	public static String IMG_HOST = "http://file.shareshenghuo.com/";//图片服务器地址

//    public static String URL = "http://192.167.1.12:9000/xiuer-api-client-interface/";//内网
//    public static String IMG_HOST = "http://192.167.1.12:9000/xiuer_upload/";//图片服务器地址
//	public static String WAP_HOST = "http://192.167.1.12:9000/xiuer-wap/";

    public static String URL = BuildConfig.HTTP_BASE_URL;//内网
    public static String IMG_HOST = BuildConfig.HTTP_IMAGE_BASE_URL;//图片服务器地址
	public static String WAP_HOST = BuildConfig.HTTP_WAP_URL;

//    public static String URL = "http://test.shareshenghuo.com:8081/xiuer-api-client-interface/";//内网
//    public static String IMG_HOST = "http://test.shareshenghuo.com:8081/xiuer_upload/";//图片服务器地址
	// 新生产
	public static final String HOST_POSM = URL;
	public static final String HOST = URL;
	public static final String HOST_POSP = URL;
	public static final String HOST_PAY = URL;
	public static final String JAVAPAY_PAY = WAP_HOST;

	//原代码中的http地址
	public static final String API = URL;//
	public static final String QVS007 = URL;//
	public static final String WAP_QIANHAIHG= URL;
	public static final String CITY = IMG_HOST;//图片

//	public static final String BASE = "http://192.167.1.12:8080/xiuer-api-client-interface/";
//	public static String IMG_HOST = "http://192.167.1.12:8080/erma_upload/";//图片服务器地址
//	public static final String API = BASE;//
//	public static final String QVS007 = BASE;//
//	public static final String WAP_QIANHAIHG= BASE;
//	public static final String CITY = IMG_HOST;//图片


//	// 新生产
//	public static final String HOST_POSM = "http://mng.qhno1.com/";
//	public static final String HOST = "http://mpay.qhno1.com/";
//	public static final String HOST_POSP = "http://mpay.qhno1.com/";
//	public static final String HOST_PAY = "http://mpay.qhno1.com/";
//	public static final String JAVAPAY_PAY = "http://javapay.qhno1.com/";
//
//	//将原代码中的http地址抽取出来
//	public static final String API ="http://192.167.1.12:8080/oneCity-api-client-interface/";// 正式
//	public static final String QVS007 = "http://192.167.1.12:8080/yewu-wap/";//商城？
//	public static final String WAP_QIANHAIHG= "http://192.167.1.12:8080/yewu-wap/";
//	public static final String CITY = "http://192.167.1.12:8080/erma_upload/";//图片

	/** 秀儿——消费界面 */
	public static final String SHARE_WEB_CONSUME = URL + "oneCity/salesman/index?SALESMANID=";
    /** 我的商户——商户界面 */
    public static final String SHARE_WEB_TENANT =  BuildConfig.HOST+ "xiuer-yewu-wap/page/client/orderList.html?id=";
    /** 我的商户——营业额 */
    public static final String SHARE_WEB_COMMERICIAL = URL + "oneCity/salesman/turnover?SALESMANID=";
    /** 我的收益 */
    public static final String SHARE_WEB_LUCRE = URL + "oneCity/salesman/userMoneyLog?SALESMANID=";
    /** 服务中心 */
    public static final String SHARE_WEB_SERVICES_CENTER = URL + "oneCity/serviceCenter/attract";



	public static final String SUFFIX = ".tran7";
	public static final String SUFFIX_POSP = ".tran";
	public static final String SUFFIX_MIDATC = ".tran7";

	// 生产
	public static final String DISTRIBUTOR = JAVAPAY_PAY + "pmagt/oemRole/";
	public static final String MEMBERUPGRADE = JAVAPAY_PAY + "pmagt/oemMember/";
	public static final String MEMBERUPGRADESUFFIX = "?sign=";
	public static final String MYPROFIT = JAVAPAY_PAY
			+ "pmagt/oemRole/incomeList?sign=";
	public static final String UNDERLINGURL = JAVAPAY_PAY + "mer/findMerByAgentId?"; // 所属下级代理
	// public static final String MALL =
	// "http://192.168.18.103:8080/app/login?sign=";//商城

	public static final String MALL = QVS007 + "app/login?sign=";// 商城
	public static final String PROXYRETURN = JAVAPAY_PAY + "mer/shareDetail?";
	public static final String RECOMMENDATION = JAVAPAY_PAY + "mer/recommandDetail?";
	public static final String TOAVERTISING = JAVAPAY_PAY + "agtMessage/manageIndex?sign=";// 广告发布
	public static final String SHEARURL = JAVAPAY_PAY + "common/dow?sign=";
	public static final String WECHATLISTDTL = JAVAPAY_PAY + "ercode/getErcodeOrderDtlList?";// 总店详情
	public static final String WECHATRECEIVE1 = JAVAPAY_PAY + "skb/bindReceiveLogin";// 个人收款宝
	// public static final String SCREENINGURL =
	// QVS007 + "qrcode/genErcode";
	public static final String SCREENINGURL = QVS007 + "qrcode/genErcodeMacthAll";
	public static final String WECHATLIST = JAVAPAY_PAY + "ercode/getErcodeOrderList?";// 微信流水
	public static final String CERTIFICATE = JAVAPAY_PAY + "html/emzf/certificate.html";// 资质证明
	public static final String HELP = JAVAPAY_PAY + "html/emzf/operationGuide.html";// 操作指南
	public static final String COMMONPROBLEM = JAVAPAY_PAY + "html/emzf/help/index.html";// 常见问题
	public static final String CFINFO = JAVAPAY_PAY + "html/emzf/wallet.html";// 简介
	public static final String REGISTRATIONAGREEMENT = JAVAPAY_PAY + "html/emzf/serviceAgreement.html";// 注册协议
	public static final String CHARGERULE = JAVAPAY_PAY + "html/emzf/tollRule.html";// 收费规则
	public static final String SCREENINGURLFA = QVS007 + "qrcode/saveQrcodeShopName";
	public static final String HG_URL = WAP_QIANHAIHG + "?/mobile/user/qhepaylogin/";
	public static final String GOWECHATRECEIVE = JAVAPAY_PAY + "mer/updateYeeCustomerInf";// 个人收款
	public static final String WECHATRECEIVE = QVS007 + "wechatReceive/genErcode";
	public static final String WECHATHEADQUARTERSLIST = JAVAPAY_PAY + "ercode/getErcodeOrderList?";// 总店
	public static final String SKBDIRECTLOGIN = JAVAPAY_PAY + "skb/skbDirectLogin";// 收款通用
	public static final String SMALLTICKETLIST = JAVAPAY_PAY + "common/printOrderList";// 小票列表
	public static final String ALIPAYLIST = JAVAPAY_PAY + "aliPay/queryMainShopRecord?";// 支付宝流水
	public static final String ALIPAYBRANCHLIST = JAVAPAY_PAY + "aliPay/queryPayMonthRecord?";// 分店记录
	public static final String ALIPAYBRANCHDAYLIST = JAVAPAY_PAY + "aliPay/queryPayDayRecord?";// 天记录
	public static final String SHOPSUM = JAVAPAY_PAY + "aliPay/queryMainShopSum";//
	public static final String BRANCHMANAGEMENT = JAVAPAY_PAY + "aliPay/querySubShopList?";// 分店管理
	public static final String NEWBRANCH = JAVAPAY_PAY + "aliPay/addSubShop";// 新建分店
	public static final String TICKETLIST = JAVAPAY_PAY + "common/findOrderList";// 小票列表
	public static final String VADIOURL = JAVAPAY_PAY + "aliPay/shopsReturn";// 视频

	public static final String NEWPAY = JAVAPAY_PAY + "ffPay/payeeCardPay";//
	public static final String NEWADDPAY = JAVAPAY_PAY + "ffPay/authenCardcode";//

    public static final String MOREMENUACTIVITY_HELP1 = JAVAPAY_PAY + "html/emzf/help/personalPay.html";
    public static final String MOREMENUACTIVITY_HELP2= JAVAPAY_PAY + "html/emzf/help/merchantPay.html";
    public static final String MOREMENUACTIVITY_HELP3 = JAVAPAY_PAY + "html/emzf/help/memberManage.html";

	public static final String NEWVIP = JAVAPAY_PAY
			+ "pmagt/serviceManagement?sign=";

	// public static final String BUSINESS =
	// API + "oneCity/salesman/salesmanLogin?sign=";//
	// public static String NEWHOST = API;
	// public static String BASE_URL = NEWHOST + "collect/shop/";

	public static String NEWHOST = API;// 正式
	// public static String NEWHOST = TEST_API +"";//测试

	public static String BASE_URL = NEWHOST + "collect/shop/";
	// public static String HOSTERMA = "http://testcity.qvs007.com/";//测试
	public static String HOSTERMA = CITY;// 正式
	//public static final String BUSINESS = TEST_API +"oneCity/salesman/salesmanLogin?sign=";//
	public static final String BUSINESS = API + "oneCity/salesman/salesmanLogin?sign=";//

	public static String ADDSHOP = BASE_URL + "add";// 添加商户
	public static String TYPELIST = NEWHOST + "industry/list";// 行业
	public static String UPDATESHOP = BASE_URL + "update";// 修改商户
	public static String PROVINCE = NEWHOST + "province/list";// 省
	public static String CITYINFO = NEWHOST
			+ "oneCity/service/getCityInfoListByWap";// 市
	public static String AREALIST = NEWHOST + "oneCity/service/getAreaInfoList";// 区
	public static String GETSHOPTYPELIST = NEWHOST
			+ "oneCity/service/getShopTypeList";// 二级
	public static String URL_UPLOAD_FILE = URL + "oneCity/service/fileUpload";
	public static String TFSUPPORTBANK = URL+ "oneCity/service/tfSupportBank";
//	public static String SEARCHBANK = TEST_API
//			+ "oneCity/service/BankCodeInfoController";//测试
	
	public static String SEARCHBANK = API
			+ "oneCity/service/BankCodeInfoController";
	
	public static String DETAILS = BASE_URL + "details";//

	public static String GETAGENTINFO = NEWHOST
			+ "oneCity/service/getAgentInfo";//
	public static String FINDAGENTTRADE = NEWHOST
			+ "oneCity/service/findAgentTrade";//
	public static String FINDAGENTSHOPNUM = NEWHOST
			+ "oneCity/service/findAgentShopNum";//
	public static String AREAQUERY = NEWHOST + "oneCity/service/areaQuery";
	public static String VIEWAGENTTRADE = NEWHOST
			+ "oneCity/service/viewAgentTrade";

	public static String TEST2 = NEWHOST + "";//
	public static String TEST3 = NEWHOST + "";//

	public static final String SUFFIX_UPLOAD = ".upload4m";

	public static final String SUFFIX_UPLOAD_J = ".upload4j";

	public static final String SUFFIX_MID = ".tran8";

	public static final String SUFFIX_CIR = ".tran6";

	// 终端号验证
	public static final int LOCALYZZD = 198107;

	public static final int ORDERPAYDETAIL = 199032;
	// 订单查询
	public static final int ORDERPAYQUERY = 199031;
	// 订单支付
	public static final int ORDERPAY = 199030;
	// 注册
	public static final int REGISTER = 199001;

	// 登录
	public static final int LOGIN = 199002;

	// (钱包)致富宝详细信息
	public static final int RICH_TREASURE_INFO = 701122;
	// 支付密码设置
	public static final int SET_PAY_PASS = 701120;
	// 支付密码修改
	public static final int UP_PAY_PASS = 701121;
	// 致富宝账单明细
	public static final int RICH_TREASURE_DETAIL = 701123;

	// 支取致富宝到银行卡
	public static final int WITHDRAWAL_ON_BANK_CARD = 701131;

	/**
	 * 存入定期
	 */
	public static final int BASIS_DEPOSIT = 701132;

	/**
	 * 存款模式列表
	 */
	public static final int BASIS_LIST = 701126;

	// 定期信息列表
	public static final int REGULAR_BASIS_LIST = 701124;

	/**
	 * 定转活
	 */
	public static final int CURRENT_TRANSFER = 701125;

	/**
	 * 找回支付密码验证码
	 */
	public static final int REG_PAY_PW_VCODE = 701493;
	/**
	 * 找回支付密码验证码验证
	 */
	public static final int REG_PAY_PW_VCODE_VD = 701494;

	/**
	 * 支付密码修改
	 */
	public static final int PAY_UPDATE = 701497;

	// 验证码验证
	public static final int VERIF_COMMIT = 198116;

	// 获取找回密码验证码
	public static final int REGET_PW_VERIF = 198115;

	// 修改密码交易码
	public static final int REGET_PASSWORD = 198117;

	// 修改密码
	public static final int REVISE_PASSWORD = 199003;

	// 刷卡查询卡银行名
	public static final int QUERY_BANK_NAME = 199104;

	// 银行卡开户城市
	public static final int QUERY_BANK_CITY = 199108;

	// 银行卡开户支行
	public static final int QUERY_BANK_BRANCH = 199109;

	// // 查看商户信息
	public static final int BUSSINESS_INFO = 199102;

	// 实名认证
	public static final int REAL_NAME_AUTHENTICATION = 199101;

	// 获取注册验证码
	public static final int REGISTER_VERIFCODE = 199018;

	// 修改开户银行信息
	public static final int REVISE_BANK_INFO = 199103;

	// 老板收款
	public static final int BOSS_RECEIVE = 199005;

	// 消费撤销
	public static final int REVOKE_PAY = 199006;

	// 银行卡余额查询
	public static final int BALANCE_QUERY = 199007;

	// 流水查询
	public static final int DEAL_RECORDS = 199008;

	// 主秘钥下载
	public static final int KEY_DOWNLOAD = 199105;

	// 上传电子签名
	public static final int UPLOAD_SIGN = 199107;

	// 签到
	public static final int CHECK = 199020;

	// // 版本检测
	public static final int VERSION_UPDATE = 199021;

	// 下支付订单
	public static final int ORADSEQ_PAY = 203015;

	// 更新支付订单流水状态
	public static final int ORADSEQ_UPDATE = 203016;
	// 银行卡信息查询
	public static final int QUERY_BANK_INFO = 203017;
	// 查询是否注册
	public static final int IS_REGISTER = 203025;
	// 申请银行卡信息修改
	public static final int APPLY_BANKCARD_INFO_CHANGES = 198101;
	// 申请修改商户实名认证
	public static final int APPLY_BANKINFO_REALNAME_CHANGES = 198102;
	// 商户申请手机号码修改
	public static final int APPLY_PHONENUMBER_CHANGES = 198109;
	// 申请修改商户手机实名认证
	public static final int APPLY_PHONENUMBER_REALNAME_CHANGES = 198110;
	// 商户申请手机号码修改获取新手机验证码
	public static final int QUERY_PHONENUMBER_CHANGES_VERIFCODE = 198220;

	// 创建订单
	public static final int ORDER_CREATE = 701613;

	// 订单查询单
	public static final int ORDER_QUERY = 701614;

	// 已付订单查询
	public static final int ORDER_QUERY_OVER = 701622;

	// 订单查询多
	public static final int ORDER_QUERY_D = 701615;// /////////////////// . . .
													// . .

	// 订单支付
	public static final int ORDER_PAY = 701616;

	public static final int TANSFER_ACCO = 701129;

	public static final int RATE_QUERY = 701709; // 费率列表

	public static final int GO_RATE = 701708;

	public static final int RECHARGE = 701640;

	public static final int PHONE_CHARGE = 701639;

	public static final int ORDERINFO = 701162;

	public static final int MYCIRCLE = 701720;

	// public static final int CREDITQUERY = 701647;

	public static final int CREDITQUERY = 702047;

	// public static final int CREDITPAY = 701646;

	public static final int CREDITPAY = 702044;

	public static final int GETBANKNAME = 701649;

	public static final int BINDINGCARD = 701648;

	// public static final int DELETECREDITCARD = 701650;
	public static final int DELETECREDITCARD = 702049;
	// 分组推送
	public static final int GROUPING = 701722;
	// 易宝
	public static final int YIBAO = 701723;

	public static final int EPAY = 701725;

	public static final int EPAYNUM = 701726;

	public static final int GETEPAYNUM = 701727;

	public static final int ACTIVATIONCODE = 000000;

	public static final int BANKCARDLIST = 701730;

	public static final int UPLODPIC = 701734;

	public static final int NEWAGENT = 701191;
	//
	public static final int ACTCODEMANAGE = 701194;

	public static final int CHIRDACT = 701192;

	public static final int VERIFICATIONPHONENUM = 701190;

	public static final int ACTCODEINIT = 701195;

	public static final int AGENTINFO = 701197;

	public static final int ACTCODELIST = 701198;

	public static final int ACTCODETRANSFER = 701196;

	public static final int RATEEDIT = 701193;

	public static final int FEEDBACK = 701973;

	public static final int INCOMEDETAIL = 701817;

	public static final int DEPOSITPURSE = 701816;

	public static final int PROMOTIONEARNINGS = 701686;

	public static final int RATEWITHDRAWALSEDIT = 701819;

	public static final int FINANCIALPRODUCTS = 702131;

	public static final int TOCHANGEINTO = 702128;

	public static final int TURNOUT = 702129;

	public static final int INTEGRALEXCHANGE = 702132;

	public static final int EXCHANGEINFO = 702133;

	public static final int TRANSACTIONTYPE = 702116;

	public static final int EPAYSAV = 701723;

	public static final int UNBINDCARD = 702149;

	public static final int UPDATEBANKCARD = 702148;

	public static final int BUYCODE = 702117;

	public static final int BINDAGENT = 702143;

	public static final int COMITADRESS = 701835;

	public static final int AVATARUPLOAD = 701997;

	public static final int CHOOSEBANK = 702005;

	public static final int BANNER = 701998;

	public static final int COLLECTIONTREASURE = 702007;

	public static final int REALNAMEAUTHENTICATION = 702008;

	public static final int INTHEQUERY = 703001;

	public static final int CODETRANSFER = 702016;

	public static final int OWNERLICENSEPLATE = 702040;

	public static final int OWNERLICENSEPLATE1 = 702041;

	public static final int UNBUNDLING = 702042;

	public static final int ETCLIST = 702043;

	public static final int PAYMENTCOUPON = 702048;

	public static final int ADDCREDITCARD = 702046;

	public static final int BANK = 702052;

	public static final int CERTIFICATIONBEFORE = 702054;

	public static final int NEWAUTHENTICATION = 702053;
}