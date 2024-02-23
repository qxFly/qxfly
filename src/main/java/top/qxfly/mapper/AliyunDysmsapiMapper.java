package top.qxfly.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AliyunDysmsapiMapper {
    /**
     * 获取手机验证
     * @param phone
     * @return
     */
    @Select("select code from captcha where phone = #{phone}")
    List<String> getCode(String phone);

    /**
     *  删除验证码
     * @param phone
     */
    @Delete("delete from captcha where phone = #{phone}")
    void deleteCode(String phone);
}
