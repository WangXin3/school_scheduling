package org.ponder.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

/**
 * @author wangxin
 * @since 2024/8/27 20:35
 */
@PlanningSolution
@NoArgsConstructor
@Data
public class TimeTable {

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Timeslot> timeslotList;

    @PlanningEntityCollectionProperty
    private List<Lesson> lessonList;

    @PlanningScore
    private HardSoftScore score;

    public TimeTable(List<Timeslot> timeslotList, List<Lesson> lessonList) {
        this.timeslotList = timeslotList;
        this.lessonList = lessonList;
    }
}
