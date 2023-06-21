package com.itheima.reggie.service;

import com.itheima.reggie.entity.Orders;
import org.springframework.stereotype.Service;

@Service
public interface OrderService extends com.baomidou.mybatisplus.extension.service.IService<com.itheima.reggie.entity.Orders>{
    public default void submit(Orders orders) {
    }
}
