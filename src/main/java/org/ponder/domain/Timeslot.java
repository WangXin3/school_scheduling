package org.ponder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

/**
 * @author wangxin
 * @since 2024/8/27 20:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timeslot {

    /**
     * 周几
     */
    private DayOfWeek dayOfWeek;

    /**
     * 当天的第几节课 1-9
     */
    private Integer flights;




}
