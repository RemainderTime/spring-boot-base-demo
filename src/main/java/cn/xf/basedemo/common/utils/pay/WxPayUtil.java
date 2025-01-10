package cn.xf.basedemo.common.utils.pay;

import cn.xf.basedemo.common.model.pay.RefundInfoRsqDTO;
import cn.xf.basedemo.common.model.pay.TransferQueryVO;
import cn.xf.basedemo.common.model.pay.WechatCashQueryVo;
import cn.xf.basedemo.common.model.pay.WxPayTransRsqVo;
import com.alibaba.fastjson2.JSON;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.QueryByOutRefundNoRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.transferbatch.TransferBatchService;
import com.wechat.pay.java.service.transferbatch.model.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WxPayUtil {

    private static String payPath;
    private static String appId;
    private static String wxMerchantId;
    private static String wxApiV3Key;
    private static String wxApiSerialNo;
    private static String wxMerchantApiCertificate;
    private static String wxMerchantApiPrivateKey;
    private static String plantSerialNo;
    private static String notifyUrl; //通知URL
    private static String refundUrl; //退款充值URL

    public WxPayUtil(String path, int type) {
        payPath = path;
        type = type;
        String path1 = "d:/pay";
        path1 = "";
        refundUrl = "" + payPath;
        switch (payPath) {
            case "a"://提现打款用-文撩
                appId = "";
                wxMerchantId = "";
                wxApiV3Key = "";
                wxApiSerialNo = "";
                wxMerchantApiCertificate = path1 + "9DE56.pem";
                wxMerchantApiPrivateKey = path1 + "apiclient_key.pem";
                plantSerialNo = "1A1D8C3A474A29DE56";
                break;
        }
    }


    @SneakyThrows
    public WxPayTransRsqVo transfer(String trans_no, String account, BigDecimal money, String trueName, String mark) {
        WxPayTransRsqVo rsqVo = new WxPayTransRsqVo();
        rsqVo.setSuccess(false);
        log.info("开始退款,{}-{}-{}-{}-{}", wxMerchantId, wxMerchantApiPrivateKey, wxApiSerialNo, wxApiV3Key, money);
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxMerchantId)
                .privateKeyFromPath(wxMerchantApiPrivateKey)
                .merchantSerialNumber(wxApiSerialNo)
                .apiV3Key(wxApiV3Key)
                .build();

        TransferBatchService service = new TransferBatchService.Builder().config(config).build();
        //数据封装
        InitiateBatchTransferRequest initiateBatchTransferRequest = new InitiateBatchTransferRequest();
        initiateBatchTransferRequest.setAppid(appId);
        initiateBatchTransferRequest.setOutBatchNo(trans_no);
        initiateBatchTransferRequest.setBatchName(mark);
        initiateBatchTransferRequest.setBatchRemark(mark);
        initiateBatchTransferRequest.setTotalAmount(money.multiply(BigDecimal.valueOf(100)).longValue());
        initiateBatchTransferRequest.setTotalNum(1);

        //initiateBatchTransferRequest.setTransferSceneId("1001");
        {
            List<TransferDetailInput> transferDetailListList = new ArrayList<>();
            {
                TransferDetailInput transferDetailInput = new TransferDetailInput();
                transferDetailInput.setTransferAmount(money.multiply(BigDecimal.valueOf(100)).longValue());//金额为分 需要乘以100
                transferDetailInput.setOutDetailNo(trans_no);
                transferDetailInput.setOpenid(account);
                transferDetailInput.setUserName(trueName);
                transferDetailInput.setTransferRemark(mark);
                transferDetailListList.add(transferDetailInput);
            }
            initiateBatchTransferRequest.setTransferDetailList(
                    transferDetailListList);
        }
        //发起商家转账
        InitiateBatchTransferResponse response;
        try {
            response = service.initiateBatchTransfer(initiateBatchTransferRequest);
            log.info("转账:", response.toString());
//            if (response.getBatchStatus().equals("ACCEPTED")) {
//                log.info("initiateBatchTransfer:", response.getBatchStatus());
//            }
//            log.error("initiateBatchTransfer:", response.getBatchStatus());
            rsqVo.setSuccess(true);

        } catch (ServiceException e) {
            log.info("出错了:", e.getErrorMessage());
//            e.printStackTrace();


            rsqVo.setCode(e.getErrorCode());
            rsqVo.setMsg(e.getErrorMessage());
        }


        return rsqVo;
    }

    @SneakyThrows
    public TransferQueryVO transferQuery(String outBatchNo, String outDetailNo) {
        TransferQueryVO transferQueryVO = new TransferQueryVO();
        //商家转账批次单号
        //商家转账明细单号
        transferQueryVO.setSuccess(false);
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxMerchantId)
                .privateKeyFromPath(wxMerchantApiPrivateKey)
                .merchantSerialNumber(wxApiSerialNo)
                .apiV3Key(wxApiV3Key)
                .build();
        try {
            TransferBatchService transferBatchService = new TransferBatchService.Builder().config(config).build();
            GetTransferDetailByOutNoRequest request = new GetTransferDetailByOutNoRequest();
            request.setOutBatchNo(outBatchNo);
            request.setOutDetailNo(outDetailNo);
            TransferDetailEntity response = transferBatchService.getTransferDetailByOutNo(request);
            log.info("转账返回信息：{}", JSON.toJSONString(response));
            transferQueryVO.setCashQueryVo(JSON.parseObject(JSON.toJSONString(response), WechatCashQueryVo.class));
            transferQueryVO.setResCode(response.getDetailStatus());
            transferQueryVO.setResCodeDes(response.getDetailStatus());
            if ("FAIL".equals(response.getDetailStatus())) {
//                transferQueryVO.setResCodeDes(getErrorMsg(String.valueOf(response.getFailReason())));
                transferQueryVO.setSuccess(false);
            } else {
                transferQueryVO.setSuccess(true);
            }

            log.info("转账返回信息II：{}", JSON.toJSONString(transferQueryVO));
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            log.error(e.getMessage());
            transferQueryVO.setResCodeDes(e.getMessage());
            // throw new RuntimeException("微信转账失败");
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            log.error(e.getMessage());
            transferQueryVO.setResCodeDes(e.getMessage());
            //throw new RuntimeException("微信转账失败");
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            log.error(e.getMessage());
            transferQueryVO.setResCodeDes(e.getMessage());
            //throw new RuntimeException("微信转账失败");
        } catch (Exception e) {
            log.error(e.getMessage());
//            e.printStackTrace();
            transferQueryVO.setResCodeDes(e.getMessage());
            //throw new RuntimeException("微信转账失败");
        }

        return transferQueryVO;
    }


    @SneakyThrows
    public RefundInfoRsqDTO refundUser(String outTradeNo, String outRefundNo, long totalFee, BigDecimal refundFee) {
        RefundInfoRsqDTO refundInfoRsqDTO = new RefundInfoRsqDTO();
        refundInfoRsqDTO.setSuccess(false);
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxMerchantId)
                .privateKeyFromPath(wxMerchantApiPrivateKey)
                .merchantSerialNumber(wxApiSerialNo)
                .apiV3Key(wxApiV3Key)
                .build();

        RefundService service = new RefundService.Builder().config(config).build();
        CreateRequest request = new CreateRequest();
        request.setOutTradeNo(outTradeNo);
        request.setOutRefundNo(outRefundNo);
        request.setNotifyUrl(refundUrl);
        AmountReq amount = new AmountReq();
        amount.setRefund(refundFee.longValue());
        amount.setTotal(totalFee);
        amount.setCurrency("CNY");
        request.setAmount(amount);
        try {
            Refund refund = service.create(request);
            log.info("refund:{}", refund.toString());
            if (refund.getStatus().toString().equals("PROCESSING")) {
                refundInfoRsqDTO.setSuccess(true);
            }
            refundInfoRsqDTO.setRefund(refund);
        } catch (ServiceException e) {
            log.error("退款失败:{}------{}-----{}", e.getErrorMessage(), e.getErrorCode(), e.getResponseBody());
            e.printStackTrace();

            refundInfoRsqDTO.setMsg(e.getErrorMessage());
        }

        return refundInfoRsqDTO;
    }

    public static RefundInfoRsqDTO queryRefund(String outRefundNo) {
        RefundInfoRsqDTO refundInfoRsqDTO = new RefundInfoRsqDTO();
        refundInfoRsqDTO.setSuccess(false);
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxMerchantId)
                .privateKeyFromPath(wxMerchantApiPrivateKey)
                .merchantSerialNumber(wxApiSerialNo)
                .apiV3Key(wxApiV3Key)
                .build();

        RefundService service = new RefundService.Builder().config(config).build();
        QueryByOutRefundNoRequest request = new QueryByOutRefundNoRequest();
        request.setOutRefundNo(outRefundNo);
        try {
            Refund refund = service.queryByOutRefundNo(request);
            log.info("refund:{}", refund.toString());
            if (refund.getStatus().toString().equals("SUCCESS")) {
                refundInfoRsqDTO.setSuccess(true);
            }
            refundInfoRsqDTO.setRefund(refund);
        } catch (ServiceException e) {
            log.error("查询失败:{}------{}-----{}", e.getErrorMessage(), e.getErrorCode(), e.getResponseBody());
            e.printStackTrace();

            refundInfoRsqDTO.setMsg(e.getErrorMessage());
        }

        return refundInfoRsqDTO;
    }

//    private String getErrorMsg(String errCode) {
//        return switch (errCode) {
//            case "ACCOUNT_FROZEN" -> "账户冻结";
//            case "REAL_NAME_CHECK_FAIL" -> "用户未实名";
//            case "NAME_NOT_CORRECT" -> "用户姓名校验失败";
//            case "OPENID_INVALID" -> "Openid校验失败";
//            case "TRANSFER_QUOTA_EXCEED" -> "超过用户单笔收款额度";
//            case "DAY_RECEIVED_QUOTA_EXCEED" -> "超过用户单日收款额度";
//            case "MONTH_RECEIVED_QUOTA_EXCEED" -> "超过用户单月收款额度";
//            case "DAY_RECEIVED_COUNT_EXCEED" -> "超过用户单日收款次数";
//            case "PRODUCT_AUTH_CHECK_FAIL" -> "产品权限校验失败";
//            case "OVERDUE_CLOSE" -> "转账关闭";
//            case "ID_CARD_NOT_CORRECT" -> "用户身份证校验失败";
//            case "ACCOUNT_NOT_EXIST" -> "用户账户不存在";
//            case "TRANSFER_RISK" -> "转账存在风险";
//            case "REALNAME_ACCOUNT_RECEIVED_QUOTA_EXCEED" -> "用户账户收款受限，请引导用户在微信支付查看详情";
//            case "RECEIVE_ACCOUNT_NOT_PERMMIT" -> "未配置该用户为转账收款人";
//            case "PAYER_ACCOUNT_ABNORMAL" -> "商户账户付款受限，可前往商户平台-违约记录获取解除功能限制指引";
//            case "PAYEE_ACCOUNT_ABNORMAL" -> "用户账户收款异常，请引导用户完善其在微信支付的身份信息以继续收款";
//            case "TRANSFER_REMARK_SET_FAIL" -> "转账备注设置失败，请调整对应文案后重新再试";
//            default -> "内部错误，请联系管理人员";
//        };
//    }

}
