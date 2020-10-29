package cn.eby.book.service;

import cn.eby.book.entity.PayOrder;
import cn.eby.book.entity.TbBookborrow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/26/14:38
 * @Description:
 */
public interface TbBookborrowService  extends IService<TbBookborrow> {


    /**
     * 查询是否有未还图书
     * @param rdId
     * @return
     */
    boolean  bookBorrow(Integer rdId);
}
