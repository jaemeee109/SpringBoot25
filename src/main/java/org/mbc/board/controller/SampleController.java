package org.mbc.board.controller;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {
    // 컨트롤러는 URL 생성과 프론트를 연결하는 부분으로 과거에 servlet-context.xml과 같은 역할을 함

    @GetMapping("/hello") // http://192.168.111.105:80/hello -> void -> hello.html
    public void hello(Model model){

        log.info("===SampleController.hello 메서드 실행===");
        model.addAttribute("msg","안녕하세요 자바의 종점입니다 !");

    }
    @GetMapping("/ex/ex1") // http://192.168.111.105:80/ex/ex1 -> /resources/templates/ex/ex1.html
    public void ex1(Model model){
        // 리스트 타입으로 데이터를 보내보자
        List<String> list = Arrays.asList("김춘식","신짱구","신짱아","홍길동","김철수");

        model.addAttribute("list",list);  // ("변수","값")

    }
    /* 클래스 안에 클래스 만들기  */
    @ToString
    class SampleDTO{
        // 이너 클래스 : 클래스 안쪽에 클래스를 선언할때 활용
        
        // 필드
        private String p1,p2,p3;
        
        // 기본 생성자
        
        // 메서드
        /* CODE -> Generate 에서 자동생성 */
        public String getP1() {
            return p1;
        }

        public String getP2() {
            return p2;
        }

        public String getP3() {
            return p3;
        }

    } // 이너클래스 SampleDTO종료
    
    @GetMapping("/ex/ex2")
    public void ex2(Model model){
        log.info("=== SampleController.ex2 메서드 실행 ===");
        // 이너클래스를 사용해서 객체를 뿌려보자 (p.426)

        List<String> strList = IntStream.range(1,10) // 1부터 10까지 정수 생성
                .mapToObj(i->"데이터"+i)
                .collect(Collectors.toList()); // 리스트에 정수(숫자) 문자열이 생성됨
        // [데이터1, ~데이터9 ]

        Map<String, String> map = new HashMap<>();
        map.put("id","kcs");
        map.put("pw","1234");

        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.p1 = "값..p1";
        sampleDTO.p2 = "값..p2";
        sampleDTO.p3 = "값..p3";
        // 최종적으로 객체 3개 완성 됨

        model.addAttribute("list",strList); // 65행에서 만들어진 객체 값
        model.addAttribute("map",map); // 70행에서 만들어진 객체 값
        model.addAttribute("dto",sampleDTO); // 74행에 만들어진 객체 값
        
        // 리턴 타입이 void 임으로 /resources/templates/ex/ex1.html 을 찾는다


    } // ex2종료
}
