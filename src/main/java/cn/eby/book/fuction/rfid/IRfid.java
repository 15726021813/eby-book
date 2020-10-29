package cn.eby.book.fuction.rfid;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类只进行卡的读写操作，不做其他任何处理
 * 全类 0 代表成功 非0代表失败
 */
abstract class IRfid {
    //获取设备句柄,单例模式
    Long hrout = 0L;
    abstract public Long getHrout();
    //获取标签序列号
    abstract public List<byte[]> getUids();
    //获取单个标签数据 begin开始读的块 size结束的块
    abstract public String getTabData(byte[] uid,int begin,int size);
    //获取多个标签数据
    public List<String> getTabDatas(int begin,int size){
        List<String> res = new ArrayList<>();
        List<byte[]> uids = getUids();
        for (byte[] uid : uids) {
            res.add(getTabData(uid,begin,size));
        }
        return res;
    }

}
