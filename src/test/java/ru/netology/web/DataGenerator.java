package ru.netology.web;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class DataGenerator {
    static {
        new Faker(new Locale("ru"));
    }

    private DataGenerator() {
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser() {
            return new UserInfo(generateCity(), generateDate(3), generateName(), generatePhone());
        }

        public static String generateCity() {
            String[] cities = new String[]{"Санкт-Петербург", "Москва", "Воронеж", "Ярославль", "Кострома"};
            int itemIndex = (int) (Math.random() * cities.length);
            return cities[itemIndex];
        }

        public static String generateDate(int daysToAdd) {
            return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }

        public static String generateName() {
            Faker faker = new Faker(new Locale("ru"));
            AtomicReference<String> randomName = new AtomicReference<>(faker.name().firstName() + " " + faker.name().lastName());
            return randomName.get();
        }

        public static String generatePhone() {
            Faker faker = new Faker(new Locale("ru"));
            return faker.phoneNumber().phoneNumber();
        }
    }
}
