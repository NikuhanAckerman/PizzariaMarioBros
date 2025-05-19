package pwaula.trabalho.pizzariamario.s;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import pwaula.trabalho.pizzariamario.s.model.PizzaEntity;
import pwaula.trabalho.pizzariamario.s.repository.PizzaRepository;

import java.lang.reflect.Field;
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
    private PizzaRepository pizzaRepository;

    @Override
    public void run(String... args) throws Exception {

        if (pizzaRepository.count() == 0) {
            System.out.println("Populando tabela de pizzas...");

            ArrayList<PizzaEntity> pizzaList = new ArrayList<>();

            Collections.addAll(pizzaList,
                    new PizzaEntity(
                            "Pizza Margherita",
                            "Clássica italiana com sabor suave e autêntico.",
                            BigDecimal.valueOf(38.90),
                            "https://img.freepik.com/free-psd/top-view-delicious-pizza_23-2151868922.jpg",
                            "Molho de tomate, mussarela de búfala, manjericão fresco, azeite de oliva."),
                    new PizzaEntity(
                            "Pizza Calabresa",
                            "Uma das favoritas do Brasil, simples e deliciosa.",
                            BigDecimal.valueOf(42),
                            "https://img.freepik.com/free-photo/thinly-sliced-pepperoni-is-popular-pizza-topping-american-style-pizzerias-isolated-white-background-still-life_639032-229.jpg",
                            "Calabresa fatiada, cebola roxa, mussarela, orégano."),
                    new PizzaEntity(
                            "Pizza Quatro Queijos",
                            "Cremosa e rica em sabores para os amantes de queijo.",
                            BigDecimal.valueOf(46),
                            "https://sgnh.com.br/wp-content/uploads/2021/07/pizza_quatro.png",
                            "Mussarela, provolone, gorgonzola, parmesão."),
                    new PizzaEntity(
                            "Pizza Portuguesa",
                            "Uma explosão de sabores variados.",
                            BigDecimal.valueOf(44.90),
                            "https://sgnh.com.br/wp-content/uploads/2021/07/pizza_portuguesa.png",
                            "Presunto, mussarela, ovo cozido, cebola, azeitonas, pimentão."),
                    new PizzaEntity(
                            "Pizza de Frango com Catupiry",
                            "Cremosa, leve e muito popular.",
                            BigDecimal.valueOf(45),
                            "https://sgnh.com.br/wp-content/uploads/2021/07/pizza_frango.png",
                            "Frango desfiado, catupiry, mussarela, milho, orégano."),
                    new PizzaEntity(
                            "Pizza Doce de Banana com Canela",
                            "Perfeita como sobremesa.",
                            BigDecimal.valueOf(36),
                            "https://www.cosmopolitapizza.com.br/wp-content/uploads/2021/03/BANANA.png",
                            "Banana, açúcar, canela, leite condensado.")
            );

            pizzaRepository.saveAll(pizzaList);

            System.out.println("Tabela de pizzas populada.");
        }

    }
}
