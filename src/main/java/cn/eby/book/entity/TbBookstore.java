package cn.eby.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Auther: 徐长乐
 * @Date: 2020/09/24/11:55
 * @Description: 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_bookstore")
@Builder
public class TbBookstore {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "barid")
    private Integer barid;

    @TableField(value = "bk_id")
    private Integer bkId;

    @TableField(value = "place")
    private String place;

    @TableField(value = "store")
    private String store;

    @TableField(value = "[status]")
    private String status;

    @TableField(value = "[state]")
    private Integer state;

    @TableField(value = "zj_mny")
    private BigDecimal zjMny;

    @TableField(value = "ipt_tme")
    private Date iptTme;

    @TableField(value = "msg")
    private String msg;

    @TableField(value = "oth")
    private String oth;

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "del_time")
    private Date delTime;

    @TableField(value = "del_rsn")
    private Integer delRsn;

    @TableField(value = "Stypes")
    private Integer stypes;

    @TableField(value = "uptimes")
    private Date uptimes;

    @TableField(value = "upcount")
    private Integer upcount;

    public static final String COL_ID = "id";

    public static final String COL_BARID = "barid";

    public static final String COL_BK_ID = "bk_id";

    public static final String COL_PLACE = "place";

    public static final String COL_STORE = "store";

    public static final String COL_STATUS = "status";

    public static final String COL_STATE = "state";

    public static final String COL_ZJ_MNY = "zj_mny";

    public static final String COL_IPT_TME = "ipt_tme";

    public static final String COL_MSG = "msg";

    public static final String COL_OTH = "oth";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_DEL_TIME = "del_time";

    public static final String COL_DEL_RSN = "del_rsn";

    public static final String COL_STYPES = "Stypes";

    public static final String COL_UPTIMES = "uptimes";

    public static final String COL_UPCOUNT = "upcount";
}