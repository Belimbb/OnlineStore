package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.dto.request.CartRequestDto;
import com.teamChallenge.dto.response.CartResponseDto;
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
import java.util.Map;

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
    public CartResponseDto create(CartRequestDto cartDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userService.findByEmail(email);

        Map<String, Integer> figureMap = cartDto.figureIdAndAmountMap();

        for (Map.Entry<String, Integer> entry : figureMap.entrySet()) {
            if (!figureService.existById(entry.getKey())) {
                figureMap.remove(entry.getKey());
            }
        }

        if (cartRepository.existsByUser(currentUser)) {
            CartEntity cart = cartRepository.findByUser(currentUser);
            return cartMapper.toResponseDto(addFigures(cart.getId(), figureMap));
        }

        int totalPrice = figureMap.keySet().stream().mapToInt(key -> figureService.findById(key).getCurrentPrice() * figureMap.get(key)).sum();
        CartEntity newCart = new CartEntity(currentUser, totalPrice, figureMap);
        CartEntity savedCart = cartRepository.save(newCart);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was created", LogEnum.SERVICE, savedCart.getId());
        return cartMapper.toResponseDto(savedCart);
    }

    private CartEntity addFigures(String cartId, Map<String, Integer> map) {
        if (cartRepository.existsById(cartId)) {
            CartEntity cart = findById(cartId);
            Map<String, Integer> figureIdAndAmountMap = cart.getFigureIdAndAmountMap();

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                if (figureIdAndAmountMap.containsKey(key)) {
                    int existAmount = entry.getValue();
                    figureIdAndAmountMap.replace(key, existAmount + figureIdAndAmountMap.get(key));
                    map.remove(key);
                }
            }

            figureIdAndAmountMap.putAll(map);
            int totalPrice = figureIdAndAmountMap.keySet().stream().mapToInt(key -> figureService.findById(key).getCurrentPrice() * figureIdAndAmountMap.get(key)).sum();
            cart.setPrice(totalPrice);

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
        Map<String, Integer> figureIdAndAmountMap = cartDto.figureIdAndAmountMap();

        for (Map.Entry<String, Integer> entry : figureIdAndAmountMap.entrySet()) {
            if (!figureService.existById(entry.getKey())) {
                figureIdAndAmountMap.remove(entry.getKey());
            }
        }

        cart.setFigureIdAndAmountMap(figureIdAndAmountMap);
        int totalPrice = figureIdAndAmountMap.keySet().stream().mapToInt(key -> figureService.findById(key).getCurrentPrice() * figureIdAndAmountMap.get(key)).sum();
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