package com.ftn.slagalica.data.model.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WhoKnowsQuestionsDTO extends ArrayList<HashMap<String, String[]>> {

    public WhoKnowsQuestionsDTO(){
//        Todo remove this Test Data when Database is implemented

        HashMap<String, String[]> questionAnswersPairs1 = new HashMap<>();
        questionAnswersPairs1.put("Koje godine je po\u010deo Prvi svetski rat?", new String[]{"1918.", "1890.", "1914.", "1963.", "1914."});
        this.add(questionAnswersPairs1);

        HashMap<String, String[]> questionAnswersPairs2 = new HashMap<>();
        questionAnswersPairs2.put("Koji je ko\u0161arka\u0161ki tim NBA lige iz Oklahome?", new String[]{"Bulls", "Thunder", "Jazz", "Timberwolves", "Thunder"});
        this.add(questionAnswersPairs2);

        HashMap<String, String[]> questionAnswersPairs3 = new HashMap<>();
        questionAnswersPairs3.put("Kako se naziva 3. Njutnov zakon?", new String[]{"Zakon sile", "Zakon inercije", "Njutn-Lajbnicov zakon", "Zakon akcije i reakcije", "Zakon akcije i reakcije"});
        this.add(questionAnswersPairs3);

        HashMap<String, String[]> questionAnswersPairs4 = new HashMap<>();
        questionAnswersPairs4.put("Sezonska promena perja kod ptica se naziva?", new String[]{"Linjanje", "Perutanje", "Mitarenje", "Timarenje", "Mitarenje"});
        this.add(questionAnswersPairs4);

        HashMap<String, String[]> questionAnswersPairs5 = new HashMap<>();
        questionAnswersPairs5.put("Najmanja jedinica podatka u ra\u010dunarstvu je?", new String[]{"Bit", "Bajt", "Boolean", "Gigabajt", "Bit"});
        this.add(questionAnswersPairs5);
    }
}
