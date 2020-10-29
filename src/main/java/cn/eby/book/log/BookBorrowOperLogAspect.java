package cn.eby.book.log;

import cn.eby.book.entity.TbBookborrow;
import cn.eby.book.entity.TbBookstore;
import cn.eby.book.entity.TbBorrowed;
import cn.eby.book.mapper.TbBookborrowMapper;
import cn.eby.book.mapper.TbBookstoreMapper;
import cn.eby.book.mapper.TbBorrowedMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class BookBorrowOperLogAspect {
    @Autowired
    private TbBorrowedMapper borrowedMapper;
    @Autowired
    private TbBookborrowMapper tBookborrowMapper;
    @Autowired
    private TbBookstoreMapper bookstoreMapper;
    @Pointcut("@annotation(cn.eby.book.log.BookBorrowLog)")
    public void logPointOut(){};

    @AfterReturning(pointcut = "logPointOut()",returning = "jsonResult")
    public Void doAfterReturn(JoinPoint joinPoint,Object jsonResult){
        //判断命令是否执行成功
        try{
            BookBorrowLog controllerBookBorrowLog = getAnnotationLog(joinPoint);


            if(controllerBookBorrowLog == null){
                return null;
            }
            //返回的结果
            Map<String,Object> map = (Map<String,Object>)jsonResult;

            TbBorrowed tbBorrowed = TbBorrowed.builder().logtype(controllerBookBorrowLog.logType().getValue()).logtime(new Date()).username("借卡机").build();
                //向数据库添加操作日记
                if((int)map.get("code") == 1){
                    //通过图书编号在 借书库找到对应记录,还书后，就找不到这条记录了，所有删除的时候假删除，在日记这进行
                    QueryWrapper<TbBookborrow> qw1 = new QueryWrapper<>();
                    qw1.eq(TbBorrowed.COL_ZL_ID,map.get("zi_id"));
                    TbBookborrow tbBookborrow = tBookborrowMapper.selectOne(qw1);
                    //如果是还书，删除借书表记录
                    if(controllerBookBorrowLog.logType() == BookBorrowLogType.RETURNED){
                        //在这把续借次数设置为9869，伪删除，在操作日记是在删除，操作日记需要此记录
                        QueryWrapper<TbBookborrow> qw4 = new QueryWrapper<>();
                        qw4.eq(TbBookborrow.COL_CONT_CNT,"9869");
                        //在这把续借次数设置为9869，伪删除，在操作日记是在删除，操作日记需要此记录
                        tBookborrowMapper.delete(qw4);
                    }
                    //查找图书所在的编目
                    QueryWrapper<TbBookstore> qw2 = new QueryWrapper<>();
                    qw2.eq(TbBookstore.COL_BARID,map.get("zi_id"));
                    TbBookstore tbBookstore = bookstoreMapper.selectOne(qw2);

//                    zlId(controllerLog.zi_id()).rdId(Integer.parseInt(tbBookborrow.getRdId())).zlKind(tbBookstore.getBkId()).
                    tbBorrowed.setZlId(Integer.parseInt(""+map.get("zi_id")));
                    String i = tbBookborrow.getRdId().equals("null") ? "0000":tbBookborrow.getRdId();
                    tbBorrowed.setRdId(Integer.parseInt(i));
                    tbBorrowed.setZlKind(tbBookstore.getBkId());
                    borrowedMapper.insert(tbBorrowed);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
    //获取切入点的注解
    private BookBorrowLog getAnnotationLog(JoinPoint joinPoint) throws Exception{
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();

        if(method != null){
            return method.getAnnotation(BookBorrowLog.class);
        }
        return null;
    }
}
