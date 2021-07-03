package com.github.at6ue.jersey.injection;

import java.util.List;
import java.util.Random;

public class IdentityService {
    private static final List<String> names = List.of("Carol", "Charlie", "Dave", "Ellen", "Frank", "Eve", "Isaac",
            "Ivan", "Justin", "Mallory", "Marvin", "Mallet", "Matilda", "Oscar", "Pat", "Peggy", "Victor", "Plod",
            "Steve", "Trent", "Trudy", "Walter", "Zoe");

    private final String name;

    // The constructor of an injectee must be public
    public IdentityService() {
        name = names.get(new Random().nextInt(names.size()));
    }

    public String getName() {
        return name;
    }
}
