package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        Long currentId = BaseContext.getUserId();
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        //查看是否有相同的菜品
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
        if (shoppingCart1 != null) {
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number+1);
            shoppingCartService.updateById(shoppingCart1);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCart1 = shoppingCart;
        }
        return R.success(shoppingCart1);
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long currentId = BaseContext.getUserId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        //log.info("shoppingCart:{}",shoppingCart);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getUserId();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
        Integer number = shoppingCart1.getNumber();
        if (number > 1) {
            shoppingCart1.setNumber(number-1);
            shoppingCartService.updateById(shoppingCart1);
        } else {
            shoppingCartService.removeById(shoppingCart1.getId());
        }

        return R.success("success");
    }
    @DeleteMapping("/clean")
    public R<String> clean() {
        Long currentId = BaseContext.getUserId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(queryWrapper);
        return R.success("success");
    }
}
