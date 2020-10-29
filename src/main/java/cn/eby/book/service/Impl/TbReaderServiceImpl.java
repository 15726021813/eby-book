package cn.eby.book.service.Impl;

import cn.eby.book.common.core.page.TableDataInfo;
import cn.eby.book.common.exception.CustomException;
import cn.eby.book.entity.SysReaderRoleRow;
import cn.eby.book.mapper.SysReaderRoleRowMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.eby.book.entity.TbReader;
import cn.eby.book.mapper.TbReaderMapper;
import cn.eby.book.service.TbReaderService;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/12/15:43
 * @Description: 
 */
@Service
public class TbReaderServiceImpl extends ServiceImpl<TbReaderMapper, TbReader> implements TbReaderService{

    @Autowired
    private TbReaderMapper tbReaderMapper;

    @Autowired
    private SysReaderRoleRowMapper sysReaderRoleRowMapper;

    @Override
    public TableDataInfo list(int page, int size) {
        IPage<TbReader>  tbReaderIPage = new Page<>(page,size);
        tbReaderMapper.selectPage(tbReaderIPage, null);

        return new TableDataInfo(tbReaderIPage.getRecords(), (int) tbReaderIPage.getTotal());
    }

    @Override
    public TbReader findById(Long readerId) {
        QueryWrapper<TbReader> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TbReader.COL_RD_ID,readerId);
        TbReader tbReader = tbReaderMapper.selectOne(queryWrapper);
        return tbReader;
    }

    @Override
    public Integer findLastId() {
        return tbReaderMapper.findLastId();
    }

    @Override
    public TbReader findByIdCard(String IdCard) {
        QueryWrapper<TbReader> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TbReader.COL_ID_CARD,IdCard);
        queryWrapper.eq(TbReader.COL_RD_STATE,0);
        TbReader tbReader = tbReaderMapper.selectOne(queryWrapper);
        return tbReader;
    }

    @Override
    public void updateState(Integer rdId) {
        QueryWrapper<TbReader> queryWrapper = new  QueryWrapper<>();
        queryWrapper.eq("rd_id",rdId);
        TbReader tbReader = new TbReader();
        tbReader.setRdState(4);
        tbReaderMapper.update(tbReader,queryWrapper);
    }

    @Override
    public Integer findConCnt(Integer rdId) {

        //获取用户角色ID
        QueryWrapper<TbReader> queryWrapper = new  QueryWrapper<>();
        queryWrapper.eq("rd_id",rdId);
        TbReader tbReader = tbReaderMapper.selectOne(queryWrapper);
       //获取角色值
        QueryWrapper<SysReaderRoleRow> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(SysReaderRoleRow.COL_ROLEID,tbReader.getRdRoleid());
        queryWrapper1.eq(SysReaderRoleRow.COL_RIGHTID,"1012");
        SysReaderRoleRow sysReaderRoleRow = sysReaderRoleRowMapper.selectOne(queryWrapper1);
        return sysReaderRoleRow.getRightvalue();
    }

    @Override
    public String findIdByCardNo(String cardNo) {
        QueryWrapper<TbReader> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TbReader.COL_ID_CARD,cardNo);
        queryWrapper.eq(TbReader.COL_RD_STATE,0);
        TbReader tbReader = tbReaderMapper.selectOne(queryWrapper);
        if (tbReader==null){
            throw new CustomException("没有查到该身份证信息");
        }
        return String.valueOf(tbReader.getRdId());
    }


    @Override
    public boolean save(TbReader entity) {
        int insert = tbReaderMapper.insert(entity);
        return insert >0;

    }
}
