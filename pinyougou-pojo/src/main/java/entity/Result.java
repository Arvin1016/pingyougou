package entity;


/**
 * @author : Arvin
 * @Project Name : pinyougou-parent
 * @Package Name : entity
 * @Creation Date: 2019-08-04 0:13
 * @Description : 返回结果
 */
public class Result {
    //保存是否成功
    private boolean success;
    //返回信息
    private String message;

    public Result() {
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
