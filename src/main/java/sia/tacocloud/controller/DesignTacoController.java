package sia.tacocloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sia.tacocloud.Ingredient;
import sia.tacocloud.Taco;
import sia.tacocloud.repository.IngredientRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }


    @ModelAttribute
    public void addIngredientsToModel(Model model){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(i -> ingredients.add(i));

//                Arrays.asList(
//                        new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
//                        new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
//                        new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
//                        new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
//                        new Ingredient("TMTO", "Deced Tomatoes", Ingredient.Type.VEGGIES),
//                        new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
//                        new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
//                        new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
//                        new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
//                        new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
//                );

        Ingredient.Type[] types = Ingredient.Type.values();

        for (Ingredient.Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    @GetMapping()
    public String showDesignForm(Model model) {
        model.addAttribute("design", new Taco());
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid @ModelAttribute("design") Taco design, Errors errors) {
        if (errors.hasErrors()) {
            return "design";
        }

        log.info("Processing design :" + design);
        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

}
