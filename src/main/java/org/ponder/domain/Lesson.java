package org.ponder.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.Objects;

/**
 * @author wangxin
 * @since 2024/8/27 20:28
 */
@Data
@PlanningEntity
@NoArgsConstructor
public class Lesson {

    @PlanningId
    private Long id;

    /**
     * 课程名
     */
    private String subject;

    /**
     * 老师
     */
    private String teacher;

    /**
     * 班级
     */
    private String studentGroup;



    @PlanningVariable
    private Timeslot timeslot;


    public Lesson(Long id, String subject, String teacher, String studentGroup) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.studentGroup = studentGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) && Objects.equals(subject, lesson.subject) && Objects.equals(teacher, lesson.teacher) && Objects.equals(studentGroup, lesson.studentGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, teacher, studentGroup);
    }
}
