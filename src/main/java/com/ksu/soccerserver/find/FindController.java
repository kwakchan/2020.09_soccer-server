package com.ksu.soccerserver.find;

import com.ksu.soccerserver.account.Account;
import com.ksu.soccerserver.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/api/find")
@RequiredArgsConstructor
@RestController
public class FindController {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/email")
    public ResponseEntity<?> findEmail(
            @RequestParam(value = "name")String name,
            @RequestParam(value = "phoneNum")String phoneNum){
        Account foundAccount = accountRepository.findByNameAndPhoneNum(name, phoneNum)
                .orElseThrow(() -> new ResponseStatusException (HttpStatus.NOT_FOUND, "가입되지 않은 사용자입니다."));
        return new ResponseEntity<>(foundAccount, HttpStatus.OK);
    }

    @GetMapping("/password")
    public ResponseEntity<?> findPW(
            @RequestParam(value = "email")String email,
            @RequestParam(value = "name")String name,
            @RequestParam(value = "phoneNum")String phoneNum){

        Account foundAccount = accountRepository.findByEmailAndNameAndPhoneNum(email, name, phoneNum)
                .orElseThrow(() -> new ResponseStatusException (HttpStatus.NOT_FOUND, "해당하는 사용자 정보를 찾을 수 없습니다."));

        return new ResponseEntity<>(foundAccount, HttpStatus.OK);
    }

    // 비밀번호 변경
    @PutMapping("/changePW")
    public ResponseEntity<?> changePW(@RequestBody Account account) {
        Account changeAccount = accountRepository.findByEmail(account.getEmail()).get();
        changeAccount.changePW(passwordEncoder.encode(account.getPassword()));

        return new ResponseEntity<> (accountRepository.save(changeAccount), HttpStatus.OK);
    }
}