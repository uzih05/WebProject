package com.example.myweb.Controller;

import com.example.myweb.model.User;
import com.example.myweb.repository.UserRepository;
import com.example.myweb.Service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // 회원가입 페이지로 이동
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password,
                         Model model) {
        // 사용자 이름 중복 확인
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "이미 사용 중인 사용자 이름입니다.");
            return "signup";
        }

        // 사용자 생성 및 저장
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
        return "redirect:/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            session.setAttribute("user", userOpt.get());
            return "redirect:/list";
        } else {
            model.addAttribute("error", "잘못된 사용자 이름 또는 비밀번호입니다.");
            return "login";
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // 비밀번호 재설정 이메일 전송 요청 처리
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            userRepository.save(user);

            // 비밀번호 재설정 링크 생성
            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            try {
                emailService.sendEmail(email, "비밀번호 재설정", "<p>비밀번호 재설정을 위해 <a href=\"" + resetLink + "\">여기</a>를 클릭하세요.</p>");
            } catch (MessagingException | jakarta.mail.MessagingException e) {
                model.addAttribute("error", "이메일 전송 중 오류가 발생했습니다.");
                return "forgot-password";
            }
            model.addAttribute("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        } else {
            model.addAttribute("error", "이메일 주소를 찾을 수 없습니다.");
        }
        return "forgot-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password,
                                Model model) {
        Optional<User> userOpt = userRepository.findByResetToken(token); // 토큰으로 사용자 찾기
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(password)); // 새 비밀번호로 업데이트
            user.setResetToken(null); // 토큰 초기화
            userRepository.save(user);

            model.addAttribute("message", "비밀번호가 성공적으로 재설정되었습니다.");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        } else {
            model.addAttribute("error", "유효하지 않은 토큰입니다.");
            return "reset-password"; // 재설정 페이지로 돌아가기
        }
    }
}