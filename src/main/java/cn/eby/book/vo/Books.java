package cn.eby.book.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Books {

    @NotNull(message = "书籍ID不能为空！")
    private String[] ziId;

    @NotNull(message = "借阅者ID不能为空！")
    private Integer rdId;

}
