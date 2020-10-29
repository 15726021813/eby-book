package cn.eby.book;


import cn.eby.book.entity.SysReaderRole;
import cn.eby.book.fuction.D1081.D1081;
import cn.eby.book.fuction.D1081.D1081Utils;
import cn.eby.book.fuction.rfid.IRfidImpl;
import cn.eby.book.service.BookOperationServer;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class EbyBookApplicationTests {

    @Autowired
    BookOperationServer bookOperationServer;
    @Autowired
    IRfidImpl iRfid;
    @Test
    void contextLoads() {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

        /**
         * D9911BDA E5C51324 15052D46 7097570A 7C0D2DE8 9C426BF     033409000000117
         *
         * D9D11B9A E5851364 15052D46 7097570A 7C4D2DA8 9C026BB     033409000000118
         *
         * 11110 01100010 10100011 10010011 11001010 01110110
         *
         */
        String str = "D9911BDAE5C5132415052D467097570A7C0D2DE89C426BF";


=======
        List<String> s = iRfid.getTabDatas(0,2);
        s.forEach(e->{
            System.out.println("---"+e);
        });
        System.out.println(iRfid.getCode());
        System.out.println(iRfid.getMsg());
    }
>>>>>>> f32f09b... irfid更新

=======
=======
>>>>>>> 0ae2f88... 1111

        /**
         * D9911BDA E5C51324 15052D46 7097570A 7C0D2DE8 9C426BF     033409000000117
         *
         * D9D11B9A E5851364 15052D46 7097570A 7C4D2DA8 9C026BB     033409000000118
         *
         * 11110 01100010 10100011 10010011 11001010 01110110
         *
         */
        String str = "D9911BDAE5C5132415052D467097570A7C0D2DE89C426BF";



<<<<<<< HEAD
>>>>>>> 0ae2f88... 1111
=======
>>>>>>> 0ae2f88... 1111
    }



}
