package com.shareshenghuo.app.shop.function;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.function.mallSettled.MallStatus;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.response.FileUploadResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.shop.util.BitmapTool;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.util.PictureUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.dialog.PickPhotoWindow;

/**
 *
 * 商家入驻-上传资料
 */
public class MallSettled_sczl_Activity extends BaseTopActivity implements
		NewMessageCallback, OnClickListener {

	public static MallSettled_sczl_Activity instance = null;

	private NewChatMsgWorker newMsgWatcher;// 监听聊天

	public static String http_yingyezhizhao_url = "";// 营业执照地址
	public static String http_faren1_url1 = "";// 法人1地址
	public static String http_faren1_url2 = "";// 法人2地址
	public static String http_faren1_url3 = "";// 法人3地址
	public static String http_teshu_url = "";// 特殊行业地址

	private String yingyezhizhao_url = "";// 营业执照地址
	private String faren1_url1 = "";// 法人1地址
	private String faren1_url2 = "";// 法人2地址
	private String faren1_url3 = "";// 法人3地址
	private String teshu_url = "";// 特殊行业地址

	private ImageView yingyezhizhao_img;// 营业执照照片
	private ImageView farenimg_1, farenimg_2, farenimg_3;// 法人正反手持照片
	private ImageView teshuhangye_img;// 特殊行业许可证照片

	private TextView shangjiaruzhu_jbxx_text;// 基本信息，标题
	private TextView real_pointimg;// 基本信息图标
	private TextView tvs1;// 基本信息后线

	private TextView shangjiaruzhu_sczl_text;// 上传资料，标题
	private TextView real_pointimg1;// 上传资料图标

	private Button btn_mall_settled_tijiao;// 上传资料，提交按钮.下一步

	private String yyzzmc, shtym, frxm, frsfzh, frlxfs, jyfl;// 第一个界面过来的数据
	private int updata_img_num = 0;// 用户要上传的图片张数
	private int upphone_item_num = 0;// 用来记录是否提交了几张图片了.(上传成功的张数)
	Handler my_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			System.out.println("===收到了消息,要上传" + updata_img_num + "张,已上传:"
					+ upphone_item_num);
			switch (msg.arg1) {
			// case 1:
			// System.out.println("==收到了消息,来自1图片");
			// if (updata_img_num == upphone_item_num) {//
			// 如果用户要上传的图片张数等于已经上到服务器的张数,就执行提交方法
			// gotoupdata(); // 提交到服务器
			// }else{
			//
			// }
			// break;
			case 1:
				if (updata_img_num == upphone_item_num) {// 如果用户要上传的图片张数等于已经上到服务器的张数,就执行提交方法
					//gotoupdata(); // 提交到服务器
				} else {
					upPhotos(new File(faren1_url1), "faren1_url1", 0);
				}
				System.out.println("==收到了消息,来自1图片");
				break;
			case 2:
				if (updata_img_num == upphone_item_num) {// 如果用户要上传的图片张数等于已经上到服务器的张数,就执行提交方法
					//gotoupdata(); // 提交到服务器
				} else {
					upPhotos(new File(faren1_url2), "faren1_url2", 0);
				}
				System.out.println("==收到了消息,来自2图片");
				break;
			case 3:
				if (updata_img_num == upphone_item_num) {// 如果用户要上传的图片张数等于已经上到服务器的张数,就执行提交方法
					//gotoupdata(); // 提交到服务器
				} else {
					upPhotos(new File(faren1_url3), "faren1_url3", 0);
				}
				System.out.println("==收到了消息,来自3图片");
				break;
			case 4:
				if (updata_img_num == upphone_item_num) {// 如果用户要上传的图片张数等于已经上到服务器的张数,就执行提交方法
					//gotoupdata(); // 提交到服务器
				} else {
					upPhotos(new File(teshu_url), "teshu_url", 0);
				}
				System.out.println("==收到了消息,来自4图片");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_settled_sczl);
		instance = this;
		getdataByIntent();
		initView();

	}

	public void initView() {
		initTopBar("商城入驻");
		yingyezhizhao_img = getView(R.id.yingyezhizhao_img);// 营业执照照片
		farenimg_1 = getView(R.id.farenimg_1);// 手持正反
		farenimg_2 = getView(R.id.farenimg_2);//
		farenimg_3 = getView(R.id.farenimg_3);//
		teshuhangye_img = getView(R.id.teshuhangye_img);// 特殊行业许可证

		btn_mall_settled_tijiao = getView(R.id.btn_mall_settled_tijiao);// 提交资料按钮

		getView(R.id.yingyezhizhao_img).setOnClickListener(this);
		getView(R.id.farenimg_1).setOnClickListener(this);
		getView(R.id.farenimg_2).setOnClickListener(this);
		getView(R.id.farenimg_3).setOnClickListener(this);
		getView(R.id.teshuhangye_img).setOnClickListener(this);

		getView(R.id.btn_mall_settled_tijiao).setOnClickListener(this);// 提交资料按钮事件

		shangjiaruzhu_jbxx_text = getView(R.id.shangjiaruzhu_jbxx_text);// 基本信息，下部文字

		real_pointimg = getView(R.id.real_pointimg);// 基本信息图标
		real_pointimg.setBackgroundResource(R.drawable.bg_circle_orange);// 基本信息圆圈，红
		real_pointimg.setTextColor(getResources().getColor(R.color.text_white));// 文字颜色设置白

		tvs1 = getView(R.id.tvs1);// 基本信息后竖条
		tvs1.setBackgroundResource(R.color.text_orange);

		shangjiaruzhu_sczl_text = getView(R.id.shangjiaruzhu_sczl_text);// 上传资料，下部文字
		shangjiaruzhu_sczl_text.setTextColor(getResources().getColor(
				R.color.text_orange));
		real_pointimg1 = getView(R.id.real_pointimg1);// 上传资料，图标
		real_pointimg1.setBackgroundResource(R.drawable.bg_circle_orange);// 上传资料圆圈，红
		real_pointimg1.setTextColor(getResources().getColor(R.color.text_white));// 上传资料文字颜色设置白

		newMsgWatcher = new NewChatMsgWorker(this, this);
		newMsgWatcher.startWork();
	}

	private void getdataByIntent() {
		Bundle bundle = getIntent().getExtras();
		yyzzmc = bundle.getString("yyzzmc");// 营业执照
		shtym = bundle.getString("shtym");// 社会统一码
		frsfzh = bundle.getString("frsfzh");// 法人身份证号
		frxm = bundle.getString("frxm");// 法人姓名
		frlxfs = bundle.getString("frlxfs");// 法人联系方式
		jyfl = bundle.getString("jyfl");
		System.out.println("===取值成功" + yyzzmc + "," + shtym + "," + frsfzh
				+ "," + frxm + "," + frlxfs + "," + jyfl);
	}

	// 0营业执照；1法人1；2法人2；3法人3；4特殊经营许可证
	private int phone_index = 0;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yingyezhizhao_img:// 营业执照。
			phone_index = 0;
			new PickPhotoWindow(MallSettled_sczl_Activity.this).showAtBottom();
			break;
		case R.id.farenimg_1:// 法人正反面123
			phone_index = 1;
			new PickPhotoWindow(MallSettled_sczl_Activity.this).showAtBottom();
			break;
		case R.id.farenimg_2:
			phone_index = 2;
			new PickPhotoWindow(MallSettled_sczl_Activity.this).showAtBottom();
			break;
		case R.id.farenimg_3:
			phone_index = 3;
			new PickPhotoWindow(MallSettled_sczl_Activity.this).showAtBottom();
			break;
		case R.id.teshuhangye_img:
			phone_index = 4;
			new PickPhotoWindow(MallSettled_sczl_Activity.this).showAtBottom();
			break;
		case R.id.btn_mall_settled_tijiao:// 下一步，审核界面
			// startActivity(new Intent(MallSettled_sczl_Activity.this,
			// MallSettled_sh_Activity.class));
			if (yingyezhizhao_url.equals("") || faren1_url1.equals("")
					|| faren1_url2.equals("") || faren1_url3.equals("")) {// 如果有一个照片为空的话,提示
				T.showShort(MallSettled_sczl_Activity.this, "请确认提交的资料是否齐全!");
			} else {
				if (teshu_url.equals("")) {
					updata_img_num = 4;// 标识一下上传几张图片
				} else {
					updata_img_num = 5;
				}
				System.out.println("===要上传几张:" + updata_img_num);
				// new submitPhotoAsync().execute("");// 后台上传图片
				upPhotos_submit();
			}
			break;
		}
	}

	/*
	 * 获取商家申请状态,根据不同状态,进入不同的界面
	 */
	class submitPhotoAsync extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			ProgressDialogUtil.showProgressDlg(MallSettled_sczl_Activity.this,
					"正在提交资料,请稍后...");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ProgressDialogUtil.dismissProgressDlg();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL
					|| requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
				// 修改头像
				String path = FileUtil.getPath(MallSettled_sczl_Activity.this,
						data.getData());
				System.out.println("===商城返回的图片地址：" + path);
				if (path == null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bmp = extras.getParcelable("data");
						String mypath = BitmapTool.Bitmap2File(
								MallSettled_sczl_Activity.this, bmp).getPath();// bitmap转文件

						if (bmp != null) {
							// upPhoto(BitmapTool.Bitmap2File(MineActivity.this,
							// bmp));
							if (phone_index == 0) {// 营业执照
								yingyezhizhao_url = mypath;
								yingyezhizhao_img.setImageBitmap(bmp);
							}
							if (phone_index == 1) {// 法人1
								faren1_url1 = mypath;
								farenimg_1.setImageBitmap(bmp);
							}
							if (phone_index == 2) {// 法人2
								faren1_url2 = mypath;
								farenimg_2.setImageBitmap(bmp);
							}
							if (phone_index == 3) {// 法人3
								faren1_url3 = mypath;
								farenimg_3.setImageBitmap(bmp);
							}
							if (phone_index == 4) {// 特殊行业
								teshu_url = mypath;
								teshuhangye_img.setImageBitmap(bmp);
							}
							// Toast.makeText(MallSettled_sczl_Activity.this,
							// "haole", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					// Toast.makeText(MallSettled_sczl_Activity.this, "buhao",
					// Toast.LENGTH_SHORT).show();
					if (phone_index == 0) {// 营业执照
						yingyezhizhao_img.setImageBitmap(PictureUtil
								.getSmallBitmap(path));
						yingyezhizhao_url = path;
					}
					if (phone_index == 1) {// 法人1
						farenimg_1.setImageBitmap(PictureUtil
								.getSmallBitmap(path));
						faren1_url1 = path;
					}
					if (phone_index == 2) {// 法人2
						farenimg_2.setImageBitmap(PictureUtil
								.getSmallBitmap(path));
						faren1_url2 = path;
					}
					if (phone_index == 3) {// 法人3
						farenimg_3.setImageBitmap(PictureUtil
								.getSmallBitmap(path));
						faren1_url3 = path;
					}
					if (phone_index == 4) {// 特殊行业
						teshuhangye_img.setImageBitmap(PictureUtil
								.getSmallBitmap(path));
						teshu_url = path;
					}
					// upPhoto(new File(path));
				}
			}
		}
	}

	// BitmapTool.Bitmap2File(MineActivity.this,bmp);
	// private void gotoupdata() {
	// T.showShort(MallSettled_sczl_Activity.this, "开始上传啦");
	// }

	/**
	 * 上传资料
	 */
	private void gotoupdata() {
		System.out.println("=======开始执行上传资料!");

		// AjaxParams aparams = new AjaxParams();
		// aparams.put("appid",
		// UserInfoManager.getAppId(MallSettled_sczl_Activity.this));
		// aparams.put("account",
		// UserInfoManager.getMallShopId(MallSettled_sczl_Activity.this));//用户标识
		// aparams.put("province", MallSettledActivity.bankProvinceid);// 省份名称
		// aparams.put("city", MallSettledActivity.bankCityid);// 城市名称
		// aparams.put("district", MallSettledActivity.bankareid);// 区名称
		// aparams.put("type_id", jyfl);// 经营分类
		// aparams.put("company_name", yyzzmc);// 营业执照名称
		// aparams.put("contacts_name", frxm);// 联系人.法人名称
		// aparams.put("contacts_phone", frlxfs);// 法人联系方式
		// aparams.put("id_card_no", frsfzh);// 法人身份证号
		// aparams.put("organization_code", shtym);// 组织机构代码,社会统一码
		// aparams.put("zhizhao", http_yingyezhizhao_url);// 营业执照图片
		// aparams.put("idcard", http_faren1_url1 + "," + http_faren1_url2 + ","
		// + http_faren1_url3);// 手持照片正反面
		// aparams.put("special_industry_voucher", http_teshu_url);// 特殊行业
		//
		// System.out.println("===上传商户参数是:" + aparams.getParamString());

		upphone_item_num = 0;
		// T.showShort(MallSettled_sczl_Activity.this, "正在提交,请稍后...");

		RequestParams params2 = new RequestParams();
		params2.addBodyParameter("appid",
				UserInfoManager.getAppId(MallSettled_sczl_Activity.this));
		params2.addBodyParameter("account",
				UserInfoManager.getMallShopId(MallSettled_sczl_Activity.this));// 用户标识
		params2.addBodyParameter("province", MallSettledActivity.bankProvinceid);// 省份名称
		params2.addBodyParameter("city", MallSettledActivity.bankCityid);// 城市名称
		params2.addBodyParameter("district", MallSettledActivity.bankareid);// 区名称
		params2.addBodyParameter("type_id", jyfl);// 经营分类
		params2.addBodyParameter("company_name", yyzzmc);// 营业执照名称
		params2.addBodyParameter("contacts_name", frxm);// 联系人.法人名称
		params2.addBodyParameter("contacts_phone", frlxfs);// 法人联系方式
		params2.addBodyParameter("id_card_no", frsfzh);// 法人身份证号
		params2.addBodyParameter("organization_code", shtym);// 组织机构代码,社会统一码
		params2.addBodyParameter("zhizhao", http_yingyezhizhao_url);// 营业执照图片
		params2.addBodyParameter("idcard", http_faren1_url1 + ","
				+ http_faren1_url2 + "," + http_faren1_url3);// 手持照片正反面
		params2.addBodyParameter("special_industry_voucher", http_teshu_url);// 特殊行业

		new HttpUtils().send(HttpMethod.POST, Api.HOST,
				params2, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						ProgressDialogUtil
								.showProgressDlg(
										MallSettled_sczl_Activity.this,
										"正在提交资料,请稍后...");
						// T.showShort(getActivity(), "正在处理请稍后...");
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						T.showShort(MallSettled_sczl_Activity.this,
								"提交失败，请重新提交");
						btn_mall_settled_tijiao.setEnabled(true);// 设置提交按钮可点击
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// T.showShort(MallSettled_sczl_Activity.this, "提交返回"
						// + arg0.result);
						MallStatus ms = new Gson().fromJson(arg0.result,
								MallStatus.class);
						if (ms.getStatus() == 1) {// 等于1说明成功
							ProgressDialogUtil.dismissProgressDlg();
							try {
								MallSettledActivity.instance.finish();
							} catch (Exception e) {
							}
							finish();// 关闭本界面
							Intent intent = new Intent();
							intent.setClass(MallSettled_sczl_Activity.this,
									MallSettled_sh_Activity.class);
							startActivity(intent);
						} else {
							T.showShort(MallSettled_sczl_Activity.this,
									"提交失败，请重新提交！");
						}
					}
				});

		// AjaxParams aparams = new AjaxParams();
		// aparams.put("appid",
		// UserInfoManager.getAppId(MallSettled_sczl_Activity.this));
		// aparams.put("account",
		// UserInfoManager.getMallShopId(MallSettled_sczl_Activity.this));//
		// 用户标识
		// aparams.put("province", MallSettledActivity.bankProvinceid);// 省份名称
		// aparams.put("city", MallSettledActivity.bankCityid);// 城市名称
		// aparams.put("district", MallSettledActivity.bankareid);// 区名称
		// aparams.put("type_id", jyfl);// 经营分类
		// aparams.put("company_name", yyzzmc);// 营业执照名称
		// aparams.put("contacts_name", frxm);// 联系人.法人名称
		// aparams.put("contacts_phone", frlxfs);// 法人联系方式
		// aparams.put("id_card_no", frsfzh);// 法人身份证号
		// aparams.put("organization_code", shtym);// 组织机构代码,社会统一码
		// aparams.put("zhizhao", http_yingyezhizhao_url);// 营业执照图片
		// aparams.put("idcard", http_faren1_url1 + "," + http_faren1_url2 + ","
		// + http_faren1_url3);// 手持照片正反面
		// aparams.put("special_industry_voucher", http_teshu_url);// 特殊行业
		//
		// System.out.println("===上传商户参数是:" + aparams.getParamString());
		// FinalHttp fh = new FinalHttp();
		// fh.post("http://119.23.146.39/apply.php", aparams,
		// new AjaxCallBack<Object>() {
		// @Override
		// public void onSuccess(Object t) {
		// System.out.println("====qingqiu chenggong:"
		// + t.toString());
		// MallStatus ms = new Gson().fromJson(t.toString(),
		// MallStatus.class);
		// if (ms.getStatus() == 1) {// 等于1说明成功
		// MallStringUtils.mallState = 0;// 设置为正在审核状态
		// try {
		// MallSettledActivity.instance.finish();
		// } catch (Exception e) {
		// }
		// finish();// 关闭本界面
		// Intent intent = new Intent();
		// intent.setClass(MallSettled_sczl_Activity.this,
		// MallSettled_sh_Activity.class);
		// startActivity(intent);
		// } else {
		// T.showShort(MallSettled_sczl_Activity.this,
		// "提交失败，请重新提交！");
		// }
		// super.onSuccess(t);
		// }
		//
		// @Override
		// public void onFailure(Throwable t, int errorNo,
		// String strMsg) {
		// T.showShort(MallSettled_sczl_Activity.this,
		// "提交失败，请重新提交！");
		// super.onFailure(t, errorNo, strMsg);
		// }
		// });
	}

	/**
	 * 提交图片到服务器获取返回路径
	 */
	private void upPhotos_submit() {
		btn_mall_settled_tijiao.setEnabled(false);// 设置提交按钮不可点击
		ProgressDialogUtil.showProgressDlg(MallSettled_sczl_Activity.this,
				"正在提交资料,请稍后...");
		upphone_item_num = 0;// 开始上传前,把上传的标志设置为0
		upPhotos(new File(yingyezhizhao_url), "yingyezhizhao_url", 0);
		// upPhotos(new File(faren1_url1), "faren1_url1");
		// upPhotos(new File(faren1_url2), "faren1_url2");
		// upPhotos(new File(faren1_url3), "faren1_url3");
		// upPhotos(new File(teshu_url), "teshu_url");

		// int temp = 0;
		// while (a == 5) {
		// try {
		// if (temp > 15) {
		// temp = temp + 1;
		// }
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// if (temp > 15) {
		// T.showShort(MallSettled_sczl_Activity.this, "提交超时,请重试!");
		// break;
		// }
		// if (a == 5) {
		// break;
		// }
		//
		// }
		// System.out.println("====开始上传其他资料");
		// a = 0;
		// if (http_yingyezhizhao_url.equals("") || http_faren1_url1.equals("")
		// || http_faren1_url2.equals("") || http_faren1_url3.equals("")
		// || http_teshu_url.equals("")) {
		// return false;// 如果有一张图片没上传成功的话,就重新请求
		// } else {
		// return true;
		// }

	}

	/**
	 *
	 * @param file要上传的文件
	 * @param type要上传的文件标识
	 * @param up_num标识是否进行第二次上传
	 */
	public void upPhotos(File file, final String type, final int up_num) {
		try {
			// 压缩图片
			String compressPath = PictureUtil.compressImage(
					MallSettled_sczl_Activity.this, file.getPath(),
					file.getName(), 65);
			file = new File(compressPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestParams params = new RequestParams();
		params.addBodyParameter("business_type", "posp");
		params.addBodyParameter("1", file);
		Log.e("",
				"" + UserInfoManager.getUserId(MallSettled_sczl_Activity.this)
						+ "");
		Log.e("", "" + Api.URL_UPLOAD_FILE);
		final File file_temp = file;// 以防上传后出错,复制一个文件对象
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// ProgressDialogUtil.showProgressDlg(
						// MallSettled_sczl_Activity.this, "请稍候..");
						super.onStart();
					}

					@Override
					public void onFailure(HttpException e, String msg) {// 上传图片错误的话,尝试第二次提交,第二次错误的话,中止操作,不提交商家入驻信息

						if (up_num == 0) {
							int a = up_num;
							a = 1;
							upPhotos(file_temp, type, a);// 尝试二次提交图片.
						} else {// 尝试了一遍之后,还是失败的话,就中止上传.让用户点击重新提交
							T.showNetworkError(MallSettled_sczl_Activity.this);
							ProgressDialogUtil.dismissProgressDlg();
							btn_mall_settled_tijiao.setEnabled(true);// 设置提交按钮可点击
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						if (resp.statusCode == 200 && resp.result != null) {
							FileUploadResponse bean = new Gson().fromJson(
									resp.result, FileUploadResponse.class);
							if (Api.SUCCEED == bean.result_code) {
								String URL = bean.data.get(0);
								if (type.equals("yingyezhizhao_url")) {
									http_yingyezhizhao_url = Api.HOSTERMA + URL;
								}
								if (type.equals("faren1_url1")) {
									http_faren1_url1 = Api.HOSTERMA + URL;
								}
								if (type.equals("faren1_url2")) {
									http_faren1_url2 = Api.HOSTERMA + URL;
								}
								if (type.equals("faren1_url3")) {
									http_faren1_url3 = Api.HOSTERMA + URL;
								}
								if (type.equals("teshu_url")) {
									http_teshu_url = Api.HOSTERMA + URL;
								}
								upphone_item_num = upphone_item_num + 1;
								// T.showShort(MallSettled_sczl_Activity.this,
								// "图片上传完毕,第:" + upphone_item_num + "张");

								System.out.println("======已经保存的地址:"
										+ Api.HOSTERMA + URL);

								if (upphone_item_num == updata_img_num) {// 如果上传成功的等于用户选择的
									// T.showShort(MallSettled_sczl_Activity.this,"图片上传完毕,准备提交参数");
									ProgressDialogUtil.dismissProgressDlg();
									gotoupdata();// 开始提交
								}else{
									Message msg = new Message();
									msg.arg1 = upphone_item_num;
									my_handler.sendMessage(msg);// 发消息给主线程
									System.out.println("========已经上传了几张?:"
											+ upphone_item_num);
								}
								// T.showShort(MallSettled_sczl_Activity.this,
								// URL);
								// if (upphone_item_num == 5) {// 第五张传完后,执行上传的借口
								// // gotoupdata(); 临时测试,屏蔽掉了
								// T.showShort(MallSettled_sczl_Activity.this,
								// "图片上传完毕,准备提交参数");
								// }
							} else {
								int a = up_num;
								a = 1;
								upPhotos(file_temp, type, a);// 尝试二次提交图片.

								// T.showShort(MallSettled_sczl_Activity.this,
								// bean.result_desc);
							}
						}
					}
				});
	}

	@Override
	public void newMessage(int which) {

	}

}
