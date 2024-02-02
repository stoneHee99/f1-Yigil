package kr.co.yigil.travel.presentation;

import java.net.URI;
import kr.co.yigil.auth.Auth;
import kr.co.yigil.auth.MemberOnly;
import kr.co.yigil.auth.domain.Accessor;
import kr.co.yigil.travel.application.CourseService;
import kr.co.yigil.travel.dto.request.CourseCreateRequest;
import kr.co.yigil.travel.dto.request.CourseUpdateRequest;
import kr.co.yigil.travel.dto.response.CourseCreateResponse;
import kr.co.yigil.travel.dto.response.CourseDeleteResponse;
import kr.co.yigil.travel.dto.response.CourseInfoResponse;
import kr.co.yigil.travel.dto.response.CourseListResponse;
import kr.co.yigil.travel.dto.response.CourseUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/places/{place_id}")
    public ResponseEntity<CourseListResponse> getCourseList(
            @PathVariable("place_id") Long placeId
    ) {
        CourseListResponse courseListResponse = courseService.getCourseList(placeId);
        return ResponseEntity.ok().body(courseListResponse);
    }

    @PostMapping
    @MemberOnly
    public ResponseEntity<CourseCreateResponse> createCourse(
            @RequestBody CourseCreateRequest courseCreateRequest,
            @Auth final Accessor accessor
    ) {
        CourseCreateResponse courseCreateResponse = courseService.createCourse(
                accessor.getMemberId(), courseCreateRequest);
        URI uri = URI.create("/api/v1/courses/" + courseCreateResponse.getCourseId());
        return ResponseEntity.created(uri).body(courseCreateResponse);
    }

    @GetMapping("/{course_id}")
    public ResponseEntity<CourseInfoResponse> getCourseInfo(
            @PathVariable("course_id") Long courseId
    ) {
        CourseInfoResponse courseInfoResponse = courseService.getCourseInfo(courseId);
        return ResponseEntity.ok().body(courseInfoResponse);
    }

    @PutMapping("/{course_id}")
    @MemberOnly
    public ResponseEntity<CourseUpdateResponse> updateCourse(
            @PathVariable("course_id") Long courseId,
            @RequestBody CourseUpdateRequest courseUpdateRequest,
            @Auth final Accessor accessor
    ) {
        CourseUpdateResponse courseUpdateResponse = courseService.updateCourse(courseId,
                accessor.getMemberId(), courseUpdateRequest);
        URI uri = URI.create("/api/v1/courses/" + courseId);
        return ResponseEntity.ok()
                .location(uri)
                .body(courseUpdateResponse);
    }

    @DeleteMapping("/{course_id}")
    @MemberOnly
    public ResponseEntity<CourseDeleteResponse> deleteCourse(
            @PathVariable("course_id") Long courseId,
            @Auth final Accessor accessor
    ) {
        CourseDeleteResponse courseDeleteResponse = courseService.deleteCourse(courseId,
                accessor.getMemberId());
        return ResponseEntity.ok().body(courseDeleteResponse);
    }

}
