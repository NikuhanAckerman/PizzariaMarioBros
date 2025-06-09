package pwaula.trabalho.pizzariamario.s;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import pwaula.trabalho.pizzariamario.s.model.*;
import pwaula.trabalho.pizzariamario.s.repository.CartRepository;
import pwaula.trabalho.pizzariamario.s.repository.ClientRepository;
import pwaula.trabalho.pizzariamario.s.repository.UserRepository;
import pwaula.trabalho.pizzariamario.s.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class AppInitializer implements CommandLineRunner {

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (productRepository.count() == 0) {
            System.out.println("Populando tabela de produtos...");

            ArrayList<ProductEntity> productList = new ArrayList<>();

            Collections.addAll(productList,
                    new ProductEntity(
                            "Pizza Margherita",
                            "Clássica italiana com sabor suave e autêntico.",
                            BigDecimal.valueOf(38.90),
                            "https://img.freepik.com/free-psd/top-view-delicious-pizza_23-2151868922.jpg",
                            ProductCategory.PIZZAS,
                            true),
                    new ProductEntity(
                            "Pizza Calabresa",
                            "Uma das favoritas do Brasil, simples e deliciosa.",
                            BigDecimal.valueOf(42),
                            "https://img.freepik.com/free-photo/thinly-sliced-pepperoni-is-popular-pizza-topping-american-style-pizzerias-isolated-white-background-still-life_639032-229.jpg",
                            ProductCategory.PIZZAS,
                            true),
                    new ProductEntity(
                            "Pizza Quatro Queijos",
                            "Cremosa e rica em sabores para os amantes de queijo.",
                            BigDecimal.valueOf(46),
                            "https://sgnh.com.br/wp-content/uploads/2021/07/pizza_quatro.png",
                            ProductCategory.PIZZAS,
                            true),
                    new ProductEntity(
                            "Pizza Portuguesa",
                            "Uma explosão de sabores variados.",
                            BigDecimal.valueOf(44.90),
                            "https://sgnh.com.br/wp-content/uploads/2021/07/pizza_portuguesa.png",
                            ProductCategory.PIZZAS,
                            true),
                    new ProductEntity(
                            "Pizza de Frango com Catupiry",
                            "Cremosa, leve e muito popular.",
                            BigDecimal.valueOf(45),
                            "https://sgnh.com.br/wp-content/uploads/2021/07/pizza_frango.png",
                            ProductCategory.PIZZAS,
                            true),
                    new ProductEntity(
                            "Pizza Doce de Banana com Canela",
                            "Perfeita como sobremesa.",
                            BigDecimal.valueOf(36),
                            "https://www.cosmopolitapizza.com.br/wp-content/uploads/2021/03/BANANA.png",
                            ProductCategory.PIZZAS,
                            true),
                    new ProductEntity(
                            "Esfiha de Queijo",
                            "Cremosa e derretida, com muito queijo em cada mordida.",
                            BigDecimal.valueOf(3.99),
                            "https://static.expressodelivery.com.br/imagens/produtos/59849/180/Expresso-Delivery_cc1430e6efe0ff8b91f33745cc0217c4.png",
                            ProductCategory.ESFIHAS,
                            true),
                    new ProductEntity(
                            "Esfiha de Carne",
                            "Tradicional e suculenta, com carne moída temperada no ponto certo.",
                            BigDecimal.valueOf(2.99),
                            "https://static.expressodelivery.com.br/imagens/produtos/59839/180/Expresso-Delivery_83f0925959b238790b062d264543b39f.png",
                            ProductCategory.ESFIHAS,
                            true),
                    new ProductEntity(
                            "Esfiha de Frango",
                            "Frango desfiado e bem temperado, uma delícia macia e saborosa.",
                            BigDecimal.valueOf(2.99),
                            "https://static.expressodelivery.com.br/imagens/produtos/59852/180/Expresso-Delivery_62d7222f0a847c7416c6d521b4a7070c.png",
                            ProductCategory.ESFIHAS,
                            true),
                    new ProductEntity(
                            "Esfiha de Calabresa",
                            "Calabresa picadinha com um toque especial de cebola, cheia de sabor.",
                            BigDecimal.valueOf(2.99),
                            "https://static.expressodelivery.com.br/imagens/produtos/229766/180/Expresso-Delivery_c23ded8af22384ef3ae130b695c9a670.png",
                            ProductCategory.ESFIHAS,
                            true),
                    new ProductEntity(
                            "Coca-Cola Lata 350ml",
                            "Refrescante e clássica, a escolha perfeita para qualquer momento",
                            BigDecimal.valueOf(6.50),
                            "https://hiperideal.vtexassets.com/arquivos/ids/225913/7894900010015_Coca-Cola-Original-350ML_1.png?v=638648768383600000",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Pepsi Lata 350ml",
                            "Sabor marcante e cheio de atitude, ideal para acompanhar sua refeição.",
                            BigDecimal.valueOf(6.25),
                            "https://dmvfarma.vtexassets.com/arquivos/ids/244197/Refrigerante-Pepsi-Cola-Lata-350Ml---Pepsi.jpg?v=638561401611700000",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Guaraná Lata 350ml",
                            "O sabor brasileiro que todo mundo ama, leve e revigorante.",
                            BigDecimal.valueOf(6.18),
                            "https://brf.file.force.com/servlet/servlet.ImageServer?id=015U600000025zh&oid=00D410000012TJa&lastMod=1703691077000",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Itubaína Lata 350ml",
                            "Retrô e deliciosa, com aquele gostinho nostálgico irresistível.",
                            BigDecimal.valueOf(6.36),
                            "https://io.convertiez.com.br/m/superpaguemenos/shop/products/images/47146/medium/refrigerante-itubaina-retro-tutti-frutti-350ml_102894.png",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Fanta Uva Lata 350ml",
                            "Doce, intensa e cheia de personalidade.",
                            BigDecimal.valueOf(6.28),
                            "https://images.tcdn.com.br/img/img_prod/1115696/fanta_uva_lata_350ml_6_und_49_1_8ddc4c976e574a4678513f12366effb6.jpg",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Fanta Limão Lata 350ml",
                            "Refrescância cítrica com um toque levemente azedinho.",
                            BigDecimal.valueOf(6.28),
                            "https://acdn-us.mitiendanube.com/stores/001/218/477/products/fanta-lemon-3-df70824218bc7e035a17026589190189-640-0.jpg",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Fanta Maracujá Lata 350ml",
                            "Exótica e suave, com o sabor tropical do maracujá.",
                            BigDecimal.valueOf(6.28),
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTrJqwgjs998QmWB1OaZUckbnJAicmcRXU7mg&s",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Fanta Guaraná Lata 350ml",
                            "Uma explosão de energia e sabor com toque brasileiro.",
                            BigDecimal.valueOf(6.28),
                            "https://images.tcdn.com.br/img/img_prod/1115696/fanta_guarana_lata_350ml_6_und_391_1_70c02724245f3df7b77ebb914092fc99.jpg",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Fanta Caju Lata 350ml",
                            "Diferente e surpreendente, com o gostinho do Nordeste.",
                            BigDecimal.valueOf(6.28),
                            "https://www.extraplus.com.br/uploads/produtos/original/199261_extrabom_refrigerante_refrigerante-fanta-caju-lata-350ml.jpg",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Suco Natural Abacaxi 350ml",
                            "Refrescante e levemente ácido, feito com fruta de verdade.",
                            BigDecimal.valueOf(7.80),
                            "https://www.casadotambaquipvh.com.br/Imagens/14193e4a9cf54e65b473a8db3c668b20.jpeg",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Suco Natural Maracujá 350ml",
                            "Sabor suave e natural, perfeito para relaxar.",
                            BigDecimal.valueOf(7.80),
                            "https://pedidos.nacanoa.com.br/wp-content/uploads/2020/09/Suco-Maracuja.png",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Suco Natural Laranja 350ml",
                            "Cítrico e nutritivo, o clássico suco natural cheio de frescor.",
                            BigDecimal.valueOf(7.80),
                            "https://alloydeliveryimages.s3.sa-east-1.amazonaws.com/item_images/623b1fa288705.webp",
                            ProductCategory.BEBIDAS,
                            true),
                    new ProductEntity(
                            "Batatas Fritas 120g",
                            "Crocantes por fora, macias por dentro e sempre irresistíveis.",
                            BigDecimal.valueOf(13.50),
                            "https://swiftbr.vteximg.com.br/arquivos/rnk-seo-batata-frita-1.png?v=638152626771070000",
                            ProductCategory.ADICIONAIS,
                            true),
                    new ProductEntity(
                            "Nuggets",
                            "Crocantes e douradinhos, sucesso com adultos e crianças.",
                            BigDecimal.valueOf(17.50),
                            "https://www.falkaolanches.com.br/wp-content/uploads/2022/05/2-2.png",
                            ProductCategory.ADICIONAIS,
                            true)
                    );

            productRepository.saveAll(productList);

            System.out.println("Tabela de produtos populada.");
        }

        if (userRepository.count() == 0) {
            System.out.println("Populando tabela de usuários e clientes...");

            List<UserEntity> userList = new ArrayList<>();
            List<ClientEntity> clientList = new ArrayList<>();

            UserEntity user1 = new UserEntity();
            user1.setEmail("mario@example.com");
            user1.setPassword(passwordEncoder.encode("123"));
            user1.setRoles("ADMIN");

            UserEntity user2 = new UserEntity();
            user2.setEmail("luigi@example.com");
            user2.setPassword(passwordEncoder.encode("123"));
            user2.setRoles("ADMIN");

            UserEntity user3 = new UserEntity();
            user3.setEmail("roberto@example.com");
            user3.setPassword(passwordEncoder.encode("123"));
            user3.setRoles("ATENDENTE");

            UserEntity user4 = new UserEntity();
            user4.setEmail("juan@example.com");
            user4.setPassword(passwordEncoder.encode("123"));
            user4.setRoles("USER");
            userRepository.save(user4);

            ClientEntity client4 = new ClientEntity();
            client4.setUserId(user4.getId());
            client4.setName("Juan Bros");
            client4.setEmail("juan@example.com");
            client4.setPhone("11999999999");
            client4.setAddress("Rua do Cogumelo, 123");
            client4.setCpf("123.456.789-00");
            clientRepository.save(client4);
            CartEntity cart2 = new CartEntity();
            cart2.setClientId(client4.getId());
            cartRepository.save(cart2);
            client4.setCartId(cart2.getId());

            userList.add(user1);
            userList.add(user2);
            userList.add(user3);
            userList.add(user4);

            clientList.add(client4);

            userRepository.saveAll(userList);
            clientRepository.saveAll(clientList);

            System.out.println("Tabela de usuários e clientes populada.");
        }

    }
}
