package com.cafuc.graduation.user.service;

import com.cafuc.graduation.user.entity.bo.InterfaceConfineBo;
import com.cafuc.graduation.user.entity.po.InterfaceConfinePo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户-接口限制，限制调用次数 服务类
 * </p>
 *
 * @author flyeat
 * @since 2020-05-27
 */
public interface IInterfaceConfineService extends IService<InterfaceConfinePo> {

    /**
     * <p>
     * 新增接口限制或自增接口调用次数
     * </p>
     *
     * @param bo bo
     * @author shijintao@supconit.com
     * @date 2020/5/27 08:47
     */
    Boolean addOrAutoIncrease(InterfaceConfineBo bo);

    /**
     * <p>
     * 通过userId和interfacePath查询是否可调用及剩余次数
     * </p>
     *
     * @param userId        userId
     * @param interfacePath interfacePath
     * @return {@link String }
     * @author shijintao@supconit.com
     * @date 2020/5/27 08:51
     */
    String queryInvokingAble(Long userId, String interfacePath);


}
