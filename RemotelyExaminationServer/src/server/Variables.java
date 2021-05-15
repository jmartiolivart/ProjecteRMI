package server;

import java.util.List;

public final class Variables {

    private static List<String> examQuestions;

    public static List<String> getExamQuestions(){
        return examQuestions;
    }

    public static void setExamQuestions( List<String> examQuestions){
        Variables.examQuestions = examQuestions;
    }
}