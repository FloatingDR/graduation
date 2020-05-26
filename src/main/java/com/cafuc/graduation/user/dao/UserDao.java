package com.cafuc.graduation.user.dao;

import com.cafuc.graduation.user.entity.bo.InsertBo;
import com.cafuc.graduation.user.entity.po.UserPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author flyeat
 * @since 2020-05-25
 */
public interface UserDao extends BaseMapper<UserPo> {


    /**
     * <p>
     * 新增用户
     * </p>
     *
     * @param userPo userPo
     * @author shijintao@supconit.com
     * @date 2020/5/26 22:15
     */
    @Insert("INSERT INTO user(user_num,user_name,college,profession,class_name) " +
            "VALUES \n" +
            "(#{userNum},#{userName},#{college},#{profession},#{className})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void saveUser(UserPo userPo);
}
