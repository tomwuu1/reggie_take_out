package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public void saveWithFlavors(DishDto dishDto);
    public DishDto getWithFlavors(Long id);

    public void updateWithFlavors(DishDto dishDto);
}
