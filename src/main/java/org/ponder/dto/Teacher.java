package org.ponder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangxin
 * @since 2024/8/27 21:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {

    /**
     * 老师名称
     */
    private String teacherName;

    /**
     * 技能列表
     */
    private List<String> courseList;
}
