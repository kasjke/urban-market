package com.example.urbanmarket.security;

import com.example.urbanmarket.entity.cart.CartServiceImpl;
import com.example.urbanmarket.entity.order.OrderEntity;
import com.example.urbanmarket.entity.user.UserEntity;
import com.example.urbanmarket.entity.user.UserServiceImpl;
import com.example.urbanmarket.entity.user.review.ReviewEntity;
import com.example.urbanmarket.enums.Role;
import com.example.urbanmarket.exception.exceptions.general.CustomNotFoundException;
import com.example.urbanmarket.exception.exceptions.user.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccessValidator {
    private final UserServiceImpl userService;
    private final CartServiceImpl cartService;

    public void hasPermission(String objectName, String id) throws UnauthorizedAccessException {
        UserEntity user = getUser();

        try {
            isAdmin(user);
            return;
        }catch (UnauthorizedAccessException ignored){

        }

        switch (objectName){
            case "user": if (user.getId().equals(id)){
                break;
            }else {
                throw new UnauthorizedAccessException();
            }
            case "cart": if (cartService.findById(id).getUser().getId().equals(user.getId())){
                break;
            }else {
                throw new UnauthorizedAccessException();
            }
            case "order": for (OrderEntity elem:user.getOrderHistory()){
                if (elem.getId().equals(id)){
                    break;
                }
            }
                throw new UnauthorizedAccessException();

            case "review": for (ReviewEntity elem:user.getReviews()){
                if (elem.getId().equals(id)){
                    break;
                }
            }
                throw new UnauthorizedAccessException();

            default:throw new CustomNotFoundException("Object for AccessValidator");
        }
    }


    public void isAdmin() throws UnauthorizedAccessException {
        isAdmin(getUser());
    }

    private void isAdmin(UserEntity user) throws UnauthorizedAccessException {
        if (!user.getRole().equals(Role.ADMIN)){
            throw new UnauthorizedAccessException();
        }
    }

    private UserEntity getUser(){
        return userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}