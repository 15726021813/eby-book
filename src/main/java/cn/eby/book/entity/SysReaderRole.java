package cn.eby.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


/**
 * @Auther: 徐长乐
 * @Date: 2020/09/24/11:55
 * @Description: 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_ReaderRole")
public class SysReaderRole {
    @TableId(value = "RoleId", type = IdType.INPUT)
    private Integer roleid;
    @TableField(value = "RoleName")
    private String rolename;

    @TableField(value = "RoleInfo")
    private String roleinfo;

    @TableField(value = "Stypes")
    private Integer stypes;

    @TableField(value = "uptimes")
    private Date uptimes;

    @TableField(value = "upcount")
    private Integer upcount;

    public static final String COL_ROLEID = "RoleId";

    public static final String COL_ROLENAME = "RoleName";

    public static final String COL_ROLEINFO = "RoleInfo";

    public static final String COL_STYPES = "Stypes";

    public static final String COL_UPTIMES = "uptimes";

    public static final String COL_UPCOUNT = "upcount";
}