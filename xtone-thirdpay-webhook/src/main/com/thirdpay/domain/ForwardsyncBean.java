package com.thirdpay.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.common.util.ConfigManager;
import org.common.util.ConnectionService;
import org.common.util.GenerateIdService;

import com.thirdpay.servlet.AlipayCountServlet;
import com.thirdpay.utils.AES;
import com.thirdpay.utils.CheckPayInfo;
import com.thirdpay.utils.ConnectionServicethirdpayCount;
import com.thirdpay.utils.Contents;
import com.thirdpay.utils.HttpUtils;

/**
 * 订单号状态0表示等待同步；1表示同步成功 计划下次处理时间，毫秒数 已经处理次数 目标地址 成功判定条件 appkey or channelId
 * 填入配置的id值 id_type数据库字段对应
 */
public class ForwardsyncBean implements Runnable {
	private static final Logger LOG = Logger.getLogger(ForwardsyncBean.class);
	private Long id;
	private int status;
	private String own_orderId;
	private String sync_status;
	private String next_time;
	private String sendCount;
	private String notify_url;
	private String successCoditions;
	private String appkey;
	private String id_type;
	private String encrypt;
	private String encrypt_key;
	private String forwardString;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOwn_orderId() {
		return own_orderId;
	}

	public void setOwn_orderId(String own_orderId) {
		this.own_orderId = own_orderId;
	}

	public String getSync_status() {
		return sync_status;
	}

	public void setSync_status(String sync_status) {
		this.sync_status = sync_status;
	}

	public String getNext_time() {
		return next_time;
	}

	public void setNext_time(String next_time) {
		this.next_time = next_time;
	}

	public String getSendCount() {
		return sendCount;
	}

	public void setSendCount(String sendCount) {
		this.sendCount = sendCount;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getSuccessCoditions() {
		return successCoditions;
	}

	public void setSuccessCoditions(String successCoditions) {
		this.successCoditions = successCoditions;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public String getEncrypt_key() {
		return encrypt_key;
	}

	public void setEncrypt_key(String encrypt_key) {
		this.encrypt_key = encrypt_key;
	}

	public String getForwardString() {
		return forwardString;
	}

	public void setForwardString(String forwardString) {
		this.forwardString = forwardString;
	}

	public ForwardsyncBean(int status, String own_orderId, String sync_status, String next_time, String sendCount,
			String notify_url, String successCoditions, String appkey, String id_type, String encrypt,
			String encrypt_key, String forwardString) {
		super();
		this.status = status;
		this.own_orderId = own_orderId;
		this.sync_status = sync_status;
		this.next_time = next_time;
		this.sendCount = sendCount;
		this.notify_url = notify_url;
		this.successCoditions = successCoditions;
		this.appkey = appkey;
		this.id_type = id_type;
		this.encrypt = encrypt;
		this.encrypt_key = encrypt_key;
		this.forwardString = forwardString;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		setId(GenerateIdService.getInstance()
				.generateNew(Integer.parseInt(ConfigManager.getConfigData("server.id").trim()), "clicks", 1));

		if (this.id > 0) {
			PreparedStatement ps = null;
			Connection con = null;
			try {

				// DbKey 选择使用的数据库
				con = ConnectionServicethirdpayCount.getInstance().getConnectionForLocal(); // DbKey选择使用config.properties
				ps = con.prepareStatement(
						"insert into `log_async_generals` (id,logId,para01,para02,para03,para04,para05,para06,para07,para08,para09,para10,para11) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");

				int m = 1;
				ps.setLong(m++, this.getId());
				ps.setInt(m++, this.getStatus());
				ps.setString(m++, this.getOwn_orderId());
				ps.setString(m++, this.getSync_status());
				ps.setString(m++, "sendNextTime=" + this.getNext_time());
				ps.setString(m++, "sendCount=" + this.getSendCount());
				ps.setString(m++, this.getNotify_url());
				ps.setString(m++, "SuccessCoditions=" + this.getSuccessCoditions());
				ps.setString(m++, this.getAppkey());
				ps.setString(m++, this.getId_type());
				ps.setString(m++, this.getForwardString());
				ps.setString(m++,"encrypt="+this.getEncrypt() );
				ps.setString(m++,"encrypt_key="+this.getEncrypt_key());
				int i = ps.executeUpdate();

				if ((i + "").equals("1")) {
					LOG.info("-------------------------插入1001成功");
					// 如果插入成功
					// 根据appKey的地址转发payment数据
					String notify_url = this.getNotify_url();
					String own_orderId = this.getOwn_orderId();
					String encrypt_key = this.getEncrypt_key();
					String forwardString = this.getForwardString();
					if (!notify_url.equals("")) {
						LOG.info("---------appKey = " + this.getAppkey() + " --------------own_orderId = " + own_orderId
								+ " -转发数据到指定url = " + this.getNotify_url());
						postPayment(notify_url, own_orderId, this.getEncrypt(), this.getAppkey(), encrypt_key,forwardString);

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * post转发数据
	 * 
	 * @param notify_url
	 * @param ownOrderId
	 * @throws Exception
	 */
	public void postPayment(String notify_url, String ownOrderId, String encrypt, String appkey, String encrypt_key,String forwardString)
			throws Exception {
		String Notencrypt_forwardString = forwardString;//未加密的forwardString
		LOG.info("appkey = " + appkey + " ownOrderId = " + ownOrderId + " 加密前的字串是：" + forwardString + " 加密的key是: "
				+ encrypt_key);

		if ("1".equals(encrypt) && encrypt_key != null && !"".equals(encrypt_key) && encrypt_key.length() == 16) {
			// 加密
			forwardString = AES.Encrypt(forwardString, encrypt_key);
			LOG.info("appkey = " + appkey + " ownOrderId = " + ownOrderId + "加密后的字串是：" + forwardString);

		}

		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("payment", forwardString));

		//返回的数据responseContent
		String responseContent = HttpUtils.post(notify_url, formparams, ownOrderId);

		// 判断返回状态
		if (responseContent.equals("200")) {

			LOG.info(ownOrderId + "返回200 , 更新1001的status为 syncSuccess ");
			// 更新1001的状态并插入1002数据
			CheckPayInfo.Updata1001(ownOrderId, notify_url,Notencrypt_forwardString);
			// CheckPayInfo.InsertInfo(ownOrderId, notify_url);

		} else {
			// 返回不为200重复发送
			LOG.info(ownOrderId + "返回数据不为200 失败 ");
			// 更新1001的下次转发时间为1分钟
			CheckPayInfo.Updata1001Time(ownOrderId,responseContent);
		}

	}
}
