package com.fedo;

import com.fedo.theme.GUIProperties;
import com.fedo.theme.ThemeSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*
        ArrayList<String> themes = new ArrayList<>();
        try {
            Field[] declaredFields = GUIProperties.class.getDeclaredFields();
            List<Field> staticFields = new ArrayList<>();
            for (Field field : declaredFields)
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                    staticFields.add(field);
            System.out.println("--------------------");
            for (int i = 2; i < staticFields.size(); i++) {
                String theme = (String) staticFields.get(i).get(null);
                System.out.println(theme);
                themes.add(theme);
            }
            System.out.println("--------------------");
        } catch (IllegalAccessException ignored) {
        }

        for (String theme : themes) {
            ThemeSetter.apply(theme);
            new Randomizer(theme);
        }
        if (1 == 1)
            return;
        */
        ThemeSetter.apply(3);
        new Randomizer("Randomizer");
    }
}
