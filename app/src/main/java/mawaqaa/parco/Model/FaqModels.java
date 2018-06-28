package mawaqaa.parco.Model;

/**
 * Created by HP on 4/10/2018.
 */

public class FaqModels {

    String faqId;
    String faqQuestion;
    String faqAnswer;

    public FaqModels(String faqId, String faqQuestion, String faqAnswer) {
        this.faqId = faqId;
        this.faqQuestion = faqQuestion;
        this.faqAnswer = faqAnswer;
    }

    public String getFaqId() {
        return faqId;
    }

    public void setFaqId(String faqId) {
        this.faqId = faqId;
    }

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public void setFaqQuestion(String faqQuestion) {
        this.faqQuestion = faqQuestion;
    }

    public String getFaqAnswer() {
        return faqAnswer;
    }

    public void setFaqAnswer(String faqAnswer) {
        this.faqAnswer = faqAnswer;
    }
}
