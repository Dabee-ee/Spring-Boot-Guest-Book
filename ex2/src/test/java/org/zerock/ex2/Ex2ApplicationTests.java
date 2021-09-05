package org.zerock.ex2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;
import org.zerock.ex2.repository.MemoRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class Ex2ApplicationTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    // findById로 조회 테스트
    @Test
    public void testSelect() {

        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("================================");

        if (result.isPresent()){
            Memo memo = result.get();

            System.out.println(memo);
        }
    }

    // getOne 조회 테스트
    @Test
    @Transactional
    public void testSelect2() {
        
        Long mno = 100L;
        
        Memo memo = memoRepository.getOne(mno);

        System.out.println("=======================");

        System.out.println("memo = " + memo);
    }

    // 수정 작업 테스트
    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();

        System.out.println(memoRepository.save(memo));
    }

    // 삭제 작업 텍스트
    @Test
    public void testDelete() {

        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    // 페이징 처리
    @Test
    public void testPageDefault() {

        //1페이지 10개
        Pageable pageable = PageRequest.of(0,10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        // Page<엔티티 객체>를 통해 사용할 수 있는 메서드 테스트

        System.out.println("==========================");

        System.out.println("result.getTotalPages() = " + result.getTotalPages()); // 총 몇 페이지
        System.out.println("result.getTotalElements() = " + result.getTotalElements()); // 전체 개수
        System.out.println("result.getNumber() = " + result.getNumber()); // 현재 페이지 번호 0부터 시작
        System.out.println("result.getSize() = " + result.getSize()); // 페이지당 데이터 개수
        System.out.println("result.hasNext() = " + result.hasNext()); // 다음 페이지 존재 여부
        System.out.println("result.isFirst() = " + result.isFirst()); // 시작 페이지(0) 여부

        System.out.println("==========================");

        // List<엔티티 타입> 처리
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }


    // 정렬 조건 추가하기
    @Test
    public void testSort() {

        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0, 10, sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    // 쿼리 메서드 테스트
    @Test
    public void testQueryMethods() {

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

    // 쿼리 메서드 + Pageable 결합 사용
    @Test
    public void testQueryMethodsWithPageable() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    // 쿼리메서드 삭제 처리
    @Test
    @Commit
    @Transactional
    public void testDeleteQueryMethods() {

        memoRepository.deleteMemoByMnoLessThan(10L);

    }
    }
