package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.dto.request.CartRequestDto;
import com.teamChallenge.dto.response.CartResponseDto;
import com.teamChallenge.dto.response.figure.FigureInCartOrderResponseDto;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.promoCode.PromoCodeServiceImpl;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomBadRequestException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    private final UserServiceImpl userService;
    private final FigureServiceImpl figureService;
    private final PromoCodeServiceImpl promoCodeService;

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
    public CartResponseDto create(CartRequestDto cartDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userService.findByEmail(email);

        List<FigureInCartOrderResponseDto> figures = figureService.getCartOrderResponseFigures(cartDto.figures());
        //figures.removeIf(elem -> !figureService.existById(elem.figureId())); - This variant isn't working (IDK why...)
        for (FigureInCartOrderResponseDto elem : figures){
            if (!figureService.existById(elem.figureId())){
                figures.remove(elem);
            }
        }

        if (figures.isEmpty()) {
            throw new CustomBadRequestException("This request is not valid");
        }

        if (cartRepository.existsByUser(currentUser)) {
            CartEntity cart = cartRepository.findByUser(currentUser);
            return cartMapper.toResponseDto(addFigures(cart.getId(), figures));
        }

        CartEntity newCart = new CartEntity(currentUser, figures, getDiscount(cartDto.promoCode()));
        CartEntity savedCart = cartRepository.save(newCart);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was created", LogEnum.SERVICE, savedCart.getId());
        return cartMapper.toResponseDto(savedCart);
    }

    @Override
    public CartResponseDto update(String id, CartRequestDto cartDto) {
        CartEntity cart = findById(id);
        List<FigureInCartOrderResponseDto> figures = figureService.getCartOrderResponseFigures(cartDto.figures());

        if (figures.isEmpty()) {
            throw new CustomBadRequestException("This request is not valid");
        }

        cart.setFigures(figures);
        cart.setTotalPrice(getDiscount(cartDto.promoCode()));

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

    private int getDiscount(String promoCode){
        int discount = 0;
        if (promoCode==null||promoCode.isBlank()){
            discount = promoCodeService.getByCode(promoCode).discount();
        }

        return discount;
    }


    private CartEntity addFigures(String cartId, List<FigureInCartOrderResponseDto> figures) {
        if (cartRepository.existsById(cartId)) {
            CartEntity cart = findById(cartId);
            List<FigureInCartOrderResponseDto> figuresInCart = new ArrayList<>(cart.getFigures());

            Map<String, FigureInCartOrderResponseDto> figureMap = figuresInCart.stream()
                    .collect(Collectors.toMap(FigureInCartOrderResponseDto::figureId, Function.identity()));

            figures.forEach(elem -> figureMap.put(elem.figureId(), elem));

            figuresInCart.clear();
            figuresInCart.addAll(figureMap.values());

            cart.setFigures(figuresInCart);

            CartEntity savedCart = cartRepository.save(cart);
            log.info("{}: " + OBJECT_NAME + " (Id: {}) updated figure list and total price throughout create method", LogEnum.SERVICE, savedCart.getId());
            return savedCart;
        }   else {
            throw new CustomNotFoundException(OBJECT_NAME, cartId);
        }
    }
}