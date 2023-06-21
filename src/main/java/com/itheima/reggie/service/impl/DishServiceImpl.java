package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Transactional
    public void saveWithFlavors(DishDto dishDto) {
        // 保存菜品
        this.save(dishDto);
        Long dishId = dishDto.getId();
        // 设置菜品口味的菜品id
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        dishFlavors = dishFlavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishId);
            return dishFlavor;
        }).collect(Collectors.toList());

        // 保存菜品口味
        dishFlavorService.saveBatch(dishFlavors);
    }
    public DishDto getWithFlavors(Long id) {
        // 查询菜品
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 查询菜品口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    @Transactional
    public void updateWithFlavors(DishDto dishDto) {
        this.updateById(dishDto);
        // 删除菜品口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 保存菜品口味
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        dishFlavors = dishFlavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavors);
    }


}
