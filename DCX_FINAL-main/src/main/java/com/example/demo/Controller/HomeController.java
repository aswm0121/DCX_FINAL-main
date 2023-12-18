package com.example.demo.Controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Entity.Member;
import com.example.demo.Entity.Storage;
import com.example.demo.Mapper.MemberMapper;
import com.example.demo.java.GoogleEmailService;
import com.example.demo.java.NaverEmailService;

import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController{

    @Autowired
    MemberMapper mapper;


   

    @GetMapping("/")
    public String intro() {

    return "login";
        
    }

    @PostMapping("/main")
    public String login(Member member, HttpSession session) {
        
        Member result = mapper.login(member);
    
        if(result == null) { // Uesr에 입력한 회원 정보가 없어 로그인에 실패
            System.out.println("로그인 실패");
            return "redirect:/"; // 로그인 페이지로 다시 이동
        } else {
            session.setAttribute("loginMember", result); // 세션에 로그인한 계정의 정보를 저장, 해당 정보는 session.invalidate()나 브라우저를 종료하기 전까지 유효함
            Member loginMember = (Member)session.getAttribute("loginMember");
            String memberId = loginMember.getId(); // 로그인한 사용자의 Id를 memberId에 할당
            System.out.println(memberId);
            System.out.println(loginMember.getEmail().substring(loginMember.getEmail().length()-9));

            String DATA_DIRECTORY = "C:/Users/smhrd/Desktop/deeplearning/video_save/";
            File dir = new File(DATA_DIRECTORY);

            String[] filenames = dir.list();
            String[] filename2 = new String[filenames.length];

            // Copying elements from filenames to filename2
            for (int i = 0; i < filenames.length; i++) {
                filename2[i] = DATA_DIRECTORY + filenames[i];
                // System.out.println(filename2[i]);
        }

        session.setAttribute("video_storage", filename2);

        for (int i = 0; i < filenames.length; i++) {
                filename2[i] = DATA_DIRECTORY + filenames[i];
                int sucornot = mapper.savevid(memberId, filename2[i].substring(59, 69), filename2[i].substring(59, 69), filename2[i]);
                // System.out.println(filename2[i]);
                if (sucornot > 0) System.out.println("데이터베이스 업데이트 성공!");

        }
            
            List<Storage> result_storage = mapper.videoList(memberId);
            if(result_storage == null) { // Uesr에 입력한 회원 정보가 없어 로그인에 실패
            System.out.println("데이터 베이스 불러오기 실패");
            }
            System.out.println(result_storage);
            session.setAttribute("result_storage", result_storage);
            return "main";
        }

    }

    @PostMapping("/join")
    public String join(@ModelAttribute Member member) {

        try {
            mapper.join(member); // 입력한 회원 정보를 t_user 테이블에 삽입
            
            System.out.println("회원가입 성공");
            System.out.println(member.getId());
            return "redirect:/";
        } catch(DataIntegrityViolationException e) { // MySQL WorkBench에서는 PrimaryKey가 중복되어 user 테이블에 데이터를 삽입할 수 없으면 에러가 떠서 예외처리를 하였음
            System.out.println("회원가입 실패");
            return "redirect:/"; // 회원가입에 실패하면 다시 회원가입 페이지로 이동
        }
        
    }

    @PostMapping("/sendemail")
    public String sendEmail(HttpSession session) {
        
        Member member = (Member)session.getAttribute("loginMember");
        
        String to = member.getEmail();
        String subject = member.getId();
        String text = member.getPw();
        
        if(to.substring(to.length()-9).equals("naver.com")) {
            
            NaverEmailService.sendEmail(to, subject, text);
            
            return "main";
            
        } else if(to.substring(to.length()-9).equals("gmail.com")) {
            
            GoogleEmailService.sendEmail(to, subject, text);
            
            return "main";
        }
        
        return null;
        
    }

    @GetMapping(value="/storage")
    public String storage(Model model, HttpSession session) {  // Model model, 
        



        
        
        return "storage";
    }

    @GetMapping(value="/loading")
    public String loading(Model model) {
        
        return "loading";
    }

    @GetMapping(value="/mypage")
    public String mypage(Model model) {
        
        return "mypage";
    }
    
    @GetMapping(value="/videosender")
    public String video(Model model) {
        
        return "videosender";
    }
    
    @GetMapping(value="/videosender4")
    public String video4(Model model) {
        
        return "videosender4";
    }

    @GetMapping(value="/videotest")
    public String python(Model model) {
        
        return "videotest";
    }
    
    @GetMapping(value="/videotest2")
    public String python2(Model model) {
        
        return "videotest2";
    }

    @GetMapping(value="/videotest3")
    public String python3(Model model) {
        
        return "videotest3";
    }
    
    
}
