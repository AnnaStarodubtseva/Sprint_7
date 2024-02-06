public class Order {
    private String firstname;
    private String lastname;
    private String address;
    private int metrostation;
    private String phone;
    private int renttime;
    private String deliverydate;
    private String comment;
    private String[] color;

    public Order(String firstname, String lastname, String address, int metrostation, String phone, int renttime, String deliverydate, String comment, String[] color) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.metrostation = metrostation;
        this.phone = phone;
        this.renttime = renttime;
        this.deliverydate = deliverydate;
        this.comment = comment;
        this.color = color;
    }
    public Order() {}

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMetrostation() {
        return metrostation;
    }

    public void setMetrostation(int metrostation) {
        this.metrostation = metrostation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRenttime() {
        return renttime;
    }

    public void setRenttime(int renttime) {
        this.renttime = renttime;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }
}
