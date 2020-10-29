package cn.eby.book.service.Impl;

import cn.eby.book.entity.TbBookborrow;
import cn.eby.book.entity.TbReader;
import cn.eby.book.mapper.TbBookborrowMapper;
import cn.eby.book.mapper.TbReaderMapper;
import cn.eby.book.service.TbBookborrowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/26/14:39
 * @Description:
 */
@Service
public class TbBookBorrowServiceImpl  extends ServiceImpl<TbBookborrowMapper, TbBookborrow> implements TbBookborrowService {

    @Autowired
    private TbBookborrowMapper tbBookborrowMapper;
    @Override
    public boolean bookBorrow(Integer rdId) {
        QueryWrapper<TbBookborrow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TbBookborrow.COL_RD_ID,rdId);
        Integer count = tbBookborrowMapper.selectCount(queryWrapper);
        if (count>0) {
            return false;
        }
        return true;
    }
}
