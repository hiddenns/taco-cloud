package sia.tacocloud.repository;

import sia.tacocloud.Ingredient;

public interface IngredientRepository {
    Iterable<Ingredient> findAll();

    Ingredient findIngredientById(String id);

    Ingredient save(Ingredient ingredient);
}
