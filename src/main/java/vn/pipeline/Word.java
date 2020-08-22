package vn.pipeline;

public class Word {

    private String form;
    private String posTag;

    public Word(Word word) {
        this.form = word.form;
        this.posTag = word.posTag;
    }

    public Word(String form, String posTag) {
        this.form = form;
        this.posTag = posTag;
    }

    public Word(String form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return this.getForm() + "\t" +
                (this.getPosTag() == null?"_": this.getPosTag());
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getPosTag() {
        return posTag;
    }

    public void setPosTag(String pos) {
        this.posTag = pos;
    }

}
