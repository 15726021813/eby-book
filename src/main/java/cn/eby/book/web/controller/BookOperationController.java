package cn.eby.book.web.controller;


import RFID.RFIDUtils;
import cn.eby.book.common.Result;
import cn.eby.book.entity.TbBookborrow;
import cn.eby.book.entity.TbInfo;
import cn.eby.book.service.BookOperationServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author 朱胜伟
 * 图书操作控制器
 */
@RestController
@RequestMapping("/bookOperation")
public class BookOperationController {

    @Autowired
    BookOperationServer bookOperationServer;

    /**
     *借书类，先要关闭书标签的状态，并获取关闭成功的书，再将这此书保存到数据库（//如果书保存到数据库失败，在去修改书asc)
     *  ziId 书编号,多本书
     *  rdId 用户编号
     * @return  code  1 代表借书成功，0代表失败
     *          msg   描述
     *          zi_id  书编号
     */
    @RequestMapping("/bookBorrow")
    public Result bookBorrow(Integer rdId) throws Exception{
//        w.test();
        //得到的是设置成功的序列号
//        List<String> uids =RFIDUtils.closeESAs();
        //通过序列号获取内容
        Set<String> books = RFIDUtils.getTabs(0);
//        List<String> books = new ArrayList<>();


//        books.add("12312");

        List<Map<String,Object>> list = new ArrayList<>();
        for (String integ : books) {
//            w.test();
//            bookOperationServer.bookBorrow(integ,rdId);
            list.add(bookOperationServer.bookBorrow(integ,rdId));
            //检查
        }


        return Result.success(list);

    }



    /**
     * 图书归还
     * @param
     * @return code    1 表示归还成功，0表示失败
     *         msg    描述
     *          zi_id  书编号
     */
    @RequestMapping("/bookBack")
    public Result bookBack() throws Exception{
        //通过序列号获取内容
        Set<String> books = RFIDUtils.getTabs(0);

        List<Map<String,Object>> list = new ArrayList<>();
        for (String integer : books) {
            list.add(bookOperationServer.bookBack(integer));
        }

        return Result.success(list);

    }

    @RequestMapping("/getTabData")
    public Result getTabData(){
        Set<String> list = null;
        try {
            list = RFIDUtils.getTabs(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(list);
    }

    /**
     * 通过图书id查询图书信息
     * @return
     */
    @RequestMapping("/getBookByZi_id")
    public Result getBookByZi_id(@RequestBody List<String> zk_id){
        List<TbInfo> list = new ArrayList<>();
        for (String s : zk_id) {
            list.add(bookOperationServer.getBookByZi_id(s));
        }
        return Result.success(list);
    }


    @RequestMapping("BookRenew")
    public Result  BookRenew(Integer id)  {
       return bookOperationServer.bookRenew(id);
    }

    @RequestMapping("BorrowList")
    public Result  borrowList(Integer rdId){
        List<TbBookborrow> list = bookOperationServer.borrowList(rdId);
        if (list==null||list.size()==0){
            return Result.error(null);
        }
        return Result.success(list);
    }
}
