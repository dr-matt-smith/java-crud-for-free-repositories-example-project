package tudublin;

public class Product
{
    public int id;
    public String description;
    public double price;

    @Override
    public String toString()
    {
        return "id = " + this.id + " / description = " + this.description + " / price = " + this.price;
    }
}
