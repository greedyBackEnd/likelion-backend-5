package com.example.jparelations.school.repo;

import com.example.jparelations.school.entity.Lecture;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.jparelations.school.entity.QLecture.lecture;

@RequiredArgsConstructor
public class LectureRepositoryCustomImpl implements LectureRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Lecture> lectureByTime(String day, Integer startTime, Integer endTime) {
        return queryFactory
                .selectFrom(lecture)
                .where(lecture.day.eq(day))
                .fetch();
    }
}
