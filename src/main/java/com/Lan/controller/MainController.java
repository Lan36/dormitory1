package com.Lan.controller;

import com.github.pagehelper.PageInfo;
import com.Lan.entity.Building;
import com.Lan.entity.Dormitory;
import com.Lan.entity.Notice;
import com.Lan.service.BuildingService;
import com.Lan.service.DormitoryService;
import com.Lan.service.DormitoryStudentService;
import com.Lan.service.NoticeService;
import com.Lan.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private DormitoryService dormitoryService;
    @Autowired
    private DormitoryStudentService dormitoryStudentService;
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/building")
    public Result building(){
        Building building = new Building();
        building.setLimit(1000);
        PageInfo<Building> pageInfo = buildingService.query(building);
        List<Map<String,Object>> list = new ArrayList<>();
        DecimalFormat df   = new DecimalFormat("######0.00");
        pageInfo.getList().forEach(entity->{
            Map<String,Object> map = new HashMap<>();
            Dormitory param = new Dormitory();
            param.setBuildingId(entity.getId());
            param.setLimit(1000000);

            PageInfo<Dormitory> dormitoryPageInfo = dormitoryService.query(param);
            int all = dormitoryPageInfo.getList().size();
            map.put("name",entity.getName());
            map.put("all",all);
            int used = dormitoryStudentService.countByBuildingId(entity.getId());
            map.put("used",used);
            int unused = all-used;
            map.put("unused",unused);
            if(all == 0){
                map.put("percent",0);
            }else{
                map.put("percent",df.format((float)used/all));
            }

            list.add(map);
        });
        return Result.ok(list);
    }

    @GetMapping("/notice")
    public Result notice(){
        Notice notice = new Notice();
        notice.setLimit(5);
        PageInfo<Notice> pageInfo = noticeService.query(notice);
        return Result.ok(pageInfo.getList());
    }


}
