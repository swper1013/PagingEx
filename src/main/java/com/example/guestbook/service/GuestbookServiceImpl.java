package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.Guestbook;
import com.example.guestbook.entity.QGuestbook;
import com.example.guestbook.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동주입
public class GuestbookServiceImpl implements GuestbookService{

    private  final GuestbookRepository guestbookRepository;

    @Override
    public Long register(GuestbookDTO dto) {
        log.info("DTO===============================");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        guestbookRepository.save(entity);


        return entity.getGno();
    }//글등록(저장)

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = guestbookRepository.findById(gno);
        
        return  result.isPresent()? entityToDTO(result.get()): null;
    }

    @Override
    public void remove(Long gno) {
        guestbookRepository.deleteById(gno);

    }

    @Override
    public void modify(GuestbookDTO dto) {
        Optional<Guestbook> result = guestbookRepository.findById(dto.getGno());

        if(result.isPresent()){
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            entity.changeWriter(dto.getWriter());

            guestbookRepository.save(entity);
        }else {
            log.info("무언가 문제가 있어서 정상 작동하지 않았습니다.",dto.getGno());
        }

    }

    //Imple 클래스 내부에 인터페이스 정의된 read의 기능 구현 findbyid를 통해 엔티티객체로 가져왔다? 그럼 바아로 DTO변환
    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        //Page<Guestbook> result = guestbookRepository.findAll(pageable);
        BooleanBuilder booleanBuilder=getSearch(requestDTO);//검색 조건 처리
        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder,pageable);
        //querydsl사용
        Function<Guestbook,GuestbookDTO>fn=(entity -> entityToDTO(entity));

        return new PageResultDTO<>(result,fn);
    }
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        //querydsl처리
        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qGuestbook.gno.gt(0L);
        //gno>0인 조건만 생성
        booleanBuilder.and(expression);
        if(type==null||type.trim().length()==0){
            //검색 조건이 ㅇ벗을때 
            return booleanBuilder;
        }
        //검색조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }
        //검색 조건 통합
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
        
    }


}
