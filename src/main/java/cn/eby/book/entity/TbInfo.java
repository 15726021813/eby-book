package cn.eby.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
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
@TableName(value = "tb_info")
public class TbInfo {
    @TableField(value = "bk_id")
    @TableId
    private Integer bkId;

    @TableField(value = "bk_isbn")
    private String bkIsbn;

    @TableField(value = "bk_ean13")
    private String bkEan13;

    @TableField(value = "bk_name")
    private String bkName;

    @TableField(value = "bk_subname")
    private String bkSubname;

    @TableField(value = "bk_keyname")
    private String bkKeyname;

    @TableField(value = "bk_auth")
    private String bkAuth;

    @TableField(value = "bk_class")
    private String bkClass;

    @TableField(value = "bk_kind")
    private String bkKind;

    @TableField(value = "prs_nme")
    private String prsNme;

    @TableField(value = "pub_plc")
    private String pubPlc;

    @TableField(value = "pub_tme")
    private String pubTme;

    @TableField(value = "page_ct")
    private String pageCt;

    @TableField(value = "bk_pric")
    private String bkPric;

    @TableField(value = "bk_edit")
    private String bkEdit;

    @TableField(value = "bk_vol")
    private String bkVol;

    @TableField(value = "bk_size")
    private String bkSize;

    @TableField(value = "bk_share")
    private String bkShare;

    @TableField(value = "bk_print")
    private String bkPrint;

    @TableField(value = "bk_lang")
    private String bkLang;

    @TableField(value = "bk_seri")
    private String bkSeri;

    @TableField(value = "bk_oth")
    private String bkOth;

    @TableField(value = "bk_py")
    private String bkPy;

    @TableField(value = "ipt_tme")
    private Date iptTme;

    @TableField(value = "bk_state")
    private Integer bkState;

    @TableField(value = "bk_type")
    private String bkType;

    @TableField(value = "bk_fz")
    private String bkFz;

    @TableField(value = "bk_905")
    private String bk905;

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "bk_pc")
    private String bkPc;

    @TableField(value = "updatetimes")
    private Integer updatetimes;

    @TableField(value = "bk_sysid")
    private Integer bkSysid;

    @TableField(value = "getdatatimes")
    private Integer getdatatimes;

    @TableField(value = "lb_suit")
    private Boolean lbSuit;

    @TableField(value = "types")
    private Integer types;

    @TableField(value = "Stypes")
    private Integer stypes;

    @TableField(value = "uptimes")
    private Date uptimes;

    @TableField(value = "upcount")
    private Integer upcount;

    public static final String COL_BK_ID = "bk_id";

    public static final String COL_BK_ISBN = "bk_isbn";

    public static final String COL_BK_EAN13 = "bk_ean13";

    public static final String COL_BK_NAME = "bk_name";

    public static final String COL_BK_SUBNAME = "bk_subname";

    public static final String COL_BK_KEYNAME = "bk_keyname";

    public static final String COL_BK_AUTH = "bk_auth";

    public static final String COL_BK_CLASS = "bk_class";

    public static final String COL_BK_KIND = "bk_kind";

    public static final String COL_PRS_NME = "prs_nme";

    public static final String COL_PUB_PLC = "pub_plc";

    public static final String COL_PUB_TME = "pub_tme";

    public static final String COL_PAGE_CT = "page_ct";

    public static final String COL_BK_PRIC = "bk_pric";

    public static final String COL_BK_EDIT = "bk_edit";

    public static final String COL_BK_VOL = "bk_vol";

    public static final String COL_BK_SIZE = "bk_size";

    public static final String COL_BK_SHARE = "bk_share";

    public static final String COL_BK_PRINT = "bk_print";

    public static final String COL_BK_LANG = "bk_lang";

    public static final String COL_BK_SERI = "bk_seri";

    public static final String COL_BK_OTH = "bk_oth";

    public static final String COL_BK_PY = "bk_py";

    public static final String COL_IPT_TME = "ipt_tme";

    public static final String COL_BK_STATE = "bk_state";

    public static final String COL_BK_TYPE = "bk_type";

    public static final String COL_BK_FZ = "bk_fz";

    public static final String COL_BK_905 = "bk_905";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_BK_PC = "bk_pc";

    public static final String COL_UPDATETIMES = "updatetimes";

    public static final String COL_BK_SYSID = "bk_sysid";

    public static final String COL_GETDATATIMES = "getdatatimes";

    public static final String COL_LB_SUIT = "lb_suit";

    public static final String COL_TYPES = "types";

    public static final String COL_STYPES = "Stypes";

    public static final String COL_UPTIMES = "uptimes";

    public static final String COL_UPCOUNT = "upcount";
}