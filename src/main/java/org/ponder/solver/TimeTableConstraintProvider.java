package org.ponder.solver;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.*;
import org.ponder.domain.Lesson;
import org.ponder.domain.Timeslot;

import java.time.DayOfWeek;
import java.util.List;
import java.util.function.Function;


/**
 * @author wangxin
 * @since 2024/8/27 20:38
 */
public class TimeTableConstraintProvider implements ConstraintProvider {


    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // 硬约束：一个老师在一天中同一时间段只能授一门课
                teacherConflict(constraintFactory),
                // 硬约束：一个班级在一天中同一时间段只能上一门课
                studentGroupConflict(constraintFactory),
                // 硬约束：一门课一天最多两节
                groupLess2(constraintFactory),
                // 硬约束：每件课都必须安排上课时间
                lessonMustHaveTimeslot(constraintFactory),
                // 硬约束：连堂课
                twoLessonMustContinuous(constraintFactory),
                // 硬约束：第一节、第二节和第六节必须排课
                oneTwoSexMustHaveLesson(constraintFactory),
                // 硬约束：体育课不能排上午前 2 节 而且只能排在周 456
                physicalEducationConflict(constraintFactory),

                // 软约束：135第一节尽可能上语文，246第一节尽可能上英语
                chineseAndEnglishSoftConflict(constraintFactory),
        };
    }

    Constraint chineseAndEnglishSoftConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .filter(lesson -> {
                    Timeslot timeslot = lesson.getTimeslot();
                    DayOfWeek dayOfWeek = timeslot.getDayOfWeek();
                    Integer flights = timeslot.getFlights();
                    String subject = lesson.getSubject();
                    List<DayOfWeek> week135 = List.of(DayOfWeek.of(1), DayOfWeek.of(3), DayOfWeek.of(5));
                    List<DayOfWeek> week246 = List.of(DayOfWeek.of(2), DayOfWeek.of(4), DayOfWeek.of(6));

                    if (subject.equals("语文") && week135.contains(dayOfWeek) && flights == 1) {
                        return true;
                    }
                    if (subject.equals("英语") && week246.contains(dayOfWeek) && flights == 1) {
                        return true;
                    }

                    return false;
                })
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("135第一节尽可能上语文，246第一节尽可能上英语");
    }

    Constraint physicalEducationConflict(ConstraintFactory constraintFactory) {

        return constraintFactory.forEach(Lesson.class)
                .filter(lesson -> {
                    if (lesson.getSubject().equals("体育")) {
                        Timeslot timeslot = lesson.getTimeslot();
                        Integer flights = timeslot.getFlights();
                        DayOfWeek dayOfWeek = timeslot.getDayOfWeek();
                        if (flights == 1 || flights == 2) {
                            return true;
                        }

                        List<DayOfWeek> week123 = List.of(DayOfWeek.of(1), DayOfWeek.of(2), DayOfWeek.of(3));
                        return week123.contains(dayOfWeek);
                    }
                    return false;
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("体育课不能排上午前 2 节 而且只能排在周 456");
    }

    Constraint oneTwoSexMustHaveLesson(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .groupBy(Lesson::getStudentGroup, lesson -> lesson.getTimeslot().getDayOfWeek(), Function.identity())
                .filter((s, dayOfWeek, lesson) -> {
                    Integer flights = lesson.getTimeslot().getFlights();
                    return flights == 1 || flights == 2 || flights == 6;
                })
                .reward(HardSoftScore.ONE_HARD)
                .asConstraint("第一节、第二节和第六节必须排课");
    }

    Constraint twoLessonMustContinuous(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroup),
                        Joiners.equal(lesson -> lesson.getTimeslot().getDayOfWeek()),
                        Joiners.equal(Lesson::getSubject))
                .filter((lesson1, lesson2) -> {
                    if (lesson1 != lesson2) {
                        Timeslot timeslot1 = lesson1.getTimeslot();
                        Timeslot timeslot2 = lesson2.getTimeslot();
                        // 如果两节课不连续，则扣分
                        if (timeslot1.getFlights() + 1 != timeslot2.getFlights() && timeslot1.getFlights() - 1 != timeslot2.getFlights()) {
                            return true;
                        }

                        // 如果两节课正好连在五六节，则扣分
                        if ((timeslot1.getFlights() == 5 && timeslot2.getFlights() == 6) || (timeslot1.getFlights() == 6 && timeslot2.getFlights() == 5)) {
                            return true;
                        }
                    }

                    return false;
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("一天中同样的课出现两次的话，则必须连续，且不能在五六节，因为第五节在上午，第六节在下午");
    }

    Constraint lessonMustHaveTimeslot(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .filter(lesson -> lesson.getTimeslot() == null)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("课必须排完");
    }

    Constraint groupLess2(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .groupBy(Lesson::getStudentGroup, lesson -> lesson.getTimeslot().getDayOfWeek(), Lesson::getSubject, ConstraintCollectors.count())
                .filter((s, dayOfWeek, s2, integer) -> integer > 2)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("一天最多两节同一种课程");
    }

    Constraint teacherConflict(ConstraintFactory constraintFactory) {
        // A teacher can teach at most one lesson at the same time.
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class, Joiners.equal(Lesson::getTeacher),
                        Joiners.equal(lesson -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    if (lesson1 != lesson2) {
                        Timeslot timeslot1 = lesson1.getTimeslot();
                        Timeslot timeslot2 = lesson2.getTimeslot();
                        // 两种课程节数相同 扣分
                        return timeslot1.getFlights().equals(timeslot2.getFlights());
                    }

                    return false;
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher conflict");
    }

    Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class, Joiners.equal(Lesson::getStudentGroup),
                        Joiners.equal(lesson -> lesson.getTimeslot() != null),
                        Joiners.equal(lesson -> lesson.getTimeslot().getDayOfWeek().getValue()))
                .filter((lesson1, lesson2) -> {
                    if (lesson1 != lesson2) {
                        Timeslot timeslot1 = lesson1.getTimeslot();
                        Timeslot timeslot2 = lesson2.getTimeslot();
                        // 两种课程节数相同 扣分
                        return timeslot1.getFlights().equals(timeslot2.getFlights());
                    }
                    return false;
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student group conflict");
    }


}
