package com.example.SHOP_APP.controllers;

import com.example.SHOP_APP.dto.AccountDTO;
import com.example.SHOP_APP.entities.Account;
import com.example.SHOP_APP.mapper.AccountMapper;
import com.example.SHOP_APP.response.BaseResponse;
import com.example.SHOP_APP.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private AccountMapper accountMapper;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentAccount() {
        BaseResponse<AccountDTO> response = new BaseResponse<>();
            Account ac = authUtil.getCurrentUser();
            if(ac!=null){
                AccountDTO accountDTO = accountMapper.toAccountDTO(ac);
                response.setData(accountDTO);
                response.setMessage("Get User Success");
                response.setSuccess(true);
                response.setStatus(200);
            }else{
                response.setMessage("Get User Failed");
                response.setSuccess(false);
                response.setStatus(400);
            }
            return ResponseEntity.status(response.getStatus()).body(response);
    }

}
