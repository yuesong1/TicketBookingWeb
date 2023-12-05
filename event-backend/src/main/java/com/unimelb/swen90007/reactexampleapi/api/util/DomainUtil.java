package com.unimelb.swen90007.reactexampleapi.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimelb.swen90007.reactexampleapi.api.objects.Section;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.util.*;

public class DomainUtil {
    public static Map<String, String> parseParams(HttpServletRequest request) throws Exception {
        Map<String, String> params = new HashMap<>();

        StringBuilder reqSB = new StringBuilder();

        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) reqSB.append(line);
        }

//        System.out.println("Received payload: " + reqSB);

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> objParams = mapper.readValue(reqSB.toString(), typeRef);

        objParams.forEach((key, value) ->
                params.put(key.toLowerCase(), value.toString().replace('=', ':')));

        return params;
    }

    public static <T> List<T>[] compareListsNoPK(List<T> requested, List<T> original) {
        System.out.println("requested = " + requested);
        System.out.println("original = " + original);


        List<T>[] sectDeleteAdd =
                new ArrayList[]{new ArrayList<T>(), new ArrayList<T>(), new ArrayList<T>()};
        HashMap<String, T> originalMap = new HashMap<>();
        for (T t: original) originalMap.put(t.toString(), t);
        for (T t: requested) {
            if (originalMap.get(t.toString()) == null) sectDeleteAdd[1].add(t);
            else originalMap.remove(t.toString());
        }
        for (T t: originalMap.values()) sectDeleteAdd[0].add(t);

        System.out.println("delete = " + sectDeleteAdd[0]);
        System.out.println("create = " + sectDeleteAdd[1]);

        return sectDeleteAdd;
    }

////---------------------------------------------debug
    public static List<Section>[] compareListsOnePKl(List<Section> sections, List<Section> sections1) {
        return null;
    }
}
