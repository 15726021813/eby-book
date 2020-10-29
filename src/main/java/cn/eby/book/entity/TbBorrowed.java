package cn.eby.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "tb_borrowed")
@Builder
public class TbBorrowed {
//    @TableId(value = "logid", type = IdType.INPUT)
//    private Long logid;

    @TableField(value = "logtype")
    private Short logtype;

    @TableField(value = "logtime")
    private Date logtime;

    @TableField(value = "rd_id")
    private Integer rdId;

    @TableField(value = "zl_id")
    private Integer zlId;

    @TableField(value = "zl_kind")
    private Integer zlKind;

    @TableField(value = "username")
    private String username;

    @TableField(value = "Stypes")
    private Integer stypes;

    @TableField(value = "uptimes")
    private Date uptimes;

    @TableField(value = "upcount")
    private Integer upcount;

    public static final String COL_LOGID = "logid";

    public static final String COL_LOGTYPE = "logtype";

    public static final String COL_LOGTIME = "logtime";

    public static final String COL_RD_ID = "rd_id";

    public static final String COL_ZL_ID = "zl_id";

    public static final String COL_ZL_KIND = "zl_kind";

    public static final String COL_USERNAME = "username";

    public static final String COL_STYPES = "Stypes";

    public static final String COL_UPTIMES = "uptimes";

    public static final String COL_UPCOUNT = "upcount";
}