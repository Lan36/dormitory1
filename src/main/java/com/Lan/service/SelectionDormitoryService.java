package com.Lan.service;

import com.Lan.mapper.SelectionDormitoryMapper;
import com.Lan.entity.SelectionDormitory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SelectionDormitoryService {

    @Autowired
    private SelectionDormitoryMapper selectionDormitoryMapper;

    /**
     * 根据班级ID和宿舍ID字符串列表创建新的宿舍选择记录。
     * 删除该班级之前的所有宿舍选择记录，然后为每个提供的宿舍ID创建新的记录。
     *
     * @param clazzId 班级的ID，字符串形式，用于指定要操作的班级。
     * @param dormitoryIds 宿舍ID的字符串列表，以逗号分隔，用于指定要为该班级选择的宿舍。
     * @return 始终返回1，表示操作成功。
     */
    public int create(String clazzId,String dormitoryIds) {
        // 将宿舍ID字符串分割成数组
        String[] arr = dormitoryIds.split(",");
        // 先删除该班级所有的宿舍选择记录
        selectionDormitoryMapper.deleteByClazzId(Integer.parseInt(clazzId));
        for (String s : arr) {
            // 忽略空或空字符串的宿舍ID
            if(!StringUtils.isEmpty(s)){
                // 创建新的宿舍选择记录并插入数据库
                SelectionDormitory selectionDormitory = new SelectionDormitory();
                selectionDormitory.setClazzId(Integer.parseInt(clazzId));
                selectionDormitory.setDormitoryId(Integer.parseInt(s));
                selectionDormitoryMapper.create(selectionDormitory);
            }
        }
        return 1;
    }


    public int delete(String ids) {
        String[] arr = ids.split(",");
        int row = 0;
        for (String s : arr) {
            if(!StringUtils.isEmpty(s)){
                selectionDormitoryMapper.delete(Integer.parseInt(s));
            row++;
            }
        }
        return row;
    }

    public int delete(Integer id) {
        return selectionDormitoryMapper.delete(id);
    }

    public int update(SelectionDormitory selectionDormitory) {
        return selectionDormitoryMapper.update(selectionDormitory);
    }

    public int updateSelective(SelectionDormitory selectionDormitory) {
        return selectionDormitoryMapper.updateSelective(selectionDormitory);
    }

    //查询符合条件的选寝信息，并返回分页后的查询结果。
    public PageInfo<SelectionDormitory> query(SelectionDormitory selectionDormitory) {
        if(selectionDormitory != null && selectionDormitory.getPage() != null){
            PageHelper.startPage(selectionDormitory.getPage(),selectionDormitory.getLimit());
        }
        return new PageInfo<SelectionDormitory>(selectionDormitoryMapper.query(selectionDormitory));
    }

    public SelectionDormitory detail(Integer id) {
        return selectionDormitoryMapper.detail(id);
    }

    public int count(SelectionDormitory selectionDormitory) {
        return selectionDormitoryMapper.count(selectionDormitory);
    }
}
