package RFID;

import java.util.ArrayList;
import java.util.List;

//rfid工具类
public class RFIDUtilsB {
    private static String COMName = "COM5";
    private static String BaudRate = "38400";
    private static String sLibPath ="C:";

    public static void main(String[] args) {
        List<String> s = new RFIDUtilsB().getTabDatas();
        System.out.println("ss");
    }
    private static Long hrOut = 1L;
    //获取检测到的标签
    public static List<String> getTabDatas(){
        List<String> lists = new ArrayList<>();

        rfidlib_reader.LoadLib(sLibPath,rfid_def.VER_WINDOWS,rfid_def.AR_X86);
        //打开连接
        String connStr = "RDType=RD201;CommType=COM;COMName="+COMName+";BaudRate="+BaudRate+";Frame=8E1;BusAddr=255";
        int res = 0;
        if(hrOut == 1L){
            res = rfidlib_reader.RDR_Open(connStr,hrOut);
            System.out.println("设备句柄："+res+":"+hrOut);
            if(res != 0){
                throw new RuntimeException("设备句柄获取失败，设备未找到");
            }
        }

        Long datares = rfidlib_reader.RDR_CreateInvenParamSpecList();
        if(res != 0){
            throw new RuntimeException("空中接口协议参数列表数据节点创建失败");
        }
        //i1 = 0 成功，非0不成功
        //以 iso15693扫描，也就是以下通过iso15693协议的都能扫描到
        rfidlib_AIP_ISO15693.LoadLib(sLibPath,rfid_def.VER_WINDOWS,rfid_def.AR_X86);
        rfidlib_AIP_ISO15693.ISO15693_CreateInvenParam(datares,Utils.hexToByte("0"),Utils.hexToByte("0"),Utils.hexToByte("0"),Utils.hexToByte("0"));

        int i1 = rfidlib_reader.RDR_TagInventory(hrOut, Utils.hexToByte("1"),Utils.hexToByte("0"),null,datares);
        if(res != 0){
            throw new RuntimeException("寻找标签失败");
        }
        int tabData = rfidlib_reader.RDR_GetTagDataReportCount(hrOut);
       if(tabData < 1){
           //表示没有标签
           return lists;
       }

        long tabdata = rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_FIRST);
        //12217392  11041272
        while (tabdata != 0){
            Integer aip_id = new Integer(1);
            Integer tag_id = new Integer(1);
            Integer ant_id = new Integer(0);
            Byte dsfid = new Byte("0");
            byte uid[] = new byte[8];
            rfidlib_AIP_ISO15693.ISO15693_ParseTagDataReport(tabdata,aip_id,tag_id,ant_id,dsfid,uid);
            String s = Utils.bytesToHexString(uid);
            lists.add(Utils.bytesToHexString(uid));

            tabdata =  rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_NEXT);
        }

        //关闭端口
        return lists;
    }
}
