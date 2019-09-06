package io.myrecipes.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "value=test",
                "property.value=propertyTest"
        },
        classes = {MyRecipesApiApplication.class}
)
public class MyRecipesApiApplicationTests {

    @Autowired
    DefaultListableBeanFactory beanFactory;

    @Value("${value}")
    private String value;

    @Value("${property.value}")
    private String propertyValue;

    @Test
    public void contextLoads() {
        assertThat(value, is("test"));
        assertThat(propertyValue, is("propertyTest"));
    }

    @Test
    public void beans() {
        for (String bean: beanFactory.getBeanDefinitionNames()) {
            System.out.println(bean + " \t" + beanFactory.getBean(bean).getClass().getName());
        }
    }

}
