package com.cafuc.graduation.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cafuc.graduation.user.dao.InterfaceConfineDao;
import com.cafuc.graduation.user.entity.bo.InterfaceConfineBo;
import com.cafuc.graduation.user.entity.po.InterfaceConfinePo;
import com.cafuc.graduation.user.service.IInterfaceConfineService;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-接口限制，限制调用次数 服务实现类
 * </p>
 *
 * @author flyeat
 * @since 2020-05-27
 */
@Service
public class InterfaceConfineServiceImpl extends ServiceImpl<InterfaceConfineDao, InterfaceConfinePo> implements IInterfaceConfineService {


    @Override
    public Boolean addOrAutoIncrease(InterfaceConfineBo bo) {
        LambdaQueryWrapper<InterfaceConfinePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceConfinePo::getUserId, bo.getUserId())
                .eq(InterfaceConfinePo::getInterfacePath, bo.getInterfacePath());
        InterfaceConfinePo one = getOne(queryWrapper);
        InterfaceConfinePo ready = new InterfaceConfinePo();
        // 如果存在则已调用次数增加
        if (one != null) {
            BeanUtils.copyProperties(one, ready);
            ready.setUserInvokeCount(ready.getUserInvokeCount() + 1);
        }
        // 如果不存在则新增
        else {
            BeanUtils.copyProperties(bo, ready);
            ready.setUserInvokeCount(1);
        }
        return saveOrUpdate(ready);
    }

    @Override
    public String queryInvokingAble(Long userId, String interfacePath) {
        LambdaQueryWrapper<InterfaceConfinePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceConfinePo::getUserId, userId)
                .eq(InterfaceConfinePo::getInterfacePath, interfacePath);
        InterfaceConfinePo one = getOne(queryWrapper);
        // 剩余次数
        int residue = one.getConfineCount() - one.getUserInvokeCount();
        // 是否已经超出调用限制
        boolean moreThan = residue <= 0;
        int res = moreThan ? 0 : residue;
        // 如果限制调用次数
        if (one.getInvokingForbid()) {
            return res + "_limited";
        }
        // 不做限制
        else {
            return res + "_unlimited";
        }
    }

    /**
     * <p>
     * 每天零点清除接口调用次数限制
     * </p>
     *
     * @author shijintao@supconit.com
     * @date 2020/5/27 09:58
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void reCountInterface() {
        LambdaUpdateWrapper<InterfaceConfinePo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(InterfaceConfinePo::getUserInvokeCount, 0);
        update(updateWrapper);
    }

}
