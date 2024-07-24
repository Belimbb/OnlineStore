package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    private final UserServiceImpl userService;

    private final FigureServiceImpl figureService;

    private static final String OBJECT_NAME = "Cart";

    @Override
    public List<CartDto> getAll() {
        List<CartEntity> cartList = cartRepository.findAll();
        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        return cartMapper.toDtoList(cartList);
    }

    @Override
    public CartDto getById(String id) {
        CartEntity cart = findById(id);
        log.info("{}: " + OBJECT_NAME + " retrieved from db by id {}", LogEnum.SERVICE, id);
        return cartMapper.toDto(cart);
    }

    @Override
    public CartDto create(CartDto cartDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userService.findByEmail(email);
        List<FigureEntity> figureList = cartDto.figures()
                .stream()
                .map(figure -> figureService.findById(figure.getId()))
                .toList();

        if (cartRepository.existsByUser(currentUser)) {
            CartEntity cart = cartRepository.findByUser(currentUser);
            return cartMapper.toDto(addFigures(cart.getId(), figureList));
        }

        int totalPrice = figureList.stream().mapToInt(FigureEntity::getCurrentPrice).sum();
        CartEntity newCart = new CartEntity(currentUser, totalPrice, figureList);
        CartEntity savedCart = cartRepository.save(newCart);
        log.info("{}: " + OBJECT_NAME + " (Id: {}) was created", LogEnum.SERVICE, savedCart.getId());
        return cartMapper.toDto(savedCart);
    }

    private CartEntity addFigures(String cartId, List<FigureEntity> figureList) {
        if (cartRepository.existsById(cartId)) {
            CartEntity cart = findById(cartId);
            List<FigureEntity> figures = cart.getFigures();

            figures.addAll(figureList);
            cart.setFigures(figures
                    .stream()
                    .distinct()
                    .toList());
            cart.setPrice(figures.stream().mapToInt(FigureEntity::getCurrentPrice).sum());

            CartEntity savedCart = cartRepository.save(cart);
            log.info("{}: " + OBJECT_NAME + " (Id: {}) updated figure list and total price throughout create method", LogEnum.SERVICE, savedCart.getId());
            return savedCart;
        }   else {
            throw new CustomNotFoundException(OBJECT_NAME, cartId);
        }
    }

    @Override
    public CartDto update(String id, CartDto cartDto) {
        CartEntity cart = findById(id);
        List<FigureEntity> figureList = cartDto.figures()
                .stream()
                .map(figure -> figureService.findById(figure.getId()))
                .toList();
        int totalPrice = figureList.stream().mapToInt(FigureEntity::getCurrentPrice).sum();

        cart.setFigures(figureList);
        cart.setPrice(totalPrice);

        CartEntity updatedCart = cartRepository.save(cart);
        log.info("{}: " + OBJECT_NAME + " (Id: {}) updated figure list and total price throughout update method", LogEnum.SERVICE, id);
        return cartMapper.toDto(updatedCart);
    }

    @Override
    public boolean delete(String id) {
        CartEntity cart = findById(id);
        cartRepository.delete(cart);
        log.info("{}: " + OBJECT_NAME + " (id: {}) deleted", LogEnum.SERVICE, id);
        return true;
    }

    private CartEntity findById(String id) {
        return cartRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }
}