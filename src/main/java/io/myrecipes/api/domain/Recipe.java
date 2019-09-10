package io.myrecipes.api.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {
        @Id
        @GeneratedValue
        private Integer id;

        private String title;

        private String image;

        private Integer estimatedTime;

        private String difficulty;

        private Integer registerUserId;

        @CreationTimestamp
        private Timestamp registerDate;

        private Integer modifyUserId;

        @UpdateTimestamp
        private Timestamp modifyDate;

        @OneToMany(mappedBy = "recipe")
        private List<RecipeMaterial> recipeMaterialList = new ArrayList<>();

        @OneToMany(mappedBy = "recipe")
        private List<RecipeStep> recipeStepList = new ArrayList<>();

        @OneToMany(mappedBy = "recipe")
        private List<RecipeTag> recipeTagList = new ArrayList<>();

        public Recipe(String title, String image, Integer estimatedTime, String difficulty) {
                this.title = title;
                this.image = image;
                this.estimatedTime = estimatedTime;
                this.difficulty = difficulty;
        }

        public void update(Recipe recipe) {
                this.title = recipe.getTitle();
                this.image = recipe.getImage();
                this.estimatedTime = recipe.getEstimatedTime();
                this.difficulty = recipe.getDifficulty();
        }
}
