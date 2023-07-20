package com.example.jparelations.school.repo;

import com.example.jparelations.school.entity.Lecture;

import java.util.List;

public interface LectureRepositoryCustom {
    List<Lecture> lectureByTime(String day, Integer startTime, Integer endTime);
}
