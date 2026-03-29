public class Card {
    //defining data fields
    private String id;
    private int Ain;
    private int revivalProgress;
    private int Abase;
    private int Acur;
    private int Hin;
    private int Hbase;
    private int Hcur;
    Node belongingNode;

    //writing the constructor
    public Card(String id, int Ain, int Hin) {
        this.id = id;
        this.Ain = Ain;
        this.Abase = Ain;
        this.Acur = Ain;
        this.Hin = Hin;
        this.Hbase = Hin;
        this.Hcur = Hin;
        this.revivalProgress=Hbase;
    }
    public Card() {

    }
    //writing the getter functions
    public String getId() {
        return id;
    }

    public int getAin() {
        return Ain;
    }

    public int getAbase() {
        return Abase;
    }

    public int getAcur() {
        return Acur;
    }
    public int getRevivalProgress() {
        return revivalProgress;
    }

    public int getHin() {
        return Hin;
    }

    public int getHbase() {
        return Hbase;
    }

    public int getHcur() {
        return Hcur;
    }

    //writing setter functions
    public void setHcur(int Hcur) {
        this.Hcur = Hcur;
    }

    public void setAcur(int Acur) {
        this.Acur = Acur;
    }

    public void setHbase(int Hbase) {
        this.Hbase = Hbase;
    }

    public void setAbase(int Abase) {
        this.Abase = Abase;
    }

    public void setRevivalProgress(int revivalProgress) {
        this.revivalProgress = revivalProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.id.equals(card.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
