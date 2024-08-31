package org.ponder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangxin
 * @since 2024/8/27 21:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentGroup {

    /**
     * 班级名称
     */
    private String studentGroupName;

    /**
     * 课程、周课时、任课老师
     */
    private List<Course> courseList;
}

