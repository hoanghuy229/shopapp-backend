package com.spring.shopappbackend.controller;

import com.spring.shopappbackend.model.Role;
import com.spring.shopappbackend.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService iRoleService;

    @GetMapping()
    public ResponseEntity<?> getAll(){
        List<Role> roles = iRoleService.getAll();
        return ResponseEntity.ok(roles);
    }
}
