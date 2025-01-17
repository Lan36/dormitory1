package com.Lan.service;

import com.Lan.mapper.StoreyMapper;
import com.Lan.entity.Storey;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StoreyService {

    @Autowired
    private StoreyMapper storeyMapper;

    public int create(Storey storey) {
        return storeyMapper.create(storey);
    }

    public int delete(String ids) {
        String[] arr = ids.split(",");
        int row = 0;
        for (String s : arr) {
            if(!StringUtils.isEmpty(s)){
                storeyMapper.delete(Integer.parseInt(s));
            row++;
            }
        }
        return row;
    }

    public int delete(Integer id) {
        return storeyMapper.delete(id);
    }

    public int update(Storey storey) {
        return storeyMapper.update(storey);
    }

    public int updateSelective(Storey storey) {
        return storeyMapper.updateSelective(storey);
    }

    public PageInfo<Storey> query(Storey storey) {
        if(storey != null && storey.getPage() != null){
            PageHelper.startPage(storey.getPage(),storey.getLimit());
        }
        return new PageInfo<Storey>(storeyMapper.query(storey));
    }

    public Storey detail(Integer id) {
        return storeyMapper.detail(id);
    }

    public int count(Storey storey) {
        return storeyMapper.count(storey);
    }
}
