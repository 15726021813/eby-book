package cn.eby.book.common.exception;

import cn.eby.book.common.Result;
import cn.eby.book.common.constant.HttpStatus;
import cn.eby.book.common.utils.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/12/15:59
 * @Description:
 */
@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(CustomException.class)
    public Result businessException(CustomException e)
    {
        if (StringUtils.isNull(e.getCode()))
        {
            return Result.error(e.getMessage());
        }
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result HttpRquestMethond(){
        return Result.error(HttpStatus.BAD_METHOD,"请求方式错误");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result exceptionHandle(MethodArgumentNotValidException e){
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return  Result.error(objectError.getDefaultMessage());
    }


}
