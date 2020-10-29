package cn.eby.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Auther: 徐长乐
 * @Date: 2020/09/22/14:42
 * @Description: 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "pay_order")
public class PayOrder {
    /**
     * 支付订单号
     */
    @TableId(value = "out_trade_no", type = IdType.INPUT)
    private Long outTradeNo;

    /**
     * 支付金额  分
     */
    @TableField(value = "pay_amount")
    private   BigDecimal payAmount;

    @TableField(value = "auth_no")
    private Long authNo;

    /**
     * 1 微信  0 支付宝
     */
    @TableField(value = "pay_type")
    private Byte payType;

    /**
     * 线上退押金需 一年之内
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "pay_status")
    private Byte payStatus;

    public static final String COL_OUT_TRADE_NO = "out_trade_no";

    public static final String COL_PAY_AMOUNT = "pay_amount";

    public static final String COL_AUTH_NO = "auth_no";

    public static final String COL_PAY_TYPE = "pay_type";

    public static final String COL_CREATE_TIME = "create_time";
}