package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.Result;
import com.shiyi.entity.Menu;
import java.util.List;

/**
 * @author blue
 * @description:
 * @date 2021/8/20 9:30
 */
public interface MenuService extends IService<Menu>{

    List<Menu> listMenuTree(List<Menu> list);

    Result listMenuApi(Integer id);

    Result insertMenu(Menu menu);

    Result updateMenu(Menu menu);

    Result deleteMenuById(Integer id);
}
