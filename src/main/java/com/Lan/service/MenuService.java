package com.Lan.service;

import com.Lan.mapper.MenuMapper;
import com.Lan.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public List<Menu> query(Integer userId){
        return menuMapper.query(userId);
    }
    public List<Menu> list(){
        return menuMapper.list();
    }

    public List<Integer> queryCheckMenuId(Integer userId){
        return menuMapper.queryCheckMenuId(userId);
    }

    public List<Menu> queryByType(){
        return menuMapper.queryByType();
    }

}
