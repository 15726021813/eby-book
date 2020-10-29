package cn.eby.book.service;

import cn.eby.book.common.Result;
import cn.eby.book.config.StateConfig;
import cn.eby.book.entity.SysReaderRoleRow;
import cn.eby.book.entity.TbBookborrow;
import cn.eby.book.entity.TbBookstore;
import cn.eby.book.entity.TbInfo;
import cn.eby.book.mapper.SysReaderRoleRowMapper;
import cn.eby.book.mapper.TbBookborrowMapper;
import cn.eby.book.mapper.TbBookstoreMapper;
import cn.eby.book.mapper.TbBorrowedMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 朱胜伟
 * 图书借阅操作类
 */
public interface BookOperationServer {

    /**
     *
     * @param zi_id 书编号
     * @param rd_id 用户编号
     * @return  code  1 代表借书成功，0代表失败
     *          msg   描述
     */
    public Map<String,Object> bookBorrow(String zi_id,Integer rd_id);

    /**
     * 图书归还
     * @param zi_id 书编号
     * @return code    1 表示归还成功，0表示失败
     *         msg    描述
     */
    public Map<String,Object> bookBack(String zi_id);
    //通过图书id查找图书信息
    public TbInfo getBookByZi_id(String id);

    List<TbBookborrow>  borrowList(Integer rd_id);

    Result bookRenew(Integer id);
}
