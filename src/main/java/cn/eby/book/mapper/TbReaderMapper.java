package cn.eby.book.mapper;

import cn.eby.book.entity.TbReader;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * @Auther: 徐长乐
 * @Date: 2020/09/12/15:43
 * @Description: 
 */
public interface TbReaderMapper extends BaseMapper<TbReader> {



    Integer findLastId();

}