package team.paradise.pbookkeeper;

public class BillItem {
    private String name;
    private String comment;
    private int number;
    private int price;
    private int total;

    public BillItem() {

    }

    public BillItem(String name, String comment, int number, int price, int total) {
        this.name = name;
        this.comment = comment;
        this.number = number;
        this.price = price;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
