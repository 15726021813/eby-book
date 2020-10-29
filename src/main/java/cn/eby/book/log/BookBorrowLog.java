package cn.eby.book.log;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BookBorrowLog {
    public BookBorrowLogType logType();
}
