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
 * @Date: 2020/09/12/15:43
 * @Description: 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_reader")
public class TbReader {
//    @TableId(value = "rd_id")
    @TableField(value = "rd_id")
    private Integer rdId;

    @TableField(value = "id")
    private Integer id;

    @TableField(value = "rd_name")
    private String rdName;

    @TableField(value = "rd_sex")
    private Short rdSex;

    @TableField(value = "rd_birth")
    private String rdBirth;

    @TableField(value = "rd_reg")
    private String rdReg;

    @TableField(value = "rd_over")
    private String rdOver;

    @TableField(value = "rd_state")
    private Integer rdState;

    @TableField(value = "rd_roleid")
    private Integer rdRoleid;

    @TableField(value = "rd_mny")
    private BigDecimal rdMny;

    @TableField(value = "rd_pwd")
    private String rdPwd;

    @TableField(value = "rd_PhotosPath")
    private String rdPhotospath;

    @TableField(value = "rd_py")
    private String rdPy;

    @TableField(value = "rd_corp")
    private String rdCorp;

    @TableField(value = "rd_dpt")
    private String rdDpt;

    @TableField(value = "rd_grad")
    private String rdGrad;

    @TableField(value = "rd_occ")
    private String rdOcc;

    @TableField(value = "rd_adr")
    private String rdAdr;

    @TableField(value = "rd_pst")
    private String rdPst;

    @TableField(value = "rd_tele")
    private String rdTele;

    @TableField(value = "rd_idk")
    private String rdIdk;

    @TableField(value = "rd_Eml")
    private String rdEml;

    @TableField(value = "rd_oth")
    private String rdOth;

    @TableField(value = "rd_pc")
    private String rdPc;

    @TableField(value = "Stypes")
    private Integer stypes;

    @TableField(value = "uptimes")
    private Date uptimes;

    @TableField(value = "upcount")
    private Integer upcount;

    /**
     * 支付宝userId
     */
    @TableField(value = "out_trade_no")
    private Long outTradeNo;
    /**
     * 身份证号
     */
    @TableField(value = "id_card")
    private String idCard;

    public static final String COL_RD_ID = "rd_id";

    public static final String COL_ID = "id";

    public static final String COL_RD_NAME = "rd_name";

    public static final String COL_RD_SEX = "rd_sex";

    public static final String COL_RD_BIRTH = "rd_birth";

    public static final String COL_RD_REG = "rd_reg";

    public static final String COL_RD_OVER = "rd_over";

    public static final String COL_RD_STATE = "rd_state";

    public static final String COL_RD_ROLEID = "rd_roleid";

    public static final String COL_RD_MNY = "rd_mny";

    public static final String COL_RD_PWD = "rd_pwd";

    public static final String COL_RD_PHOTOSPATH = "rd_PhotosPath";

    public static final String COL_RD_PY = "rd_py";

    public static final String COL_RD_CORP = "rd_corp";

    public static final String COL_RD_DPT = "rd_dpt";

    public static final String COL_RD_GRAD = "rd_grad";

    public static final String COL_RD_OCC = "rd_occ";

    public static final String COL_RD_ADR = "rd_adr";

    public static final String COL_RD_PST = "rd_pst";

    public static final String COL_RD_TELE = "rd_tele";

    public static final String COL_RD_IDK = "rd_idk";

    public static final String COL_RD_EML = "rd_Eml";

    public static final String COL_RD_OTH = "rd_oth";

    public static final String COL_RD_PC = "rd_pc";

    public static final String COL_STYPES = "Stypes";

    public static final String COL_UPTIMES = "uptimes";

    public static final String COL_UPCOUNT = "upcount";

    public static final String COL_ALIPAY_USER_ID = "alipay_user_id";

    public static final String COL_ID_CARD = "id_card";
}