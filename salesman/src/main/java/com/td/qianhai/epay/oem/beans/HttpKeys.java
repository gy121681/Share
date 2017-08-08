package com.td.qianhai.epay.oem.beans;

public class HttpKeys {

	// 验证终端号
	public static final String[] REGIST_POSZD_ASK = { "TRANCODE", "TERMNO",
			"BSOPERID", "APPTYP" };

	public static final String[] REGIST_POSZD_BACK = null;
	
	// 登录
	public static final String[] LOGIN_ASK = { "TRANCODE", "PHONENUMBER",
			"PASSWORD", "PCSIM", "TERMINALNUMBER", "APPTYP","APPUSERID","APPCHANNELID","CLIENTTYPE","OEMID","JPTAG","JPALIAS","JPREGISTRATIONID"};
	public static final String[] LOGIN_BACK = { "PHONENUMBER", "MERSNM",
			"TERMINALNUMBER", "MERCNUM", "STS","NOCARDFEERATE","IDCARDPICSTA","CUSTPICSTA","FRYHKIMGPATHSTA","CURROL","AGENTID","OEMFEERATE","ISRETAILERS","ISSALEAGT","ISGENERALAGENT","ISSENIORMEMBER","LOEMID","OEMID","PERSONPIC","NCONTENT","NCREATETIM","ISAREAAGENT","NOTICEMESSAGE","PROVID","CITYID","AREAID"};

	// （钱包）致付宝详细信息
	public static final String[] RICH_TREASURE_INFO_ASK = { "TRANCODE", "ACTID" };
	public static final String[] RICH_TREASURE_INFO_BACK = { "LOGSTS",
		"ACTSTS", "ACTCARD", "BANKNAM", "CRDFLG", "YESTERINCOM", "MERNAM",
		"TOTAMT", "FIXAMT", "CHECKAMT", "AVAAMT", "FRZAMT", "DPTRATE",
		"CUMULATIVE", "MILINCOM", "WEEKINCOM", "MONTHINCOM", "ISACTPWOUT","ISSETPAYPWD","EPURSTFSUMAMT","EPURSTFCOUNT","EPURSWITHDRAWSUMAMT","EPURSWITHDRAWSINGAMT","EPURSWITHDRAWCOUNT",
		"INTERESTAMT","LEFTPOINTSTPOINTS","TOTPOINTS","DEPOSITAMT","SALEAGTAMT","LOWWDAMT","ISBRUSHOTHERSCARD","APPLYDAT","RSPCOD", "RSPMSG" };
	// 修改支付密码
	public static final String[] UPPAYPASS_ASK = { "TRANCODE", "ACTID",
			"OLDACTPW", "NEWACTPW" };
	public static final String[] UPPAYPASS_BACK = null;

	// 找回支付密码验证码
	public static final String[] REG_PAY_PW_VCODE_ASK = { "TRANCODE", "ACTID",
			"IDCODE" };
	public static final String[] REG_PAY_PW_VCODE_BACK = { "RSPCOD", "RSPMSG" };

	// 找回支付密码验证码验证
	public static final String[] REG_PAY_PW_VCODE_VD_ASK = { "TRANCODE",
			"ACTID", "VLIDATECODE", "IDCODE" };
	public static final String[] REG_PAY_PW_VCODE_VD_BACK = { "RSPCOD",
			"RSPMSG" };

	// 找回支付密码修改
	public static final String[] PAY_UPDATE_ASK = { "TRANCODE", "ACTID",
			"PASSWORD" };
	public static final String[] PAY_UPDATE_BACK = { "RSPCOD", "RSPMSG" };

	// 支付宝详细信息
	public static final String[] RICH_TREASURE_DETAIL_ASK = { "TRANCODE",
		"ACTID", "PAGENUM", "NUMPERPAGE", "SDAT", "EDAT","OPERSTYPID" };
	public static final String[] RICH_TREASURE_DETAIL_BACK = { "MERNAM",
		"TOLCNT", "ALOGNO", "OPERBTYP", "OPERSTYP", "SUMAMT", "TXNAMT",
		"FEEAMT", "OPERTIM", "OPERID", "OPERSTS", "SOURACTID", "TARGACTID",
		"TARGCARDNO", "TARGATCNAM", "REMARK","RECNUMBER","CARDNO","CARDNAM","OUTACTNAM","PACTNAM","OPERSTYPNAM", "RSPCOD", "RSPMSG" };

	// 提现
	public static final String[] WITHDRAWAL_ON_BANK_CARD_ASK = { "TRANCODE",
			"ACTID", "TXNAMT", "ACTPW" ,"ISURGENT"};
	public static final String[] WITHDRAWAL_ON_BANK_CARD_BACK = { "CHECKAMT",
			"AVAAMT", "RSPCOD", "RSPMSG" };

	// 存入定期
	public static final String[] BASIS_DEPOSIT_ASK = { "TRANCODE", "ACTID",
			"DPTMID", "TXNAMT" };
	public static final String[] BASIS_DEPOSIT_BACK = { "CHECKAMT", "AVAAMT",
			"RSPCOD", "RSPMSG" };

	// 定期模式列表
	public static final String[] BASIS_LIST_ASK = { "TRANCODE" };
	public static final String[] BASIS_LIST_BACK = { "DPTMID", "DPTMNAM",
			"DPTRATE", "DPTCYCLE", "DPTMIN", "SVAKIND", "CURRENCY",
			"BRKMODNAM", "RSPCOD", "RSPMSG" };

	// 定期列表
	public static final String[] REGULAR_BASIS_LIST_ASK = { "TRANCODE",
			"ACTID", "PAGENUM", "NUMPERPAGE" };
	public static final String[] REGULAR_BASIS_LIST_BACK = { "SUMPRINCIPAL",
			"DPTLOGNO", "DPTMNAM", "PRINCIPAL", "DPTCYCLE", "DPTRATE",
			"EXPINTEREST", "EXPSUMAMT", "OPENDAT", "ENDDAT", "SVAKIND",
			"CURRENCY", "MODSTS", "RSPCOD", "RSPMSG" };

	// 转出到活期
	public static final String[] CURRENT_TRANSFER_ASK = { "TRANCODE", "ACTID",
			"DPTLOGNO" };
	public static final String[] CURRENT_TRANSFER_BACK = { "CHECKAMT",
			"TOTAMT", "AVAAMT", "FIXAMT", "RSPCOD", "RSPMSG" };

	// // 注册
	// public static final String[] REGISTER_ASK = { "TRANCODE", "ACCOUNTNAME",
	// "IDNUMBER", "PHONENUMBER", "PASSWORD", "TRMTYP", "BANKACCOUNT",
	// "PROVID", "CITYID", "BKNO", "BRANKBRANCH", "RANDOM", "REFPHONE" };

	// 注册
	public static final String[] REGISTER_ASK = { "PHONENUMBER", "PASSWORD",
			"EMAIL", "MERMOD", "RANDOM", "TRMTYP" ,"APPMARKNUMBER","RECOMMENDATION","ACTCODE"};
	public static final String[] REGISTER_BACK = null;

	// 获取密码修改验证码
	public static final String[] REGET_PASSWORD_VERIf_ASK = { "TRANCODE",
			"PHONENUMBER", "IDCARDNUMBER", "TERMNO", "APPTYP" };
	public static final String[] REGET_PASSWORD_VERIF_BACK = { "PHONENUMBER",
			"IDCARDNUMBER", "RSPCOD", "RSPMSG" };

	// 验证验证码
	public static final String[] VERIF_COMMIT_ASK = { "TRANCODE",
			"PHONENUMBER", "VLIDATECODE", "APPTYP" };
	public static final String[] VERIF_COMMIT_BACK = { "PHONENUMBER", "RSPCOD",
			"RSPMSG" };

	// 重置密码
	public static final String[] REGET_PASSWORD_ASk = { "TRANCODE",
			"PHONENUMBER", "PASSWORD" };
	public static final String[] REGET_PASSWORD_BACK = { "PHONENUMBER",
			"RSPCOD", "RSPMSG" };

	// 修改密码
	public static final String[] REVISE_PASSWORD_ASK = { "TRANCODE",
			"PHONENUMBER", "PASSWORD", "PASSWORDNEW" };
	public static final String[] REVISE_PASSWORD_BACK = { "PHONENUMBER" };

	// 获取注册验证码
	public static final String[] GET_VERIFCODE_ASK = { "TRANCODE",
			"PHONENUMBER", "TYPE", "TERMINALNUMBER", "TRMTYP","APPMARKNUMBER" };
	public static final String[] GET_VERIFCODE_BACK = { "PHONENUMBER" };

	// // 实名认证
	// public static final String[] REAL_NAME_AUTH_ASK = { "TRANCODE",
	// "PHONENUMBER", "IDCARDPIC", "CUSTPIC", "TERMNO", "TRMTYP" };
	// public static final String[] REAL_NAME_AUTH_BACK = null;

	// 实名认证
	public static final String[] REAL_NAME_AUTH_ASK = { "TRANCODE", "MERCNUM","IDCARDPIC", "CUSTPIC","BANKACCOUNT","MERCNAM","CORPORATEIDENTITY","PROVID","CITYID","AREAID"};
	public static final String[] REAL_NAME_AUTH_BACK = null;
	
	
	// 商户信息
	public static final String[] BUSSINESS_INFO_ASK = { "TRANCODE",
		"PHONENUMBER", "TRMTYP", "REGFLAG"};
	public static final String[] BUSSINESS_INFO_BACK = { "PHONENUMBER",
		"ACTNAM", "ACTNO", "OPNBNK", "CARDID", "MERSTATUS", "MERCNUM" ,"ISRECOMMENDED","RECOMMENDED","PROVNAM","CITYNAM","MERLEVEL","FEERAT","SALEFEERATE","VIPFEERATE","STRATE","AREANAM","AGTPROVNAM","AGTCITYNAM","AGTAREANAM","ISAREAAGENT"};

	// 修改开户银行信息
	public static final String[] REVISE_BANK_INFO_ASK = { "TRANCODE",
			"PHONENUMBER", "TERMNO", "TRMTYP", "OPNBNK", "OPNBNKPRO",
			"OPNBNKCITY", "OPNBNKBRA", "ACTNAM", "IDCARD", "ACTNO" };
	public static final String[] REVISE_BANK_INFO_BACK = { "PHONENUMBER" };

	// 银行卡余额查询
	public static final String[] BALANCE_QUERY_ASK = { "TRANCODE",
			"PHONENUMBER", "TERMINALNUMBER", "PCSIM", "CRDNO", "Track2",
			"Track3", "TPINBLK", "PSAMCARDNO" };
	public static final String[] BALANCE_QUERY_BACK = { "PHONENUMBER",
			"BALINF", "TTXNTM", "TTXNDT" };

	// 老板收款
	public static final String[] BOSS_RECEIVE_ASK = { "TRANCODE",
			"PHONENUMBER", "TERMINALNUMBER", "PCSIM", "CRDNO", "Track2",
			"Track3", "TPINBLK", "ELESIGNA", "ELESIGNATYPE", "CTXNAT",
			"PSAMCARDNO", "STLTYPE" };
	public static final String[] BOSS_RECEIVE_BACK = { "PHONENUMBER", "LOGNO" };

	// 上传电子签名
	public static final String[] UPLOAD_SIGN_ASK = { "TRANCODE", "PHONENUMBER",
			"TERMINALNUMBER", "LOGNO", "ELESIGNA", "ELESIGNATYPE" };
	public static final String[] UPLOAD_SIGN_BACK = { "PHONENUMBER" };

	// 消费撤销
	public static final String[] REVOKE_PAY_ASK = { "TRANCODE", "PHONENUMBER",
			"TERMINALNUMBER", "PCSIM", "CRDNO", "TPINBLK", "Track2", "Track3",
			"SRefNo", "CTXNAT", "PSAMCARDNO", "LOGNO" };
	public static final String[] REVOKE_PAY_BACK = { "PHONENUMBER" };

	// 流水查询
	public static final String[] DEAL_RECORDS_ASK = { "TRANCODE",
			"PHONENUMBER", "PAGENUM", "NUMPERPAGE", "SDAT", "EDAT" };
	public static final String[] DEAL_RECORDS_BACK = { "LOGNO", "SYSDAT",
			"MERCNAM", "LOGDAT", "TXNAMT", "CRDNO", "TRANNAM", "TXNSTS",
			"SREFNO" };

	// 终端主秘钥下载
	public static final String[] KEY_DOWNLOAD_ASK = { "TRANCODE", "TERMNO",
			"PCSIM" };
	public static final String[] KEY_DOWNLOAD_BACK = { "PHONENUMBER", "TMKKEY" };

	// 签到
	public static final String[] CHECK_ASK = { "TRANCODE", "PHONENUMBER",
			"TERMNO", "PCSIM" };
	public static final String[] CHECK_BACK = { "PINKEY", "MACKEY",
			"ENCRYPTKEY" };

	// 检测版本更新
	public static final String[] VERSION_UPDATE_ASK = { "TRANCODE", "APKNAME" };
	public static final String[] VERSION_UPDATE_BACK = { "VERCODE", "VERNAME",
			"APKNAME", "CREATETIM", "APKRUL", "ISFORCE" };

	// 刷卡查询银行卡名称
	public static final String[] BANK_NAME_ASK = { "TRANCODE", "TERMNO",
			"TRACK2", "TRACK3" };
	public static final String[] BANK_NAME_BACK = { "PROVID", "PROVNAM",
			"DCFLAG" };

	// 银行卡开户城市
	public static final String[] BANK_CITY_ASK = { "TRANCODE", "PAGENUM",
			"NUMPERPAGE", "PROVID" };
	public static final String[] BANK_CITY_BACK = { "CITYNAM", "CITYID" };

	// 银行卡支行
	public static final String[] BANK_BRANCH_ASK = { "TRANCODE", "PAGENUM",
			"NUMPERPAGE", "ISSNO", "PROVID", "CITYID" };
	public static final String[] BANK_BRANCH_BACK = { "BENELX", "BKNO" };

	public static final String[] ORADSEQ_PAY_ASK = { "TRANCODE", "TXNTYP",
			"TXNSTS", "MERCID", "TERMNO", "AGTORG", "TXNAMT" };
	public static final String[] ORADSEQ_PAY_BACK = { "LOGNO" };

	public static final String[] ORADSEQ_UPDATE_ASK = { "TRANCODE", "LOGNO",
			"TXNSTS" };
	public static final String[] ORADSEQ_UPDATE_BACK = null;

	// 银行卡信息查询
	public static final String[] QUERY_BANK_INFO_ASK = { "TRANCODE", "MERNO" };
	public static final String[] QUERY_BANK_INFO_BACK = { "PROVID", "PROVNAM" };

	// 查询知否注册
	public static final String[] IS_REGISTER_ASK = { "TRANCODE", "TERMNO" };
	public static final String[] IS_REGISTER_BACK = { "TERMNO", "BINDFLAG" };

	// 申请银行卡信息修改
	public static final String[] APPLY_BANKCARD_INFO_CHANGES_ASK = {
			"TRANCODE", "PHONENUMBER", "OPNBNK", "OPNBNKPRO", "OPNBNKCITY",
			"OPNBNKBRA", "ACTNAM", "ACTNO" };
	public static final String[] APPLY_BANKCARD_INFO_CHANGES_BACK = { "PHONENUMBER" };

	// 申请修改商户实名认证
	public static final String[] APPLY_REALNAME_CHANGES_ASK = { "TRANCODE",
			"MERCNUM", "IDCARDPIC", "CUSTPIC", "TRMTYP" };
	public static final String[] APPLY_REALNAME_CHANGES_BACK = null;

	// 商户申请手机号码修改
	public static final String[] APPLY_PHONENUMBER_CHANGES_ASK = { "TRANCODE",
			"NEWPHONENUMBER", "PHONENUMBER", "RANDOM", "MERCNUM" };
	public static final String[] APPLY_PHONENUMBER_CHANGES_BACK = null;
	// 商户申请手机号码修改获取新手机验证码
	public static final String[] QUERY_PHONENUMBER_CHANGES_VERIFCODE_ASK = {
			"TRANCODE", "PHONENUMBER","OLDPHONENUMBER" };
	public static final String[] QUERY_PHONENUMBER_CHANGES_VERIFCODE_BACK = {
			"PHONENUMBER", "RSPCOD", "RSPMSG" };

	// 申请修改商户手机实名认证
	public static final String[] APPLY_PHONENUMBER_REALNAME_CHANGES_ASK = {
			"TRANCODE", "MERCNUM", "IDCARDPIC", "CUSTPIC", "TRMTYP" };
	public static final String[] APPLY_PHONENUMBER_REALNAME_CHANGES_BACK = null;

	// 订单支付
	public static final String[] ORDERPAY_ASK = { "TRANCODE", "TERMINALNUMBER",
			"PHONENUMBER", "ORDAMT", "PRDNAME", "PRDUNITPRICE", "BUYCOUNT",
			"STLTYPE" };

	public static final String[] ORDERPAY_BACK = { "PHONENUMBER",
			"TERMINALNUMBER", "PSAMCARDNO", "ORDERNUM", "TN", "RSPCOD",
			"RSPMSG" };

	// 订单支付批量查询
	public static final String[] ORDERPAYQUERY_ASK = { "TRANCODE",
			"PHONENUMBER", "PAGENUM", "NUMPERPAGE", "SDAT", "EDAT" };

	// 订单支付批量查询
	public static final String[] ORDERPAYQUERY_BACK = { "PHONENUMBER",
			"PAGENUM", "TOLCNT", "ORDAMT", "ORDERNUM", "ORDERTIM", "TRANSTS",
			"QN", "TN", "PAYORDNO", "PRDORDTYPE" };

	public static final String[] ORDERPAYDETAIL_ASK = { "TRANCODE",
			"PHONENUMBER", "ORDERNUM", "ORDERTIME" };

	public static final String[] ORDERPAYDETAIL_BACK = { "PHONENUMBER",
			"ORDERNUM", "ORDERTIME", "TRANSTS", "ORDAMT" };

	// 创建订单
	public static final String[] ORDER_CREATE_ASK = { "TRANCODE",
			"PHONENUMBER", "ORDAMT", "URLTYPE", "MERORDERNAME", "PRDSHORTNAME",
			"MERORDERDESC", "MERTYPE" };

	public static final String[] ORDER_CREATE_BACK = { "MERORDERID",
			"MERORDERDATE", "REURL", "RSPCOD", "RSPMSG" };

	// 订单查询单个
	public static final String[] ORDER_QUERY_ASK = { "TRANCODE", "PHONENUMBER",
			"MERORDERID", "MERORDERDATE", "URLTYPE" };

	public static final String[] ORDER_QUERY_BACK = { "MERORDERSTATUS",
			"RSPCOD", "RSPMSG" };

	// 订单查询多个
	public static final String[] ORDER_QUERY_D_ASK = { "TRANCODE",
			"PHONENUMBER", "PAGENUM", "NUMPERPAGE", "SDAT", "EDAT" };

	public static final String[] ORDER_QUERY_D_BACK = { "CLSLOGNO", "ORDAMT",
			"ORDERTIM", "TRANSTS", "PRDORDTYPE", "ORDERNUM", "TN", "QN",
			"RSPCOD", "RSPMSG" };

	// 已付订单查询
	public static final String[] ORDER_QUERY_D_OVER_ASK = { "TRANCODE",
			"PHONENUMBER", "PAGENUM", "NUMPERPAGE", "SDAT", "EDAT" };

	public static final String[] ORDER_QUERY_D_OVER_BACK = { "RSPMSG",
			"PHONENUMBER", "NUMPERPAGE", "TOLCNT", "CLSLOGNO", "TXNDAT",
			"CLSDAT", "TXNAMT", "FEERAT", "CLSAMT", "FEEAMT", "CLSSTS",
			"RSPCOD","TRANSTS" };

	// 订单支付
	public static final String[] ORDER_PAY_ASK = { "TRANCODE", "PHONENUMBER",
			"PRDORDNO", "URLTYPE" };

	public static final String[] ORDER_PAY_BACK = { "REURL", "RSPCOD", "RSPMSG" };

	// 设置支付密码
	public static final String[] SETPAYPASS_ASK = { "TRANCODE", "ACTID",
			"ACTPW" };
	public static final String[] SETPAYPASS_BACK = null;

	public static final String[] TRANSFERACC_ASK = { "TRANCODE", "OACTID",
			"PACTID", "PACTNAM", "ACTPW", "ACCESSTYP", "TRANAMT" };

	public static final String[] TRANSFERACC_BACK = null;
	
	//费率列表
	public static final String[] RATE_QUERY_ASK = {"TRANCODE","PHONENUMBER"};
	
	public static final String[] RATE_QUERY_BACK = {"FEERATNO","FEERATE","FEERATEDESC","PRICE"};
	
	//费率升级
	public static final String[] GO_RATE_ASK = {"TRANCODE","PHONENUMBER","FEERATBUYTYP","FEERATNO","FORMATCPW","ACTCODE"};
	
	public static final String[] GO_RATE_BACK = {"RSPCOD", "RSPMSG","AGENTID","NOCARDFEERATE"};
	
	
//	public static final String[] RECHARGE_ASK = {"TRANCODE","PHONENUMBER"};
	
	public static final String[] RECHARGE_BACK = {"PRDTYPE","PRDNUM","PRDNAME","PRDID","PRDAMT","PRDPARVALUE"};
	
	
	public static final String[] PHONE_CHARGE_ASK = {"TRANCODE","PHONENUMBER","TRAPWD","REQOPERATORS","RECTYPE","PRDID","MERCNUM"};
	
	public static final String[] PHONE_CHARGE_BACK = null;
	
	
	
	public static final String[] ORDERINFO_ASK = {"TRANCODE","ALOGNO"};
	public static final String[] ORDERINFO_BACK = { "ACTID", "ACTNAM",
			"PACTID", "PACTNAM", "ALOGNO", "OPERBTYP", "OPERSTYP", "TXNAMT",
			"FEEAMT", "OPERTIM", "REMARK", "OPERSTS","TARGCARDNO","TARGATCNAM","RECNUMBER","CARDNO","CARDNAM","BANKNAM","OUTACTID","OUTACTNAM","TARGACTID"};
	
	
	
	public static final String[] MYCIRCLE_ASK = {"TRANCODE","PHONENUMBER","PAGENUM","NUMPERPAGE"};
	public static final String[] MYCIRCLE_BACK = { "MERCNAM", "MERPHONENUMBER",
			"APPLYDAT","TOLCNT","TOTSHRAMT","PERSONPIC","ISSENIORMEMBER","ISRETAILERS","ISSALEAGT","ISGENERALAGENT","RSPCOD", "RSPMSG" };
	
	
//	public static final String[] CREDITQUERY_ASK = {"TRANCODE","MERCNUM"};
//	public static final String[] CREDITQUERY_BACK = { "CARDACTID", "CARDNO",
//			"CARDNAME","ISSNAM","ISSNO","RSPCOD", "RSPMSG" };
	
	public static final String[] CREDITQUERY_ASK = {"TRANCODE","ACTID","PAGENUM","NUMPERPAG"};
	public static final String[] CREDITQUERY_BACK = { "ISSUER", "CARDNAME",
			"CARDCODE","FRPID","RSPCOD", "RSPMSG" };
	
//	public static final String[] CREDITPAY_ASK = {"TRANCODE","MERCNUM","RCVACCTNO","RCVACCTNAME","TRNAMT","TRAPWD","CARDID"};
//	public static final String[] CREDITPAY_BACK = { "ORDEERID","RSPCOD", "RSPMSG" };
	
	public static final String[] CREDITPAY_ASK = {"TRANCODE","ACTID","TXNAMT","CREDITCARD","ISURGENT","ACTPW","PAYAMT"};
	public static final String[] CREDITPAY_BACK = { "RSPCOD", "RSPMSG" };
	
	public static final String[] GETBANKNAME_ASK = {"TRANCODE","CARNO"};
	public static final String[] GETBANKNAME_BACK = { "ISSNAM","ISSNO","RSPCOD", "RSPMSG" };
	
	public static final String[] BINDINGCARD_ASK = {"TRANCODE","ORDERID","REFUNDDATE","MERCNUM"};
	public static final String[] BINDINGCARD_BACK = null;
	
//	public static final String[] DELETECREDITCARD_ASK = {"TRANCODE","CARCATNO"};
//	public static final String[] DELETECREDITCARD_BACK = null;
	
	public static final String[] DELETECREDITCARD_ASK = {"TRANCODE","ACTID","CARDCODE"};
	public static final String[] DELETECREDITCARD_BACK =  { "RSPCOD", "RSPMSG" };;
	
	public static final String[] GROUPING_ASK = {"TRANCODE","PHONENUMBER","TAGNAME"};
	public static final String[] GROUPING_BACK = null;
	
	public static final String[] YIBAO_ASK = { "TRANCODE", "ORDAMT", "URLTYPE", "MERORDERNAME", "PRDSHORTNAME",
		"MERORDERDESC", "MERTYPE","CREDTYPE","CREDCODE","BUYERNAME","ACTCODE","CARDWAY","PHONENUMBER" };
	public static final String[] YIBAO_BACK = null;
	
	public static final String[] EPAY_ASK = { "TRANCODE", "PHONENUMBER", "MERTYPE", "ORDAMT", "CARDTEL",
		"CREDTYPE", "CREDCODE","CARDNAME","CARDCODE","FRPID","EXPIREYEAR","EXPIREMONTH","CVV","ISSUER","CARDWAY","ISBIND","IMGPATH"};
	public static final String[] EPAY_BACK = {"CLSLOGNO","ISCHECK","VERIFYCODE","RSPCOD", "RSPMSG"};
	
	
	public static final String[] EPAYNUM_ASK = { "TRANCODE", "VERIFYCODE", "CLSLOGNO"};
	public static final String[] EPAYNUM_BACK = null;
	
	public static final String[] GETEPAYNUM_ASK = { "TRANCODE", "CLSLOGNO", "CARDTEL"};
	public static final String[] GETEPAYNUM_BACK = null;
	
	
	public static final String[] BANKCARDLIST_ASK = { "TRANCODE", "PHONENUMBER","CRDFLG"};
	public static final String[] BANKCARDLIST_BACK = {"CREDTYPE","CREDCODE","ISSUER","FRPID","CARDTEL","CARDNAME","CARDCODE","EXPIREYEAR","EXPIREMONTH","CVV","RECHARGENUMBER","RSPCOD","RSPMSG"};
	
	public static final String[] UPLODPIC_ASK = { "TRANCODE", "IMGNAME"};
	public static final String[] UPLODPIC_BACK = {"IMGPATH","RSPCOD", "RSPMSG"};
	
	
	public static final String[] NEWAGENT_ASK = { "TRANCODE", "PHONENUMBER","AGTPHONENUM","FEERATE","URGENTFEE"};
	public static final String[] NEWAGENT_BACK = {"AGENTID","RSPCOD", "RSPMSG"};
	
	public static final String[] ACTCODEMANAGE_ASK = { "TRANCODE", "PHONENUMBER","AGENTID"};
	public static final String[] ACTCODEMANAGE_BACK = {"ACTIVENUM","MAKENUM", "UNMAKENUM","ALLOTNUM","RSPCOD", "RSPMSG"};
	
	public static final String[] CHIRDACT_ASK = { "TRANCODE", "PHONENUMBER","CURAGENTID","PAGENUM","NUMPERPAGE"};
	public static final String[] CHIRDACT_BACK = {"AGENTID","AGTNAM","STRATE","TOTACTCOD","ACTID","URGENTFEE","MINRATE","MAXRATE","MINURGENTFEE","MAXURGENTFEE","MINFEERATE"};
	
	public static final String[] VERIFICATIONPHONENUM_ASK = { "TRANCODE", "PHONENUMBER","CHECKPHONENUM"};
	public static final String[] VERIFICATIONPHONENUM_BACK = {"RSPCOD", "RSPMSG"};
	
	public static final String[] ACTCODEINIT_ASK = { "TRANCODE", "PHONENUMBER","AGENTID","MAKEAMOUNT","ACTPW"};
	public static final String[] ACTCODEINIT_BACK = {"ACTCODE","RSPCOD", "RSPMSG"};
	
	public static final String[] AGENTINFO_ASK = { "TRANCODE", "PHONENUMBER","AGENTID","MERCNUM"};
	public static final String[] AGENTINFO_BACK = {"USEDACTCOD","LEFTSHRAMT","TOTSHRAMT","MINFEERATE","MAXFEERATE","NOCARAGTTYP","AGTNAM","RATESHRAMT","WDSHRAMT","MINURGENTFEE","MAXURGENTFEE","RSPCOD","RSPMSG"};
	
	public static final String[] ACTCODELIST_ASK = { "TRANCODE", "PHONENUMBER","AGENTID","PAGENUM","NUMPERPAGE"};
	public static final String[] ACTCODELIST_BACK = {"ACTCOD","ACTCODTYP","ACTIVDAT","INVAILDAT","RSPCOD","RSPMSG"};
	
	
	public static final String[] ACTCODETRANSFER_ASK = { "TRANCODE", "PHONENUMBER","AGENTID","TOAGENTID","ALLOTNUM","ACTPW"};
	public static final String[] ACTCODETRANSFER_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] RATEEDIT_ASK = { "TRANCODE", "PHONENUMBER","AGENTID","FEERATE"};
	public static final String[] RATEEDIT_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] FEEDBACK_ASK = { "TRANCODE", "PHONENUMBER","CHANNEL","CONTENT"};
	public static final String[] FEEDBACK_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] INCOMEDETAIL_ASK = { "TRANCODE", "AGTACTID","SHRTYPE","PAGENUM","NUMPERPAG"};
	public static final String[] INCOMEDETAIL_BACK = {"LOGNO","TOTTXNAMT","AGTSHRAMT","BJSTRATE","RATEDIFF","TXNDATE","TXNTIME","SHRSTS","SHRTYPE","RES","RSPCOD","RSPMSG"};
	
	public static final String[] DEPOSITPURSE_ASK = { "TRANCODE", "AGTACTID"};
	public static final String[] DEPOSITPURSE_BACK = {"LEFTSHRAMT","RSPCOD","RSPMSG"};
	
	public static final String[] PROMOTIONEARNINGS_ASK = { "TRANCODE", "REFACTID","PAGENUM","NUMPERPAG","MONTH","YEAR"};
	public static final String[] PROMOTIONEARNINGS_BACK = {"RYEARMONTH","RDAY","INCOMAMT","MEMBERNUM","TYP"};
	
	public static final String[] RATEWITHDRAWALSEDIT_ASK = { "TRANCODE", "PHONENUMBER","AGENTID","FEERATE"};
	public static final String[] RATEWITHDRAWALSEDIT_BACK = {"RSPCOD","RSPMSG"};
	
	
	public static final String[] FINANCIALPRODUCTS_ASK = {"TRANCODE","ACTID","PAGENUM","NUMPERPAG"};
	public static final String[] FINANCIALPRODUCTS_BACK = {"DAYOUTINTEGRAL","MAXOUTINTEGRAL","OEMID","OUTSTATUS",
		"DPTCYCLE","INSTATUS","MAXOUTAMT","MINOUTINTEGRAL","STATUS","DAYINTEGRALNUM","MIDOEMTYP","MININAMT",
		"UPDATIM","DAYINNUM","DAYSUMAMT","MINOUTAMT","DPTRATE","MIDOEMID","DAYOUTSUMAMT","DAYOUTNUM","MAXINAMT","SAVEDAMT","MIDOEMNAM","DPTCYCLETYP","TOTPOINTS","OPOINTS","RSPCOD","RSPMSG"};
	
	public static final String[] TOCHANGEINTO_ASK = {"TRANCODE","ACTID","MIDOEMID","MIDOEMTYP","TXNAMT","ACTPW"};
	public static final String[] TOCHANGEINTO_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] TURNOUT_ASK = {"TRANCODE","ACTID","TXNAMT","ACTPW"};
	public static final String[] TURNOUT_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] INTEGRALEXCHANGE_ASK = {"TRANCODE","ACTID","TXNAMT","ACTPW"};
	public static final String[] INTEGRALEXCHANGE_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] EXCHANGEINFO_ASK = {"TRANCODE","ACTID","MIDOEMID"};
	public static final String[] EXCHANGEINFO_BACK = {"MIDOEMID","MIDOEMNAM","MIDOEMTYP","DPTCYCLE","DPTCYCLETYP","SAVEDAMT","TOTPOINTS","OPOINTS","OEMID","INSTATUS","OUTSTATUS","DPTRATE","MININAMT","MAXINAMT",
		"DAYINNUM","DAYOUTNUM","MINOUTINTEGRAL","MAXOUTINTEGRAL","DAYOUTINTEGRAL","STATUS","DAYINTEGRALNUM","MINOUTAMT","MAXOUTAMT","DAYOUTSUMAMT","UPDATIM","DAYSUMAMT","RSPCOD","RSPMSG"};
	
	public static final String[] EXCHANGEINFO1_ASK = {"TRANCODE","ACTID","MIDOEMID","FIXOPERDATSTR","FIXOPERDATEND","PAGENUM","NUMPERPAG"};
	public static final String[] EXCHANGEINFO1_BACK = {"TOLCNT","DPTMNAM","DPTLOGNO","PRINCIPAL","OPENDAT","ENDDAT","EXPSUMAMT","EXPINTEREST","MODSTS","RSPCOD","RSPMSG"};
	
	public static final String[] DISTRIBUTOR = {"roleId","roleName","upgradeAmt","actNum","rechargeRate","commWithdrawAmt","urgentWithdrawRate","oemId","alias","buyNum","buyPrice","buyTotalPrice"};
	public static final String[] MYPROFITBACK = {"TYPNAM","AMT","OPERSTYP","AMTTYP"};
	
	public static final String[] TRANSACTIONTYPE_ASK = {"TRANCODE"};
	public static final String[] TRANSACTIONTYPE_BACK = {"OPERSTYPID","OPERSTYPNAM"};
	
	
	public static final String[] EPAYSAV_ASK = {"TRANCODE", "PHONENUMBER", "MERTYPE", "ORDAMT", "CARDTEL",
		"CREDTYPE", "CREDCODE","CARDNAME","CARDCODE","ISSUER","FRPID","CARDWAY","ISBIND","IMGPATH"};
	public static final String[] EPAYSAV_BACK = {"CLSLOGNO","ISCHECK","VERIFYCODE","RSPCOD", "RSPMSG"};
	
	public static final String[] UNBINDCARD_ASK = {"TRANCODE","ACTID","CARDCODE"};
	public static final String[] UNBINDCARD_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] UPDATEBANKCARD_ASK = {"TRANCODE","ACTID","CARDCODE","CARDTEL"};
	public static final String[] UPDATEBANKCARD_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] BUYCODE_ASK = {"TRANCODE","PHONENUMBER","ACTPW","CODNUM"};
	public static final String[] BUYCODE_BACK = null;
	
	public static final String[] SUBORDINATEAGENTSASK = {"agentId","PageNum","NumPerPag","MERPHONE","MERCNAM","gradeFlag"};
	public static final String[] SUBORDINATEAGENTSBACK = {"mercnam","merphonenumber","recommendation","nocardagtid","oemid","isseniormember","isretailers","issaleagt","isgeneralagent","personpic"};
	
	public static final String[] BINDAGENT_ASK = {"TRANCODE","ACTID","PACTID"};
	public static final String[] BINDAGENT_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] COMITADRESS_ASK = {"TRANCODE","MERCNUM","PROVID","CITYID","OPNBNKAREA"};
	public static final String[] COMITADRESS_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] AVATARUPLOAD_ASK = {"TRANCODE","PHONENUMBER","PERSONPIC"};
	public static final String[] AVATARUPLOAD_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] CHOOSEBANK_ASK = {"TRANCODE"};
	public static final String[] CHOOSEBANK_BACK = {"IMG_URL","BANKNAME"};
	
	public static final String[] BANNER_ASK =  {"TRANCODE","PHONENUMBER","OEMID"};
	public static final String[] BANNER_BACK = {"IMG_URL","FORWARD_URL","AGTMERCNUM","CONTENT","NICKNAME","ADDRESS","AGTPHONE","LINKURL","FONTCOLOR"};
	
	public static final String[] COLLECTIONTREASURE_ASK = {"TRANCODE","OEMID","PHONENUMBER"};
	public static final String[] COLLECTIONTREASURE_BACK = {"MAINCUSTOMERNUMBER","CUSTOMERNUMBER","LOGINURL","TARGET","HMAC","RSPCOD","RSPMSG"};
	
	public static final String[] REALNAMEAUTHENTICATION_ASK = {"TRANCODE","MERCNUM","BANKCARDPHOTO","PERSONPHOTO","IDCARDBACKPHOTO"};
	public static final String[] REALNAMEAUTHENTICATION_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] PROXYRETURNASK = {"PHONENUMBER","PageNum","NumPerPag","MERPHONE","MERCNAM","shrtype","SELTYPE"};
	public static final String[] PROXYRETURN = {"mercnam","merphonenumber","isseniormember","issaleagt","tottxnamt","agtshramt","systim","personpic","isretailers","isgeneralagent","shrtype","res"};
	
	public static final String[] RECOMMANDDETAILASK = {"PHONENUMBER","PageNum","NumPerPag","MERPHONE","SELTYPE","MERCNAM","gradeFlag"};
	public static final String[] RECOMMANDDETAIL = {"mercnam","merphonenumber","isseniormember","issaleagt","isgeneralagent","isretailers","applydat","directCommonMerCount","directCommonSeniorMerCount","directCommonSaleAgtMerCount","indirectCommonMerCount","indirectCommonSeniorMerCount","indirectCommonSaleAgtMerCount",
								"directCommonRetailersMerCount","directCommonAgentMerCount","indirectCommonRetailersMerCount","indirectCommonAgentMerCount","personpic"};
	
	public static final String[] INTHEQUERY_ASK = {"TRANCODE","PARENTCODE"};
	public static final String[] INTHEQUERY_BACK = {"AREACODE","AREANAME","RSPCOD","RSPMSG"};
	
	public static final String[] CODETRANSFER_ASK = {"TRANCODE","PHONENUMBER","AGTPHONENUM","ALLOTNUM"};
	public static final String[] CODETRANSFER_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] SCREENINGURL_ASK = {"mobile"};
	public static final String[] SCREENINGURL_BACK = {"flag","message","qrcode_url","shopName","subMchId","is_subbranch"};
	
	public static final String[] SCREENINGURLFA_ASK = {"mobile","shopName"};
	public static final String[] SCREENINGURLFA_BACK = {"flag","message"};
	
	public static final String[] GETWECHATLIST_ASK = {"mobile","payMonth","subMchId"};
	public static final String[] GETWECHATLIST_BACK = {"totalFee","payTime"};
	
	public static final String[] OWNERLICENSEPLATE_ASK = {"TRANCODE","MERCNUM","LICENSENUMBER","LICENSECOLOR"};
	public static final String[] OWNERLICENSEPLATE_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] OWNERLICENSEPLATE1_ASK = {"TRANCODE","MERCNUM"};
	public static final String[] OWNERLICENSEPLATE1_BACK = {"LICENSENUMBER","LICENSECOLOR","RSPCOD","RSPMSG"};
	
	public static final String[] UNBUNDLING_ASK = {"TRANCODE","MERCNUM","LICENSENUMBER"};
	public static final String[] UNBUNDLING_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] ETCLIST_ASK = {"TRANCODE","MERCNUM","PAGENUM", "NUMPERPAGE","YEAR","MONTH"};
	public static final String[] ETCLIST_BACK = {"ADDRESS","SUMAMT","OPERTIM","OPERSTS","TOLCNT","RSPCOD","RSPMSG"};
	
	public static final String[] WECHATRECEIVE_ASK = {"loginType","mobile"};
	public static final String[] WECHATRECEIVE_ASK1 = {"mobile"};
	public static final String[] WECHATRECEIVE_BACK = {"qrcode_url","RSPCOD","RSPMSG"};
	
	public static final String[] WECHATRECEIVE1_BACK = {"login_url","RSPCOD","RSPMSG"};
	
	public static final String[] GOWECHATRECEIVE_ASK = {"mercNum","shopName"};
	public static final String[] GOWECHATRECEIVE_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] HEADQUARTERS_ASK = {"mobile","payMonth"};
	public static final String[] HEADQUARTERS_BACK = {"mobile","payMonth","is_subbranch","RSPCOD","RSPMSG"};
	
	public static final String[] PAYMENTCOUPON_ASK = {"TRANCODE","ACTID"};
	public static final String[] PAYMENTCOUPON_BACK = {"PAYMENTMINSINGAMT","PAYMENTMAXSINGAMT","PAYMENTCOUNT","AVAAMT","URGENTFATE","RSPCOD","RSPMSG"};
	
	public static final String[] ADDCREDITCARD_ASK = {"TRANCODE","ACTID","ISSUER","FRPID","CARDCODE"};
	public static final String[] ADDCREDITCARD_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] WECHATLIST_ASK = {"mobile","payMonth","is_subbranch"};
	public static final String[] WECHATLIST_BACK = {"orderCount","payDate","totalFee","shopCodeMobile","isSubbranch","shopName"};
	
	public static final String[] WECHATLISTDTL_ASK = {"mobile","payMonth","is_subbranch","more","flag"};
	public static final String[] WECHATLISTDTL_BACK = {"id","shopCodeId","totalFee","shopCodeCustid","shopCodeMobile","totalFee","orderNo","status","payDate","createTime","payTime"};
	
	public static final String[] BANK_ASK = {"TRANCODE"};
	public static final String[] BANK_BACK = {"FRPID","BANKNAME","IMG_URL","RSPCOD","RSPMSG"};
	
	public static final String[] CERTIFICATIONBEFORE_ASK = {"TRANCODE","MERCNUM"};
	public static final String[] CERTIFICATIONBEFORE_BACK = {"IDCARDPIC","IDCARDPICSTA","IDCARDBACKPHOTO","IDCARDBANKPICSTA","BANKCARDPHOTO","FRYHKIMGPATHSTA","PERSONPHOTO","CUSTPICSTA","MERCNAM","CORPORATEIDENTITY","BANKACCOUNT","BANKNAME","IMG_URL","PROVID","PROVNAME","CITYID","CITYNAME","AREAID","AREANAME","RSPCOD","RSPMSG"};
	
	public static final String[] NEWAUTHENTICATION_ASK = {"TRANCODE","MERCNUM","IDCARDPIC","IDCARDBACKPHOTO","BANKCARDPHOTO","PERSONPHOTO","MERCNAM","CORPORATEIDENTITY","BANKACCOUNT","PROVID","CITYID","AREAID"};
	public static final String[] NEWAUTHENTICATION_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] SMALLTICKETLIST_ASK = {"MERCNUM","PRINTTYPE"};
	public static final String[] SMALLTICKETLIST_BACK = {"shopname","orderno","amount","paytime","paytype","shoptype"};
	
	public static final String[] ALIPAYLIST_ASK = {"mobile","payMonth","lastMonth"};
	public static final String[] ALIPAYLIST_BACK = {"monthTotalCount","monthTotalFee","shopName","mobile"};
	
	public static final String[] ALIPAYBRANCHLIST_ASK = {"mobile","payMonth","lastMonth"};
	public static final String[] ALIPAYBRANCHLIST_BACK = {"payDay","dayTotalFee","dayTotalCount"};
	
	public static final String[] ALIPAYBRANCHDAYLIST_ASK = {"mobile","payDay"};
	public static final String[] ALIPAYBRANCHDAYLIST_BACK = {"gmtPayment","totalFee"};
	
	public static final String[] SHOPSUM_ASK = {"mobile"};
	public static final String[] SHOPSUM_BACK = {"totalPayFee","withDrawFee"};
	
	public static final String[] BRANCHMANAGEMENT_ASK = {"mobile"};
	public static final String[] BRANCHMANAGEMENT_BACK = {"subMobile","shopname","mercnam"};
	
	public static final String[] NEWBRANCH_ASK = {"mainMobile","subMercnam","subMobile","shopName"};
	public static final String[] NEWBRANCH_BACK = {"RSPCOD","RSPMSG"};
	
	public static final String[] TICKETLIST_ASK = {"MERCNUM","PageNum","NumPerPag"};
	public static final String[] TICKETLIST_BACK = {"shopname","paytime","orderno","amount","paytype","shoptype"};
	
	public static final String[] NEWPAY_ASK = {"FRPID","CARDCODE","ISSUER","ORDAMT","MOBILE","CRDFLG","SIGN"};
	public static final String[] NEWPAY_BACK = {"RSPCOD","RSPMSG","returnUrl","requestType","requestId","totalBizType","merchantCode",
													"totalPrice","passThrough","goodsDesc","rePayTimeOut","noticeUrl","encode",
													"successReturnUrl","failReturnUrl","backUrl","bankAccount","cardNumberLock",
													"signature","productId","productName","fund","merAcct","bizType","productNumber"};
	
	public static final String[] NEWADDPAY_ASK = {"FRPID","CARDTEL","CREDCODE","MOBILE","CARDNAME","CARDCODE","EXPIREYEAR","EXPIREMONTH","ISSUER","CVV","CRDFLG"};
	public static final String[] NEWADDPAY_BACK = {"RSPCOD","RSPMSG"};
	
	public static String[] getAsk(int status) {
		switch (status) {
		case HttpUrls.LOGIN:
			return LOGIN_ASK;

		case HttpUrls.REGISTER_VERIFCODE:
			return GET_VERIFCODE_ASK;

		case HttpUrls.REVISE_PASSWORD:
			return REVISE_PASSWORD_ASK;

		case HttpUrls.REGISTER:
			return REGISTER_ASK;

		case HttpUrls.RICH_TREASURE_INFO:
			return RICH_TREASURE_INFO_ASK;

		case HttpUrls.REAL_NAME_AUTHENTICATION:
			return REAL_NAME_AUTH_ASK;

		case HttpUrls.BUSSINESS_INFO:
			return BUSSINESS_INFO_ASK;

		case HttpUrls.REVISE_BANK_INFO:
			return REVISE_BANK_INFO_ASK;

		case HttpUrls.BALANCE_QUERY:
			return BALANCE_QUERY_ASK;

		case HttpUrls.BOSS_RECEIVE:
			return BOSS_RECEIVE_ASK;

		case HttpUrls.DEAL_RECORDS:
			return DEAL_RECORDS_ASK;

		case HttpUrls.KEY_DOWNLOAD:
			return KEY_DOWNLOAD_ASK;

		case HttpUrls.CHECK:
			return CHECK_ASK;

		case HttpUrls.REVOKE_PAY:
			return REVOKE_PAY_ASK;

		case HttpUrls.UPLOAD_SIGN:
			return UPLOAD_SIGN_ASK;

		case HttpUrls.VERSION_UPDATE:
			return VERSION_UPDATE_ASK;

		case HttpUrls.ORADSEQ_PAY:
			return ORADSEQ_PAY_ASK;
		case HttpUrls.ORADSEQ_UPDATE:
			return ORADSEQ_UPDATE_ASK;
		case HttpUrls.QUERY_BANK_NAME:
			return BANK_NAME_ASK;

		case HttpUrls.QUERY_BANK_CITY:
			return BANK_CITY_ASK;

		case HttpUrls.QUERY_BANK_BRANCH:
			return BANK_BRANCH_ASK;
		case HttpUrls.QUERY_BANK_INFO:
			return QUERY_BANK_INFO_ASK;
		case HttpUrls.IS_REGISTER:
			return IS_REGISTER_ASK;
		case HttpUrls.APPLY_BANKCARD_INFO_CHANGES:
			return APPLY_BANKCARD_INFO_CHANGES_ASK;
		case HttpUrls.APPLY_BANKINFO_REALNAME_CHANGES:
			return APPLY_REALNAME_CHANGES_ASK;

		case HttpUrls.APPLY_PHONENUMBER_CHANGES:
			return APPLY_PHONENUMBER_CHANGES_ASK;

		case HttpUrls.APPLY_PHONENUMBER_REALNAME_CHANGES:
			return APPLY_PHONENUMBER_REALNAME_CHANGES_ASK;

		case HttpUrls.QUERY_PHONENUMBER_CHANGES_VERIFCODE:
			return QUERY_PHONENUMBER_CHANGES_VERIFCODE_ASK;

		case HttpUrls.REGET_PW_VERIF:
			return REGET_PASSWORD_VERIf_ASK;

		case HttpUrls.REGET_PASSWORD:
			return REGET_PASSWORD_ASk;

		case HttpUrls.VERIF_COMMIT:
			return VERIF_COMMIT_ASK;

		case HttpUrls.ORDERPAY:

			return ORDERPAY_ASK;
		case HttpUrls.ORDERPAYQUERY:
			return ORDERPAYQUERY_ASK;

		case HttpUrls.ORDERPAYDETAIL:
			return ORDERPAYDETAIL_ASK;

		case HttpUrls.LOCALYZZD:
			return REGIST_POSZD_ASK;

		case HttpUrls.ORDER_CREATE:
			return ORDER_CREATE_ASK;

		case HttpUrls.ORDER_QUERY_D:
			return ORDER_QUERY_D_ASK;

		case HttpUrls.ORDER_QUERY:
			return ORDER_QUERY_ASK;

		case HttpUrls.ORDER_PAY:
			return ORDER_PAY_ASK;

		case HttpUrls.UP_PAY_PASS:
			return UPPAYPASS_ASK;

		case HttpUrls.RICH_TREASURE_DETAIL:
			return RICH_TREASURE_DETAIL_ASK;

		case HttpUrls.REG_PAY_PW_VCODE:
			return REG_PAY_PW_VCODE_ASK;

		case HttpUrls.REG_PAY_PW_VCODE_VD:
			return REG_PAY_PW_VCODE_VD_ASK;

		case HttpUrls.PAY_UPDATE:
			return PAY_UPDATE_ASK;

		case HttpUrls.WITHDRAWAL_ON_BANK_CARD:
			return WITHDRAWAL_ON_BANK_CARD_ASK;

		case HttpUrls.BASIS_DEPOSIT:
			return BASIS_DEPOSIT_ASK;

		case HttpUrls.BASIS_LIST:
			return BASIS_LIST_ASK;

		case HttpUrls.REGULAR_BASIS_LIST:
			return REGULAR_BASIS_LIST_ASK;

		case HttpUrls.CURRENT_TRANSFER:
			return CURRENT_TRANSFER_ASK;

		case HttpUrls.SET_PAY_PASS:
			return SETPAYPASS_ASK;

		case HttpUrls.ORDER_QUERY_OVER:
			return ORDER_QUERY_D_OVER_ASK;

		case HttpUrls.TANSFER_ACCO:
			return TRANSFERACC_ASK;
			
		case HttpUrls.RATE_QUERY:
			return RATE_QUERY_ASK;
			
		case HttpUrls.GO_RATE:
			return GO_RATE_ASK;
			
		case HttpUrls.PHONE_CHARGE:
			return PHONE_CHARGE_ASK;

		case HttpUrls.ORDERINFO:
			return ORDERINFO_ASK;
			
		case HttpUrls.MYCIRCLE:
			return MYCIRCLE_ASK;
			
		case HttpUrls.CREDITQUERY:
			return CREDITQUERY_ASK;
			
		case HttpUrls.CREDITPAY:
			return CREDITPAY_ASK;
			
		case HttpUrls.GETBANKNAME:
			return GETBANKNAME_ASK;
			
		case HttpUrls.BINDINGCARD:
			return BINDINGCARD_ASK;
			
		case HttpUrls.DELETECREDITCARD:
			return DELETECREDITCARD_ASK;
			
		case HttpUrls.GROUPING:
			return GROUPING_ASK;
			
//		case HttpUrls.YIBAO:
//			return YIBAO_ASK;
			
		case HttpUrls.EPAY:
			return EPAY_ASK;
			
		case HttpUrls.EPAYNUM:
			return EPAYNUM_ASK;
			
		case HttpUrls.GETEPAYNUM:
			return GETEPAYNUM_ASK;
			
		case HttpUrls.BANKCARDLIST:
			return BANKCARDLIST_ASK;
			
		case HttpUrls.UPLODPIC:
			return UPLODPIC_ASK;
			
		case HttpUrls.NEWAGENT:
			return NEWAGENT_ASK;
			
		case HttpUrls.ACTCODEMANAGE:
			return ACTCODEMANAGE_ASK;
			
		case HttpUrls.CHIRDACT:
			return CHIRDACT_ASK;
			
		case HttpUrls.VERIFICATIONPHONENUM:
			return VERIFICATIONPHONENUM_ASK;
			
		case HttpUrls.ACTCODEINIT:
			return ACTCODEINIT_ASK;
			
		case HttpUrls.AGENTINFO:
			return AGENTINFO_ASK;
			
		case HttpUrls.ACTCODELIST:
			return ACTCODELIST_ASK;
			
		case HttpUrls.ACTCODETRANSFER:
			return ACTCODETRANSFER_ASK;
			
		case HttpUrls.RATEEDIT:
			return RATEEDIT_ASK;
			
		case HttpUrls.FEEDBACK:
			return FEEDBACK_ASK;
			
		case HttpUrls.INCOMEDETAIL:
			return INCOMEDETAIL_ASK;
			
		case HttpUrls.DEPOSITPURSE:
			return DEPOSITPURSE_ASK;
			
		case HttpUrls.PROMOTIONEARNINGS:
			return PROMOTIONEARNINGS_ASK;
			
		case HttpUrls.RATEWITHDRAWALSEDIT:
			return RATEWITHDRAWALSEDIT_ASK;
			
		case HttpUrls.FINANCIALPRODUCTS:
			return FINANCIALPRODUCTS_ASK;
			
		case HttpUrls.TOCHANGEINTO:
			return TOCHANGEINTO_ASK;
			
		case HttpUrls.TURNOUT:
			return TURNOUT_ASK;
			
		case HttpUrls.INTEGRALEXCHANGE:
			return INTEGRALEXCHANGE_ASK;
			
		case HttpUrls.EXCHANGEINFO:
			return EXCHANGEINFO_ASK;
			
		case HttpUrls.TRANSACTIONTYPE:
			return TRANSACTIONTYPE_ASK;
			
		case HttpUrls.EPAYSAV:
			return EPAYSAV_ASK;
			
		case HttpUrls.UNBINDCARD:
			return UNBINDCARD_ASK;
			
		case HttpUrls.UPDATEBANKCARD:
			return UPDATEBANKCARD_ASK;
			
		case HttpUrls.BUYCODE:
			return BUYCODE_ASK;
			
		case HttpUrls.BINDAGENT:
			return BINDAGENT_ASK;
			
		case HttpUrls.COMITADRESS:
			return COMITADRESS_ASK;
			
		case HttpUrls.AVATARUPLOAD:
			return AVATARUPLOAD_ASK;
			
		case HttpUrls.CHOOSEBANK:
			return CHOOSEBANK_ASK;
			
		case HttpUrls.BANNER:
			return BANNER_ASK;
			
		case HttpUrls.COLLECTIONTREASURE:
			return COLLECTIONTREASURE_ASK;
			
		case HttpUrls.REALNAMEAUTHENTICATION:
			return REALNAMEAUTHENTICATION_ASK;
			
		case HttpUrls.INTHEQUERY:
			return INTHEQUERY_ASK;
			
		case HttpUrls.CODETRANSFER:
			return CODETRANSFER_ASK;
			
		case HttpUrls.OWNERLICENSEPLATE:
			return OWNERLICENSEPLATE_ASK;
			
		case HttpUrls.OWNERLICENSEPLATE1:
			return OWNERLICENSEPLATE1_ASK;
			
		case HttpUrls.UNBUNDLING:
			return UNBUNDLING_ASK;
			
		case HttpUrls.ETCLIST:
			return ETCLIST_ASK;
			
		case HttpUrls.PAYMENTCOUPON:
			return PAYMENTCOUPON_ASK;
			
		case HttpUrls.ADDCREDITCARD:
			return ADDCREDITCARD_ASK;
			
		case HttpUrls.BANK:
			return BANK_ASK;
			
		case HttpUrls.CERTIFICATIONBEFORE:
			return CERTIFICATIONBEFORE_ASK;
			
		case HttpUrls.NEWAUTHENTICATION:
			return NEWAUTHENTICATION_ASK;
			
		default:
			return null;
		}
	}

	public static String[] getBack(int status) {
		switch (status) {
		case HttpUrls.LOGIN:
			return LOGIN_BACK;

		case HttpUrls.REGISTER_VERIFCODE:
			return GET_VERIFCODE_BACK;

		case HttpUrls.REVISE_PASSWORD:
			return REVISE_PASSWORD_BACK;

		case HttpUrls.RICH_TREASURE_INFO:
			return RICH_TREASURE_INFO_BACK;

		case HttpUrls.REGISTER:
			return REGISTER_BACK;

		case HttpUrls.REAL_NAME_AUTHENTICATION:
			return REAL_NAME_AUTH_BACK;

		case HttpUrls.BUSSINESS_INFO:
			return BUSSINESS_INFO_BACK;

		case HttpUrls.REVISE_BANK_INFO:
			return REVISE_BANK_INFO_BACK;

		case HttpUrls.BALANCE_QUERY:
			return BALANCE_QUERY_BACK;

		case HttpUrls.BOSS_RECEIVE:
			return BOSS_RECEIVE_BACK;

		case HttpUrls.DEAL_RECORDS:
			return DEAL_RECORDS_BACK;

		case HttpUrls.KEY_DOWNLOAD:
			return KEY_DOWNLOAD_BACK;

		case HttpUrls.CHECK:
			return CHECK_BACK;

		case HttpUrls.REVOKE_PAY:
			return REVOKE_PAY_BACK;

		case HttpUrls.UPLOAD_SIGN:
			return UPLOAD_SIGN_BACK;

		case HttpUrls.VERSION_UPDATE:
			return VERSION_UPDATE_BACK;
		case HttpUrls.ORADSEQ_PAY:
			return ORADSEQ_PAY_BACK;
		case HttpUrls.ORADSEQ_UPDATE:
			return ORADSEQ_UPDATE_BACK;
		case HttpUrls.QUERY_BANK_NAME:
			return BANK_NAME_BACK;

		case HttpUrls.QUERY_BANK_CITY:
			return BANK_CITY_BACK;

		case HttpUrls.QUERY_BANK_BRANCH:
			return BANK_BRANCH_BACK;
		case HttpUrls.QUERY_BANK_INFO:
			return QUERY_BANK_INFO_BACK;
		case HttpUrls.IS_REGISTER:
			return IS_REGISTER_BACK;
		case HttpUrls.APPLY_BANKCARD_INFO_CHANGES:
			return APPLY_BANKCARD_INFO_CHANGES_BACK;
		case HttpUrls.APPLY_BANKINFO_REALNAME_CHANGES:
			return APPLY_REALNAME_CHANGES_BACK;
		case HttpUrls.APPLY_PHONENUMBER_CHANGES:
			return APPLY_PHONENUMBER_CHANGES_BACK;

		case HttpUrls.APPLY_PHONENUMBER_REALNAME_CHANGES:
			return APPLY_PHONENUMBER_REALNAME_CHANGES_BACK;

		case HttpUrls.QUERY_PHONENUMBER_CHANGES_VERIFCODE:
			return QUERY_PHONENUMBER_CHANGES_VERIFCODE_BACK;

		case HttpUrls.REGET_PW_VERIF:
			return REGET_PASSWORD_VERIF_BACK;

		case HttpUrls.REGET_PASSWORD:
			return REGET_PASSWORD_BACK;

		case HttpUrls.VERIF_COMMIT:
			return VERIF_COMMIT_BACK;

		case HttpUrls.ORDERPAY:
			return ORDERPAY_BACK;

		case HttpUrls.ORDERPAYQUERY:
			return ORDERPAYQUERY_BACK;

		case HttpUrls.ORDERPAYDETAIL:
			return ORDERPAYDETAIL_BACK;

		case HttpUrls.LOCALYZZD:
			return REGIST_POSZD_BACK;

		case HttpUrls.ORDER_CREATE:
			return ORDER_CREATE_BACK;

		case HttpUrls.ORDER_QUERY_D:
			return ORDER_QUERY_D_BACK;

		case HttpUrls.ORDER_QUERY:
			return ORDER_QUERY_BACK;

		case HttpUrls.ORDER_PAY:
			return ORDER_PAY_BACK;

		case HttpUrls.UP_PAY_PASS:
			return UPPAYPASS_BACK;

		case HttpUrls.RICH_TREASURE_DETAIL:
			return RICH_TREASURE_DETAIL_BACK;

		case HttpUrls.REG_PAY_PW_VCODE:
			return REG_PAY_PW_VCODE_BACK;

		case HttpUrls.REG_PAY_PW_VCODE_VD:
			return REG_PAY_PW_VCODE_VD_BACK;

		case HttpUrls.PAY_UPDATE:
			return PAY_UPDATE_BACK;

		case HttpUrls.WITHDRAWAL_ON_BANK_CARD:
			return WITHDRAWAL_ON_BANK_CARD_BACK;

		case HttpUrls.BASIS_DEPOSIT:
			return BASIS_DEPOSIT_BACK;

		case HttpUrls.BASIS_LIST:
			return BASIS_LIST_BACK;

		case HttpUrls.REGULAR_BASIS_LIST:
			return REGULAR_BASIS_LIST_BACK;

		case HttpUrls.CURRENT_TRANSFER:
			return CURRENT_TRANSFER_BACK;

		case HttpUrls.SET_PAY_PASS:
			return SETPAYPASS_BACK;

		case HttpUrls.ORDER_QUERY_OVER:
			return ORDER_QUERY_D_OVER_BACK;
			
		case HttpUrls.TANSFER_ACCO:
			return TRANSFERACC_BACK;
			
		case HttpUrls.RATE_QUERY:
			return RATE_QUERY_BACK;
			
		case HttpUrls.GO_RATE:
			return GO_RATE_BACK;
			
		case HttpUrls.RECHARGE:	
			return RECHARGE_BACK;
			
		case HttpUrls.PHONE_CHARGE:	
			return PHONE_CHARGE_BACK;
			
		case HttpUrls.ORDERINFO:	
			return ORDERINFO_BACK;
			
		case HttpUrls.MYCIRCLE:	
			return MYCIRCLE_BACK;
			
		case HttpUrls.CREDITQUERY:	
			return CREDITQUERY_BACK;
			
		case HttpUrls.CREDITPAY:	
			return CREDITPAY_BACK;
			
		case HttpUrls.GETBANKNAME:	
			return GETBANKNAME_BACK;
			
		case HttpUrls.BINDINGCARD:	
			return BINDINGCARD_BACK;
			
		case HttpUrls.DELETECREDITCARD:	
			return BINDINGCARD_BACK;
			
		case HttpUrls.GROUPING:	
			return GROUPING_BACK;
			
//		case HttpUrls.YIBAO:	
//			return YIBAO_BACK;
			
		case HttpUrls.EPAY:	
			return EPAY_BACK;
			
		case HttpUrls.EPAYNUM:	
			return EPAYNUM_BACK;
			
		case HttpUrls.GETEPAYNUM:	
			return GETEPAYNUM_BACK;
			
		case HttpUrls.BANKCARDLIST:	
			return BANKCARDLIST_BACK;
			
		case HttpUrls.UPLODPIC:	
			return UPLODPIC_BACK;

		case HttpUrls.NEWAGENT:	
			return NEWAGENT_BACK;
			
		case HttpUrls.ACTCODEMANAGE:	
			return ACTCODEMANAGE_BACK;
			
		case HttpUrls.CHIRDACT:	
			return CHIRDACT_BACK;
			
		case HttpUrls.VERIFICATIONPHONENUM:	
			return VERIFICATIONPHONENUM_BACK;
			
		case HttpUrls.ACTCODEINIT:	
			return ACTCODEINIT_BACK;
			
		case HttpUrls.AGENTINFO:	
			return AGENTINFO_BACK;
			
		case HttpUrls.ACTCODELIST:	
			return ACTCODELIST_BACK;
			
		case HttpUrls.ACTCODETRANSFER:	
			return ACTCODETRANSFER_BACK;
			
		case HttpUrls.RATEEDIT:	
			return RATEEDIT_BACK;
			
		case HttpUrls.FEEDBACK:	
			return FEEDBACK_BACK;
			
		case HttpUrls.INCOMEDETAIL:	
			return INCOMEDETAIL_BACK;
			
		case HttpUrls.DEPOSITPURSE:	
			return DEPOSITPURSE_BACK;
			
		case HttpUrls.PROMOTIONEARNINGS:	
			return PROMOTIONEARNINGS_BACK;
			
		case HttpUrls.RATEWITHDRAWALSEDIT:	
			return RATEWITHDRAWALSEDIT_BACK;
			
		case HttpUrls.FINANCIALPRODUCTS:	
			return FINANCIALPRODUCTS_BACK;
			
		case HttpUrls.TOCHANGEINTO:	
			return TOCHANGEINTO_BACK;
			
		case HttpUrls.TURNOUT:	
			return TURNOUT_BACK;
			
		case HttpUrls.INTEGRALEXCHANGE:	
			return INTEGRALEXCHANGE_BACK;
			
		case HttpUrls.EXCHANGEINFO:	
			return EXCHANGEINFO_BACK;
			
		case HttpUrls.TRANSACTIONTYPE:	
			return TRANSACTIONTYPE_BACK;
			
		case HttpUrls.EPAYSAV:	
			return EPAYSAV_BACK;
			
		case HttpUrls.UNBINDCARD:	
			return UNBINDCARD_BACK;
			
		case HttpUrls.UPDATEBANKCARD:	
			return UPDATEBANKCARD_BACK;
			
		case HttpUrls.BUYCODE:	
			return BUYCODE_BACK;
			
		case HttpUrls.BINDAGENT:	
			return BINDAGENT_BACK;
			
		case HttpUrls.COMITADRESS:	
			return COMITADRESS_BACK;
			
		case HttpUrls.AVATARUPLOAD:	
			return AVATARUPLOAD_BACK;
			
		case HttpUrls.BANNER:	
			return BANNER_BACK;
			
		case HttpUrls.COLLECTIONTREASURE:	
			return COLLECTIONTREASURE_BACK;
			
		case HttpUrls.REALNAMEAUTHENTICATION:	
			return REALNAMEAUTHENTICATION_BACK;
			
		case HttpUrls.INTHEQUERY:	
			return INTHEQUERY_BACK;
			
		case HttpUrls.CODETRANSFER:	
			return CODETRANSFER_BACK;
			
		case HttpUrls.OWNERLICENSEPLATE:	
			return OWNERLICENSEPLATE_BACK;
			
		case HttpUrls.OWNERLICENSEPLATE1:	
			return OWNERLICENSEPLATE1_BACK;
			
		case HttpUrls.UNBUNDLING:	
			return UNBUNDLING_BACK;
			
		case HttpUrls.ETCLIST:	
			return ETCLIST_BACK;
			
		case HttpUrls.PAYMENTCOUPON:	
			return PAYMENTCOUPON_BACK;
			
		case HttpUrls.ADDCREDITCARD:	
			return ADDCREDITCARD_BACK;
			
		case HttpUrls.CERTIFICATIONBEFORE:	
			return CERTIFICATIONBEFORE_BACK;
			
		case HttpUrls.NEWAUTHENTICATION:	
			return NEWAUTHENTICATION_BACK;
		default:
			return null;
		}
	}
}
