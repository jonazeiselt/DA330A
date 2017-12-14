package model;

/**
 * Represents a customer to access swimming pool.
 * Created by Jonas Eiselt on 2017-12-01.
 */
public class Customer
{
    private boolean vip;
    private String name;
    private int age;

    public Customer(boolean vip)
    {
        this.vip = vip;
    }

    public boolean isVip()
    {
        return vip;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    @Override
    public String toString()
    {
        return name + ", " + age + " yrs" + (vip ? " (VIP)" : "");
    }
}
