package cn.eby.book.fuction.rfid;

import RFID.Utils;
import RFID.rfid_def;
import RFID.rfidlib_AIP_ISO15693;
import RFID.rfidlib_reader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class IRfidImpl extends IRfid {
    @Value("${RFID.COMName}")
    private String COMName;
    @Value("${RFID.BaudRate}")
    private String BaudRate;
    @Value("${RFID.sLibPath}")
    private String sLibPath;
    //对寻找设备句柄，创建空中协议，查找标签三种状态进行记录
    private int code = 0; //如果失败这里会有对应的姿态码 0 成功 非0失败
    private String msg = "操作成功"; //这里是失败的描述
    public void setCodeMsg(int code,String msg){
        if(this.code == 0){
            this.code = code;
            this.msg = msg;
        }
    }
    @Override
    public Long getHrout() {
        if(0L == hrout){
            synchronized (IRfidImpl.class){
                if(0L == hrout){
                    rfidlib_reader.LoadLib(sLibPath, rfid_def.VER_WINDOWS,rfid_def.AR_X86);
                    String connStr = "RDType=RD201;CommType=COM;COMName="+COMName+";BaudRate="+BaudRate+";Frame=8E1;BusAddr=255";
                    int res = rfidlib_reader.RDR_Open(connStr,hrout);
                    if(res != 0){
                        setCodeMsg(res,"获取设备句柄失败");
                    }
                }
            }
        }
        return hrout.longValue();
    }

    @Override
    public List<byte[]> getUids() {
        List<byte[]> lists = new ArrayList<>();
        if(createInvenParamSpecList() != 0){
            //表示没有标签
            return lists;
        }

        long tabdata = rfidlib_reader.RDR_GetTagDataReport(getHrout(),rfid_def.RFID_SEEK_FIRST);
        //12217392  11041272
        while (tabdata != 0){
            Integer aip_id = new Integer(1);
            Integer tag_id = new Integer(1);
            Integer ant_id = new Integer(0);
            Byte dsfid = new Byte("0");
            byte[] uid = new byte[8];
            rfidlib_AIP_ISO15693.ISO15693_ParseTagDataReport(tabdata,aip_id,tag_id,ant_id,dsfid,uid);
            String s = Utils.bytesToHexString(uid);

            lists.add(uid);

            tabdata =  rfidlib_reader.RDR_GetTagDataReport(getHrout(),rfid_def.RFID_SEEK_NEXT);
        }
        return lists;
    }

    @Override
    public String getTabData(byte[] uid, int begin, int size) {
        StringBuffer res = new StringBuffer();
        byte[] buffer = new byte[size * 5];

        int j = rfidlib_AIP_ISO15693.ISO15693_ReadMultiBlocks(getHrout(),createTabConn(uid),Utils.hexToByte("1"),begin,size,12,buffer,buffer.length,1200);
//        if(j != 0){
//            setCodeMsg(j,"读数据失败:"+Utils.bytesToHexString(uid));
//
//        }
        String str = Utils.bytesToHexString(buffer);
        for (int i = 0; i < size; i++) {
            res.append(str.substring(i*8+(i+1)*2,i*8+(i+1)*2+8));
        }
        return res.toString();
    }
    //创建空中协议列表，并指定扫描标签的方法,执行完就能读到标签了
    public int createInvenParamSpecList(){
        int res = 0;
        getHrout();
        Long datares = rfidlib_reader.RDR_CreateInvenParamSpecList();
        if(datares == 0){
            res = 1;
            setCodeMsg(res,"创建空中协议列表失败");
        }
        //i1 = 0 成功，非0不成功
        //以 iso15693扫描，也就是以下通过iso15693协议的都能扫描到
        rfidlib_AIP_ISO15693.LoadLib(sLibPath,rfid_def.VER_WINDOWS,rfid_def.AR_X86);
        rfidlib_AIP_ISO15693.ISO15693_CreateInvenParam(datares, Utils.hexToByte("0"),Utils.hexToByte("0"),Utils.hexToByte("0"),Utils.hexToByte("0"));

        int i1 = rfidlib_reader.RDR_TagInventory(getHrout(), Utils.hexToByte("1"),Utils.hexToByte("0"),null,datares);
        if(i1 != 0){
            res = 1;
            setCodeMsg(res,"寻找标签失败");
        }

        return res;
    }

    //和标签建立连接，建立完连接就可以读数据了,返回标签的句柄
    public Long createTabConn(byte[] uid){
        Long ht = new Long(0L);
        //必须new,不new会覆盖原来的值
        int i = rfidlib_AIP_ISO15693.ISO15693_Connect(getHrout(),1,Utils.hexToByte("1"),uid,ht);
//        if(i != 0){
//            setCodeMsg(i,"和标签建立连接失败");
//        }
        return ht;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
