package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;
import org.springframework.stereotype.Service;


public interface CategoryService extends IService<Category> {

    void removeById(Long id);
}
