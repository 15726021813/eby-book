package cn.eby.book.service;

import cn.eby.book.common.core.page.TableDataInfo;
import cn.eby.book.entity.TbReader;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.service.IService;
    
/**
 * @Auther: 徐长乐
 * @Date: 2020/09/12/15:43
 * @Description: 
 */
public interface TbReaderService extends IService<TbReader>{

    TableDataInfo list(int page, int size);

    TbReader findById(Long ReaderId);


    Integer findLastId();


    TbReader findByIdCard(String IdCard);

    void updateState(Integer rdId);

    Integer findConCnt(Integer rdId);

    String  findIdByCardNo(String cardNo);


}
