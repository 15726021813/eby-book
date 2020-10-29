package cn.eby.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * @Auther: 徐长乐
 * @Date: 2020/09/24/11:55
 * @Description: 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_bookborrow")
@Builder
public class TbBookborrow {
//    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "zl_id")
    private String zlId;

    @TableField(value = "rd_id")
    private String rdId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "bor_date")
    private Date borDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "rtn_date")
    private Date rtnDate;

    @TableField(value = "cont_cnt")
    private Integer contCnt;

    @TableField(value = "username")
    private String username;

    @TableField(value = "remarks")
    private String remarks;

    @TableField(value = "Stypes")
    private Integer stypes;

    @TableField(value = "uptimes")
    private Date uptimes;

    @TableField(value = "upcount")
    private Integer upcount;


    @TableField(exist = false)
    private String bkName;
    @TableField(exist = false)
    private String bkAuth;

    public static final String COL_ID = "id";

    public static final String COL_ZL_ID = "zl_id";

    public static final String COL_RD_ID = "rd_id";

    public static final String COL_BOR_DATE = "bor_date";

    public static final String COL_RTN_DATE = "rtn_date";

    public static final String COL_CONT_CNT = "cont_cnt";

    public static final String COL_USERNAME = "username";

    public static final String COL_REMARKS = "remarks";

    public static final String COL_STYPES = "Stypes";

    public static final String COL_UPTIMES = "uptimes";

    public static final String COL_UPCOUNT = "upcount";
}