package com.restApi.banking.service.impl;

import com.restApi.banking.dto.AccountDto;
import com.restApi.banking.entity.Account;
import com.restApi.banking.mapper.AccountMapper;
import com.restApi.banking.repository.AccountRepository;
import com.restApi.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl  implements AccountService {

    private AccountRepository accountRepository;

    //dependency injection
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto){
        Account account = AccountMapper.mapToAccount(accountDto);
       Account savedAccount =  accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id){
      Account account = accountRepository
              .findById(id)
              .orElseThrow(() -> new RuntimeException("Account Does not exist!!"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposite(Long id, double amount) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account Does not exist!!"));
       double total =  account.getBalance() + amount;
       account.setBalance(total);
      Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account Does not exist!!"));
        if(account.getBalance() < amount){
            throw new RuntimeException("Insufficient Amount !!");
        }
        double total =  account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return  accounts.stream().map((account)-> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account Does not exist!!"));
        accountRepository.deleteById(id);
    }


}
