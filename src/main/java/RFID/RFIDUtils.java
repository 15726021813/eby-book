package RFID;


import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//rfid工具类
public class RFIDUtils {
    private static String COMName = "COM5";
    private static String BaudRate = "38400";
    private static String sLibPath ="C:";
    private static Long hrOut = 1L; //设备句柄
    private static Long tabConn = 2L; //标签句柄

    private static final String enableESA = "e2";
    private static final String closeESA = "00";
    //所有操作前都要进行初始化 22192632
    //
    public static void main(String[] args) {
        try{


            Set<String> s = getTabs(0);

            s.forEach(e->{
                System.out.println(e);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将设备上的标签读到   读标签 获取标签的数据  blkadd 要读取的块
     * @param blkadd
     * @return
     * @throws Exception
     */
    public static Set<String> getTabs(Integer blkadd) throws Exception
    {
        Set<String> lists = new HashSet<>();
        init();
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
            byte[] uid = new byte[8];
            rfidlib_AIP_ISO15693.ISO15693_ParseTagDataReport(tabdata,aip_id,tag_id,ant_id,dsfid,uid);
            String s = Utils.bytesToHexString(uid);

            setTabConn(hrOut,uid);
            lists.add(getTabData(hrOut,blkadd));

            tabdata =  rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_NEXT);
        }

        //关闭端口
//        rfidlib_reader.RDR_DisconnectAllTags(hrOut);
//        rfidlib_reader.RDR_Close(hrOut);
        return lists;
    }
    //通过标签的序列号获取数据
    public static List<String> getTabsByTabUid(Integer blkadd,List<String> uids) throws Exception
    {
        List<String> lists = new ArrayList<>();
        init();
        int tabData = rfidlib_reader.RDR_GetTagDataReportCount(hrOut);
        if(tabData < 1){
            //表示没有标签
            return lists;
        }
        //tabdata是一个数据节点
        long tabdata = rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_FIRST);
        //12217392  11041272
        while (tabdata != 0){
            Integer aip_id = new Integer(1);
            Integer tag_id = new Integer(1);
            Integer ant_id = new Integer(0);
            Byte dsfid = new Byte("0");
            byte[] uid = new byte[8];
            rfidlib_AIP_ISO15693.ISO15693_ParseTagDataReport(tabdata,aip_id,tag_id,ant_id,dsfid,uid);
            String s = Utils.bytesToHexString(uid);
            if(uids.contains(s)){
                setTabConn(hrOut,uid);
                lists.add(getTabData(hrOut,blkadd));
            }


            tabdata =  rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_NEXT);
        }

        //关闭端口
//        rfidlib_reader.RDR_DisconnectAllTags(hrOut);
//        rfidlib_reader.RDR_Close(hrOut);
//        rfidlib_reader.DNODE_Destroy(hrOut);
//
//        rfidlib_reader.RDR_DisconnectAllTags(hrOut);
//        rfidlib_reader.RDR_Close(hrOut);
        return lists;
    }
    //写标签

    /**
     * 开启标签安全 设置成功的标签记录在list
     * @throws Exception
     */
    public static List<String> enableESAs() throws Exception{
        List<String> lists = new ArrayList<>();
        init();
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
            byte[] uid = new byte[8];
            rfidlib_AIP_ISO15693.ISO15693_ParseTagDataReport(tabdata,aip_id,tag_id,ant_id,dsfid,uid);

            setTabConn(hrOut,uid);
            int i = enableESA(hrOut,tabConn);
            if(i == 0){
                //代表此标签开启成功

                lists.add(getTabData(hrOut,0));
            }

            tabdata =  rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_NEXT);
        }

        //关闭端口
//        rfidlib_reader.RDR_DisconnectAllTags(hrOut);
        return lists;
    }
    /**
     * 关闭标签安全 设置成功的标签记录在list
     * 返回的是标签的uid
     * @throws Exception
     */
    public static List<String> closeESAs() throws Exception{
        List<String> lists = new ArrayList<>();

        init();
        int tabData = rfidlib_reader.RDR_GetTagDataReportCount(hrOut);
        if(tabData < 1){
            //表示没有标签
            return lists;
        }
//
        long tabdata = rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_FIRST);
//        //12217392  11041272
        while (tabdata != 0){
            Integer aip_id = new Integer(1);
            Integer tag_id = new Integer(1);
            Integer ant_id = new Integer(0);
            Byte dsfid = new Byte("0");
            byte[] uid = new byte[8];
            rfidlib_AIP_ISO15693.ISO15693_ParseTagDataReport(tabdata,aip_id,tag_id,ant_id,dsfid,uid);
//
            setTabConn(hrOut,uid);
//
            int i =  closeESA(hrOut,tabConn);
            if(i == 0){
                //代表此标签开启成功
                System.out.println("-------------");

                lists.add(Utils.bytesToHexString(uid));
            }
//
            tabdata =  rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_NEXT);
        }

        //关闭端口
//        rfidlib_reader.DNODE_Destroy(hrOut);
//
//        rfidlib_reader.RDR_DisconnectAllTags(hrOut);
//        rfidlib_reader.RDR_Close(hrOut);
        return lists;
    }

    /**
     * 开启标签安全 设置成功的标签记录在list
     * @throws Exception
     */
    public static List<String> enableESAByBookId(String bookId) throws Exception{
        List<String> lists = new ArrayList<>();
        init();
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
            byte[] uid = new byte[8];
            rfidlib_AIP_ISO15693.ISO15693_ParseTagDataReport(tabdata,aip_id,tag_id,ant_id,dsfid,uid);

            setTabConn(hrOut,uid);
            if(getTabData(hrOut,0).equals(bookId)){
                int i = enableESA(hrOut,tabConn);
                if(i == 0){
                    //代表此标签开启成功

                    lists.add(getTabData(hrOut,0));
                }
                break;
            }


            tabdata =  rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_NEXT);
        }

        //关闭端口
//        rfidlib_reader.RDR_DisconnectAllTags(hrOut);
        return lists;
    }
    /**
     * 关闭标签安全 设置成功的标签记录在list
     * 返回的是标签的uid
     * @throws Exception
     */
    public static List<String> closeESAByBookId(String bookId) throws Exception{
        List<String> lists = new ArrayList<>();

        init();
        int tabData = rfidlib_reader.RDR_GetTagDataReportCount(hrOut);
        if(tabData < 1){
            //表示没有标签
            return lists;
        }
//
        long tabdata = rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_FIRST);
//        //12217392  11041272
        while (tabdata != 0){
            Integer aip_id = new Integer(1);
            Integer tag_id = new Integer(1);
            Integer ant_id = new Integer(0);
            Byte dsfid = new Byte("0");
            byte[] uid = new byte[8];
            rfidlib_AIP_ISO15693.ISO15693_ParseTagDataReport(tabdata,aip_id,tag_id,ant_id,dsfid,uid);
//
            setTabConn(hrOut,uid);
            if(getTabData(hrOut,0).equals(bookId)){
                int i = closeESA(hrOut,tabConn);
                if(i == 0){
                    //代表此标签开启成功

                    lists.add(getTabData(hrOut,0));
                }
                break;
            }
//
            tabdata =  rfidlib_reader.RDR_GetTagDataReport(hrOut,rfid_def.RFID_SEEK_NEXT);
        }

        //关闭端口
//        rfidlib_reader.DNODE_Destroy(hrOut);
//
//        rfidlib_reader.RDR_DisconnectAllTags(hrOut);
//        rfidlib_reader.RDR_Close(hrOut);
        return lists;
    }
    //初始化项目  获取设备句柄 与 寻找标签
    public static void init(){
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
    }
    //开启esa，没有外借
    public static int enableESA(Long hr,Long ht){
        hr = new Long(hr);
        ht = new Long(ht);
        int res = 0;
        int i = rfidlib_AIP_ISO15693.ISO15693_WriteDSFID(hr,ht,Utils.hexToByte(enableESA));
        if(i != 0){
            if(res == 0){
                res = i;
            }
        }
        i = rfidlib_AIP_ISO15693.ISO15693_WriteAFI(hr,ht,Utils.hexToByte(enableESA));

        if(i != 0){
            if(res == 0){
                res = i;
            }
        }
        i = rfidlib_AIP_ISO15693.NXPICODESLI_EableEAS(hr,ht);
        return res;
    }
    //关闭esa, 已外借
    public static int closeESA(Long hr,Long ht){
        hr = new Long(hr);
        ht = new Long(ht);
        int res = 0;
        int i = rfidlib_AIP_ISO15693.ISO15693_WriteDSFID(hr,ht,Utils.hexToByte(closeESA));

        if(i != 0){
            if(res == 0){
                res = i;
            }
        }
        i = rfidlib_AIP_ISO15693.ISO15693_WriteAFI(hr,ht,Utils.hexToByte(closeESA));

        if(i != 0){
            if(res == 0){
                res = i;
            }
        }
        i = rfidlib_AIP_ISO15693.NXPICODESLI_DisableEAS(hr,ht);
        return res;
    }



    //获取标签的数据

    public static void setTabConn(Long hrOut,byte[] uid) throws Exception{

        Long hr = new Long(hrOut);
        //获取标签的名柄
        Long ht = new Long(1L);
        //必须new,不new会覆盖原来的值
        int i = rfidlib_AIP_ISO15693.ISO15693_Connect(new Long(hr),1,Utils.hexToByte("1"),uid,ht);
        if(i != 0){
            new RuntimeException("获取标签句柄失败");
        }
        tabConn = ht;
    }
    /**
     *
     * @param hr  设备句柄
     * @param blkAddr 读取块地址
     *
     */
    public static String getTabData(Long hr,int blkAddr) throws Exception{
        int i = 0;
        byte[] buffer = new byte[32];
        Integer size = 1;//17851360 17855416
        i = rfidlib_AIP_ISO15693.ISO15693_ReadMultiBlocks(hr,tabConn,Utils.hexToByte("1"),0,3,12,buffer,buffer.length,1200);

        if(i != 0){
            new RuntimeException("获取标签数据失败");
        }


        String res = Utils.bytesToHexString(buffer);

        StringBuffer re = new StringBuffer("");
        re.append(res.substring(8,10));
        re.append(res.substring(12,18));
        re.append(res.substring(20,27));


        return re.toString();
    }

    /**
     *
     * @param hr  设备句柄
     * @param blkAddr 块地址
     * @param data 写入的数据
     */
    public static void writeTabData(Long hr,int blkAddr,byte[] data) throws Exception{
        int i;
        i = rfidlib_AIP_ISO15693.ISO15693_WriteSingleBlock(hr,tabConn,blkAddr,data,data.length);
        if(i != 0){
            new RuntimeException("标签数据写入失败");
        }

        //断开于标签的连接
//        rfidlib_reader.RDR_SetCommuImmeTimeout(hr);
    }

}
