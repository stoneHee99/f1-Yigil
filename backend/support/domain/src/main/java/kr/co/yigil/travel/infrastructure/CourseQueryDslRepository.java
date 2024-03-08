package kr.co.yigil.travel.infrastructure;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import kr.co.yigil.global.Selected;
import kr.co.yigil.travel.domain.Course;
import kr.co.yigil.travel.domain.QCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CourseQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Course> findAllByMemberIdAndIsPrivate(Long memberId, Selected visibility,
        Pageable pageable) {
        QCourse course = QCourse.course;
        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null) {
            builder.and(course.member.id.eq(memberId));
        }

        switch (visibility) {
            case Selected.PRIVATE -> builder.and(course.isPrivate.eq(true));
            case Selected.PUBLIC -> builder.and(course.isPrivate.eq(false));
            case Selected.ALL -> {
            }// 'all'일 때는 아무런 필터링을 하지 않습니다.
            default ->
                throw new IllegalArgumentException("Invalid visibility value: " + visibility);
        }

        Predicate predicate = builder.getValue();
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());

        List<Course> courses = jpaQueryFactory.select(course)
            .from(course)
            .where(predicate)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = jpaQueryFactory.selectFrom(course)
            .where(predicate)
            .fetchCount();

        return new PageImpl<>(courses, pageable, total);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {

        List<OrderSpecifier<?>> specifiers = new ArrayList<>();

        if(!isEmpty(sort)){
            for(Sort.Order order:sort){
                if(order.getProperty().equals("rate")  || order.getProperty().equals("createdAt"))
                    specifiers.add(getSortedColumn(order.getDirection().isAscending() ? Order.ASC : Order.DESC, QCourse.course, order.getProperty()));
                OrderSpecifier<?> specifier = getSortedColumn(order.getDirection().isAscending() ? Order.ASC : Order.DESC, QCourse.course, order.getProperty());
                specifiers.add(specifier);
            }
        }

        return specifiers;
    }

    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);

        return new OrderSpecifier(order, fieldPath);
    }

}