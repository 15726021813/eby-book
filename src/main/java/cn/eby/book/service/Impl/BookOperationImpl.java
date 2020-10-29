package cn.eby.book.service.Impl;

import RFID.RFIDUtils;
import RFID.Utils;
import cn.eby.book.common.Result;
import cn.eby.book.config.StateConfig;
import cn.eby.book.entity.SysReaderRoleRow;
import cn.eby.book.entity.TbBookborrow;
import cn.eby.book.entity.TbBookstore;
import cn.eby.book.entity.TbInfo;
import cn.eby.book.log.BookBorrowLog;
import cn.eby.book.log.BookBorrowLogType;
import cn.eby.book.mapper.*;
import cn.eby.book.service.BookOperationServer;
import cn.eby.book.service.TbReaderService;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 朱胜伟
 * 图书借阅操作类
 */
@Service
public class BookOperationImpl implements BookOperationServer {
    @Autowired
    TbBookborrowMapper tbBookborrowMapper;   //借书表
    @Autowired
    SysReaderRoleRowMapper readerRoleRowMapper;  //读者权限表
    @Autowired
    TbBorrowedMapper tbBorrowedMapper;   //操作日记表
    @Autowired
    TbBookstoreMapper tbBookstoreMapper; //馆藏图书表
    @Autowired
    TbInfoMapper tbInfoMapper; //图书信息表

    @Autowired
    private TbReaderService tbReaderService;
    /**
     *
     * @param zi_id 书编号
     * @param rd_id 用户编号
     * @return  code  1 代表借书成功，0代表失败
     *          msg   描述
     *          zi_id  书编号
     */
    @Transactional
    @BookBorrowLog(logType = BookBorrowLogType.BORROWED)
    public Map<String,Object> bookBorrow(String zi_id,Integer rd_id) {
        Map<String,Object> res = new HashMap<>();
        res.put("code",1);
        res.put("msg",  StateConfig.map.get(9));
        TbInfo tbInfo = getBookByZi_id(zi_id);
        res.put("bkId", tbInfo.getBkId());
        res.put("bkName",  tbInfo.getBkName());
        res.put("bkClass",  tbInfo.getBkClass());
        res.put("bkAuth",  tbInfo.getBkAuth());
        res.put("zi_id",zi_id);
        //1，查看用户借阅数是否超过最大上限
        QueryWrapper<TbBookborrow> qw1 = new QueryWrapper<>();
        qw1.eq(TbBookborrow.COL_RD_ID,rd_id);
        if(tbBookborrowMapper == null){
            System.out.println("-----------------------");
        }
//        tbBookborrowMapper.selectCount(qw1);
        int i  = tbBookborrowMapper.selectCount(qw1);
        QueryWrapper<SysReaderRoleRow> qw2 = new QueryWrapper<>();
        qw2.eq(SysReaderRoleRow.COL_RIGHTID,"1001");
        //4为新增的对办证机来说的条件
        qw2.eq(SysReaderRoleRow.COL_ROLEID,"4");
        SysReaderRoleRow rw = readerRoleRowMapper.selectOne(qw2);
        if(i > rw.getRightvalue()){
            res.put("code",0);
            res.put("msg","用户已达到最大借阅数："+rw.getRightvalue());
        }else {
            //2,判断此书的状态
            QueryWrapper<TbBookstore> qw3 = new QueryWrapper<>();
            qw3.eq(TbBookstore.COL_BARID,zi_id);
            TbBookstore tbBookstore = tbBookstoreMapper.selectOne(qw3);
            if(tbBookstore == null){
                res.put("code",0);
                res.put("msg", "在图书档案未找到此本书记录，可能未将此书记录。请连接管理员操作");
            }else if(tbBookstore.getState() != 0){
                //代表此书不在状态
                res.put("code",0);
                res.put("msg", StateConfig.map.get(tbBookstore.getState()));
            }else if(tbBookstore.getStypes() > 2){
                res.put("code",0);
                res.put("msg", StateConfig.map.get(8));
            }else {
                //3,代表此书可借，修改数据库
                //3.1修改借书记录表
                Date e = new Date();
                qw2 = new QueryWrapper<>();
                qw2.eq(SysReaderRoleRow.COL_RIGHTID,"1011");
                //4为新增的对办证机来说的条件
                qw2.eq(SysReaderRoleRow.COL_ROLEID,"4");
                SysReaderRoleRow rw5 = readerRoleRowMapper.selectOne(qw2);
                e.setDate(e.getDay()+rw5.getRightvalue());

                TbBookborrow tbBookborrow = TbBookborrow.builder().zlId(""+zi_id).rdId(""+rd_id).borDate(new Date()).rtnDate(e).username("办证机").remarks("").build();
                tbBookborrowMapper.insert(tbBookborrow);
                //3.2 修改馆藏图书表
                TbBookstore tbBookstore1 = TbBookstore.builder().barid(Integer.parseInt(zi_id)).state(1).stypes(2).build();
                tbBookstoreMapper.update(tbBookstore1,qw3);
                try {
                    List ij = RFIDUtils.closeESAByBookId(zi_id);
                    if(ij.size() < 1){
                        res.put("code",0);
                        res.put("msg",  "关闭图书安全状态失败，请联系管理员(请勿私自操作)");
                    }

                } catch (Exception exception) {
                    res.put("code",0);
                    res.put("msg",  "关闭图书安全状态失败，请联系管理员(请勿私自操作)");
                    exception.printStackTrace();
                }


            }
        }
        return res;
//        return null;
    }

    /**
     * 图书归还
     * @param zi_id 书编号
     * @return code    1 表示归还成功，0表示失败
     *         msg    描述
     *          zi_id  书编号
     */
    @BookBorrowLog(logType = BookBorrowLogType.RETURNED)
    public Map<String,Object> bookBack(String zi_id){
        Map<String,Object> res = new HashMap<>();
        res.put("zi_id",zi_id);
        res.put("code",1);
        res.put("msg","图书归还成功");
        TbInfo tbInfo = getBookByZi_id(zi_id);
        res.put("bkId", tbInfo.getBkId());
        res.put("bkName",  tbInfo.getBkName());
        res.put("bkClass",  tbInfo.getBkClass());
        res.put("bkAuth",  tbInfo.getBkAuth());
        //1检查书的状态，借书记录表是否有记录，图书表是否有记录
        QueryWrapper<TbBookborrow> qw1 = new QueryWrapper<>();
        qw1.eq(TbBookborrow.COL_ZL_ID,zi_id);
        //在这把续借次数设置为9869，伪删除，在操作日记是在删除，操作日记需要此记录

        int i = tbBookborrowMapper.update(TbBookborrow.builder().contCnt(9869).build(),qw1);
        if(i == 0){
            res.put("code",0);
            res.put("msg","此书已被归还"+"");
        }else {
            QueryWrapper<TbBookstore> qw2 = new QueryWrapper<>();
            qw2.eq(TbBookstore.COL_BARID,zi_id);
            TbBookstore bookstore = TbBookstore.builder().state(0).build();
            int j = tbBookstoreMapper.update(bookstore,qw2);
            if(i == 0){

                res.put("code",0);
                res.put("msg","此书没有记录："+"请连接管理员");
            }else {
                try {
                    List ij = RFIDUtils.enableESAByBookId(zi_id);
                    if(ij.size() < 1) {
                        res.put("code", 0);
                        res.put("msg", "开启图书安全状态失败，请联系管理员(请勿私自操作)");
                    }

                } catch (Exception exception) {
                    res.put("code",0);
                    res.put("msg",  "开启图书安全状态失败，请联系管理员(请勿私自操作)");
                    exception.printStackTrace();
                }

            }

        }
        return res;
    }

    //通过图书id查找图书信息
    public TbInfo getBookByZi_id(String id){
        QueryWrapper<TbBookstore> qw = new QueryWrapper<>();
        qw.eq(TbBookstore.COL_BARID,id);
        QueryWrapper<TbInfo> qw1 = new QueryWrapper<>();
        qw1.eq(TbInfo.COL_BK_ID,tbBookstoreMapper.selectOne(qw).getBkId());
        return tbInfoMapper.selectOne(qw1);
    }

    @Override
    public List<TbBookborrow> borrowList(Integer rd_id) {
        QueryWrapper<TbBookborrow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TbBookborrow.COL_RD_ID,rd_id);
        queryWrapper.orderByAsc(TbBookborrow.COL_RTN_DATE);
        List<TbBookborrow> tbBookborrows = tbBookborrowMapper.selectList(queryWrapper);
        for (TbBookborrow tbBookborrow : tbBookborrows) {
            TbInfo bookByZi_id = this.getBookByZi_id(tbBookborrow.getZlId());
            tbBookborrow.setBkName(bookByZi_id.getBkName());
            tbBookborrow.setBkAuth(bookByZi_id.getBkAuth());
        }
        return tbBookborrows;
    }

    @Override
    public Result bookRenew(Integer id) {

        TbBookborrow tbBookborrow = tbBookborrowMapper.selectById(id);
        String rdId = tbBookborrow.getRdId();
        Integer conCnt = tbReaderService.findConCnt(Integer.valueOf(rdId));
        if (conCnt<=tbBookborrow.getContCnt()){
            return Result.error("已到达最大续借次数");
        }

        DateTime offset = DateUtil.offsetDay(tbBookborrow.getRtnDate(),3);
        tbBookborrow.setRtnDate(offset);
        tbBookborrow.setContCnt(tbBookborrow.getContCnt()+1);
        int i = tbBookborrowMapper.updateById(tbBookborrow);
        if (i>0){
            return Result.success("续借成功");
        }
        return Result.error("续借失败");
    }
}
