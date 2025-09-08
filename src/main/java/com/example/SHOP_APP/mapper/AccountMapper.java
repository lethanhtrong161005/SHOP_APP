package com.example.SHOP_APP.mapper;

import com.example.SHOP_APP.dto.AccountDTO;
import com.example.SHOP_APP.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {

    AccountDTO toAccountDTO(Account account);
}
