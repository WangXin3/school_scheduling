package org.ponder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    /**
     * 课程名
     */
    private String courseName;

    /**
     * 每周课时
     */
    private Integer num;

    /**
     * 老师
     */
    private Teacher teacher;
}