package cn.xf.basedemo.common.utils.pay;

import cn.xf.basedemo.common.model.pay.AlipayTransRsqVo;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

@Slf4j
public class AliPayUtil {

    //支付路径
    private static String payPath;
    private static PayInfo payInfo;
    private static String gatewayUrl = "https://openapi.alipay.com/gateway.do";//"https://openapi.alipaydev.com/gateway.do";
    private static String format = "json";
    private static String charset = "utf-8";
    private static String signType = "RSA2";

    @Data
    static class PayInfo {
        String appId;
        String appCertPath;
        String alipayCertPath;
        String rootCertPath;
        String rsaPrivateKey;
    }

    public AliPayUtil(String path) {
        payPath = path;
        String path1 = "d:/pay/";
        path1 = "";
        if (path.equals("a")) {
            payInfo = new PayInfo();
            payInfo.setAppId("");
            payInfo.setAppCertPath(path1 + "15080.crt");
            payInfo.setAlipayCertPath(path1 + "alipayCertPublicKey_RSA2.crt");
            payInfo.setRootCertPath(path1 + "alipayRootCert.crt");
            payInfo.setRsaPrivateKey(readFileContent(path1 + "private.key"));
        }
    }

    @SneakyThrows
    public AlipayTransRsqVo transfer(String trans_no, String account, BigDecimal money, int type, String trueName, String mark) {
        AlipayTransRsqVo resVo = new AlipayTransRsqVo();
        // 创建API客户端实例
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(gatewayUrl);
        certAlipayRequest.setAppId(payInfo.appId);
        certAlipayRequest.setPrivateKey(payInfo.rsaPrivateKey);
        certAlipayRequest.setFormat(format);
        certAlipayRequest.setCharset(charset);
        certAlipayRequest.setSignType(signType);
        //设置应用公钥证书路径
        certAlipayRequest.setCertPath(payInfo.getAppCertPath());
        //设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(payInfo.alipayCertPath);
        //certAlipayRequest.setAlipayPublicCertContent(AlipaySignature.getAlipayPublicKey(payInfo.alipayCertPath));
        //设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(payInfo.getRootCertPath());
        AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);


        /** 实例化具体API对应的request类，类名称和接口名称对应,当前调用接口名称alipay.fund.trans.uni.transfer(单笔转账接口) **/
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();

        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();

        /******必传参数******/
//      商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯一
        model.setOutBizNo(trans_no);

//     转账金额，TRANS_ACCOUNT_NO_PWD产品取值最低0.1
        model.setTransAmount(String.valueOf(money.setScale(2, RoundingMode.HALF_UP)));

//      销售产品码。单笔无密转账固定为 TRANS_ACCOUNT_NO_PWD。
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");

//      业务场景。单笔无密转账固定为 DIRECT_TRANSFER
        model.setBizScene("DIRECT_TRANSFER");

//      转账业务的标题，用于在支付宝用户的账单里显示。
        model.setOrderTitle("订单标题");

//      收款方信息
        Participant payeeInfo = new Participant();
//      参与方的标识类型,设置ALIPAY_USER_ID或者ALIPAY_LOGON_ID
//      ALIPAY_USER_ID：支付宝会员的用户 ID，可通过 获取会员信息 获取：https://opendocs.alipay.com/open/284/106000
//      ALIPAY_LOGON_ID：支付宝登录号，支持邮箱和手机号格式。
        if (type == 1) {
            payeeInfo.setIdentityType("ALIPAY_USER_ID");
        }
        if (type == 2) {
            payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
        }

//      参与方的标识 ID，根据identity_type类型选择对应信息
//      当 identity_type=ALIPAY_USER_ID 时，填写支付宝用户 UID。示例值：2088123412341234。
//      当 identity_type=ALIPAY_LOGON_ID 时，填写支付宝登录号。示例值：186xxxxxxxx。
        payeeInfo.setIdentity(account);
//      参与方真实姓名。如果非空，将校验收款支付宝账号姓名一致性。
//      当 identity_type=ALIPAY_LOGON_ID 时，本字段必填。
        payeeInfo.setName(trueName);
        model.setPayeeInfo(payeeInfo);

        /******可选参数******/
//      业务备注
        model.setRemark(mark);

//      转账业务请求的扩展参数
//        payer_show_name_use_alias：是否展示付款方别名，可选，收款方在支付宝账单中可见。枚举支持：
//        * true：展示别名，将展示商家支付宝在商家中心 商户信息 > 商户基本信息 页面配置的 商户别名。
//        * false：不展示别名。默认为 false。
        // model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");

        request.setBizModel(model);

        try {
            // 发送请求并获取响应
            // AlipayFundTransToaccountTransferResponse response = alipayClient.certificateExecute(request);
            AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
            // 处理响应，通常需要检查out_biz_no，trade_no，和resultCode
            if (response.isSuccess()) {
                resVo.setSuccess(true);
                resVo.setMsg(response.getSubMsg());
                //resVo.setBody(JSON.parseObject(response.getBody(), AliPayBodyVo.class));
                resVo.setOrderId(response.getOrderId());
                resVo.setStatus(response.getStatus());
                resVo.setPayFundOrderID(response.getPayFundOrderId());
                resVo.setTransDate(response.getTransDate());
                resVo.setOutBizNo(response.getOutBizNo());
                resVo.setSettleSerialNo(response.getSettleSerialNo());
                resVo.setAmount(response.getAmount());


                log.info("转账成功: " + response.getBody());
            } else {
                resVo.setSuccess(false);
                resVo.setMsg(response.getSubMsg());
                resVo.setCode(response.getCode());
                resVo.setSubCode(response.getSubCode());
                log.info("转账失败: " + response.getSubCode() + " - " + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            //e.printStackTrace();
            resVo.setSuccess(false);
            resVo.setMsg(e.getMessage());
        }

        return resVo;
    }

    public static PrivateKey getPrivateKeyFromPath(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] keyBytes = new byte[fis.available()];
            fis.read(keyBytes);
            fis.close();

            String privateKeyPEM = new String(keyBytes);
            // 移除PEM头部和尾部
            privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
            privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
            // 对于Windows平台，请使用"\r\n"作为分隔符；对于Linux/Mac平台，应使用"\n"
            privateKeyPEM = privateKeyPEM.replaceAll("\\r\\n|\\n|\\r", "");

            byte[] decoded = Base64.decodeBase64(privateKeyPEM);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readFileContent(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}
