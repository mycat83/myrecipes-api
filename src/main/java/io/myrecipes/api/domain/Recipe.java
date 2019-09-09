package io.myrecipes.api.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
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

        public Recipe() {
        }

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
