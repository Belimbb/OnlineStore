package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.dto.request.CartRequestDto;
import com.teamChallenge.dto.response.CartResponseDto;
import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<CartResponseDto> getAll() {
        List<CartEntity> cartList = cartRepository.findAll();
        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        return cartMapper.toDtoList(cartList);
    }

    @Override
    public CartResponseDto getById(String id) {
        CartEntity cart = findById(id);
        log.info("{}: " + OBJECT_NAME + " retrieved from db by id {}", LogEnum.SERVICE, id);
        return cartMapper.toResponseDto(cart);
    }

    @Override
    public CartResponseDto create(UserEntity user, List<FigureEntity> figureList) {
//        List<FigureEntity> figureList = cartDto.figures()
//                .stream()
//                .map(figure -> figureService.findById(figure.getId()))
//                .toList();
        if (cartRepository.existsByUser(user)) {
            CartEntity cart = cartRepository.findByUser(user);
            return cartMapper.toResponseDto(addFigures(cart.getId(), figureList));
        }

        int totalPrice = figureList.stream().mapToInt(FigureEntity::getCurrentPrice).sum();
        CartEntity newCart = new CartEntity(user, totalPrice, figureList);
        CartEntity savedCart = cartRepository.save(newCart);
        log.info("{}: " + OBJECT_NAME + " (Id: {}) was created", LogEnum.SERVICE, savedCart.getId());
        return cartMapper.toResponseDto(savedCart);
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
    public CartResponseDto update(String id, CartRequestDto cartDto) {
        CartEntity cart = findById(id);
        List<FigureEntity> figureList = cartDto.figures()
                .stream()
                .map(figure -> figureService.findById(figure.id()))
                .toList();
        int totalPrice = figureList.stream().mapToInt(FigureEntity::getCurrentPrice).sum();

        cart.setFigures(figureList);
        cart.setPrice(totalPrice);

        CartEntity updatedCart = cartRepository.save(cart);
        log.info("{}: " + OBJECT_NAME + " (Id: {}) updated figure list and total price throughout update method", LogEnum.SERVICE, id);
        return cartMapper.toResponseDto(updatedCart);
    }

    @Override
    public void delete(String id) {
        CartEntity cart = findById(id);
        cartRepository.delete(cart);
        log.info("{}: " + OBJECT_NAME + " (id: {}) deleted", LogEnum.SERVICE, id);
    }

    private CartEntity findById(String id) {
        return cartRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }
}