package org.ponder;

import lombok.extern.slf4j.Slf4j;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicType;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.LocalSearchType;
import org.optaplanner.core.config.solver.SolverConfig;
import org.ponder.domain.Lesson;
import org.ponder.domain.TimeTable;
import org.ponder.domain.Timeslot;
import org.ponder.dto.Course;
import org.ponder.dto.StudentGroup;
import org.ponder.dto.Teacher;
import org.ponder.solver.TimeTableConstraintProvider;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ${USER}
 * @since ${DATE} ${TIME}
 */
@Slf4j
public class Main {

    static List<Teacher> teacherList = new ArrayList<>();
    static List<StudentGroup> studentGroupList = new ArrayList<>();

    static {
        teacherList.add(new Teacher("铃铃", List.of("化学")));
        teacherList.add(new Teacher("代驾", List.of("历史")));
        teacherList.add(new Teacher("萌萌", List.of("语文")));
        teacherList.add(new Teacher("利群", List.of("历史")));
        teacherList.add(new Teacher("余响", List.of("地理")));
        teacherList.add(new Teacher("新新", List.of("地理")));
        teacherList.add(new Teacher("妹妹", List.of("数学")));
        teacherList.add(new Teacher("叶真", List.of("英语")));
        teacherList.add(new Teacher("家家", List.of("数学")));
        teacherList.add(new Teacher("周钱", List.of("数学")));
        teacherList.add(new Teacher("无敌", List.of("地理")));
        teacherList.add(new Teacher("礼炮", List.of("英语")));
        teacherList.add(new Teacher("大奔", List.of("化学")));
        teacherList.add(new Teacher("五六", List.of("生物")));
        teacherList.add(new Teacher("小朋", List.of("生物")));
        teacherList.add(new Teacher("小李", List.of("语文")));
        teacherList.add(new Teacher("小王", List.of("历史")));
        teacherList.add(new Teacher("李四", List.of("物理")));
        teacherList.add(new Teacher("王五", List.of("物理", "信息技术")));
        teacherList.add(new Teacher("赵六", List.of("数学")));
        teacherList.add(new Teacher("书书", List.of("物理")));
        teacherList.add(new Teacher("三峰", List.of("地理")));
        teacherList.add(new Teacher("无忌", List.of("英语")));
        teacherList.add(new Teacher("师太", List.of("地理")));
        teacherList.add(new Teacher("大侠", List.of("物理")));
        teacherList.add(new Teacher("小凡", List.of("美术")));
        teacherList.add(new Teacher("瓶儿", List.of("数学")));
        teacherList.add(new Teacher("碧瑶", List.of("英语")));
        teacherList.add(new Teacher("鬼王", List.of("生物")));
        teacherList.add(new Teacher("幽姨", List.of("日语")));
        teacherList.add(new Teacher("张宝", List.of("音乐")));
        teacherList.add(new Teacher("悟空", List.of("生物")));
        teacherList.add(new Teacher("八戒", List.of("政治")));
        teacherList.add(new Teacher("沙僧", List.of("语文")));
        teacherList.add(new Teacher("唐僧", List.of("物理")));
        teacherList.add(new Teacher("观音", List.of("历史")));
        teacherList.add(new Teacher("玉帝", List.of("语文")));
        teacherList.add(new Teacher("师兄", List.of("政治")));
        teacherList.add(new Teacher("师妹", List.of("数学")));
        teacherList.add(new Teacher("王林", List.of("英语")));
        teacherList.add(new Teacher("化元", List.of("语文")));
        teacherList.add(new Teacher("藤一", List.of("化学")));
        teacherList.add(new Teacher("藤二", List.of("历史")));
        teacherList.add(new Teacher("藤三", List.of("历史")));
        teacherList.add(new Teacher("藤四", List.of("政治")));
        teacherList.add(new Teacher("藤五", List.of("政治")));
        teacherList.add(new Teacher("藤六", List.of("语文")));
        teacherList.add(new Teacher("化门", List.of("语文")));
        teacherList.add(new Teacher("阳门", List.of("语文")));
        teacherList.add(new Teacher("香谷", List.of("数学")));
        teacherList.add(new Teacher("天音", List.of("政治")));
        teacherList.add(new Teacher("成功", List.of("体育")));
        teacherList.add(new Teacher("郭襄", List.of("政治")));
        teacherList.add(new Teacher("阎王", List.of("化学")));
        teacherList.add(new Teacher("牛头", List.of("地理")));
        teacherList.add(new Teacher("马面", List.of("生物")));
        teacherList.add(new Teacher("尊宝", List.of("语文")));
        teacherList.add(new Teacher("陈真", List.of("语文")));
        teacherList.add(new Teacher("世民", List.of("英语")));
        teacherList.add(new Teacher("杨辉", List.of("数学")));
        teacherList.add(new Teacher("天罡", List.of("数学")));
        teacherList.add(new Teacher("星云", List.of("英语")));
        teacherList.add(new Teacher("茂贞", List.of("数学")));

        Map<String, Teacher> teacherMap = teacherList.stream().collect(Collectors.toMap(Teacher::getTeacherName, Function.identity()));

        studentGroupList.add(new StudentGroup("高三1班", List.of(
                new Course("语文", 8, teacherMap.get("玉帝")),
                new Course("数学", 9, teacherMap.get("杨辉")),
                new Course("英语", 8, teacherMap.get("碧瑶")),
                new Course("物理", 9, teacherMap.get("李四")),
                new Course("化学", 8, teacherMap.get("阎王")),
                new Course("生物", 7, teacherMap.get("马面")),
                new Course("体育", 1, teacherMap.get("成功"))
        )));
        studentGroupList.add(new StudentGroup("高三2班", List.of(
                new Course("语文", 8, teacherMap.get("玉帝")),
                new Course("数学", 9, teacherMap.get("杨辉")),
                new Course("英语", 8, teacherMap.get("星云")),
                new Course("政治", 8, teacherMap.get("天音")),
                new Course("历史", 8, teacherMap.get("观音")),
                new Course("地理", 8, teacherMap.get("新新")),
                new Course("体育", 1, teacherMap.get("成功"))
        )));
        studentGroupList.add(new StudentGroup("高三3班", List.of(
                new Course("语文", 8, teacherMap.get("尊宝")),
                new Course("数学", 9, teacherMap.get("家家")),
                new Course("英语", 8, teacherMap.get("星云")),
                new Course("政治", 8, teacherMap.get("藤四")),
                new Course("历史", 8, teacherMap.get("利群")),
                new Course("地理", 8, teacherMap.get("新新")),
                new Course("体育", 1, teacherMap.get("成功"))
        )));
        studentGroupList.add(new StudentGroup("高三4班", List.of(
                new Course("语文", 8, teacherMap.get("化门")),
                new Course("数学", 9, teacherMap.get("天罡")),
                new Course("英语", 8, teacherMap.get("无忌")),
                new Course("政治", 8, teacherMap.get("藤四")),
                new Course("历史", 8, teacherMap.get("小王")),
                new Course("地理", 8, teacherMap.get("三峰")),
                new Course("体育", 1, teacherMap.get("成功"))
        )));
        studentGroupList.add(new StudentGroup("高三5班", List.of(
                new Course("语文", 8, teacherMap.get("化门")),
                new Course("数学", 9, teacherMap.get("茂贞")),
                new Course("英语", 8, teacherMap.get("无忌")),
                new Course("政治", 8, teacherMap.get("师兄")),
                new Course("历史", 8, teacherMap.get("利群")),
                new Course("地理", 8, teacherMap.get("三峰")),
                new Course("体育", 1, teacherMap.get("成功"))
        )));

    }

    public static void main(String[] args) {
        SolverConfig solverConfig = new SolverConfig();

        // 构造启发式解决方案
        ConstructionHeuristicPhaseConfig constructionHeuristicPhaseConfig = new ConstructionHeuristicPhaseConfig();
        constructionHeuristicPhaseConfig.setConstructionHeuristicType(ConstructionHeuristicType.FIRST_FIT);

        // 本地搜索解决方案
        LocalSearchPhaseConfig localSearchPhaseConfig = new LocalSearchPhaseConfig();
        // 简化配置
//        localSearchPhaseConfig.setLocalSearchType(LocalSearchType.HILL_CLIMBING);
        localSearchPhaseConfig.setLocalSearchType(LocalSearchType.TABU_SEARCH);
//        localSearchPhaseConfig.setLocalSearchType(LocalSearchType.LATE_ACCEPTANCE);
//        localSearchPhaseConfig.setLocalSearchType(LocalSearchType.GREAT_DELUGE);
//        localSearchPhaseConfig.setLocalSearchType(LocalSearchType.VARIABLE_NEIGHBORHOOD_DESCENT);

        // 高级配置
//        localSearchPhaseConfig.setAcceptorConfig(new LocalSearchAcceptorConfig()
//                .withEntityTabuRatio(0.1).withLateAcceptanceSize(250));
//        localSearchPhaseConfig.setForagerConfig(new LocalSearchForagerConfig().withAcceptedCountLimit(4));
//
//        localSearchPhaseConfig.setMoveSelectorConfig(new UnionMoveSelectorConfig().withMoveSelectors(
//                new ChangeMoveSelectorConfig()
//                        .withValueSelectorConfig(new ValueSelectorConfig("timeslot"))));

        solverConfig.withSolutionClass(TimeTable.class)
                .withEntityClasses(Lesson.class)
                .withConstraintProviderClass(TimeTableConstraintProvider.class)
//                .withTerminationConfig(new TerminationConfig().withUnimprovedSecondsSpentLimit(5L))
                .withTerminationSpentLimit(Duration.ofSeconds(10)) // (0hard/-14soft)
//                .withTerminationSpentLimit(Duration.ofSeconds(30))   // (0hard/-14soft)
//                .withTerminationSpentLimit(Duration.ofSeconds(60))   // (0hard/-12soft)
//                .withTerminationSpentLimit(Duration.ofSeconds(120))   // (0hard/-12soft)
                // 多线程求解
//                .withMoveThreadCount("4")
//                .withMoveThreadBufferSize(10)
//                .withTerminationConfig(new TerminationConfig().withScoreCalculationCountLimit(10000L))
                .withPhaseList(List.of(constructionHeuristicPhaseConfig, localSearchPhaseConfig));
//                .withPhaseList(List.of(constructionHeuristicPhaseConfig));


        SolverFactory<TimeTable> solverFactory = SolverFactory.create(solverConfig);

        // Load the problem
        TimeTable problem = generateDemoData();

        // Solve the problem
        Solver<TimeTable> solver = solverFactory.buildSolver();
        TimeTable solution = solver.solve(problem);

        List<Lesson> lessonList = solution.getLessonList();
        Map<String, List<Lesson>> groupByStudent = lessonList.stream().collect(Collectors.groupingBy(Lesson::getStudentGroup));


        for (Map.Entry<String, List<Lesson>> entry : groupByStudent.entrySet()) {
            Lesson[][] lessons = new Lesson[9][6];
            System.out.println("------------------------" + entry.getKey() + "------------------------");
            System.out.println();

            for (Lesson lesson : entry.getValue()) {
                Timeslot timeslot = lesson.getTimeslot();
                int row = timeslot.getDayOfWeek().getValue();

                Integer flights = timeslot.getFlights();
                lessons[flights - 1][row - 1] = lesson;
            }

            printLessonsArray(lessons);

        }

//        System.out.println(solution);
    }

    private static void printLessonsArray(Lesson[][] lessons) {
        int rowLength = lessons.length;
        int columnLength = lessons[0].length;

        for (int i = -1; i < rowLength; i++) {
            if (i == -1) {
                System.out.printf("%-15s", "  节/星期  ");
                System.out.printf("%-15s", "   周一   ");
                System.out.printf("%-15s", "   周二   ");
                System.out.printf("%-15s", "   周三   ");
                System.out.printf("%-15s", "   周四   ");
                System.out.printf("%-15s", "   周五   ");
                System.out.printf("%-15s", "   周六   ");
                System.out.println();
                continue;
            }


            for (int j = 0; j < columnLength; j++) {
                Lesson lesson = lessons[i][j];
                if (j == 0) {
                    System.out.printf("%-15s", "第" + (i + 1) + "节课");
                }

                if (lesson != null) {
                    System.out.printf("%-15s", lesson.getSubject() + ":" + lesson.getTeacher());
                } else {
                    System.out.printf("%-15s", "自习:没人");
                }
            }
            System.out.println();
        }

    }

    public static TimeTable generateDemoData() {
        // 只考虑每周每天一门课最多上两节
        List<Timeslot> timeslotList = new ArrayList<>();

        List<DayOfWeek> weekList = List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
        for (DayOfWeek dayOfWeek : weekList) {
            for (int i = 1; i <= 9; i++) {
                timeslotList.add(new Timeslot(dayOfWeek, i));
            }
        }

        List<Lesson> lessonList = new ArrayList<>();
        long id = 0;
        for (StudentGroup studentGroup : studentGroupList) {
            // 班级名称
            String studentGroupName = studentGroup.getStudentGroupName();
            // 课程
            List<Course> courseList = studentGroup.getCourseList();

            for (Course course : courseList) {
                Integer num = course.getNum();

                List<Lesson> tempLessonList = new ArrayList<>();

                for (int i = 0; i < num; i++) {
                    tempLessonList.add(new Lesson(id++, course.getCourseName(), course.getTeacher().getTeacherName(), studentGroupName));
                }

                lessonList.addAll(tempLessonList);
            }
        }


        return new TimeTable(timeslotList, lessonList);
    }
}