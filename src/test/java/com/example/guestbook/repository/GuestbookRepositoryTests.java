package com.example.guestbook.repository;

import com.example.guestbook.entity.Guestbook;
import com.example.guestbook.entity.QGuestbook;
import com.example.guestbook.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {
    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i->{
            Guestbook guestbook = Guestbook.builder()
                    .title("title!!!!!!!!!!!!!!!!1"+i)
                    .content("Content!!!!!!!!!!!!!!"+i)
                    .writer("user"+(i%10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));

        });
    }
    @Test
    public void updateTest(){
        Optional<Guestbook> result = guestbookRepository.findById(10L);

        if(result.isPresent()){
            Guestbook guestbook = result.get();

            guestbook.changeTitle("바뀜!!!!!!!!!!!!!!!!!!!!!!!!!");
            guestbook.changeContent("컨텐츠도바뀜!!!!!!!!!!!!!!!!!!!!!!!!!");

            guestbookRepository.save(guestbook);
        }
    }
    @Test
    public void testQuery1(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;//1
        String keyword ="1";

        BooleanBuilder builder = new BooleanBuilder();//2

        BooleanExpression expression = qGuestbook.title.contains(keyword);//3

        builder.and(expression);//4

        Page<Guestbook> result = guestbookRepository.findAll(builder,pageable);//5

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }
    @Test
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;//1
        String keyword ="1";

        BooleanBuilder builder = new BooleanBuilder();//2

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);//3
        BooleanExpression exContent = qGuestbook.content.contains(keyword);//3

        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);
        builder.and(qGuestbook.gno.gt(0L));//3
        Page<Guestbook> result = guestbookRepository.findAll(builder,pageable);//5

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });


    }
}
