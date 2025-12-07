package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.AssessmentResult;

public class AssessmentService {

    private static final int[] DEP = {2,4,9,12,15,16,20};
    private static final int[] ANX = {1,3,6,8,14,18,19};
    private static final int[] STR = {0,5,7,10,11,13,17};

    public static AssessmentResult evaluate(int[] answers) {

        int dep = score(answers, DEP);
        int anx = score(answers, ANX);
        int str = score(answers, STR);

        return new AssessmentResult(dep, anx, str);
    }

    private static int score(int[] answers, int[] map) {
        int sum = 0;
        for (int i : map) sum += answers[i];
        return sum * 2;
    }
}