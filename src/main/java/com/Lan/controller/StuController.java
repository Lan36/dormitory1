package com.Lan.controller;

import com.github.pagehelper.PageInfo;
import com.Lan.entity.*;
import com.Lan.service.*;
import com.Lan.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/stu")
public class StuController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private SelectionDormitoryService selectionDormitoryService;
    @Autowired
    private DormitoryService dormitoryService;
    @Autowired
    private DormitoryStudentService dormitoryStudentService;
    @Autowired
    private AbsenceService absenceService;
    @Autowired
    private RepairService repairService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;
    @Autowired
    private SelectionService selectionService;
    @Autowired
    private BuildingService buildingService;

    @GetMapping("/info")
    public Result info(HttpServletRequest request){
        Student param = (Student)request.getAttribute("student");
        Student student = studentService.detail(param.getId());
        student.setOrg(orgService.detail(student.getClazzId()));
        student.setGrade(gradeService.detail(student.getGradeId()));
        return Result.ok(student);
    }



    /**
     * 查询并展示可选宿舍信息
     *
     * @param request HttpServletRequest对象，用于获取请求中的参数和属性
     * @return Result对象，包含宿舍选择列表的信息
     */
    @GetMapping("/select_dormitory")
    public Result select_dormitory(HttpServletRequest request){
        // 从请求中获取学生信息
        Student param = (Student)request.getAttribute("student");
        Student student = studentService.detail(param.getId());

        // 初始化宿舍选择对象并设置初始参数
        SelectionDormitory selectionDormitory = new SelectionDormitory();
        selectionDormitory.setClazzId(student.getClazzId());
        selectionDormitory.setLimit(1000);
        // 查询符合筛选条件的宿舍信息
        PageInfo<SelectionDormitory> pageInfo = selectionDormitoryService.query(selectionDormitory);

        List<Map<String,Object>> list = new ArrayList<>();
        // 遍历查询结果，组装每个宿舍的详细信息
        List<SelectionDormitory> selectionDormitorys = pageInfo.getList();
        for (SelectionDormitory sd : selectionDormitorys) {
            Map<String,Object> map = new HashMap<>();
            // 查询宿舍基础信息
            Dormitory dormitory = dormitoryService.detail(sd.getDormitoryId());
            map.put("capacity",dormitory.getCapacity());
            map.put("id",dormitory.getId());
            map.put("no",dormitory.getNo());
            map.put("sex",dormitory.getSex());
            // 查询宿舍所在楼栋信息
            Building building = buildingService.detail(dormitory.getBuildingId());
            map.put("buildingName",building.getName());

            // 查询已选择该宿舍的学生数量
            DormitoryStudent ds = new DormitoryStudent();
            ds.setDormitoryId(sd.getDormitoryId());
            ds.setLimit(1000);
            PageInfo<DormitoryStudent> dormitoryStudents = dormitoryStudentService.query(ds);
            map.put("selected",dormitoryStudents.getList().size());

            // 构造已选择宿舍的学生信息列表
            List<Map<String,Object>> studentMapList = new ArrayList<>();
            List<DormitoryStudent> list1 = dormitoryStudents.getList();
            list1.forEach(ds1->{
                Map<String,Object> studentMap = new HashMap<>();
                Student detail = studentService.detail(ds1.getStudentId());
                studentMap.put("stuNo",detail.getStuNo());
                studentMap.put("name",detail.getName());
                studentMap.put("bedId",ds1.getBedId());
                studentMapList.add(studentMap);
            });
            map.put("studentList",studentMapList);
            list.add(map);
        }
        // 返回组装好的宿舍选择列表
        return Result.ok(list);
    }


    @PostMapping("/select_dormitory_submit")
    public Result select_dormitory(@RequestBody Map<String,String> map,HttpServletRequest request){
        Student param = (Student)request.getAttribute("student");
        Student student = studentService.detail(param.getId());

        List<Selection> selections = selectionService.queryByClazzId(student.getClazzId());
        if(selections != null && selections.size()==0){
            return Result.fail("操作失败，未设置！请联系管理员");
        }
        Selection selection = selections.get(0);
        if(selection.getStartTime().getTime()>System.currentTimeMillis() || System.currentTimeMillis()>selection.getEndTime().getTime()){
            return Result.fail("操作失败，不在时间段内选择");

        }
        String bedId = map.get("bedId");
        String dormitoryId = map.get("dormitoryId");
        int row = dormitoryStudentService.select_dormitory_submit(student.getId(),Integer.parseInt(dormitoryId),Integer.parseInt(bedId));
        if(row>0){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }


    @PostMapping("/absence")
    public Map<String,Object> query(@RequestBody Absence absence,HttpServletRequest request){
        Student param = (Student)request.getAttribute("student");
        absence.setStudentId(param.getId());
        PageInfo<Absence> pageInfo = absenceService.query(absence);
        pageInfo.getList().forEach(entity->{
            Student detail = studentService.detail(entity.getStudentId());
            entity.setStudent(detail);
            Dormitory dormitory = dormitoryService.detail(entity.getDormitoryId());
            entity.setDormitory(dormitory);
        });
        return Result.ok(pageInfo);
    }

    @PostMapping("repair_create")
    public Result repair_create(@RequestBody Repair repair,HttpServletRequest request){
        Student param = (Student)request.getAttribute("student");
        DormitoryStudent ds = new DormitoryStudent();
        ds.setStudentId(param.getId());
        PageInfo<DormitoryStudent> pageInfo = dormitoryStudentService.query(ds);
        if(pageInfo.getList() != null && pageInfo.getList().size()>0){
            DormitoryStudent dormitoryStudent = pageInfo.getList().get(0);
            Dormitory detail = dormitoryService.detail(dormitoryStudent.getDormitoryId());
            repair.setBuildingId(detail.getBuildingId());
            repair.setDormitoryId(dormitoryStudent.getDormitoryId());
            repair.setStudentId(param.getId());
            repair.setCreateDate(new Date());
            repair.setStatus(0);
            int flag = repairService.create(repair);
            if(flag>0){
                return Result.ok();
            }else{
                return Result.fail();
            }
        }else{
            return Result.fail("操作失败，没有关联宿舍");
        }
    }


    @PostMapping("notice_query")
    public Map<String, Object> notice_query(@RequestBody Notice notice, HttpServletRequest request){
        Student param = (Student)request.getAttribute("student");
        DormitoryStudent ds = new DormitoryStudent();
        ds.setStudentId(param.getId());
        PageInfo<Notice> noticePageInfo = null;
        PageInfo<DormitoryStudent> pageInfo = dormitoryStudentService.query(ds);
        if(pageInfo.getList() != null && pageInfo.getList().size()>0){
            DormitoryStudent dormitoryStudent = pageInfo.getList().get(0);
            Dormitory detail = dormitoryService.detail(dormitoryStudent.getDormitoryId());
            notice.setBuildingId(detail.getBuildingId());
            noticePageInfo = noticeService.queryByBuildingId(notice);
            noticePageInfo.getList().forEach(entity->{
                entity.setUser(userService.detail(entity.getUserId()));
            });
            return Result.ok(noticePageInfo);
        }else{
            return Result.ok(noticePageInfo);
        }
    }
}
