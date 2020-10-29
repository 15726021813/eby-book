package cn.eby.book.log;


public enum BookBorrowLogType {
    BORROWED("借阅",(short)0),
    RETURNED("归还",(short)1),
    RENEWED("续借",(short)2),
    BEFORE_BORROWED("预借",(short)3),
    LOST("丢失",(short)4),
    REPORTED_LOSS("已报损",(short)5),
    DAMAGED("损坏不可用",(short)6),
    RET_LOST("丢失撤销",(short)7),
    DEPRECIATION("折旧",(short)8);
    private String msg;
    private short value;
    BookBorrowLogType(String msg, short value){
        this.msg = msg;
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }
}
