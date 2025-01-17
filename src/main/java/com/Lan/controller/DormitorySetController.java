package com.Lan.controller;

import com.github.pagehelper.PageInfo;
import com.Lan.entity.DormitorySet;
import com.Lan.service.DormitorySetService;
import com.Lan.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dormitorySet")
public class DormitorySetController {

    @Autowired
    private DormitorySetService dormitorySetService;

    @PostMapping("create")
    public Result create(@RequestBody DormitorySet dormitorySet){
        int flag = dormitorySetService.create(dormitorySet);
        if(flag>0){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @GetMapping("delete")
    public Result delete(String ids){
        int flag = dormitorySetService.delete(ids);
        if(flag>0){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @PostMapping("update")
    public Result update(@RequestBody DormitorySet dormitorySet){
        int flag = dormitorySetService.update(dormitorySet);
        if(flag>0){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @GetMapping("detail")
    public DormitorySet detail(Integer id){
        return dormitorySetService.detail(id);
    }

    @PostMapping("query")
    public Map<String,Object> query(@RequestBody  DormitorySet dormitorySet){
        PageInfo<DormitorySet> pageInfo = dormitorySetService.query(dormitorySet);
        return Result.ok(pageInfo);
    }

}
